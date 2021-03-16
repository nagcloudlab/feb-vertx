package example.streams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerResponse;

public class FileStreamingServerVerticle extends AbstractVerticle {
	
	private final Logger logger = LoggerFactory.getLogger(FileStreamingServerVerticle.class);


	@Override
	public void start(Promise<Void> startPromise) throws Exception {

		vertx.createHttpServer().requestHandler(request -> {
			
			logger.info("new request");
			
			HttpServerResponse response = request.response();
			response
			.putHeader("Content-Type", "application/octet-stream")
			.setChunked(true);
			
			OpenOptions options = new OpenOptions().setRead(true);
			vertx.fileSystem().open("/Users/nag/Downloads/temp_5GB_file", options, ar -> {
				if (ar.succeeded()) {
					logger.info("file opened");
					
					AsyncFile readStream = ar.result();
					
//					 readStream
//			            .handler(buffer->{
//			            	
//			            	response.write(buffer);
//			            	
//			            	if(response.writeQueueFull()) {
//			            		readStream.pause();
//			            		response.drainHandler(v->{
//				            		readStream.resume();
//				            	});
//			            	}
//			            	
//			            })
//			            .exceptionHandler(Throwable::printStackTrace)
//			            .endHandler(done -> {
//			                System.out.println("\n--- DONE");
//			                response.end();
//			                readStream.close();
//			            });
					
					
					// - or -
					
					readStream.pipeTo(response);
					
					
				} else {
					System.out.println(ar.cause());
				}
			});

		}).listen(8080,ar->{
			if(ar.succeeded()) {
				System.out.println("server up");
			}else {
				ar.cause().printStackTrace();
			}
		});

	}

	@Override
	public void stop(Promise<Void> stopPromise) throws Exception {
	}
}
