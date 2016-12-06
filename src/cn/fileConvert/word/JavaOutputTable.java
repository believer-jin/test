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
	private final static String WRITER_FILE_PATH = "f:\\JavaOutputTable.doc";// ����txt�ļ�

	public static void main(String[] args) {
		try {
			/** ����Document����word�ĵ��� */
			Document doc = new Document(PageSize.A4);
			/**�ļ������*/
			FileOutputStream fos = new FileOutputStream(WRITER_FILE_PATH);
			/** ����һ����д����document���������ͨ����д�����Խ��ĵ�д�뵽������� */
			RtfWriter2.getInstance(doc, fos);
			doc.open();
			/** �������� */
			RtfFont titleFont = new RtfFont("����_GB2312", 12, Font.NORMAL, Color.BLACK);
			/** �������� */
			RtfFont contextFont = new RtfFont("����_GB2312", 9, Font.NORMAL, Color.BLACK);
			/** ������� */
			Table table = new Table(4);
			int[] withs = { 20, 30, 10, 40 };
			/** ����ÿ����ռ���� */
			table.setWidths(withs);
			/** �����ռҳ���� */
			table.setWidth(100);
			/** ������ʾ */
			table.setAlignment(Element.ALIGN_CENTER);
			/** �Զ����� */
			table.setAutoFillEmptyCells(true);
			String titlestr = "����java������word��";
			Paragraph title = new Paragraph(titlestr, titleFont);
			/** ���ñ����ʽ���䷽ʽ */
			title.setAlignment(Element.ALIGN_CENTER);
			doc.add(title);
			/** ������� */
			Cell[] cellHeaders = new Cell[4];
			cellHeaders[0] = new Cell(new Phrase("���", contextFont));
			cellHeaders[1] = new Cell(new Phrase("����", contextFont));
			cellHeaders[2] = new Cell(new Phrase("��������", contextFont));
			cellHeaders[3] = new Cell(new Phrase("�Ա�", contextFont));
			for (int i = 0; i < 4; i++) {
				/** ������ʾ author:yyli Sep 15, 2010 */
				cellHeaders[i].setHorizontalAlignment(Element.ALIGN_CENTER);
				/** ���������ʾ author:yyli Sep 15, 2010 */
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
