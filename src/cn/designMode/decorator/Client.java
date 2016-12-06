package cn.designMode.decorator;

/**
 * �ͻ��˵��ó���
 * @author dingsj
 *
 */
public class Client {
	
	public static void main(String[] args) {
		System.out.println("���������ʼ...");
		//������ͨ��������ͷʵ��
		//������Ҫ��װ��װ�Σ��Ķ���ʵ��
		Bread bread = new BreadImpl();
		
		//��ʼ����ͨ���������װ��
		bread = new CornDecorator(bread);
		bread.process();
		System.out.println("�����������...");
	}
}
