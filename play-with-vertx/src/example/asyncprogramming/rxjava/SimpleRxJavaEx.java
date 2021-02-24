package example.asyncprogramming.rxjava;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

public class SimpleRxJavaEx {

	public static void main(String[] args) {

		//
		Observable<String> stream = Observable.create(emitter -> {
			int i = 0;
			while (true) {
				i++;
				TimeUnit.SECONDS.sleep(2);
				String data = "data-" + i;
				emitter.onNext(data);
			}
		});

		stream = stream.map(data -> data.toUpperCase());

		stream.subscribe(data -> {
			System.out.println(data);
		}, error -> {
			System.out.println(error);
		}, () -> {
			System.out.println("complete");
		});

	}

}
