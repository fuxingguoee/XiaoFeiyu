package com.il360.xiaofeiyu.common;


public class Constants {

	// appid
	// 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data
	// android:scheme="wxf267cc65fb7d67c7"/>为新设置的appid
	public static final String APP_ID = "wxf267cc65fb7d67c7";
	
//	public static BizEnum BIZ_ENUM = null;
	public static String BIZ_NO = null;//业务订单号
	
	
	
	public static final int BASE_ID = 0;
	public static final int RQF_PAY = BASE_ID + 1;
	public static final int RQF_INSTALL_CHECK = RQF_PAY + 1;
	public static final String SERVER_URL = "http://yintong.com.cn/secure_server/x.htm";
	public static final String PAY_PACKAGE = "com.yintong.secure";
	// 银通支付安全支付服务apk的名称，必须与assets目录下的apk名称一致
	public static final String YT_PLUGIN_NAME = "SecurePay.apk";

	public static final String RET_CODE_SUCCESS = "0000";// 0000 交易成功
	public static final String RET_CODE_PROCESS = "2008";// 2008 支付处理中
	public static final String RESULT_PAY_SUCCESS = "SUCCESS";
	public static final String RESULT_PAY_PROCESSING = "PROCESSING";
	public static final String RESULT_PAY_FAILURE = "FAILURE";
	public static final String RESULT_PAY_REFUND = "REFUND";
    //异步通知回调地址
    public static final String NOTIFY_URL = "http://10.20.41.35:8080/tradepayapi/receiveNotify.htm";
}

class YTPayDefine {
	public static final String IMEI = "imei";
	public static final String IMSI = "imsi";
	public static final String KEY = "key";
	public static final String USER_AGENT = "user_agent";
	public static final String VERSION = "version";
	public static final String DEVICE = "device";
	public static final String SID = "sid";
	public static final String PARTNER = "partner";
	public static final String TRANSCODE = "transcode";
	public static final String CHARSET = "charset";
	public static final String SIGN_TYPE = "sign_type";
	public static final String SIGN = "sign";

	public static final String URL = "URL";
	public static final String SPLIT = "&";

	public static final String ACTION = "action";
	public static final String ACTION_UPDATE = "update";
	public static final String DATA = "data";
	public static final String PLATFORM = "platform";


}
