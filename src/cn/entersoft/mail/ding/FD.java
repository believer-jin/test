package cn.entersoft.mail.ding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class FD {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.host", "smtp.163.com");
		Session session = Session.getInstance(props,
				new Authenticator()
				{
					protected PasswordAuthentication getPasswordAuthentication()
					{
						return new PasswordAuthentication("entertest111","enter123");
					}
				}
		);
		session.setDebug(true);
		
		/*Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress("itcast_xxx@sina.com"));
		msg.setSubject("中文主题");
		msg.setRecipients(RecipientType.TO, 
				InternetAddress.parse("itcast_test@sina.com,itcast_test@sohu.com"));
		msg.setContent("<span style='color:red'>中文呵呵呵</span>", "text/html;charset=gbk");
		
		
		Transport.send(msg);*/
		
		Message msg;
		try {
			msg = new MimeMessage(session,new FileInputStream("src/resource/demo3.eml"));
			Transport.send(msg,InternetAddress.parse("entertest222@163.com"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
