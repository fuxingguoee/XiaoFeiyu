package com.il360.xiaofeiyu.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.il360.xiaofeiyu.activity.WelcomeActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.GetObjectRequest;
import com.tencent.cos.task.listener.IDownloadTaskListener;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ImagePathFromTxyUtil {
	
	public static void loadImage(Context context, String url, String flag) {
		initSignForUrl(context, url, flag);
	}

	public static void initSignForUrl(final Context context, final String url, final String flag) {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					// params.put("token", UserUtil.getToken());
					params.put("picurl", url);
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign",
							params);
					if (result.getSuccess()) {
						final JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							loadPic(context, url, obj.getString("result"), flag);
						}
					}
				} catch (Exception e) {
				}
			}
		});
	}

	public static void loadPic(final Context context, final String url, String sign, final String flag) {
		final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "load";

		GetObjectRequest getObjectRequest = new GetObjectRequest(url, savePath);
		getObjectRequest.setSign(sign);
		getObjectRequest.setListener(new IDownloadTaskListener() {
			@Override
			public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
			}

			@Override
			public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

				if(flag.equals("1")){
					String fileName = getNameFromUrl(url);
					String picPath = savePath + File.separator + fileName;
					Bitmap signBitmap = BitmapFactory.decodeFile(picPath);
					LoginActivity.setPicPathBigImage(signBitmap);
				} else if(flag.equals("2")){
					String fileName = getNameFromUrl(url);
					String picPath = savePath + File.separator + fileName;
					Bitmap signBitmap = BitmapFactory.decodeFile(picPath);
					LoginActivity.setPicPathBlockImage(signBitmap);
				}

			}

			@Override
			public void onFailed(COSRequest COSRequest, COSResult cosResult) {
				Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
			}

			@Override
			public void onCancel(COSRequest arg0, COSResult arg1) {

			}
		});

		WelcomeActivity.getCOSClient().getObject(getObjectRequest);
	}

	public static String getNameFromUrl(String loadUrl) {
		String[] a = loadUrl.split("/");
		String s = a[a.length - 1];
		return s;
	}
}
