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
	 * 生成一个excel文件
	 * @param filePath 路径
	 * @param fileName 名称
	 * @return
	 * @author dingsj
	 * @since 2014-1-9 上午11:21:42
	 */
	public static boolean writeExcel(String filePath,String fileName){
		try {
			/**创建Excel的工作书册WorkBook,对应一个excel文档*/
			HSSFWorkbook workbook = new HSSFWorkbook();
			/**创建Excel的工作sheet,对应excel的文档tab*/
			HSSFSheet sheet = workbook.createSheet("sheet1");
			/**设置excel每列宽度*/
			sheet.setColumnWidth(0, 4000);
			sheet.setColumnWidth(1, 3500);
			/**创建字体样式*/
			HSSFFont font = workbook.createFont();
			font.setFontName("华文新魏");
			font.setBoldweight((short)100);
			font.setFontHeight((short) 300);
			font.setColor(HSSFColor.BLUE.index);
			/**创建单元格样式*/
			HSSFCellStyle style = workbook.createCellStyle();
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			/**创建边框*/
			style.setBottomBorderColor(HSSFColor.RED.index);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			/**设定字体*/
			style.setFont(font);
			/**创建Excel的sheet的一行*/
			HSSFRow row = sheet.createRow(0);
			row.setHeight((short) 500);//设定行的高度
			/**创建一个Excel单元格*/
			HSSFCell cell = row.createCell(0);
			/**合并单元格（strarRow,endRow,startColumn,endColumn）*/
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));
			/**给excel的单元格设置样式和赋值*/
			cell.setCellStyle(style);
			cell.setCellValue("Hello,World");
			/**设置单元格内容样式*/
			HSSFCellStyle style2 = workbook.createCellStyle();
			style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm:ss"));
			style2.setWrapText(true);//自动换行
			row = sheet.createRow(1);
			/**设定单元格的样式格式*/
			cell = row.createCell(0);
			cell.setCellStyle(style2);
			cell.setCellValue(new Date());
			/**创建超链接*/
			HSSFHyperlink link = new HSSFHyperlink(HSSFHyperlink.LINK_URL);
			link.setAddress("http://www.baidu.com");
			cell = row.createCell(1);
			cell.setCellValue("百度");
			cell.setHyperlink(link);//设定单元格的超链接
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
