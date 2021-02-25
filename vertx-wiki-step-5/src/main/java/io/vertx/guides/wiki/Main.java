package io.vertx.guides.wiki;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {
	
	public static void main(String[] args) {
		
		Vertx vertx=Vertx.vertx();
		vertx.
		deployVerticle("io.vertx.guides.wiki.MainVerticle",new DeploymentOptions())
		.onFailure(Throwable::printStackTrace);
		
		
	}

}
