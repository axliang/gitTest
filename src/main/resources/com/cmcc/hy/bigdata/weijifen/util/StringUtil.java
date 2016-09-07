package com.cmcc.hy.bigdata.weijifen.util;
/**
 * 字符串工具类
 *
 * @Project: credit-collection-hivedata
 * @File: StringUtil.java
 * @Date: 2015年11月24日
 * @Author: Jinyibin
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class StringUtil {
	/**
	 * 判断字符串不为空串或null
	 * 
	 * @param str
	 */
	public static boolean strIsNotNull(String str) {
		return ((str != null) && (!"".equals(str)));
	}
	/**
	 * 判断字符串为空或null
	 * @param str
	 * @return
	 */
	public static boolean strIsNull(String str){
		return (str == null || str.isEmpty());
	}
	/**
	 * 返回指定字符串的最后一个指定分隔符之后的子串
	 * @param str
	 * @param separator
	 * @return
	 */
	public static String getStrAfterLastSep(String str,String separator){
		return str.substring(str.lastIndexOf(separator)+1);
	}
}
