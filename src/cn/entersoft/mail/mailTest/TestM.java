package cn.entersoft.mail.mailTest;

public class TestM {
	public static void main(String []args){
		OneMail themail = new OneMail("smtp.126.com");
		themail.setNamePass("enttest", "qwerty");
		String mailbody = "<img src=\"/ueditor/jsp/upload/2.jpg\" width=\"150px\" height=\"150px\"/>这是一个邮件测试邮件，请不要回复，谢谢！";
		mailbody+="<hr><img src=\"http://imgtuku.mingxing.com/upload/attach/2012/08/20506-9VXAbSS.jpg\"/>";
		themail.setNeedAuth(false);

		themail.setSubject("邮件发送测试2");
		themail.addFileAffix("D:/2.jpg");// 附件文件路径
		themail.setBody(mailbody);

		themail.setFrom("enttest@126.com");

		long start = System.currentTimeMillis();
		System.out.println("=========================================\n准备发邮件");
		themail.setTo("entusr@126.com");
		themail.sendout();
		long end = System.currentTimeMillis(); // 获取运行结束时间
		System.out.println("发送该邮件时间： " + (end - start) + "ms");
	}
}
