package cn.entersoft.threadstudy.threadpool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * �Զ����̳߳�
 *@author dingsj
 *2013-12-23
 */
public class ThreadPool {
	/**ʵ���̳߳ض���ʱ���Ŀ����߳���*/
	private int maxThreads;
	/**�̳߳ع�������������*/
	private boolean on = false;
	/**���ﶨ����һ�����������������������Ҫ���������linkedlist���Ƚ������ȴ���*/
	private LinkedList<Runnable> tasks = new LinkedList<Runnable>();
	/**������һ������Thread����������������������е��߳�*/
	private List<Thread> poolThreads;
	
	/**
	 * @param maxThreads ����߳���
	 */
	public ThreadPool(int maxThreads){
		this.maxThreads = maxThreads;
		poolThreads = new ArrayList<Thread>();
		for(int i = 0; i < this.maxThreads; i++){
			poolThreads.add(new Thread(new SingleThread()));
		}
	}
	
	/**
	 * �����߳�
	 * @author dingsj
	 * @since 2013-12-23 ����01:21:50
	 */
	public void start(){
		this.on = true;
		/**����ÿһ���߳�*/
		for(int i = 0; i < this.maxThreads; i++){
			poolThreads.get(i).start();
			System.out.println("the number "+i+"thread of"+poolThreads.size());
		}
	}
	
	/**
	 * �ر��̳߳صĹ���
	 * @author dingsj
	 * @since 2013-12-23 ����01:25:03
	 */
	public void shutDown(){
		this.on = false;
	}
	
	/**
	 * ִ��������
	 * @param task  ��ִ�е�������ʵ��runnable�ӿ�
	 * @author dingsj
	 * @since 2013-12-23 ����01:26:19
	 */
	public void execute(Runnable task){
		synchronized (tasks) {
			tasks.add(task);//��������ӵ��̳߳���ִ��
			tasks.notifyAll();//֪ͨ���е�����tasks�̣߳�����ִ���¼��������
		}
	}
	
	private class SingleThread implements Runnable{
		@Override
		public void run() {
			/**����̳߳��ڹ���*/
			while (on) {
				synchronized (tasks) {
					Runnable task = tasks.poll();//ȡ����������е�����ִ��
					if(task != null){
						task.run();
					}
					/**���������û�еȴ���������ֹͣ�̣߳�ֱ����������������*/
					if(tasks.size() == 0){
						try {
							tasks.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
	}
}
