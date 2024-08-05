package com.example.test;

import io.grpc.stub.StreamObserver;
import org.example.generated.GuessRequest;
import org.example.generated.GuessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.CountDownLatch;


public class GuessResponseHandler implements StreamObserver<GuessResponse>{

    public static final Logger log = LoggerFactory.getLogger(GuessResponseHandler.class) ;

    private final CountDownLatch latch = new CountDownLatch(1) ;

    private StreamObserver<GuessRequest> requestStreamObserver ;
    private int lower ;
    private  int upper;
    private int middle ;

    @Override
    public void onNext(GuessResponse guessResponse) {
//        log.info("attempt :{}", guessResponse.getAttempt());

        switch (guessResponse.getResult()){
            case LOW -> this.send(this.middle,this.upper) ;
            case HIGH -> this.send(this.lower, this.middle) ;
        }

    }

    @Override
    public void onError(Throwable throwable) {
        latch.countDown();
    }

    @Override
    public void onCompleted() {
        requestStreamObserver.onCompleted();
        latch.countDown();
    }
    private void send(int low , int high){
        this.lower= low ;
        this.upper = high ;
        this.middle = low+ (high-low)/2 ;
        log.info("client guess: {} ", this.middle);
        this.requestStreamObserver.onNext(GuessRequest.newBuilder().setGuess(this.middle).build());
    }

    public void start(){
        this.send(0,100) ;
    }

    public void await(){
        try {
            latch.await();
        }catch (InterruptedException e){
            throw new RuntimeException(e) ;
        }
    }

    public void setRequestStreamObserver(StreamObserver<GuessRequest> requestStreamObserver){
        this.requestStreamObserver = requestStreamObserver ;
    }
}
