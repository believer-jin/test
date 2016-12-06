package cn.entersoft.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHyperlink;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

public class excelUtils {
	
	/**
	 * ����һ��excel�ļ�
	 * @param filePath ·��
	 * @param fileName ����
	 * @return
	 * @author dingsj
	 * @since 2014-1-9 ����11:21:42
	 */
	public static boolean writeExcel(String filePath,String fileName){
		try {
			/**����Excel�Ĺ������WorkBook,��Ӧһ��excel�ĵ�*/
			HSSFWorkbook workbook = new HSSFWorkbook();
			/**����Excel�Ĺ���sheet,��Ӧexcel���ĵ�tab*/
			HSSFSheet sheet = workbook.createSheet("sheet1");
			/**����excelÿ�п��*/
			sheet.setColumnWidth(0, 4000);
			sheet.setColumnWidth(1, 3500);
			/**����������ʽ*/
			HSSFFont font = workbook.createFont();
			font.setFontName("������κ");
			font.setBoldweight((short)100);
			font.setFontHeight((short) 300);
			font.setColor(HSSFColor.BLUE.index);
			/**������Ԫ����ʽ*/
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			/**�����߿�*/
			style.setBottomBorderColor(HSSFColor.RED.index);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			/**�趨����*/
			style.setFont(font);
			/**����Excel��sheet��һ��*/
			HSSFRow row = sheet.createRow(0);
			row.setHeight((short) 500);//�趨�еĸ߶�
			/**����һ��Excel��Ԫ��*/
			HSSFCell cell = row.createCell(0);
			/**�ϲ���Ԫ��strarRow,endRow,startColumn,endColumn��*/
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			/**��excel�ĵ�Ԫ��������ʽ�͸�ֵ*/
			cell.setCellStyle(style);
			cell.setCellValue("Hello,World");
			/**���õ�Ԫ��������ʽ*/
			HSSFCellStyle style2 = workbook.createCellStyle();
			style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm:ss"));
			style2.setWrapText(true);//�Զ�����
			row = sheet.createRow(1);
			/**�趨��Ԫ�����ʽ��ʽ*/
			cell = row.createCell(0);
			cell.setCellStyle(style2);
			cell.setCellValue(new Date());
			/**����������*/
			HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
			link.setAddress("http://www.baidu.com");
			cell = row.createCell(1);
			cell.setCellValue("�ٶ�");
			cell.setHyperlink(link);//�趨��Ԫ��ĳ�����
			FileOutputStream fStream = new FileOutputStream(filePath+File.separator+fileName);
			workbook.write(fStream);
			fStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	public static void main(String[] args) {
		writeExcel("E:\\xx\\xx","book.xls");
//		System.out.println("E:\\xx\\xx"+File.separator+"book.xls");
	}
}
