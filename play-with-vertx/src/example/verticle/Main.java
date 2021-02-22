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
//		JsonObject config=new JsonObject();
//		config.put("http.port", 8181);
//		DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(config);
//
//		vertx.deployVerticle("example.verticle.HTTPServerVerticle", deploymentOptions, ar -> {
//			if (ar.succeeded()) {
//				logger.info("Open http://localhost:8080/");
//			} else {
//				logger.error(ar.cause().getMessage());
//			}
//		});

		// ---------------------------------------------------------------

		DeploymentOptions opts = new DeploymentOptions().setInstances(1);

		vertx.deployVerticle("example.verticle.OffloadVerticle", opts,ar->{
			if(ar.succeeded()) {
				logger.info("deployed");
			}else {
				ar.cause().printStackTrace();
			}
		});

		// ---------------------------------------------------------------

	}

}
