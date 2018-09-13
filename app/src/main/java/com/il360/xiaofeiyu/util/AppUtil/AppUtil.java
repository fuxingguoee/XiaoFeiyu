package com.il360.xiaofeiyu.util.AppUtil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AppUtil {
	/**
	 * 获取App具体设置
	 *
	 * @param context 上下文
	 */
	public static void getAppDetailsSettings(Context context, int requestCode) {
	    getAppDetailsSettings(context, context.getPackageName(), requestCode);
	}
	 
	/**
	 * 获取App具体设置
	 *
	 * @param context     上下文
	 * @param packageName 包名
	 */
	public static void getAppDetailsSettings(Context context, String packageName, int requestCode) {
		if (isSpace(packageName)) {
			return;
		} else {
//			((android.support.v7.app.AppCompatActivity) context).startActivityForResult(getAppDetailsSettingsIntent(packageName), requestCode);
			context.startActivity(getAppDetailsSettingsIntent(packageName));
		}
	}
	 
	/**
	 * 获取App具体设置的意图
	 *
	 * @param packageName 包名
	 * @return intent
	 */
	public static Intent getAppDetailsSettingsIntent(String packageName) {
	    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
	    intent.setData(Uri.parse("package:" + packageName));
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    return intent;
	}
	 
	/**
	 * 通过任务管理器杀死进程
	 * 需添加权限 {@code <uses-permission android:name="android.permission.RESTART_PACKAGES">}<p> </p>
	 *
	 * @param context
	 */
	public static void restart(Context context) {
	    int currentVersion = android.os.Build.VERSION.SDK_INT;
	    if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
	        Intent startMain = new Intent(Intent.ACTION_MAIN);
	        startMain.addCategory(Intent.CATEGORY_HOME);
	        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        context.startActivity(startMain);
	        System.exit(0);
	    }
	}
	
	
    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
	public static boolean isSpace(final String s) {
		if (s == null)
			return true;
		for (int i = 0, len = s.length(); i < len; ++i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
