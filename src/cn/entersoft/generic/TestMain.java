package cn.entersoft.generic;


import org.junit.Test;

public class TestMain {
	@Test
	public void testDemo_list(){
		Demo_list<String> dList = new Demo_list<String>(10);
		dList.add("你好！");
		dList.add("世界");
		dList.add("中国");
		dList.add("宇宙");
		dList.remove("你好！");
		System.out.println(dList.get(0));
	}
	
	@Test
	public void testDemo_Gen1(){
		/*定义了泛型类Demo_Gen1的一个Integer版本*/
		Demo_Gen1<Integer> dGen1 = new Demo_Gen1<Integer>(88);
		dGen1.showType();
		int i = dGen1.getT();
		System.out.println("value："+i);
		System.out.println("---------------------------------------------------");
		/*定义了泛型类Demo_Gen1的一个String版本*/
		Demo_Gen1<String> dGen12 = new Demo_Gen1<String>("hello,world!");
		dGen12.showType();
		String str = dGen12.getT();
		System.out.println("value："+str);	
	}
	
	@Test
	public void testDemo_Gen2(){
		/*定义了泛型类Demo_Gen1的一个Integer版本*/
		Demo_Gen2 dGen1 = new Demo_Gen2(new Integer(88));
		dGen1.showType();
		int i = (Integer) dGen1.getOb();
		System.out.println("value："+i);
		System.out.println("---------------------------------------------------");
		/*定义了泛型类Demo_Gen1的一个String版本*/
		Demo_Gen2 dGen12 = new Demo_Gen2("hello,world!");
		dGen12.showType();
		String str = (String) dGen12.getOb();
		System.out.println("value："+str);	
	}
}
