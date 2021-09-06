package com.ashish.grpc.server;

import com.ashish.grpc.interceptor.HeaderServerInterceptor;
import com.ashish.grpc.service.PingPongService;
import com.ashish.grpc.service.PongPingService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class GrpcServer {
    private static final Logger logger = Logger.getLogger(GrpcServer.class.getName());
    @Value("${server.port}")
    private Integer port;
    private final Server server;

    public GrpcServer() {
        port = port ==null ? 9091 : port.intValue();
        server = ServerBuilder.forPort(port)
                .addService(ServerInterceptors.intercept(new PingPongService(),new HeaderServerInterceptor()))
                .addService(ServerInterceptors.intercept(new PongPingService(),new HeaderServerInterceptor()))
                .build();
    }

    public static void main(String[] args) throws Exception {
        GrpcServer server = new GrpcServer();
        server.start();
        server.blockUntilShutdown();
    }

    /* Start serving requests. */
    public void start() throws IOException {
        long startTime=System.currentTimeMillis();
        server.start();
        long serverStartTime=System.currentTimeMillis() - startTime;
        System.out.println("Server Start Time :"+serverStartTime);
        logger.info("Server started, listening on " + port);
        // Add shutDown hook to shut down gracefully
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use Sysout here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }


}
