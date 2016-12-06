package cn.entersoft.mail.framework;

import java.util.HashMap;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * �ʼ���ĸ���
 * ��Ҫ�ṩһЩ���û��߳��õ�����
 * @author dingsj
 * @since 2014-07-01
 */
public class MailPort{
		/**Ĭ�ϱ��뷽ʽ*/
		protected final String DEFAULT_CHARSET = "GBK";
		/**session*/
		protected  Session session;
		/**���ʹ������Transport*/
		protected  Transport transport;
		/**����store*/
		protected Store store;
		/**����·��������ͼƬ*/
		protected String attachPath;
		/**��������   key:value(������������·��)*/
		protected HashMap<String, String> attachMap;
		/**����ͼƬ����  key:value(ͼƬ����ͼƬ·��)*/
		protected HashMap<String, String> imgMap;
		/** ����MIME�ʼ����� */
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
		 * �����ļ����֣����ļ����ͺ�׺�ֿ�
		 * @param fileName
		 * @return String[]  0��name|1��suffix
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
