/*
 * Modified on 2006.12.06 by zhusy  ���ʼ�ͷ������X-mailer�ֶΣ������������Ϳͻ��˵���ݣ��Է�ֹĳЩ������ϵͳ������ϵͳ�������ʼ�����Ϊ�����ʼ�
 * Modified on 2006.12.27 by zhusy  �޸��ʼ��Ĺ��췽ʽ������ΪMultipart/relate)���Խ��������ǶͼƬ���ʼ���������ĳЩ�ʼ����ߣ�OUTLOOK EXPRESS�����պ���ʾΪ����������
 * Modified on 2006.01.23 by zhusy  ���ⷢ�ʼ��ı����Ϊutf-8,�Խ�����ж��ġ����ĵ����������ַ����ʼ�������Է���ʾΪ���������
 * Modified on 2007.06.01 by zhusy  ���ⷢ�ʼ��ĸ������Ƶı����Ϊutf-8
 * Modified on 2007.06.12 by zhusy  ���¹����ʼ���Ϣ��,�ʼ���ʽ���ӹ淶��
 */
package cn.entersoft.mail.mailTest;
/**
 * @author zhusy
 *  
 */ 
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Map.Entry; 
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import common.log.Logger;
import enterbean.EntAsp;

public class Mail {
	static  Logger log = new Logger();
    private MimeMessage mimeMsg;
    private Session session;
    private Properties props;
    private String username = "";
    private String password = "";
    private boolean isDeBug=true;
    private LinkedHashMap<String,String> hmImg=new LinkedHashMap<String,String> ();
    private ArrayList vctAffix=new ArrayList();
    private String mailBody="";
    private String mailCharSet="utf-8";//GB2312 Ĭ�ϲ�ѡ��Ϊutf-8
    private String mailbodyflg="HTML";
    private String localFilePre="/enterdoc/uploadfile/";
    private String localFilePreBak="style=\"BACKGROUND-IMAGE: url(/enterdoc/uploadfile/";
    private String entEditPath=EntAsp.getEntsoftPath("")+"Entsoft/EnterDoc/uploadfile/";
    private String entEditPathC="C:/jb3-tom4/server/default/deploy/entsoft.war/entereditor/uploadfile/";
    
    private MimeMultipart mpMain = null;

   public Mail() {}
   
   /** 
    * @param smtp
    */
   public Mail(String smtp){
	   setSmtpHost(smtp);
	   createMimeMessage();
   }
   /**
    * 
    * @param smtp
    * @param name
    * @param pass
    * @param smtpport
    */
   public Mail(String smtp,String name, String pass,String smtpport) {
       setSmtpHost(smtp.trim(),name,pass,smtpport);
   } 
   /**
    * 
    * @param smtp
    * @param name
    * @param pass
    */
   public Mail(String smtp,String name, String pass){
       setSmtpHost(smtp.trim(),name,pass,"465");
   }
   /**
    * 
    * @param hostName
    */
   public void setSmtpHost(String hostName) {
   	if (isDeBug)
   		System.out.println("mail.smtp.host = " + hostName);
       if (props == null)
           props = new Properties(); 

       props.put("mail.smtp.host", hostName);
       props.put("mail.smtp.connectiontimeout","60000");
       props.put("mail.smtp.timeout","600000");
       props.setProperty("mail.smtp.socketFactory.class", "");
       props.setProperty("mail.smtp.socketFactory.fallback", "true");
       props.setProperty("mail.smtp.port", "25");
       props.setProperty("mail.smtp.socketFactory.port", "25");
   }
   /** 
    * 
    * @param smtp
    * @param username
    * @param password
    * @param port
    */
   public void setMailParm(String smtp,String username,String password,String port,String sslFlag){
	   this.username = username;
       this.password = password;
	   if (props == null)  props = new Properties(); 
	   props.put("mail.smtp.host",smtp);
	   if (sslFlag!=null&&sslFlag.equals("Y")){//SSL����
		   Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
	       //final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	       props.put("mail.smtp.host",smtp);
	       props.put("mail.transport.protocol", "smtps");
	       props.setProperty("mail.smtp.port",port);
	       props.setProperty("mail.smtp.socketFactory.port",port);
	       //props.setProperty("mail.smtp.socketFactory.class",SSL_FACTORY);
	       props.setProperty("mail.smtp.socketFactory.fallback","false");
	       props.setProperty("mail.smtp.starttls.enable","true");
	       props.put("mail.smtp.quitwait","false");
	   }else{
		   props.setProperty("mail.smtp.socketFactory.class", "");
	       props.setProperty("mail.smtp.socketFactory.fallback", "true");
	       props.setProperty("mail.smtp.port",port);
	       props.setProperty("mail.smtp.socketFactory.port",port);
	   }
	   props.put("mail.smtp.connectiontimeout","60000");
       props.put("mail.smtp.timeout","600000");
       createMimeMessage();
   }
 /**
  * 
  * @param smtp
  * @param name
  * @param pass
  * @param smtpport
  */
   private void setSmtpHost(String smtp,String name, String pass,String smtpport) {
       username = name;
       password = pass;
       if (isDeBug)
           System.out.println("mail.smtp.host = " + smtp);
       if (props == null)
           props = new Properties();
       Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
       //final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
       props.put("mail.smtp.host",smtp);
       props.put("mail.transport.protocol", "smtps");
       props.setProperty("mail.smtp.port",smtpport); 
       props.setProperty("mail.smtp.socketFactory.port",smtpport);
       //props.setProperty("mail.smtp.socketFactory.class",SSL_FACTORY);
       props.setProperty("mail.smtp.socketFactory.fallback","false");
       props.setProperty("mail.smtp.starttls.enable","true");
       props.put("mail.smtp.quitwait","false");//
       props.put("mail.smtp.connectiontimeout","60000");
       props.put("mail.smtp.timeout","600000");
       session = Session.getInstance(props, new Authenticator(){
       protected PasswordAuthentication getPasswordAuthentication() {
           return new PasswordAuthentication(username, password);
       }});
       mimeMsg = new MimeMessage(session);
   }
   /**
    * @param debug
    */
   public void setDeBug(boolean debug){
   	isDeBug=debug;
   }
   
  /**
   * @param mailCharSet
   */
   public void setMailCharacter(String mailCharSet){
   	 this.mailCharSet=mailCharSet;
   }
   /**
    * 
    *
    */    
    public void setReplySign(){	
		try{
			mimeMsg.setHeader("Disposition-Notification-To",mimeMsg.getHeader("From")[0]);
		}catch (MessagingException me){
			System.err.println("setReplySign:"+me.getMessage());
		}
    }
    /**
     * 
     * @param priority
     */
    public void setPriority(String priority){	
		try{
			mimeMsg.setHeader("X-Priority",priority);   
		}catch (MessagingException me){
			System.err.println("setPriority:"+me.getMessage());
		}
    }
    /**
     * 
     * @return
     */
    public boolean createMimeMessage() {
        try {
        	session = Session.getInstance(props, null);
        }catch (Exception e) {
            System.err.println("createMimeMessage1:"+e.getMessage());
            return false;
        }
        try {
            mimeMsg = new MimeMessage(session);
            return true;
        }catch (Exception e) {
            System.err.println("createMimeMessage2:"+e.getMessage());
            return false;
        }
    }
    /**
     * 
     * @param need
     */
    public void setNeedAuth(boolean need) {
      try{
      	if (isDeBug)
      		System.out.println("mail.smtp.auth = " + need);
        if (props == null)
          props = new Properties();
        if (need) {
          props.put("mail.smtp.auth", "true");
        }
        else {
          props.put("mail.smtp.auth", "false");
        }
      }catch(Exception ex){
          System.err.println("setNeedAuth Occurs errors:"+ex.getMessage());
      }
    }
    /**
     * 
     * @param name
     * @param pass
     */
    public void setNamePass(String name, String pass) {
        username = name;
        password = pass;
    }
   /**
    * @param mailSubject
    * @return
    */
    public boolean setSubject(String mailSubject){
    	try {
            mimeMsg.setSubject(MimeUtility.encodeWord(mailSubject,mailCharSet,null));
            return true;
    	}catch(Exception e){
    		System.err.println("setSubject:"+e.getMessage());
    		return false;
    	}
    }
    /**
     * 
     * @param mailbodysendflg
     * @return
     */
    public boolean setBodySendFlG(String mailbodysendflg) {
        try {
        	mailbodyflg=mailbodysendflg;
        	return true;
        }catch (Exception e) {
            System.err.println("setBodySendFlG:"+e.getMessage());
            return false;
        }
    }
    /**
     * 
     * @param mp
     */
    private void addinLineResource(MimeMultipart mp){
        
    	if (hmImg!=null&&hmImg.size()>0){
    		for(Entry<String, String> entry:hmImg.entrySet()){
    			this.addEmbedFile(entry.getValue(),entry.getKey(),mp);
    		}
    	}
    }
    
    public void setinLineResourcePath(String localFilePre,String localFilePreBak,String entEditPath){
    	this.localFilePre=localFilePre;
    	this.localFilePreBak=localFilePreBak;
    	this.entEditPath=entEditPath;
    }
    
    /**
     * �ϴ��ļ���·���滻
     * @param mailBody
     * @return
     */
    public String processContent(String mailBody){
    	
    	//�ѱ���ͼƬ�����滻���滻��background="XXX"����ʽ����Ϊ163����ȶ�style���ָ�ʽ�������ˣ�����background�ܽ���
    	String regEx="style=\"BACKGROUND-IMAGE: url\\((/enterdoc/uploadfile/\\d+\\.\\w+)\\)\"";
        Pattern pattern=Pattern.compile(regEx);
        Matcher matcher=pattern.matcher(mailBody);
        mailBody=matcher.replaceAll("background=\"$1\"");//�б���ͼƬ,�滻��background="XXX"����ʽ
        
    	/*  
    	 *  ���Ĵ�����ͼƬ�� /enterdoc/uploadfile/
    	 *  �滻Ϊʵ�ʵ�·��,���ת���޷���������ͼƬ�����޸Ĵ˴�����
    	 */
        //System.out.println("mailBody:"+mailBody);
        regEx="\"((http://[^<\\s]+)*(/enterdoc/uploadfile/[a-z0-9A-Z\\(\\)\\+\\^\\~\\%\\&\\#\\\\*-\\_\\@\\$]+\\.\\w+))\"(.*?)/*>";
        pattern=Pattern.compile(regEx);
        matcher=pattern.matcher(mailBody);
        
        int i = 1;
        
        for (  ; matcher.find() ; ) {
        	String imgName=matcher.group(1);// example:      D:/enterdoc/uploadfile/20081124104511160.gif
        	String strFileName=entEditPath+imgName.substring(imgName.lastIndexOf("/")+1);
        	hmImg.put((i)+"@entsoft.net",strFileName.replaceAll("&amp;", "&"));
        	mailBody=mailBody.replace(imgName, "cid:"+i+"@entsoft.net"); 
        	i++;
        	
        }
        //System.out.println("mailBody1:"+mailBody);
        
        regEx="\"(\\.\\./entereditor/uploadfile/[a-z0-9A-Z\\(\\)\\+\\^\\~\\%\\&\\#\\\\*-\\_\\@\\$]+\\.\\w+)\"(.*?)/*>";
        
        pattern=Pattern.compile(regEx);
        matcher=pattern.matcher(mailBody);
        
        for (  ; matcher.find() ; ) {
        	
        	String imgName=matcher.group(1);// example:      /enterdoc/uploadfile/20081124104511160.gif
        	
        	String strFileName=entEditPathC+imgName.substring(imgName.lastIndexOf("/")+1);
        	
        	hmImg.put((i)+"@entsoft.net",strFileName);
        	mailBody=mailBody.replace(imgName, "cid:"+i+"@entsoft.net"); 
        	i++;
        	
        }
        
        /* ȥ�����ϰ汾��ʵ�֣���Ҫ�ϰ汾������VSS 2009 5��ǰ�Ĵ��� */
        return mailBody;
    }
    
    /**
     * 
     * @param mailBody
     * @return
     */
    public boolean setBody(String mailBody) {
        this.mailBody=mailBody;
        return true;
    }
    /**
     * 
     * @param strFileName
     * @param strContentID
     * @param mp
     * @return
     */
    private boolean addEmbedFile(String strFileName,String strContentID, MimeMultipart mp){
    	try{
    		 
    		MimeBodyPart bp = new MimeBodyPart();
    		//bp.set
            FileDataSource fileds = new FileDataSource(strFileName);
            bp.setDataHandler(new DataHandler(fileds));
            
            bp.setContentID("<"+strContentID+">");
            bp.setHeader("Content-Type","image/jpeg;name=\""+fileds.getName()+"\"");
            mp.addBodyPart(bp);
            
            return true;
        }catch (Exception e) {
            System.err.println("addEmbedFile:"+e.getMessage());
            return false;
        }
    }
    /**
     * 
     * @param filename
     * @return
     */
    public boolean addFileAffix(String filename) {
        vctAffix.add(filename);
        return true;
    }
	/**
	 * 
	 * @param from
	 * @return
	 */
    public boolean setFrom(String from) {
        try{    
            mimeMsg.setFrom(new InternetAddress(from));
            return true;
        }catch (Exception e) {
            System.err.println("setFrom:"+e.getMessage());
            return false;
        }
    }
    /**
     * 
     * @param name
     * @param add
     * @return
     */
    public boolean setFrom(String name,String add){
        try{
            if (name!=null&&!name.trim().equals("")){
            	name=MimeUtility.encodeWord(name,mailCharSet,null);
                mimeMsg.setFrom(new InternetAddress(add,name));
                return true;
            }else{
                return this.setFrom(add);
            }
        }catch (Exception e) {
            System.err.println("setFrom:"+e.getMessage());
           return false;
        }     
    }
    
    /**
	 * @param to
	 */
    public boolean setTo(String to) {
       return setCommonTo(Message.RecipientType.TO, to);
    }
    /**
     * @param copyto
     */
    public boolean setCopyTo(String copyto) {
    	return setCommonTo(Message.RecipientType.CC,copyto);
    }
    /**
     * @param bcc
     */
    public boolean setBCC(String bcc) {
    	return setCommonTo(Message.RecipientType.BCC,bcc);
    }
    
    /**
     * �����ռ��������ַ
     * @Author yangl
     * Created:2011-3-16 ����03:50:04
     * @param to	
     * @param name
     * @return
     */
    public boolean setTo(String to,String name) {
        return setCommonTo(Message.RecipientType.TO, to,name);
     }
    /**
     * ���ó����������ַ
     * @Author yangl
     * Created:2011-3-16 ����03:52:17
     * @param copyto
     * @param name
     * @return
     */
    public boolean setCopyTo(String copyto,String name) {
    	return setCommonTo(Message.RecipientType.CC,copyto,name);
    }
    /**
     * �����ܼ������������ַ
     * @Author yangl
     * Created:2011-3-16 ����03:52:45
     * @param bcc
     * @param name
     * @return
     */
    public boolean setBCC(String bcc,String name) {
    	return setCommonTo(Message.RecipientType.BCC,bcc,name);
    }
    
    private boolean setCommonTo(javax.mail.Message.RecipientType reciptType,String commonTo,String personName){
   	 if (commonTo == null) return false;
        try {
            StringBuilder strTo=new StringBuilder();
            for(StringTokenizer st = new StringTokenizer(commonTo,";");st.hasMoreTokens();){
           	 InternetAddress ia = new InternetAddress();
           	 ia.setAddress(st.nextToken());
           	 ia.setPersonal(personName,mailCharSet);
           	 mimeMsg.setRecipient(reciptType,ia);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
   }
    private boolean setCommonTo(javax.mail.Message.RecipientType reciptType,String commonTo){
    	 if (commonTo == null) return false;
         try {
             StringBuilder strTo=new StringBuilder();
             for(StringTokenizer st = new StringTokenizer(commonTo,";");st.hasMoreTokens();){
            	 strTo.append(st.nextToken()).append(",");   
             }
             mimeMsg.setRecipients(reciptType,InternetAddress.parse(strTo.toString()));
             return true;
         } catch (Exception e) {
             return false;
         }
    }
    
    
    public int saveEmlAsFile(String filepath,String strFileName){ 
        
        File dest = new File(filepath+strFileName);
        if (dest.exists())dest.delete();
        
        try{
            this.buildMimeMultipart();
            mimeMsg.setContent(mpMain);
            FileOutputStream fos = new FileOutputStream(dest,true);
            mimeMsg.writeTo(fos); 
            fos.close(); 
            if(dest.exists()){
            	System.out.println("Mail.java-->saveEmlAsFile �ʼ��ɹ���Ϊeml��ʽ��"+dest);
            }else{
            	System.out.println("Mail.java-->saveEmlAsFile �ʼ���Ϊeml��ʽʧ��*****��"+dest);
            }
        }catch (FileNotFoundException fe){
            System.err.println("mail/saveEmlAsFile-->fe:"+fe.getMessage());
            return -1;
        }catch (MessagingException me){
            System.err.println("mail/saveEmlAsFile-->me:"+me.getMessage());
            return -1;
        }catch (IOException io){
            System.err.println("mail/saveEmlAsFile-->io:"+io.getMessage());
            return -1;
        }
        return 1;
    }
    /**
     * 
     * @param mpBody
     * @param mailBody
     * @throws MessagingException
     */
    private void addBodyContent(MimeMultipart mpBody,String mailBody) throws MessagingException{
        BodyPart bodyHTML = new MimeBodyPart();
        BodyPart bodyText = new MimeBodyPart();
        int i=mailBody.indexOf("-$-=entsoftmailbodypartitionflag=-$-");
        
        String charsetHead = "";
        
        if("utf-8".trim().equalsIgnoreCase(mailCharSet)){
        	charsetHead = "<meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\" />";
        }
        
        if(i!=-1){	//��HTML��TEXT����ģʽ
        	String strHtml=mailBody.substring(0,i);//HTML����
        	String strText=mailBody.substring(i+36,mailBody.length());//TEXT����
        	
        	
	    	bodyHTML.setContent("<HTML>"+charsetHead+"<BODY>"+strHtml+"</BODY></HTML>", "text/html;charset="+mailCharSet);
	        bodyText.setContent(strText,"text/plain;charset="+mailCharSet);
        }else{		//ֻ��һ��ģʽ
	        bodyHTML.setContent("<HTML>"+charsetHead+"<BODY>"+mailBody+"</BODY></HTML>", "text/html;charset="+mailCharSet);
	        bodyText.setContent("This is a HTML mail, please switch to HTML means check the mail","text/plain;charset="+mailCharSet);
        }
        
        bodyHTML.setHeader("Content-Transfer-Encoding","base64");
        bodyText.setHeader("Content-Transfer-Encoding","base64");
        /* ȥ�����ϰ汾��ʵ�֣���Ҫ�ϰ汾������VSS 2009 5��ǰ�Ĵ���*/
        mpBody.addBodyPart(bodyText);
        mpBody.addBodyPart(bodyHTML);
    }
    /**
     * 
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
     private void buildMimeMultipart() throws MessagingException,UnsupportedEncodingException{
         mailBody = processContent(mailBody);
         mpMain.setPreamble("This is a multi-part message in MIME format.");
         if (vctAffix.size()>0){
             mpMain.setSubType("mixed");
             if (hmImg.size()>0){
                 BodyPart bp = new MimeBodyPart();
                 MimeMultipart mp = new MimeMultipart("related");
                 BodyPart bpBody = new MimeBodyPart();
                 MimeMultipart mpBody = new MimeMultipart("alternative");
                 this.addBodyContent(mpBody,mailBody);
                 bpBody.setContent(mpBody);
                 mp.addBodyPart(bpBody);
                 addinLineResource(mp);
                 bp.setContent(mp);
                 mpMain.addBodyPart(bp);
             }else{
            	 BodyPart bpBody = new MimeBodyPart();
            	 MimeMultipart mpBody = new MimeMultipart("alternative");
            	 this.addBodyContent(mpBody,mailBody);
            	 bpBody.setContent(mpBody);
            	 mpMain.addBodyPart(bpBody);
             }
             for (int i=0;i<vctAffix.size();i++){
                 String filename=(String)vctAffix.get(i);
                 BodyPart bpAffix = new MimeBodyPart();
                 FileDataSource fileds = new FileDataSource(filename);
                 bpAffix.setDataHandler(new DataHandler(fileds));
                 bpAffix.setFileName(MimeUtility.encodeWord(fileds.getName(), mailCharSet, null));
                 mpMain.addBodyPart(bpAffix);
             }
         }else{
        	 if (hmImg.size()>0){
                 mpMain.setSubType("related");
                 BodyPart bpBody = new MimeBodyPart();
                 MimeMultipart mpBody = new MimeMultipart("alternative");
                 this.addBodyContent(mpBody,mailBody);
                 bpBody.setContent(mpBody);
                 mpMain.addBodyPart(bpBody);      
                 addinLineResource(mpMain);
        	 }else{
        		 mpMain.setSubType("alternative");
                 this.addBodyContent(mpMain,mailBody);
        	 }
         }  
     }
     public String sendoutEx() {
     	try {
     	    this.buildMimeMultipart();
             mimeMsg.setSentDate(new Date());
             //mimeMsg.setHeader("X-Mailer","javamail@entsoft");//Foxmail 7.0.1.90[cn]//javamail@entsoft
             mimeMsg.setContent(mpMain);
             mimeMsg.saveChanges();
             Session mailSession = Session.getInstance(props, null);
             Transport transport = mailSession.getTransport("smtp");
             transport.connect((String) props.get("mail.smtp.host"),username,password);
             if (isDeBug)
             	System.out.println("SMTP���������ӳɹ�!");
            transport.sendMessage(mimeMsg, mimeMsg.getAllRecipients());
             if (isDeBug)
             	System.out.println("�ʼ����ͳɹ�!");
             transport.close();
             return ""; 
     	}catch (Exception e) {
     		e.printStackTrace();
            System.err.println("dd:"+e.getMessage());
            String strErr=e.toString();
            if(strErr==null)return "�����ʼ������з����쳣����";
            if(strErr.indexOf("javax.mail.AuthenticationFailedException")!=-1){
            	   return "�ʼ���������֤ʧ��,�������ʼ��ʺ���������";
            }else if(strErr.indexOf("java.net.UnknownHostException")!=-1){
            	   return "�������ʼ���������["+strErr+"]";
            }else if(strErr.indexOf("javax.mail.MessagingException")!=-1){
            	   return "�������ʼ���������["+strErr+"]";
            }else if(strErr.indexOf("java.lang.IllegalMonitorStateException")!=-1){
            	   return "�������ʼ���������["+strErr+"]";
            }else {
                	if (strErr.indexOf("250 Ok: queued as")!=-1)//����ӵ����Ͷ���,���������Ҳ��ɹ�
                	     return "";
                	 else
                	     return "����ʧ��:"+e.toString();
            }
     	}
     }
 	/**
 	 * 
 	 * @param from
 	 * @return
 	 */
	public boolean setSendDate(String sendDate) {
         try{   
        	 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        	 Date date = sdf.parse(sendDate);
        	 mimeMsg.setSentDate(date);
             return true;
             
         }catch (Exception e) {
             System.err.println("setFrom:"+e.getMessage());
             return false;
         }
     }
	
	public String getMsgID() {
		try {
			return mimeMsg.getMessageID();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return "";
}
}
