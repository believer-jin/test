package cn.collection.cache;

public interface CacheInterface<K,V> {
	/**
	 * ���ص�ǰ����Ĵ�С
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 ����01:26:08
	 */
	public int size();
	
	/**
	 * ����Ĭ�Ϲ���ʱ��
	 * @author dingsj
	 * @since 2014-2-20 ����01:27:00
	 */
	public long getDefaultExpire();
	
	/**
	 * �򻺴����������,Ĭ�ϴ��ʱ��
	 * @param key
	 * @param value
	 * @author dingsj
	 * @since 2014-2-20 ����01:28:16
	 */
	public void put(K key,V value);
	
	/**
	 * �򻺴���������ݣ�ָ������ʱ��
	 * @param key
	 * @param value
	 * @param expire ����ʱ��
	 * @author dingsj
	 * @since 2014-2-20 ����01:29:22
	 */
	public void put(K key,V value,long expire);
	
	/**
	 * ����key�Ƴ�ָ��Ԫ��
	 * @param key
	 * @author dingsj
	 * @since 2014-2-20 ����01:28:51
	 */
	public void remove(K key);
	
	/**
	 * ���һ������
	 * @param key
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 ����01:30:27
	 */
	public V gerValueByKey(K key);
	
	/**
	 * ��̭����
	 * @return ��ɾ������Ĵ�С
	 * @author dingsj
	 * @since 2014-2-20 ����01:31:53
	 */
	public int eliminate();
	
	/**
	 * �����Ƿ��Ѿ�����
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 ����01:32:30
	 */
	public boolean isFull();
	
	/**
	 * ��ջ���
	 * 
	 * @author dingsj
	 * @since 2014-2-20 ����01:33:02
	 */
	public void clear();
	
	/**
	 * ���ػ����С
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 ����01:33:25
	 */
	public int getCacheSize();
	
	/**
	 * �������Ƿ�Ϊ��
	 * @return
	 * @author dingsj
	 * @since 2014-2-20 ����01:34:15
	 */
	public boolean isEmpty();
}
