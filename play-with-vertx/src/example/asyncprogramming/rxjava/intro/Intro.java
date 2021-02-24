package example.asyncprogramming.rxjava.intro;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class Intro {
	public static void main(String[] args) throws Exception {

		
//		Observable.just(1, 2, 3)
//		.map(x->x*10)
//		.map(x->x+1)
//		.subscribe(System.out::println);
		
		
//		System.out.println();
		
		
//		Observable.<String>error(() -> new RuntimeException("Woops"))
//		.map(String::toUpperCase)
//		.subscribe(System.out::println, Throwable::printStackTrace);
		
//		System.out.println();
		
		
//		Single<String> s1 = Single.just("foo");
//		Single<String> s2 = Single.just("bar");
//		Flowable<String> m=Single.merge(s1,s2);
//		m.subscribe(System.out::println);
		
		
//		Observable.just("--", "this", "is", "--", "a", "sequence", "of", "items", "!") // file . n/w source . db , .....
//		.delay(2, TimeUnit.SECONDS)
//		.doOnSubscribe(d->System.out.println("subscribed"))
//		.filter(s->!s.startsWith("--"))
//		.doOnNext(x->System.out.println("doOnNext() "+x))
//		.map(String::toUpperCase)
//		.buffer(2)
//		.subscribe(data->System.out.println("next: "+data),Throwable::printStackTrace,() -> System.out.println("~Done~"));
//		
//		
//	    Thread.sleep(10_000);
		
		
		

	}
}
