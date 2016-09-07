package com.cmcc.hy.bigdata.weijifen.util;

import java.util.regex.Pattern;

public class ValidationUtil {
	/**
	 * 二代身份证18位格式
	 */
	private static final String IDENTITY_CART_18_FORMAT = "(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|81|82)\\d{4}[1|2]\\d{3}[0|1]\\d[0-3]\\d{4}\\S";
//	private static final String IDENTITY_CART_15_FORMAT = "(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|81|82)\\d{6}[0|1]\\d[0-3]\\d{4}";
	
	
	/**
	 * 判断号码是否是二代身份证号码
	 * @description
	 * @author hechan
	 * @date 2016年3月30日
	 * @param idNumber
	 * @return boolean
	 */
	public static boolean isValidIdentityCard(String idNumber){
		if(idNumber.length() == 18){
			return Pattern.matches(IDENTITY_CART_18_FORMAT, idNumber);
		}else{
			return false;
		}
	}
}
