package cn.entersoft.threadstudy;

import java.util.HashMap;
import java.util.Map;

/**
 * �ʼ������̣߳�ֻ����һ���ʼ������߳�ִֻ��һ��
 * 
 * @author dingsj 2013-12-23
 */
@SuppressWarnings("rawtypes")
public class OneMailSend implements Runnable {
	private boolean exit = true;// �߳�ִ�еĿ���
	private Map paramMap = null;// ��������

	public void stopThread() {
		this.exit = false;
	}

	/**
	 * ֹͣ�߳�
	 * 
	 * @param paramMap
	 * @author dingsj
	 * @since 2013-12-23 ����05:01:48
	 */
	public void setParamMap(Map paramMap) {
		this.paramMap = paramMap;
	}

	@Override
	public void run() {
		System.out.println(exit);
		while (exit) {
			if (paramMap != null) {
				/** �ʼ����� */
				System.out.println(Thread.currentThread().getName());
			} else {
				System.out
						.println("�ʼ������쳣------------->�������Map----------->null");
			}
			stopThread();
		}
	}

	/**
	 * ���Է���
	 * 
	 * @param args
	 * @author dingsj
	 * @since 2013-12-23 ����03:38:42
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		for (int i = 0; i <= 10000; i++) {
			OneMailSend oneMailSend = new OneMailSend();
			HashMap paramMap = new HashMap();
			paramMap.put("1", "222");
			Thread thread = null;
			oneMailSend.setParamMap(paramMap);
			System.out.println(i);
			thread = new Thread(oneMailSend, "ThreadName+" + i);
			thread.start();
		}
	}

}
