package cn.entersoft.test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.mail.internet.MimeUtility;


public class testss {

	
	public void test1() throws IOException{
		File file = new File("e:/xx/HTML5入门.pdf");
		/*		String[] strs = file.list();
		for(String str : strs){
			if(strs.length <= 0 ){ file.delete();
		
		}
			
			if(strs.length >= 0){
				System.out.println("大于0遍历文件夹："+str);
			}
		}*/
		
		
//		System.out.println("是否是一个目录"+is);
		
		
		InputStream is = new FileInputStream(file);
		OutputStream os = new FileOutputStream(new File("e:/xx/HTML5入门.txt"));
		byte[] b = new byte[512];
		
		int size = is.read(b);
		while(size != -1){
			os.write(b, 0, size);
			size = is.read(b);
		}
		
		
		long time = file.lastModified();
		System.out.println("文件的最后修改时间："+time);
		String s = String.valueOf(time);
		System.out.println("long转String：" + s);
		
		long l = Long.valueOf(s);
		Date date_l = new Date(l);
		System.out.println("将Long转成String，再转成Long，最后转成data： "+date_l);
		
		
		Date date = new Date(time);
		System.out.println("date:___"+date);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println(simpleDateFormat.format(time));
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		System.out.println("getTime："+calendar.getTime());
		
		
	}
	
	private static HashMap<String, String> map = null;
	
	
	public static void test2(String s){
		try {
			String str = new String(s.getBytes("iso-8859-1"),"utf-8");
			System.out.println(str);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void decodeTest(String s){
		try {
			System.out.println(MimeUtility.decodeText(s));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) throws UnsupportedEncodingException{
//		decodeTest("=?gb2312?Q?=D3=A2=B9=FAQUALTEX=B8=C7=D5=C2=C7=A9=D7=D6=B5=C4=B6=A9?==?gb2312?Q?=B5=A5GD13-JUN18-UK13?=");
	}

}
