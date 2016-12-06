package cn.entersoft.mail.framework;

import java.util.HashMap;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * 邮件体的父类
 * 主要提供一些公用或者常用的属性
 * @author dingsj
 * @since 2014-07-01
 */
public class MailPort{
		/**默认编码方式*/
		protected final String DEFAULT_CHARSET = "GBK";
		/**session*/
		protected  Session session;
		/**发送传输对象Transport*/
		protected  Transport transport;
		/**接收store*/
		protected Store store;
		/**附件路径，包括图片*/
		protected String attachPath;
		/**附件集合   key:value(附件名：附件路径)*/
		protected HashMap<String, String> attachMap;
		/**正文图片集合  key:value(图片名：图片路径)*/
		protected HashMap<String, String> imgMap;
		/** 整个MIME邮件对象 */
		protected MimeMessage message = null;
		
		public Transport getTransport() {
			return transport;
		}

		public void setTransport(Transport transport) {
			this.transport = transport;
		}

		public Session getSession() {
			return session;
		}
		
		public void setSession(Session session) {
			this.session = session;
		}

		public Store getStore() {
			return store;
		}

		public void setStore(Store store) {
			this.store = store;
		}

		public String getAttachPath() {
			return attachPath;
		}

		public void setAttachPath(String attachPath) {
			this.attachPath = attachPath;
		}

		public HashMap<String, String> getAttachMap() {
			return attachMap;
		}

		public void setAttachMap(HashMap<String, String> attachMap) {
			this.attachMap = attachMap;
		}

		public HashMap<String, String> getImgMap() {
			return imgMap;
		}

		public void setImgMap(HashMap<String, String> imgMap) {
			this.imgMap = imgMap;
		}

		public MimeMessage getMessage() {
			return message;
		}

		public void setMessage(MimeMessage message) {
			this.message = message;
		}
		
		/**
		 * 处理文件名字，将文件名和后缀分开
		 * @param fileName
		 * @return String[]  0：name|1：suffix
		 */
		public String[] dealFileName(String fileName){
			if(fileName.contains(".")){
				String suffix = fileName.substring(fileName.lastIndexOf("."),fileName.length());
				String name = fileName.substring(0,fileName.lastIndexOf("."));
				return new String[]{name,suffix};
			}else{
				return new String[]{fileName};
			}
		} 
}
