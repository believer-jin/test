package cn.entersoft.mail.ding;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 该类主要用于邮件的发送
 * <p>
 * 主要方法：<br>
 * send():邮件的发送 Email_Autherticator：内部类--用于用户邮箱登录验证 getSession:获取session对象
 * 
 * @author dingsj 2013-5-14 SendMail
 */
public class SendMail1 {

	/**
	 * 发送方法 ,以文本格式发送邮件
	 * 
	 * @author dingsj
	 * @param MailInfo
	 * @return void
	 * @since 2013-5-14
	 */
	public void sendText(MailInfo mailInfo) {
		String host = mailInfo.getMailServerHost();
		boolean validate = mailInfo.isValidate();
		String userName = mailInfo.getUserName();
		String passWord = mailInfo.getPassWord();
		String mail_to = mailInfo.getMail_To();
		String mail_from = mailInfo.getMail_From();
		String mail_subject = mailInfo.getMail_subject();
		String mail_body = mailInfo.getMail_body();
		try {
			/** 获取session */
//			MailUtil mUtil = new MailUtil(new MailParam());
			MailUtil mUtil = null;
			Session session = mUtil.getSession();

			/** deBug模式 */
			session.setDebug(false);

			/** 和邮箱服务器进行通讯 */
			MimeMessage message = new MimeMessage(session);

			Address address = new InternetAddress(mail_from);// 发件人地址
			Address toAddress = new InternetAddress(mail_to);// 收件人地址

			/** 设置邮件内容 */
			message.setFrom(address);// 设置发件人地址
			message.addRecipient(Message.RecipientType.TO, toAddress);// 设置收件人地址
			message.setSentDate(new Date());// 设置发送时间
			message.setSubject(mail_subject);// 设置邮件主题
			message.setText(mail_body);// 设置邮件正文
			System.out.println("文本格式发送正文：" + mail_body);
			/** 发送邮件 */
			Transport.send(message);
			System.out.println("文本格式邮件发送成功！");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//

	}

	/**
	 * 发送方法 ,以HTML格式发送邮件
	 * 
	 * @author dingsj
	 * @param MailInfo
	 * @return void
	 * @since 2013-5-14
	 */
	public void sendHtml(MailInfo mailInfo) {
		/**从mainInfo中取出对应的参数*/
		String host = mailInfo.getMailServerHost();// 邮箱服务器
		boolean validate = mailInfo.isValidate();// 是否需要身份验证
		String userName = mailInfo.getUserName();// 邮箱登陆用户名
		String passWord = mailInfo.getPassWord();// 邮箱登陆密码
		String mail_to = mailInfo.getMail_To();// 收件人
		String mail_from = mailInfo.getMail_From();// 发件
		String mail_subject = mailInfo.getMail_subject();// 邮件主题
		String mail_body = mailInfo.getMail_body();// 邮件正文
		ArrayList<String> mail_attach = mailInfo.getMail_attach();// 邮件附件
		ArrayList<String> mail_picture = mailInfo.getMail_picture();//邮件图片
		try {
			/** 获取session */
//			MailUtil mUtil = new MailUtil(new MailParam());
			MailUtil mUtil = null;
			Session session = mUtil.getSession();

			/** deBug模式 */
//			session.setDebug(true);
			
			/***/
			Transport transport = null;

			/** 和邮箱服务器进行通讯 */
			MimeMessage message = new MimeMessage(session);

			Address address = new InternetAddress(mail_from);// 发件人地址
			Address toAddress = new InternetAddress(mail_to);// 收件人地址

			/**
			 * 将文本格式的正文转换成HTML格式 MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象
			 */
			Multipart multipart = new MimeMultipart("related");
			/** 创建一个包含HTML内容的MimeBodyPart */
			BodyPart bPart = new MimeBodyPart();
			/** 设置HTML内容 */
			bPart.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>"+mail_body.toString(), "text/html; charset=utf-8");
			
			multipart.addBodyPart(bPart);
			
			/**附件不为空*/
			if(!mail_attach.isEmpty()){
				for (int i = 0; i < mail_attach.size(); i++) {
					bPart = new MimeBodyPart();
					/**取出每一个附件*/
					String fileName = mail_attach.get(i).toString();
					/**得到数据源*/
					FileDataSource fSource = new FileDataSource(fileName);
					/**得到附件本身，并放入BodyPart中*/
					bPart.setDataHandler(new DataHandler(fSource));
					/**得到文件名，并放入BodyPart中*/
					bPart.setFileName(fSource.getName());
					multipart.addBodyPart(bPart);
				}
			}
			
			/**邮件图片不为空*/
			if(!mail_picture.isEmpty()){
				for (int i = 0; i < mail_picture.size(); i++) {
					bPart = new MimeBodyPart();
					/**取出每一张图片*/
					String pictureName = mail_picture.get(i).toString();
					/**得到数据源*/
					FileDataSource fSource = new FileDataSource(pictureName);
					/**得到图片本身，并放入BodyPart中*/
					bPart.setDataHandler(new DataHandler(fSource));
					/**得到图片名，并放入BodyPart中*/
					bPart.setFileName(fSource.getName());
					System.out.println("添加的图片的名字："+fSource.getName());
					bPart.setHeader("content-id", fSource.getName());
					System.out.println("邮件正文中图片content-id："+ fSource.getName());
					multipart.addBodyPart(bPart);
				}
			}

			/** 设置邮件内容 */
			message.setFrom(address);// 设置发件人地址
			message.addRecipient(Message.RecipientType.TO, toAddress);// 设置收件人地址
			message.setSentDate(new Date());// 设置发送时间
			message.setSubject(mail_subject);// 设置邮件主题
			message.setContent(multipart);// 设置邮件正文
			System.out.println("HTML格式发送正文：" + multipart);
			/** 发送邮件 */
			message.saveChanges();
			transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			System.out.println("HTML格式邮件发送成功！");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		
	}


}
