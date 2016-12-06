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
	private static Properties properties = null;//jvamail 属性设置
	private Authenticator au = null;// 登录验证
	private static Map<String, MailSessionFactory> poolMap = new HashMap<String, MailSessionFactory>();
	static{
		MailSessionFactory mailSessionFactory = new MailSessionFactory();
		poolMap.put(mailSessionFactory.getClass().getName(), mailSessionFactory);
	}
	
	/**
	 *私有的构造器
	 */
	private MailSessionFactory(){
		
	}
	
	/**
	 * 静态的工厂方法，返回此类的唯一实例
	 * @param name
	 * @return
	 * @return MailSessionFactory
	 * @author dingsj
	 * @since 2013-12-19 上午11:32:52
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
	 * 设定发送属性
	 * @return Properties
	 * @author dingsj
	 * @since 2013-12-19 上午11:43:55
	 */
	private Properties setMailProperties(MailParam mailParam){
		properties = System.getProperties();//获取系统属性对象
		properties.setProperty("mail.store.protocol", mailParam.getProtocol());//设定协议
		properties.put("mail."+mailParam.getProtocol()+".host",mailParam.getServerHost());//设定服务器
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
			/**设置端口*/
			properties.setProperty("mail."+mailParam.getProtocol()+".port", mailParam.getServerPort());
			properties.setProperty("mail."+mailParam.getProtocol()+".starttls.enable","true");
		}else{
			properties.setProperty("mail."+mailParam.getProtocol()+".port", mailParam.getServerPort());
		}
		properties.setProperty("java.security.debug", "certpath");
		properties.setProperty("javax.net.debug", "trustmanager");
		/** 判断是否需要登录验证 */
		if (mailParam.isValidate()) {
			properties.put("mail."+mailParam.getProtocol()+".auth", "true");
			/** 邮箱用户验证 */
			au = new Email_Autherticator(mailParam.getUserName(), mailParam.getPassWord());
		} else {
			properties.put("mail."+mailParam.getProtocol()+".auth", "false");
		}
        return  properties;
	}
	
	
	/**
	 * 创建Mail  Session对象
	 * @return Session
	 * @author dingsj
	 * @since 2013-12-19 上午11:34:25
	 */
	public Session getMailSession(MailParam mailParam){
		setMailProperties(mailParam);
		return Session.getDefaultInstance(properties, au);
	}
	
	/**
	 * 内部类：用于邮箱用户登录验证
	 * 
	 * @author dingsj 2013-5-14 Email_Autherticator
	 */
	public class Email_Autherticator extends Authenticator {
		private  String userName;
		private String passWord;

		/** 有参构造器，初始化用户名和密码 */
		public Email_Autherticator(String userName, String passWord) {
			this.userName = userName;
			this.passWord = passWord;
		}

		/** 登录验证代码 */
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(userName, passWord);
		}

	}

}
