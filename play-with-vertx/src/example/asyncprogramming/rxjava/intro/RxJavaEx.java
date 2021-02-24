package example.asyncprogramming.rxjava.intro;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

//----------------------------------------------------

//Trainer
//----------------------------------------------------

class Trainer {
	public Flowable<Integer> doTraining() {
		Flowable<Integer> webex = Flowable.create(emitter -> {
			int i = 0;
			while (i < 10) {
				i++;
				System.out.println(Thread.currentThread()+" pushing data/event into stream");
				emitter.onNext(i);
				TimeUnit.SECONDS.sleep(1);
				if(i==5) {
					emitter.onError(new IllegalStateException("Not Well"));
					break;
				}
			}
			emitter.onComplete();

		}, BackpressureStrategy.LATEST);
		return webex;
	}
}

//----------------------------------------------------
//Participant
//----------------------------------------------------

class Particpant {

	public void doLearn(Trainer trainer) {
		Flowable<Integer> webex = trainer.doTraining();
		webex
//		.subscribeOn(Schedulers.computation())
//		.observeOn(Schedulers.io())
		.filter(topic->topic%2==0)
		.buffer(2)
		.subscribe(topic -> {
			System.out.println(Thread.currentThread()+" reacting to the topic : " + topic);
		}, error -> {
			System.out.println(Thread.currentThread()+" participant reacting to the error : " + error);
		}, () -> {
			System.out.println(Thread.currentThread()+" participant reacting to the complete signal : Thanks");
		});
	}
}

public class RxJavaEx {

	public static void main(String[] args)throws Exception {

		Trainer trainer = new Trainer();
		Particpant particpant = new Particpant();

		particpant.doLearn(trainer);
		
		
		Thread.sleep(10*1000);

	}

}
