package cn.entersoft.threadstudy.synchronizedtest;

/**
 * 测试同步代码块
 * @author dingsj
 *
 */
public class Test_Synchronized_Main {
	public static void main(String[] args) {
		Thread t1 = new Thread(new Test_Synchronized_Thread(),"线程1");
		t1.start();
		Thread t2 = new Thread(new Test_Synchronized_Thread(),"线程2");
		t2.start();
	}
}
