package cn.entersoft.proxy;

import cn.entersoft.proxy.impl.LogHandler;
import cn.entersoft.proxy.impl.Real;

public class TestMain {
	public static void main(String[] args) {
//		new Proxy(new Real()).browser();
		Real real = new Real();
		LogHandler handler = new LogHandler(real);
		NetWork proxy = (NetWork) java.lang.reflect.Proxy.newProxyInstance(real.getClass().getClassLoader(), real.getClass().getInterfaces(), handler);
		proxy.print("ALL the test");
		proxy.browser();
	}
}
