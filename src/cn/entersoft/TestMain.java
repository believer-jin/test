package cn.entersoft;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;


public class TestMain {
	private static int count = 0;
	private static int getAttachSize(String pathString,String c_filenam){
		int result = 0;
	  	  StringTokenizer st = new StringTokenizer(c_filenam,";");
	  	  File file = null;
		  while(st.hasMoreTokens()){
			 String straffix=st.nextToken();
			 file = new File(pathString+straffix);
			 if(file.exists()){
				result += file.length();
			 }
		  }	
		  return result;
	  }
	
	private static int getAttachSize1(String pathString,String c_filenam){
		int result = 0;
	  	  StringTokenizer st = new StringTokenizer(c_filenam,";");
	  	  File file = null;
	  	  FileInputStream fis = null;
		  while(st.hasMoreTokens()){
			 String straffix=st.nextToken();
			 file = new File(pathString+straffix);
			 if(file.exists()){
				 try {
					fis = new FileInputStream(file);
					 result = result + fis.available();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
		  }	
		  return result;
	  }
	
	private static String testBase(){
		System.out.println("count:"+count);
		String str = "fafs1adfas";
		try {
			if(count == 0){
				str = str.substring(20);
			}else{
				str = str.substring(20);
			}
			if(count < 1){
				count ++;
				str = testBase();
			}
		} catch (Exception e) {
			System.out.println("^^^^^^^^^^^^^^^^^^^^");
			e.printStackTrace();
		}
		return str;
	}
	
	public static void testSub(){
		String ss = "E:\\workspace_eclipse\\apache-tomcat-7.0.61\\webapps\\zfsjzx\\controller\\upload\\14352855003352306607.doc";
		String swfDir = ss.substring(ss.lastIndexOf("controller"+File.separator),ss.lastIndexOf(".doc"))+".swf";
		System.out.println(swfDir);
		
		swfDir = ss.substring(ss.lastIndexOf("\\controller\\"),ss.lastIndexOf(".doc"))+".swf";
		swfDir = swfDir.replace("\\", "/");
		System.out.println(swfDir);
	}
	
	public static void main(String[] args) {
//		String s = "¶©ÊéèªºÇºÇ";
//		System.out.println(replaceWithAsterisk(s,1,s.length() - 1));
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			String s1 = "2016-11-01 00:00:00";
			String s2 = "2016-12-28 23:59:59";
			Date d1 = s.parse(s1);
			Date d2 = s.parse(s2);
			System.out.println(s1+"   "+d1.getTime());
			System.out.println(s2+"   "+d2.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
    private static String replaceWithAsterisk(String string, int start, int length) {
        if ((string.length() >= start + length) && string.length() >= start && string.length() >= length) {
            String resultString = string.substring(0, start);
            for (int i = 0; i < length; i++) {
                resultString = resultString + "*";
            }
            return resultString + string.substring(start + length, string.length());
        } else {
            return string;
        }
}
}
