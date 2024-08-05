package org.example.requesthandlers;

import io.grpc.stub.StreamObserver;
import org.example.generated.GuessRequest;
import org.example.generated.GuessResponse;
import org.example.generated.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class GuessRequestHandler implements StreamObserver<GuessRequest> {

    private static final Logger log = LoggerFactory.getLogger(GuessRequestHandler.class);
    private final StreamObserver<GuessResponse> guessResponseStreamObserver ;
    private final int secret ;
    private int attempt ;

    public GuessRequestHandler(StreamObserver<GuessResponse> guessResponseStreamObserver) {
        this.guessResponseStreamObserver = guessResponseStreamObserver;
        this.secret = ThreadLocalRandom.current().nextInt(1,101);
        log.info("Server secret is: {}" , this.secret);
    }


    @Override
    public void onNext(GuessRequest guessRequest) {
        if(guessRequest.getGuess() > secret){
            this.send(Result.HIGH) ;
        }else if(guessRequest.getGuess() < secret){
            this.send(Result.LOW);
        }else{
            log.info("client guessed correct number that is: {} ",guessRequest.getGuess());
            this.send(Result.CORRECT);
            this.guessResponseStreamObserver.onCompleted();
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        this.guessResponseStreamObserver.onCompleted();
    }

    private void send(Result result){
        attempt++ ;
        var response = GuessResponse.newBuilder()
                .setAttempt(attempt)
                .setResult(result)
                .build() ;

        this.guessResponseStreamObserver.onNext(response) ;
    }
}
