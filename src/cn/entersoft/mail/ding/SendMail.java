package cn.entersoft.mail.ding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import cn.entersoft.mail.framework.MailPort;
import common.utils.Utils;

/**
 * 该类主要用于邮件的发送
 * <p>
 * 主要方法：<br>
 * 
 * 
 * @author dingsj 2013-5-14 SendMail
 */
public class SendMail extends MailPort{
	
	/** 整个MIME邮件对象 */
	private MimeMessage message = null;
	
	/**整个邮件复杂体正文的MimeMultipart对象，例如：正文+附件*/
	private MimeMultipart msgMimeMultipart = null;
	
	/**邮件正文的MimeBodyPart对象，该邮件正文可能是一个复杂体的正文，文本部分（html+内嵌图片）*/
	private MimeBodyPart content= null;
	
	/**邮件附件的MimeBodyPart对象*/
	private MimeBodyPart attbodyPart = null;
	
	/**邮件正文复杂体中的MimeMultipart对象，用来处理复杂的正文部分，例如：正文的文本部分（html+内嵌图片）*/
	private MimeMultipart bodymultipart = null;

	private static SendMail sendMail;
	

	/**
	 * 有参构造器，在创建SendMail对象的时候链接邮箱服务器，并且创建MimeMessage对象
	 * 
	 * @param host
	 * @param userName
	 * @param passWord
	 * @param validate
	 * @param deBug
	 */
	private SendMail(Session session) {
	try {
			this.session = session;
		} catch (Exception e) {
			System.err.println("获取Session失败！" + e.getMessage());
		}
		try {
			/** 创建MimeMessage对象，和邮箱服务器进行通讯 */
			message = new testMimeMessage(session);

			/** 创建MimeMultipart容器类,MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象 */
			msgMimeMultipart = new MimeMultipart("mixed");
		} catch (Exception e) {
			System.err.println("创建MimeMessage对象失败" + e.getMessage());
		}
		/**创建全局的Mimebodypart对象，邮件正文的MimeBodyPart对象，该邮件正文可能是一个复杂体的正文*/
		try {
			content = new MimeBodyPart();
			msgMimeMultipart.addBodyPart(content);
		} catch (Exception e) {
			System.err.println("创建MimeBodyPart对象失败" + e.getMessage());
		}
	}
	
	
	public static SendMail getDefaultSendMail(Session session) {
		if(sendMail == null){
			sendMail = new SendMail(session);
		}
		return sendMail;
	}

	public boolean send(){
		try {
			/**设置邮件正文*/
			message.setContent(msgMimeMultipart);
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(message,message.getRecipients(RecipientType.TO));
			transport.close();
		} catch (Exception e) {
			System.out.println("发送邮件失败"+new Date());
			e.printStackTrace();
		}
		return true;
	}

	
	public MimeMessage getMessage() {
		return message;
	}


	public MimeMultipart getMsgMimeMultipart() {
		return msgMimeMultipart;
	}

	/**
	 * 设置邮件优先级
	 * @param priority
	 * @return
	 * @return boolean
	 * @author dingsj
	 * @since 2013-12-9 下午01:11:53
	 */
	public boolean setPriority(String priority){
		priority = Utils.nullToDefault(priority, "3");
		try {
			message.setHeader("X-Priority",priority);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}   
		return true;
	}
	
	
/**
 * 设置发件时间
 * @return
 * @return boolean
 * @author dingsj
 * @since 2013-12-9 下午01:09:09
 */
	public boolean setSendDate(){
		try {
			message.setSentDate(new Date(System.currentTimeMillis()));
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 设置收件人
	 * @param addressTo 收件人
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 上午11:23:53
	 */
	public boolean setAddressTo(String addressTo) {
		try {
			message.setRecipients(RecipientType.TO,InternetAddress.parse(addressTo));
		} catch (Exception e) {
			System.err.println("设置发件人失败" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 设置抄送人
	 * @param  address  邮箱地址
	 * @return
	 * @author dingsj
	 * @since 2014-07-01
	 */
	public boolean setCC(String address){
		try {
			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(address));
		} catch (Exception e) {
			System.err.println("设置抄送人失败" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 设置密送人
	 * @param  address  邮箱地址
	 * @return
	 * @author dingsj
	 * @since 2014-07-01
	 */
	public boolean setBcc(String address){
		try {
			message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(address));
		} catch (Exception e) {
			System.err.println("设置密送人" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 设置邮件客户端
	 * @return
	 * @return boolean
	 * @author dingsj
	 * @since 2013-12-9 上午11:28:13
	 */
	public boolean setMailer(String mailer){
		try {
			message.setHeader("X-Mailer", mailer);
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	/**
	 * 设置邮件主题
	 * 
	 * @param subject
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 上午11:32:31
	 */
	public boolean setSubject(String subject) {
		if (subject == null)
			subject = "无主题";
		try {
			message.setSubject(subject);
		} catch (Exception e) {
			System.err.println("设置主题失败！" + e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * 设置发件人
	 * @param addressFrom   邮箱地址
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 上午11:36:00
	 */
	public boolean setAddressFrom(String addressFrom) {
		try {
			message.setFrom(new InternetAddress(addressFrom));
		} catch (Exception e) {
			System.err.println("设置发件人失败！" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * 设置发件人
	 * @param addressFrom
	 * @param name 友好名称
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 上午11:36:00
	 */
	public boolean setAddressFrom(String addressFrom,String name) {
		try {
			if (name ==null || "".equals(name)) {
				setAddressFrom(addressFrom);
			}else{
				message.setFrom(new InternetAddress(addressFrom, MimeUtility.encodeWord(name)));
			}
		} catch (Exception e) {
			System.err.println("设置发件人失败！" + e.getMessage());
			return false;
		}
		return true;
	}
	
	

	/**
	 * 设置邮件正文
	 * 
	 * @param mailBody
	 * @param imgMap
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 上午11:46:58
	 */
	public boolean setMailBody(String mailBody, HashMap<String, String> imgMap) {
		try {
			bodymultipart = new MimeMultipart("related");
			content.setContent(bodymultipart);
			/** 创建一个包含HTML内容的BodyPart */
			MimeBodyPart htmlPart = new MimeBodyPart();
			bodymultipart.addBodyPart(htmlPart);
			String bodyHtml = "<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody;
			htmlPart.setContent(bodyHtml,"text/html;charset=UTF-8");
			/** 处理正文中的图片 */
			setImg(bodymultipart, imgMap);
		} catch (Exception e) {
			System.err.println("设置邮件正文时发生错误！" + e);
			return false;
		}
		return true;
	}

	/**
	 * 处理邮件正文中的图片
	 * 
	 * @param multipart
	 * @param imgMap
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 上午09:29:18
	 */
	public boolean setImg(MimeMultipart bodyMultipart,
			HashMap<String, String> imgMap) {
		/** foeach 循环处理正文中的图片 */
		for (Entry<String, String> entry : imgMap.entrySet()) {
			try {
				/** 创建MimeBodyPart对象 */
				MimeBodyPart picPart = new MimeBodyPart();
				bodyMultipart.addBodyPart(picPart);
				/** 得到数据源 */
				FileDataSource fSource = new FileDataSource(entry.getValue());
				/** 得到图片本身，并放入MimeBodyPart中 */
				picPart.setDataHandler(new DataHandler(fSource));
				picPart.setDescription("");
				/** 设置Content-ID */
				picPart.setContentID(entry.getKey());
				/** 设置图片头 
				picPart.setHeader("Content-id",entry.getValue());*/
			} catch (MessagingException e) {
				System.err.println("处理正文中的图片报错！" + entry.getKey()
						+ e.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * 设置附件
	 * @param attachMap
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 上午11:56:58
	 */
	public boolean setAttach(HashMap<String, String> attachMap) {
		/** 循环处理附件 */
		for (Entry<String, String> entry : attachMap.entrySet()) {
			try {
				/** 创建MimeBodyPart对象 */
				attbodyPart = new MimeBodyPart();
				/** 得到数据源 */
				FileDataSource fSource = new FileDataSource(entry.getValue());
				/** 得到附件本身，并放入MimeBodyPart中 */
				attbodyPart.setDataHandler(new DataHandler(fSource));
				/** 设置附件名字 */
				attbodyPart.setFileName(MimeUtility.encodeText(entry.getKey()));
				/** 设置附件头 */
				attbodyPart.setHeader("content-id", fSource.getName());
				/** 添加附件 */
				msgMimeMultipart.addBodyPart(attbodyPart);
			} catch (Exception e) {
				System.err.println("增加邮件附件：" + entry.getKey() + "发生错误！" + e);
				return false;
			}
		}
		System.out.println("附件处理完成");
		return true;
	}
	
	/**
     * 设置邮件回复地址
     * @param replyAdd   邮件回复地址
     * @return boolean
     * @author dingsj
     * @since 2013-11-5 下午02:47:10
     */
    public boolean setReply(String replyAdd){
    	try {
    		InternetAddress[] replAddresses = {new InternetAddress(replyAdd)};
    		message.setReplyTo(replAddresses );
			return true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    
    /**
     * 设置邮件回复地址
     * @param name
     * @param replyAdd
     * @return boolean
     * @author dingsj
     * @since 2013-11-5 下午03:01:57
     */
	public boolean setReply(String name, String replyAdd) {
		try {
			if (name != null && !name.trim().equals("")) {
				name = MimeUtility.encodeWord(name);
	    		InternetAddress[] replAddresses = {new InternetAddress(replyAdd,name)};
	    		message.setReplyTo(replAddresses);
				return true;
			}else{
				return this.setReply(replyAdd);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}
	
	/**
	 * 保存为eml文件
	 * @param filePath  存储路径以“\”结束
	 * @param fileName  文件名字
	 * @return
	 * @author dingsj
	 * @since 2014-07-01
	 */
	public boolean saveMailToFile(String filePath,String fileName){
		File file = null;
		try {
			file = new File(filePath+fileName);
			if(file.exists()) file.delete();
			message.setContent(msgMimeMultipart);
			FileOutputStream fos = new FileOutputStream(file,true);
			message.writeTo(fos);
			fos.close();
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return file.exists();
	}

}
