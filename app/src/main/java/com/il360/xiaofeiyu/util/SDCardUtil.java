package com.il360.xiaofeiyu.util;

import com.il360.xiaofeiyu.R;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * 操作SD卡
 * @author Steven
 * @version 1.15 
 * @modify 2013-8-14 上午10:11:17
 */
public class SDCardUtil {
	
	/**
	 * 检测存储设备
	 * @param Context context 上下文对象
	 * @return Boolean 是否检测到SD卡
	 */
	public static boolean hasSDCard(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
		}else {
			Toast.makeText(context, context.getResources().getString(R.string.storageDevice), Toast.LENGTH_SHORT).show();
			return false;
		}
	}
}