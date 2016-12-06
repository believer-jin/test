package cn.entersoft.mail.framework.session;

import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import cn.entersoft.mail.framework.entity.MailParam;

import com.sun.mail.util.MailSSLSocketFactory;

public class MailSessionFactory {
	private static Properties properties = null;//jvamail ��������
	private Authenticator au = null;// ��¼��֤
	private static Map<String, MailSessionFactory> poolMap = new HashMap<String, MailSessionFactory>();
	static{
		MailSessionFactory mailSessionFactory = new MailSessionFactory();
		poolMap.put(mailSessionFactory.getClass().getName(), mailSessionFactory);
	}
	
	/**
	 *˽�еĹ�����
	 */
	private MailSessionFactory(){
		
	}
	
	/**
	 * ��̬�Ĺ������������ش����Ψһʵ��
	 * @param name
	 * @return
	 * @return MailSessionFactory
	 * @author dingsj
	 * @since 2013-12-19 ����11:32:52
	 */
	public static MailSessionFactory createMailSessionFactory(String name){
		if(name == null){
			name = MailSessionFactory.class.getName();
		}
		if(poolMap.get(name) == null){
				poolMap.put(name,new MailSessionFactory());
		}
		return poolMap.get(name);
	}
	
	
	/**
	 * �趨��������
	 * @return Properties
	 * @author dingsj
	 * @since 2013-12-19 ����11:43:55
	 */
	private Properties setMailProperties(MailParam mailParam){
		properties = System.getProperties();//��ȡϵͳ���Զ���
		properties.setProperty("mail.store.protocol", mailParam.getProtocol());//�趨Э��
		properties.put("mail."+mailParam.getProtocol()+".host",mailParam.getServerHost());//�趨������
		if("S".equals(mailParam.getMailType().toString().trim())){
			MailSSLSocketFactory sf;
			try {
				sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
				properties.setProperty("mail."+mailParam.getProtocol()+".ssl.enable", "true");
				properties.put("mail."+mailParam.getProtocol()+".ssl.socketFactory", sf);
				properties.put("mail."+mailParam.getProtocol()+".ssl.port", mailParam.getServerPort());
				properties.setProperty("mail."+mailParam.getProtocol()+".ssl.socketFactory.fallback", "false");
			} catch (GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if("T".equals(mailParam.getMailType().toString().trim())){
			/**���ö˿�*/
			properties.setProperty("mail."+mailParam.getProtocol()+".port", mailParam.getServerPort());
			properties.setProperty("mail."+mailParam.getProtocol()+".starttls.enable","true");
		}else{
			properties.setProperty("mail."+mailParam.getProtocol()+".port", mailParam.getServerPort());
		}
		properties.setProperty("java.security.debug", "certpath");
		properties.setProperty("javax.net.debug", "trustmanager");
		/** �ж��Ƿ���Ҫ��¼��֤ */
		if (mailParam.isValidate()) {
			properties.put("mail."+mailParam.getProtocol()+".auth", "true");
			/** �����û���֤ */
			au = new Email_Autherticator(mailParam.getUserName(), mailParam.getPassWord());
		} else {
			properties.put("mail."+mailParam.getProtocol()+".auth", "false");
		}
        return  properties;
	}
	
	
	/**
	 * ����Mail  Session����
	 * @return Session
	 * @author dingsj
	 * @since 2013-12-19 ����11:34:25
	 */
	public Session getMailSession(MailParam mailParam){
		setMailProperties(mailParam);
		return Session.getDefaultInstance(properties, au);
	}
	
	/**
	 * �ڲ��ࣺ���������û���¼��֤
	 * 
	 * @author dingsj 2013-5-14 Email_Autherticator
	 */
	public class Email_Autherticator extends Authenticator {
		private  String userName;
		private String passWord;

		/** �вι���������ʼ���û��������� */
		public Email_Autherticator(String userName, String passWord) {
			this.userName = userName;
			this.passWord = passWord;
		}

		/** ��¼��֤���� */
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, passWord);
		}

	}

}
