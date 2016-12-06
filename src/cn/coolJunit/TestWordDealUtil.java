package cn.coolJunit;
import org.junit.Assert;
import org.junit.Test;

public class TestWordDealUtil {
	 // 测试 wordFormat4DB 正常运行的情况
	 @Test 
	 public void wordFormat4DBNormal(){ 
		 String target = "employeeInfo"; 
		 String result = WordDealUtil.wordFormat4DB(target); 
		 Assert.assertEquals("employee_info", result); 
	 } 
}
