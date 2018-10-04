package org.reynoldsm88.protobuf.grpc.server;

import org.reynoldsm88.protobuf.model.MyServiceGrpc.MyServiceImplBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieSession;
import org.reynoldsm88.protobuf.model.Output;
import org.reynoldsm88.protobuf.model.Request;
import org.reynoldsm88.protobuf.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.stub.StreamObserver;

public class GrpcProtobufMyService extends MyServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger( GrpcProtobufMyService.class );

    @Override
    public void execute( Request request, StreamObserver<Response> responseObserver ) {
        request.getInputList().forEach( i -> LOG.info( "Hello " + i.getName() + "!" ) );

        KieSession session = KieServices.Factory.get().newKieClasspathContainer().newKieSession( "protobuf-ksession" );
        request.getInputList().forEach( session::insert );
        session.fireAllRules();

        Output output = (Output) session.getObjects( new ClassObjectFilter( Output.class ) ).stream().findFirst().get();

        Response response = Response.newBuilder().setOutput( output ).build();
        responseObserver.onNext( response );
        responseObserver.onCompleted();
    }
}