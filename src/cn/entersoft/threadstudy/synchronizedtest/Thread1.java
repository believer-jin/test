package cn.entersoft.threadstudy.synchronizedtest;

/**
 * 当两个并发线程访问同一个对象object中的这个synchronized(this)同步代码块时，一个时间内只能有一个线程得到执行。
 * 另一个线程必须等待当前线程执行完这个代码块以后才能执行该代码块。
 *@author dingsj
 *2013-12-23
 */
public class Thread1  implements Runnable{
	@Override
	public void run() {
		synchronized (this) {
			for(int i = 0; i < 5; i++){
				System.out.println(Thread.currentThread().getName()+"--->synchronized loop--->"+i);
			}
		}
	}
	
	public static void main(String[] args) {
		Thread1 t1 = new Thread1();
		Thread thread = new Thread(t1,"A");
		Thread thread2 = new Thread(t1,"B");
		thread.start();
		thread2.start();
	}
}
