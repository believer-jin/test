package cn.designMode.decorator;

public class BreadImpl implements Bread {

	@Override
	public void prepair() {
		System.out.println("开始准备材料...");
	}

	@Override
	public void kneadFlour() {
		System.out.println("开始和面...");
	}

	@Override
	public void toast() {
		System.out.println("开始烤面包...");
	}

	@Override
	public void process() {
		prepair();
		kneadFlour();
		toast();
	}

}
