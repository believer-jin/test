package cn.entersoft.threadstudy;

import java.util.HashMap;
import java.util.Map;

/**
 * 邮件发送线程：只发送一封邮件，该线程只执行一次
 * 
 * @author dingsj 2013-12-23
 */
@SuppressWarnings("rawtypes")
public class OneMailSend implements Runnable {
	private boolean exit = true;// 线程执行的开关
	private Map paramMap = null;// 参数集合

	public void stopThread() {
		this.exit = false;
	}

	/**
	 * 停止线程
	 * 
	 * @param paramMap
	 * @author dingsj
	 * @since 2013-12-23 下午05:01:48
	 */
	public void setParamMap(Map paramMap) {
		this.paramMap = paramMap;
	}

	@Override
	public void run() {
		System.out.println(exit);
		while (exit) {
			if (paramMap != null) {
				/** 邮件发送 */
				System.out.println(Thread.currentThread().getName());
			} else {
				System.out
						.println("邮件发送异常------------->邮箱参数Map----------->null");
			}
			stopThread();
		}
	}

	/**
	 * 测试方法
	 * 
	 * @param args
	 * @author dingsj
	 * @since 2013-12-23 下午03:38:42
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
