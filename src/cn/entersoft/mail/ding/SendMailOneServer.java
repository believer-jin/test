package cn.entersoft.mail.ding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.*;

/**
 * <a href="http://www.hongyun2000.com/">��������</a>
 * @version 0.1
 * @author heui
 */
public class SendMailOneServer {

	/**
	 * ����MIME�ʼ�����
	 */
	private MimeMessage mimeMsg;
	/**
	 * ר�����������ʼ���Session�Ự
	 */
	private Session session;
	/**
	 * ��װ�ʼ�����ʱ��һЩ������Ϣ��һ�����Զ���
	 */
	private Properties props;
	/**
	 * �����˵��û���
	 */
	private String username;
	/**
	 * �����˵�����
	 */
	private String password;
	/**
	 * ����ʵ�ָ�����ӵ����
	 */
	private MimeMultipart mp;
	/**
	 * ����ͳ��
	 */
	public static int count=0;
	/**
	 * ������list,����element����byte[],��ͼƬ�Ķ�������
	 */
	//private LinkedList attachlist;
	/**
	 * ���Ͳ�����ʼ��,�еķ���������Ҫ�û���֤������������û�����������г�ʼ��""
	 * 
	 * @param smtp
	 */
	public SendMailOneServer(String smtp) {
		username = "";
		password = "";
		// �����ʼ�������
		setSmtpHost(smtp);
		// �����ʼ�
		createMimeMessage();
	}

	/**
	 * ���÷����ʼ�������(JavaMail��ҪProperties������һ��session����
	 * ����Ѱ���ַ���"mail.smtp.host"������ֵ���Ƿ����ʼ�������);
	 * 
	 * @param hostName
	 */
	public void setSmtpHost(String hostName) {
		System.out.println("����ϵͳ���ԣ�mail.smtp.host = " + hostName);
		if (props == null)
			props = System.getProperties();
		props.put("mail.smtp.host", hostName);
	}

	/**
	 * (���Session�����JavaMail �е�һ���ʼ�session. ÿһ������
	 * JavaMail��Ӧ�ó���������һ��session���ǿ�����������session�� �����������,
	 * Session������Ҫ֪�����������ʼ���SMTP ��������
	 */
	public boolean createMimeMessage() {
		try {
			System.out.println("׼����ȡ�ʼ��Ự����");
			// ��props��������������ʼ��session����
			session = Session.getDefaultInstance(props, null);
		} catch (Exception e) {
			System.err.println("��ȡ�ʼ��Ự����ʱ��������" + e);
			return false;
		}
		System.out.println("׼������MIME�ʼ�����");
		try {
			// ��session��������������ʼ���ʼ�����
			mimeMsg = new MimeMessage(session);
			// ���ɸ��������ʵ��
			// mp = new MimeMultipart();
			mp = new MimeMultipart("related");
		} catch (Exception e) {
			System.err.println("����MIME�ʼ�����ʧ�ܣ�" + e);
			return false;
		}
		return true;
	}

	/**
	 * ����SMTP�������֤
	 */
	public void setNeedAuth(boolean need) {
		System.out.println("����smtp�����֤��mail.smtp.auth = " + need);
		if (props == null)
			props = System.getProperties();
		if (need)
			props.put("mail.smtp.auth", "true");
		else
			props.put("mail.smtp.auth", "false");
	}

	/**
	 * �����û������֤ʱ�������û���������
	 */
	public void setNamePass(String name, String pass) {
		System.out.println("����õ��û���������");
		username = name;
		password = pass;
	}

	/**
	 * �����ʼ�����
	 * 
	 * @param mailSubject
	 * @return
	 */
	public boolean setSubject(String mailSubject) {
		System.out.println("�����ʼ����⣡");
		try {
			mimeMsg.setSubject(mailSubject);
		} catch (Exception e) {
			System.err.println("�����ʼ����ⷢ������");
			return false;
		}
		return true;
	}

	/**
	 * �����ʼ�����,��������Ϊ�ı���ʽ��HTML�ļ���ʽ�����뷽ʽΪUTF-8
	 * 
	 * @param mailBody
	 * @return
	 */
	public boolean setBody(String mailBody) {
		try {
			System.out.println("�����ʼ����ʽ");
			
			BodyPart bp = new MimeBodyPart();
			bp.setContent(
					"<meta http-equiv=Content-Type content=text/html; charset=UTF-8>"
							+ mailBody, "text/html;charset=UTF-8");
			
			mp.setSubType("related");
			// �����������ʼ��ı�			
			mp.addBodyPart(bp);
			
		} catch (Exception e) {
			System.err.println("�����ʼ�����ʱ��������" + e);
			return false;
		}
		return true;
	}

	/**
	 * ���ӷ��͸���
	 * 
	 * @param filename
	 *            �ʼ������ĵ�ַ��ֻ���Ǳ�����ַ�������������ַ�������׳��쳣
	 * @return
	 */
	public boolean addFileAffix(String filename) {
		System.out.println("�����ʼ�������" + filename);
		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			// ���͵ĸ���ǰ����һ���û�����ǰ׺
			//bp.setFileName(fileds.getName());
			bp.setFileName("1.jpg");
			bp.setHeader("content-id", "1.jpg"); 
			// ��Ӹ���
			mp.addBodyPart(bp);
			//mimeMsg.setContent(mp);
		} catch (Exception e) {
			System.err.println("�����ʼ�������" + filename + "��������" + e);
			return false;
		}
		return true;
	}

	/**
	 * ���÷����˵�ַ
	 * 
	 * @param from
	 *            �����˵�ַ
	 * @return
	 */
	public boolean setFrom(String from) {
		System.out.println("���÷����ˣ�");
		try {
			mimeMsg.setFrom(new InternetAddress(from));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * �����ռ��˵�ַ
	 * 
	 * @param to
	 *            �ռ��˵ĵ�ַ
	 * @return
	 */
	public boolean setTo(String to) {
		System.out.println("����������");
		if (to == null)
			return false;
		try {
			mimeMsg.setRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(to));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * ���͸���
	 * 
	 * @param copyto
	 * @return
	 */
	public boolean setCopyTo(String copyto) {
		System.out.println("���͸�����");
		if (copyto == null)
			return false;
		try {
			mimeMsg.setRecipients(javax.mail.Message.RecipientType.CC,
					InternetAddress.parse(copyto));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * �����ʼ�
	 * 
	 * @return
	 */
	public boolean sendout() {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			System.out.println("���ڷ����ʼ�....");
			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			// �����������ʼ������������������֤

			transport.connect(props.getProperty("mail.smtp.host"), username,
					password);

			// �����ʼ�
			transport.sendMessage(mimeMsg, mimeMsg
					.getRecipients(javax.mail.Message.RecipientType.TO));

			System.out.println("�����ʼ��ɹ�����" + (count++) + "��");
			transport.close();
		} catch (Exception e) {
			System.err.println("�ʼ�����ʧ�ܣ�" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		ArrayList<SmtpServer> ssarr = new ArrayList<SmtpServer>();
		
		BufferedReader br = null;
		String line = null;
		
		try {
			br = new BufferedReader(new FileReader("src/server.txt"));
			
			while((line = br.readLine())!=null)
			{
				ssarr.add(new SmtpServer(line));
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
				
		Iterator<SmtpServer> iter = ssarr.iterator();
		SmtpServer ss = iter.next();
		SendMailOneServer themail = new SendMailOneServer(ss.getSmtp());

		String mailbody = "<a href=\"http://www.hongyun2000.com/heuimail\"><img src=\"cid:1.jpg\" /></a><br />�������Ӫһ������վ���п�ȥ���������������лл<a href=\"http://www.hongyun2000.com/heuimail\">www.hongyun2000.com</a>";
		themail.setNeedAuth(true);

		themail.setSubject("С����С����ͼ");
		themail.addFileAffix("D:/1.jpg");// �����ļ�·��
		themail.setBody(mailbody);
		
		themail.setFrom(ss.getUser());

		themail.setNamePass(ss.getUser(), ss.getPassword());
		
		
		try {
			br = new BufferedReader(new FileReader("src/client.txt"));
			
			
			while((line = br.readLine())!=null/* && iter.hasNext()*/)
			{
				long start=System.currentTimeMillis(); 

				System.out.println("====================================================\n׼�����ʼ���"+line);
				themail.setTo(line);
				themail.sendout();
				
				long end=System.currentTimeMillis(); //��ȡ���н���ʱ��
				System.out.println("���͸��ʼ�ʱ�䣺 "+(end-start)+"ms"); 
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
		
	
	}
	
}
