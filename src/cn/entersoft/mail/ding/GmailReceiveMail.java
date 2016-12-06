package cn.entersoft.mail.ding;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.util.MailSSLSocketFactory;

/***
 * �ʼ��Ľ�����
 * 
 * @author dingsj 2013-5-31 ReceiveMail
 */
public class GmailReceiveMail {

	/**
	 * �����ʼ�
	 * 
	 * @author dingsj
	 * @param
	 * @return void
	 * @throws GeneralSecurityException 
	 * @since 2013-5-31
	 */
	public void receive() throws GeneralSecurityException {
		try {
			/** �������Զ��� */
			Properties proerties = new Properties();
			proerties.setProperty("mail.store.protocol", "imap");// Э��
			proerties.setProperty("mail.imap.host", "imap.vip.163.com");// smtp������
			
			//**����SSLѡ��*//*
			if("Y".equals("Y")){
				MailSSLSocketFactory sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
				proerties.setProperty("mail.imap.ssl.enable", "true");
//				proerties.setProperty("mail.imap.ssl.checkserveridentity", "true");
				proerties.put("mail.imap.ssl.socketFactory", sf);  
				proerties.setProperty("mail.imap.port", "993");// �˿�
			}
			/** ����Session���� */
			Session session = Session.getInstance(proerties);
			session.setDebug(true);
			Store store = session.getStore("imap");
			store.connect("janec@vip.163.com", "jane1019");

			/**��ȡ���е��ļ���*/
			Folder[] folders = store.getDefaultFolder().list();
			for(Folder fd : folders){
				System.out.println("�ļ��е�������>>"+fd.getName());
			}
			
			/** ��ȡ�ռ��� */
			Folder folder = store.getFolder("INBOX");

			/**
			 * ���ռ��� Folder.READ_ONLY��ֻ��Ȩ�� Folder.READ_WRITE���ɶ���д�������޸��ʼ���״̬��
			 */
			folder.open(Folder.READ_WRITE);
			/**����Ԥ��ȡ*/
			FetchProfile profile = new FetchProfile();
			/**
			 * ����Ԥ����ֵ��UID.....�ʼ��������ϵ�ÿ���ʼ�Ψһid
			 * ע�⣺�����ʱ��������洢����Ϊ��ͬ������uid���ظ���
			 * */
			profile.add(UIDFolder.FetchProfileItem.UID);
			profile.add(FetchProfile.Item.ENVELOPE);
			/**
			 * ע�ͣ�POP3Э���޷���֪�ʼ���״̬,����getUnreadMessageCount�õ������ռ�����ʼ�����
			 * getDeletedMessageCount()��getNewMessageCount()ʼ����0
			 */
			System.out.println("δ���ʼ�������" + folder.getUnreadMessageCount());
			System.out.println("��ɾ���ʼ�����" + folder.getDeletedMessageCount());
			System.out.println("���ʼ���" + folder.getNewMessageCount());
			System.out.println("�ʼ�������" + folder.getMessageCount());
			System.out.println("====================================================");
			
			/**���folder��IMAPFolder��һ��ʵ��*/
			if(folder instanceof IMAPFolder){
				/**��folderת����IMAPFolder*/
				IMAPFolder imapFolder = (IMAPFolder) folder;
				/** �õ��ռ����е������ʼ��������� */
				Message[] messages = imapFolder.getMessages();
				imapFolder.fetch(messages, profile);
				
				System.out.println("�ʼ�������"+messages.length);

				/*for (int i = 0; i < messages.length; i++) {
					MimeMessage mimeMessage = (MimeMessage) messages[i];
					long UID = imapFolder.getUID(mimeMessage);
					System.out.println("���ʼ���UID��: "+UID);
					int count = imapFolder.getUnreadMessageCount();
					System.out.println("δ���ʼ���������"+count);
					URLName urlName = imapFolder.getURLName();
					System.out.println("URLName��ʲô��"+urlName);
					parseMessage(getMailBodyByUID(imapFolder, new long[]{UID}));
				}
*/
				parseMessage(getMailBodyByUID(imapFolder, new long[]{56}));
			}
			/** �ͷ���Դ */
			folder.close(true);
			store.close();

		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 *����UID��ȡ�ʼ����� 
	 * @return void
	 * @author dingsj
	 * @since 2013-11-11 ����11:23:41
	 */
	public Message[] getMailBodyByUID(IMAPFolder folder,long[] UID){
		try {
			return  folder.getMessagesByUID(UID);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * �����ʼ�
	 * 
	 * @author dingsj
	 * @param @param message Ҫ�������ʼ��б�
	 * @return void
	 * @since 2013-5-31
	 */
	public void parseMessage(Message[] messages) {
		try {
			if (messages == null || messages.length < 1) {
				throw new MessagingException("δ�ҵ�Ҫ�������ʼ�");
			}
			/** ���������ʼ� */
			for (int i = 0; i < messages.length; i++) {
				MimeMessage msg = (MimeMessage) messages[i];
				System.out.println("���ڽ�����" + msg.getMessageNumber() + "���ʼ���");
				System.out.println("���⣺" + getSubject(msg));
				System.out.println("�����ˣ�" + getFrom(msg));
				System.out.println("�ռ��ˣ�" + getReceiveAddress(msg, null));
				System.out.println("����ʱ�䣺" + getSentDate(msg, null));
				System.out.println("�Ƿ��Ѷ���" + isSeen(msg));
				System.out.println("�ʼ����ȼ���" + getPriority(msg));
				System.out.println("�Ƿ���Ҫ��ִ��" + isReplySign(msg));
				System.out.println("�ʼ���С��" + msg.getSize()*1024 + "kb");

				/** �����Ƿ�������� */
				boolean isContainerAttachment = isContainerAttachment(msg);
				System.out.println("�Ƿ����������" + isContainerAttachment);

				/** ���渽�� */
				if (isContainerAttachment) {
					saveAttachment(msg, "e:\\mailtest" + msg.getSubject() + "_");
				}

				StringBuffer contentBuffer = new StringBuffer();
				getMailTextContent(msg, contentBuffer);
				System.out.println("�ʼ����ģ�"
						+ (contentBuffer.length() > 100 ? contentBuffer
								.substring(0, 100) + "..." : contentBuffer
								.toString()));
				System.out.println("��" + msg.getMessageNumber() + "���ʼ�����������");
				System.out
						.println("=====================================================================");

			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ����ʼ�����
	 * 
	 * @author dingsj
	 * @param @param msg �ʼ�����
	 * @return String �������ʼ�����
	 * @param @throws UnsupportedEncodingException
	 * @param @throws MessagingException
	 * @since 2013-5-31
	 */
	private String getSubject(MimeMessage msg)
			throws UnsupportedEncodingException, MessagingException {
		String subject = MimeUtility.decodeText(msg.getSubject());
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
	private String getFrom(MimeMessage msg)
			throws UnsupportedEncodingException, MessagingException {
		String from = "";

		/** ���ʼ���ȡ�������˵�ַ */
		Address[] addresses = msg.getFrom();

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
	private String getReceiveAddress(MimeMessage msg, Message.RecipientType type)
			throws MessagingException {
		StringBuffer receiveAddress = new StringBuffer();
		Address[] addresses = null;

		/** ����ռ�������Ϊ�� */
		if (type == null) {
			addresses = msg.getAllRecipients();
		} else {
			addresses = msg.getRecipients(type);
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
	private String getSentDate(MimeMessage msg, String pattern)
			throws MessagingException {
		Date sendDate = msg.getSentDate();

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
	private boolean isSeen(MimeMessage msg) throws MessagingException {
		return msg.getFlags().contains(Flags.Flag.SEEN);
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
	private boolean isReplySign(MimeMessage msg) throws MessagingException {
		boolean replySign = false;
		String[] headers = msg.getHeader("Disposition-Notification-To");

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
	private String getPriority(MimeMessage msg) throws MessagingException {
		String priority = "��ͨ";
		String[] headers = msg.getHeader("X-Priority");

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
	private void getMailTextContent(Part part, StringBuffer content)
			throws MessagingException, IOException {
		/**
		 * ������ı����͵ĵĸ�����ͨ��getContent() ��������ȡ���ı����ݣ����Ը����е��ı�Ҳ������������� ���������Ҫ���ж�
		 */
		boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
		if (part.isMimeType("text/*") && !isContainTextAttach) {
			content.append(part.getContent().toString());
		} else if (part.isMimeType("message/rfc822")) {
			getMailTextContent((Part) part.getContent(), content);
		} else if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent();
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				BodyPart bodyPart = multipart.getBodyPart(i);
				getMailTextContent(bodyPart, content);
			}
		}
	}

	/**
	 * ���渽��
	 * 
	 * @param part
	 *            �ʼ��ж��������е�����һ�������
	 * @param destDir
	 *            ��������Ŀ¼
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveAttachment(Part part, String destDir)
			throws UnsupportedEncodingException, MessagingException,
			IOException {
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // �������ʼ�
			// �������ʼ���������ʼ���
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// ��ø������ʼ�������һ���ʼ���
				BodyPart bodyPart = multipart.getBodyPart(i);
				// ĳһ���ʼ���Ҳ�п������ɶ���ʼ�����ɵĸ�����
				String disp = bodyPart.getDisposition();
				if (disp != null
						&& (disp.equalsIgnoreCase(Part.ATTACHMENT) || disp
								.equalsIgnoreCase(Part.INLINE))) {
					InputStream is = bodyPart.getInputStream();
					saveFile(is, destDir, decodeText(bodyPart.getFileName()));
				} else if (bodyPart.isMimeType("multipart/*")) {
					saveAttachment(bodyPart, destDir);
				} else {
					String contentType = bodyPart.getContentType();
					if (contentType.indexOf("name") != -1
							|| contentType.indexOf("application") != -1) {
						saveFile(bodyPart.getInputStream(), destDir,
								decodeText(bodyPart.getFileName()));
					}
				}
			}
		} else if (part.isMimeType("message/rfc822")) {
			saveAttachment((Part) part.getContent(), destDir);
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
	private void saveFile(InputStream is, String destDir, String fileName)
			throws IOException {
		System.out.println("--------"+fileName);
		BufferedInputStream bis = new BufferedInputStream(is);
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(new File(destDir + fileName)));
		int len = -1;
		while ((len = bis.read()) != -1) {
			bos.write(len);
			bos.flush();
		}
		bos.close();
		bis.close();
	}

	/**
	 * �ı�����
	 * 
	 * @param encodeText
	 *            ����MimeUtility.encodeText(String text)�����������ı�
	 * @return �������ı�
	 * @throws UnsupportedEncodingException
	 */
	private String decodeText(String encodeText)
			throws UnsupportedEncodingException {
		if (encodeText == null || "".equals(encodeText)) {
			return "";
		} else {
			return MimeUtility.decodeText(encodeText);
		}
	}
	
	public static void main(String[] args) {
		try {
			new GmailReceiveMail().receive();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
