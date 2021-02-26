package example.streams;

import io.vertx.core.Vertx;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.OpenOptions;

public class AsyncStream {
	
	public static void main(String[] args) {
		
	    Vertx vertx = Vertx.vertx();
	    
	    OpenOptions opts = new OpenOptions().setRead(true);
	    // stream
	    vertx.fileSystem().open("Menu.txt", opts, ar->{
	    	if(ar.succeeded()) {
	            AsyncFile readStream = ar.result();
	            readStream
	            .handler(System.out::println)
	            .exceptionHandler(Throwable::printStackTrace)
	            .endHandler(done -> {
	                System.out.println("\n--- DONE");
	                readStream.close();
	            });
	    	}else {
	    		System.out.println(ar.cause());
	    	}
	    });
		
	}

}
