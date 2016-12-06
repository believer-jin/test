package cn.entersoft.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogHandler implements InvocationHandler{

	private Object object;
	
	public LogHandler(Object object){
		this.object = object;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {
		doBefore();
		Object result = method.invoke(object, args);
		after();
		return result;
	}
	
	private void doBefore(){
		System.out.println("before.....................");
	}
	
	private void after(){
		System.out.println("after........................");
	}

}
