package example.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

public class NeverBlockEventLoop extends AbstractVerticle {
	private final Logger logger = LoggerFactory.getLogger(NeverBlockEventLoop.class);

	@Override
	public void start() throws Exception {

		vertx.setPeriodic(5000, id -> {
			logger.info("tick");
			// e.g blocking IO or long-running execution
			while(true) {}
		});

	}

}
