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
 * 邮件的接收类
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
	 * 获取正文
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public String getMailTextContent() throws MessagingException, IOException {
		getMailTextContent(message);
		return bodyContent.toString();
	}

	/**
	 * 获取附件
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void saveAttachment() throws UnsupportedEncodingException, MessagingException, IOException{
		saveAttachment(message);
	}
	
	/**
	 * 是否包含附件
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
	 * 获得邮件主题
	 * @author dingsj
	 * @param @param msg 邮件内容
	 * @return String 解码后的邮件主题
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
	public String getFrom()
			throws UnsupportedEncodingException, MessagingException {
		String from = "";

		/** 从邮件中取出发件人地址 */
		Address[] addresses = message.getFrom();

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
	public String getReceiveAddress(Message.RecipientType type)
			throws MessagingException {
		StringBuffer receiveAddress = new StringBuffer();
		Address[] addresses = null;

		/** 如果收件人类型为空 */
		if (type == null) {
			addresses = message.getAllRecipients();
		} else {
			addresses = message.getRecipients(type);
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
	public String getSentDate(String pattern)
			throws MessagingException {
		Date sendDate = message.getSentDate();

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
	public boolean isSeen() throws MessagingException {
		return message.getFlags().contains(Flags.Flag.SEEN);
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
	public boolean isReplySign() throws MessagingException {
		boolean replySign = false;
		String[] headers = message.getHeader("Disposition-Notification-To");

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
	public String getPriority() throws MessagingException {
		String priority = "普通";
		String[] headers = message.getHeader("X-Priority");

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
	private void getMailTextContent(Part part)throws MessagingException, IOException {
		/**
		 * 如果是文本类型的的附件，通过getContent() 方法可以取到文本内容，所以附件中的文本也会出现在正文中 因此这里需要做判断
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
	 * 保存附件
	 * 
	 * @param part
	 *            邮件中多个组合体中的其中一个组合体
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveAttachment(Part part)
			throws UnsupportedEncodingException, MessagingException,IOException {
		if (part.isMimeType("multipart/*")) {
			Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
			// 复杂体邮件包含多个邮件体
			int partCount = multipart.getCount();
			for (int i = 0; i < partCount; i++) {
				// 获得复杂体邮件中其中一个邮件体
				BodyPart bodyPart = multipart.getBodyPart(i);
				// 某一个邮件体也有可能是由多个邮件体组成的复杂体
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
				Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
				// 复杂体邮件包含多个邮件体
				int partCount = multipart.getCount();
				for (int i = 0; i < partCount; i++) {
					// 获得复杂体邮件中其中一个邮件体
					BodyPart bodyPart = multipart.getBodyPart(i);
					String[] contentIDs = bodyPart.getHeader("Content-ID");
					/*如果Content-ID不为空，则代表可能是正文附件*/
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
	 * 检验正文中是否有contentID指向的附件
	 * @param contentID 
	 * @return   boolean true：有；false：没有
	 */
	private boolean checkImgToContent(String contentID){
		String content = this.bodyContent.toString();
		/*如果contentID为空，则不是附件*/
		if("".equals(contentID.toString().trim())){
			return false;
		}else if(contentID.startsWith("<")){/*如果contenID是以"<"开始*/
			String contentID_temp = contentID.substring(1,contentID.length()-1);//去掉尖括号
			if("".equals(contentID_temp.trim())){
				return false;
			}
			if(content.indexOf("cid:"+contentID_temp) == -1){//正文中没有contentID
				return false;
			}
			return true;
		}else{
			if(content.indexOf("cid:"+content) == -1){//正文中没有contentID
				return false;
			}
			return true;
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
	 * 文本解码
	 * 
	 * @param encodeText
	 *            解码MimeUtility.encodeText(String text)方法编码后的文本
	 * @return 解码后的文本
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
