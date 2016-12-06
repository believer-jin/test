package cn.entersoft.generic;

/**
 * 仿照list的使用泛型的集合类
 *@author dingsj
 *2014-2-12
 *@param <V>
 */
public class Demo_list<V> {
	
	private V[] array;
	private int size;
	
/**
 * 构造函数
 * @param capacity
 */
	@SuppressWarnings("unchecked")
	public Demo_list(int capacity){
//		array = new V[capacity];   //错误的方式，原因很复杂
		array = (V[]) new Object[capacity];//构造函数的一种可能的实现（该实现使用集合类所采用的方法
	}
	
	/**
	 * 获取集合类的长度
	 * @return
	 * @author dingsj
	 * @since 2014-2-12 下午02:00:26
	 */
	public int size(){
		return size;
	}
	
	/**
	 * 增加元素
	 * @param value
	 * @author dingsj
	 * @since 2014-2-12 下午01:42:52
	 */
	public void add(V value){
		if(size == array.length){
			throw new IndexOutOfBoundsException(Integer.toString(size));
		}else if(value == null){//控制该集合类不能保存null值
			throw new NullPointerException();
		}
		array[size++] = value;
	}
	
	/***
	 * 移除元素
	 * @param value
	 * @author dingsj
	 * @since 2014-2-12 下午01:46:22
	 */
	public void remove(V value){
		int removalCount = 0;
		/*循环，查找指定的value*/
		for(int i = 0; i < size; i++ ){
			if(array[i].equals(value)){//如果找到指定的的value，则计数器+1
				++removalCount;
			}else if(removalCount > 0){//重新排列数组.将当前的value前移一位。
				array[i - removalCount] = array[i];
				array[i] = null;
			}
		}
		size -= removalCount;
	}
	
	/**
	 * 根据索引获取指定元素
	 * @param index
	 * @return
	 * @author dingsj
	 * @since 2014-2-12 下午02:00:47
	 */
	public V get(int index){
		if(index >= size){
			throw new IndexOutOfBoundsException(Integer.toString(index));
		}
		return array[index];
	}
}
