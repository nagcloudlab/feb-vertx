package example.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

public class VerticleTwo extends AbstractVerticle {
	
	private final Logger logger = LoggerFactory.getLogger(VerticleTwo.class);

	@Override
	public void start() {
		logger.info("Start");
	}

	@Override
	public void stop() {
		logger.info("Stop");
	}
}