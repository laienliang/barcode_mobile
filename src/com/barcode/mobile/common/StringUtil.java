package com.barcode.mobile.common;

public class StringUtil {

	/**
	 * 判断是否为NULL或空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		return null == str || "".equals(str.toString());
	}
}
