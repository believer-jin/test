package cn.entersoft.mail.ding;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 * mail辅助类<p>
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
	 * @param @param host 邮箱服务器
	 * @param @param userName 用户名
	 * @param @param passWord 用户密码
	 * @param @return
	 * @return Session
	 * @since 2013-5-14
	 */
	private MailUtil(MailParam mailParam) {
			Properties p = new Properties();// 获取系统环境
			Authenticator au = null;// 登录验证
			Session session = null;//创建Session

			/** 邮箱参数设置 */
			p.setProperty("mail.store.protocol", mailParam.getProtocol());
			p.put("mail."+mailParam.getProtocol()+".host",mailParam.getServerHost());
			p.setProperty("mail."+mailParam.getProtocol()+".port", mailParam.getServerPort());// 端口
			/**如果开启SSL---第二种开启方式*/
			if("S".equals(mailParam.getMailType().toString().trim())){
				try {
					MailSSLSocketFactory sf = new MailSSLSocketFactory();
					sf.setTrustAllHosts(true);
					p.setProperty("mail."+mailParam.getProtocol()+".ssl.enable", "true");
//					p.setProperty("mail.imap.ssl.checkserveridentity", "true");
					p.put("mail."+mailParam.getProtocol()+".ssl.socketFactory", sf);  
					p.setProperty("mail."+mailParam.getProtocol()+".ssl.socketFactory.fallback", "false");
				} catch (GeneralSecurityException e) {
					System.out.println("MailSSLSocketFactory异常！");
				}
			
			}else if("T".equals(mailParam.getMailType().toString().trim())){
				p.setProperty("mail.smtp.starttls.enable","true");
			}
			
//			p.setProperty("mail.smtp.connectiontimeout", "120000");
//			p.setProperty("mail.smtp.timeout", "120000");
			
			/** 判断是否需要登录验证 */
			if (mailParam.isValidate()) {
				p.put("mail."+mailParam.getProtocol()+".auth", "true");
				/** 邮箱用户验证 */
				au = new Email_Autherticator(mailParam.getUserName(), mailParam.getPassWord());
				/** 创建Session */
				session = Session.getDefaultInstance(p, au);
			} else {
				p.put("mail."+mailParam.getProtocol()+".auth", "false");
				/** 创建Session */
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
	 * 根据协议和属性获取设置项
	 * @param protocol
	 * @param properties
	 * @return
	 * @return String
	 * @author dingsj
	 * @since 2013-11-12 下午01:50:15
	 */
	public String  getMail_server_key(String protocol,String properties) {
		return "mail."+protocol+"."+properties;
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
