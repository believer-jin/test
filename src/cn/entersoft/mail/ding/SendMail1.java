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
 * ������Ҫ�����ʼ��ķ���
 * <p>
 * ��Ҫ������<br>
 * send():�ʼ��ķ��� Email_Autherticator���ڲ���--�����û������¼��֤ getSession:��ȡsession����
 * 
 * @author dingsj 2013-5-14 SendMail
 */
public class SendMail1 {

	/**
	 * ���ͷ��� ,���ı���ʽ�����ʼ�
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
			/** ��ȡsession */
//			MailUtil mUtil = new MailUtil(new MailParam());
			MailUtil mUtil = null;
			Session session = mUtil.getSession();

			/** deBugģʽ */
			session.setDebug(false);

			/** ���������������ͨѶ */
			MimeMessage message = new MimeMessage(session);

			Address address = new InternetAddress(mail_from);// �����˵�ַ
			Address toAddress = new InternetAddress(mail_to);// �ռ��˵�ַ

			/** �����ʼ����� */
			message.setFrom(address);// ���÷����˵�ַ
			message.addRecipient(Message.RecipientType.TO, toAddress);// �����ռ��˵�ַ
			message.setSentDate(new Date());// ���÷���ʱ��
			message.setSubject(mail_subject);// �����ʼ�����
			message.setText(mail_body);// �����ʼ�����
			System.out.println("�ı���ʽ�������ģ�" + mail_body);
			/** �����ʼ� */
			Transport.send(message);
			System.out.println("�ı���ʽ�ʼ����ͳɹ���");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//

	}

	/**
	 * ���ͷ��� ,��HTML��ʽ�����ʼ�
	 * 
	 * @author dingsj
	 * @param MailInfo
	 * @return void
	 * @since 2013-5-14
	 */
	public void sendHtml(MailInfo mailInfo) {
		/**��mainInfo��ȡ����Ӧ�Ĳ���*/
		String host = mailInfo.getMailServerHost();// ���������
		boolean validate = mailInfo.isValidate();// �Ƿ���Ҫ�����֤
		String userName = mailInfo.getUserName();// �����½�û���
		String passWord = mailInfo.getPassWord();// �����½����
		String mail_to = mailInfo.getMail_To();// �ռ���
		String mail_from = mailInfo.getMail_From();// ����
		String mail_subject = mailInfo.getMail_subject();// �ʼ�����
		String mail_body = mailInfo.getMail_body();// �ʼ�����
		ArrayList<String> mail_attach = mailInfo.getMail_attach();// �ʼ�����
		ArrayList<String> mail_picture = mailInfo.getMail_picture();//�ʼ�ͼƬ
		try {
			/** ��ȡsession */
//			MailUtil mUtil = new MailUtil(new MailParam());
			MailUtil mUtil = null;
			Session session = mUtil.getSession();

			/** deBugģʽ */
//			session.setDebug(true);
			
			/***/
			Transport transport = null;

			/** ���������������ͨѶ */
			MimeMessage message = new MimeMessage(session);

			Address address = new InternetAddress(mail_from);// �����˵�ַ
			Address toAddress = new InternetAddress(mail_to);// �ռ��˵�ַ

			/**
			 * ���ı���ʽ������ת����HTML��ʽ MimeMultipart����һ�������࣬����MimeBodyPart���͵Ķ���
			 */
			Multipart multipart = new MimeMultipart("related");
			/** ����һ������HTML���ݵ�MimeBodyPart */
			BodyPart bPart = new MimeBodyPart();
			/** ����HTML���� */
			bPart.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>"+mail_body.toString(), "text/html; charset=utf-8");
			
			multipart.addBodyPart(bPart);
			
			/**������Ϊ��*/
			if(!mail_attach.isEmpty()){
				for (int i = 0; i < mail_attach.size(); i++) {
					bPart = new MimeBodyPart();
					/**ȡ��ÿһ������*/
					String fileName = mail_attach.get(i).toString();
					/**�õ�����Դ*/
					FileDataSource fSource = new FileDataSource(fileName);
					/**�õ���������������BodyPart��*/
					bPart.setDataHandler(new DataHandler(fSource));
					/**�õ��ļ�����������BodyPart��*/
					bPart.setFileName(fSource.getName());
					multipart.addBodyPart(bPart);
				}
			}
			
			/**�ʼ�ͼƬ��Ϊ��*/
			if(!mail_picture.isEmpty()){
				for (int i = 0; i < mail_picture.size(); i++) {
					bPart = new MimeBodyPart();
					/**ȡ��ÿһ��ͼƬ*/
					String pictureName = mail_picture.get(i).toString();
					/**�õ�����Դ*/
					FileDataSource fSource = new FileDataSource(pictureName);
					/**�õ�ͼƬ����������BodyPart��*/
					bPart.setDataHandler(new DataHandler(fSource));
					/**�õ�ͼƬ����������BodyPart��*/
					bPart.setFileName(fSource.getName());
					System.out.println("��ӵ�ͼƬ�����֣�"+fSource.getName());
					bPart.setHeader("content-id", fSource.getName());
					System.out.println("�ʼ�������ͼƬcontent-id��"+ fSource.getName());
					multipart.addBodyPart(bPart);
				}
			}

			/** �����ʼ����� */
			message.setFrom(address);// ���÷����˵�ַ
			message.addRecipient(Message.RecipientType.TO, toAddress);// �����ռ��˵�ַ
			message.setSentDate(new Date());// ���÷���ʱ��
			message.setSubject(mail_subject);// �����ʼ�����
			message.setContent(multipart);// �����ʼ�����
			System.out.println("HTML��ʽ�������ģ�" + multipart);
			/** �����ʼ� */
			message.saveChanges();
			transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			System.out.println("HTML��ʽ�ʼ����ͳɹ���");
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//
		
	}


}
