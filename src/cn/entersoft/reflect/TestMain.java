package cn.entersoft.reflect;

public class TestMain {
	
	/**
	 * 根据对象获取完整的包名和类名
	 */
	public void test1(){
		Demo1 demo1 = new Demo1();
		System.out.println(demo1.getClass().getName());
	}
	
	
	public static void main(String[] args) {
		
	}
}
