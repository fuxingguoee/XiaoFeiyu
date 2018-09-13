package com.il360.xiaofeiyu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 读取手机与SIM卡的相关信息
 * @author Steven
 * @version 1.5 
 * @modify 2013-8-14 上午10:11:17
 */
public class SIMCardUtil {
	/**
	 * TelephonyManager提供设备上获取通讯服务信息的入口。 应用程序可以使用这个类方法确定的电信服务商和国家 以及某些类型的用户访问信息。
	 * 应用程序也可以注册一个监听器到电话收状态的变化。不需要直接实例化这个类
	 * 使用Context.getSystemService(Context.TELEPHONY_SERVICE)来获取这个类的实例。
	 */
	private static TelephonyManager telephonyManager;

	private static TelephonyManager getTelephonyManager(Context context){
		if (telephonyManager != null){
			return telephonyManager;
		}
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager;
	}

	/**
	 * Role:Telecom service providers获取手机服务商信息 <BR>
	 * 需要加入权限<uses-permission
	 * android:name="android.permission.READ_PHONE_STATE"/> <BR>
	 * 
	 */
	public static String getProvidersName(Context context) {
		String ProvidersName = null;
		// 返回唯一的用户ID;就是这张卡的编号神马的
		String IMSI = getTelephonyManager(context).getSimOperator();
		// IMSI号前面3位460是国家，紧接着后面2位00 02 07是中国移动，01是中国联通，03是中国电信。
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")
				|| IMSI.startsWith("46007")) {
			ProvidersName = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			ProvidersName = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			ProvidersName = "中国电信";
		} else {
			ProvidersName = "未知";
		}
		return ProvidersName;
	}
	
	public static boolean isMobileNo(String phone){
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
		Pattern p = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
		Matcher m = p.matcher(phone);
		return m.matches();
		
	}
	
	
	public static boolean isPhoneNo(String phone){
		Pattern p = Pattern.compile("^(0[0-9]{2,3}\\-)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$|(^1[3|4|5|7|8][0-9]\\d{8}$)");
		Matcher m = p.matcher(phone);
		return m.matches();
		
	}
	
	/** 验证输入的是否为6位数字短信验证码 **/
	public static boolean isSMSCode(String code) {
		Pattern p = Pattern.compile("[0-9]{4}");
		Matcher m = p.matcher(code);
		return m.matches();
	}
	
}