package cn.entersoft.mail.framework.implementation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import cn.entersoft.mail.framework.MailPort;

/***
 * �ʼ��Ľ�����
 * @author dingsj 
 * @since 2013-5-31 
 */
public class ReceiveMail extends MailPort{
	
	private StringBuffer bodyContent;
	
	public ReceiveMail(MimeMessage message,String attachPath){
		this.message = message;
		this.attachPath = attachPath;
	}
	
	/**
	 * ��ȡ����
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String getMailTextContent() throws MessagingException, IOException {
		getMailTextContent(message);
		return bodyContent.toString();
	}

	/**
	 * ��ȡ����
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void saveAttachment() throws UnsupportedEncodingException, MessagingException, IOException{
		saveAttachment(message);
	}
	
	/**
	 * �Ƿ��������
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void isContainerAttachment() throws MessagingException, IOException{
		isContainerAttachment(message);
	}
	
	public String getMessageID() throws MessagingException{
		if(message != null){
		 	return message.getMessageID();
		}else {
			return "";
		}
	}

	/**
	 * ����ʼ�����
	 * @author dingsj
	 * @param @param msg �ʼ�����
	 * @return String �������ʼ�����
	 * @param @throws UnsupportedEncodingException
	 * @param @throws MessagingException
	 * @since 2013-5-31
	 */
	public String getSubject()
			throws UnsupportedEncodingException, MessagingException {
		String subject = MimeUtility.decodeText(message.getSubject());
		if (subject == null)
			subject = "";
		else
			subject = subject.toString();
		return subject;
	}

	/**
	 * ��ȡ������
	 * 
	 * @author dingsj
	 * @param msg
	 *            �ʼ�����
	 * @return from(String����) ����+Email��ַ
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @since 2013-5-31
	 */
	public String getFrom()
			throws UnsupportedEncodingException, MessagingException {
		String from = "";

		/** ���ʼ���ȡ�������˵�ַ */
		Address[] addresses = message.getFrom();

		/** ���������Ϊ�� */
		if (addresses.length < 1) {
			throw new MessagingException("û�з�����");
		}

		InternetAddress address = (InternetAddress) addresses[0];
		String person = address.getPersonal();

		/** ����ռ��˲�Ϊ�գ������ */
		if (person != null) {
			person = MimeUtility.decodeText(person).toString().trim();
		} else {
			person = "";
		}

		from = person + "<" + address.getAddress() + ">";

		return from;
	}

	/**
	 * �����ռ������ͣ���ȡ�ʼ��ռ��ˣ����ͣ������͵�ַ������ռ�������Ϊ�գ��������е��ռ���
	 * <p>
	 * Message.RecipientType.TO
	 * </p>
	 * <p>
	 * Message.RecipientType.TO �ռ���
	 * </p>
	 * <p>
	 * Message.RecipientType.CC ����
	 * </p>
	 * <p>
	 * Message.RecipientType.BCC ����
	 * </p>
	 * 
	 * @param msg
	 *            �ʼ�����
	 * @param type
	 *            �ռ�������
	 * @author dingsj
	 * @return String �ռ���
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @since 2013-5-31
	 */
	public String getReceiveAddress(Message.RecipientType type)
			throws MessagingException {
		StringBuffer receiveAddress = new StringBuffer();
		Address[] addresses = null;

		/** ����ռ�������Ϊ�� */
		if (type == null) {
			addresses = message.getAllRecipients();
		} else {
			addresses = message.getRecipients(type);
		}

		/** ����ռ��˵�ַΪ�� */
		if (addresses == null || addresses.length < 1) {
			throw new MessagingException("û���ռ���");
		}

		/** ������ȡ��ÿһ����ַ */
		for (Address address : addresses) {
			InternetAddress internetAddress = (InternetAddress) address;
			receiveAddress.append(internetAddress.toUnicodeString())
					.append(",");
		}

		/** ɾ�����һ������ */
		receiveAddress.deleteCharAt(receiveAddress.length() - 1);

		return receiveAddress.toString();
	}

	/**
	 * @Description: ��ȡ�ʼ�����ʱ��
	 * @param msg
	 *            �ʼ�����
	 * @param pattern
	 *            ���ڸ�ʽ
	 * @return String
	 * @throws MessagingException
	 * @author dingsj
	 * @since 2013-5-31
	 */
	public String getSentDate(String pattern)
			throws MessagingException {
		Date sendDate = message.getSentDate();

		if (sendDate == null) {
			return "";
		}
		if (pattern == null || "".equals(pattern)) {
			pattern = "yyyy��MM��dd�� E HH:mm:ss ";
		}

		return new SimpleDateFormat(pattern).format(sendDate);
	}

	/**
	 * �����Ƿ��������
	 * 
	 * @param @param msg �ʼ�����
	 * @return boolean ���ڷ���true�������ڷ���false
	 * @throws MessagingException
	 * @author dingsj
	 * @throws IOException
	 * @since 2013-5-31
	 */
	private boolean isContainerAttachment(Part part) throws MessagingException,
			IOException {
		boolean flag = false;

		if (part.isMimeType("multipart/*")) {
			MimeMultipart multipart = (MimeMultipart) part.getContent();
			int partCount = multipart.getCount();

			for (int i = 0; i < partCount; i++) {
				BodyPart bPart = multipart.getBodyPart(i);
				String disp = bPart.getDisposition();

				if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT)
						|| disp.equalsIgnoreCase(Part.INLINE))) {
					flag = true;
				} else if (bPart.isMimeType("multipart/*")) {
					flag = isContainerAttachment(bPart);
				} else {
					String contentType = bPart.getContentType();

					if (contentType.indexOf("application") != -1) {
						flag = true;
					}

					if (contentType.indexOf("name") != -1) {
						flag = true;
					}
				}

				if (flag)
					break;
			}
		} else if (part.isMimeType("message/rfc822")) {
			flag = isContainerAttachment((Part) part.getContent());
		}

		return flag;
	}

	/**
	 * �ж��ʼ��Ƿ��Ѷ�
	 * 
	 * @param msg
	 *            �ʼ�����
	 * @return boolean
	 * @throws MessagingException
	 * @author dingsj
	 * @since 2013-5-31
	 */
	public boolean isSeen() throws MessagingException {
		return message.getFlags().contains(Flags.Flag.SEEN);
	}

	/**
	 * �ж��ʼ��Ƿ���Ҫ�Ѷ���ִ
	 * 
	 * @param MinmeMessage
	 *            msg �ʼ�����
	 * @return boolean
	 * @author dingsj
	 * @since 2013-5-31
	 */
	public boolean isReplySign() throws MessagingException {
		boolean replySign = false;
		String[] headers = message.getHeader("Disposition-Notification-To");

		if (headers != null) {
			replySign = true;
		}

		return replySign;
	}

	/**
	 * ����ʼ������ȼ�
	 * 
	 * @param msg
	 *            �ʼ�����
	 * @return String
	 * @throws MessagingException
	 * @author dingsj
	 * @since 2013-5-31
	 */
	public String getPriority() throws MessagingException {
		String priority = "��ͨ";
		String[] headers = message.getHeader("X-Priority");

		if (headers != null) {
			String headerPriority = headers[0];
			if (headerPriority.indexOf("1") != -1
					|| headerPriority.indexOf("High") != -1) {
				priority = "����";
			} else if (headerPriority.indexOf("5") != -1
					|| headerPriority.indexOf("Low") != -1) {
				priority = "��";
			} else {
				priority = "��ͨ";
			}
		}

		return priority;
	}

	/**
	 * ����ʼ��ı�
	 * 
	 * @param part
	 *            �ʼ�����
	 * @param content
	 *            �洢�ʼ��ı����ݵ��ַ���
	 * @return void
	 * @throws MessagingException
	 * @throws IOException
	 * @author dingsj
	 * @since 2013-5-31
	 */
	private void getMailTextContent(Part part)throws MessagingException, IOException {
		/**
		 * ������ı����͵ĵĸ�����ͨ��getContent() ��������ȡ���ı����ݣ����Ը����е��ı�Ҳ������������� ���������Ҫ���ж�
		 */
		boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
		if (part.isMimeType("text/*") && !isContainTextAttach) {
			bodyContent.append(part.getContent().toString());
		} else if (part.isMimeType("message/rfc822")) {
			getMailTextContent((Part) part.getContent());
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				getMailTextContent(bodyPart);
			}
		}
	}

	/**
	 * ���渽��
	 * 
	 * @param part
	 *            �ʼ��ж��������е�����һ�������
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveAttachment(Part part)
			throws UnsupportedEncodingException, MessagingException,IOException {
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // �������ʼ�
			// �������ʼ���������ʼ���
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// ��ø������ʼ�������һ���ʼ���
				BodyPart bodyPart = multipart.getBodyPart(i);
				// ĳһ���ʼ���Ҳ�п������ɶ���ʼ�����ɵĸ�����
				String disp = bodyPart.getDisposition();
				if (disp != null && (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp.equalsIgnoreCase(Part.INLINE))) {
					InputStream is = bodyPart.getInputStream();
					saveFile(is,decodeText(bodyPart.getFileName()));
				} else if (bodyPart.isMimeType("multipart/*")) {
					saveAttachment(bodyPart);
				} else {
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("name") != -1 || contentType.indexOf("application") != -1) {
						saveFile(bodyPart.getInputStream(),decodeText(bodyPart.getFileName()));
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachment((Part) part.getContent());
		}
	}

	@SuppressWarnings("unused")
	private void saveContentImg(Part part){
		try {
			if(part.isMimeType("multipart/*")){
				Multipart multipart = (Multipart) part.getContent(); // �������ʼ�
				// �������ʼ���������ʼ���
				int partCount = multipart.getCount();
				for (int i = 0; i < partCount; i++) {
					// ��ø������ʼ�������һ���ʼ���
					BodyPart bodyPart = multipart.getBodyPart(i);
					String[] contentIDs = bodyPart.getHeader("Content-ID");
					/*���Content-ID��Ϊ�գ��������������ĸ���*/
					if(contentIDs != null && contentIDs.length > 0){
						for(int j = 0; j < contentIDs.length; j++){
							String contentID = contentIDs[j] == null ? "" : contentIDs[j].toString();
							if(checkImgToContent(contentID)){
								InputStream is = bodyPart.getInputStream();
								saveFile(is,decodeText(bodyPart.getFileName()));
							}
						}
					}else{
						saveContentImg(bodyPart);
					}
				}
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �����������Ƿ���contentIDָ��ĸ���
	 * @param contentID 
	 * @return   boolean true���У�false��û��
	 */
	private boolean checkImgToContent(String contentID){
		String content = this.bodyContent.toString();
		/*���contentIDΪ�գ����Ǹ���*/
		if("".equals(contentID.toString().trim())){
			return false;
		}else if(contentID.startsWith("<")){/*���contenID����"<"��ʼ*/
			String contentID_temp = contentID.substring(1,contentID.length()-1);//ȥ��������
			if("".equals(contentID_temp.trim())){
				return false;
			}
			if(content.indexOf("cid:"+contentID_temp) == -1){//������û��contentID
				return false;
			}
			return true;
		}else{
			if(content.indexOf("cid:"+content) == -1){//������û��contentID
				return false;
			}
			return true;
		}
	}
	
	/**
	 * ��ȡ�������е����ݱ�����ָ��Ŀ¼
	 * 
	 * @param is
	 *            ������
	 * @param fileName
	 *            �ļ���
	 * @param destDir
	 *            �ļ��洢Ŀ¼
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveFile(InputStream is,String fileName)throws IOException {
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(new File(this.attachPath + fileName)));
		int len = -1;
		while ((len = bis.read()) != -1) {
			bos.write(len);
			bos.flush();
		}
		bos.close();
		bis.close();
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
			FileOutputStream fos = new FileOutputStream(file,true);
			this.message.writeTo(fos);
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

	
	
	/**
	 * �ı�����
	 * 
	 * @param encodeText
	 *            ����MimeUtility.encodeText(String text)�����������ı�
	 * @return �������ı�
	 * @throws UnsupportedEncodingException
	 */
	public String decodeText(String encodeText) throws UnsupportedEncodingException {
		if (encodeText == null || "".equals(encodeText)) {
			return "";
		} else {
			return MimeUtility.decodeText(encodeText);
		}
	}
}
