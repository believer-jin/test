package cn.collection.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * LRUʵ��cache
 *@author dingsj
 *2014-2-20
 *@param <K>
 *@param <V>
 */
public class LRUCache<K,V> extends CacheManage<K, V>{

	public LRUCache(int cacheSize, long defaultExpire) {
		super(cacheSize, defaultExpire);
		/**
		 * linkedHash�Ѿ�ʵ����LRU�㷨����ͨ��˫��������ʵ�֣���ĳ��λ�ñ����У�ͨ������
		 * �����ָ�򽫸�λ�õ�����ͷλ�ã��¼��������ֱ�ӷ�������ͷ�����һ������������е�����
		 * �����ݾ�������ͷ�ƶ�����Ҫ�滻ʱ����������λ�þ�������ʹ�õ�λ��
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
	 * ֻ��Ҫʵ��������ڵĶ���Ϳ����ˣ�linkedHashMap�Ѿ�ʵ����LRU
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
