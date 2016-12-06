package cn.entersoft.proxy.impl;

import cn.entersoft.proxy.NetWork;

/**
 * 使用 代理类来完成中间代理工作，屏蔽实现代理细节
 * @author dingsj
 *2013-12-18
*Proxy
 */
public class Proxy implements NetWork {
	private NetWork netWork;
	
	public Proxy(NetWork netWork){
		this.netWork = netWork;
	}

	@Override
	public void browser() {
		// TODO Auto-generated method stub
		checkName();
		this.netWork.browser();
	}
	private void checkName(){
		System.out.println("checkName........");
	}

	@Override
	public void print(String p) {
		// TODO Auto-generated method stub
		
	};
}
