package cn.collection.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * LRU实现cache
 *@author dingsj
 *2014-2-20
 *@param <K>
 *@param <V>
 */
public class LRUCache<K,V> extends CacheManage<K, V>{

	public LRUCache(int cacheSize, long defaultExpire) {
		super(cacheSize, defaultExpire);
		/**
		 * linkedHash已经实现了LRU算法，是通过双向链表来实现，当某个位置被命中，通过调整
		 * 链表的指向将该位置调整到头位置，新加入的内容直接放在链表头，如此一来，最近被命中的内容
		 * 的内容就向链表头移动，需要替换时，链表最后的位置就是最少使用的位置
		 */
		this.cacheMap = new LinkedHashMap<K, CacheObject<K,V>>(cacheSize+1, 1f, true){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Entry<K, CacheObject<K, V>> eldest) {
				return LRUCache.this.removeEldestEntry(eldest);
			}
			
		};
	}

	private boolean removeEldestEntry(Map.Entry<K, CacheObject<K, V>> eldest){
		if(this.cacheSize == 0){
			return false;
		}
		return size() > cacheSize;
	}
	
	/**
	 * 只需要实现清除过期的对象就可以了，linkedHashMap已经实现了LRU
	 */
	@Override
	protected int eliminateCahe() {
		if(!isNeedClearExpireObject()){
			return 0;
			}
		Iterator<CacheObject<K, V>> iterator = cacheMap.values().iterator();
		int count = 0;
		while(iterator.hasNext()){
			CacheObject<K, V> cObject = iterator.next();
			if(cObject.isExpired()){
				iterator.remove();
				count++;
			}
		}
		return count;
	}

}
