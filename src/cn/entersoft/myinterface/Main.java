package cn.entersoft.myinterface;

public class Main {
	public static void main(String[] args) {
		Test test = new Test() {
			
			@Override
			public void test_method() {
				System.out.println("ִ��-------------------------------");
				
			}
		};
//		test.test_method();
	}
	
	interface Test{
		public void test_method();
	}
}
