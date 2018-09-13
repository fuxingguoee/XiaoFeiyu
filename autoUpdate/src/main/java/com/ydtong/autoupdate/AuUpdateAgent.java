package com.ydtong.autoupdate;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ydtong.autoupdate.net.HttpResult;
import com.ydtong.autoupdate.net.HttpUtils;

public class AuUpdateAgent {
	
	public static final String TAG = "AuUpdateAgent";
	public static final String MD_AU_APPKEY = "AU_APPKEY";
	public static final String MD_AU_CHANNEL = "AU_CHANNEL";
	
	private static Context mContext;
	private static AuUpdateDialog.Builder builder;
	private static AuUpdateInfo info;
	private static AuDownloadReceiver downloadReceiver;
	
	private static String AU_APPKEY;
	private static String AU_CHANNEL;
	
	public static void forceUpdate(Context context) {
		mContext = context;
		if(readMetaData()) {
			builder = new AuUpdateDialog.Builder(context);
			IntentFilter filter = new IntentFilter();
			filter.addAction(AuConst.ACTION_DOWNLOAD_SUCCESS);
			downloadReceiver = new AuDownloadReceiver(handle);
			mContext.registerReceiver(downloadReceiver, filter);
			launch();
		} else {
			handle.sendEmptyMessage(0);
		}
	}
	
	public static boolean readMetaData() {
		try {
			AU_APPKEY = ApkUtils.getMetaDataValue(mContext, MD_AU_APPKEY);
			AU_CHANNEL = ApkUtils.getMetaDataValue(mContext, MD_AU_CHANNEL);
			if(TextUtils.isEmpty(AU_APPKEY) || TextUtils.isEmpty(AU_CHANNEL)) {
				return false;
			}
			return true;
		} catch (Exception e) {
			Log.e(TAG, "readMetaData", e);
			return false;
		}
	}
	
	public static void setDialogListener(final AuDialogButtonListener listener) {
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                //设置你的操作事项  
                listener.onClick(AuUpdateStatus.Update);
                if(ApkUtils.existSDCard()) {
                	Intent intent = new Intent(mContext, AuDownloadService.class);
                    intent.putExtra("url", info.getVersionAddress());
                    mContext.startService(intent);
                } else {
                	handle.sendEmptyMessage(5);
                }
                
            }  
        });  
  
        builder.setNegativeButton("取消",  
                new android.content.DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int which) { 
                    	listener.onClick(AuUpdateStatus.Ignore);
                        dialog.dismiss();  
                        
                    }  
                });  
  
	}
	
	public static Handler handle = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case 0:
				Toast.makeText(mContext, "无效的参数", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				info = (AuUpdateInfo) msg.obj;
				builder.setMessage(info.getVersionContent());
				builder.setTitle("自动更新");
				AuUpdateDialog dialog = builder.create();
				dialog.setCancelable(false);
				dialog.show();
				break;
			case 2:
				Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
				startInstall();
				break;
			case 3:
				Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(mContext, "签名校验失败", Toast.LENGTH_SHORT).show();
				break;
			case 5:
				Toast.makeText(mContext, "应用下载失败，请插入SD卡", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
	
	public static void launch() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				AuUpdateInfo info = getUpdateInfo();
				if(info != null) {
					Message msg = new Message();
					msg.what = 1;
					msg.obj = info;
					handle.sendMessage(msg);
				}
			}
		}).start();
	}
	
	
	
	private static AuUpdateInfo getUpdateInfo() {
		Integer versionCode = ApkUtils.getAppVersionCode(mContext);
		
		HttpUtils.BASIC_USER = AuConst.BASIC_USER;
		HttpUtils.BASIC_PWD = AuConst.BASIC_PWD;
		Map<String, String> params = new HashMap<String, String>();
		params.put("appName", AU_APPKEY);
		params.put("versionNo", versionCode.toString());
		params.put("channel", AU_CHANNEL);
		params.put("type", AuConst.TYPE_ANDROID);
		HttpResult result = HttpUtils.sendGet(AuConst.URL + "queryVersion", params);
		if(result.isSuccess()) {
			AuUpdateInfo info = parseFromJson(result.getResult().toString());
			return info;
		} else {
			Log.e("getUpdateInfo", result.getResult() == null ? "无响应结果" : result.getResult().toString());
		}
		return null;
	}
	
	private static AuUpdateInfo parseFromJson(String result) {
		AuUpdateInfo auInfo = null;
		try {
			JSONObject obj = new JSONObject(result);
			int code = obj.getInt("code");
			if(code == 1) {
				JSONObject jsonInfo = obj.getJSONObject("result");
				String appName = jsonInfo.getString("appName");
				String channel = jsonInfo.getString("channel");
				String versionContent = jsonInfo.getString("versionContent");
				Integer versionId = jsonInfo.getInt("versionId");
				String versionNo = jsonInfo.getString("versionNo");
				String versionAddress = jsonInfo.getString("versionAddress");
				auInfo = new AuUpdateInfo();
				auInfo.setAppName(appName);
				auInfo.setChannel(channel);
				auInfo.setVersionContent(versionContent);
				auInfo.setVersionId(versionId);
				auInfo.setVersionNo(versionNo);
				auInfo.setVersionAddress(versionAddress);
			} else {
				String desc = obj.getString("desc");
				Log.i("获取更新结果", desc);
			}
		} catch (Exception e) {
			Log.e(TAG, "parseFromJson", e);
		}
		return auInfo;
	}
	
	private static void startInstall() {
		try {
			String apkPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + AuConst.APK_PATH + AuConst.APK_NAME;
			String newSign = ApkUtils.showUninstallAPKSignatures(apkPath);
			String oldSign = ApkUtils.getSign(mContext);
			if (oldSign.equals(newSign)) {
				ApkUtils.install(mContext, apkPath);
			} else {
				handle.sendEmptyMessage(4);
			}
		} catch (Exception e) {
			Log.e(TAG, "startInstall", e);
		}

	}
	

}
