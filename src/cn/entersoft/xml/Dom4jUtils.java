package cn.entersoft.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

/**
 * dom4j����xml�ļ�ѧϰ
 *
 * @author dingsj 2014-1-8
 */
public class Dom4jUtils {

	/**
	 * ����xml�ļ�������map ʹ��List�����ķ�ʽ����xml
	 * 
	 * @return
	 * @author dingsj
	 * @since 2014-1-8 ����11:32:28
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMapForList(String filePath) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			Document document = reader.read(file);
			/** ��ȡ���ڵ� */
			Element root = document.getRootElement();
			/** ��ȡ���ڵ��µ������ӽڵ� */
			List<Element> childElements = root.elements();
			for (Element child : childElements) {
				/** δ֪������������� */
				List<Attribute> attributeList = child.attributes();
				for (Attribute attr : attributeList) {
					System.out.println(attr.getName() + " : " + attr.getValue());
				}
				/** ��֪������������� */
				System.out.println("��֪������id: " + child.attributeValue("id"));
				;

				/** δ֪��Ԫ����������� */
				List<Element> elementList = child.elements();
				for (Element element : elementList) {
					System.out.println(element.getName() + " : " + element.getText());
				}

				/** ��֪��Ԫ���� */
				System.out.println("��֪��Ԫ����title:" + child.elementText("title"));
				System.out.println("��֪��Ԫ����author:" + child.elementText("author"));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * ����xml�ļ�������map ʹ��Iterator�����ķ�ʽ����xml
	 * 
	 * @return
	 * @author dingsj
	 * @since 2014-1-8 ����11:32:28
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMapForIterator(String filePath) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			Document document = reader.read(file);
			/** ��ȡ���ڵ� */
			Element root = document.getRootElement();
			Iterator<Element> iterator = root.elementIterator();
			while (iterator.hasNext()) {
				/** ��ȡ�ӽڵ� */
				Element element = iterator.next();
				/** δ֪������������� */
				Iterator<Attribute> attrIterator = element.attributeIterator();
				while (attrIterator.hasNext()) {
					Attribute attribute = attrIterator.next();
					System.out.println(attribute.getValue());
				}
				/** ��֪�������Ƶ������ */
				System.out.println("��֪������id: " + element.attributeValue("id"));
				/** δ֪Ԫ����������� */
				Iterator<Element> eleIterator = element.elementIterator();
				while (eleIterator.hasNext()) {
					Element e = eleIterator.next();
					System.out.println(e.getName() + " : " + e.getText());
				}
				/** ��֪Ԫ�����Ƶ������ */
				System.out.println("��֪Ԫ����title: " + element.elementText("title"));
				System.out.println("��֪Ԫ����author: " + element.elementText("author"));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * д��һ��xml�ļ������������ļ������ֺ�·��
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param data
	 *            ����
	 * @param fileName
	 *            ��������
	 * @return
	 * @author dingsj
	 * @since 2014-1-8 ����11:37:52
	 */
	public static boolean writeXml(String filePath, String data, String fileName) {
		try {
			/** ����dom4j��document���� */
			Document document = DocumentHelper.createDocument();
			/** ���Ӹ��ڵ�books */
			Element books = document.addElement("books");
			/** �����ӽڵ�book1 */
			Element book1 = books.addElement("book");
			/** Ϊ�ӽڵ����Ԫ��title1,author */
			Element title1 = book1.addElement("title");
			Element author1 = book1.addElement("author");

			Element book2 = books.addElement("book");
			Element title2 = book2.addElement("title");
			Element author2 = book2.addElement("author");

			/** Ϊ�ӽڵ�������� */
			book1.addAttribute("id", "0001");
			/** ΪԪ��������� */
			title1.addText("Thinking in java");
			author1.addText("java");

			book2.addAttribute("id", "0002");
			title2.addText("Thinking in c++");
			author2.addText("c++");

			/** ʵ���������ʽ���� */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** ����������� */
			format.setEncoding("utf-8");
			/** ������Ҫд���File�ļ����� */
			File file = new File(filePath + File.separator + fileName);
			/** ����XMLWriter���󣬹��캯���еĲ���Ϊ��Ҫ������ļ����͸�ʽ */
			XMLWriter writer = new XMLWriter(new FileOutputStream(file), format);
			writer.write(document);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * �����ַ�������xml document����
	 * @param xmlStr ������ַ���
	 * @return
	 * @author dingsj 2015��5��12�� ����9:55:59
	 * @throws DocumentException 
	 */
	public static Document parseXmlFile(String xmlStr) throws DocumentException {
		return DocumentHelper.parseText(xmlStr);
	}
	
	/**
	 * ��xml�ַ���д��xml�ļ�
	 * @param xmlStr ������ַ���
	 * @return   �����ļ�·��
	 * @author dingsj 2015��5��12�� ����9:55:59
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public static String writerXmlFile(String filePath,String fileName,String xmlStr) throws DocumentException, IOException {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8");
		File file_temp = new File(filePath);
		if(! file_temp.exists()){
			file_temp.mkdirs();
		}
		XMLWriter writer = new XMLWriter(new FileOutputStream(new File(filePath + File.separator + fileName+".xml")), format);
		writer.write(parseXmlFile(xmlStr));
		return filePath + File.separator + fileName;
	}
	
	/**
	 * ��ʽ��xml
	 * @param Document
	 * @return
	 * @author dingsj
	 * 2015��5��12�� ����10:07:16
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static String formatXML(String xmlStr) throws IOException, DocumentException{
		StringWriter out = null;
		OutputFormat format = OutputFormat.createPrettyPrint();
		out = new StringWriter();
		XMLWriter writer = new XMLWriter(out,format);
		writer.write(parseXmlFile(xmlStr));
		return out.toString();
	}

	public static void main(String[] args) {
		// System.out.println(writeXml("E:\\xx\\xx", "", "book.xml"));
		// xmlToMapForList("E:\\xx\\xx\\book.xml");
		xmlToMapForIterator("E:\\xx\\xx\\book.xml");

	}
}
