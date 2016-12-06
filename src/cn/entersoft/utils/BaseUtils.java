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
 * ����������
 * @author dingsj
 *2013-12-11
*BaseUtils
 */
public class BaseUtils {
	
	/**
	 *�����ַ��� 
	 * @param data  Ҫ������ַ���
	 * @param charset  ���뷽ʽ
	 * @return String
	 * @author dingsj
	 * @throws UnsupportedEncodingException 
	 * @since 2013-12-11 ����03:39:21
	 */
	public static String  characterString(String data,String charset) throws UnsupportedEncodingException{
		return new String(data.getBytes("iso-8859-1"),charset);
	}
	
	/**
	 * ��һ���ַ���ת����������
	 * @param data
	 * @return InputStream
	 * @author dingsj
	 * @since 2013-12-12 ����10:44:17
	 */
	public static InputStream StringToInputStream(String data){
		if(data != null && !"".equals(data.toString().trim())){
			return  new ByteArrayInputStream(data.getBytes());
		}
		return null;
	}
	
	/**
	 * ��������ת�����ַ���
	 * @param in
	 * @return String
	 * @author dingsj
	 * @since 2013-12-12 ����10:49:21
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
	 * ��ȡ�ļ����뷽ʽ
	 * ���õ�������Դ��cpdetector��ȡ�ļ������ʽ
	 * @param filePath   Ҫ�ж��ļ������ʽ��Դ�ļ�·��
	 * @return String
	 * @author dingsj
	 * @since 2013-12-12 ����09:17:51
	 */
	public static String getFileCharset(String filePath){
		/**
		 * detector��̽����������̽�����񽻸������̽��ʵ�����ʵ�����ꡣcpDetector������һЩ���õ�̽��ʵ���࣬��Щ̽��ʵ�����ʵ������ͨ��add������ӽ��������磺
		 * 				ParsingDetector��JChardetFacade��UnicodeDetector��ASCIIDetector
		 * cpDetector���ա�˭���ȷ��طǿյ�̽���������Ըý��Ϊ׼����ԭ�򷵻�̽�⵽���ַ������롣��Ҫʹ�õ������ܰ���antlr.jar,chardet.jar��cpdetector.jar
		 * cpDetector�ǻ���ͳ��ѧԭ��ģ��޷���֤��ȫ��ȷ
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/**
		 * JChardetFacade��װ����Mozilla��֯�ṩ��Jchardet,��������ɴ�����ļ��ı���ⶨ��
		 * ����һ���������̽�����Ϳ�������������Ŀ��Ҫ������㻹�ǲ����ģ����Զ�Ӽ���̽��������������ģ�ASCIIDetector��UnicodeDetector
		 */
		detector.add(JChardetFacade.getInstance());
		/**
		 * ParsingDetector�����ڼ��HTML��XML���ļ����ַ����ı��룬���췽���еĲ�������ָʾ�Ƿ���ʾ̽����̵���ϸ��Ϣ��false:����ʾ
		 */
		detector.add(new ParsingDetector(false));
		
		//ASCIIDetector����ASCII����ⶨ
		detector.add(ASCIIDetector.getInstance());
		//UnicodeDetector����Unicode�������Ĳⶨ
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
	 * ��ȡ���ı��뷽ʽ
	 * ���õ�������Դ��cpdetector��ȡ�ļ������ʽ
	 * @param in   Ҫ�жϱ������
	 * @return String
	 * @author dingsj
	 * @since 2013-12-12 ����09:17:51
	 */
	public static String getStreamCharset(InputStream in){
		/**
		 * detector��̽����������̽�����񽻸������̽��ʵ�����ʵ�����ꡣcpDetector������һЩ���õ�̽��ʵ���࣬��Щ̽��ʵ�����ʵ������ͨ��add������ӽ��������磺
		 * 				ParsingDetector��JChardetFacade��UnicodeDetector��ASCIIDetector
		 * cpDetector���ա�˭���ȷ��طǿյ�̽���������Ըý��Ϊ׼����ԭ�򷵻�̽�⵽���ַ������롣��Ҫʹ�õ������ܰ���antlr.jar,chardet.jar��cpdetector.jar
		 * cpDetector�ǻ���ͳ��ѧԭ��ģ��޷���֤��ȫ��ȷ
		 */
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		/**
		 * JChardetFacade��װ����Mozilla��֯�ṩ��Jchardet,��������ɴ�����ļ��ı���ⶨ��
		 * ����һ���������̽�����Ϳ�������������Ŀ��Ҫ������㻹�ǲ����ģ����Զ�Ӽ���̽��������������ģ�ASCIIDetector��UnicodeDetector
		 */
		detector.add(JChardetFacade.getInstance());
		/**
		 * ParsingDetector�����ڼ��HTML��XML���ļ����ַ����ı��룬���췽���еĲ�������ָʾ�Ƿ���ʾ̽����̵���ϸ��Ϣ��false:����ʾ
		 */
		detector.add(new ParsingDetector(false));
		
		//ASCIIDetector����ASCII����ⶨ
		detector.add(ASCIIDetector.getInstance());
		//UnicodeDetector����Unicode�������Ĳⶨ
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
