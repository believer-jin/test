package cn.designMode.decorator;

/**
 * 染色的面包
 * 染色剂装饰者
 * @author dingsj
 *
 */
public class CornDecorator extends AbstractBread{

	public CornDecorator(Bread bread) {
		super(bread);
	}

	public void paint(){
		System.out.println("添加柠檬黄的染色剂...");
	}

	@Override
	public void kneadFlour() {
		this.paint();
		super.kneadFlour();
	}
	
}
