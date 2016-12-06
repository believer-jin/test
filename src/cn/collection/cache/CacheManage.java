package cn.collection.cache;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>���������
 *@author dingsj
 *@version 1.0
 *@since 2014-2-20
 */
public abstract class CacheManage<K,V> implements CacheInterface<K, V>{
	protected  Map<K , CacheObject<K, V>> cacheMap = null;//���ڱ��滺�����ݵ�map����
	private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();//��ȡ������
	private final Lock readLock = cacheLock.readLock();//��ȡ��ȡ��
	private final Lock writeLock = cacheLock.writeLock();//��ȡд����
	protected int cacheSize;//�����С
	protected boolean existCustomExpire;//�Ƿ�����Ĭ�Ϲ���ʱ��
	protected long defaultExpire;//Ĭ�Ϲ���ʱ��,0 -> ��������
	
	public CacheManage(int cacheSize,long defaultExpire){
		this.cacheSize = cacheSize;
		this.defaultExpire = defaultExpire;
	}
	
	/**
	 * �Ƿ���Ҫ������ڵĶ���
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 ����01:56:02
	 */
	protected boolean isNeedClearExpireObject(){
		return defaultExpire > 0 || existCustomExpire;
	}
	
	@Override
	public int size() {
		return cacheMap.size();
	}

	@Override
	public long getDefaultExpire() {
		return defaultExpire;
	}

	@Override
	public void put(K key, V value) {
		put(key, value, defaultExpire);
	}

	@Override
	public void put(K key, V value, long expire) {
		writeLock.lock();
		try {
			CacheObject<K, V> cObject = new CacheObject<K, V>(key,value,expire);
			if(expire != 0){
				existCustomExpire = true;
			}
			if(isFull()){
				eliminate();
			}
			cacheMap.put(key, cObject);
		} finally{
			writeLock.unlock();
		}
	}

	@Override
	public void remove(K key) {
		writeLock.lock();
		try{
			cacheMap.remove(key);
		}finally{
			writeLock.unlock();
		}
	}

	@Override
	public V gerValueByKey(K key) {
		readLock.lock();
		try {
			CacheObject<K, V> cObject = cacheMap.get(key);
			if(cObject == null){
				return null;
			}
			if(cObject.isExpired() == true){
				cacheMap.remove(key);
				return null;
			}
			return cObject.getObject();
		} finally{
			readLock.unlock();
		}
	}

	@Override
	public final int eliminate() {
		writeLock.lock();
		try {
			return eliminateCahe();
		} finally{
			writeLock.unlock();
		}
	}

	/**
	 * ��̭����ľ���ʵ��
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 ����04:26:41
	 */
	protected abstract int eliminateCahe();
	
	@Override
	public boolean isFull() {
		if(cacheSize == 0){/*������*/
			return false;
		}
		return cacheMap.size() >= cacheSize;
	}

	@Override
	public void clear() {
		writeLock.lock();
		try{
			cacheMap.clear();
		}finally{
			writeLock.lock();
		}
	}

	@Override
	public int getCacheSize() {
		return cacheSize;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
}
