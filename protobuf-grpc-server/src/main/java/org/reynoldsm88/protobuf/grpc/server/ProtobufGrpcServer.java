package org.reynoldsm88.protobuf.grpc.server;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ProtobufGrpcServer {

    // https://github.com/grpc/grpc-java/blob/master/examples/src/main/java/io/grpc/examples/helloworld/HelloWorldServer.java
    private static final Logger LOG = LoggerFactory.getLogger( ProtobufGrpcServer.class );

    private Server server;

    public void start() throws InterruptedException {
        int port = 63088;
        try {
            server = ServerBuilder.forPort( port ).addService( new GrpcProtobufMyService() ).build().start();
            LOG.info( "Server started on port " + port );

            Runtime.getRuntime().addShutdownHook( new Thread() {
                @Override
                public void run() {
                    // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                    LOG.warn( "*** shutting down gRPC server since JVM is shutting down" );
                    ProtobufGrpcServer.this.stop();
                    LOG.warn( "*** server shut down" );
                }
            } );

            blockUntilShutdown();
        } catch ( IOException e ) {
            LOG.error( "Error starting server " + e.getMessage() );
            e.printStackTrace();
        }
    }

    public void stop() {
        if ( server != null ) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if ( server != null ) {
            server.awaitTermination();
        }
    }

    public static void main( String[] args ) throws Exception {
        final ProtobufGrpcServer server = new ProtobufGrpcServer();
        server.start();
    }

}