package cn.designMode.decorator;

/**
 * 客户端调用程序
 * @author dingsj
 *
 */
public class Client {
	
	public static void main(String[] args) {
		System.out.println("生产面包开始...");
		//创建普通的正常馒头实例
		//这是需要包装（装饰）的对象实例
		Bread bread = new BreadImpl();
		
		//开始对普通的面包进行装饰
		bread = new CornDecorator(bread);
		bread.process();
		System.out.println("生产面包结束...");
	}
}
