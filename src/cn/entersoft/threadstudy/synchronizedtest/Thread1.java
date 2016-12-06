package cn.entersoft.threadstudy.synchronizedtest;

/**
 * �����������̷߳���ͬһ������object�е����synchronized(this)ͬ�������ʱ��һ��ʱ����ֻ����һ���̵߳õ�ִ�С�
 * ��һ���̱߳���ȴ���ǰ�߳�ִ�������������Ժ����ִ�иô���顣
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
