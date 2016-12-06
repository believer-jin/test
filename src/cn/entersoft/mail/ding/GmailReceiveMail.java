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
 * 邮件的接收类
 * 
 * @author dingsj 2013-5-31 ReceiveMail
 */
public class GmailReceiveMail {

	/**
	 * 接收邮件
	 * 
	 * @author dingsj
	 * @param
	 * @return void
	 * @throws GeneralSecurityException 
	 * @since 2013-5-31
	 */
	public void receive() throws GeneralSecurityException {
		try {
			/** 创建属性对象 */
			Properties proerties = new Properties();
			proerties.setProperty("mail.store.protocol", "imap");// 协议
			proerties.setProperty("mail.imap.host", "imap.vip.163.com");// smtp服务器
			
			//**设置SSL选项*//*
			if("Y".equals("Y")){
				MailSSLSocketFactory sf = new MailSSLSocketFactory();
				sf.setTrustAllHosts(true);
				proerties.setProperty("mail.imap.ssl.enable", "true");
//				proerties.setProperty("mail.imap.ssl.checkserveridentity", "true");
				proerties.put("mail.imap.ssl.socketFactory", sf);  
				proerties.setProperty("mail.imap.port", "993");// 端口
			}
			/** 创建Session对象 */
			Session session = Session.getInstance(proerties);
			session.setDebug(true);
			Store store = session.getStore("imap");
			store.connect("janec@vip.163.com", "jane1019");

			/**获取所有的文件夹*/
			Folder[] folders = store.getDefaultFolder().list();
			for(Folder fd : folders){
				System.out.println("文件夹的名字是>>"+fd.getName());
			}
			
			/** 获取收件箱 */
			Folder folder = store.getFolder("INBOX");

			/**
			 * 打开收件箱 Folder.READ_ONLY：只读权限 Folder.READ_WRITE：可读可写（可以修改邮件的状态）
			 */
			folder.open(Folder.READ_WRITE);
			/**设置预读取*/
			FetchProfile profile = new FetchProfile();
			/**
			 * 设置预读的值是UID.....邮件服务器上的每个邮件唯一id
			 * 注意：保存的时候按照邮箱存储，因为不同的邮箱uid会重复的
			 * */
			profile.add(UIDFolder.FetchProfileItem.UID);
			profile.add(FetchProfile.Item.ENVELOPE);
			/**
			 * 注释：POP3协议无法获知邮件的状态,所以getUnreadMessageCount得到的是收件箱的邮件总数
			 * getDeletedMessageCount()和getNewMessageCount()始终是0
			 */
			System.out.println("未读邮件数量：" + folder.getUnreadMessageCount());
			System.out.println("已删除邮件数：" + folder.getDeletedMessageCount());
			System.out.println("新邮件：" + folder.getNewMessageCount());
			System.out.println("邮件总数：" + folder.getMessageCount());
			System.out.println("====================================================");
			
			/**如果folder是IMAPFolder的一个实例*/
			if(folder instanceof IMAPFolder){
				/**将folder转换成IMAPFolder*/
				IMAPFolder imapFolder = (IMAPFolder) folder;
				/** 得到收件箱中的所有邮件，并解析 */
				Message[] messages = imapFolder.getMessages();
				imapFolder.fetch(messages, profile);
				
				System.out.println("邮件数量："+messages.length);

				/*for (int i = 0; i < messages.length; i++) {
					MimeMessage mimeMessage = (MimeMessage) messages[i];
					long UID = imapFolder.getUID(mimeMessage);
					System.out.println("该邮件的UID是: "+UID);
					int count = imapFolder.getUnreadMessageCount();
					System.out.println("未读邮件的数量："+count);
					URLName urlName = imapFolder.getURLName();
					System.out.println("URLName是什么："+urlName);
					parseMessage(getMailBodyByUID(imapFolder, new long[]{UID}));
				}
*/
				parseMessage(getMailBodyByUID(imapFolder, new long[]{56}));
			}
			/** 释放资源 */
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
	 *根据UID获取邮件正文 
	 * @return void
	 * @author dingsj
	 * @since 2013-11-11 上午11:23:41
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
	 * 解析邮件
	 * 
	 * @author dingsj
	 * @param @param message 要解析的邮件列表
	 * @return void
	 * @since 2013-5-31
	 */
	public void parseMessage(Message[] messages) {
		try {
			if (messages == null || messages.length < 1) {
				throw new MessagingException("未找到要解析的邮件");
			}
			/** 解析所有邮件 */
			for (int i = 0; i < messages.length; i++) {
				MimeMessage msg = (MimeMessage) messages[i];
				System.out.println("正在解析第" + msg.getMessageNumber() + "封邮件！");
				System.out.println("主题：" + getSubject(msg));
				System.out.println("发件人：" + getFrom(msg));
				System.out.println("收件人：" + getReceiveAddress(msg, null));
				System.out.println("发送时间：" + getSentDate(msg, null));
				System.out.println("是否已读：" + isSeen(msg));
				System.out.println("邮件优先级：" + getPriority(msg));
				System.out.println("是否需要回执：" + isReplySign(msg));
				System.out.println("邮件大小：" + msg.getSize()*1024 + "kb");

				/** 检验是否包含附件 */
				boolean isContainerAttachment = isContainerAttachment(msg);
				System.out.println("是否包含附件：" + isContainerAttachment);

				/** 保存附件 */
				if (isContainerAttachment) {
					saveAttachment(msg, "e:\\mailtest" + msg.getSubject() + "_");
				}

				StringBuffer contentBuffer = new StringBuffer();
				getMailTextContent(msg, contentBuffer);
				System.out.println("邮件正文："
						+ (contentBuffer.length() > 100 ? contentBuffer
								.substring(0, 100) + "..." : contentBuffer
								.toString()));
				System.out.println("第" + msg.getMessageNumber() + "封邮件解析结束！");
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
	 * 获得邮件主题
	 * 
	 * @author dingsj
	 * @param @param msg 邮件内容
	 * @return String 解码后的邮件主题
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
	 * 获取发件人
	 * 
	 * @author dingsj
	 * @param msg
	 *            邮件内容
	 * @return from(String类型) 姓名+Email地址
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @since 2013-5-31
	 */
	private String getFrom(MimeMessage msg)
			throws UnsupportedEncodingException, MessagingException {
		String from = "";

		/** 从邮件中取出发件人地址 */
		Address[] addresses = msg.getFrom();

		/** 如果发件人为空 */
		if (addresses.length < 1) {
			throw new MessagingException("没有发件人");
		}

		InternetAddress address = (InternetAddress) addresses[0];
		String person = address.getPersonal();

		/** 如果收件人不为空，则解码 */
		if (person != null) {
			person = MimeUtility.decodeText(person).toString().trim();
		} else {
			person = "";
		}

		from = person + "<" + address.getAddress() + ">";

		return from;
	}

	/**
	 * 根据收件人类型，获取邮件收件人，抄送，和密送地址。如果收件人类型为空，则获得所有的收件人
	 * <p>
	 * Message.RecipientType.TO
	 * </p>
	 * <p>
	 * Message.RecipientType.TO 收件人
	 * </p>
	 * <p>
	 * Message.RecipientType.CC 抄送
	 * </p>
	 * <p>
	 * Message.RecipientType.BCC 密送
	 * </p>
	 * 
	 * @param msg
	 *            邮件内容
	 * @param type
	 *            收件人类型
	 * @author dingsj
	 * @return String 收件人
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @since 2013-5-31
	 */
	private String getReceiveAddress(MimeMessage msg, Message.RecipientType type)
			throws MessagingException {
		StringBuffer receiveAddress = new StringBuffer();
		Address[] addresses = null;

		/** 如果收件人类型为空 */
		if (type == null) {
			addresses = msg.getAllRecipients();
		} else {
			addresses = msg.getRecipients(type);
		}

		/** 如果收件人地址为空 */
		if (addresses == null || addresses.length < 1) {
			throw new MessagingException("没有收件人");
		}

		/** 遍历，取出每一个地址 */
		for (Address address : addresses) {
			InternetAddress internetAddress = (InternetAddress) address;
			receiveAddress.append(internetAddress.toUnicodeString())
					.append(",");
		}

		/** 删除最后一个逗号 */
		receiveAddress.deleteCharAt(receiveAddress.length() - 1);

		return receiveAddress.toString();
	}

	/**
	 * @Description: 获取邮件发送时间
	 * @param msg
	 *            邮件内容
	 * @param pattern
	 *            日期格式
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
			pattern = "yyyy年MM月dd日 E HH:mm:ss ";
		}

		return new SimpleDateFormat(pattern).format(sendDate);
	}

	/**
	 * 检验是否包含附件
	 * 
	 * @param @param msg 邮件内容
	 * @return boolean 存在返回true，不存在返回false
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
	 * 判断邮件是否已读
	 * 
	 * @param msg
	 *            邮件内容
	 * @return boolean
	 * @throws MessagingException
	 * @author dingsj
	 * @since 2013-5-31
	 */
	private boolean isSeen(MimeMessage msg) throws MessagingException {
		return msg.getFlags().contains(Flags.Flag.SEEN);
	}

	/**
	 * 判断邮件是否需要已读回执
	 * 
	 * @param MinmeMessage
	 *            msg 邮件内容
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
	 * 获得邮件的优先级
	 * 
	 * @param msg
	 *            邮件内容
	 * @return String
	 * @throws MessagingException
	 * @author dingsj
	 * @since 2013-5-31
	 */
	private String getPriority(MimeMessage msg) throws MessagingException {
		String priority = "普通";
		String[] headers = msg.getHeader("X-Priority");

		if (headers != null) {
			String headerPriority = headers[0];
			if (headerPriority.indexOf("1") != -1
					|| headerPriority.indexOf("High") != -1) {
				priority = "紧急";
			} else if (headerPriority.indexOf("5") != -1
					|| headerPriority.indexOf("Low") != -1) {
				priority = "低";
			} else {
				priority = "普通";
			}
		}

		return priority;
	}

	/**
	 * 获得邮件文本
	 * 
	 * @param part
	 *            邮件内容
	 * @param content
	 *            存储邮件文本内容的字符串
	 * @return void
	 * @throws MessagingException
	 * @throws IOException
	 * @author dingsj
	 * @since 2013-5-31
	 */
	private void getMailTextContent(Part part, StringBuffer content)
			throws MessagingException, IOException {
		/**
		 * 如果是文本类型的的附件，通过getContent() 方法可以取到文本内容，所以附件中的文本也会出现在正文中 因此这里需要做判断
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
	 * 保存附件
	 * 
	 * @param part
	 *            邮件中多个组合体中的其中一个组合体
	 * @param destDir
	 *            附件保存目录
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveAttachment(Part part, String destDir)
			throws UnsupportedEncodingException, MessagingException,
			IOException {
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
			// 复杂体邮件包含多个邮件体
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// 获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);
				// 某一个邮件体也有可能是由多个邮件体组成的复杂体
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
	 * 读取输入流中的数据保存至指定目录
	 * 
	 * @param is
	 *            输入流
	 * @param fileName
	 *            文件名
	 * @param destDir
	 *            文件存储目录
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
	 * 文本解码
	 * 
	 * @param encodeText
	 *            解码MimeUtility.encodeText(String text)方法编码后的文本
	 * @return 解码后的文本
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
