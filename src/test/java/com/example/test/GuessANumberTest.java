package com.example.test;

import com.example.test.common.AbstractChannelTest;
import org.example.common.GrpcServer;
import org.example.generated.GuessNumberGrpc;
import org.example.service.GuessNumberService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GuessANumberTest extends AbstractChannelTest {

    private final GrpcServer grpcServer  = GrpcServer.create(new GuessNumberService());

    private GuessNumberGrpc.GuessNumberStub stub ;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        this.stub = GuessNumberGrpc.newStub(channel) ;
    }


    @Test
    public void guessANumberGame(){
        var responseObserver = new GuessResponseHandler() ;

        var requestObserver = this.stub.makeGuess(responseObserver) ;

        responseObserver.setRequestStreamObserver(requestObserver);
        responseObserver.start();
        responseObserver.await();
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }


}
