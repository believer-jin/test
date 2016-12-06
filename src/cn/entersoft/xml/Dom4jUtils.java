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
 * dom4j解析xml文件学习
 *
 * @author dingsj 2014-1-8
 */
public class Dom4jUtils {

	/**
	 * 解析xml文件，返回map 使用List遍历的方式解析xml
	 * 
	 * @return
	 * @author dingsj
	 * @since 2014-1-8 上午11:32:28
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMapForList(String filePath) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			Document document = reader.read(file);
			/** 获取根节点 */
			Element root = document.getRootElement();
			/** 获取根节点下的所有子节点 */
			List<Element> childElements = root.elements();
			for (Element child : childElements) {
				/** 未知属性名的情况下 */
				List<Attribute> attributeList = child.attributes();
				for (Attribute attr : attributeList) {
					System.out.println(attr.getName() + " : " + attr.getValue());
				}
				/** 已知属性名的情况下 */
				System.out.println("已知属性名id: " + child.attributeValue("id"));
				;

				/** 未知子元素名的情况下 */
				List<Element> elementList = child.elements();
				for (Element element : elementList) {
					System.out.println(element.getName() + " : " + element.getText());
				}

				/** 已知子元素名 */
				System.out.println("已知子元素名title:" + child.elementText("title"));
				System.out.println("已知子元素名author:" + child.elementText("author"));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 解析xml文件，返回map 使用Iterator遍历的方式解析xml
	 * 
	 * @return
	 * @author dingsj
	 * @since 2014-1-8 上午11:32:28
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMapForIterator(String filePath) {
		Map<String, String> resultMap = new HashMap<String, String>();
		try {
			SAXReader reader = new SAXReader();
			File file = new File(filePath);
			Document document = reader.read(file);
			/** 获取根节点 */
			Element root = document.getRootElement();
			Iterator<Element> iterator = root.elementIterator();
			while (iterator.hasNext()) {
				/** 获取子节点 */
				Element element = iterator.next();
				/** 未知属性名的情况下 */
				Iterator<Attribute> attrIterator = element.attributeIterator();
				while (attrIterator.hasNext()) {
					Attribute attribute = attrIterator.next();
					System.out.println(attribute.getValue());
				}
				/** 已知属性名称的情况下 */
				System.out.println("已知属性名id: " + element.attributeValue("id"));
				/** 未知元素名的情况下 */
				Iterator<Element> eleIterator = element.elementIterator();
				while (eleIterator.hasNext()) {
					Element e = eleIterator.next();
					System.out.println(e.getName() + " : " + e.getText());
				}
				/** 已知元素名称的情况下 */
				System.out.println("已知元素名title: " + element.elementText("title"));
				System.out.println("已知元素名author: " + element.elementText("author"));
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * 写入一个xml文件，返回生成文件的名字和路径
	 * 
	 * @param filePath
	 *            文件路径
	 * @param data
	 *            数据
	 * @param fileName
	 *            附件名字
	 * @return
	 * @author dingsj
	 * @since 2014-1-8 上午11:37:52
	 */
	public static boolean writeXml(String filePath, String data, String fileName) {
		try {
			/** 创建dom4j的document对象 */
			Document document = DocumentHelper.createDocument();
			/** 增加根节点books */
			Element books = document.addElement("books");
			/** 增加子节点book1 */
			Element book1 = books.addElement("book");
			/** 为子节点添加元素title1,author */
			Element title1 = book1.addElement("title");
			Element author1 = book1.addElement("author");

			Element book2 = books.addElement("book");
			Element title2 = book2.addElement("title");
			Element author2 = book2.addElement("author");

			/** 为子节点添加属性 */
			book1.addAttribute("id", "0001");
			/** 为元素添加内容 */
			title1.addText("Thinking in java");
			author1.addText("java");

			book2.addAttribute("id", "0002");
			title2.addText("Thinking in c++");
			author2.addText("c++");

			/** 实例化输出格式对象 */
			OutputFormat format = OutputFormat.createPrettyPrint();
			/** 设置输出编码 */
			format.setEncoding("utf-8");
			/** 创建需要写入的File文件对象 */
			File file = new File(filePath + File.separator + fileName);
			/** 生成XMLWriter对象，构造函数中的参数为需要输出的文件流和格式 */
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
	 * 解析字符串生成xml document对象
	 * @param xmlStr 输入的字符串
	 * @return
	 * @author dingsj 2015年5月12日 上午9:55:59
	 * @throws DocumentException 
	 */
	public static Document parseXmlFile(String xmlStr) throws DocumentException {
		return DocumentHelper.parseText(xmlStr);
	}
	
	/**
	 * 将xml字符串写入xml文件
	 * @param xmlStr 输入的字符串
	 * @return   返回文件路径
	 * @author dingsj 2015年5月12日 上午9:55:59
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
	 * 格式化xml
	 * @param Document
	 * @return
	 * @author dingsj
	 * 2015年5月12日 上午10:07:16
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
