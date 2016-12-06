package cn.entersoft.generic;

/**
 * ����list��ʹ�÷��͵ļ�����
 *@author dingsj
 *2014-2-12
 *@param <V>
 */
public class Demo_list<V> {
	
	private V[] array;
	private int size;
	
/**
 * ���캯��
 * @param capacity
 */
	@SuppressWarnings("unchecked")
	public Demo_list(int capacity){
//		array = new V[capacity];   //����ķ�ʽ��ԭ��ܸ���
		array = (V[]) new Object[capacity];//���캯����һ�ֿ��ܵ�ʵ�֣���ʵ��ʹ�ü����������õķ���
	}
	
	/**
	 * ��ȡ������ĳ���
	 * @return
	 * @author dingsj
	 * @since 2014-2-12 ����02:00:26
	 */
	public int size(){
		return size;
	}
	
	/**
	 * ����Ԫ��
	 * @param value
	 * @author dingsj
	 * @since 2014-2-12 ����01:42:52
	 */
	public void add(V value){
		if(size == array.length){
			throw new IndexOutOfBoundsException(Integer.toString(size));
		}else if(value == null){//���Ƹü����಻�ܱ���nullֵ
			throw new NullPointerException();
		}
		array[size++] = value;
	}
	
	/***
	 * �Ƴ�Ԫ��
	 * @param value
	 * @author dingsj
	 * @since 2014-2-12 ����01:46:22
	 */
	public void remove(V value){
		int removalCount = 0;
		/*ѭ��������ָ����value*/
		for(int i = 0; i < size; i++ ){
			if(array[i].equals(value)){//����ҵ�ָ���ĵ�value���������+1
				++removalCount;
			}else if(removalCount > 0){//������������.����ǰ��valueǰ��һλ��
				array[i - removalCount] = array[i];
				array[i] = null;
			}
		}
		size -= removalCount;
	}
	
	/**
	 * ����������ȡָ��Ԫ��
	 * @param index
	 * @return
	 * @author dingsj
	 * @since 2014-2-12 ����02:00:47
	 */
	public V get(int index){
		if(index >= size){
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		return array[index];
	}
}
