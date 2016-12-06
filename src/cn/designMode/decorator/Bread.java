package cn.designMode.decorator;

public interface Bread {
	
	/**
	 * 准备材料
	 * @author dingsj
	 * 2015年11月26日 下午3:34:40
	 */
	void prepair();
	
	/**
	 * 和面
	 * @author dingsj
	 * 2015年11月26日 下午3:34:40
	 */
	void kneadFlour();
	
	/**
	 * 烤面包
	 * @author dingsj
	 * 2015年11月26日 下午3:34:40
	 */
	void toast();
	
	/**
	 * 加工面包
	 * @author dingsj
	 * 2015年11月26日 下午3:34:40
	 */
	void process();
	
	
}
