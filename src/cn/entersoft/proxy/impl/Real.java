package cn.entersoft.proxy.impl;

import cn.entersoft.proxy.NetWork;

/**
 *Real ������û�������ʵ�ʶ���������鿴��ҳ 
 * @author dingsj
 *2013-12-18
 *Real
 */
public class Real implements NetWork {

	@Override
	public void browser() {
		System.out.println("���������Ϣ!");
	}

	@Override
	public void print(String p) {
		System.out.println("print �� "+p);
	}

}
