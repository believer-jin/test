package cn.entersoft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import cn.ding.utils.FileUtils;
import cn.entersoft.test.ThreadTest;


public class TestCollection {
	
	@Test
	/**
	 * ����һ���ַ����Ƿ�ȫ�����������
	 * ���ҽ����ַ���ת��long��
	 */
	public void StringConvertlong(){
		String dd = "dfafsafdsafsdfas-$-=entsoftmailbodypartitionflag=-$-";
		System.out.println(dd.substring(0, dd.indexOf("-$-=entsoftmailbodypartitionflag=-$-")));
	}
	
	@Test
	/**
	 * ������ʽ
	 */
	public void zhengze() throws IOException{
		/**ƥ��ǿ��ұ���������*/
		String str = "\\d*";
		System.out.println("������Ҫ��֤���ַ�����");
		String consoleIn = new BufferedReader(new InputStreamReader(System.in)).readLine();
		Pattern pattern = Pattern.compile(str);
		if(pattern.matcher(consoleIn).matches()){
			System.out.println("SUCCESS");
		}else{
			System.out.println("FAILED");
		}
	}
	
	@Test
	/**
	 * ���ļ��Ĳ���
	 */
	public void file() throws IOException{
		File file = new File("e:/xx");
		/**��ȡ���ļ����µ����ļ���*/
		String[] files = file.list();
		
		/**�������ļ����µ��������ļ�*/
		for(String childFile : files){
			System.out.println(childFile);
		}
		if(files.length <= 0 ){
			System.out.println("This file is  empty��");
		}else{
			System.out.println("This file is not empty��");
		}
		
		/**�ʼ��Ŀ������Ƿ��ı��ʼ�������޸�ʱ��*/
		File file1 = new File("e:/xx/ddddd/�������.txt");
		long time1 = file1.lastModified();
		System.out.println("time1��"+time1);
		File file2 = new File("e:/xx/�������.txt");
		long time2 = file2.lastModified();
		FileInputStream fis = new FileInputStream(file1);
		FileOutputStream oStream = new FileOutputStream(file2);
		byte[] bytes = new byte[1024];
		int len = fis.read(bytes);
		System.out.println("len��"+len);
		while(len != -1){
			System.out.println("ѭ���е�len   ��"+len);
			oStream.write(bytes, 0, len);
			len = fis.read(bytes);
			System.out.println("ѭ���е�len��"+len);
		}
		System.out.println("time2��"+time2);
		
	}
	
	@Test
	public void lastFile(){
		try {
			File file1 = new File("e:\\xx\\9be419dbjw1e8c34ijaqrj211s0lc79h.jpg");
			boolean b = file1.renameTo(new File("e:\\xx\\xx"));
			FileInputStream fiStream = new FileInputStream(file1);
			System.out.println(b);
			FileOutputStream oStream = new FileOutputStream("e:\\xx\\9be419dbjw1e8c34ijaqrj211s0lc79h"+1+".jpg");
			byte[] bytes = new byte[fiStream.available()];
 			int len = fiStream.read(bytes);
 			oStream.write(bytes, 0, len);
 			oStream.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	@Test
	public void sub(){
		String content = FileUtils.readerToFile("f://111.txt");
		String eml = "(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)";// ƥ��Email��ַ��������ʽ
		Pattern p = Pattern.compile(eml);
		Matcher m = p.matcher(content);
		if (m.find()) {
			System.out.println("--------------------------------"+m.group(0));
		}
		
	}

	  public static String getRealMailAddress(String mailAddress){
		  String resultStr = "";
		  /**1.���mailAddress����������ַĬ����;������*/
		  StringTokenizer sTokenizer = new StringTokenizer(mailAddress, ";");
		  while (sTokenizer.hasMoreTokens()) {
			  String address_temp = sTokenizer.nextToken().trim();
			  /**2.��������ַ�а���<>�����ȡ<>�еĵ�ַ*/
			  if(address_temp.contains("<")&&address_temp.contains(">")){
				  /**���resultStr��Ϊ�գ�����Ҫ��������ַ֮�����;*/
				  if("".equals(resultStr)){
					  resultStr += address_temp.substring(address_temp.indexOf("<")+1, address_temp.indexOf(">"));
				  }else{
					  resultStr += ";"+address_temp.substring(address_temp.indexOf("<")+1, address_temp.indexOf(">"));
				  }
			  }else{
				  /**���resultStr��Ϊ�գ�����Ҫ��������ַ֮�����;*/
				  if("".equals(resultStr)){
					  resultStr += address_temp;
				  }else{
					  resultStr += ";"+address_temp;
				  }
			  }
		  }
		  return resultStr;
	  }
	
	@Test
	public void dd(){
		
		String str = "5FVQ62TX-ID8PI2JT-OM33PDGS";
		System.out.println(str.length());
	}
	

	@Test
	/***
	 * java��ȡCPU���к�
	 */
	public void test() throws IOException{
	
		long start = System.currentTimeMillis();
	
		Process process = Runtime.getRuntime().exec(

		        new String[] { "wmic", "cpu", "get", "ProcessorId" });

		process.getOutputStream().close();

		Scanner sc = new Scanner(process.getInputStream());

		String property = sc.next();
	
		String serial = sc.next();

		System.out.println(property + ": " + serial);
	
		System.out.println("time:" + (System.currentTimeMillis() - start));

			
	}
	
	@Test
	/**
	 * javaע�⿪������ȡע��
	 */
	public void AnnotationTest() throws SecurityException, NoSuchMethodException{

		Annotation[] annotations  = new TestCollection().getClass().getMethod("test").getAnnotations();
		System.out.println("���ȣ�"+annotations.length);
		for(Annotation ann : annotations){
			System.out.println(ann.annotationType());
		}
	}
	
	@Test
	/**
	 * <p>replace��replaceAll������</p>
	 *���ַ����а��������ַ�ʱ����:$����ʹ��replaceall���������ã����ǿ���ʹ��replace<br>
	 *replace�Ǹ����ַ���ƥ�䣬�Ǽ򵥵��ַ�ƥ��<br>
	 *replaceAll�Ǹ���������ʽƥ�䣬Ҳ���Լ��ַ�ƥ��
	 */
	public void replaceOrreplaceAllTest(){	
		String firstStr = "cid:635da914$1$1409f05a5c7$Coremail$bright0509$126.com";
		String secondStr = "<img src=\"cid:635da914$1$1409f05a5c7$Coremail$bright0509$126.com\">" ;
/*		System.out.println("firstStr��"+firstStr);
		String temp_str = firstStr.replaceAll("\\$","\\\\");
		System.out.println(temp_str);*/
		String centerStr = "";
		System.out.println(secondStr);
		centerStr = secondStr.replace(firstStr, "D://eee");
		System.out.println(centerStr);
		
	}
	@Test
	public void runThred() throws InterruptedException{
		ThreadTest t = new ThreadTest();
		/*for(int i = 0; i <10; i++){
			System.out.println("ѭ���С�������"+i);
			t.run();
		}*/
		System.out.println("****************************************************");
		ArrayList<Thread> ths = new ArrayList<Thread>();
		for(int i = 0; i <10; i++){
			System.out.println("ѭ���С�������"+i);
			ths.add(new Thread(t));
		}
		System.out.println(ths.size());
		for(int j = 0; j <ths.size(); j++){
			Thread sThread = ths.get(j);
			Thread.sleep(1000);
			sThread.start();
		}
	}

	@Test
	public void replaceTest(){
		Vector<String> vector = new Vector<String>();
		if(!vector.isEmpty()){
			System.out.println("isEmpty:"+vector.get(0));
		}
		if(vector.size() > 0){
			System.out.println("vector.size():"+vector.get(0));
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void paixu(){
		Comparator cmp = Collator.getInstance(Locale.CHINA);
		ArrayList<String> list1 = new ArrayList<String>();
		list1.add("����");
		list1.add("����");
		list1.add("��ë");
		list1.add("aa");
		list1.add("zz");
		list1.add("AA");
		list1.add("XX");
		list1.add("11");
		
		Collections.sort(list1,cmp);
		for(String str1 : list1){
			System.out.println("list:"+str1);
		}
		String[] strs = {"����","����","��ë","aa","zz","AA","XX","11"};
		Arrays.sort(strs,cmp);
		for(String str : strs){
			System.out.println("String[]:"+str);
		}
	}
	
	@Test
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void mapList(){
		Map map = new LinkedHashMap();
		map.put("11",null);
		map.put("22", "bb");
		Iterator e = map.keySet().iterator();
		while(e.hasNext()){
			String name = (String) e.next();
			Object v = map.get(name);
			try {
				System.out.println((String)v);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		TestCollection testCollection = new TestCollection();
		testCollection.mapList();
	}
	
	@Test
	public void test22(){
		JSONObject jObject = new JSONObject();
		try {
			System.out.println();
			jObject.put("mailsmtpsslflg", "T");
			System.out.println(jObject.optString("mailsmtpsslflg"));
			if(jObject.optString("ss") != null){
				System.out.println(jObject.optString("ss"));
				if("".equals(jObject.optString("ss"))){
					System.out.println("��");
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//�趨���ͼ��ܷ�ʽΪTLS
	}
	
	
	/**
	 * i++
	 * ++i
	 * @return void
	 * @author dingsj
	 * @since 2013-12-10 ����10:51:36
	 */
	@Test
	public void testii(){
		int n = 0;
		for(int i = 0; i < n ;i++){
			System.out.println(n);
			n++;
		}
		StringTokenizer stringTokenizer = new StringTokenizer("1;2;3;4;5",";");
		if(stringTokenizer.countTokens() > 0){
			System.out.println(stringTokenizer.countTokens());
		}
		String[] s = {};
		System.out.println(s.length);
	}
	
	@Test
	public void test222(){
		String  str = "1.5";
		str = str.substring(0, str.indexOf("."));
		int count = Integer.parseInt(str);
		System.out.println(count);
	}
	
	@Test
	/**
	 * ����continue,break,return
	 */
	public void test_continue_break_return(){
		/**����continue
		 * ���Խ������������ѭ����ִ����һ��ѭ�������磺
		 * continue֮ǰ��0
		 * continue֮��0
		 * continue֮ǰ��1
		 * continue֮��1
		 * continue֮ǰ��2
		 * continueִ�У����������˱���ѭ����ִ����һ��
		 * continue֮ǰ��3
		 * continue֮��3
		 * */
		for(int i = 0; i <= 5; i++){
			System.out.println("continue֮ǰ��"+i);
			if(i == 2){
				continue;
			}
			System.out.println("continue֮��"+i);
		}
		/**����break
		 * ���Խ������������ѭ���������磺
		 * break֮ǰ��0
		 * break֮��0
		 * break֮ǰ��1
		 * break֮��1
		 * break֮ǰ��2
		 * breakִ�У���������ѭ��
		 * */
		for(int m = 0; m <= 5; m++){
			System.out.println("break֮ǰ��"+m);
			if(m == 2){
				break;
			}
			System.out.println("break֮��"+m);
		}
		/**����return
		 * ���Խ�������أ�����ִ��ѭ���������磺
		 * return֮ǰ��0
		 * return֮��0
		 * return֮ǰ��1
		 * return֮��1
		 * return֮ǰ��2
		 * returnִ�У����أ�����ִ��ѭ��
		 * */
		for(int n = 0; n <= 5; n++){
			System.out.println("return֮ǰ��"+n);
			if(n == 2){
				return;
			}
			System.out.println("return֮��"+n);
		}
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testObject() {
		File file = new File("E:\\xx\\xx");
		System.out.println("separator-------->"+File.separator);
		System.out.println("separatorChar-------->"+File.separatorChar);
		System.out.println("pathSeparator-------->"+File.pathSeparator);
		System.out.println("pathSeparatorChar-------->"+File.pathSeparatorChar);
	}
	
	@Test
	public void testSub(){
		String string =  "20140101232049@163.com";
		System.out.println(System.currentTimeMillis());
		String str = new Timestamp(System.currentTimeMillis()).toString().trim().substring(0,19);
		str=str.replaceAll("-","");
		str=str.replaceAll(" ","");
		str=str.replaceAll(":","");
		System.out.println(str);
		System.out.println(string.substring(string.indexOf("@")));
		double d = Math.random();
		System.out.println(d);
		System.out.println(String.valueOf(Math.random()).substring(2,8));
			long time = System.currentTimeMillis();
			String messageID = new Timestamp(time).toString().trim();
			System.out.println(messageID);
			messageID=messageID.replace("-","");
			messageID=messageID.replace(" ","");
			messageID=messageID.replace(":","");
			messageID=messageID.replace(".","");
			System.out.println(messageID);
			messageID = messageID+String.valueOf(Math.random()).substring(2,6);
			System.out.println(messageID);
	}
	
	@Test
	@SuppressWarnings("unused")
	public void testline(){
		String str = "Abcd";
		String str2 = "ABCd";
		System.out.println("equals:"+str.equals(str2));
		System.out.println("equalsIgnoreCase:"+str.equalsIgnoreCase(str2));
		if( 2 == 1)
			System.out.println(111);
		else if(2 == 2)
			System.out.println(222);
		else if(3 == 3)
			System.out.println(333);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("k", "value");
		map.put("b", new ArrayList<String>());
		System.out.println(map);
		for(Map.Entry<?, ?> entry : map.entrySet() ){
			System.out.println(entry.getKey()+"::"+entry.getValue());
		}
	}
	
	@Test
	public void testsubstring(){
		String str = "lusteelta10@lusteel.com/2123";
		String str_temp = str.substring(0, str.indexOf("/"));
		System.out.println(str.substring(str.indexOf("/")));
		System.out.println(str_temp);
		ArrayList<String> list = new ArrayList<String>();
		if(list != null && list.size() > 0){
			System.out.println(    );
		}else {
			System.out.println("==================");
		}
		StringBuffer sBuffer = new StringBuffer();
		System.out.println(sBuffer.length());
	}
	
	@Test
	public void testFile(){
		File file = new File("E:/Entsoft/EnterDOC/EnterMail/20140219/entertest@163.com/EnterMail.txt");
		File dest = new File("E:/Entsoft/EnterDOC/EnterMail/20140219/entertest@163.com/55524/552552/123");
		FileInputStream fis;
		int x = 0;
		try {
			if(!dest.exists()){
				System.out.println("..........");
				dest.mkdirs();
				return ;
			}
			if(!String.valueOf(x).trim().equals("") && dest.exists()){
				System.out.println("==============");
			}
			fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(dest, true);
			byte[] buf = new byte[512];
			int size = fis.read(buf);
			while (size != -1) {
				fos.write(buf, 0, size);
				size = fis.read(buf);
			}
			fis.close();
			fos.close();
			// ɾ����ԭ�����ļ�����ֹ�ظ��洢
			if (file.exists())
				file.delete();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
