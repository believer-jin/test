package cn.entersoft.threadstudy.threadpool;

import java.util.concurrent.Callable;

public class TestCallable implements Callable<String>{
	private int i ;
	private int j;
	
	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	@Override
	public String call() throws Exception {
		return "i="+i+"___________"+"j="+j;
	}

}
