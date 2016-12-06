package cn.entersoft.threadstudy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest2 {
	public static void main(String[] args) {
		ExecutorService threadPool = Executors.newFixedThreadPool(5);
		for(int i = 0; i< 100;i++){
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName()+"----");
				}
			});
		}
	}
}
