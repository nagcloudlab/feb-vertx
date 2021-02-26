package io.vertx.book.message;

import io.vertx.core.json.JsonObject;
import io.vertx.rxjava.core.AbstractVerticle;
import io.vertx.rxjava.core.eventbus.Message;
import rx.Single;

public class HelloConsumerMicroservice extends AbstractVerticle {

    @Override
    public void start() {
    	
    
    	vertx.createHttpServer()
    	.requestHandler(req->{
    		
    		Single<JsonObject> s1=vertx.eventBus()
    		     .<JsonObject>rxSend("hello.address", "Nag")
    		     .map(Message::body);
    		
    		Single<JsonObject> s2=vertx.eventBus()
       		     .<JsonObject>rxSend("hello.address", "IBM")
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
    				req.response().end(new JsonObject().encodePrettily());
    			}
    		);
    		
    		
    	})
    	.listen(8181);

    }

}
