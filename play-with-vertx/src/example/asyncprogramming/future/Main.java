package example.asyncprogramming.future;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {
	
	public static void main(String[] args) {
		
		Vertx vertx=Vertx.vertx();
		
		
		vertx.deployVerticle("example.asyncprogramming.sensor.HeatSensor",
				new DeploymentOptions()
				.setConfig(new JsonObject()
						.put("http.port", 3001)));
		
		vertx.deployVerticle("example.asyncprogramming.sensor.HeatSensor",
				new DeploymentOptions()
				.setConfig(new JsonObject()
						.put("http.port", 3002)));
		
		vertx.deployVerticle("example.asyncprogramming.sensor.HeatSensor",
				new DeploymentOptions()
				.setConfig(new JsonObject()
						.put("http.port", 3003)));
		
		vertx.deployVerticle("example.asyncprogramming.snapshot.SnapshotService",
				new DeploymentOptions());
		
		vertx.deployVerticle("example.asyncprogramming.future.EdgeService",
				new DeploymentOptions());
		
	}

}
