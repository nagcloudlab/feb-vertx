package example.asyncprogramming.rxjava.intro;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.RxHelper;
import io.vertx.reactivex.core.Vertx;


public class VertxRxjavaEx extends AbstractVerticle {

	
	@Override
	public Completable rxStart() {
		
		Observable
		.interval(1, TimeUnit.SECONDS,RxHelper.scheduler(vertx))
		.subscribe(n->System.out.println("tick"));
		
		
		return vertx.createHttpServer()
		.requestHandler(r -> r.response().end("Ok"))
		.rxListen(8080)
		.ignoreElement();
		
		
	}
	

	  public static void main(String[] args) {
	    Vertx.vertx().deployVerticle(new VertxRxjavaEx());
	  }
	
	
}
