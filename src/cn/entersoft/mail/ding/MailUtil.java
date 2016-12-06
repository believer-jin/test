package cn.entersoft.mail.ding;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * mail������<p>
 * @author dingsj
 *2013-5-27
*MailUtil
 */
public class MailUtil {
	private  Session session ;
	private static  MailUtil mailUtil;
	
	/**
	 * 
	 * @author dingsj
	 * @param @param host ���������
	 * @param @param userName �û���
	 * @param @param passWord �û�����
	 * @param @return
	 * @return Session
	 * @since 2013-5-14
	 */
	private MailUtil(MailParam mailParam) {
			Properties p = new Properties();// ��ȡϵͳ����
			Authenticator au = null;// ��¼��֤
			Session session = null;//����Session

			/** ����������� */
			p.setProperty("mail.store.protocol", mailParam.getProtocol());
			p.put("mail."+mailParam.getProtocol()+".host",mailParam.getServerHost());
			p.setProperty("mail."+mailParam.getProtocol()+".port", mailParam.getServerPort());// �˿�
			/**�������SSL---�ڶ��ֿ�����ʽ*/
			if("S".equals(mailParam.getMailType().toString().trim())){
				try {
					MailSSLSocketFactory sf = new MailSSLSocketFactory();
					sf.setTrustAllHosts(true);
					p.setProperty("mail."+mailParam.getProtocol()+".ssl.enable", "true");
//					p.setProperty("mail.imap.ssl.checkserveridentity", "true");
					p.put("mail."+mailParam.getProtocol()+".ssl.socketFactory", sf);  
					p.setProperty("mail."+mailParam.getProtocol()+".ssl.socketFactory.fallback", "false");
				} catch (GeneralSecurityException e) {
					System.out.println("MailSSLSocketFactory�쳣��");
				}
			
			}else if("T".equals(mailParam.getMailType().toString().trim())){
				p.setProperty("mail.smtp.starttls.enable","true");
			}
			
//			p.setProperty("mail.smtp.connectiontimeout", "120000");
//			p.setProperty("mail.smtp.timeout", "120000");
			
			/** �ж��Ƿ���Ҫ��¼��֤ */
			if (mailParam.isValidate()) {
				p.put("mail."+mailParam.getProtocol()+".auth", "true");
				/** �����û���֤ */
				au = new Email_Autherticator(mailParam.getUserName(), mailParam.getPassWord());
				/** ����Session */
				session = Session.getDefaultInstance(p, au);
			} else {
				p.put("mail."+mailParam.getProtocol()+".auth", "false");
				/** ����Session */
				session = Session.getDefaultInstance(p, null);
			}
			this.session = session;
			System.out.println(p);
	}


	public static MailUtil getDefaultMailUtil(MailParam mailParam){
		if(mailUtil == null){
			mailUtil =  new MailUtil(mailParam);
		}
		return mailUtil;
	}
	
	public  Session getSession() {
		return this.session;
	}
	
	


	/**
	 * ����Э������Ի�ȡ������
	 * @param protocol
	 * @param properties
	 * @return
	 * @return String
	 * @author dingsj
	 * @since 2013-11-12 ����01:50:15
	 */
	public String  getMail_server_key(String protocol,String properties) {
		return "mail."+protocol+"."+properties;
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
