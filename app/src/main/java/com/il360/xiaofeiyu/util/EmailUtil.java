package com.il360.xiaofeiyu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * email的工具类
 * @author Steven
 * @version 1.0
 * @modify 2013-10-17 上午11:01:11
 */
public class EmailUtil {
	
	/**
	 * 检查email格式是否正确
	 * @param email 邮件地址
	 * @return true:正确 false:不正确 
	 */
	public static boolean isEmail(String email) {
		Pattern p = Pattern.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
		Matcher m = p.matcher(email);
		return m.matches();
	}
}
