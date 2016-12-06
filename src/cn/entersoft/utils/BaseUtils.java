package cn.entersoft.utils;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;

/**
 * 基础辅助类
 * @author dingsj
 *2013-12-11
*BaseUtils
 */
public class BaseUtils {
	
	/**
	 *编码字符串 
	 * @param data  要编码的字符串
	 * @param charset  编码方式
	 * @return String
	 * @author dingsj
	 * @throws UnsupportedEncodingException 
	 * @since 2013-12-11 下午03:39:21
	 */
	public static String  characterString(String data,String charset) throws UnsupportedEncodingException{
		return new String(data.getBytes("iso-8859-1"),charset);
	}
	
	/**
	 * 将一串字符串转换成输入流
	 * @param data
	 * @return InputStream
	 * @author dingsj
	 * @since 2013-12-12 上午10:44:17
	 */
	public static InputStream StringToInputStream(String data){
		if(data != null && !"".equals(data.toString().trim())){
			return  new ByteArrayInputStream(data.getBytes());
		}
		return null;
	}
	
	/**
	 * 将输入流转换成字符串
	 * @param in
	 * @return String
	 * @author dingsj
	 * @since 2013-12-12 上午10:49:21
	 */
	public static String InputStreamToString(InputStream in){
		if(in != null){
			try {
				StringBuffer buffer = new StringBuffer();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
				String temp_line = new String("");
				while((temp_line = bufferedReader.readLine()) != null){
						buffer.append(temp_line);
				}
				return buffer.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	/**
	 * 获取文件编码方式
	 * 利用第三方开源包cpdetector获取文件编码格式
	 * @param filePath   要判断文件编码格式的源文件路径
	 * @return String
	 * @author dingsj
	 * @since 2013-12-12 上午09:17:51
	 */
	public static String getFileCharset(String filePath){
		/**
		 * detector：探查器，它把探测任务交给具体的探测实现类的实例来完。cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法添加进来，例如：
		 * 				ParsingDetector，JChardetFacade，UnicodeDetector，ASCIIDetector
		 * cpDetector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的字符集编码。需要使用第三方架包：antlr.jar,chardet.jar和cpdetector.jar
		 * cpDetector是基于统计学原理的，无法保证完全正确
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/**
		 * JChardetFacade封装了由Mozilla组织提供的Jchardet,它可以完成大多数文件的编码测定。
		 * 所以一般有了这个探测器就可以满足大多数项目的要求，如果你还是不放心，可以多加几个探测器，比如下面的：ASCIIDetector，UnicodeDetector
		 */
		detector.add(JChardetFacade.getInstance());
		/**
		 * ParsingDetector可用于检查HTML，XML等文件或字符流的编码，构造方法中的参数用于指示是否显示探测过程的详细信息。false:不显示
		 */
		detector.add(new ParsingDetector(false));
		
		//ASCIIDetector用于ASCII编码测定
		detector.add(ASCIIDetector.getInstance());
		//UnicodeDetector用于Unicode家族编码的测定
		detector.add(UnicodeDetector.getInstance());
		 
		File file = new File(filePath);
		try {
			Charset charset = detector.detectCodepage(file.toURI().toURL());
			return charset.name();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 获取流的编码方式
	 * 利用第三方开源包cpdetector获取文件编码格式
	 * @param in   要判断编码的流
	 * @return String
	 * @author dingsj
	 * @since 2013-12-12 上午09:17:51
	 */
	public static String getStreamCharset(InputStream in){
		/**
		 * detector：探查器，它把探测任务交给具体的探测实现类的实例来完。cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法添加进来，例如：
		 * 				ParsingDetector，JChardetFacade，UnicodeDetector，ASCIIDetector
		 * cpDetector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的字符集编码。需要使用第三方架包：antlr.jar,chardet.jar和cpdetector.jar
		 * cpDetector是基于统计学原理的，无法保证完全正确
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/**
		 * JChardetFacade封装了由Mozilla组织提供的Jchardet,它可以完成大多数文件的编码测定。
		 * 所以一般有了这个探测器就可以满足大多数项目的要求，如果你还是不放心，可以多加几个探测器，比如下面的：ASCIIDetector，UnicodeDetector
		 */
		detector.add(JChardetFacade.getInstance());
		/**
		 * ParsingDetector可用于检查HTML，XML等文件或字符流的编码，构造方法中的参数用于指示是否显示探测过程的详细信息。false:不显示
		 */
		detector.add(new ParsingDetector(false));
		
		//ASCIIDetector用于ASCII编码测定
		detector.add(ASCIIDetector.getInstance());
		//UnicodeDetector用于Unicode家族编码的测定
		detector.add(UnicodeDetector.getInstance());
		try {
			Charset charset = detector.detectCodepage(in, in.available());
			return charset.name();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
