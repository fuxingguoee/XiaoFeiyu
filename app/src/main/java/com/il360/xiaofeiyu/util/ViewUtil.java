package com.il360.xiaofeiyu.util;

import com.il360.xiaofeiyu.activity.main.MainActivity_;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class ViewUtil {
	/**
	 * 获得textview的text
	 * @param textView
	 * @return
	 */
	public static String getText(TextView textView) {
		return textView.getText().toString().trim();
	}
	
	/**
	 * 返回APP应用首页MainActivity
	 * @param view
	 */
	public static void backHomeActivity(Context context) {
		Intent intent = new Intent(context, MainActivity_.class);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		context.startActivity(intent);
	}
}
