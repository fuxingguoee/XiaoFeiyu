package com.il360.xiaofeiyu.util;

import android.app.Activity;
import android.content.Context;
import android.view.Display;

/**
 * 单位之间的转换类
 * @Description: 单位之间的转换类
 */
public class DimensionUtil {
	/**
	 * 根据手机的分辨率从 dp的单位转成为 px(像素)
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素)的单位转成为 dp
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * 获取手机的分辨率
	 * @param activity
	 * @param orientation 0:宽; 1:高
	 * @return int
	 */
	public static int getPhoneWH(Activity activity,int orientation){
		//DisplayMetrics dm = new DisplayMetrics();
		Display display = activity.getWindowManager().getDefaultDisplay();
        //int density = (int) dm.density;
        if(orientation == 0)
        	//return (int) (dm.widthPixels * density);
        	return display.getWidth();
        else if(orientation == 1)
        	//return (int) (dm.heightPixels * density);
        	return display.getHeight();
        else
        	return 0;
	}
}
