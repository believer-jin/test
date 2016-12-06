package cn.entersoft.threadstudy.threadpool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 自定义线程池
 *@author dingsj
 *2013-12-23
 */
public class ThreadPool {
	/**实现线程池对象时最大的可用线程数*/
	private int maxThreads;
	/**线程池工作的启动开关*/
	private boolean on = false;
	/**这里定义了一个任务队列容器，用来放置要处理的任务，linkedlist：先进来的先处理*/
	private LinkedList<Runnable> tasks = new LinkedList<Runnable>();
	/**定义了一个放置Thread对象的容器，用来放置所有的线程*/
	private List<Thread> poolThreads;
	
	/**
	 * @param maxThreads 最大线程数
	 */
	public ThreadPool(int maxThreads){
		this.maxThreads = maxThreads;
		poolThreads = new ArrayList<Thread>();
		for(int i = 0; i < this.maxThreads; i++){
			poolThreads.add(new Thread(new SingleThread()));
		}
	}
	
	/**
	 * 启动线程
	 * @author dingsj
	 * @since 2013-12-23 下午01:21:50
	 */
	public void start(){
		this.on = true;
		/**启动每一个线程*/
		for(int i = 0; i < this.maxThreads; i++){
			poolThreads.get(i).start();
			System.out.println("the number "+i+"thread of"+poolThreads.size());
		}
	}
	
	/**
	 * 关闭线程池的工作
	 * @author dingsj
	 * @since 2013-12-23 下午01:25:03
	 */
	public void shutDown(){
		this.on = false;
	}
	
	/**
	 * 执行新任务
	 * @param task  被执行的新任务，实现runnable接口
	 * @author dingsj
	 * @since 2013-12-23 下午01:26:19
	 */
	public void execute(Runnable task){
		synchronized (tasks) {
			tasks.add(task);//将任务添加到线程池中执行
			tasks.notifyAll();//通知所有的锁定tasks线程，让其执行新加入的任务
		}
	}
	
	private class SingleThread implements Runnable{
		@Override
		public void run() {
			/**如果线程池在工作*/
			while (on) {
				synchronized (tasks) {
					Runnable task = tasks.poll();//取出任务队列中的任务执行
					if(task != null){
						task.run();
					}
					/**如果队列中没有等待的任务，则停止线程，直到有任务加入队列中*/
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
