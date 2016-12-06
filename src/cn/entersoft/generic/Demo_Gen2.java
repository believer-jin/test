package cn.entersoft.generic;

/**
 * 没有使用泛型
 *@author dingsj
 *2014-2-12
 */
public class Demo_Gen2 {

	private Object ob;
	
	public Demo_Gen2(Object ob){
		this.ob = ob;
	}
	
	public Object getOb(){
		return ob;
	}
	
	public void setOb(Object ob){
		this.ob = ob;
	}
	
	public void showType(){
		System.out.println("T的实际类型是："+ob.getClass().getName());
	}
}
