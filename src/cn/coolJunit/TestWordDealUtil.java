package cn.coolJunit;
import org.junit.Assert;
import org.junit.Test;

public class TestWordDealUtil {
	 // ���� wordFormat4DB �������е����
	 @Test 
	 public void wordFormat4DBNormal(){ 
		 String target = "employeeInfo"; 
		 String result = WordDealUtil.wordFormat4DB(target); 
		 Assert.assertEquals("employee_info", result); 
	 } 
}
