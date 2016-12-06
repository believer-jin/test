package cn.entersoft.mail.ding;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class dd {
	   	private static final String username = "entertest111";
		private static final String password = "enter123"; 
	    public static void set() throws AddressException, MessagingException{
	    Properties props = new Properties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.starttls.enable","true");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.host", "smtp.163.com");
	    props.put("mail.smtp.port", "465");
	    
//	    props.put("mail.transport.protocol", "smtp");
//	    props.put("mail.smtp.host", "smtp.163.com");  
//	    props.put("mail.smtp.port", "465"); 
//	    props.put("mail.smtp.ssl.enable", true);
//	    props.put("mail.smtp.socketFactory.port", "465");
//	    props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
//	    props.put("mail.smtp.socketFactory.fallback", "false"); 
//	    props.put("mail.smtp.quitwait", "false"); 
//	    props.put("mail.smtp.auth", "true"); 

	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	              protected PasswordAuthentication getPasswordAuthentication() {
	                  return new PasswordAuthentication(username, password);
	              }
	            });
session.setDebug(true);
	    Message message = new MimeMessage(session);
	    message.setFrom(new InternetAddress("entertest111@163.com"));
	    message.setRecipients(Message.RecipientType.TO,
	        InternetAddress.parse("xiaoliy88@126.com"));
	    message.setSubject("Test Mail"); 
	    message.setText("TestMail ");
	    Transport.send(message);
	    }
	    public static void main(String[] args) throws AddressException, MessagingException {
			set();
		}
}
