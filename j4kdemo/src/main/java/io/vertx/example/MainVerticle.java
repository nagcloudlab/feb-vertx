package io.vertx.example;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.config.ConfigChange;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.Counter;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

// TODO: Get configuration
// TODO: File config
// TODO: k8s configmap
// TODO: Setup Router
// TODO: Log all REST requests
// TODO: RoutingContext chaning
// TODO: Initialize our session management
// TODO: Add REST Endpoints
// TODO: Set up our eventbus bridge
// TODO: Sockejs websockets
// TODO: create an HTTP server
// TODO: Attach Router to server

public class MainVerticle extends AbstractVerticle {

	private final Logger logger = LoggerFactory.getLogger(MainVerticle.class);

	private JsonObject currentConfig = config();
	private static final String INSTANCE_ID = UUID.randomUUID().toString();
	private static final String REQUEST_COUNTER = "requestCount";
	private static final String APP_NAME = "appname";
	private static final String MESSAGE_COUNTER = "messageCounter";
	

	private Counter counter;
	
	@Override
	public void start(Promise<Void> startPromise) {

		vertx.fileSystem().exists("./config.json").onSuccess(this::initConfig);

		Router router = Router.router(vertx);
		router.route().handler(this::logRequest);

		SessionStore clusteredSession = ClusteredSessionStore.create(vertx);
		router.route().handler(SessionHandler.create(clusteredSession));

		router.get("/api/v1/podinfo").handler(this::getPodInfo);
		router.get("/api/v1/healthz").handler(this::healthCheck);

		router.mountSubRouter("/api/eventbus", this.createSockJSEventBusBridge());
		
		router.get().handler(this.configStaticHandler());
		
		vertx.createHttpServer()
		.requestHandler(router)
		.listen(currentConfig.getInteger("port",8080))
		.compose(this::initSharedDataCounter)
		.compose(this::storeCounterReference)
		.compose(this::setupPeriodicEventBusMessage)
		.onSuccess(startPromise::complete)
		.onFailure(startPromise::fail);
		

	}
	
	private StaticHandler configStaticHandler() {
		return StaticHandler.create()
				.setIndexPage("index.html")
				.setCachingEnabled(true)
				.setFilesReadOnly(true)
				.setWebRoot(currentConfig.getString("webroot","webroot"))
				.setDirectoryListing(false);
	}
	
	private Future<Void> setupPeriodicEventBusMessage(Void unused) {
		vertx.setPeriodic(500, this::triggerMessageSend);
		return Future.succeededFuture();
	}
	private void triggerMessageSend(Long timerId) {
		counter.incrementAndGet()
		.onSuccess(this::sendStatusMessage)
		.onFailure(err->{
			logger.error("error getting shared data counter ",err);
		});
	}
	
	private void sendStatusMessage(Long count) {
		JsonObject message=new JsonObject()
					.put("id", INSTANCE_ID)
					.put(APP_NAME, currentConfig.getString(APP_NAME,"J4k"))
					.put(MESSAGE_COUNTER, count);
		vertx.eventBus().send("status", message);
	}
	
	private  Future<Void> storeCounterReference(Counter counter){
		this.counter=counter;
		return Future.succeededFuture();
	}

	private Future<Counter> initSharedDataCounter(HttpServer httpServer){
		return vertx.sharedData().getCounter(MESSAGE_COUNTER);
	}

	private Router createSockJSEventBusBridge() {
		final SockJSBridgeOptions bridgeOptions = new SockJSBridgeOptions()
				.addInboundPermitted(new PermittedOptions().setAddressRegex(".*"))
				.addOutboundPermitted(new PermittedOptions().setAddressRegex(".*"));
		return SockJSHandler.create(vertx).bridge(bridgeOptions);
	}

	private void getPodInfo(RoutingContext ctx) {
		Session session = ctx.session();
		JsonObject podInfo = new JsonObject();
		podInfo.put("id", INSTANCE_ID);
		Integer reqCount = (Integer) session.data().getOrDefault(REQUEST_COUNTER, 0);
		session.data().put(REQUEST_COUNTER, reqCount + 1);
		ctx.response().setStatusCode(200).setStatusMessage("OK").putHeader("Content-Type", "application/json")
				.end(podInfo.encodePrettily());
		logger.info("Pod Info: {}", podInfo.encodePrettily());
	}

	private void healthCheck(RoutingContext ctx) {

	}

	private void logRequest(RoutingContext ctx) {
		logger.info("Request: {}", ctx.request().path());
		ctx.next();
	}

	private void initConfig(boolean hasConfigJson) {
		ConfigRetrieverOptions configOptions = new ConfigRetrieverOptions();
		if (hasConfigJson) {
			configOptions.addStore(new ConfigStoreOptions().setType("file").setFormat("json")
					.setConfig(new JsonObject().put("path", "./config.json")));
		}
		if (System.getenv().containsKey("KUBERNETES_NAMESPACE")) {
			configOptions.addStore(new ConfigStoreOptions().setType("configmap")
					.setConfig(new JsonObject()
							.put("namespace", System.getenv().getOrDefault("KUBERNETES_NAMESPACE", "default"))
							.put("name", "j4kdemo")));
		}
		ConfigRetriever.create(vertx, configOptions).listen(this::loadNewConfig);
	}

	private void loadNewConfig(ConfigChange change) {
		this.currentConfig.mergeIn(change.getNewConfiguration());
	}

}
