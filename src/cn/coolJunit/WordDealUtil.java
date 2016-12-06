package cn.coolJunit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordDealUtil {
	/**
	 * �� Java �������ƣ�ÿ�����ʵ�ͷ��ĸ��д������ ���ݿ�������ϰ�߽��и�ʽ�� ��ʽ���������ΪСд��ĸ������ʹ���»��߷ָ���������
	 * 
	 * ���磺employeeInfo ������ʽ��֮���Ϊ employee_info
	 * 
	 * @param name
	 *            Java ��������
	 */
	public static String wordFormat4DB(String name) {
		Pattern p = Pattern.compile("[A-Z]");
		Matcher m = p.matcher(name);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "_" + m.group());
		}
		return m.appendTail(sb).toString().toLowerCase();
	}
}
