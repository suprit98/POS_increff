package com.increff.pos.util;

import java.security.SecureRandom;

public class BarcodeUtil {
	
	private static String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
	private static SecureRandom rnd = new SecureRandom();

	//Generate Random alphanumeric string
	public static String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}


}
