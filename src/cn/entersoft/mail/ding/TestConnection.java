package cn.entersoft.mail.ding;


import java.util.Date;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;

public class TestConnection {
	
	public static void connection(){
		try{
		Properties properties = System.getProperties();
/*		MailParam mailParam = new MailParam();
		mailParam.setUserName("customerlogin@avantronics.com");
		mailParam.setPassWord("WanCh7741");
		mailParam.setServerHost("mail.avantronics.com");*/
		
//		mailParam.setUserName("entertest111@163.com");
//		mailParam.setPassWord("enter123");
//		mailParam.setServerHost("smtp.163.com");
		
/*		mailParam.setUserName("angel.jinfang@gmail.com");
		mailParam.setPassWord("jinfang1990");
		mailParam.setServerHost("smtp.gmail.com");*/
		
/*		mailParam.setProtocol("smtp");
		mailParam.setValidate(true);
		mailParam.setDebug(true);*/
//		/**改变smtp为ssl证书链接*/
//		properties.setProperty("mail.transport.protocol", "smtps");
//		/**设置开启ssl*/
//		properties.setProperty("mail.smtp.ssl.enable", "true");
//		/**设置启用检查ssl*/
//		properties.setProperty("mail.smtp.ssl.checkserveridentity", "true");
//		properties.setProperty("mail.store.protocol", "smtps");// 协议
//		properties.setProperty("mail.smtps.host", "smtp.163.com");// smtp服务器
//		properties.setProperty("mail.smtps.port", "25");
//		properties.put("mail.smtps.auth", "false");
		/**方法一*********************************************/
/*		MailUtil mailUtil = MailUtil.getDefaultMailUtil(mailParam);
		Session session = mailUtil.getSession();
		session.setDebug(mailParam.isDebug());
		if(mailParam.getProtocol().contains("smtp")){
			Transport transport =  session.getTransport(mailParam.getProtocol());
			transport.connect(mailParam.getUserName(),mailParam.getPassWord());
		}else{
			Store store = session.getStore(mailParam.getProtocol());
			System.out.println("--------------------------------");
			store.connect(mailParam.getUserName(),mailParam.getPassWord());
		}*/
		/**************************************************/
		
		/**方法二*********************************************/
		properties.setProperty("mail.transport.protocol", "smtps");
		properties.setProperty("mail.smtps.host", "smtp.163.com");// smtp服务器
		properties.put("mail.smtps.auth", "false");
		Session session = Session.getDefaultInstance(properties, null);
		session.setDebug(true);
		Transport transport =  session.getTransport("smtps");
		transport.connect("entertest111@163.com","enter123");
		/**************************************************/
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void connection1(){
		try {
			MailParam mailParam = new MailParam();
//			mailParam.setUserName("customerlogin@avantronics.com");
//			mailParam.setPassWord("WanCh7741");
//			mailParam.setServerHost("mail.avantronics.com");
			
//			mailParam.setUserName("angel.jinfang@gmail.com");
//			mailParam.setPassWord("jinfang1990");
//			mailParam.setServerHost("smtp.gmail.com");
			
			mailParam.setUserName("love.famg@gmail.com");
			mailParam.setPassWord("miss@fang0303");
			mailParam.setServerHost("imap.gmail.com");
			
			
			mailParam.setProtocol("imap");
			mailParam.setValidate(true);
			mailParam.setDebug(true);
			mailParam.setMailType("");
			mailParam.setServerPort("143");
			
			MailUtil mailUtil = MailUtil.getDefaultMailUtil(mailParam);
			Session session = mailUtil.getSession();
			session.setDebug(mailParam.isDebug());
			if(mailParam.getProtocol().contains("smtp")){
				Transport transport = session.getTransport(mailParam.getProtocol());
				System.out.println("开始时间："+new Date());
				transport.connect(mailParam.getUserName(),mailParam.getPassWord());
				System.out.println("结束时间："+new Date());
			}else{
				Store store = session.getStore(mailParam.getProtocol());
				System.out.println("开始时间："+new Date());
				store.connect(mailParam.getUserName(),mailParam.getPassWord());
			}
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			System.out.println("结束时间："+new Date());
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			System.out.println("结束时间："+new Date());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
//		connection();
		
		connection1();
	}
}
