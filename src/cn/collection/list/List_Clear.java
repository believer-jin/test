package cn.collection.list;

import java.util.ArrayList;
import java.util.List;

public class List_Clear {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		System.out.println("�ڴ濪ʼʹ������"+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
		System.out.println("�ڴ濪ʼʹ������"+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB");
		for(int j = 0; j <= 100; j++){
			list.add("�񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ���������Ƿ�񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ���������ִ� ��������"
					+ "�񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������"
			+ "�񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������"
			+ "�񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������"
					+ "�񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������񶨾�·����������۰������������ͷ�����������flash���� ¥���ȵ����� ���ִ� ��������");
			System.out.println("�ڴ�ʹ������"+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
			System.out.println("�ڴ�ʹ������"+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB");
		}
		System.out.println("�ڴ�ʹ������"+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
		System.out.println("�ڴ�ʹ������"+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB");
		System.out.println("clear֮ǰ����������"+list.size());
		System.out.println("clear֮ǰ����������"+list.toString());
		list.clear();
		System.out.println("claer֮��......"+list.toString());
		System.out.println("�ڴ����ʹ������"+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
		System.out.println("�ڴ����ʹ������"+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB");
		list = null;
	}

}
