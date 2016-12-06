package cn;

import java.util.List;

import cn.fileConvert.FileUtils;

/**
 * 股票成本核算
 * @author lenovo
 *
 */
public class ManthMoney {
	public final static String FILE_PATH = "F:\\POETRY\\股票流水.txt";
	
	public static void main(String[] args) {
		List<String> list = FileUtils.readerLineForFile(FILE_PATH);
		long tatal = 0l;
		for (String str : list) {
			String[] metas = str.split(":");
			if (metas.length >= 2) {
				if ("+".equals(metas[0].toString().trim())) {
					tatal += Long.valueOf(metas[1].toString().trim());
					System.out.println("add:" + metas[1].toString().trim());
				} else if ("-".equals(metas[0].toString().trim())) {
					tatal -= Long.valueOf(metas[1].toString().trim());
					System.out.println("subtraction:" + metas[1].toString().trim());
				}
			}
		}
		System.out.println("tatal:"+tatal);
	}
}
