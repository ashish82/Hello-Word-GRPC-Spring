package com.ashish.grpc.service;

import com.ashish.grpc.exception.ServiceException;
import com.ashish.grpc.proto.generate.Ping;
import com.ashish.grpc.proto.generate.PingPongGrpc;
import com.ashish.grpc.proto.generate.Pong;
import io.grpc.Metadata;
import io.grpc.stub.StreamObserver;

import java.util.logging.Level;
import java.util.logging.Logger;

import static io.grpc.Status.FAILED_PRECONDITION;
import static io.grpc.Status.trailersFromThrowable;

public class PongPingService extends PingPongGrpc.PingPongImplBase {
    private static final Logger logger = Logger.getLogger(PongPingService.class.getName());

    @Override
    public void sendPing(Ping pingRequest, StreamObserver<Pong> pongResponseObserver){

        Pong pong= null;
        try {
            pong = createPongResponse(pingRequest);
        } catch (ServiceException e) {
            logger.log(Level.WARNING,"Error in PingPong Service");
            Metadata metadata  = trailersFromThrowable(e);
            pongResponseObserver.onError(FAILED_PRECONDITION.asRuntimeException(metadata));
        }
        pongResponseObserver.onNext(pong);
        pongResponseObserver.onCompleted();
    }

    private Pong createPongResponse(Ping pingRequest) throws ServiceException {
        Pong.Builder pongBuilder=Pong.newBuilder();
        String message = pingRequest.getMessage();
        float delayInSec = pingRequest.getDelaySeconds();
        if (message.contains("ashish")) {
            pongBuilder.setMessage("Hello " + message.toUpperCase() + "," + "How are you ?");
        } else {
            pongBuilder.setMessage("Hello " + message.toUpperCase() + "," + "Call you later");
        }
        return pongBuilder.build();
    }

}
