package com.il360.xiaofeiyu.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class AppInfoUtil {
	private static List<String> appList = new ArrayList<String>();

	/**
	 * 获取非系统应用信息列表
	 */
	public static List<String> getAppNameList(Context mContext) {
		if(appList != null && appList.size() > 0){
			appList.clear();
		}
		PackageManager pm = mContext.getPackageManager();
		// Return a List of all packages that are installed on the device.
		List<PackageInfo> packages = pm.getInstalledPackages(0);
		for (PackageInfo packageInfo : packages) {
			// 判断系统/非系统应用
			if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) // 非系统应用
			{
				if(packageInfo.applicationInfo.loadLabel(pm).toString().length() < 30){
					appList.add(packageInfo.applicationInfo.loadLabel(pm).toString());
				}
				
			} else {
				// 系统应用
			}
		}
		return appList;
	}
}
