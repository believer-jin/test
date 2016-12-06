package cn.entersoft.generic;

/**
 * 使用了泛型
 *@author dingsj
 *2014-2-12
 */
public class Demo_Gen1<T> {
	private T t;//定义泛型成员变量
	
	public Demo_Gen1(T t){
		this.t = t;
	}
	
	public T getT(){
		return t;
	}
	
	public void setT(T t){
		this.t = t;
	}
	
	public void showType(){
		System.out.println("T的实际类型是："+t.getClass().getName());
	}
}
