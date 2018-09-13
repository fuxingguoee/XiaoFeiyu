package com.il360.xiaofeiyu.util;

import android.annotation.SuppressLint;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DataUtil {
	
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm");
	
	/**
	 * 两个时间之间的天数
	 * 
	 * @param date1
	 * @param date2
	 * @return date1-date2
	 */
	public static long getDays(String date1, String date2) {
		if (date1 == null || date1.equals(""))
			return 0;
		if (date2 == null || date2.equals(""))
			return 0;
		java.util.Date date = null;
		java.util.Date mydate = null;
		try {
			date = formatter2.parse(date1);
			mydate = formatter2.parse(date2);
		} catch (Exception e) {
		}
		long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		return day;
	}
	
	/**
	 * 获取年月日
	 * 
	 * @param date
	 * @return
	 */
	public static String getDate(String date) {
		String newDateStr = date;
		try {
			Date newdate = formatter.parse(date);
			newDateStr = formatter2.format(newdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return newDateStr;
	}

	/**
	 * 获取小时分
	 * 
	 * @param date
	 * @return
	 */
	public static String getTime(String time) {
		String newTimeStr = time;
		try {
			Date newTime = formatter.parse(time);
			newTimeStr = formatter3.format(newTime);
		} catch (ParseException e) {

			e.printStackTrace();
		}

		return newTimeStr;
	}
	
	/**
	  * 获取现在时间
	  * 
	  * @return 返回短时间字符串格式yyyy-MM-dd
	  */
	public static String getStringDateShort() {
	  Date currentTime = new Date();
	  String dateString = formatter2.format(currentTime);
	  return dateString;
	}
	
	/**
	  * 获取现在时间
	  * 
	  * @return 返回短时间字符串格式yyyy-MM-dd
	  */
	public static String getURLDateShort() {
		String dateString = null;
		try {
			URL url = new URL("http://www.bjtime.cn");// 取得资源对象
			URLConnection uc = url.openConnection();// 生成连接对象
			uc.connect(); // 发出连接
			long ld = uc.getDate(); // 取得网站日期时间
			Date date = new Date(ld); // 转换为标准时间对象
			dateString = formatter2.format(date);
		} catch (Exception e) {
		}
		return dateString;
	}
	
	/**
	  * 转换时间
	  * @param long lg
	  * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
	  */
	public static String getLongToDate(Long ld) {
		Date date = new Date(ld);
		String dateString = formatter.format(date);
		return dateString;
	}
	
	/**
	  * 转换时间
	  * @param long lg
	  * @return 返回短时间字符串格式yyyy-MM-dd
	  */
	public static String getLongToDateShort(Long ld) {
		Date date = new Date(ld);
		String dateString = formatter2.format(date);
		return dateString;
	}
}
