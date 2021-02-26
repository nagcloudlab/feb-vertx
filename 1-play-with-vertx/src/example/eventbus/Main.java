package example.eventbus;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

public class Main {
	
	public static void main(String[] args) {
		
		Vertx vertx=Vertx.vertx();
		DeploymentOptions opts1=new DeploymentOptions()
							  .setInstances(4);
		vertx.deployVerticle("example.eventbus.HeatSensor",opts1);
		vertx.deployVerticle("example.eventbus.Listener");
		DeploymentOptions opts2=new DeploymentOptions()
				  .setConfig(new JsonObject().put("port", 8181));
		vertx.deployVerticle("example.eventbus.HttpServer",opts2);
		vertx.deployVerticle("example.eventbus.SensorData");
		
		
		
	}

}
