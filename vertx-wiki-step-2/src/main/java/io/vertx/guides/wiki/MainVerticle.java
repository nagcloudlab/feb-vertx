package io.vertx.guides.wiki;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startPromise) throws Exception {

		Promise<String> dbVerticleDeployment = Promise.promise(); // <1>
		vertx.deployVerticle(new WikiDatabaseVerticle(), dbVerticleDeployment); // <2>

		dbVerticleDeployment.future().compose(id -> {
			Promise<String> httpVerticleDeployment = Promise.promise();
			vertx.deployVerticle("io.vertx.guides.wiki.HttpServerVerticle", // <4>
					new DeploymentOptions().setInstances(2), // <5>
					httpVerticleDeployment);
			return httpVerticleDeployment.future(); // <6>
		}).onComplete(ar -> {
			if (ar.succeeded()) {
				startPromise.complete();
			} else {
				startPromise.fail(ar.cause());
			}
		});

	}

}