package cn.test.concurrent.cas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFuture {

	private AtomicInteger i = new AtomicInteger(0);

	public void test() {
		try {
			System.out.println("start thread pool");
			ExecutorService pool = Executors.newFixedThreadPool(5);
			List<Future<String>> list = new ArrayList<Future<String>>();
			for (int x = 0; x < 10; x++) {
				Future<String> fu = pool.submit(new TestTask());
				list.add(fu);
			}
//			System.out.println("thread commit done");
//			Thread.currentThread().sleep(6000);
			for (Future<String> fu : list) {
				System.out.println(System.currentTimeMillis());
					System.out.println(fu.get());
					System.out.println(System.currentTimeMillis());
			}

			System.out.println("start thread runable");
			List<Future<?>> list1 = new ArrayList<Future<?>>();
			for (int x = 0; x < 10; x++) {
				Future<?> xx = pool.submit(new TestRunnable());
				list1.add(xx);
			}
			System.out.println("thread runable commit done");
			for (Future<?> fu : list1) {
//				System.out.println(fu.get());
			}
			System.out.println("thread runable done");
			System.out.println("start Executor pool");
			Executor ex = Executors.newFixedThreadPool(5);
			for (int x = 0; x < 10; x++) {
				ex.execute(new TestRunnable());
			}
			System.out.println("main thread done");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public static void main(String[] args) {
		TestFuture f = new TestFuture();
		f.test();
	}

	private class TestTask implements Callable<String> {

		@Override
		public String call() throws Exception {
			Thread.sleep(5000);
			System.out.println("TestRunnable currentThread:" + Thread.currentThread().getName() + "   i=" + i.incrementAndGet());
			return i.toString();
		}

	}

	private class TestRunnable implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
				System.out.println("TestRunnable currentThread:" + Thread.currentThread().getName() + "   i=" + i.incrementAndGet());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
