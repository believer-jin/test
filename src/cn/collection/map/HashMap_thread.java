package cn.collection.map;

import java.util.HashMap;
import java.util.UUID;

/**
 * ����hashMap�ڶ��߳��µĲ���ȫ��������
 *@author dingsj
 *2014-2-20
 */
public class HashMap_thread {
	
	public static void main(String[] args) throws InterruptedException {
		final HashMap<String, String> hMap = new HashMap<String, String>(2);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for(int i = 0; i < 100000; i++){
					new Thread(new Runnable() {
						@Override
						public void run() {
							System.out.println(Thread.currentThread().getName());
							hMap.put(UUID.randomUUID().toString(), "");
						}
					},"ftf"+i).start();
				}
			}
		},"ftf");
		thread.start();
		thread.join();
	}
}
