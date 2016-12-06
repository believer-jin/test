package cn.fileConvert.word;

import java.awt.Color;
import java.io.FileOutputStream;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.style.RtfFont;

public class JavaOutputTable {
	private final static String WRITER_FILE_PATH = "f:\\JavaOutputTable.doc";// 生成txt文件

	public static void main(String[] args) {
		try {
			/** 创建Document对象（word文档） */
			Document doc = new Document(PageSize.A4);
			/**文件输出流*/
			FileOutputStream fos = new FileOutputStream(WRITER_FILE_PATH);
			/** 建立一个书写器与document对象关联，通过书写器可以将文档写入到输出流中 */
			RtfWriter2.getInstance(doc, fos);
			doc.open();
			/** 标题字体 */
			RtfFont titleFont = new RtfFont("仿宋_GB2312", 12, Font.NORMAL, Color.BLACK);
			/** 正文字体 */
			RtfFont contextFont = new RtfFont("仿宋_GB2312", 9, Font.NORMAL, Color.BLACK);
			/** 表格设置 */
			Table table = new Table(4);
			int[] withs = { 20, 30, 10, 40 };
			/** 设置每列所占比例 */
			table.setWidths(withs);
			/** 表格所占页面宽度 */
			table.setWidth(100);
			/** 居中显示 */
			table.setAlignment(Element.ALIGN_CENTER);
			/** 自动填满 */
			table.setAutoFillEmptyCells(true);
			String titlestr = "测试java输出表格到word中";
			Paragraph title = new Paragraph(titlestr, titleFont);
			/** 设置标题格式对其方式 */
			title.setAlignment(Element.ALIGN_CENTER);
			doc.add(title);
			/** 表格内容 */
			Cell[] cellHeaders = new Cell[4];
			cellHeaders[0] = new Cell(new Phrase("序号", contextFont));
			cellHeaders[1] = new Cell(new Phrase("姓名", contextFont));
			cellHeaders[2] = new Cell(new Phrase("出生年月", contextFont));
			cellHeaders[3] = new Cell(new Phrase("性别", contextFont));
			for (int i = 0; i < 4; i++) {
				/** 居中显示 author:yyli Sep 15, 2010 */
				cellHeaders[i].setHorizontalAlignment(Element.ALIGN_CENTER);
				/** 纵向居中显示 author:yyli Sep 15, 2010 */
				cellHeaders[i].setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cellHeaders[i]);
			}
			doc.add(table);
			doc.close();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
