package cn.entersoft.threadstudy.synchronizedtest;

/**
 * ����ͬ�������
 * @author dingsj
 *
 */
public class Test_Synchronized_Main {
	public static void main(String[] args) {
		Thread t1 = new Thread(new Test_Synchronized_Thread(),"�߳�1");
		t1.start();
		Thread t2 = new Thread(new Test_Synchronized_Thread(),"�߳�2");
		t2.start();
	}
}
