package cn.hd.cf.test;

import java.math.BigDecimal;

public class TestService {
	
	public static void testAction()
	{
		float   f  = (float)22.5;  
		  BigDecimal   b   =   new   BigDecimal(f);  
		  int   f1   =   (int)b.setScale(0,   BigDecimal.ROUND_HALF_UP).floatValue();        
		  System.out.println(f1);  
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestService.testAction();
	}

}
