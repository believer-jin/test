//package cn.fileConvert.txt;
//
//import java.io.BufferedReader;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
////import com.itextpdf.text.Element;
////import com.itextpdf.text.Font;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfWriter;
//
///**
// * txt转换成pdf
// * 
// * @author dingsj
// *
// */
//public class TxtConvertPdf {
//	private final static String READ_FILE_PATH = "D:\\Documents\\Downloads\\万水千山走遍.txt";// 读取的txt文件
//	private final static String WRITER_FILE_PATH = "f:\\万水千山走遍.pdf";// 生成txt文件
//
//	public static void main(String[] args) {
//		BufferedReader read = null;
//		Document document = null;
//		try {
//			document = new Document(PageSize.A4, 80, 80, 60, 30);
//			PdfWriter.getInstance(document, new FileOutputStream(WRITER_FILE_PATH));
//			document.open();
//			BaseFont bf = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//			Font f = new Font(bf, 18, Font.NORMAL);
//			Paragraph t = new Paragraph("oracle手册", f);// 起一个别名
//			t.setAlignment(Element.ALIGN_CENTER);
//			t.setLeading(30.0f);
//			document.add(t);
//			f = new Font(bf, 11, Font.NORMAL);
//			read = new BufferedReader(new FileReader(READ_FILE_PATH));
//			String line = null;
//			while ((line = read.readLine()) != null) {
//				t = new Paragraph(line, f);
//				t.setAlignment(Element.ALIGN_LEFT);
//				t.setLeading(20.0f);
//				document.add(t);
//			}
//			read.close();
//			document.close();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {     
//            try {     
//                read.close();     
//                document.close();     
//            } catch (IOException e) {     
//                e.printStackTrace();     
//            }     
//        }     
//	}
//}
