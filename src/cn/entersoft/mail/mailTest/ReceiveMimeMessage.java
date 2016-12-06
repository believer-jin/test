package cn.entersoft.mail.mailTest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.ContentDisposition;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimePartDataSource;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.ParseException;

import net.freeutils.tnef.Attachment;
import net.freeutils.tnef.TNEFInputStream; 
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.apache.commons.lang.time.DateFormatUtils;

import sun.misc.BASE64Decoder;
import sun.nio.cs.ext.GBK;

import cn.ding.utils.BaseUtils;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.MimeUtil;
import com.sun.mail.util.QDecoderStream;
import com.sun.mail.util.UUDecoderStream;
import common.utils.FileUtils;
import common.utils.Utf7Coder;
import common.utils.Utils;

public class ReceiveMimeMessage{
	
	private static  final  String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
	private static  final  Class<RecipientType> claz = Message.RecipientType.class;
	private static  final  SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	
	private MimeMessage mimeMessage = null;
	private String saveAttachPath = "";
	
	private String inlineRelPath = "/enterdoc/uploadfile/";
	private String inlineImgPath = "";

	private boolean bNeedSaveEml = false;
	
	private StringBuilder bodytextPlain  =  new StringBuilder();
	private StringBuilder bodytextHtml   =  new StringBuilder();
	private StringBuilder sbFileName     =  new StringBuilder();
	
	private HashMap<String,String> hmImg = new HashMap<String,String>();
	
	private int 	intSeqNo = 0;
	
	private static  String osName = System.getProperty("os.name");
	private static final String DEFALT_CHARSET="UTF-8";
	private static  String separator = null ;
	
	private static final Pattern addressTokenizerPattern = Pattern.compile("([^\\s]+?)(\\s|<|$)+");
    private static final Pattern dateTokenizerPattern    = Pattern.compile("(\\d{4}\\s{1}\\d{2}:\\d{2}:\\d{2})");
    private static final Pattern ipTokenizerPattern 	 = Pattern.compile("\\[(.+)\\]");
     
	static{ // System.getProperty("file.separator");
		if ( osName.toLowerCase().contains("win") ) {
			separator = "\\";
		}else {
			separator = "/";
		}
	}
	
	{
		System.setProperty("mail.mime.base64.ignoreerrors","true");
		System.setProperty("mail.mime.parameters.strict", "false");
	}
	
	/**
	 * FCKEditorͼƬԤ�������·��
	 * @Author : qicw
	 * @Date   : May 20, 2009 5:18:31 PM
	 */
	public String getInlineRelPath() {
		return inlineRelPath;
	}
	
	/**
	 * FCKEditorͼƬԤ�������·��
	 * @Author : qicw
	 * @Date   : May 20, 2009 5:18:31 PM
	 */
	public void setInlineRelPath(String inlineRelPath) {
		this.inlineRelPath = inlineRelPath;
	}
	
	
	/**
	 * �쳣����������������ʼ���ʱ�׳���ص��쳣
	 * @Author : qicw
	 * @Date   : May 20, 2009 5:19:53 PM
	 */ 
	private static void messagingExceptionHandler(String exceptDetail) throws MessagingException {
		
		if( exceptDetail==null ) {
			System.err.println("ReceiveMimeMessage.MessagingExceptionHandler().null");
			return;
		}
		String msg = exceptDetail.toLowerCase().toString();
		
		if( msg.contains("pop3") || msg.contains("eof on socket") || msg.contains("folder is not open") 
				|| msg.contains("no inputstream from datasource") || msg.contains("read timed out") ){
			throw new MessagingException(exceptDetail);
		}
		
		System.err.println("ReceiveMimeMessage.MessagingExceptionHandler()."+exceptDetail);
		
	}
	
	/**
	 * @param mimeMessage
	 */
	public ReceiveMimeMessage(MimeMessage mimeMessage){
	    this.mimeMessage = mimeMessage;
	}
	
	/**
	 * @param strEmlFile    
	 * @throws MessagingException
	 */
	public ReceiveMimeMessage(String strEmlFile)  throws MessagingException{
	    this.mimeMessage = importFromEml(strEmlFile);
	}

	public String getAttachmentFileName() {
	  String tmpFileName = sbFileName.toString();
	  if(tmpFileName.length()==0)
	      return "";

	  return tmpFileName.substring(0,tmpFileName.length()-1);
	}
	
	/**
	 * @return
	 * @throws MessagingException
	 */
	public int getMailSize() throws MessagingException {
		try {
		    return mimeMessage.getSize();
		} catch (MessagingException ex){
			System.err.println("ReceiveMimeMessage.getMailSize().");
			messagingExceptionHandler((ex.toString()));
		} catch (Exception exce) {
			System.err.println("getMailSize:"+exce.getMessage());
		}
		return 0;
	}
	
	/**
	 * @return
	 * @throws MessagingException
	 */
	public String getContentType() throws MessagingException{
	    return  mimeMessage.getEncoding();
	}
	
	/**
	 * @return
	 * @author zhusy
	 * @throws Exception 
	 */ 
	public String getReplyTo() throws MessagingException {
		String strReplyTo = null;
		try{
			String tempReplyTo ;
			if(mimeMessage.getReplyTo()!=null && mimeMessage.getReplyTo().length > 0 ){
				tempReplyTo = mimeMessage.getReplyTo()[0].toString();
			} else {
				return "";
			}
			
//			System.out.println("getReplayTo..........."+tempReplyTo);
			strReplyTo = decodeText(tempReplyTo);
			if (null != strReplyTo && !"".equals(strReplyTo.trim()) ){
				strReplyTo= new InternetAddress(strReplyTo).getAddress();
			}
			
		}catch (AddressException ae){
			System.err.println("ReceiveMimeMessage.getReplyTo()"+strReplyTo);
		    strReplyTo = addressCompile(strReplyTo); 
		}catch (MessagingException me){
			System.err.println("getReplyTo:111"+ me.getMessage());
			messagingExceptionHandler((me.toString()));
		}catch (Exception ee){
			System.err.println("MessagingException222:"+ ee.getMessage());
		}
		return dealFrom(strReplyTo);
	}

	private String addressCompile(String strReplyTo) {
		if(strReplyTo==null){
			strReplyTo ="";
		}
		else  {
			strReplyTo.replaceAll("\"", "\\\"");
		}
		Matcher addressTokenizer = addressTokenizerPattern.matcher(strReplyTo);
		
		while (addressTokenizer.find()) {
			String tempAdd = addressTokenizer.group(1);
			if(tempAdd.contains("@")){ 
				strReplyTo = tempAdd.replace(">", "") ;
			}
		}
		return strReplyTo;
	}
	

	public String getOrigFrom() throws MessagingException{
		String strRet  = null;
		String strFrom = null;
		try{
			String tempFrom;
			if( mimeMessage.getHeader("From")!=null && mimeMessage.getHeader("From").length > 0 ){
				tempFrom = mimeMessage.getHeader("From")[0];
			} else {
				return "";
			}
			/**
			 * ���ڲ��淶��header��
			 *  From: "Mark Cheng"
				Sender: "Grace and Joy Ltd." <info@gracenjoy.com>
				To: "'Guy Price'" <priceindustries.ca@gmail.com>
				��ȡSenderΪ������
			 */
			String regEx="(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)";//ȡ�ʼ���ַ��������ʽ		
			Pattern pattern=Pattern.compile(regEx);
			Matcher matcher=pattern.matcher(tempFrom);
			if(!matcher.find()){
				if(mimeMessage.getHeader("Sender")!=null && mimeMessage.getHeader("Sender").length>0){
					tempFrom = mimeMessage.getHeader("Sender")[0];
				}
			}
			strFrom = decodeText(tempFrom);
			
			InternetAddress ia = new InternetAddress(strFrom);
			strRet= ia.getAddress();
			
		} catch (AddressException ae){
			strRet = addressCompile(strFrom);
		} catch (MessagingException me){
			System.err.println("getOrigFrom me:"+me.getMessage());
			messagingExceptionHandler((me.toString()));
		} catch (Exception ee){
			System.err.println("getOrigFrom ee:"+ee.getMessage());
		} 
		return dealFrom(strRet); 
	}

	public String getFrom() throws MessagingException{
		
		String strReplyTo = null ;
		
		try{
			
			strReplyTo = getReplyTo();					//	ȡ���ʼ���replyTo�������ַ
			
			if (null!=strReplyTo && !"".equals(strReplyTo.trim()) && strReplyTo.contains("@")) {
				
	  			/*�ʼ���ַ��������ʽ�������ȡ���ĵ�ַΪ���ʼ���ַ����ȡ*/
				String regEx="(\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*)";//ȡ�ʼ���ַ��������ʽ		
				Pattern pattern=Pattern.compile(regEx);
				Matcher matcher=pattern.matcher(strReplyTo);
				
				if(!matcher.find()){			//�������ʼ���ַ�򷵻�getOrigFrom
					strReplyTo = getOrigFrom();
				} else {
												//replyTo�ĵ�ַ���Ϲ淶��ʲô������
				}
				
			} else {							//�������ʼ���ַ�򷵻�getOrigFrom
				strReplyTo = getOrigFrom();			
			}
			
		} catch(MessagingException ex) {
			System.err.println("ReceiveMimeMessage.getFrom()."+ex.getMessage());
			messagingExceptionHandler((ex.toString()));
			strReplyTo = getOrigFrom();
		} catch(Exception e) {
			System.err.println("��ȡ�ظ���ַ����!"+strReplyTo);
			strReplyTo = getOrigFrom();
		} 
		
		//�������ݿ�like��������126���ַ������������²��� 
		return strReplyTo.length()>126?(strReplyTo.substring(strReplyTo.length()-50, strReplyTo.length())):strReplyTo;
		
	 }
	
	/**
	 * @param type 
	 * @return
	 * @throws MessagingException 
	 */
	public String getMailAddress(String type) throws MessagingException {
		String email = null;
		String addtype = type.toUpperCase();
		try {
			InternetAddress[] address = null;
			if ("TO".equals(addtype) || "CC".equals(addtype)
					|| "BCC".equals(addtype)) {
				/*
				 * if (addtype.equals("TO")) { address = (InternetAddress[])
				 * mimeMessage.getRecipients(Message. RecipientType.TO); }else
				 * if (addtype.equals("CC")) { address = (InternetAddress[])
				 * mimeMessage.getRecipients(Message. RecipientType.CC); }else {
				 * address = (InternetAddress[])
				 * mimeMessage.getRecipients(Message. RecipientType.BCC); }
				 */
				address = (InternetAddress[]) mimeMessage
						.getRecipients((RecipientType) (claz.getField(addtype)
								.get(addtype)));
 
				if (address != null && address.length > 0) {
					for (int i = 0; i < address.length; i++) {
						String emailtmp = address[i].getAddress();
						if (emailtmp == null)
							emailtmp = "";
						else {
							emailtmp = MimeUtility.decodeText(emailtmp);
						}
						if (i == 0)
							email = emailtmp;
						else
							email = email + ";" + emailtmp;

					}
				}
			}
		} catch (Exception e) {
			System.err.println("ReceiveMimeMessage.getMailAddress().");
			messagingExceptionHandler(e.toString());
		}
		return email;
	}
	
	private String subjectPri ;
 

	public String getSubject() throws MessagingException {
		String subject = null;
		String strOrig = null;
		try{
			if (mimeMessage.getHeader("Subject") == null  || mimeMessage.getHeader("Subject").length<=0) return "";
			 
			
			strOrig = mimeMessage.getHeader("Subject")[0];
			
			subjectPri = strOrig; 
			
			//System.out.println("ReceiveMimeMessage.getSubject()"+strOrig);
			
			if (strOrig == null) {
				strOrig = mimeMessage.getSubject();
				if (strOrig==null)
					return strOrig;
			}
			boolean secAna=false;
			
			if(strOrig.toLowerCase().contains("hz-gb-2312"))  secAna=true;
			//���⻻�е��½�������ȥ���ַ����еĿո񡢻س������з����Ʊ��     add by yaozl 2013.12.19
			Pattern p = Pattern.compile("\t|\r|\n");
			Matcher m = p.matcher(strOrig);
			strOrig = m.replaceAll("");

			strOrig = strOrig.replace("\\s\\t\\n\\r", "`");
			subject = decodeText(strOrig);
			
			
			if(secAna) subject = hz2gb (subject);
			
		} catch (MessagingException me){
			System.err.println("Re/me:"+me.getMessage());
			messagingExceptionHandler((me.toString()));
		}
		
		return subject;
	}

	
	
	public String getSenderIP() throws MessagingException{
		String  StrIp = "";
		String  Received="";
		try{
			final String[] headerIp = getHeader("Received");
			
			if ( headerIp!=null && headerIp.length>0) {
				int recsize=headerIp.length;
				
				for(int i=recsize-1 ;i>=0;i--){
					Received = headerIp[i];
					Matcher m = ipTokenizerPattern.matcher(Received);
					if (m.find()) {
						StrIp = m.group(1);
					}else{
						StrIp = ""; 
					}
					if(!"".equals(StrIp))break;
				}
			}
			
		}catch (Exception e) {
			System.err.println("Re/getSenderIP:"+e.getMessage());
			messagingExceptionHandler(e.toString());
			return StrIp;
		}
		return StrIp;
	}

	private String[] getHeader(String headerParam) throws MessagingException {
		return mimeMessage.getHeader(headerParam);
	}
	
	public Date getSentDateTime()throws MessagingException {
		Date sentDate=mimeMessage.getSentDate();

		if(sentDate!=null){
		Date today = new Date();
		if (sentDate.after(today))
			sentDate = today;
		} else {
			return new Date();
		}
		
		return sentDate;
	}
	 
	public String getSentDate() throws MessagingException {
		//���滻Ϊ  ȡ���ʼ�����ʱ��
		return getReceivedDate();
		
	}

	/**
	 * ȡ���ʼ�����ʱ��
	 * @Author : qicw
	 * @Date   : Oct 10, 2009 10:13:33 AM
	 */
	private String getSendDate() throws MessagingException {
		String strDate ="";
		
		Date sendDate = mimeMessage.getSentDate();
		
		if(sendDate == null) {
			strDate = dateFormat.format(new Date(System.currentTimeMillis()));
		} else {
			strDate = compareDate(sendDate);
		}

		return strDate;
	}
	
	/**
	 * ȡ���ʼ�����ʱ��
	 * @throws MessagingException 
	 * @Author : qicw
	 * @Date   : Oct 10, 2009 10:10:48 AM
	 */
	private String getReceivedDate() throws MessagingException {
		
		try{
			
			String strDate="";
			Date receivedDate = mimeMessage.getReceivedDate() ;
			
			if ( receivedDate == null ) {//��� receivedDate Ϊ�գ������ͷ
				//���� Received ͷ
				for (String oneHead : getHeader("Received") ) {		
					for(String onePart:oneHead.split(";")){		//�ԣ��и��ַ���  �� ����������ʽ��ƥ��
						Matcher match = dateTokenizerPattern.matcher(onePart);
						if ( match.find() ) {// �������ڵĸ�ʽ
							strDate = DateFormatUtils.format(new Date(onePart), DATE_FORMAT);
							break;
						}
					}
					//�����ƥ�����ֱ��break
					if(!"".equals(strDate)) break;
				}
				//���strDateû��ƥ�䣬�׳��쳣�����ص�ǰ����
				if("".equals(strDate)) throw new MessagingException();
				
			} else {
				strDate = compareDate(receivedDate);
			}
			
			return strDate;
	
		} catch (Exception ex) {
			 //���ȡ���ʼ�����ʱ������쳣�� ȡ�ʼ�����ʱ��
			return getSendDate();
		}
		
	}
	

	private String compareDate(Date receivedDate) {
		
		String strDate;
		
		Date minDay = new Date("Tue Jan 01 00:00:00 CST 1980");
		Date today  = new Date();
		
		if (receivedDate.compareTo(today) < 0 && receivedDate.compareTo(minDay) >0 ) {
			strDate = dateFormat.format(receivedDate);
		}else{
			strDate = dateFormat.format(today);
		}
		
		return strDate;
	}
	
	public String getBodyText() {
	    return bodytextHtml.toString() + " -$-=entsoftmailbodypartitionflag=-$- " + bodytextPlain.toString();
	}
	
	/**
	 * @throws MessagingException
	 * @throws IOException
	 * @throws Exception
	 */
	public void getMailContent() throws MessagingException,IOException,Exception{
	    getMailContent(mimeMessage); 
	}
	
	public boolean getNeedSaveEml(){
	    return bNeedSaveEml ;
	}
	
	public void setEmlNeedSav(){
		bNeedSaveEml = true ; 
	}
	
	/**
	 * @param part
	 * @throws MessagingException
	 * @throws IOException
	 * @throws Exception
	 */
	public void getMailContent(Part part) throws MessagingException,IOException,Exception  {
		
    	String contenttype = part.getContentType();

    	if (contenttype.toLowerCase().contains("multipart/")){
			MimeMultipart multipart = (MimeMultipart)part.getContent();
			
	   		int intCount = 0 ;
	   		
	   		try{
	   		    intCount = multipart.getCount();
	   		} catch (Exception e) {
	   			System.err.println("getCount occur errors:"+e.getMessage());
	   			if(e.getMessage()!=null){
		   			messagingExceptionHandler(e.toString());
	   			}
	   		}

	       for ( int i = 0 ; i < intCount ; i++ ) {
	           BodyPart mpart = multipart.getBodyPart(i);
	           String disposition = null;
	           if (mpart.getHeader("Content-Disposition")!=null){
	               disposition = mpart.getHeader("Content-Disposition")[0];
	           }
	           
	           if(disposition != null && disposition.toLowerCase().contains("attachment")) continue;
	           if(disposition != null && disposition.toLowerCase().contains("inline")) {
	        	   try{
	        		   if (mpart.getFileName()!=null && !mpart.getFileName().trim().equals("")){
		            	   if(mpart.getContentType().contains("multipart/mixed")){
		            		   //��������´��ڸ���
		            		   continue;
		            	   }
		            	   getMailContent(mpart);
		            	   continue;
		               }
	        	   }catch(Exception e){
	        		   System.out.println("Exception:ReceiveMimeMessage->getMailContent:"+e.getMessage());
	        	   }
	           }
	           getMailContent(mpart);
	       }
	     
    	}else if(contenttype.toLowerCase().contains("text/html")||
    			 contenttype.toLowerCase().contains("text/plain")){

    		String disposition=null;
    	    if (part.getHeader("Content-Disposition")!=null){
	           disposition = part.getHeader("Content-Disposition")[0];
	        }
	        if(disposition != null &&disposition.toLowerCase().contains("attachment")) return;
	        
	        if (contenttype.toLowerCase().contains("charset")){
		   	    try{
		   	    	if(contenttype.toLowerCase().contains("text/html")){
		   	    		/*******************************--1--�������´���,�ʼ�����������*******************************/
		   	    		String contentUnsupported = "";
		   	    		if((MimeUtility.getEncoding(part.getDataHandler()).equals("quoted-printable"))){
		   	    			//gb2312��֧�ַ��壬Ϊ��ֹ���룬ʹ�ð���gb2312�ַ�����gbk����
		   	    			try{
		   	    				if(contenttype.toLowerCase().contains("gb2312")){
			   	    				part.setHeader("Content-Type", "text/html;charset=GBK");
			   	    			}	
		   	    			}catch(Exception e){
		   	    				System.out.println("ReceiveMimeMessage:->getMailContent:"+e.getMessage());
		   	    			}		   	    			
		   	    			String temphtml = (String)part.getContent();
		   	    			if(temphtml.toLowerCase().indexOf("charset=shift_jis")!=-1){
		   	    				String partCharSet = "";
		   	    				String ctypeCharSet = "";
		   	    				String regex = "charset=(.*?)\"";//����
		   	    		        Pattern pattern = Pattern.compile(regex);
		   	    		        Matcher matcher = pattern.matcher(temphtml.toLowerCase());
			   	    		     while(matcher.find()) {
			   	    		    	partCharSet = matcher.group(1);
			   	    		     }
			   	    		     regex = "charset=\"(.*?)\"";//part.getContentType()
			   	    		     pattern = Pattern.compile(regex);
			   	    		     matcher = pattern.matcher(contenttype.toLowerCase());
			   	    		     while(matcher.find()) {
			   	    		    	ctypeCharSet = matcher.group(1);
			   	    		     }
			   	    		     if(!partCharSet.equals("")&&!ctypeCharSet.equals("")&&!partCharSet.equals(ctypeCharSet)){
			   	    		    	contentUnsupported = parseContentForUnsupportedEncoding(part.getInputStream(),partCharSet);
			   	    		     }
		   	    			}
		   	    		}
		   	    		if(!contentUnsupported.equals("")){
		   	    			bodytextHtml.append(contentUnsupported);
		   	    			/*******************************--1--end*******************************/
		   	    		}else{
		   	    			bodytextHtml.append((String)part.getContent());
		   	    		}
					}else if(contenttype.toLowerCase().contains("text/plain")){/**����ʼ����ĸ�ʽ��text/plain���޸�ʽ���ģ�*/
		   	    		String contentUnsupported = "";
		   	    		if((MimeUtility.getEncoding(part.getDataHandler()).equals("quoted-printable"))){
		   	    			//gb2312��֧�ַ��壬Ϊ��ֹ���룬ʹ�ð���gb2312�ַ�����gbk����
		   	    			try{
		   	    				if(contenttype.toLowerCase().contains("gb2312")){
			   	    				part.setHeader("Content-Type", "text/html;charset=GBK");
			   	    			}	
		   	    			}catch(Exception e){
		   	    				System.out.println("ReceiveMimeMessage:->getMailContent:"+e.getMessage());
		   	    			}	
		   	    			
		   	    			String temphtml = ((String)part.getContent());

		   	    			if(temphtml.toLowerCase().indexOf("charset=shift_jis")!=-1){
		   	    				String partCharSet = "";
		   	    				String ctypeCharSet = "";
		   	    				String regex = "charset=(.*?)\"";//����
		   	    		        Pattern pattern = Pattern.compile(regex);
		   	    		        Matcher matcher = pattern.matcher(temphtml.toLowerCase());
			   	    		     while(matcher.find()) {
			   	    		    	partCharSet = matcher.group(1);
			   	    		     }
			   	    		     regex = "charset=\"(.*?)\"";//part.getContentType()
			   	    		     pattern = Pattern.compile(regex);
			   	    		     matcher = pattern.matcher(contenttype.toLowerCase());
			   	    		     while(matcher.find()) {
			   	    		    	ctypeCharSet = matcher.group(1);
			   	    		     }
			   	    		     if(!partCharSet.equals("")&&!ctypeCharSet.equals("")&&!partCharSet.equals(ctypeCharSet)){
			   	    		    	contentUnsupported = parseContentForUnsupportedEncoding(part.getInputStream(),partCharSet);
			   	    		     }
		   	    			}
		   	    		}
		   	    		if(!contentUnsupported.equals("")){
		   	    			bodytextPlain.append(contentUnsupported);
		   	    		}else{
		   	    			bodytextPlain.append((String)part.getContent());
		   	    		}
					}else{
		   	    			bodytextPlain.append((String)part.getContent());
					}
				}catch (UnsupportedEncodingException uex) {
					System.err.println("UnsupportedEncodingException23:"+uex.getMessage());
					String strTmpStr = this.parseContentForUnsupportedEncoding(part.getInputStream(),uex.getMessage());
					if (strTmpStr!=null){
						if(contenttype.toLowerCase().contains("text/html"))
							bodytextHtml.append(strTmpStr);
						else
							bodytextPlain.append(strTmpStr);
					}
				}catch (ClassCastException ce){
					System.err.println("ClassCastException:"+ce.getMessage());
					String strTmpStr = convertSharedISToStr(part.getContent());
					if (strTmpStr!=null&&!strTmpStr.trim().equals("")){
						if(contenttype.toLowerCase().contains("text/html"))
							bodytextHtml.append(MimeUtility.decodeText(strTmpStr));
						else
							bodytextPlain.append(MimeUtility.decodeText(strTmpStr));
					}
				}catch(Exception e){
					  if (e.getMessage().startsWith("Unknown encoding")) {
						  part.removeHeader("Content-Transfer-Encoding");
						  String strTmpStr = (String)part.getContent();
							if (strTmpStr!=null){
								if(contenttype.toLowerCase().contains("text/html"))
									bodytextHtml.append(strTmpStr);
								else
									bodytextPlain.append(strTmpStr);
							}
					  }
				}
			}else{
			    	try{
			    		/**
			    		 * @modify dingsj 2013-06-26
			    		 * ��Ϊ��Щ�ʼ�����content-type��û�мӱ���ķ�ʽ��ʹ��Ĭ�ϱ���ʱ����ʼ���gb2312����ʱ��������룬���Դ�html�ı������в������Ƿ��б��뷽ʽ
			    		 *final String htmlStr = new String(((String)part.getContent()).getBytes("iso-8859-1"),DEFALT_CHARSET);
			    		*/
			    		/**ȡ���ʼ�html��ʽ �ı�����*/
			    		String htmlStr = (String)part.getContent();
			    		/**�ж��Ƿ���gb2312���룬����Ǿ���������뷽ʽΪGBK��������������Ĭ��Ĭ�ϱ���*/
			    		if(htmlStr.toLowerCase().contains("charset=gb2312")){
			    			htmlStr = new String(htmlStr.getBytes("iso-8859-1"),"GBK");
			    		}else{
			    			String charsetString =  BaseUtils.getStreamCharset(BaseUtils.stringToInputStream(htmlStr));
			    			htmlStr = new String(htmlStr.getBytes("iso-8859-1"),charsetString);
			    		}
			    		if(htmlStr.indexOf("begin 6")>-1){
			    			/*����ʹ�� UUENCODE ���� TNEF���ʼ�*/
				    		boolean isUUD  = false;
			    			try{
					    		UUDecoderStream uds = (UUDecoderStream)MimeUtility.decode(part.getInputStream(), "uuencode");
					    		saveFile(uds.getName(),uds);
					    	    isUUD = true;
		    				}catch (MessagingException e) { 
								e.printStackTrace();
							}
			    		
							if(contenttype.toLowerCase().contains("text/html")){
				   				if(!isUUD){
				   					bodytextHtml.append(htmlStr);
				   				} else {
				   					bodytextHtml.append(htmlStr.substring(0,htmlStr.lastIndexOf("begin 6")));
				   				}
				   			} else {
				   				if(!isUUD){
				   					bodytextPlain.append(htmlStr);
				   				} else {
				   					bodytextPlain.append(htmlStr.substring(0,htmlStr.lastIndexOf("begin 6")));
				   				}
				   			}
			    		}else{
				   			if(contenttype.toLowerCase().contains("text/html")){
				   				bodytextHtml.append(htmlStr);
				   			}
				   			else{
				   				bodytextPlain.append(htmlStr);
				   			}
			    		}
					}catch (UnsupportedEncodingException uex) {
						System.err.println("UnsupportedEncodingException:"+uex.getMessage());
						String strTmpStr=this.parseContentForUnsupportedEncoding(part.getInputStream(),uex.getMessage());
						if (strTmpStr!=null){
							if(contenttype.toLowerCase().contains("text/html"))
								bodytextHtml.append(strTmpStr);
							else
								bodytextPlain.append(strTmpStr);
						}
					}catch (ClassCastException ce){
						System.err.println("ClassCastException2:"+ce.getMessage());
						String strTmpStr=convertSharedISToStr(part.getContent());
						if (strTmpStr!=null&&!strTmpStr.trim().equals("")){
							if(contenttype.toLowerCase().contains("text/html"))
								bodytextHtml.append(MimeUtility.decodeText(strTmpStr));
							else
								bodytextPlain.append(MimeUtility.decodeText(strTmpStr));
						}
					}catch(Exception e){
						  if (e.getMessage().startsWith("Unknown encoding")) {
							  part.removeHeader("Content-Transfer-Encoding");
							  String strTmpStr = (String)part.getContent();
								if (strTmpStr!=null){
									if(contenttype.toLowerCase().contains("text/html"))
										bodytextHtml.append(strTmpStr);
									else
										bodytextPlain.append(strTmpStr);
								}
						  }
					}
		   	}
		 } 
    	//��Ϊ�Ѿ��е�������ͼƬ�͸����ķ����ˡ�����о����࣬��ʱȥ�� 2-12-12-17 by tucy
    	/*else if (contenttype.toLowerCase().contains("image/")){
		     if (part.getHeader("Content-ID")==null||part.getHeader("Content-ID").length==0){
			     if (part.getHeader("Content-Disposition")!=null){
			         ContentDisposition contentDisposition = new ContentDisposition(part.getDisposition());
			         if(contentDisposition.getDisposition().equals("inline") && part.getFileName()!=null && !(part.getFileName()).equals("")){
			        	inline�Ҵ����ļ��������渽��  add by linxh
			         }else{
			        	 ContentType content=new ContentType(contenttype);
				         saveFile(content.getParameter("name"),part.getInputStream());
			         }
			     }else{
			    	 ǰ���54�汾��Content-Disposition��Ϊ��null(part.getHeader("Content-Disposition")!=null),
			    	  * ���Լ���,��Ϊ��Content-ID��Content-Disposition��Ϊnull����µ�����ͼƬ  
			    	  * add by linxh on 2010-7-13
			    	  * 
		        	 ContentType content=new ContentType(contenttype);
		        	 if(content.getParameter("name")!=null){
				         saveFile(content.getParameter("name"),part.getInputStream()); 
		        	 }

			     }
		     }
		 }else if (contenttype.toLowerCase().contains("application/")){
			 
		     if (part.getHeader("Content-Disposition")==null){
		         ContentType content=new ContentType(contenttype);
		         String strFileName=content.getParameter("name");
		         if (strFileName==null){
		             if (content.getSubType().equals("msword")){
		                 strFileName="unknow"+intSeqNo+".doc";
		             	 ++intSeqNo;
		             }else if (content.getSubType().equals("msexcel")){
		                 strFileName="unknow"+intSeqNo+".excel";
		                 ++intSeqNo;
		             }
		         }
		         saveFile(strFileName,part.getInputStream());
			 }else{ 
					 try{
						 ContentDisposition contentDisposition = new ContentDisposition(part.getDisposition());
						 if(contentDisposition.getDisposition().equals("inline") && part.getFileName()!=null && !(part.getFileName()).equals("")){
							inline�Ҵ����ļ��������渽��  add by linxh
						 }else{
							 ContentType content=new ContentType(contenttype);
					    	 String strFileName=content.getParameter("name");
					         if (strFileName!=null) {
					        	 saveFile(decodeText(strFileName.replaceAll("\n", "")),part.getInputStream());
					         }
				         }
					 } catch (Exception ce){
						 System.err.println("Exception:"+ce.getMessage());
						 messagingExceptionHandler(ce.toString());
					 }
		     }
		 }*/else if(contenttype.toLowerCase().contains("message/")){
			 try {
				 /*part.isMimeType("message/disposition-notification")�����ݲ���outlook�Ĵ�������Ϊ����*/
				 if(part.isMimeType("message/disposition-notification")){
					 ContentType content=new ContentType(contenttype);
					 String strFileName=content.getParameter("name");
					 if(strFileName==null)strFileName = "unknown.txt";
					 saveFile(strFileName,part.getInputStream()); 
				}
			} catch (Exception e) {
				System.err.println("Exception:"+e.getMessage());
			}
		 }
	}
	/**
	 * 
	 * @param part
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	private String parseContentForUnsupportedEncoding(InputStream is,String errorMsg) throws MessagingException,IOException{
		
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();

	    byte[] buffer = new byte[8192];
	    
	    for (int n = 0;-1 != (n = is.read(buffer));) {
	    	bos.write(buffer, 0, n);
	    }
	    
	    String content = null;
	    errorMsg=Utils.nullToSpace(errorMsg);
	    
	    if( errorMsg.toLowerCase().contains("cp932")){
	    	content = new String(bos.toByteArray(), "shift_jis");
	    }else if( errorMsg.toLowerCase().contains("shift_jis")){
	    	content = new String(bos.toByteArray(), "shift_jis");
	    }else if( errorMsg.toLowerCase().contains("gbk")){
	    	content = new String(bos.toByteArray(), "gbk");
	    }else if( errorMsg.toLowerCase().contains("utf-7")){//java��֧��utf-7���룬ʹ�ù�������롢����
	    	content = Utf7Coder.decode(bos.toByteArray());
	    }else{
	    	content = new String(bos.toByteArray(), DEFALT_CHARSET);
	    }
	    
	    if( errorMsg.toLowerCase().contains("hz-gb-2312"))
	    	content=hz2gb(content);
	    
	    return content ;
	}
	/**
	 * 
	 * @param object
	 * @return
	 * @throws MessagingException 
	 */
	private String convertSharedISToStr(Object object) throws MessagingException{
		
		String strText=null;
		StringBuilder sbtext=new StringBuilder();
		String strLineText=null;
		try{
		    //SharedInputStream sharedInputStream=(SharedInputStream)object;
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader((InputStream)object) );
			while ((strLineText = bufferedReader.readLine()) != null) {
				sbtext.append(strLineText);
			}
		}catch (Exception ie){
			System.err.println("convertSharedInputStreamToString occur error:"+ie.getMessage());
			messagingExceptionHandler(ie.toString());
		}
		
		strText=sbtext.toString();
		
		return strText;
	}

	/**
	* 
	*/
	public boolean getReplySign()throws MessagingException{
	   boolean replysign = false;
	   String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");
	   if (needreply != null) {
	     replysign = true;
	   }
	   return replysign;
	}

	public String  getPriority() throws MessagingException{	
		String  priority="3";
		try{
			String mailpriority[] =mimeMessage.getHeader("X-Priority");   
			if (mailpriority != null && mailpriority.length > 0){
				priority = mailpriority[0];
			}
		}catch (MessagingException me){
			System.err.println("getPriority:"+me.getMessage());
			messagingExceptionHandler((me.toString()));
		}
		return priority;
	}
	
	public void saveAttachMent() throws MessagingException{
	    saveAttachMent(mimeMessage); 
	}
	/**
	 * �����ʼ������ĸ����ķ����������������ȡ
	 * 
	 * @param part
	 * @throws MessagingException
	 */
  	public void saveAttachMent(Part part) throws MessagingException{
  		String fileName = "";
  		String strFileSuffix = "";
  		String strContentType=null;
  		try{
  			if(part.isMimeType("multipart/*")){
  				MimeMultipart mp = (MimeMultipart)part.getContent();
  				if (mp==null) return ;
  				 
  		   		int intCount=0;
  		   		try{
  		   		    intCount = mp.getCount();
  		   		}catch (Exception e){
  		   			System.err.println("saveAttachMent getCount occur errors:"+e.getMessage());
  		   			messagingExceptionHandler(e.toString());
  		   		}
  		   		String strNewBody = getBodyText();
  				for(int i = 0; i < intCount; i++){
  					BodyPart mpart = mp.getBodyPart(i);
  					//������ʼ���ǻ�����ϵͳ����������Ҫ����ѭ������
  					if(mpart.isMimeType("multipart/*") || mpart.isMimeType("message/rfc822") ){
  						saveAttachMent (mpart);
  					}else{//������н���
	  					String disposition = null;
	  					if ( mpart == null ) continue;
	  					if ( mpart.getHeader("Content-Disposition") != null ) {
	  						disposition = mpart.getHeader("Content-Disposition")[0];
	  					}
	  					
	  					if ( mpart == null ) continue;
	  					if ( mpart.getContentType() != null ) {
	  						strContentType = mpart.getContentType();
	  					}
	  					
	  					if (mpart.getHeader("Content-ID")== null && (
	  							(disposition != null&&!disposition.trim().equals("")) 
	  							|| (strContentType!=null && (strContentType.toLowerCase().contains("application/")||strContentType.toLowerCase().contains("image/")))
	  							)){//���Content-IDΪ�գ���disposition��Ϊ�գ�������Ǹ���
							String[] s = getFileFinalNameByTwoNames(mpart);
	  	      	  	 		fileName = s[0];
	  	      	  	 		strFileSuffix = s[1];
							//�ȴ�������
	  	      	  	 		if(fileName==null||"".equals(fileName))fileName = "UnknownFileName";
	  	      	  	 		//�ٴ�������
	  	      	  	 		String strSuffix = getFileType(mpart.getInputStream(),false);//�����ļ���������
							if (strSuffix==null||"".equals(strSuffix)){//�����׺Ϊ���򿴿��ǲ�����������׺
								if (strContentType.contains("text/html")) {
									strSuffix = "html";
								}
							}
							//������ͻ���Ϊ�ջ������ͺ��ļ������Ͳ�һ�£����ļ�������ƴ�ӻ�ȥ
							if(strSuffix==null||"".equals(strSuffix)){
				   				if(strFileSuffix!=null&&!"".equals(strFileSuffix))
				   					strSuffix = strFileSuffix;
				   			}else{
				   				if(strFileSuffix!=null&&!"".equals(strFileSuffix)){//����ļ�����׺��Ϊ��
				   						if(!strSuffix.equals("jpeg".equals(strFileSuffix.toLowerCase())?"jpg":strFileSuffix.toLowerCase()
				   								)){//����ļ�����׺Ϊjpeg��ת����jpg��Ȼ���ʱ���ļ��ĺ�׺�ȶԣ�������ȣ����ļ�����׺�ӻظ��ļ���
				   							fileName+="."+strFileSuffix;
				   						}else{//����ļ�����׺�����ļ���׺�����ļ�����׺�����ļ���׺
				   							strSuffix = strFileSuffix;
				   						}
				   				}
				   			}
							fileName = fileName.trim();//�����,���и����ļ���ǰ���пո��
							
							/**��������ǽ���������ȡ�ø���,�˴�ֻ�ǶԵ���������д���, dingsj 2013-09-12*/
							String attachFile = fileName+"."+strSuffix;
							//if(strContentType != null && strContentType.toLowerCase().contains("application/ms-tnef")&&attachFile.equals("winmail.dat")){
						//	}else{
								saveFile(fileName,strSuffix,mpart.getInputStream());
							//}
	  					}else if(mpart.getHeader("Content-ID")!= null){//���Content-ID��Ϊ��
	  						for (int j=0;j<mpart.getHeader("Content-ID").length;j++){
	  							String strContentID=mpart.getHeader("Content-ID")[j]==null?"":mpart.getHeader("Content-ID")[j].toString();
	  							if(!this.isInLine(strContentID, strNewBody)){//����������ĸ�������洢
	  								String[] s = getFileFinalNameByTwoNames(mpart);
	  	      	      	  	 		fileName = s[0];
	  	      	      	  	 		strFileSuffix = s[1];
	  								//�ȴ�������
	  	      	      	  	 		if(fileName==null||"".equals(fileName))fileName = "UnknownFileName";
	  	      	      	  	 		//�ٴ�������
	  	      	      	  	 		String strSuffix = getFileType(mpart.getInputStream(),false);//�����ļ���������
	  	  							if (strSuffix==null||"".equals(strSuffix)){//�����׺Ϊ���򿴿��ǲ�����������׺
	  	  								if (strContentType.contains("text/html")) {
	  	  									strSuffix = "html";
	  	  								}
	  	  							}
	  	  							//������ͻ���Ϊ�գ����ļ�������ƴ�ӻ�ȥ,
		  	  						if(strSuffix==null||"".equals(strSuffix)){
						   				if(strFileSuffix!=null&&!"".equals(strFileSuffix))
						   					strSuffix = strFileSuffix;
						   			}else{
						   				if(strFileSuffix!=null&&!"".equals(strFileSuffix)){//����ļ�����׺��Ϊ��
					   						if(strSuffix.equals("jpeg".equals(strFileSuffix.toLowerCase())?"jpg":strFileSuffix.toLowerCase())){//����ļ�����׺Ϊjpeg��ת����jpg��Ȼ���ʱ���ļ��ĺ�׺�ȶԣ����һ�������ļ�����׺�����ļ���׺
					   							strSuffix = strFileSuffix;
					   						}else{//����ļ�����׺���ļ���׺��һ�������ļ�����׺�ӻظ��ļ���
					   							fileName+="."+strFileSuffix;
					   						}
						   				}
						   			}
	  	  							fileName = fileName.trim();//�����,���и����ļ���ǰ���пո��
	  	  							saveFile(fileName,strSuffix,mpart.getInputStream());
	  	  						}
	  						}
	  					}
	  				}
  				}    
  			}else if(part.isMimeType("message/rfc822")){
				MimeBodyPart mbp = new MimeBodyPart(part.getInputStream());
				String strFileName=null;
				strFileName=mbp.getHeader("Subject",null);
				if (strFileName==null||strFileName.trim().equals("")) {
					strFileName="unknownEml";
				}else{
					strFileName=decodeText(strFileName);
					saveFile(strFileName,"eml",part.getInputStream());
				}
			}else{//����������ֶ����ǣ�����Ϊ������������
				String disposition = null;
				if ( part == null ) return;
				if ( part.getHeader("Content-Disposition") != null ) {
					disposition = part.getHeader("Content-Disposition")[0];
				}
				
				if ( part.getContentType() != null ) {
					strContentType = part.getContentType();
				}
				
				if ((disposition != null&&!disposition.trim().equals("")) 
						|| (strContentType!=null && strContentType.toLowerCase().contains("application/"))){//�����disposition��Ϊ�գ�ContentTypeΪapplication��������Ǹ���
					String[] s = getFileFinalNameByTwoNames(part);
      	  	 		fileName = s[0];
      	  	 		strFileSuffix = s[1];
					//�ȴ�������
      	  	 		if(fileName==null||"".equals(fileName))fileName = "UnknownFileName";
      	  	 		//�ٴ�������
      	  	 		String strSuffix = getFileType(part.getInputStream(),false);//�����ļ���������
					if (strSuffix==null||"".equals(strSuffix)){//�����׺Ϊ���򿴿��ǲ�����������׺
						if (strContentType.contains("text/html")) {
							strSuffix = "html";
						}
					}
					//������ͻ���Ϊ�ջ������ͺ��ļ������Ͳ�һ�£����ļ�������ƴ�ӻ�ȥ
					if(strSuffix==null||"".equals(strSuffix)){
		   				if(strFileSuffix!=null&&!"".equals(strFileSuffix))
		   					strSuffix = strFileSuffix;
		   			}else{
		   				if(strFileSuffix!=null&&!"".equals(strFileSuffix)){//����ļ�����׺��Ϊ��
		   						if(!strSuffix.equals("jpeg".equals(strFileSuffix.toLowerCase())?"jpg":strFileSuffix.toLowerCase()
		   								)){//����ļ�����׺Ϊjpeg��ת����jpg��Ȼ���ʱ���ļ��ĺ�׺�ȶԣ����һ�������ļ�����׺�����ļ���׺
		   							strSuffix = strFileSuffix;
		   						}else{//����ļ�����׺�����ļ���׺�����ļ�����׺�ӻظ��ļ���
		   							fileName+="."+strFileSuffix;
		   						}
		   				}
		   			}
					fileName = fileName.trim();//�����,���и����ļ���ǰ���пո��
					saveFile(fileName,strSuffix,part.getInputStream());
				}
			}
  			delDatFile();
  		}catch (MessagingException me){
  			System.err.println("re/me2:"+me.getMessage());
  			messagingExceptionHandler((me.toString()));
  		}catch (IOException io){
  			System.err.println("re/me2:"+io.getMessage());
  		}
  	}
  	
  	public void saveImgAttach() throws MessagingException{
	    saveImgAttach(this.mimeMessage);
	}
	
  	/**
	 * �洢�ʼ�����ͼƬ�������ʼ������ʼ���������ǶͼƬ������
	 * ֻ���ļ��޷�ȷ������ʱ���Ż���������
	 * 
	 * @param part  �ʼ�����
	 * @throws MessagingException 
	 */
	public void saveImgAttach(Part part) throws MessagingException{
		try{
			if (part.isMimeType("multipart/*")){
				Multipart multipart = (Multipart)part.getContent();	
				int counts = multipart.getCount();
				String strNewBody = getBodyText();
				for(int i = 0; i < counts; i++){
					
				   	BodyPart bodyPart=multipart.getBodyPart(i);
                    if(bodyPart.getHeader("Content-ID")!= null && bodyPart.getHeader("Content-ID").length>0){//���Content-ID��Ϊ�գ��������������ĸ���
					   for (int j=0;j<bodyPart.getHeader("Content-ID").length;j++){
				   		    String strContentID=bodyPart.getHeader("Content-ID")[j]==null?"":bodyPart.getHeader("Content-ID")[j].toString();
				   		 
					   		if (isInLine(strContentID,strNewBody)){//���Content-ID���������ҵ���Content-ID��Ϊ�գ���Ϊ����ͼƬ
					   			if (strContentID.contains("*")||strContentID.contains("?")||strContentID.contains("+")) continue;//contentid�������������򲻴���
					   			//����ļ�����
					   			String[] fileNameMessage = getFileFinalNameByTwoNames(bodyPart);
					   			String strFileName=fileNameMessage[0];
					   			String strFileSuffix=fileNameMessage[1];
					   			
					   			//1.���ļ�����ֵ����
			   					if (strFileName==null||"".equals(strFileName)){//1.����ļ���Ϊ�գ���Ĭ�ϸ�����ֵ
		   							strFileName="unknown("+intSeqNo+")";
			   						++intSeqNo;
				   				}
					   			
					   			//2.��ȡ���ļ�����ĺ�׺
					   			String strSuffix = getFileType(bodyPart.getInputStream(),false);
					   			if(strSuffix==null){//2.����ò���ʵ�ʵ��ļ����ͣ��ʹ��ʼ���contentType��ȡ
						   			//���ContentType��ͼƬ��ʽ��
					   				if(bodyPart.getContentType().contains("image/")){//�����ͼƬ��ʽ
					   					//��ȡ�ļ�����
					   					String contentType = bodyPart.getContentType();
					   					strSuffix = contentType.substring("image/".length(), bodyPart.getContentType().length());//contentType��׺
					   					if(strSuffix.contains(";"))
					   					strSuffix = strSuffix.substring(0,strSuffix.indexOf(";"));
					   				}else{//3.���contentTypeҲû�У���Ϊ�գ�������Կս��д���
					   					if(strSuffix == null)strSuffix="";//����ļ�����ʵ��ȡ������������Ϊ��
					   				}
					   			}
					   			
					   			//3.ȷ�������ĺ�׺ֵ
					   			if(strSuffix==null||"".equals(strSuffix)){
					   				if(strFileSuffix==null||"".equals(strFileSuffix))
					   					continue;
					   				else
					   					strSuffix = strFileSuffix;
					   			}else{
					   				if(strFileSuffix!=null&&!"".equals(strFileSuffix)){//������Լ�⵽�ļ����ͣ���Ѱ����ƽ��������ͼӻ�ȥ
					   					//�����jpeg�����ļ�����׺�ĳ�jpg��Ȼ����ļ���׺�Աȣ������һ�������ļ���׺�����ļ�����׺
					   					if(strSuffix.equals("jpeg".equals(strFileSuffix.toLowerCase())?"jpg":strFileSuffix.toLowerCase())){
					   						strSuffix = strFileSuffix;
					   					}else{//����ӻ��ļ���
					   						strFileName+="."+strFileSuffix;
					   					}
					   				}
					   			}
					   			
				   				if(strFileName.contains("\\"))strFileName=strFileName.substring(strFileName.lastIndexOf("\\")+1);
				   				saveImgFile(strContentID,strFileName,strSuffix,bodyPart.getInputStream());
					   		}
				   		}
					}else{
				   		saveImgAttach((multipart.getBodyPart(i)));
				   	}
				}
			}
		}catch (MessagingException me){
			System.err.println("rece/me2:"+me.getMessage());
			messagingExceptionHandler((me.toString()));
		}catch (IOException io){
	      	System.err.println("rece/io2:"+io.getMessage());
	    }
	}
	
	/**
	 * @param attachpath
	 */
	public void setAttachPath(String attachpath){
		
		mkDirs(attachpath);
		this.saveAttachPath = attachpath;
	}
	
	
	public String getAttachPath(){
	  return saveAttachPath;
	}
	
	
	public void setInlineImgPath(String inlineImgPath){	
		mkDirs(inlineImgPath);
	    this.inlineImgPath=inlineImgPath;
	}
	
	private static void mkDirs(String inlineImgPath) {
		File dest = new File(inlineImgPath);
		if(!dest.exists()) dest.mkdirs();
	}
	
	
	public String getInlineImgPath(){
	    return inlineImgPath;
	}


/**
 * 
 * @param strFileName
 * @return
 * @throws MessagingException 
 */
	public int saveEmlAsFile(String strFileName) throws MessagingException {
		String storedir = getAttachPath();
		String strFile = storedir + "/" + strFileName;
		File dest = new File(strFile);
		if (dest.exists())
			dest.delete();
		try {
			FileOutputStream fos = new FileOutputStream(dest, true);
			mimeMessage.writeTo(fos);
			sbFileName.append(strFileName + ";");
			fos.close();
		} catch (FileNotFoundException fe) {
			System.out.println("rece/fe:" + fe.getMessage());
			return -1;
		} catch (MessagingException me) {
			System.out.println("rece/me:" + me.getMessage());
			messagingExceptionHandler((me.toString()));
			return -1;
		} catch (IOException ie) {
			System.out.println("rece/ie" + ie.getMessage());
			return -1;
		}
		return 1;
	}
	
	public void exportToFile(String storedir, String uid) {
		mkDirs(storedir);
		String strFile  = storedir + "/"+uid+".eml";
		try {
			FileOutputStream fos = new FileOutputStream(strFile, true);
			mimeMessage.writeTo(fos);
			fos.close();
			
			FileUtils.fileWriter(storedir+"/"+uid+".txt", "ISO8859_1", updBodyForImg().replaceAll("cid:", ""));
			
		} catch (FileNotFoundException fe) {
			System.out.println("rece/fe:" + fe.getMessage());
		} catch (MessagingException me) {
			System.out.println("rece/me:" + me.getMessage());
		} catch (IOException ie) {
			System.out.println("rece/ie" + ie.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param fileName
	 * @param in
	 */
	public void saveFile(String fileName,InputStream in) {
		
		
		if (fileName==null||fileName.trim().equals("")) return ;
	  
		String storedir = getAttachPath();
		int intLastPos=-1;  
		intLastPos=fileName.lastIndexOf("\\");
	  	if (intLastPos>0){  
	  		fileName=fileName.substring(intLastPos+1,fileName.length());
	  	}

	  	
	  	
	  	fileName=decodeWord(fileName);
	  	fileName=dealSpecialCharForFileName(fileName);
	  	int intLastDotPos=fileName.lastIndexOf(".");
	  	String strName;
	  	String strSuffix="";
	  	if (intLastDotPos>0){
	  		strSuffix =fileName.substring(intLastDotPos,fileName.length());
	  		if (strSuffix.contains("?")){
	  			strSuffix=strSuffix.substring(0,strSuffix.indexOf("?"));
	  		}
	  	}else{
	  		strName= fileName;
	  	}
	  	//System.out.println(fileName);
	  	if (fileName.contains("?")) {
	  		
	  		fileName="unknown"+intSeqNo+strSuffix;
	  		intLastDotPos=("unknown"+intSeqNo).toString().length();
	  		++intSeqNo;
	  	}
	
	  	if (intLastDotPos>0){
	  		strName=fileName.substring(0,intLastDotPos);
	  	}else{
	      	strName=fileName;
	  	}
	  	if (fileName.length()>255) {
	  		fileName=strName.substring(0,255-strSuffix.length())+strSuffix;
	  	}
	  	File storefile = new File(storedir + separator + fileName);
	  	int i=0;
	  	if (storefile.exists()){
	  		fileName=strName+"("+i+")"+strSuffix;
	  		storefile = new File(storedir + separator + fileName);
	  		while (storefile.exists()){
	  			i++;
	  			fileName=strName+"("+i+")"+strSuffix;
	  			storefile = new File(storedir + separator + fileName);
	  		}
	  	}
	  	
	  	BufferedOutputStream bos = null;
	  	BufferedInputStream bis = null;
	  	
	  	try {
	  		bos = new BufferedOutputStream(new FileOutputStream(storefile));
	  		bis = new BufferedInputStream(in);
	  		int c;
	  		int k=0;
	  		
	  		byte[] tempBt = new byte[8192];
	  		
	  		while ( (c = bis.read(tempBt)) != -1) {
	  			bos.write(tempBt,0,c);
	  			k++;
	  			if( k % 4 == 0 ) bos.flush();
	  		}
	  		if(storefile.exists()){
	  			System.out.println("ReceiveMimeMessage.java-->saveFile �������Ƴɹ������Ƶ���"+storefile);
	  		}else{
	  			System.out.println("ReceiveMimeMessage.java-->saveFile ��������ʧ��*****�����Ƶ���"+storefile);
	  		}
	  		
	  		
	  	}catch (FileNotFoundException fe){
	  		System.err.println("re/fe:"+fe.getMessage());
	  		return;
	  	}catch (IOException ie){
	  		System.err.println("re/ie:???"+ie.getMessage());
	  		return;
	  	}
	  	finally {
	  		try{
				if(bos!=null)bos.close();
				if(bis!=null)bis.close();
	  		}catch (IOException io){
	  			System.err.println("re/io:"+io.getMessage());
	  		}
	  	}
	  	
	  	sbFileName.append(fileName+";");
	  	
  		if(isNeedAttach(fileName)){
  			try {
				webMailAttach(new net.freeutils.tnef.Message(new TNEFInputStream(getAttachPath()+"/"+fileName)),getAttachPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
  		}
  		
	}
	
	/**
	 *
	 * @param fileName
	 * @param in
	 */
	public void saveFile(String fileName,String strSuffix, InputStream in) {
		if (fileName==null||fileName.trim().equals("")) return ;
		if (strSuffix==null) strSuffix = "" ;
		if(!"".equals(strSuffix))strSuffix = "."+strSuffix;
		String storedir = getAttachPath();
		//String storedir = "E:\\";
		int intLastPos=-1;  
		intLastPos=fileName.lastIndexOf("\\");
	  	if (intLastPos>0){  
	  		fileName=fileName.substring(intLastPos+1,fileName.length());
	  	}

	  	
	  	
	  	fileName=decodeWord(fileName);
	  	fileName=dealSpecialCharForFileName(fileName);
	  	String strName = "";
	  	if(fileName!=null||!"".equals(fileName.trim()))
	  		strName=fileName;
	  	else
	  		strName="UnknownFileName";
	  	//System.out.println(fileName);
	  	if (fileName.contains("?")) {
	  		fileName="unknown"+intSeqNo;
	  		++intSeqNo;
	  		strName=fileName;
	  	}
	  	
	  	
	  	if (fileName.length()>255) {
	  		fileName=strName.substring(0,255-strSuffix.length());
	  	}
	  	File storefile = new File(storedir + separator + fileName+strSuffix);
	  	int i=0;
	  	if (storefile.exists()){
	  		fileName=strName+"("+i+")";
	  		storefile = new File(storedir + separator + fileName+strSuffix);
	  		while (storefile.exists()){
	  			i++;
	  			fileName=strName+"("+i+")";
	  			storefile = new File(storedir + separator + fileName+strSuffix);
	  		}
	  	}
	  	
	  	BufferedOutputStream bos = null;
	  	BufferedInputStream bis = null;
	  	
	  	try {
	  		bos = new BufferedOutputStream(new FileOutputStream(storefile));
	  		bis = new BufferedInputStream(in);
	  		int c;
	  		int k=0;
	  		System.out.println("-------------------->"+storefile.getPath());
			System.out.println("�������뷽ʽ--------------------->"+BaseUtils.getFileCharset(storefile.getPath()));
			System.out.println(BaseUtils.characterString(fileName,"gbk"));
	  		byte[] tempBt = new byte[8192];
	  		
	  		while ( (c = bis.read(tempBt)) != -1) {
	  			bos.write(tempBt,0,c);
	  			k++;
	  			if( k % 4 == 0 ) bos.flush();
	  		}
	  		if(storefile.exists()){
	  			System.out.println("ReceiveMimeMessage.java-->saveFile �������Ƴɹ������Ƶ���"+storefile);
	  		}else{
	  			System.out.println("ReceiveMimeMessage.java-->saveFile ��������ʧ��*****�����Ƶ���"+storefile);
	  		}
	  		
	  		
	  	}catch (FileNotFoundException fe){
	  		System.err.println("re/fe:"+fe.getMessage());
	  		return;
	  	}catch (IOException ie){
	  		System.err.println("re/ie:???"+ie.getMessage());
	  		return;
	  	}
	  	finally {
	  		try{
				if(bos!=null)bos.close();
				if(bis!=null)bis.close();
	  		}catch (IOException io){
	  			System.err.println("re/io:"+io.getMessage());
	  		}
	  	}
	  	
	  	sbFileName.append(fileName+strSuffix+";");
	  	
		if(isNeedAttach(fileName+strSuffix)){
			try {
				webMailAttach(new net.freeutils.tnef.Message(new TNEFInputStream(getAttachPath()+"/"+fileName+strSuffix)),getAttachPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
 		
	}
	/**
	 * 
	 * @param strFileName
	 * @param in
	 */
	private void saveImgFile(String strContentID,String strFileName,InputStream in){
		if (strFileName==null||strFileName.trim().equals("")) return;
		if (in==null) return;
		String storedir=getInlineImgPath();
		
		if (storedir == null || storedir.trim().equals("")) storedir=getAttachPath();
		if (storedir == null || storedir.equals("")) return ;   

		int i=0;
		String strSuffix="";
		String strName="";
		
		strFileName=decodeWord(strFileName);
		strFileName=dealSpecialCharForFileName(strFileName);
		
		if(osName.toLowerCase().contains("win"))
			strFileName = strFileName.replaceAll("\\?", ""); // ȥ��windows�����ݷ���
		
		int intLastPos=strFileName.lastIndexOf(".");
		if (intLastPos>0){
			strSuffix=strFileName.substring(intLastPos,strFileName.length());
			if (strSuffix.contains("?")){
	  			strSuffix=strSuffix.substring(0,strSuffix.indexOf("?"));
	  		}
			strName=strFileName.substring(0,intLastPos); 
		}else{
			strName=strFileName;
		}
		if (strFileName.length()>255) {
			strFileName=strName.substring(0,255-strSuffix.length())+strSuffix;
		}
		
		File storefile = new File(storedir + separator + strFileName);
		
		if (storefile.exists()){
	        strFileName=strName+"("+i+")"+strSuffix;
	        storefile = new File(storedir + separator + strFileName);
	        while (storefile.exists()){
	            i++;
	            strFileName=strName+"("+i+")"+strSuffix;
	            storefile = new File(storedir + separator + strFileName);
	        }
	    }
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
		    bos = new BufferedOutputStream(new FileOutputStream(storefile));
		    bis = new BufferedInputStream(in);
		    int c;
		    while ( (c = bis.read()) != -1) {
		    	bos.write(c);
		    	bos.flush();  
		    }
		    if(storefile.exists()){
		    	System.out.println("ReceiveMimeMessage.java-->saveImgFile �������Ƴɹ������Ƶ�"+storefile);
		    }else{
		    	System.out.println("ReceiveMimeMessage.java-->saveImgFile ��������ʧ��*****�����Ƶ�"+storefile);
		    }
		    hmImg.put(strContentID,strFileName);
		}catch (FileNotFoundException fe){
	  		System.err.println("re/fe3:"+fe.getMessage());
	  		return ;
		}catch (IOException ie){
			System.err.println("re/ie3:"+ie.getMessage());
			return ;
		}finally {
			try{
				if(bos!=null)bos.close();
				if(bis!=null)bis.close();
			}catch (IOException io){
				System.err.println("re/iodd:"+io.getMessage());
				return ;
			}
		}
	}
	/**
	 * 
	 * @param strFileName
	 * @param in
	 */
	private void saveImgFile(String strContentID,String strFileName,String strSuffix,InputStream in){
		if (strFileName==null||strFileName.trim().equals("")) return;
		if (in==null) return;
		String storedir=getInlineImgPath();
		
		if (storedir == null || storedir.trim().equals("")) storedir=getAttachPath();
		if (storedir == null || storedir.equals("")) return ;   

		int i=0;
		String strName="";
		if(strSuffix!=null&&!"".equals(strSuffix)){
			strSuffix = "."+strSuffix;
		}
		strFileName=decodeWord(strFileName);
		strFileName=dealSpecialCharForFileName(strFileName);
		
		if(osName.toLowerCase().contains("win"))
			strFileName = strFileName.replaceAll("\\?", ""); // ȥ��windows�����ݷ���
		
		strName=strFileName;
		
		if (strFileName.length()>255) {
			strFileName=strName.substring(0,255-strSuffix.length());
		}
		
		File storefile = new File(storedir + separator + strFileName+strSuffix);
		
		if (storefile.exists()){
	        strFileName=strName+"("+i+")";
	        storefile = new File(storedir + separator + strFileName+strSuffix);
	        while (storefile.exists()){
	            i++;
	            strFileName=strName+"("+i+")";
	            storefile = new File(storedir + separator + strFileName+strSuffix);
	        }
	    }
		
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
		    bos = new BufferedOutputStream(new FileOutputStream(storefile));
		    bis = new BufferedInputStream(in);
		    int c;
		    while ( (c = bis.read()) != -1) {
		    	bos.write(c);
		    	bos.flush();  
		    }
		    if(storefile.exists()){
		    	System.out.println("ReceiveMimeMessage.java-->saveImgFile �������Ƴɹ������Ƶ�"+storefile);
		    }else{
		    	System.out.println("ReceiveMimeMessage.java-->saveImgFile ��������ʧ��*****�����Ƶ�"+storefile);
		    }
		    hmImg.put(strContentID,strFileName+strSuffix);
		}catch (FileNotFoundException fe){
	  		System.err.println("re/fe3:"+fe.getMessage());
	  		return ;
		}catch (IOException ie){
			System.err.println("re/ie3:"+ie.getMessage());
			return ;
		}finally {
			try{
				if(bos!=null)bos.close();
				if(bis!=null)bis.close();
			}catch (IOException io){
				System.err.println("re/iodd:"+io.getMessage());
				return ;
			}
		}
	}
	/**
	 * ��ȡ��ǶͼƬ�б�
	 * @return
	 */
	public HashMap getHmImg(){
		return hmImg;
	}
	
	public static String getStringFromIS(InputStream inputstream, int i, String s) throws Exception{
		if(i == -1)  i = 128;
		byte abyte0[] = new byte[i];
		int j;
		String s1;
		for( s1 = ""; (j = inputstream.read(abyte0)) != -1;  ){
			 s1 = s1 + new String(abyte0, 0, j, s);
		}
		return s1;
	}
	/**
	 * ���ļ����е�������Ž��д���
	 * @param fileName
	 * @return
	 */
	private String dealSpecialCharForFileName(String fileName){
	    return fileName.replaceAll("[?/\t\"\r\n:;<>#*|\\\\]", "_");
	}



	public static String decodeWord(String s) {
		
		
	    if(!s.startsWith("=?")) return s;
	    int i = 2;
	    int j;
	    if((j = s.indexOf(63, i)) == -1)
	    return s;
	    i = j + 1;
	    if((j = s.indexOf(63, i)) == -1)
	    return s;
	    String s2 = s.substring(i, j);
	    i = j + 1;
	    if((j = s.indexOf("?=", i)) == -1)
	    return s;
	    String s3 = s.substring(i, j);
	    try{
	        ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(s3.getBytes());
	        InputStream obj;
	        if(s2.equalsIgnoreCase("B"))   
	        	obj = new BASE64DecoderStream(bytearrayinputstream);
	        else
	            if(s2.equalsIgnoreCase("Q"))   
	            	obj = new QDecoderStream(bytearrayinputstream);
	            else
	            	return s;
	        int k = bytearrayinputstream.available();
	        byte abyte0[] = new byte[k];
	        k = obj.read(abyte0, 0, k);
	        return new String(abyte0, 0, k );
	    }catch(Exception ex){
	        return s;
	    }
	}

	public static MimeMessage importFromEml(String strFileName) throws MessagingException {
	    MimeMessage msg = null;
	    try {
	        //TODO importFromEml
	        InputStream fis = new FileInputStream(strFileName);
	        Session mailSession = Session.getDefaultInstance(System.getProperties(), null);
	        msg = new MimeMessage(mailSession,fis);
	        fis.close();
	    } catch (IOException ie) {
	        // TODO Auto-generated catch block
	        ie.printStackTrace();
	    }
	    return  msg;
	}
	
	protected String decodeText(String text) {
		if (text == null) return "";
		String strRet="";
	    try{
	        text = text.replaceFirst("gb2312","GBK");
	        text = text.replaceFirst("GB2312","GBK");
	        text = text.replaceFirst("hz-gb-2312","GBK");
	        text = text.replaceFirst("HZ-GB-2312","GBK");
	        text = text.replaceFirst("x-gbk","GBK");
	        text = text.replaceAll("'","");
	        String tmpText = text ;
	        
		    if (text.trim().toLowerCase().contains("=?")){
		    	if(text.trim().toLowerCase().contains("iso-2022-jp")){ //���ĵ�����
		    		for(String onstr:text.split(" ")){
		    			String localNam = jpCharacterDecoding(onstr);
						strRet += localNam.trim().equals("")?(onstr+" "):(localNam);
		    		}
		    	} else{
		    		strRet = MimeUtility.decodeText(text);
		    	}
		    } else {
		    	if( tmpText.contains("windows-1251") ||
		    			(subjectPri!=null && (subjectPri.contains("windows-1251") || subjectPri.contains("koi8-r")))){
		    		strRet = new String(text.getBytes("ISO8859_1"),"Cp1251");//
		    	} else {
		    		strRet = new String(text.getBytes("ISO8859_1"),"gbk");//Cp1251
		    	}
		    }
	    }catch (UnsupportedEncodingException ue){
	    	System.err.println("decodeText/ue:"+ue.getMessage());
	    	strRet = text;
	    }catch (Exception ee){
	    	System.err.println("decodeText/ee:"+ee.getMessage());
	    	strRet = text;
	    }
	    return strRet.replaceAll("\r\n","");
	}
	
	/**
	 * �����˵�ַ���⴦��
	 * @param from
	 * @return
	 */
	public String dealFrom(String from){
		if(from!=null){
			if (from.toLowerCase().startsWith("=?gbk?b?") && from.endsWith("?=")) {
				from = getFromBASE64(from.substring(8, from.indexOf("?=")));
			}
			if (from.toLowerCase().startsWith("=?gb2312?b?") && from.endsWith("?=")) {
				from = getFromBASE64(from.substring(11,from.indexOf("?=")));
			} 
		} else {
			from="";
		}
		return from;
	}
	
	private static String getFromBASE64(String s) {
	    if (s == null) return null; 
	    BASE64Decoder  decoder = new BASE64Decoder(); 
	    try { 
	        byte[] b = decoder.decodeBuffer(s); 
	        return new String(b); 
	    }catch (Exception e) {}
	    return null; 
	}
	
	
	/**
	 * ����������ͼƬ��·��Ϊ��ͼƬʵ�ʵĴ��·��
	 * @return
	 */
	public String updBodyForImg() {
		String strNewBody = getBodyText();
		
		try {
			Set setKey = hmImg.keySet();
			// System.out.println("strRealPath"+strRealPath);
			Collection collValue = hmImg.values();
			Iterator itValue = collValue.iterator();
			Iterator itKey = setKey.iterator(); 
			//�滻cid��ͼƬidΪʵ��ͼƬ·��
			while (itKey.hasNext()) {
				String strContentID = (String) itKey.next();				
				if (strContentID.startsWith("<")) {
					strContentID = strContentID.substring(1, strContentID.length() - 1);
				}
				String strFileName = (String) itValue.next();
				strNewBody=strNewBody.replaceAll("cid:" + strContentID, inlineRelPath+strFileName);				
			}
		} catch (Exception e) {
			System.err.println("ReceiveMimeMessage.updBodyForImg()."+e.getMessage());
		}
		return strNewBody;
	}
	

	public String subjectProcess(String subject) throws UnsupportedEncodingException {
		
		String tempSubject = subject.toLowerCase();
		
		if (tempSubject.startsWith("=?gbk?b?")
				&& subject.endsWith("?=")) {
			subject = getFromBASE64(subject.substring(8,subject.indexOf("?=")));
		}
		
		if (tempSubject.startsWith("=?gb2312?b?")
				&& subject.endsWith("?=")) {
			subject = getFromBASE64(subject.substring(11,
					subject.indexOf("?=")));
		}
		
		if (tempSubject.startsWith("=?gbk?q?")
				&& subject.endsWith("?=")) {
			subject = MimeUtility.decodeText(subject);
		}
		
		if (tempSubject.startsWith("=?gb2312?q?")
				&& subject.endsWith("?=")) {
			subject = MimeUtility.decodeText(subject);
		}
		
		return subject;
	}
	
	public String directionProcecss(String direction) throws UnsupportedEncodingException {
		
		direction = new String(direction.getBytes("ISO8859_1"), "GBK");
		
		String tempDirection = direction.toLowerCase();
		
		if ((tempDirection.startsWith("=?gbk?b?") && direction
				.endsWith("?="))
				|| (tempDirection.startsWith(
						"=?gb2312?b?") && direction
						.endsWith("?="))) {
			
			direction = getFromBASE64(direction.substring(8,direction.indexOf("?=")));
		}
		
		return direction;
	}
	
	/**
	 * WebMail��������
	 * @Author : qicw
	 * @Date   : Mar 9, 2009 9:03:29 AM
	 */
	private int webMailAttach(net.freeutils.tnef.Message message, String outputdir) throws IOException {
	    
        List attachments = message.getAttachments();
        int count =  0 ;
        
        if( attachments != null ){
	        for ( int i = 0 , j = attachments.size() ; i < j ; i++ ) {
	            Attachment attachment = (Attachment)attachments.get(i);
	            if(attachment != null){
		            if (attachment.getNestedMessage() != null) { // nested message
		                count += webMailAttach(attachment.getNestedMessage(),outputdir);
		            } else { // regular attachment
		                String tempName = getWebMailAttachName(++count, attachment);
				        String filename = getAttachPath() + tempName;
				        attachment.writeTo(filename);
				        sbFileName.append(tempName+";");
		            }
	            }
	        }
        }
        return count;
        
	}
	
	private String getWebMailAttachName(int count, Attachment attachment) {
		return attachment.getFilename() == null
		                    ? ("attachment" + count)
		                    : attachment.getFilename();
	}
	
	/**
	 * �Ƿ���WebMail�ĸ���
	 * @Author : qicw
	 * @Date   : Mar 9, 2009 9:03:11 AM
	 */
	private static boolean isNeedAttach(String oneFile) {
		return oneFile!=null && 
			("winmail.dat" .equals(oneFile.toLowerCase().replaceAll("\\(\\d+\\)", "")) 
					||
			 "att00001.dat".equals(oneFile.toLowerCase())
			);
	}
	
	public static String jpCharacterDecoding(String str) {
		
		StringTokenizer strTok = new StringTokenizer(str, " ");
		String token = null;
		StringBuilder headBuffer = new StringBuilder();

		if (strTok.hasMoreTokens()) {
			while (strTok.hasMoreTokens() && !((token = strTok.nextToken()).contains("=?") || !token.contains("?=")))

				headBuffer.append(token + " ");

			if (token!=null && token.contains("=?") && token.contains("?=")) {
				int indexFin = 0 ;
				while (token.contains("=?") && token.contains("?=")) {
					int indexDeb = token.indexOf("=?");

					headBuffer.append(token.substring(0, indexDeb));
					token = token.substring(indexDeb);

					int codeIndex;

					if ((codeIndex = token.indexOf("?Q?")) == -1)
						codeIndex = token.indexOf("?P?");

					indexFin = token.indexOf("?=", codeIndex + 3);

					String codedStr = token.substring(0, indexFin + 2);
					token = token.substring(indexFin + 2);
					try {
						//System.out.println("CodeStr : " + codedStr);
						headBuffer.append(MimeUtility.decodeText(codedStr));
					} catch (java.io.UnsupportedEncodingException uEE) {
						headBuffer.append(codedStr);
					}
				}
				
				if (!token.equals("")) headBuffer.append(token);
				//����   From֮����   <vedernikov@refriger.ru>
				while(strTok.hasMoreTokens()) headBuffer.append(strTok.nextElement());
				
			}
		}
		return headBuffer.toString();
	}
	
	
	private static String hz2gb(String hzstring) {
		byte[] hzbytes = new byte[2];
		byte[] gbchar = new byte[2];
		int byteindex = 0;
		StringBuffer gbstring = new StringBuffer("");

		try {
			hzbytes = hzstring.getBytes("8859_1");
		} catch (Exception usee) {
			System.err.println("Exception " + usee.toString());
			return hzstring;
		}

		// Convert to look like equivalent Unicode of GB
		for (byteindex = 0; byteindex < hzbytes.length; byteindex++) {
			if (hzbytes[byteindex] == 0x7e) {
				if (hzbytes[byteindex + 1] == 0x7b) {
					byteindex += 2;
					while (byteindex < hzbytes.length) {
						if (hzbytes[byteindex] == 0x7e
								&& hzbytes[byteindex + 1] == 0x7d) {
							byteindex++;
							break;
						} else if (hzbytes[byteindex] == 0x0a
								|| hzbytes[byteindex] == 0x0d) {
							gbstring.append((char) hzbytes[byteindex]);
							break;
						}
						gbchar[0] = (byte) (hzbytes[byteindex] + 0x80);
						gbchar[1] = (byte) (hzbytes[byteindex + 1] + 0x80);
						try {
							gbstring.append(new String(gbchar, "GB2312"));
						} catch (Exception usee) {
							System.err.println("Exception " + usee.toString());
						}
						byteindex += 2;
					}
				} else if (hzbytes[byteindex + 1] == 0x7e) { // ~~ becomes ~
					gbstring.append('~'); 
				} else { // false alarm
					gbstring.append((char) hzbytes[byteindex]);
				}
			} else {
				gbstring.append((char) hzbytes[byteindex]);
			}
		}
		return gbstring.toString();
	}
	
	/**
	 * ��ȡ�ʼ���Message-ID
	 * @return
	 * @throws MessagingException
	 */
	public String getMessageId() throws MessagingException{
		return mimeMessage.getMessageID();
	}
	
	/**
  	 * ���ø����Ƿ����ʼ����ĵĸ���
  	 * 
  	 * @param strContentID ����λ�ñ�ʶ
  	 * @param strNewBody   �ʼ�����
  	 * @return true:�����ĸ��� false:�����ĸ���
  	 * 
  	 * @since 2012-11-28
  	 * @author tucy
  	 */
  	private boolean isInLine(String strContentID,String strNewBody){
  		if("".equals(strContentID.trim())){//���ContentIDΪ���������ĸ���
  			return false;
  		}else if (strContentID.startsWith("<")) {//�м����ŵ����
  			String tmpCid = strContentID.substring(1, strContentID.length() - 1);//��ȥ��������
  			if("".equals(tmpCid.trim()))//���ContentIDΪ���������ĸ���
  	  			return false;
			if(strNewBody.indexOf("cid:" + tmpCid)==-1){//����ʼ�id������û�У���Ϊ���ĸ���
				return false;
			}
			return true;
		}else{//û�м����ŵ����
			if(strNewBody.indexOf("cid:" + strContentID)==-1){//����ʼ�id������û�У���Ϊ���ĸ���
				return false;
			}
			return true;
		}
  	}
  	
  	/**
  	 * ����ַ�����ʵ���ļ����ͣ��÷�����Ҫ�Լ����ļ�����ά����ȥ����ֻ����Jboss���ã�ǣ���İ���
  	 * jmimemagic-0.1.0.jar commons-logging.jar
  	 * 
  	 * @param in �ļ���
  	 * @param showAll �Ƿ���ʾδ�򻯵��ļ�ȫ����
  	 * @return 
  	 */
  	public String getFileType(InputStream in,boolean showAll){
  		String type = "";
  		//�ļ�����ת��ֵ
  		HashMap fileTypes = new HashMap();
  		fileTypes.put("image/gif", "gif");
  		fileTypes.put("image/png", "png");
  		fileTypes.put("image/jpeg", "jpg");
  		fileTypes.put("image/bmp", "bmp");
  		fileTypes.put("application/x-shockwave-flash", "swf");
  		
  		//���ַ���ת����byte����
  		if(in!=null){
  			byte[] imgdata = new byte[4096];
  			ByteArrayOutputStream bytestream = new ByteArrayOutputStream();   
  			int count = -1; 
	  		try {
	  	        while((count = in.read(imgdata,0,4096)) != -1){
	  	        	bytestream.write(imgdata, 0, count);
	  	        }
				imgdata = bytestream.toByteArray();  
				bytestream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error on class ReceiveMimeMessage method getFileType(InputStream file) IOException:"+e1);
				return type;
			}
			//����ļ�����
  			Magic parser = new Magic();
  	        MagicMatch match;
  			try {
  				match = parser.getMagicMatch(imgdata,false);
  				String result = match.getMimeType();
  				if(showAll){
  					type = fileTypes.get(result)==null?result:fileTypes.get(result).toString();
  				}else{
  					type = fileTypes.get(result)==null?null:fileTypes.get(result).toString();
  				}
  			} catch (Exception e2) {
  				System.out.println("Error on class ReceiveMimeMessage method getFileType(InputStream file):"+e2);
  				return type;
  			}
  		}
  		return type;
  	}
  	
  	/**
  	 * ���Content-ID��name��ֵ
  	 * 
  	 * @param part
  	 * @return
  	 */
	private String getFileNameByContentId(MimePart part) {
		String filename = "";
		String s;
		try {
			s = part.getHeader("Content-Type", null);
			s = MimeUtil.cleanContentType(part, s);
			if (s != null) {
				try {
					ContentType ct = new ContentType(s);
					filename = ct.getParameter("name");
				} catch (ParseException pex) {
				} // ignore it
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (filename == null)
			filename = "";
		return filename;
	}
	/**
	 * �ж�һ���ļ����ǲ��ǺϷ����Ϸ�����true������false
	 * @param filename(String)	�ļ���
	 * @return boolean
	 */
	private boolean isAFileName(String filename){
		try{
			//�ļ����б����ܰ�����\|/<>?:*
			//�ļ��а���%ʱ���ᵼ���޷��ҵ��ļ�������Ҳ���˵�
			if(filename==null||"".equals(filename)||filename.contains("\\")||filename.contains("\"")
					||filename.contains("|")||filename.contains("/")
					||filename.contains("<")||filename.contains(">")
					||filename.contains("?")||filename.contains(":")
					||filename.contains("*")||filename.contains("*")){
				return false;
			}
			return true;
		}catch(Exception e){
			System.out.println("Error in ReceiveMimeMessage->isAFileName:"+e.getMessage());
			return false;
		}		
	}
	
	/**
	 * �滻�ļ����Ƿ��ַ�<p>
	 * ˵��:���ļ����ķǷ��ַ��滻���ļ��������ܰ���:\|/<>?:*"
	 *
	 * @param filename (String)	�ļ���
	 * @return value (String) �����޸ĺõ��ļ���
	 * 
	 * @since 2013-08-02
	 * @author tucy
	 */
	private String replaceWrongCharForFileName(String filename){
		//�ļ����б����ܰ�����\|/<>?:*
		//�ļ��а���%ʱ���ᵼ���޷��ҵ��ļ�������Ҳ���˵�
		filename = filename
			.replace("\\", "_").replace("/", "_")
			.replace(":", "_").replace("*", "_")
			.replace("?", "_").replace("\"", "_")
			.replace("<", "_").replace(">", "_")
			.replace("|", "_");
		return filename;		
	}
	
	/**
  	 * ���Disposition��filename��ֵ
  	 * 
  	 * @param part
  	 * @return
  	 */
	private String getFileNameByDisposition(MimePart part) {
		String filename = "";
		String s;
		try {
			s = part.getHeader("Content-Disposition", null);
			if (s != null) {
			    // Parse the header ..
			    ContentDisposition cd = new ContentDisposition(s);
			    filename = cd.getParameter("filename");
			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			System.out.println("Exception in ReceiveMimeMessage->getFileNameByDisposition:"+e.getMessage());
		}
		if (filename == null)
			filename = "";
		return filename;
	}
	
	/**
	 * ����ContentType��Disposition���������ƻ���������<p>
	 * ˵��:ContentType��Disposition�������Ȼ�����ֺ����͡�����ContentType�����ƺ����ͣ�������������ƺ����Ͷ�Ϊ�գ��򷵻�Ϊ��
	 * ���ֱ���ȱ�����ڲ���ʶ���ĸ��ļ�������������ȷ������
	 * 
	 * @param part (String) �������ʼ���֧
	 * @return value String[]<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;{name (String) ��������,<br>
	 * &nbsp;&nbsp;&nbsp;&nbsp;suffix (String) ��������}
	 * 
	 * @since 2013-08-02
	 * @author tucy
	 */
	private String[] getFileFinalNameByTwoNames(Part tmpPart){
		/**1.��ȡ�����ļ���*/
		MimePart part = (MimePart)tmpPart;
		String ContentTypeName = getFileNameByContentId(part);
		String DispositionName = getFileNameByDisposition(part);
		
		/**2.�����ַ���*/
		try {
			ContentTypeName = MimeUtility.decodeText(ContentTypeName);
			DispositionName = MimeUtility.decodeText(DispositionName);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name = "";
		String suffix = "";
		
		/**3.����У��*/
		/*(1).��У��ContentTypeName*/
		if(ContentTypeName!=null&&!"".equals(ContentTypeName)){//1.���ContentType��Ϊ������
			//ȡ�����ֺ��ļ�����
			if(ContentTypeName.contains(".")){//�е�����
				suffix = ContentTypeName.substring(ContentTypeName.lastIndexOf(".")+1,ContentTypeName.length());
				name = ContentTypeName.substring(0,ContentTypeName.lastIndexOf("."));
			}else{//û������
				if("".equals(name)){//DispositionΪ���������name����ContentType��ֵ
					name = ContentTypeName;
				}
			}
		}
		
		if("".equals(name)||"".equals(suffix)||!isAFileName(name)||!isAFileName(suffix)){//�����ContentType�еó�������Ϊ�ջ��ߺ�׺Ϊ�ջ�������(��׺)���Ϸ��������ڶ���У��
			/*(2).��У��DispositionName*/
			if(DispositionName!=null&&!"".equals(DispositionName)){//1.���Disposition��Ϊ������
				//ȡ�����ֺ��ļ�����
				if(DispositionName.contains(".")){//�е�����
					name = DispositionName.substring(0,DispositionName.lastIndexOf("."));
					suffix = DispositionName.substring(DispositionName.lastIndexOf(".")+1,DispositionName.length());
				}else{//û������
					name = DispositionName;
				}
			}
		}
		
		/*(3).ǰ����������󣬶��ļ����ͺ�׺���зǷ��ַ��滻����*/
		name=this.replaceWrongCharForFileName(name);
		suffix=this.replaceWrongCharForFileName(suffix);
		String[] result = {name,suffix};
		return result;
	}
	
	/**
	 * ɾ��winmail.dat�ļ�
	* @Title: delDatFile 
	* @author yaozl
	* @date  2013-11-11
	* @Description: TODO(������һ�仰�����������������)     
	* @return void    �������� 
	* @throws
	 */
	private void delDatFile(){
		String regEx = "winmail(\\(\\d*\\))?.dat[;]?";
    	Pattern pat = Pattern.compile(regEx);
    	Matcher mat = pat.matcher(sbFileName);
    	if(mat.find()){
			int start = sbFileName.indexOf(mat.group());
			sbFileName.replace(start, start+mat.group().length(), "");
		}
	}
	public static void main(String args[])throws Exception{
		
		MimeMessage msg = ReceiveMimeMessage.importFromEml("C:\\Documents and Settings\\Administrator\\Desktop\\��ʱʹ���ļ�\\�����ʼ�\\201312\\[-]-����Խ��ľ���Ƭ�ɱ�����-2013��12��10��.eml");
		ReceiveMimeMessage pmm = new  ReceiveMimeMessage(msg);
		//pmm.getMailContent();
		//pmm.saveImgAttach();
		//pmm.setAttachPath("E:/");
		//pmm.saveAttachMent();
		try{ 
		System.err.println("1--Received Date "+pmm.getReceivedDate());
		
		System.out.println("2--Subject   "+pmm.getSubject());
		System.err.println("3--From   "+pmm.getFrom()); 
		System.out.println("4--dfdsf__"+pmm.getOrigFrom()); 
		System.err.println("5--"+pmm.getSenderIP());
		System.out.println("6--"+pmm.getSentDateTime()); 
		System.out.println("7!!!!!getSentDate!!__" +pmm.getSentDate());
		System.out.println("8!!!_~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~_" +pmm.getPriority());
		
		System.out.println("9!!!__" +pmm.getMailSize());
		System.out.println("10!!!__" +pmm.getPriority());
		System.out.println("11!!!__" +pmm.getReplyTo());
		System.out.println("12!!!__" +pmm.getSenderIP());
		System.out.println("13!!!__" +pmm.getReplySign()); 
		pmm.setAttachPath("E:\\xx\\���Ը���");
		pmm.getMailContent();
		pmm.setInlineImgPath("E:\\xx\\���Ը���");
		pmm.saveImgAttach();
		System.out.println("14!~~~~~~~~~~~!!__" +pmm.getBodyText());
		System.err.println("15---getReplySign"+pmm.getReplySign());
		System.out.println("16!!!__" +pmm.getContentType());
		System.out.println("17!!!__" +pmm.getInlineImgPath()); 
		System.out.println("18!!!__" +pmm.getMailSize());
		System.err.println("19!!!__" +pmm.getOrigFrom()); 
		System.out.println("20!!!__" +pmm.getSentDate());
		System.out.println("21!!!__" +pmm.getSubject());
		System.out.println("22-----------!!getPriority!__" +pmm.getPriority());
		System.err.println("23--To__"+pmm.getMailAddress("TO"));
		System.err.println("24--CC______"+pmm.getMailAddress("CC"));
		System.err.println("25--MessageID______"+pmm.getMessageId());
		
		Calendar today    = Calendar.getInstance();
		Calendar sentdate = Calendar.getInstance();
		sentdate.setTime(new Date((pmm.getSentDate())));
		sentdate.add(Calendar.DATE,60);
		boolean bdeletedflag=false;
		
		if (today.after(sentdate)){
			bdeletedflag = true;
		}
		
		System.err.println("ReceiveMimeMessage.main()."+bdeletedflag);
		pmm.saveAttachMent( msg );
		System.out.println("��������----->"+pmm.getAttachmentFileName());
		//pmm.delDatFile();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}