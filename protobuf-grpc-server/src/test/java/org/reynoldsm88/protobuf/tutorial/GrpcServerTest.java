package org.reynoldsm88.protobuf.tutorial;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.reynoldsm88.protobuf.grpc.server.GrpcProtobufMyService;
import org.reynoldsm88.protobuf.model.Input;
import org.reynoldsm88.protobuf.model.MyServiceGrpc;
import org.reynoldsm88.protobuf.model.Request;
import org.reynoldsm88.protobuf.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;

public class GrpcServerTest {

    private static final Logger LOG = LoggerFactory.getLogger( GrpcServerTest.class );

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    @Test
    public void serverShouldRespondWithCorrectOuptuForGivenInput() throws Exception {
        String serverName = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcCleanup.register( InProcessServerBuilder.forName( serverName ).directExecutor().addService( new GrpcProtobufMyService() ).build().start() );

        //@formatter:off
        MyServiceGrpc.MyServiceBlockingStub blockingStub = MyServiceGrpc.newBlockingStub( 
                                                                        grpcCleanup.register( 
                                                                                    InProcessChannelBuilder
                                                                                        .forName( serverName )
                                                                                        .directExecutor()
                                                                                        .build() ) );
        //@formatter:on

        Input i1 = Input.newBuilder().setName( "Michael" ).build();
        Input i2 = Input.newBuilder().setName( "Billy" ).build();
        Input i3 = Input.newBuilder().setName( "Matt" ).build();
        Input i4 = Input.newBuilder().setName( "Katie" ).build();
        Request request = Request.newBuilder().addAllInput( Arrays.asList( i1, i2, i3, i4 ) ).build();

        Response response = blockingStub.execute( request );

        LOG.info( "Response from server was : " + response );
        LOG.info( "Output from response : " + response.getOutput() );

        assertEquals( 4, response.getOutput().getCount() );

    }

}