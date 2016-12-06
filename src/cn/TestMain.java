package cn;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TestMain {
	
	public static void main(String[] args) {
		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(1480521600000L);
		System.out.println(d.format(c.getTime()));
		System.out.println(5183831/(60*60*1000));
	}
	
}
