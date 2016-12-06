package cn.entersoft.mail.framework;

import javax.mail.Session;

import cn.entersoft.mail.framework.entity.MailParam;
import cn.entersoft.mail.framework.implementation.SendMail;
import cn.entersoft.mail.framework.session.MailSessionFactory;

public class TestMail {
	/**
	 *∑¢ÀÕ≤‚ ‘ 
	 * @return boolean
	 * @author dingsj
	 * @since 2013-12-20 …œŒÁ11:21:56
	 */
	public static boolean testSend(){
		MailParam mailParam = new MailParam();
		mailParam.setProtocol("smtp");
		mailParam.setValidate(true);
		mailParam.setMailType("S");
		mailParam.setUserName("entertest111@163.com");
		mailParam.setPassWord("enter123");
		mailParam.setServerHost("smtp.163.com");
		mailParam.setServerPort("994");
		Session session = MailSessionFactory.createMailSessionFactory("MailSessionFactory").getMailSession(mailParam);
		session.setDebug(true);
		return new SendMail(session,"smtp").testConnection();
	}
	
	public static void main(String[] args) {
		System.out.println(testSend());
	}
}
