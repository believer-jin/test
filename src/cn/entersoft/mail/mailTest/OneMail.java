package cn.entersoft.mail.mailTest;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class OneMail {

	/**
	 * 整个MIME邮件对象
	 */
	private MimeMessage mimeMsg;
	/**
	 * 专门用来发送邮件的Session会话
	 */
	private Session session;
	/**
	 * 封装邮件发送时的一些配置信息的一个属性对象
	 */
	private Properties props;
	/**
	 * 发件人的用户名
	 */
	private String username;
	/**
	 * 发件人的密码
	 */
	private String password;
	/**
	 * 用来实现附件添加的组件
	 */
	private MimeMultipart mp;
	//存放正文图片名<key:cid,value:file name>
	private LinkedHashMap<String,String> hmImg=new LinkedHashMap<String,String> ();
	//存放附件名
    private ArrayList vctAffix=new ArrayList();
    //private final static String projectUrl="/emailpro";
    //附件/正文图片存放的绝对路径前半部分（工程名前）
    private String baseUrl="";
    //附件/正文图片存放的相对路径（工程名后，包括工程名）
    private String editorFileUrl="/emailpro/ueditor/jsp/upload/";
    //private String baseUrl="D:/";
    //private final static String editorFileUrl="/ueditor/jsp/upload/";
	/**
	 * 发送统计
	 */
	public static int count=0;
	/**
	 * 附件的list,它的element都是byte[],即图片的二进制流
	 */
	//private LinkedList attachlist;
	public OneMail(){
		this.username="";
		this.password="";
		// 创建邮件
		createMimeMessage();
	}
	/**
	 * 发送参数初始化,有的服务器不需要用户验证，所以这里对用户名和密码进行初始化""
	 * 
	 * @param smtp
	 */
	public OneMail(String smtp) {
		this.username="";
		this.password="";
		// 设置邮件服务器
		setSmtpHost(smtp);
		// 创建邮件
		createMimeMessage();
	}
	public OneMail(String smtp,String username,String password){
		this.username=username;
		this.password=password;
		setSmtpHost(smtp);
		createMimeMessage();
	}
	/**
	 * 设置发送邮件的主机(JavaMail需要Properties来创建一个session对象。
	 * 它将寻找字符串"mail.smtp.host"，属性值就是发送邮件的主机);
	 * 
	 * @param hostName
	 */
	public void setSmtpHost(String hostName) {
		System.out.println("设置系统属性：mail.smtp.host = " + hostName);
		if (props == null)
			props = System.getProperties();
		props.put("mail.smtp.host", hostName);
	}

	/**
	 * (这个Session类代表JavaMail 中的一个邮件session. 每一个基于
	 * JavaMail的应用程序至少有一个session但是可以有任意多的session。 在这个例子中,
	 * Session对象需要知道用来处理邮件的SMTP 服务器。
	 */
	public boolean createMimeMessage() {
		try {
			System.out.println("准备获取邮件会话对象！");
			// 用props对象来创建并初始化session对象
			session = Session.getDefaultInstance(props, null);
		} catch (Exception e) {
			System.err.println("获取邮件会话对象时发生错误！" + e);
			return false;
		}
		System.out.println("准备创建MIME邮件对象！");
		try {
			// 用session对象来创建并初始化邮件对象
			mimeMsg = new MimeMessage(session);
			// 生成附件组件的实例
			// mp = new MimeMultipart();
			mp = new MimeMultipart("related");
		} catch (Exception e) {
			System.err.println("创建MIME邮件对象失败！" + e);
			return false;
		}
		return true;
	}

	/**
	 * 设置SMTP的身份认证
	 */
	public void setNeedAuth(boolean need) {
		System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
		if (props == null)
			props = System.getProperties();
		if (need)
			props.put("mail.smtp.auth", "true");
		else
			props.put("mail.smtp.auth", "false");
	}

	/**
	 * 进行用户身份验证时，设置用户名和密码
	 */
	public void setNamePass(String name, String pass) {
		System.out.println("程序得到用户名与密码");
		username = name;
		password = pass;
	}

	/**
	 * 设置邮件主题
	 * 
	 * @param mailSubject
	 * @return
	 */
	public boolean setSubject(String mailSubject) {
		System.out.println("设置邮件主题！");
		try {
			mimeMsg.setSubject(mailSubject);
		} catch (Exception e) {
			System.err.println("设置邮件主题发生错误！");
			return false;
		}
		return true;
	}

	/**
	 * 设置邮件内容,并设置其为文本格式或HTML文件格式，编码方式为UTF-8
	 * 
	 * @param mailBody
	 * @return
	 */
	public boolean setBody(String mailBody) {
		try {
			System.out.println("开始设置邮件体格式……");			
			BodyPart bp = new MimeBodyPart();
			//bp.setText("");			
			String bodyHtml="<meta http-equiv=Content-Type content=text/html; charset=UTF-8>"
					+ mailBody;
			bodyHtml=processContent(bodyHtml);	
			addInlineImg(mp);
			bp.setContent(bodyHtml,"text/html;charset=UTF-8");
			//bp.setDataHandler(new DataHandler(bodyHtml,"text/html;charset=UTF-8"));
			//mp.setSubType("mixed");
			//mp.setSubType("related");
			mp.setSubType("alternative");
			// 在组件上添加邮件文本			
			mp.addBodyPart(bp);			
		} catch (Exception e) {
			System.err.println("设置邮件正文时发生错误！" + e);
			return false;
		}
		return true;
	}
	public boolean addInlineImg(MimeMultipart mp){
		System.out.println("正在处理邮件正文中图片……");
		if (hmImg!=null&&hmImg.size()>0){
    		try{
    			for(Entry<String, String> entry:hmImg.entrySet()){    		
        			MimeBodyPart bp = new MimeBodyPart();
                    FileDataSource fileds = new FileDataSource(entry.getValue());
                    bp.setDataHandler(new DataHandler(fileds));
                    //bp.setFileName(filename);
                    //bp.setHeader("Content-ID", filename);
                    bp.setContentID("<"+entry.getKey()+">");
                    bp.setHeader("Content-Type","image/jpeg;name=\""+fileds.getName()+"\"");
                    mp.addBodyPart(bp);
        		}
    		}catch(Exception e){ 
    			System.err.println("处理正文图片发生错误！"+e.getMessage());
    			return false;
    		}
    	}
		System.out.println("处理邮件正文中图片结束！");
		return true;		
	}
	/**
	 * 增加发送附件
	 * 
	 * @param filename
	 * 邮件附件的地址，只能是本机地址而不能是网络地址，否则抛出异常
	 * @return
	 */
	public boolean addFileAffix(String filename) {
		System.out.println("增加邮件附件：" + filename);
		try {
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			// 发送的附件前加上一个用户名的前缀
			//bp.setFileName(fileds.getName());
			bp.setFileName(filename);
			bp.setHeader("content-id", filename); 
			// 添加附件
			mp.addBodyPart(bp);
			//mimeMsg.setContent(mp);
		} catch (Exception e) {
			System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
			return false;
		}
		return true;
	}

	/**
	 * 设置发件人地址
	 * 
	 * @param from
	 *            发件人地址
	 * @return
	 */
	public boolean setFrom(String from) {
		System.out.println("设置发信人:"+from);
		try {
			mimeMsg.setFrom(new InternetAddress(from));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 设置收件人地址
	 * 
	 * @param to
	 *            收件人的地址
	 * @return
	 */
	public boolean setTo(String to) {
		if (to == null)
			return false;
		try {
			System.out.println("设置收信人:"+to+"__________");
			mimeMsg.setRecipients(javax.mail.Message.RecipientType.TO,
					InternetAddress.parse(to));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 发送附件
	 * 
	 * @param copyto
	 * @return
	 */
	public boolean setCopyTo(String copyto) {
		System.out.println("抄送到："+copyto);
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
	 * 发送邮件
	 * 
	 * @return
	 */
	public boolean sendout() {
		try {
			mimeMsg.setContent(mp);
			System.out.println("邮件内容—————"+mimeMsg);
			System.out.println(mimeMsg.getSize());
			mimeMsg.saveChanges();
			System.out.println("正在发送邮件....");
			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			// 真正的连接邮件服务器并进行身份验证
			transport.connect(props.getProperty("mail.smtp.host"), username, password);
			// 发送邮件
			transport.sendMessage(mimeMsg, mimeMsg
					.getRecipients(javax.mail.Message.RecipientType.TO));
			System.out.println("发送邮件成功！第" + (count++) + "封");
			transport.close();
		} catch (Exception e) {
			System.err.println("邮件发送失败！" + e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public String processContent(String mailBody){    	
    	//把背景图片进行替换，替换成background="XXX"的形式，因为163邮箱等对style这种格式解析不了，而对background能解析
    	String regEx="style=\"BACKGROUND-IMAGE: url\\(("+editorFileUrl+"\\d+\\.\\w+)\\)\"";
        mailBody=mailBody.replaceAll(regEx, "background=\"$1\"");    
    	        
        int i = 1;        
        regEx="\"("+editorFileUrl+"[a-z0-9A-Z\\(\\)\\+\\^\\~\\%\\&\\#\\\\*-\\_\\@\\$]+\\.\\w+)\"(.*?)/*>";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(mailBody);        
        for (  ; matcher.find() ; ) {   
        	//example: /emailpro/ueditor/jsp/upload/20081124104511160.gif
        	String imgName=matcher.group(1);        	
        	String strFileName=baseUrl+imgName; 
        	//处理正文中丢失的图片：丢失的图片从正文删除并且提示
        	if(!new File(strFileName).exists()){
        		//选定包含指定内容的整个img标签
        		String newEx="<img.*"+matcher.group();
        		mailBody=mailBody.replaceAll(newEx, " ");
        		System.out.println("正文中有丢失的图片，发送时已自动去除！");
        		System.out.println(mailBody);
        	}else{
        		hmImg.put(i+"@taidou.net",strFileName);
            	mailBody=mailBody.replace(imgName, "cid:"+i+"@taidou.net"); 
        	}
        	i++;        	
        }
        return mailBody;
    }
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	public void setEditorFileUrl(String editorFileUrl) {
		this.editorFileUrl = editorFileUrl;
	}
	
	/*public static void main(String[] args) {
		ArrayList<SmtpServer> ssarr = new ArrayList<SmtpServer>();		
		BufferedReader br = null;
		String line = null;
		
		try {
			br = new BufferedReader(new FileReader("server.txt"));			
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

		String mailbody = "<a href=\"http://www.hongyun2000.com/heuimail\"><img src=\"cid:1.jpg\" /></a><br />最近在运营一个新网站，有空去看看，给点意见，谢谢<a href=\"http://www.hongyun2000.com/heuimail\">www.hongyun2000.com</a>";
		themail.setNeedAuth(true);
		themail.setSubject("小娇和小婉新图");
		themail.addFileAffix("D:/1.jpg");// 附件文件路径
		themail.setBody(mailbody);
		
		themail.setFrom(ss.getUser());

		themail.setNamePass(ss.getUser(), ss.getPassword());
		
		
		try {
			br = new BufferedReader(new FileReader("client.txt"));			
			
			while((line = br.readLine())!=null && iter.hasNext())
			{
				long start=System.currentTimeMillis(); 

				System.out.println("====================================================\n准备发邮件给"+line);
				themail.setTo(line);
				themail.sendout();
				long end=System.currentTimeMillis(); //获取运行结束时间
				System.out.println("发送该邮件时间： "+(end-start)+"ms"); 
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
	}*/
	/*内部类，用于登录身份验证
	 * class SmtpAuthenticator extends Authenticator {
	    //SMTP身份验证
	    public SmtpAuthenticator(String username, String password) {
	        this.username = username;
	        this.password = password;
	    }
	    public PasswordAuthentication getPasswordAuthentication() {
	        return new PasswordAuthentication(this.username, this.password);
	    }
	    String username = null;
	    String password = null;
	}*/
}

