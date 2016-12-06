package cn.collection.cache;

/**
 * 缓存对象
 *@author dingsj
 *@version 1.0
 *@since 2014-2-20
 */
public class CacheObject<K,V> {
	private K keyK;//key
	private V cacheObject;//value
	private long lastAccess; //最后访问时间
	private long accessCount;//访问次数
	private long ttl;//对象存活时间（time-to-live）
	
	public CacheObject(){
		
	}
	
	public CacheObject(K keyK, V cacheObject, long ttl) {
		this.keyK = keyK;
		this.cacheObject = cacheObject;
		this.lastAccess = System.currentTimeMillis();
		this.ttl = ttl;
	}
	public K getKeyK() {
		return keyK;
	}
	public void setKeyK(K keyK) {
		this.keyK = keyK;
	}
	public V getCacheObject() {
		return cacheObject;
	}
	public void setCacheObject(V cacheObject) {
		this.cacheObject = cacheObject;
	}
	public long getLastAccess() {
		return lastAccess;
	}
	public void setLastAccess(long lastAccess) {
		this.lastAccess = lastAccess;
	}
	public long getAccessCount() {
		return accessCount;
	}
	public void setAccessCount(long accessCount) {
		this.accessCount = accessCount;
	}
	public long getTtl() {
		return ttl;
	}
	public void setTtl(long ttl) {
		this.ttl = ttl;
	}
	
	/**
	 * 缓存对象是否存活
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午01:44:30
	 */
	public boolean isExpired(){
		if(ttl == 0){
			return false;
		}
		return (lastAccess + ttl) < System.currentTimeMillis();
	}
	
	/**
	 * 获取缓存对象
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午01:45:23
	 */
	public V getObject(){
		lastAccess = System.currentTimeMillis();
		accessCount++;
		return cacheObject;
	}
}
