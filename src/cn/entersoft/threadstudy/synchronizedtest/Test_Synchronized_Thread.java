package cn.entersoft.threadstudy.synchronizedtest;

/**
 * ����ͬ�������
 * @author dingsj
 *
 */
public class Test_Synchronized_Thread implements Runnable{
	
	public void run(){
		/*�߳�1���߳�2ͬʱ��������߳�1���߳�2��Ҫִ������������ܼ���ִ�С�*/
		synchronized (this) {
			System.out.println("�߳���ס��Ŷ��"+Thread.currentThread().getName());
			try {
				Thread.sleep(3000l);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("�߳�û����"+Thread.currentThread().getName());
	}
}
