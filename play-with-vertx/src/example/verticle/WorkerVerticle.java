package example.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;

public class WorkerVerticle extends AbstractVerticle {
	private final Logger logger = LoggerFactory.getLogger(WorkerVerticle.class);

	@Override
	public void start() {
		vertx.setPeriodic(4000, id -> {
			try {
				logger.info("Zzz...");
				Thread.sleep(8000); // blocking code
				logger.info("Up!");
			} catch (InterruptedException e) {
				logger.error("Woops", e);
			}
		});
	}

}