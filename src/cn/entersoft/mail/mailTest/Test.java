package cn.entersoft.mail.mailTest;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class Test {

	public static void main(String[] args) throws Exception{
		//String s="shift_jis";
		//String t=new String(s.getBytes(),"shift_jis");
		//System.out.println(t);

		//File file=new File("src/IVT-1 DISC CABLE REF_ BACKTG773163CCC.Eml");
		//File file=new File("src/RE_ RE_Material swatch for kp053012-06.eml");
		//File file=new File("src/Re_ Delivery by air or DHL - Uniview.eml");
		//File file=new File("src/+Defect+Product+-+EM4000+(203162).eml");
		//File file=new File("src/Re_ Ord #.008 Address.eml");
		//File file=new File("src/Signed PI and Axiz POP105307.eml");
		//File file=new File("src/系统退信.eml");
		//File file=new File("src/系统退信31.eml");
		//File file=new File("src/PI+for+3x40HQ.eml");
		//File file=new File("src/New Moss PO 58889.eml");
		//File file=new File("src/休假通知.eml");
		//File file=new File("src/RV Bending tools for VALSI-MEXICO (excel).eml");
		//File file = new File("src/网页订购信息：Дмитрий.eml");
		//File file = new File("src/Re_ Fw_ Re_ Your white copy ofOrder_ CG529601 Style_ LHAT-247ANIMAL.eml");
		//File file = new File("src/Violet Intl Ltd. _ 9882294 _ HKSL1304057728FW.eml");
		//File file = new File("src/RE_ news nails.eml");
		File file = new File("src/Violet Intl Ltd. _ 9882294 _ HKSL1304057728FW.eml");
		//System.out.println(file.getPath());
		ReceiveMimeMessage rmm = new ReceiveMimeMessage(file.getPath());
		System.out.println("senddate:"+rmm.getSentDate());
		//System.out.println("senddatetime:"+rmm.getSentDateTime());
		rmm.setAttachPath("src/"); //设置附件保存路径    
		rmm.setInlineImgPath("src/");  //设置正文图片保存路径
		rmm.getMailContent();// 获取正文内容
		rmm.saveImgAttach();	//保存正文图片
		rmm.saveAttachMent();	//保存附件
		String strBodyText = rmm.getBodyText();// 获取正文
		strBodyText = rmm.updBodyForImg();		//处理正文图片
		//strBodyText = strBodyText.replaceAll("cid:", "");//用于解决因正文中存在因包含cid:.....等脚本造成的页面无法显示的问题
		//System.out.println(newStr);
		//System.out.println(coder(newStr,"GB18030","GBK"));
		System.out.println(strBodyText);		
		
	}
	public static String coder(String str,String fromcharset,String tocharset){
		try {
			return new String(str.getBytes(fromcharset),tocharset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			return "";
		}
	}
}
