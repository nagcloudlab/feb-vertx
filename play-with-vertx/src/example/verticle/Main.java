package example.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx();

		// ---------------------------------------------------------------
		// #1 : basic verticle with external configuration
		// ---------------------------------------------------------------
		
		JsonObject config = new JsonObject();
		config.put("http.port", 8181);
		DeploymentOptions opts1 = new DeploymentOptions().setConfig(config);

		vertx.deployVerticle("example.verticle.HTTPServerVerticle", opts1, ar -> {
			if (ar.succeeded()) {
				logger.info("Open http://localhost:8080/");
			} else {
				logger.error(ar.cause().getMessage());
			}
		});
		
		// ---------------------------------------------------------------
		// #2 Never Block EventLoop
		// ---------------------------------------------------------------

		vertx.deployVerticle(new NeverBlockEventLoop());

		// ---------------------------------------------------------------
		// #3 verticle deploying other verticles
		// ---------------------------------------------------------------

		vertx.deployVerticle(new VerticleOne());

		// ---------------------------------------------------------------
		// #4 verticle on worker threads
		// ---------------------------------------------------------------

		DeploymentOptions opts2 = 
				new DeploymentOptions()
				.setInstances(1)
				.setWorker(true);

		vertx.deployVerticle("example.verticle.WorkerVerticle", opts2, ar -> {
			if (ar.succeeded()) {
				logger.info("deployed");
			} else {
				ar.cause().printStackTrace();
			}
		});

		// ---------------------------------------------------------------

	}

}
