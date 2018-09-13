package com.il360.xiaofeiyu.common;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.il360.xiaofeiyu.activity.main.MainActivity_;
import com.moxie.client.manager.MoxieSDK;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import cn.fraudmetrix.octopus.aspirit.main.OctopusManager;

/** 
 * 编写自己的Application，管理全局状态信息，比如Context
 * @author yy 
 * 
 */  
public class MyApplication extends android.support.multidex.MultiDexApplication{
   
	private static Context context;

	public Vibrator mVibrator;
	
	public LocationClient mLocationClient;

	@Override
	public void onCreate() {
		// 获取Context
		context = getApplicationContext();
		
		mVibrator = (Vibrator) getApplicationContext().getSystemService(
				Service.VIBRATOR_SERVICE);

//		 ImageLoaderUtil.init(context);
		 
		 mLocationClient = new LocationClient(this.getApplicationContext());
		 initLocation();
		 
		 initPush();
		 
		 UMShareAPI.get(this);
		 initShare();
		 
		 MoxieSDK.init(this);
		 
		 OctopusManager.getInstance().init(this,"cmsc_mohe","1c7f65b25ca48ff96f1a4dd568b51d4d");
		 
		super.onCreate();
	}
	
	private void initShare() {
		PlatformConfig.setWeixin(Variables.WX_APPID,Variables.WX_APPSECRET);
        PlatformConfig.setQQZone(Variables.QQ_APPID, Variables.QQ_APPSECRET);
	}

	private void initPush() {
		PushAgent mPushAgent = PushAgent.getInstance(this);
		//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {

		    @Override
		    public void onSuccess(String deviceToken) {
		        //注册成功会返回device token
		    	String device_token = deviceToken;
		    }

		    @Override
		    public void onFailure(String s, String s1) {
		    	String ss = s + s1;
		    }
		});
		
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
			@Override
			public void launchApp(Context context, UMessage msg) {
				Intent intent = new Intent(context, MainActivity_.class);
				intent.putExtra("title", msg.title);
				intent.putExtra("text", msg.text);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		};

		mPushAgent.setNotificationClickHandler(notificationClickHandler);
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("gcj02");// 可选，默认gcj02，设置返回的定位结果坐标系，
		option.setScanSpan(0);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

		mLocationClient.setLocOption(option);
	}
	
	// 返回
	public static Context getContextObject() {
		return context;
	}
}
