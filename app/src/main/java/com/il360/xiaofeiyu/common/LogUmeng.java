package com.il360.xiaofeiyu.common;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;

public class LogUmeng {
	
	public static void reportError(Context context, String error) {
		MobclickAgent.reportError(context, error);
	}
	
	public static void reportError(Context context, Throwable e) {
		MobclickAgent.reportError(context, e);
	}

}
