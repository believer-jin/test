package cn.entersoft.generic;

/**
 * û��ʹ�÷���
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
		System.out.println("T��ʵ�������ǣ�"+ob.getClass().getName());
	}
}
