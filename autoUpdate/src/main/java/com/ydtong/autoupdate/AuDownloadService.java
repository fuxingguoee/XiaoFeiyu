package com.ydtong.autoupdate;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.ydtong.autoupdate.net.HttpResult;
import com.ydtong.autoupdate.net.HttpUtils;

public class AuDownloadService extends Service {
	
	private String downloadUrl = "";
	private NotificationManager notificationManager;
	private Notification notification;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		downloadNotify();
		Bundle bun = intent.getExtras();
		downloadUrl =  bun.getString("url");
		final String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath() + AuConst.APK_PATH;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HttpResult result = HttpUtils.formDownload(downloadUrl, sdCardPath, handler);
				if(result.isSuccess()) {
					handler.sendEmptyMessage(1);
				} else {
					handler.sendEmptyMessage(2);
				}
				
			}
		}).start();
		
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	public Handler handler = new Handler() {
		Intent intent = null;
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				intent = new Intent(AuConst.ACTION_DOWNLOAD_SUCCESS);
				sendBroadcast(intent);
				break;
			case 2:
				intent = new Intent(AuConst.ACTION_DOWNLOAD_FAILED);
				sendBroadcast(intent);
			case 3:
				notification.contentView.setTextViewText(R.id.content_view_text1, msg.arg1 + "%");
				notification.contentView.setProgressBar(R.id.content_view_progress, 100, msg.arg1, false);
				notificationManager.notify(0, notification);
				break;
			case 4:
				notification.contentView.setTextViewText(R.id.content_view_text1, "下载成功，即将安装");
				notification.contentView.setProgressBar(R.id.content_view_progress, 100, 100, false);
				notificationManager.notify(0, notification);
				break;
			default:
				break;
			}
			stopSelf();
		};
	};
	
	private void downloadNotify() {
		//点击通知栏后打开的activity  
//        Intent intent = new Intent(this, AuUpdateActivity.class);
        
//		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = R.drawable.ic_app_icon;
		notification.tickerText = "下载进度";
		notification.contentView = new RemoteViews(getPackageName(), R.layout.au_notifycation_view);
//		notification.contentIntent = pIntent;
		notificationManager.notify(0, notification);
		
	}
	
	

}
