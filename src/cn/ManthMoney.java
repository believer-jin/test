package cn;


import cn.fileConvert.FileUtils;

import java.util.List;

/**
 * 股票成本核算
 *
 * @author lenovo
 */
public class ManthMoney {
    public final static String FILE_PATH = "F:\\POETRY\\股票流水.txt";


    public void test() {
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
        System.out.println("tatal:" + tatal);
    }

    main
}
