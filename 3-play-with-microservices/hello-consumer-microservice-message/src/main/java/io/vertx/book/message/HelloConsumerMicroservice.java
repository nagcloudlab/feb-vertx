package io.vertx.book.message;

import java.util.concurrent.TimeUnit;

import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.circuitbreaker.CircuitBreaker;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.RxHelper;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

public class HelloConsumerMicroservice extends AbstractVerticle {

    @Override
    public void start() {
    	
    	CircuitBreaker breaker = CircuitBreaker.create("my-circuit-breaker", vertx,
    		    new CircuitBreakerOptions()
    		        .setMaxFailures(2) // number of failure before opening the circuit
    		        .setTimeout(2000) // consider a failure if the operation does not succeed in time
    		        .setFallbackOnFailure(true) // do we call the fallback on failure
    		        .setResetTimeout(5000) // time spent in open state before attempting to re-try
    		);
    	
    
    	vertx.createHttpServer()
    	.requestHandler(req->{
//    		
//    		breaker.execute(promise->{
//    			
//    			System.out.println("invoking hello service....becoz circuit closed");
    			
    			Single<JsonObject> s1=vertx.eventBus()
    	    		     .<JsonObject>rxSend("hello.address", "Nag")
    	    		     .subscribeOn(RxHelper.scheduler(vertx))
    	    		     .map(Message::body);
    	    		
    	    		Single<JsonObject> s2=vertx.eventBus()
    	       		     .<JsonObject>rxSend("hello.address", "IBM")
    	       		     .subscribeOn(RxHelper.scheduler(vertx))
    	       		     .timeout(3, TimeUnit.SECONDS)
    	       		     .map(Message::body);
    	    		
    	    		
    	    		Single.zip(s1,s2,(nag,ibm)->{
    	    			return new JsonObject()
    	    					.put("nag", nag.getString("message")+  " from " + nag.getString("served-by"))
    	    					.put("ibm", ibm.getString("message")+ " from " + ibm.getString("served-by"));
    	    		})
    	    		.subscribe(
    	    			x->{
    	    				req.response().putHeader("Content-Type", "application/json").end(x.encode());
    	    			},
    	    			t->{
//    	    				promise.fail(t);
    	    				req.response().end(new JsonObject().encodePrettily());
    	    			}
    	    		);
    	    		
//    		});
    		
    	})
    	.listen(8181);

    }

}
