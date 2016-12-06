package cn.entersoft.threadstudy.synchronizedtest;

/**
 * 测试同步代码块
 * @author dingsj
 *
 */
public class Test_Synchronized_Thread implements Runnable{
	
	public void run(){
		/*线程1和线程2同时到达，但是线程1和线程2都要执行完该锁块后才能继续执行。*/
		synchronized (this) {
			System.out.println("线程锁住了哦："+Thread.currentThread().getName());
			try {
				Thread.sleep(3000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("线程没锁："+Thread.currentThread().getName());
	}
}
