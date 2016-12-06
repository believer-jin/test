package cn.entersoft.proxy.impl;

import cn.entersoft.proxy.NetWork;

/**
 *Real 类代表用户上网的实际动作，比如查看网页 
 * @author dingsj
 *2013-12-18
 *Real
 */
public class Real implements NetWork {

	@Override
	public void browser() {
		System.out.println("上网浏览信息!");
	}

	@Override
	public void print(String p) {
		System.out.println("print ： "+p);
	}

}
