package cn.entersoft.mail.ding;

import java.util.HashMap;

import javax.mail.Session;
import javax.mail.internet.MimeMultipart;


/**
 * �����ʼ����ͽ��յĲ���
 * @author dingsj
 *2013-5-14
*Test
 */
public class TestSend {
	public static void main(String[] args) {
		/**�����ʼ��ķ���*/
		MailInfo mailInfo = new MailInfo();
//		ArrayList<String> list_attach = new ArrayList<String>();
		HashMap<String,String> imgMap = new HashMap<String, String>();
		HashMap<String,String> attachMap = new HashMap<String, String>();
//		list_attach.add("src/HKSL1304057728FW.PDF");
		imgMap.put("e:/resource/1.jpg","e:/resource/1.jpg");
//		attachMap.put("�������.txt", "E:\\xx\\�������.txt");
		
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
		

		
		/**�ʼ����ͳ�ʼ��*/
		mailInfo.setMail_To("ding245@qq.com");
		mailInfo.setMail_From("love.famg@gmail.com");
		mailInfo.setMail_subject("����ͷ��Ϣ");
		mailInfo.setDeBug(true);
		mailInfo.setMail_body("<img src=\"cid:e:/resource/1.jpg\" width=\"150px\" height=\"150px\"/>����һ���ʼ������ʼ����벻Ҫ�ظ���лл��");

		SendMail sendMail = SendMail.getDefaultSendMail(session);
//		SendMail1 send1 = new SendMail1();
		
//		/**�ı���ʽ����*/
//		sendMail.sendText(mailInfo);
		
		sendMail.setAddressTo(mailInfo.getMail_To());
		sendMail.setAddressFrom(mailInfo.getMail_From());
		sendMail.setSubject(mailInfo.getMail_subject());
		sendMail.setPriority("3");
		sendMail.setMailer("Mr.Ding");
		sendMail.setSendDate();
		sendMail.setReply("����", "xiaoliy88@126.com");
		sendMail.setAttach(attachMap);
		sendMail.setMailBody(mailInfo.getMail_body(),imgMap);
		
//		MimeMultipart mimeMultipart = sendMail.getMsgMimeMultipart();
		
		/**HTML��ʽ����*/
		sendMail.send();
		
	}
}
