package cn.entersoft.mail.ding;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

public class testMimeMessage extends MimeMessage{

	public testMimeMessage(Session session) {
		super(session);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void updateMessageID() throws MessagingException {
		// TODO Auto-generated method stub
		setHeader("Message-ID","<201401151524075459630@gmail.com>");
	}
}
