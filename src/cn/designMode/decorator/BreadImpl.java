package cn.designMode.decorator;

public class BreadImpl implements Bread {

	@Override
	public void prepair() {
		System.out.println("��ʼ׼������...");
	}

	@Override
	public void kneadFlour() {
		System.out.println("��ʼ����...");
	}

	@Override
	public void toast() {
		System.out.println("��ʼ�����...");
	}

	@Override
	public void process() {
		prepair();
		kneadFlour();
		toast();
	}

}
