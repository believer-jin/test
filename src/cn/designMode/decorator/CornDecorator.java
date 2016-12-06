package cn.designMode.decorator;

/**
 * Ⱦɫ�����
 * Ⱦɫ��װ����
 * @author dingsj
 *
 */
public class CornDecorator extends AbstractBread{

	public CornDecorator(Bread bread) {
		super(bread);
	}

	public void paint(){
		System.out.println("������ʻƵ�Ⱦɫ��...");
	}

	@Override
	public void kneadFlour() {
		this.paint();
		super.kneadFlour();
	}
	
}
