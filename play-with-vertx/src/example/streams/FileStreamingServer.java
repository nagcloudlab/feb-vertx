package example.streams;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.http.HttpServerResponse;

public class FileStreamingServer extends AbstractVerticle {

	@Override
	public void start() throws Exception {

		vertx.createHttpServer().requestHandler(request -> {
			
			HttpServerResponse response=request.response();
			
			OpenOptions opts = new OpenOptions().setRead(true);
			vertx
			.fileSystem()
			.open("/Users/nag/Downloads/temp_5GB_file", opts,ar->{
				if(ar.succeeded()) {
					AsyncFile readStream=ar.result();
					response.setStatusCode(200)
					.putHeader("Content-Type", "application/octet-stream")
					.setChunked(true);
					
//					readStream.handler(buffer->{
//						response.write(buffer);
//						if(response.writeQueueFull()) {
//							readStream.pause();
//							response.drainHandler(v->readStream.resume());
//						}
//					});
//					readStream.endHandler(v->response.end());
					
					// - or -
					
					readStream.pipeTo(response);
					
					
				}else {
					ar.cause().printStackTrace();
				}
			});
			
			
		}).listen(8080);

	}

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new FileStreamingServer());

	}

}
