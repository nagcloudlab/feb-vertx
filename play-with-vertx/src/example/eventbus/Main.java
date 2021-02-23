package example.eventbus;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {
	
	public static void main(String[] args) {
		
		Vertx vertx=Vertx.vertx();
		vertx.deployVerticle("example.eventbus.HeatSensor",new DeploymentOptions().setInstances(4));
		vertx.deployVerticle("example.eventbus.Listener",new DeploymentOptions());
		vertx.deployVerticle("example.eventbus.HttpServer",new DeploymentOptions());
		vertx.deployVerticle("example.eventbus.SensorData",new DeploymentOptions());
		
		
	}

}
