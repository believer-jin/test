package cn.entersoft.extend;

public class Son extends Father{
public int max = 1;
	public void add(){
		System.out.println("Son");
		System.out.println(this.max);
	}
	
	public void dd(){
		
	}
	
	public static void main(String[] args) {
		Father father = new Father();
//		father.add();
		Father son = new Son();
		((Son) son).dd();
		son.add();
	}
}
