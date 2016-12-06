package cn.collection.list;

import java.util.ArrayList;
import java.util.List;

public class List_Clear {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		System.out.println("内存开始使用量："+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
		System.out.println("内存开始使用量："+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB");
		for(int j = 0; j <= 100; j++){
			list.add("否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达是否否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达轮大法 及阿凡达"
					+ "否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达"
			+ "否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达"
			+ "否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达"
					+ "否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达否定句奥帆基地酸辣粉啊附件的拉升客服拉开范德萨flash发的 楼咖啡的拉升 法轮大法 及阿凡达");
			System.out.println("内存使用量："+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
			System.out.println("内存使用量："+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB");
		}
		System.out.println("内存使用量："+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
		System.out.println("内存使用量："+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB");
		System.out.println("clear之前。。。。。"+list.size());
		System.out.println("clear之前。。。。。"+list.toString());
		list.clear();
		System.out.println("claer之后......"+list.toString());
		System.out.println("内存结束使用量："+Runtime.getRuntime().totalMemory()/(1024*1024)+"MB");
		System.out.println("内存结束使用量："+Runtime.getRuntime().freeMemory()/(1024*1024)+"MB");
		list = null;
	}

}
