package com.ydtong.autoupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class AuDownloadReceiver extends BroadcastReceiver {
	
	private Handler mHandle;
	
	public AuDownloadReceiver(Handler handle) {
		mHandle = handle;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if(action.equals(AuConst.ACTION_DOWNLOAD_SUCCESS)) {
			mHandle.sendEmptyMessage(2);
		} else if(action.equals(AuConst.ACTION_DOWNLOAD_FAILED)) {
			mHandle.sendEmptyMessage(3);
		}
	}
	
}
