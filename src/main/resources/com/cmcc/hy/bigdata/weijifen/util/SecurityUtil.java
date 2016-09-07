package com.cmcc.hy.bigdata.weijifen.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 安全工具类，包含所有安全相关的公用方法
 *
 * @Project: SHZA-mapreduce-analysis
 * @File: SecurityUtil.java
 * @Date: 2015年9月17日
 * @Author: Lucifer
 * @Copyright: 版权所有 (C) 2015 中国移动 杭州研发中心.
 *
 * @注意：本内容仅限于中国移动内部传阅，禁止外泄以及用于其他的商业目的
 */
public class SecurityUtil {

	private static final Logger logger = LoggerFactory.getLogger(SecurityUtil.class);

	/**
	 * MD5加密函数
	 * 
	 * @description
	 * @author hechan
	 * @date 2015年6月1日
	 * @param source源字符串
	 * @param type加密类型(MD5,SHA)
	 * @return
	 */
	public static String encrypt(String source, String type) {
		StringBuilder sb = new StringBuilder();
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance(type);
			md5.update(source.getBytes());
			for (byte b : md5.digest()) {
				// 10进制转16进制，X表示以十六进制形式输出，02表示不足两位前面补0输出
				sb.append(String.format("%02X", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
