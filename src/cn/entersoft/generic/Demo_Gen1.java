package cn.entersoft.generic;

/**
 * ʹ���˷���
 *@author dingsj
 *2014-2-12
 */
public class Demo_Gen1<T> {
	private T t;//���巺�ͳ�Ա����
	
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
		System.out.println("T��ʵ�������ǣ�"+t.getClass().getName());
	}
}
