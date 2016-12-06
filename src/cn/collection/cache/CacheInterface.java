package cn.collection.cache;

public interface CacheInterface<K,V> {
	/**
	 * 返回当前缓存的大小
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午01:26:08
	 */
	public int size();
	
	/**
	 * 返回默认过期时间
	 * @author dingsj
	 * @since 2014-2-20 下午01:27:00
	 */
	public long getDefaultExpire();
	
	/**
	 * 向缓存中添加数据,默认存活时间
	 * @param key
	 * @param value
	 * @author dingsj
	 * @since 2014-2-20 下午01:28:16
	 */
	public void put(K key,V value);
	
	/**
	 * 向缓存中添加数据，指定其存活时间
	 * @param key
	 * @param value
	 * @param expire 过期时间
	 * @author dingsj
	 * @since 2014-2-20 下午01:29:22
	 */
	public void put(K key,V value,long expire);
	
	/**
	 * 根据key移除指定元素
	 * @param key
	 * @author dingsj
	 * @since 2014-2-20 下午01:28:51
	 */
	public void remove(K key);
	
	/**
	 * 查找缓存对象
	 * @param key
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午01:30:27
	 */
	public V gerValueByKey(K key);
	
	/**
	 * 淘汰对象
	 * @return 被删除对象的大小
	 * @author dingsj
	 * @since 2014-2-20 下午01:31:53
	 */
	public int eliminate();
	
	/**
	 * 缓存是否已经满了
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午01:32:30
	 */
	public boolean isFull();
	
	/**
	 * 清空缓存
	 * 
	 * @author dingsj
	 * @since 2014-2-20 下午01:33:02
	 */
	public void clear();
	
	/**
	 * 返回缓存大小
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午01:33:25
	 */
	public int getCacheSize();
	
	/**
	 * 缓存中是否为空
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 下午01:34:15
	 */
	public boolean isEmpty();
}
