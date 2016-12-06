package cn.entersoft.threadstudy.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TestMain {
	public static void main(String[] args) {
		List<Future<String>> list = new ArrayList<Future<String>>();
		for(int i = 0; i <= 10; i++){
			ExecutorService pool = Executors.newFixedThreadPool(5);
			for(int j = 0; j <= 5; j++){
				TestCallable testCallable = new TestCallable();
				testCallable.setI(i);
				testCallable.setJ(j);
				Future<String> future = pool.submit(testCallable);
				list.add(future);
			}
		}
		for(Future<String> future : list){
			try {
				System.out.println(future.get());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
