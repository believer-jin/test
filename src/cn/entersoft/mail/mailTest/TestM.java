package cn.entersoft.mail.mailTest;

public class TestM {
	public static void main(String []args){
		OneMail themail = new OneMail("smtp.126.com");
		themail.setNamePass("enttest", "qwerty");
		String mailbody = "<img src=\"/ueditor/jsp/upload/2.jpg\" width=\"150px\" height=\"150px\"/>����һ���ʼ������ʼ����벻Ҫ�ظ���лл��";
		mailbody+="<hr><img src=\"http://imgtuku.mingxing.com/upload/attach/2012/08/20506-9VXAbSS.jpg\"/>";
		themail.setNeedAuth(false);

		themail.setSubject("�ʼ����Ͳ���2");
		themail.addFileAffix("D:/2.jpg");// �����ļ�·��
		themail.setBody(mailbody);

		themail.setFrom("enttest@126.com");

		long start = System.currentTimeMillis();
		System.out.println("=========================================\n׼�����ʼ�");
		themail.setTo("entusr@126.com");
		themail.sendout();
		long end = System.currentTimeMillis(); // ��ȡ���н���ʱ��
		System.out.println("���͸��ʼ�ʱ�䣺 " + (end - start) + "ms");
	}
}
