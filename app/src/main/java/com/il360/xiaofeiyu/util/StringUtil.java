package com.il360.xiaofeiyu.util;

import android.text.TextUtils;

/**
 * Created by IntelliJ IDEA.
 * User: zhouxin@easier.cn
 * 字符串的处理类
 * Date: 12-11-22
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
	/**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     * @return 判断后的字符串
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }
    
    
	/**
     * 全角转半角
     *
     * @param input String
     * @return out
     */
	public static String toDBC(String input) {
		char c[] = input.toCharArray();

		for (int i = 0; i < c.length; i++) {
			if (c[i] == '\u3000') {
				c[i] = ' ';
			} else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
				c[i] = (char) (c[i] - 65248);
			}
		}
		String out = new String(c);
		return out;
	}
	
	
    /**
     * 获取","之前的内容
     *
     * @param str 待判断字符串
     * @return s
     */
	public static String getStringSub(String str) {
		String s = "";
		if (!TextUtils.isEmpty(str)) {
			String[] temp = null;
			temp = str.split(",");
			s = temp[0];
		}
		return s;
	}
	
}

