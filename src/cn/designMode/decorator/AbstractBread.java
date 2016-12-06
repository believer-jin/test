package cn.designMode.decorator;

/**
 * ����װ����
 * @author dingsj
 *
 */
public abstract class AbstractBread implements Bread{
	
	/**
	 * AbstractBread������װ���ŵ�Ҫ��
	 * 1.����ʵ�Ķ��������ͬ�Ľӿ�
	 * 2.����һ����ʵ���������
	 * 3.�������Կͻ��˵����󣬲�����Щ����ת������ʵ����
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
