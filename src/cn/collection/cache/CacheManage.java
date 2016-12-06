package cn.collection.cache;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>缓存管理类
 *@author dingsj
 *@version 1.0
 *@since 2014-2-20
 */
public abstract class CacheManage<K,V> implements CacheInterface<K, V>{
	protected  Map<K , CacheObject<K, V>> cacheMap = null;//用于保存缓存数据的map容器
	private final ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();//获取对象锁
	private final Lock readLock = cacheLock.readLock();//获取读取锁
	private final Lock writeLock = cacheLock.writeLock();//获取写入锁
	protected int cacheSize;//缓存大小
	protected boolean existCustomExpire;//是否设置默认过期时间
	protected long defaultExpire;//默认过期时间,0 -> 永不过期
	
	public CacheManage(int cacheSize,long defaultExpire){
		this.cacheSize = cacheSize;
		this.defaultExpire = defaultExpire;
	}
	
	/**
	 * 是否需要清除过期的对象
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午01:56:02
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
	 * 淘汰对象的具体实现
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午04:26:41
	 */
	protected abstract int eliminateCahe();
	
	@Override
	public boolean isFull() {
		if(cacheSize == 0){/*无限制*/
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
