package cn.entersoft.mail.mailTest;

import java.security.Security;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.UIDFolder;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.pop3.POP3Folder;
import common.mail.Mail;



public class Test3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// testPopReceive();
		// testPopReceive2();
		//testHotmail();
		//testMailConnectionSmtp();
		//testMailConnectionPopImap();
		//testSendHotmail();
		//testImapReceiveGmail2();
		//testPop3();
		//testPop4();
		//testPop5();
		testPop6();
		//testPop7();
		//testPop8();
	}
	public static void testPop8(){
		String c_mailpopsslflg = "N";
		String c_mailpopport = "110";
		String tmpPopAddr = "pop.qq.com";
		String tmpUser = "594475263@qq.com";
		String tmpPasswd = "yushiyuan955+++";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, "");
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			
			profile.add(UIDFolder.FetchProfileItem.UID);
			Message[] messages = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			folder.fetch(messages, profile);
			System.out.println("�ռ������ʼ���:"+messages.length);
			int count=0;
			//for(int i=messages.length-1;i>=1;i--){	
			for(int i=messages.length-1;i>=0;i--){	
				try{
					Message m=messages[i];
					String subject = m.getSubject();
					String uid = ((POP3Folder)folder).getUID(m);
					ReceiveMimeMessage rmm=new ReceiveMimeMessage((MimeMessage)m);
					String from=rmm.getFrom();
					String senddate=rmm.getSentDate();				
					System.out.println(i+"�����ڣ�"+senddate+"��msgid:"+uid+"�����⣺"+subject);	
					System.out.println("������:"+from);
				}catch(Exception e){
					System.out.println("err:"+i+","+messages[i]+","+e.getMessage());
				}
			}			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public static void testPop7(){
		String c_mailpopsslflg = "N";
		String c_mailpopport = "110";
		String tmpPopAddr = "mail.affeel.com";
		String tmpUser = "lisa@affeel.com";
		String tmpPasswd = "lisa123321";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, "");
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			
			profile.add(UIDFolder.FetchProfileItem.UID);
			Message[] messages = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			folder.fetch(messages, profile);
			System.out.println("�ռ������ʼ���:"+messages.length);
			int count=0;
			//for(int i=messages.length-1;i>=1;i--){	
			for(int i=messages.length-1;i>=0;i--){	
				try{
					Message m=messages[i];
					String subject = m.getSubject();
					String uid = ((POP3Folder)folder).getUID(messages[i]);
					ReceiveMimeMessage rmm=new ReceiveMimeMessage((MimeMessage)m);
					String from=rmm.getFrom();
					String senddate=rmm.getSentDate();				
					System.out.println(i+"�����ڣ�"+senddate+"��msgid:"+uid+"�����⣺"+subject);	
					System.out.println("������:"+from);
				}catch(Exception e){}
			}			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	//???Ϊʲô��null��uidlΪ��ô��null
	public static void testPop6(){
		String c_mailpopsslflg = "N";
		String c_mailpopport = "143";//143 imap  110 pop		
		//String tmpPopAddr = "pop.tom.com";
		//String tmpUser = "tcyjlove2@tom.com";
		//String tmpPasswd = "62144878a";
		//String tmpUser = "yltest@tom.com";
		//String tmpPasswd = "wxfx0806";
		String tmpPopAddr="imap.126.com";
		String tmpUser = "enttest@126.com";
		String tmpPasswd = "qwerty";
		String revType = "imap";//pop,smtp,''
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			
			profile.add(UIDFolder.FetchProfileItem.UID);
			//profile.add("Subject");
			//profile.add("From");
			//profile.add("");
			Message[] messages = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			folder.fetch(messages, profile);
			System.out.println("�ռ������ʼ���:"+messages.length);
			int count=0;
			//for(int i=messages.length-1;i>=1;i--){	
			for(int i=messages.length-1;i>=0;i--){	
				Message m=messages[i];	
				String subject = m.getSubject();
				String uid="";
				//String uid = ((POP3Folder)folder).getUID(m);
				if("pop".equals(revType)){
					uid = ((POP3Folder)folder).getUID(m);					
				}else if("imap".equals(revType)){
					uid=String.valueOf(((IMAPFolder)folder).getUID(m));
				}
				//ReceiveMimeMessage pmm = new ReceiveMimeMessage((MimeMessage) message[i]);
				ReceiveMimeMessage rmm=new ReceiveMimeMessage((MimeMessage)m);
				String from=rmm.getFrom();
				String senddate=rmm.getSentDate();				
				System.out.println(i+"�����ڣ�"+senddate+"��msgid:"+uid+"�����⣺"+subject);	
				//System.out.println(rmm.getMessageId());
				System.out.println("������:"+from);	
				/*receiveOneMailContent(rmm,"<516EBC03.1030202@whisperingsmith.com>",
						"Re: Fw: Re: Your white copy of Order: CG529601 Style: LHAT-247ANIMAL",
						"nallum@whisperingsmith.com","2013/04/18 13:19:27");*/				
			}			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	//����ָ��������һ���ʼ������(from,subject,senddate)��msg�еĶ�Ӧ��Ϣ��ƥ��������ȡ������true�����򷵻�false
	public static boolean receiveOneMailContent(ReceiveMimeMessage msg,String msg_id,String subject,
			String from,String senddate){
		//�ж�����ʼ��Ƿ���Ҫ��ȡ���ʼ�
		//����msgid�жϣ���ͬ�����from,to,subject,senddate�жϣ������ͬ����ȡ���ĺ͸���
		String msgid="";
		String content="";
		String osubject="";
		String ofrom="";
		String osenddate="";
		try {
			msgid=msg.getMessageId();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		try{
			if(msgid!=null&&msgid.equals(msg_id)){
				msg.setAttachPath("src/");     
				msg.setInlineImgPath("src/");
				msg.getMailContent();
				content=msg.getBodyText();
				
				//File afile = new File(EntAsp.getEntsoftPath(tenantID) + "Entsoft/EnterDOC" + filepath + "/EnterMail/" + strmaildate + "/" + tmpMailAddr);
				//if (!afile.exists()) afile.mkdirs();
				//����ǰ��ɾ��ԭ���ĸ���
				
				msg.saveImgAttach();
				msg.saveAttachMent();
				//System.out.println("A");
			}else{
				ofrom=msg.getFrom();
				osubject=msg.getSubject();
				osenddate=msg.getSentDate();				
				if(ofrom.equals(from)&&osubject.equals(subject)&&osenddate.equals(senddate)){
					msg.setAttachPath("src/");     
					msg.setInlineImgPath("src/");
					//pmm.setAttachPath(EntAsp.getEntsoftPath(tenantID) + "Entsoft/EnterDOC" + filepath + "/EnterMail/" + strmaildate + "/"  + tmpMailAddr + "/");
					//pmm.setInlineImgPath(EntAsp.getEntsoftPath(tenantID) + "Entsoft/EnterDOC/uploadfile/");
					msg.getMailContent();
					content=msg.getBodyText();
					
					//File afile = new File(EntAsp.getEntsoftPath(tenantID) + "Entsoft/EnterDOC" + filepath + "/EnterMail/" + strmaildate + "/" + tmpMailAddr);
					//if (!afile.exists()) afile.mkdirs();
					//����ǰ��ɾ��ԭ���ĸ���
					
					msg.saveImgAttach();
					msg.saveAttachMent();
				}
				//System.out.println("B");
			}
			if(!"".equals(content)){
				System.out.println(msgid);
				System.out.println(osubject+","+ofrom+","+osenddate);
				System.out.println("-----------------����-----------------");
				System.out.println("�ʼ�������ȡ��"+content);
				System.out.println("------------------------------------");
			}				
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}		
		return false;
	}
	public static void testPop5(){
		String c_mailpopsslflg = "N";
		String c_mailpopport = "110";
		String tmpPopAddr = "pop3.sina.net";//pop.163.com
		String tmpUser = "market@lordmed.com";//enters1@163.com
		String tmpPasswd = "greathonsun";//entsoft
		String revType = "pop";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			
			profile.add(UIDFolder.FetchProfileItem.UID);
			profile.add("Subject");
			profile.add("From");
			//profile.add("");
			Message[] messages = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			folder.fetch(messages, profile);
			System.out.println("�ռ������ʼ���:"+messages.length);
			//DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			for(int i=messages.length-1;i>=0;i--){							
				Message m=messages[i];	
				String subject = m.getSubject();	
				String uid = ((POP3Folder)folder).getUID(m);
				ReceiveMimeMessage rmm=new ReceiveMimeMessage((MimeMessage)m);
				String from=rmm.getFrom();
				String senddate=rmm.getSentDate();				
				/*if(subject!=null&&!subject.contains("Re: Fw: Re: Your white copy of Order: CG529601 Style:")){
					continue;
				}*/
				System.out.println(i+"�����ڣ�"+senddate+"��msgid:"+uid+"�����⣺"+subject);		//2013/04/18 13:19:27		
				//System.out.println("������:"+from);				
				receiveOneMailContent(rmm,"<516EBC03.1030202@whisperingsmith.com>",
						"Re: Fw: Re: Your white copy of Order: CG529601 Style: LHAT-247ANIMAL",
						"nallum@whisperingsmith.com","2013/04/18 13:19:27");				
			}			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public static void testPop4(){
		String c_mailpopsslflg = "N";
		String c_mailpopport = "110";
		String tmpPopAddr = "pop.china-jnbx.com";
		String tmpUser = "zhangguichao@china-jnbx.com";
		String tmpPasswd = "du750801750902";
		String revType = "pop";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			
			profile.add(UIDFolder.FetchProfileItem.UID);
			profile.add("Subject");
			profile.add("From");
			//profile.add("");
			Message[] messages = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			folder.fetch(messages, profile);
			System.out.println("�ռ������ʼ���:"+messages.length);
			int count=0;
			for(int i=messages.length-1;i>=0;i--){							
				Message m=messages[i];	
				String subject = m.getSubject();	
				String uid = ((POP3Folder)folder).getUID(m);
				ReceiveMimeMessage rmm=new ReceiveMimeMessage((MimeMessage)m);
				String from=rmm.getFrom();
				String senddate=rmm.getSentDate();				
				if (subject!=null) {
					System.out.println(i+"�����ڣ�"+senddate+"��msgid:"+uid+"�����⣺"+subject);
				}else{
					System.out.println(i+",ʱ�䣺"+senddate+"��msgid:"+uid+":����Ϊ:null");
					count++;					
				}				
				if(from!=null&&"".equals(from.trim())){
					System.out.println("�õ�ַ�޷�����");
				}else{
					System.out.println("������:"+from);
				}				
			}
			//System.out.println("����Ϊnull���ʼ�����"+count);			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public static void testPop2(){
		String c_mailpopsslflg = "N";
		String c_mailpopport = "110";
		String tmpPopAddr = "pop.sign-in-china.com";
		String tmpUser = "overseas12@sign-in-china.com";
		String tmpPasswd = "a888888";
		String revType = "pop";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			
			profile.add(UIDFolder.FetchProfileItem.UID);
			profile.add("Subject");
			profile.add("From");
			//profile.add("");
			Message[] messages = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			folder.fetch(messages, profile);
			System.out.println("�ռ������ʼ���:"+messages.length);
			int count=0;
			for(int i=messages.length-1;i>=0;i--){							
				Message m=messages[i];
				String subject = m.getSubject();	
				String uid = ((POP3Folder)folder).getUID(m);
				ReceiveMimeMessage rmm=new ReceiveMimeMessage((MimeMessage)m);
				String from=rmm.getFrom();
				String senddate=rmm.getSentDate();
				if(senddate!=null&&!senddate.contains("2013/03/27")){
					continue;
				}
				if (subject!=null) {
					System.out.println(i+"�����ڣ�"+senddate+"��msgid:"+uid+"�����⣺"+subject);
				}else{
					System.out.println(i+",ʱ�䣺"+senddate+"��msgid:"+uid+":����Ϊ:null");
					count++;					
				}				
				if(from!=null&&"".equals(from.trim())){
					System.out.println("�õ�ַ�޷�����");
				}else{
					System.out.println("������:"+from);
				}				
			}
			System.out.println("����Ϊnull���ʼ�����"+count);			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public static void testPop3(){
		String c_mailpopsslflg = "N";
		String c_mailpopport = "110";
		String tmpPopAddr = "pop.qq.com";
		String tmpUser = "enttest@qq.com";
		String tmpPasswd = "qwerty";
		String revType = "";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			/*Folder defau = store.getDefaultFolder();
			for(Folder f:defau.list()){
				System.out.println(f.getName());
			}*/
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			
			profile.add(UIDFolder.FetchProfileItem.UID);
			profile.add("Subject");
			profile.add("From");
			//profile.add("");
			Message[] messages = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			folder.fetch(messages, profile);
			System.out.println("�ռ������ʼ���:"+messages.length);
			for(int i=messages.length-1;i>=0;i--){							
				Message m=messages[i];	
				String subject = m.getSubject();	
				String uid = "";
				if("pop".equals(revType)){
					uid = ((POP3Folder)folder).getUID(m);					
				}else if("imap".equals(revType)){
					uid=String.valueOf(((IMAPFolder)folder).getUID(m));
				}
				ReceiveMimeMessage rmm=new ReceiveMimeMessage((MimeMessage)m);
				String from=rmm.getFrom();
				String senddate=rmm.getSentDate();
				/*try {
					//System.out.println(MimeUtility.decodeText(m.getHeader("Subject")[0]));
					System.out.println(MimeUtility.decodeText(m.getHeader("Content-Type")[0]));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}*/	
				if (subject!=null) {
					System.out.println(i+"�����ڣ�"+senddate+"��msgid:"+uid+"�����⣺"+subject+"");
				}else{
					System.out.println(i+",ʱ�䣺"+senddate+"��msgid:"+uid+":����Ϊ:null");
					//count++;					
				}				
				if(from!=null&&"".equals(from.trim())){
					System.out.println("�õ�ַ�޷�����");
				}else{
					System.out.println("������:"+from);
				}				
			}
			//System.out.println("����Ϊnull���ʼ�����"+count);			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public static void testSendHotmail(){
		String smtp="smtp.live.com";
		String username="entfax2006@hotmail.com";
		String password="qwerty123456";
		String smtpport="587";//25,587,465
		//String sslflag="Y";		
		Mail oneMail=new Mail(smtp,username,password,smtpport);
		oneMail.setNeedAuth(true);
		oneMail.setFrom("fax","entfax2006@hotmail.com");
		oneMail.setTo("entusr@126.com");
		oneMail.setSubject("���ʼ�����Ax");
		//String smtp="smtp.126.com";
		//String username="enttest";
		//String password="qwerty";
		//String smtpport="25";
		//Mail oneMail=new Mail(smtp,username,password,smtpport);
		//oneMail.setFrom("enttest","enttest@126.com");
		//oneMail.setTo("entusr@126.com");		
		
		oneMail.setBody("����");
		String flag=oneMail.sendoutEx();
		System.out.println("���ͳɹ�ô��"+flag);
	}
	public static void testMailConnectionPopImap() {
		String mailpopsslflg = "Y";
		String mailpopport = "995";
		String mailPOP = "pop3.live.com";
		String mailUsr = "entfax2006@hotmail.com";
		String mailPwd = "qwerty123456";
		String message = "";
		Store store = null;
		try {
			MailRecStore storePop = new MailRecStore(mailpopsslflg,
					mailpopport, mailPOP, mailUsr, mailPwd);
			storePop.connect();
			store = storePop.getStore();
		} catch (AuthenticationFailedException authe) {
			message = "POP-IMAP��������¼ʧ��!���������ʺż������Ƿ�������ȷ��������Ϣ��"
					+ authe.getMessage() + ",mailPOP=" + mailPOP + ",MAILUSR="
					+ mailUsr;
		} catch (MessagingException me) {
			message = "POP-IMAP����������ʧ��!����POP�����������Ƿ�������ȷ��MessagingException:"
					+ me.getMessage();
		} catch (Exception e) {
			message = "POP-IMAP����������ʧ��!Exception:" + e.getMessage();
		} finally {
			try {
				store.close();
			} catch (Exception e) {
			}
		}
		if("".equals(message)){
			message="POP-IMAP��������¼�ɹ���"; 
		}
		System.out.println(message);
	}

	public static void testMailConnectionSmtp() {
		
		//String hostName="smtp.126.com";
		//final String mailUsr="enttest@126.com";
		//final String mailPwd="qwerty";
		
		//String hostName="smtp.gmail.com";
		//final String mailUsr="enttest99@gmail.com";
		//final String mailPwd="QWErty123456";
		
		//String hostName = "smtp.live.com";
		//String smtpport = "465";
		//String mailsmtpsslflg = "Y";
		String hostName = "smtp.live.com";
		String smtpport = "25";
		String mailsmtpsslflg = "N";
		final String mailUsr = "entfax2006@hotmail.com";
		final String mailPwd = "qwerty123456";

		Transport transport = null;
		Session mailSession = null;

		String message = "";
		try {
			// �ж������Ƿ�����ssl����
			Properties props = new Properties();// ���ϵͳ���Զ���
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", hostName); // ����SMTP����
			props.put("mail.transport.protocol", "smtp");
			props.setProperty("mail.smtp.port", smtpport);
			props.setProperty("mail.smtp.socketFactory.port", smtpport);
			props.put("mail.smtp.connectiontimeout", "60000");
			props.put("mail.smtp.timeout", "600000");
			if (mailsmtpsslflg.equals("Y")) {
				Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

				props.setProperty("mail.smtp.starttls.enable", "true");
				props.setProperty("mail.smtp.socketFactory.fallback", "false");
				props.put("mail.smtp.quitwait", "false");

				mailSession = Session.getInstance(props, new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mailUsr, mailPwd);
					}
				});
			} else {
				props.setProperty("mail.smtp.socketFactory.class",
						"javax.net.SocketFactory");
				props.setProperty("mail.smtp.socketFactory.fallback", "true");
				mailSession = Session.getInstance(props, null);
			}
		} catch (Exception e) {
			System.err.println("Error in Test3-->" + e.getMessage());
		}
		try {
			transport = mailSession.getTransport("smtp");
			transport.connect(hostName, mailUsr, mailPwd);
		} catch (NoSuchProviderException NSP) {
			message = "SMTP����������ʧ��!NoSuchProviderException:" + NSP.getMessage();
		} catch (AuthenticationFailedException authe) {
			message = "SMTP��������¼ʧ��!���������ʺż������Ƿ�������ȷ��������Ϣ:" + authe.getMessage();
		} catch (MessagingException MSE) {
			message = "SMTP����������ʧ��!MessagingException:" + MSE.getMessage();
		} catch (Exception e) {
			message = "SMTP����������ʧ��!Exception:" + e.getMessage();
		} finally {
			try {
				transport.close();
			} catch (MessagingException MSE) {

			}
		}
		if ("".equals(message)) {
			message = "SMTP��������¼�ɹ���";
		}
		System.out.println(message);
	}

	public static void testHotmail() {
		// pop3.live.com/smtp.live.com
		MailRecStore recStore=new MailRecStore("Y","995","pop3.live.com","entfax2006@hotmail.com","qwerty123456","pop");
		//MailRecStore recStore = new MailRecStore("N", "25", "smtp.live.com",
		//		"entfax2006@hotmail.com", "qwerty123456", "smtp");
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder defd = store.getDefaultFolder();
			Folder[] fds = defd.list();
			for (Folder f : fds) {
				System.out.println(f.getFullName() + "," + f.getName());
			}
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message[] msgs = folder.getMessages();
			System.out.println("�ռ�����ʼ���:" + msgs.length);
			for (Message m : msgs) {
				m.setFlag(Flags.Flag.DELETED, false);
				String subject = m.getSubject();
				System.out.println(subject);
				Address[] from = m.getReplyTo();
				System.out.println("from:" + from[0]);
				System.out.println("address:"
						+ new InternetAddress(from[0].toString()).getAddress());
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void testPopReceive2() {
		// MailRecStore recStore=new
		// MailRecStore("N","110","pop.alibaba.com","","","pop");
		MailRecStore recStore = new MailRecStore("N", "110", "pop.alibaba.com",
				"sales6@myrhinestone.cn", "tehetelin", "pop");
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message[] msgs = folder.getMessages();
			System.out.println("�ռ�����ʼ���:" + msgs.length);
			for (Message m : msgs) {
				String subject = m.getSubject();
				// Address []from=m.getReplyTo();
				// System.out.println("from:"+from[0]);
				// System.out.println("address:"+new
				// InternetAddress(from[0].toString()).getAddress());
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void testPopReceive() {
		String c_mailpopsslflg = "N";
		String c_mailpopport = "110";
		String tmpPopAddr = "pop.126.com";
		String tmpUser = "entfax";
		String tmpPasswd = "qwerty";
		String revType = "pop";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			FetchProfile profile = new FetchProfile();
			profile.add(UIDFolder.FetchProfileItem.UID);
			Message[] messages = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			folder.fetch(messages, profile);
			System.out.println(messages.length);
			for (Message m : messages) {
				String subject = m.getSubject();
				if (subject.contains("����") || subject.contains("����")) {
					System.out.println(subject);
				}
			}			
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void testImapReceive() {
		String c_mailpopsslflg = "N";
		String c_mailpopport = "143";
		String tmpPopAddr = "imap.163.com";
		String tmpUser = "enters1";
		String tmpPasswd = "entsoft";
		String revType = "IMAP";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			// 126,163:INBOX(�ռ���)��Sent Items(������),Junk
			// E-mail(�����ʼ�),Drafts(�ݸ�),Deleted Items(��ɾ��,���������),
			// sina�ķ�����������ʲô��֪��
			// qq���䣺
			FetchProfile profile = new FetchProfile();
			profile.add(UIDFolder.FetchProfileItem.UID);

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message[] message = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			// folder.fetch(message, profile);
			System.out.println("�ռ��䣺" + message.length);
			// for(Message m:message){
			// String uid = String.valueOf(((IMAPFolder)folder).getUID(m));
			// System.out.println(uid);
			// }

			Folder folder2 = store.getFolder("Sent Items");
			folder2.open(Folder.READ_WRITE);
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder2.open(Folder.READ_WRITE);
			}
			Message[] message2 = folder2.getMessages();
			System.out.println("�����䣺" + message2.length);
			for (Message m : message2) {
				String uid = String.valueOf(((IMAPFolder) folder2).getUID(m));
				System.out.println(uid);
			}

			folder.close(true);
			folder2.close(true);
			store.close();
			recStore.disConnect();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public static void testImapReceiveGmail2() {//imap:993,smtp:587
		String c_mailpopsslflg = "Y";
		String c_mailpopport = "993";//993,
		String tmpPopAddr = "pop.gmail.com";
		//String tmpUser = "saiausb0001@gmail.com";
		//String tmpPasswd = "usbflash198484";
		String tmpUser="imcflash@gmail.com";
		String tmpPasswd="Kayla5197202";
		String revType = "IMAP";//IMAP,POP
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			Folder folder = store.getDefaultFolder();
			Folder[] fds=folder.list();
			for(int i=0;i<fds.length;i++){
				System.out.println(fds[i].getFullName()+":"+fds[i].getName());
			}
			Folder sent=store.getFolder("INBOX");
			
			FetchProfile profile = new FetchProfile();			
			profile.add(UIDFolder.FetchProfileItem.UID);
			profile.add("Subject");
			profile.add("From");
			sent.open(Folder.READ_WRITE);				
			Message []msgs=sent.getMessages();
			System.out.println("�ռ����ʼ�������"+msgs.length);
			sent.fetch(msgs, profile);			
			
			int count=0;
			for(int i=msgs.length-1;i>=0;i--){
				Message m=msgs[i];
				ReceiveMimeMessage msg=new ReceiveMimeMessage((MimeMessage)m);
				String subject=msg.getSubject();	
				//System.out.println(subject);
				System.out.println("=============������ȡ��"+(count++)+"���ʼ�======================");
				String uid = String.valueOf(((IMAPFolder)sent).getUID(m));
				System.out.println("msg_id:"+uid);				
				System.out.println("���⣺"+subject);
				System.out.println("���ڣ�"+msg.getSentDate());
				//System.out.println("----------------����Start-----------------------");				
				//msg.getMailContent(m);
				//System.out.println(msg.getBodyText());
				//System.out.println("----------------����End-----------------------");
				//msg.setAttachPath("src/");
				//msg.setInlineImgPath("src/");
				//msg.saveImgAttach();
				//msg.saveAttachMent();
				//String attachFilename=msg.getAttachmentFileName();
				//HashMap imgMap=msg.getHmImg();
				//System.out.println(attachFilename);
				//System.out.println(imgMap);
				System.out.println("==============��"+(count++)+"����ȡ���=====================");
			}
			sent.close(true);
			store.close();
			recStore.disConnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void testImapReceiveGmail() {
		String c_mailpopsslflg = "Y";
		String c_mailpopport = "993";
		String tmpPopAddr = "imap.gmail.com";
		String tmpUser = "enttest99";
		String tmpPasswd = "QWErty123456";
		String revType = "IMAP";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();		

			Folder folder = store.getDefaultFolder();
			Folder[] fds=folder.list();
			for(int i=0;i<fds.length;i++){
				System.out.println(fds[i].getFullName()+":"+fds[i].getName());
			}
			Folder sent=store.getFolder("Sent");
			sent.open(Folder.READ_WRITE);
			Message []msgs=sent.getMessages();
			System.out.println("�ѷ����ʼ�������"+msgs.length);
			for(Message m:msgs){
				System.out.println(m.getSubject());
			}
			sent.close(true);
			store.close();
			recStore.disConnect();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	public static void testImapReceive2() {
		String c_mailpopsslflg = "N";
		String c_mailpopport = "143";
		String tmpPopAddr = "pop3.sinoriver.com";
		String tmpUser = "enter@sinoriver.com";
		String tmpPasswd = "sino5656";
		String revType = "IMAP";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			// 126,163:INBOX(�ռ���)��Sent Items(������),Junk
			// E-mail(�����ʼ�),Drafts(�ݸ�),Deleted Items(��ɾ��,���������),
			// sina�ķ�����������ʲô��֪��
			// qq���䣺
			FetchProfile profile = new FetchProfile();
			profile.add(UIDFolder.FetchProfileItem.UID);

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message[] message = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			// folder.fetch(message, profile);
			System.out.println("�ռ��䣺" + message.length);
			// for(Message m:message){
			// String uid = String.valueOf(((IMAPFolder)folder).getUID(m));
			// System.out.println(uid);
			// }
			Folder fd = store.getDefaultFolder();
			Folder[] fds = fd.list();
			System.out.println("�ļ��и�����" + fds.length);
			for (Folder f : fds) {
				System.out.println(f.getFullName() + "," + f.getName());
			}
			// �ռ��䣬��������ʲô
			Folder folder2 = store.getFolder("out");
			folder2.open(Folder.READ_WRITE);
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder2.open(Folder.READ_WRITE);
			}
			Message[] message2 = folder2.getMessages();
			System.out.println("�����䣺" + message2.length);
			for (Message m : message2) {
				String uid = String.valueOf(((IMAPFolder) folder2).getUID(m));
				System.out.println(uid);
			}

			folder.close(true);
			// folder2.close(true);
			store.close();
			recStore.disConnect();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public static void testImapReceive3() {
		String c_mailpopsslflg = "N";
		String c_mailpopport = "143";
		String tmpPopAddr = "imapcom.263xmail.com";
		String tmpUser = "sophie@qdjtf.com";
		String tmpPasswd = "Qdjtf002";
		String revType = "IMAP";
		MailRecStore recStore = new MailRecStore(c_mailpopsslflg,
				c_mailpopport, tmpPopAddr, tmpUser, tmpPasswd, revType);
		try {
			recStore.connect();
			Store store = recStore.getStore();
			// 126,163:INBOX(�ռ���)��Sent Items(������),Junk
			// E-mail(�����ʼ�),Drafts(�ݸ�),Deleted Items(��ɾ��,���������),
			// sina�ķ�����������ʲô��֪��
			// qq���䣺
			FetchProfile profile = new FetchProfile();
			profile.add(UIDFolder.FetchProfileItem.UID);

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message[] message = folder.getMessages();
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder.open(Folder.READ_WRITE);
			}
			// folder.fetch(message, profile);
			System.out.println("�ռ��䣺" + message.length);
			// for(Message m:message){
			// String uid = String.valueOf(((IMAPFolder)folder).getUID(m));
			// System.out.println(uid);
			// }
			Folder fd = store.getDefaultFolder();
			Folder[] fds = fd.list();
			System.out.println("�ļ��и�����" + fds.length);
			for (Folder f : fds) {
				System.out.println(f.getFullName() + "," + f.getName());
			}
			// �ռ��䣬��������ʲô
			Folder folder2 = store.getFolder("�ѷ���");
			folder2.open(Folder.READ_WRITE);
			if (!folder.isOpen()) { // ����qq�����ر�Folder���б����´�
				folder2.open(Folder.READ_WRITE);
			}
			Message[] message2 = folder2.getMessages();
			System.out.println("�����䣺" + message2.length);
			for (Message m : message2) {
				String uid = String.valueOf(((IMAPFolder) folder2).getUID(m));
				System.out.println(uid);
			}

			folder.close(true);
			// folder2.close(true);
			store.close();
			recStore.disConnect();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
