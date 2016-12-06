package cn.entersoft.mail.ding;

import java.util.ArrayList;

/**
 * �����Ƿ����ʼ�������Ϣ��JAVABean JAVABean
 * 
 * @author dingsj 2013-5-15 MailInfo
 */
public class MailInfo {
	private String mailServerHost;// ���������
	private String mialServerPort;// ����������Ķ˿�
	private String userName;// �����½�û���
	private String passWord;// �����½����
	private boolean validate = false;// �Ƿ���Ҫ�����֤��Ĭ��Ϊ����Ҫ
	private String mail_From;// ������
	private String mail_To;// �ռ���
	private String mail_subject;// �ʼ�����
	private String mail_body;// �ʼ�����
	private ArrayList<String> mail_picture;//�ʼ�ͼƬ
	private ArrayList<String> mail_attach;// �ʼ�����
	private boolean deBug = false; //deBug��־

	public MailInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getMailServerHost() {
		return mailServerHost;
	}

	public void setMailServerHost(String mailServerHost) {
		this.mailServerHost = mailServerHost;
	}

	public String getMialServerPort() {
		return mialServerPort;
	}

	public void setMialServerPort(String mialServerPort) {
		this.mialServerPort = mialServerPort;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getMail_From() {
		return mail_From;
	}

	public void setMail_From(String mail_From) {
		this.mail_From = mail_From;
	}

	public String getMail_To() {
		return mail_To;
	}

	public void setMail_To(String mail_To) {
		this.mail_To = mail_To;
	}

	public String getMail_subject() {
		return mail_subject;
	}

	public void setMail_subject(String mail_subject) {
		this.mail_subject = mail_subject;
	}

	public String getMail_body() {
		return mail_body;
	}

	public void setMail_body(String mail_body) {
		this.mail_body = mail_body;
	}

	public void setMail_attach(ArrayList<String> mail_attach) {
		this.mail_attach = mail_attach;
	}

	public ArrayList<String> getMail_attach() {
		return mail_attach;
	}

	public boolean isValidate() {
		return validate;
	}

	public void setValidate(boolean validate) {
		this.validate = validate;
	}
	

	public ArrayList<String> getMail_picture() {
		return mail_picture;
	}

	public void setMail_picture(ArrayList<String> mail_picture) {
		this.mail_picture = mail_picture;
	}

	public boolean isDeBug() {
		return deBug;
	}

	public void setDeBug(boolean deBug) {
		this.deBug = deBug;
	}

	@Override
	public String toString() {
		return "MailInfo [mailServerHost=" + mailServerHost
				+ ", mialServerPort=" + mialServerPort + ", userName="
				+ userName + ", passWord=" + passWord + ", validate="
				+ validate + ", mail_From=" + mail_From + ", mail_To="
				+ mail_To + ", mail_subject=" + mail_subject + ", mail_body="
				+ mail_body + ", mail_picture=" + mail_picture
				+ ", mail_attach=" + mail_attach + ", deBug=" + deBug + "]";
	}





}
