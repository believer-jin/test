package cn.entersoft.mail.ding;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class TestReceive {
	public static void main(String[] args) throws GeneralSecurityException {
//		ReceiveMail receiveMail = new ReceiveMail();
//		receiveMail.receive();
		
		ReceiveMail2 receiveMail2 = new ReceiveMail2();
		receiveMail2.receive();
		
//		
//		try {
//			/**���Խ����ʼ�*/
//			InputStream fis = new FileInputStream("src/resource/����444+ԭʼ+�Լ������Լ�.eml");
//			Session session = Session.getInstance(System.getProperties(),null);
//			MimeMessage message = new MimeMessage(session, fis);
//			Message[] messages = {message};
//			receiveMail.parseMessage(messages);
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
