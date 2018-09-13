package com.il360.xiaofeiyu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Locale;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;

/**
 * 系统工具类
 */
public class SystemUtil {

	/**
	 * 获取当前手机系统语言。
	 * 
	 * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
	 */
	public static String getSystemLanguage() {
		return Locale.getDefault().getLanguage();
	}

	/**
	 * 获取当前系统上的语言列表(Locale列表)
	 * 
	 * @return 语言列表
	 */
	public static Locale[] getSystemLanguageList() {
		return Locale.getAvailableLocales();
	}

	/**
	 * 获取当前手机系统版本号
	 * 
	 * @return 系统版本号
	 */
	public static String getSystemVersion() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return 手机型号
	 */
	public static String getSystemModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取手机厂商
	 * 
	 * @return 手机厂商
	 */
	public static String getDeviceBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
	 * 
	 * @return 手机IMEI
	 * @throws NoSuchMethodException 
	 */
	public static String getIMEI(Context ctx){
		TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		if (manager != null) {
			try {
				Method method = manager.getClass().getMethod("getDeviceId", int.class);
				String imei1 = manager.getDeviceId();
				String imei2 = (String) method.invoke(manager, 1);
				String meid = (String) method.invoke(manager, 2);
				return meid;
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return null;
	}

	/**
	 * 获取手机内存
	 * 
	 * @return 手机内存
	 */
	public static String getTotalRam(Context context) {// GB
		String path = "/proc/meminfo";
		String firstLine = null;
		int totalRam = 0;
		try {
			FileReader fileReader = new FileReader(path);
			BufferedReader br = new BufferedReader(fileReader, 8192);
			firstLine = br.readLine().split("\\s+")[1];
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (firstLine != null) {
			totalRam = (int) Math.ceil((new Float(Float.valueOf(firstLine) / (1024 * 1024)).doubleValue()));
		}

		return totalRam + "GB";// 返回1GB/2GB/3GB/4GB
	}

	/**
	 * 获取运营商信息
	 * 
	 * @return 运营商信息
	 */
	public static String getOperators(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String operator = null;
		String IMSI = tm.getSubscriberId();
		if (IMSI == null || IMSI.equals("")) {
			return operator;
		}
		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
			operator = "中国移动";
		} else if (IMSI.startsWith("46001")) {
			operator = "中国联通";
		} else if (IMSI.startsWith("46003")) {
			operator = "中国电信";
		}
		return operator;
	}

	/**
	 * 判断是否包含SIM卡
	 *
	 * @return 是否有SIM卡
	 */
	public static boolean hasSimCard(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		int simState = tm.getSimState();
		boolean result = true;
		switch (simState) {
		case TelephonyManager.SIM_STATE_ABSENT:
			result = false; // 没有SIM卡
			break;
		case TelephonyManager.SIM_STATE_UNKNOWN:
			result = false;
			break;
		}
		return result;
	}
	
	/**
	 * 获取DNS信息
	 *
	 * @return 获取DNS信息
	 */
	public static String getLocalDNS() {
		Process cmdProcess = null;
		BufferedReader reader = null;
		String dnsIP = "";
		try {
			cmdProcess = Runtime.getRuntime().exec("getprop net.dns1");
			reader = new BufferedReader(new InputStreamReader(cmdProcess.getInputStream()));
			dnsIP = reader.readLine();
			return dnsIP;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
			}
			cmdProcess.destroy();
		}
	}
	
	/**
	 * 获取WIFI功能
	 *
	 * @return WIFI功能
	 */
	public static boolean isWiFiActive(Context context){
		ConnectivityManager connectivity=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity!=null){
			NetworkInfo[] info=connectivity.getAllNetworkInfo();
			if(info!=null){
				for(int i=0; i<info.length; i++){
					if(info[i].getTypeName().equals("WIFI") && info[i].isConnected()){
						return true;}
					}
				}
			}
		return false;
		} 
	
	/**
     * 获取手机内部存储空间
     * 
     * @param context
     * @return 以M,G为单位的容量
     */
    public static String getInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        long size = blockCountLong * blockSizeLong;
        return Formatter.formatFileSize(context, size);
    }
    
    /**
     * 获取手机内部空间总大小
     *
     * @return 大小，字节为单位
     */
    static public String getTotalInternalMemorySize(Context context) {
        //获取内部存储根目录
        File path = Environment.getDataDirectory();
        //系统的空间描述类
        StatFs stat = new StatFs(path.getPath());
        //每个区块占字节数
        long blockSize = stat.getBlockSize();
        //区块总数
        long totalBlocks = stat.getBlockCount();
        return Formatter.formatFileSize(context,totalBlocks * blockSize);
    }
}
