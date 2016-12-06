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
 * ������Ҫ�����ʼ��ķ���
 * <p>
 * ��Ҫ������<br>
 * 
 * 
 * @author dingsj 2013-5-14 SendMail
 */
public class SendMail extends MailPort{
	
	/** ����MIME�ʼ����� */
	private MimeMessage message = null;
	
	/**�����ʼ����������ĵ�MimeMultipart�������磺����+����*/
	private MimeMultipart msgMimeMultipart = null;
	
	/**�ʼ����ĵ�MimeBodyPart���󣬸��ʼ����Ŀ�����һ������������ģ��ı����֣�html+��ǶͼƬ��*/
	private MimeBodyPart content= null;
	
	/**�ʼ�������MimeBodyPart����*/
	private MimeBodyPart attbodyPart = null;
	
	/**�ʼ����ĸ������е�MimeMultipart�������������ӵ����Ĳ��֣����磺���ĵ��ı����֣�html+��ǶͼƬ��*/
	private MimeMultipart bodymultipart = null;

	private static SendMail sendMail;
	

	/**
	 * �вι��������ڴ���SendMail�����ʱ��������������������Ҵ���MimeMessage����
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
			System.err.println("��ȡSessionʧ�ܣ�" + e.getMessage());
		}
		try {
			/** ����MimeMessage���󣬺��������������ͨѶ */
			message = new testMimeMessage(session);

			/** ����MimeMultipart������,MimeMultipart����һ�������࣬����MimeBodyPart���͵Ķ��� */
			msgMimeMultipart = new MimeMultipart("mixed");
		} catch (Exception e) {
			System.err.println("����MimeMessage����ʧ��" + e.getMessage());
		}
		/**����ȫ�ֵ�Mimebodypart�����ʼ����ĵ�MimeBodyPart���󣬸��ʼ����Ŀ�����һ�������������*/
		try {
			content = new MimeBodyPart();
			msgMimeMultipart.addBodyPart(content);
		} catch (Exception e) {
			System.err.println("����MimeBodyPart����ʧ��" + e.getMessage());
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
			/**�����ʼ�����*/
			message.setContent(msgMimeMultipart);
			message.saveChanges();
			Transport transport = session.getTransport("smtp");
			transport.connect();
			transport.sendMessage(message,message.getRecipients(RecipientType.TO));
			transport.close();
		} catch (Exception e) {
			System.out.println("�����ʼ�ʧ��"+new Date());
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
	 * �����ʼ����ȼ�
	 * @param priority
	 * @return
	 * @return boolean
	 * @author dingsj
	 * @since 2013-12-9 ����01:11:53
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
 * ���÷���ʱ��
 * @return
 * @return boolean
 * @author dingsj
 * @since 2013-12-9 ����01:09:09
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
	 * �����ռ���
	 * @param addressTo �ռ���
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 ����11:23:53
	 */
	public boolean setAddressTo(String addressTo) {
		try {
			message.setRecipients(RecipientType.TO,InternetAddress.parse(addressTo));
		} catch (Exception e) {
			System.err.println("���÷�����ʧ��" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * ���ó�����
	 * @param  address  �����ַ
	 * @return
	 * @author dingsj
	 * @since 2014-07-01
	 */
	public boolean setCC(String address){
		try {
			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(address));
		} catch (Exception e) {
			System.err.println("���ó�����ʧ��" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * ����������
	 * @param  address  �����ַ
	 * @return
	 * @author dingsj
	 * @since 2014-07-01
	 */
	public boolean setBcc(String address){
		try {
			message.setRecipients(Message.RecipientType.BCC,InternetAddress.parse(address));
		} catch (Exception e) {
			System.err.println("����������" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * �����ʼ��ͻ���
	 * @return
	 * @return boolean
	 * @author dingsj
	 * @since 2013-12-9 ����11:28:13
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
	 * �����ʼ�����
	 * 
	 * @param subject
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 ����11:32:31
	 */
	public boolean setSubject(String subject) {
		if (subject == null)
			subject = "������";
		try {
			message.setSubject(subject);
		} catch (Exception e) {
			System.err.println("��������ʧ�ܣ�" + e.getMessage());
			return false;
		}

		return true;
	}

	/**
	 * ���÷�����
	 * @param addressFrom   �����ַ
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 ����11:36:00
	 */
	public boolean setAddressFrom(String addressFrom) {
		try {
			message.setFrom(new InternetAddress(addressFrom));
		} catch (Exception e) {
			System.err.println("���÷�����ʧ�ܣ�" + e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * ���÷�����
	 * @param addressFrom
	 * @param name �Ѻ�����
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 ����11:36:00
	 */
	public boolean setAddressFrom(String addressFrom,String name) {
		try {
			if (name ==null || "".equals(name)) {
				setAddressFrom(addressFrom);
			}else{
				message.setFrom(new InternetAddress(addressFrom, MimeUtility.encodeWord(name)));
			}
		} catch (Exception e) {
			System.err.println("���÷�����ʧ�ܣ�" + e.getMessage());
			return false;
		}
		return true;
	}
	
	

	/**
	 * �����ʼ�����
	 * 
	 * @param mailBody
	 * @param imgMap
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 ����11:46:58
	 */
	public boolean setMailBody(String mailBody, HashMap<String, String> imgMap) {
		try {
			bodymultipart = new MimeMultipart("related");
			content.setContent(bodymultipart);
			/** ����һ������HTML���ݵ�BodyPart */
			MimeBodyPart htmlPart = new MimeBodyPart();
			bodymultipart.addBodyPart(htmlPart);
			String bodyHtml = "<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody;
			htmlPart.setContent(bodyHtml,"text/html;charset=UTF-8");
			/** ���������е�ͼƬ */
			setImg(bodymultipart, imgMap);
		} catch (Exception e) {
			System.err.println("�����ʼ�����ʱ��������" + e);
			return false;
		}
		return true;
	}

	/**
	 * �����ʼ������е�ͼƬ
	 * 
	 * @param multipart
	 * @param imgMap
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 ����09:29:18
	 */
	public boolean setImg(MimeMultipart bodyMultipart,
			HashMap<String, String> imgMap) {
		/** foeach ѭ�����������е�ͼƬ */
		for (Entry<String, String> entry : imgMap.entrySet()) {
			try {
				/** ����MimeBodyPart���� */
				MimeBodyPart picPart = new MimeBodyPart();
				bodyMultipart.addBodyPart(picPart);
				/** �õ�����Դ */
				FileDataSource fSource = new FileDataSource(entry.getValue());
				/** �õ�ͼƬ����������MimeBodyPart�� */
				picPart.setDataHandler(new DataHandler(fSource));
				picPart.setDescription("");
				/** ����Content-ID */
				picPart.setContentID(entry.getKey());
				/** ����ͼƬͷ 
				picPart.setHeader("Content-id",entry.getValue());*/
			} catch (MessagingException e) {
				System.err.println("���������е�ͼƬ����" + entry.getKey()
						+ e.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * ���ø���
	 * @param attachMap
	 * @return boolean
	 * @author dingsj
	 * @since 2013-6-19 ����11:56:58
	 */
	public boolean setAttach(HashMap<String, String> attachMap) {
		/** ѭ�������� */
		for (Entry<String, String> entry : attachMap.entrySet()) {
			try {
				/** ����MimeBodyPart���� */
				attbodyPart = new MimeBodyPart();
				/** �õ�����Դ */
				FileDataSource fSource = new FileDataSource(entry.getValue());
				/** �õ���������������MimeBodyPart�� */
				attbodyPart.setDataHandler(new DataHandler(fSource));
				/** ���ø������� */
				attbodyPart.setFileName(MimeUtility.encodeText(entry.getKey()));
				/** ���ø���ͷ */
				attbodyPart.setHeader("content-id", fSource.getName());
				/** ��Ӹ��� */
				msgMimeMultipart.addBodyPart(attbodyPart);
			} catch (Exception e) {
				System.err.println("�����ʼ�������" + entry.getKey() + "��������" + e);
				return false;
			}
		}
		System.out.println("�����������");
		return true;
	}
	
	/**
     * �����ʼ��ظ���ַ
     * @param replyAdd   �ʼ��ظ���ַ
     * @return boolean
     * @author dingsj
     * @since 2013-11-5 ����02:47:10
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
     * �����ʼ��ظ���ַ
     * @param name
     * @param replyAdd
     * @return boolean
     * @author dingsj
     * @since 2013-11-5 ����03:01:57
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
	 * ����Ϊeml�ļ�
	 * @param filePath  �洢·���ԡ�\������
	 * @param fileName  �ļ�����
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
