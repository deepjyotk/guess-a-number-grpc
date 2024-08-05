package org.example.service;

import io.grpc.stub.StreamObserver;

import org.example.generated.GuessNumberGrpc;
import org.example.generated.GuessRequest;
import org.example.generated.GuessResponse;
import org.example.requesthandlers.GuessRequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuessNumberService extends GuessNumberGrpc.GuessNumberImplBase {
    public static final Logger logger  = LoggerFactory.getLogger(GuessNumberService.class) ;


    @Override
    public io.grpc.stub.StreamObserver<GuessRequest> makeGuess(io.grpc.stub.StreamObserver<GuessResponse> responseObserver) {

        return new GuessRequestHandler(responseObserver) ;

    }
}
