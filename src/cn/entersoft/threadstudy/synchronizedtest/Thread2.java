package cn.entersoft.threadstudy.synchronizedtest;

/**
 * ��һ���̷߳���object��һ��synchronized(this)ͬ�������ʱ����һ���߳���Ȼ���Է��ʸ�object�еķ�synchronized(
 * this)ͬ������顣
 * 
 * @author dingsj 2013-12-23
 */
public class Thread2 implements Runnable {

	@Override
	public void run() {

	}

	public void m4t1() {
		synchronized (this) {
			int i = 5;
			while (i-- > 0) {
				System.out.println(Thread.currentThread().getName() + "------>"
						+ i);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void m4t2() {
		int i = 5;
		while (i-- > 0) {
			System.out
					.println(Thread.currentThread().getName() + "------>" + i);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		final Thread2 myt2 = new Thread2();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				myt2.m4t1();
			}
		}, "t1");
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				myt2.m4t2();
			}
		}, "t2");
		t1.start();
		t2.start();
	}
}
