package cn.entersoft.mail.ding;

import java.util.HashMap;

import javax.mail.Session;
import javax.mail.internet.MimeMultipart;


/**
 * 用于邮件发送接收的测试
 * @author dingsj
 *2013-5-14
*Test
 */
public class TestSend {
	public static void main(String[] args) {
		/**测试邮件的发送*/
		MailInfo mailInfo = new MailInfo();
//		ArrayList<String> list_attach = new ArrayList<String>();
		HashMap<String,String> imgMap = new HashMap<String, String>();
		HashMap<String,String> attachMap = new HashMap<String, String>();
//		list_attach.add("src/HKSL1304057728FW.PDF");
		imgMap.put("e:/resource/1.jpg","e:/resource/1.jpg");
//		attachMap.put("你好世界.txt", "E:\\xx\\你好世界.txt");
		
		MailParam mailParam = new MailParam();
		mailParam.setProtocol("smtp");
		mailParam.setValidate(true);
		mailParam.setDebug(true);
		mailParam.setMailType("T");
		mailParam.setUserName("love.famg@gmail.com");
		mailParam.setPassWord("miss@fang0303");
		mailParam.setServerHost("smtp.gmail.com");
		mailParam.setServerPort("25");
		MailUtil mailUtil = MailUtil.getDefaultMailUtil(mailParam);
		Session session = mailUtil.getSession();
		session.setDebug(mailParam.isDebug());
		

		
		/**邮件发送初始化*/
		mailInfo.setMail_To("ding245@qq.com");
		mailInfo.setMail_From("love.famg@gmail.com");
		mailInfo.setMail_subject("测试头信息");
		mailInfo.setDeBug(true);
		mailInfo.setMail_body("<img src=\"cid:e:/resource/1.jpg\" width=\"150px\" height=\"150px\"/>这是一个邮件测试邮件，请不要回复，谢谢！");

		SendMail sendMail = SendMail.getDefaultSendMail(session);
//		SendMail1 send1 = new SendMail1();
		
//		/**文本格式发送*/
//		sendMail.sendText(mailInfo);
		
		sendMail.setAddressTo(mailInfo.getMail_To());
		sendMail.setAddressFrom(mailInfo.getMail_From());
		sendMail.setSubject(mailInfo.getMail_subject());
		sendMail.setPriority("3");
		sendMail.setMailer("Mr.Ding");
		sendMail.setSendDate();
		sendMail.setReply("哈哈", "xiaoliy88@126.com");
		sendMail.setAttach(attachMap);
		sendMail.setMailBody(mailInfo.getMail_body(),imgMap);
		
//		MimeMultipart mimeMultipart = sendMail.getMsgMimeMultipart();
		
		/**HTML格式发送*/
		sendMail.send();
		
	}
}
