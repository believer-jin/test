package cn.designMode.decorator;

/**
 * 抽象装饰者
 * @author dingsj
 *
 */
public abstract class AbstractBread implements Bread{
	
	/**
	 * AbstractBread满足了装饰着的要求：
	 * 1.和真实的对象具有相同的接口
	 * 2.包含一个真实对象的引用
	 * 3.接收来自客户端的请求，并将这些请求转发给真实对象
	 */
	
	private final Bread bread;
	

	public AbstractBread(Bread bread) {
		this.bread = bread;
	}

	@Override
	public void prepair() {
		this.bread.prepair();
	}

	@Override
	public void kneadFlour() {
		this.bread.kneadFlour();
	}

	@Override
	public void toast() {
		this.bread.toast();
	}

	@Override
	public void process() {
		prepair();
		kneadFlour();
		toast();
	}
	
}
