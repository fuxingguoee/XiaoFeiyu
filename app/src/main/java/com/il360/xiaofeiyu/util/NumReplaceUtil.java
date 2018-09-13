package com.il360.xiaofeiyu.util;

public class NumReplaceUtil {
	public static String newBankNum(String s) { // 替换部分为*
		if (s == null) {
			return s;
		} else {
			if (s.length() < 14) {
				return s;
			} else {
				return s.substring(0, s.length() - 10) + "******" + s.substring(s.length() - 4);// 用*代替倒数10-5位
			}
		}
	}

	public static String newName(String s) { // 替换部分为*
		if (s == null) {
			return s;
		} else {
			if (s.length() < 1) {
				return s;
			} else {
				String newName = s.substring((int) ((s.length() + 1) / 2));
				while (newName.length() < s.length()) {
					newName = "*" + newName;
				}

				return newName;// 用*代替名字
			}
		}
	}

	public static String newPhoneNum(String s) { // 替换部分为*
		if (s == null) {
			return s;
		} else {
			if (s.length() < 11) {
				return s;
			} else {
				return s.substring(0, 3) + "****" + s.substring(s.length() - 4);// 用*代替中间四位
			}
		}
	}
	
	public static String newIDNum(String s) { // 替换部分为*
		if (s == null) {
			return s;
		} else {
			if (s.length() < 15) {
				return s;
			} else {
				return s.substring(0, 4) + "*****" + s.substring(s.length() - 4);// 用*代替中间
			}
		}
	}
	
	public static String lastNum(String s , int i) { // 获取末i位数据
		if(s == null){
			return s;
		} else {
			if (s.length() < i) {
				return s;
			} else {
				return s.substring(s.length() - i);
			}
		}
	}

	public static String newLoanOrderNum(String s) { // 8位，不足8位前面补0
		if (s != null) {
			while (s.length() < 8) {
				s = "0" + s;
			}
		} else {
			s = "00000000";
		}
		return s;
	}

	public static String newMonth(String s) { // 2位，不足2位前面补0
		if (s != null) {
			while (s.length() < 2) {
				s = "0" + s;
			}
		} else {
			s = "00";
		}
		return s;
	}
}
