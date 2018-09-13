package com.ydtong.autoupdate;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

public class ApkUtils {

	/**
	 * 返回当前程序版本名
	 */
	public static String getAppVersionName(Context context) {
		String versionName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if (versionName == null || versionName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versionName;
	}

	/**
	 * 返回当前程序版本号
	 */
	public static Integer getAppVersionCode(Context context) {
		Integer versioncode = null;
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versioncode = pi.versionCode;
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return versioncode;
	}

	/**
	 * 返回当前程序包名
	 */
	public static String getAppPackageName(Context context) {
		String packageName = "";
		try {
			// ---get the package info---
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			packageName = pi.packageName;
			if (packageName == null || packageName.length() <= 0) {
				return "";
			}
		} catch (Exception e) {
			Log.e("VersionInfo", "Exception", e);
		}
		return packageName;
	}

	/**
	 * 获取APK的签名信息
	 * 
	 * @param apkPath
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String showUninstallAPKSignatures(String apkPath) {
		String PATH_PackageParser = "android.content.pm.PackageParser";
		
		File apkFile = new File(apkPath);
		if (!apkFile.exists() || !apkPath.toLowerCase().endsWith(".apk")) {
			System.out.println("file path is not correct");
			return null;
		}
		
		try {
			// apk包的文件路径
			// 这是一个Package 解释器, 是隐藏的
			// 构造函数的参数只有一个, apk文件的路径
			// PackageParser packageParser = new PackageParser(apkPath);
			Class pkgParserCls = Class.forName(PATH_PackageParser);
			Class[] typeArgs = new Class[1];
			typeArgs[0] = String.class;
			Object[] valueArgs = new Object[1];
			valueArgs[0] = apkPath;
			Constructor pkgParserCt;
			Object pkgParser;
			
			 Method pkgParser_parsePackageMtd;  
		     Object pkgParserPkg;  
			
			 if (Build.VERSION.SDK_INT >= 21) { 
				 pkgParserCt = pkgParserCls.getConstructor();
		            pkgParser = pkgParserCt.newInstance();
		            typeArgs = new Class[2];  
		            typeArgs[0] = File.class;  
		            typeArgs[1] = Integer.TYPE;  
		            pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",typeArgs);  
		  
		            valueArgs = new Object[2];  
		            valueArgs[0] = new File(apkPath);  
		            valueArgs[1] = 0;  
		  
		            pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);  
			 }else{
				 pkgParserCt = pkgParserCls.getConstructor(typeArgs);  
		            pkgParser = pkgParserCt.newInstance(valueArgs);  
		  
		            typeArgs = new Class<?>[] { File.class, String.class, DisplayMetrics.class, int.class };  
		            pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);  
		  
		            DisplayMetrics metrics = new DisplayMetrics();  
		            metrics.setToDefaults();  
		  
		            valueArgs = new Object[4];  
		            valueArgs[0] = new File(apkPath);  
		            valueArgs[1] = apkPath;  
		            valueArgs[2] = metrics;  
		            valueArgs[3] = 0;  
		  
		            pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
			 }
//			Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
//			Object[] valueArgs = new Object[1];
//			valueArgs[0] = apkPath;
//			Object pkgParser = pkgParserCt.newInstance(valueArgs);
			Log.d("ApkUtils", "pkgParser:" + pkgParser.toString());
			// 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
			DisplayMetrics metrics = new DisplayMetrics();
			metrics.setToDefaults();
			// PackageParser.Package mPkgInfo = packageParser.parsePackage(new
			// File(apkPath), apkPath,
			// metrics, 0);
//			typeArgs = new Class[4];
//			typeArgs[0] = File.class;
//			typeArgs[1] = String.class;
//			typeArgs[2] = DisplayMetrics.class;
//			typeArgs[3] = Integer.TYPE;
//			pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod(
//					"parsePackage", typeArgs);
//			valueArgs = new Object[4];
//			valueArgs[0] = new File(apkPath);
//			valueArgs[1] = apkPath;
//			valueArgs[2] = metrics;
//			valueArgs[3] = PackageManager.GET_SIGNATURES;
//			pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser,
//					valueArgs);

			typeArgs = new Class[2];
			typeArgs[0] = pkgParserPkg.getClass();
			typeArgs[1] = Integer.TYPE;
			Method pkgParser_collectCertificatesMtd = pkgParserCls
					.getDeclaredMethod("collectCertificates", typeArgs);
			valueArgs = new Object[2];
			valueArgs[0] = pkgParserPkg;
			valueArgs[1] = PackageManager.GET_SIGNATURES;
			pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
			// 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
			Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField(
					"mSignatures");
			Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
			Log.d("ApkUtils", "size:" + info.length);
			Log.d("ApkUtils", info[0].toCharsString());
			return info[0].toCharsString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	

	/**
	 * 获取程序自身的签名
	 * 
	 * @param context
	 * @return
	 */
	public static String getSign(Context context) {
		String curPackage = getAppPackageName(context);
//		curPackage = "com.bxs.klyg.app";
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> apps = pm
				.getInstalledPackages(PackageManager.GET_SIGNATURES);
		Iterator<PackageInfo> iter = apps.iterator();
		while (iter.hasNext()) {
			PackageInfo packageinfo = iter.next();
			String packageName = packageinfo.packageName;
			if (packageName.equals(curPackage)) {
				Log.d("ApkUtils", packageinfo.signatures[0].toCharsString());
				return packageinfo.signatures[0].toCharsString();
			}
		}
		return null;
	}
	
	/** 
     * 安装 
     *  
     * @param context 
     *            接收外部传进来的context 
     */  
    public static void install(Context context, String apkPath) {  
        // 核心是下面几句代码  
        Intent intent = new Intent(Intent.ACTION_VIEW);  
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),  
                "application/vnd.android.package-archive");  
        context.startActivity(intent);  
    }  
    
    /**
     * 获取meta-data值
     * @param context
     * @param name
     * @return
     */
    public static String getMetaDataValue(Context context, String name) {
        Object value = null;
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo(context
            .getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                value = applicationInfo.metaData.get(name);
            }
        } catch (NameNotFoundException e) {
            throw new RuntimeException(
            "Could not read the name in the manifest file.", e);
        }
        if (value == null) {
            throw new RuntimeException("The name '" + name
            + "' is not defined in the manifest file's meta data.");
        }
        return value.toString();
     
    }
    
    /**
     * 是否存在SD卡
     * @return
     */
	public static boolean existSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

}
