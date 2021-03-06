package example.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class HttpServerVerticle extends AbstractVerticle {

	private final Logger logger = LoggerFactory.getLogger(HttpServerVerticle.class);
	private long counter = 1;

	@Override
	public void start(Promise<Void> promise) throws Exception {
		vertx.setPeriodic(5000, id -> {
			logger.info("tick");
		});

		vertx.createHttpServer().requestHandler(req -> {
			logger.info("Request #{} from {}", counter++, req.remoteAddress().host());
			req.response().end("Hello");
		}).listen(config().getInteger("http.port", 8080), ar -> {
			if (ar.succeeded()) {
				promise.complete();
			} else {
				promise.fail(ar.cause().getMessage());
			}
		});

	}

	@Override
	public void stop() throws Exception {
		System.out.println("HTTPServerVerticle:stop()");
	}

}
