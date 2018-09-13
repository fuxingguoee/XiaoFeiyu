package com.il360.xiaofeiyu.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;

/**
 * 获取硬件信息工具
 * @author wangjie
 * @version 创建时间：2013-3-25 下午2:38:14
 */
public class HardwareInfoUtil {

	private final static String TAG = "HardwareInfoUtil";

	/**
	 * 判断是否为手机
	 * @param context
	 * @return
	 */
	public static boolean isPhone(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		int height = manager.getDefaultDisplay().getHeight();
		Log.d(TAG, "width == " + width + "   height == " + height);
		if (width >= 1300 || height >= 900) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 获取设备id
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String deviceId;
		if (isPhone(context)) {
			TelephonyManager telephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = telephony.getDeviceId();
		} else {
			deviceId = Secure.getString(context.getContentResolver(),
					Secure.ANDROID_ID);

		}
		Log.v(TAG, "device id: " + deviceId);
		return deviceId;
	}

	/**
	 * 获取设备mac地址
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		String macAddress;
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		macAddress = info.getMacAddress();
		System.out.println("macAddress is null: " + (macAddress == null));
		if (null == macAddress) {
			return "";
		}
		macAddress = macAddress.replace(":", "");
		return macAddress;
	}

}
