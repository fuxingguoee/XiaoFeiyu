package com.il360.xiaofeiyu.activity.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.captcha.Captcha;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.common.MyApplication;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.NetWork;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.address.ArrayOfUserAddress;
import com.il360.xiaofeiyu.model.home.ArrayOfSwitch;
import com.il360.xiaofeiyu.model.home.OutContact;
import com.il360.xiaofeiyu.model.home.RemainTimes;
import com.il360.xiaofeiyu.model.hua.OutUserBank;
import com.il360.xiaofeiyu.model.user.LoginUserReturnMessage;
import com.il360.xiaofeiyu.model.user.OutUserRz;
import com.il360.xiaofeiyu.model.user.ProjectileContent;
import com.il360.xiaofeiyu.model.user.SliderInfo;
import com.il360.xiaofeiyu.util.AESEncryptor;
import com.il360.xiaofeiyu.util.AppInfoUtil;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ImagePathFromTxyUtil;
import com.il360.xiaofeiyu.util.StringUtil;
import com.il360.xiaofeiyu.util.ThreeDES;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;
import com.umeng.message.PushAgent;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_login)
public class LoginActivity extends BaseWidgetActivity {
	
	@ViewById
	LinearLayout llLoginPwd;//密码登录
	@ViewById
	TextView line1;
	@ViewById
	RelativeLayout rlLoginPwd;
	
	@ViewById
	LinearLayout llVerificaCode;//短信登录
	@ViewById
	TextView line2;
	@ViewById
	RelativeLayout rlVerificaCode;
	@ViewById
	EditText etVerificaCode;
	@ViewById
	TextView tvGetCode;

	private int loginType = 1;//1密码登录2短信登录
	
	/** 倒计时器 */
	private Timer timer = new Timer();
	private static final int TIME_DOWN_COUNT = 60;// 倒计时60秒
	private int time = 0;// 倒计时计时数,初始为0
	
	/** 登录账户 **/
	@ViewById
	EditText etAccount;
	/** 登录密码 **/
	@ViewById
	EditText etPassword;
	/** 忘记密码 **/
	@ViewById
	TextView tvForgetPwd;
	/** 注册 **/
	@ViewById
	TextView btnRegister;
	/** 登录 **/
	@ViewById
	Button btnLogin;
	/** 密码显示/隐藏 **/
	@ViewById
	LinearLayout lly_isshow_pwd;
	@ViewById
	ImageView iv_isshow_pwd;
	/** 密码当前状态 （显示/隐藏） **/
	private boolean isshow = false;

	private ProjectileContent myProjectileContent;
	
	protected ProgressDialog transDialog;
	
	public static LocationClient mLocationClient;
	public static MyLocationListener mMyLocationListener;
	private String myProvince;
	private String myCity;
	private String myDistrict;
    private String myLocation = "";
    private String myLongitude;//经度
    private String myLatitude;//纬度
	
	private String device_token;

	private static String encryTop;
	private static String encrtLeft;

	private static String bigImage;
	private static String blockImage;
	private static String top;

	private static Bitmap picPathBigImage;
	private static Bitmap picPathBlockImage;
	private static String left;

	public static String getLeft() {
		return left;
	}

	public static void setLeft(String left) {
		LoginActivity.left = left;
	}

	public static Bitmap getPicPathBigImage() {
		return picPathBigImage;
	}

	public static void setPicPathBigImage(Bitmap picPathBigImage) {
		LoginActivity.picPathBigImage = picPathBigImage;
	}

	public static Bitmap getPicPathBlockImage() {
		return picPathBlockImage;
	}

	public static void setPicPathBlockImage(Bitmap picPathBlockImage) {
		LoginActivity.picPathBlockImage = picPathBlockImage;
	}

	private int flag = 0;
	private int flag2 = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		LoginActivity.mLocationClient = ((MyApplication) getApplication()).mLocationClient;
		LoginActivity.mMyLocationListener = new MyLocationListener();
		LoginActivity.mLocationClient.registerLocationListener(LoginActivity.mMyLocationListener);
		
		PushAgent mPushAgent = PushAgent.getInstance(this);
		device_token = mPushAgent.getRegistrationId();
	}
	/**
     * 实现实时位置回调监听
     */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location != null) {
				if (location.getLocType() == BDLocation.TypeNetWorkLocation && location.getAddress() != null
						&& !TextUtils.isEmpty(location.getAddress().city)) {

					String PROVINE = location.getAddress().province == null ? "" : location.getAddress().province;
					String CITY = location.getAddress().city == null ? "" : location.getAddress().city;
					String DISTRICT = location.getAddress().district == null ? "" : location.getAddress().district;
					String STREET = location.getAddress().street == null ? "" : location.getAddress().street;
					String STREETNUMBER = location.getAddress().streetNumber == null ? "" : location.getAddress().streetNumber;
					String address = STREET + STREETNUMBER;
					myLongitude = location.getLongitude() + "";
					myLatitude = location.getLatitude() + "";
					myLocation = StringUtil.getStringSub(address);
					myProvince = StringUtil.getStringSub(PROVINE);
					myCity = StringUtil.getStringSub(CITY);
					myDistrict = StringUtil.getStringSub(DISTRICT);
					return;
				}
				if(location.getLocType() != 61){
					showInfo(getResources().getString(R.string.location_failure) + "(" + location.getLocType() + ")");
				}
			} else {
				showInfo(getResources().getString(R.string.location_failure));
			}

		}
	}
    
	private void GPS() {
		if (!NetWork.checkNetWorkStatus(this)) {
			showInfo(getResources().getString(R.string.check_network));
		} else {
			mLocationClient.start();// 定位SDK start之后会默认发起一次定位请求
			mLocationClient.requestLocation();
		}
	}

	@AfterViews
	void init() {
		GlobalPara.appNameList = AppInfoUtil.getAppNameList(LoginActivity.this);
		
		line1.setVisibility(View.VISIBLE);
		rlLoginPwd.setVisibility(View.VISIBLE);
		line2.setVisibility(View.INVISIBLE);
		rlVerificaCode.setVisibility(View.GONE);
		loginType = 1;

		initPop();

		initTimeDown();

//		 etAccount.setText("13958302557");
//		 etAccount.setText("18758327096");
//		 etPassword.setText("123456");
//		 etAccount.setText("18815292634");
//		 etPassword.setText("111111");
		 GPS();
	}
	
	/** 验证倒计时 */
	private void initTimeDown() {
		timer.schedule(task, 100, 1000);
	}

	/** 倒计时的内容 */
	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			mHandler.sendEmptyMessage(time--);
		}
	};

	/** handler */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what > 0) {
				tvGetCode.setText(msg.what + "秒后重发");
				tvGetCode.setEnabled(false);
			} else {
				if (!ViewUtil.getText(etAccount).equals("")) {
					if (validateAccount()) {
						tvGetCode.setText("发送验证码");
						tvGetCode.setEnabled(true);
					} else {
						tvGetCode.setEnabled(false);
					}
				} else {
					tvGetCode.setEnabled(false);
				}
			}
		};
	};
	
	@Click
	void tvGetCode() {
		tvGetCode.setClickable(false);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("phone", etAccount.getText().toString());
					params.put("verifyType", "3");// 3用户登录验证码
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"user/attachUserVerifyCode", params);
					if (result.getSuccess()) {
						try {
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								JSONObject objRes = obj.getJSONObject("result");
								String desc = (String) objRes.getString("returnMessage");
								showInfo(desc);
								time = TIME_DOWN_COUNT;
							} else {
								showInfo(obj.getString("result"));
							}
						} catch (JSONException e) {
							Log.e("LoginActivity", "tvGetCode", e);
							LogUmeng.reportError(LoginActivity.this, e);
						}
					} else {
						showInfo(getResources().getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				} finally {
					if (!tvGetCode.isClickable()) {
						tvGetCode.setClickable(true);
					}
				}
			}
		});
	}

	@AfterTextChange
	void etAccount() {
		validateLogin();
		validateGetCode();
	}

	@AfterTextChange
	void etPassword() {
		validateLogin();
	}
	
	@AfterTextChange
	void etVerificaCode() {
		validateLogin();
	}
	
	@Click
	void llLoginPwd(){
		line1.setVisibility(View.VISIBLE);
		rlLoginPwd.setVisibility(View.VISIBLE);
		line2.setVisibility(View.INVISIBLE);
		rlVerificaCode.setVisibility(View.GONE);
		loginType = 1;
		validateLogin();
	}
	
	@Click
	void llVerificaCode(){
		line1.setVisibility(View.INVISIBLE);
		rlLoginPwd.setVisibility(View.GONE);
		line2.setVisibility(View.VISIBLE);
		rlVerificaCode.setVisibility(View.VISIBLE);
		loginType = 2;
		validateLogin();
	}

	/**
	 * 密码显示/隐藏
	 */
	@Click
	void lly_isshow_pwd() {
		if (isshow) {
			isshow = false;
			iv_isshow_pwd.setBackgroundResource(R.drawable.ic_login_pwd_hide);
			etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());// 隐藏
		} else {
			isshow = true;
			iv_isshow_pwd.setBackgroundResource(R.drawable.ic_login_pwd_show);
			etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());// 显示
		}
	}

	/**
	 * 忘记密码
	 */
	@Click
	void tvForgetPwd() {
		Intent intent = new Intent(LoginActivity.this, GetBackPasswordActivity_.class);// 手机找回
		startActivity(intent);
	}

	/**
	 * 注册
	 */
	@Click
	void btnRegister() {
		Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);
		startActivity(intent);
//		Intent intent = new Intent(LoginActivity.this, SliderActivity_.class);
//		startActivity(intent);
	}

	/**
	 * 登录
	 */
	@Click
	void btnLogin(View view) {
		if (isok()) {

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			// 获取软键盘的显示状态
			if(imm.isActive()) {
				// 强制隐藏软键盘
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}

			if(flag == 1){
				runOnUiThread(new Runnable() {
					public void run() {
						showCaptcha();
					}
				});
			} else {

				initLogin();
			}
		}

	}
	

	private boolean isok() {
		if (ViewUtil.getText(etAccount).length() != 11 || !validateAccount()) {
			showInfo(getString(R.string.account_format_error));
		} else if (loginType == 1 && ViewUtil.getText(etPassword).length() < 6) {
			showInfo("登录密码至少6位");
		} else if (loginType == 2 && TextUtils.isEmpty(ViewUtil.getText(etVerificaCode))) {
			showInfo("请输入短信验证码");
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 验证登录按钮可用状态
	 */
	private void validateLogin() {
		if (ViewUtil.getText(etAccount).length() > 0) {
			if(loginType == 1 && ViewUtil.getText(etPassword).length() > 0){
				btnLogin.setEnabled(true);
			} else if(loginType == 2 && ViewUtil.getText(etVerificaCode).length() > 0){
				btnLogin.setEnabled(true);
			} else {
				btnLogin.setEnabled(false);
			}
		} else {
			btnLogin.setEnabled(false);
		}
	}
	
	private void validateGetCode() {
		if (!ViewUtil.getText(etAccount).equals("")) {
			if (validateAccount()) {
				tvGetCode.setEnabled(true);
			} else {
				tvGetCode.setEnabled(false);
			}
		} else {
			tvGetCode.setEnabled(false);
		}
	}

	/**
	 * 验证用户名输入格式规范 以字母开头，数字、下划线、“.“号组合，不超过16位
	 * 
	 * @return Boolean true:正常; false:错误;
	 */
	private boolean validateAccount() {
		Pattern p1 = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
		Matcher m1 = p1.matcher(etAccount.getText().toString().trim());
		return m1.matches();
	}

	private void initLogin() {
		if(flag == 0){
			btnLogin.setClickable(false);
			transDialog = ProgressDialog.show(LoginActivity.this, null, "加载中...", true);
		}
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("loginName", ViewUtil.getText(etAccount));
					params.put("type", loginType + "");
					params.put("location", myLocation);
					params.put("province", myProvince);
					params.put("city", myCity);
					params.put("area", myDistrict);
					params.put("iPhoneType", "2");
					params.put("longitude", myLongitude);
					params.put("latitude", myLatitude);
					if(flag == 1 || flag ==2){
						params.put("param1", encrtLeft);
						params.put("param2",encryTop);
					}

					if (loginType == 1) {
						params.put("loginPwd", AESEncryptor.encrypt(ThreeDES.encryptDESCBC(ViewUtil.getText(etPassword))));
					} else if (loginType == 2) {
						params.put("verifyCode", ViewUtil.getText(etVerificaCode));
					}
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "user/loginIn2",params);
					flag = 0;
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							runOnUiThread(new Runnable() {
								public void run() {
									popWin.dismiss();
								}
							});
							JSONObject objRes = obj.getJSONObject("result");
							LoginUserReturnMessage msg = FastJsonUtils.getSingleBean(objRes.toString(),
									LoginUserReturnMessage.class);
							if (msg.getReturnCode() == 1) {
								UserUtil.loadUserInfo(msg.getReturnResult());

								if(msg.getReturnMessage() != null){
									myProjectileContent = FastJsonUtils.getSingleBean(msg.getReturnMessage(),
											ProjectileContent.class);
								} else {
									myProjectileContent = null;
								}

								UserUtil.setProjectileContent(myProjectileContent);

								upDateUser();
								initUserRz();
								initAddress();
								initSwitch();
								doGoto();
							} else {
								showInfo(msg.getReturnMessage());
							}
						} else if(obj.getInt("code") == -3){//密码出错
							flag = 1;
							LoginActivity.setPicPathBigImage(null);
							LoginActivity.setPicPathBlockImage(null);
							showInfo(obj.getString("desc"));
							JSONObject objRes = obj.getJSONObject("result");
							SliderInfo info = FastJsonUtils.getSingleBean(objRes.toString(),
									 SliderInfo.class);
							bigImage = info.getBigImage();
							blockImage = info.getBlockImage();
							top = AESEncryptor.decrypt(AESEncryptor.KEY, info.getCaptchaParam());

							ImagePathFromTxyUtil.loadImage(LoginActivity.this, bigImage,"1");
							ImagePathFromTxyUtil.loadImage(LoginActivity.this, blockImage,"2");

							runOnUiThread(new Runnable() {
								public void run() {
									popWin.dismiss();
								}
							});

						} else if(obj.getInt("code") == -100){//滑块滑错
							flag = 2;
							LoginActivity.setPicPathBigImage(null);
							LoginActivity.setPicPathBlockImage(null);
							showInfo(obj.getString("desc"));
							JSONObject objRes = obj.getJSONObject("result");
							SliderInfo info = FastJsonUtils.getSingleBean(objRes.toString(),
									SliderInfo.class);
							bigImage = info.getBigImage();
							blockImage = info.getBlockImage();
							top = AESEncryptor.decrypt(AESEncryptor.KEY, info.getCaptchaParam());

							ImagePathFromTxyUtil.loadImage(LoginActivity.this, bigImage,"1");
							ImagePathFromTxyUtil.loadImage(LoginActivity.this, blockImage,"2");

							Thread.sleep(1500);

							if(LoginActivity.getPicPathBigImage() != null && LoginActivity.getPicPathBlockImage() !=null){
								runOnUiThread(new Runnable() {
									public void run() {
										showCaptcha();
									}
								});
							}

						} else if(obj.getInt("code") == -200){//滑块滑错5次
							flag = 0;
							showInfo(obj.getString("desc"));
							runOnUiThread(new Runnable() {
								public void run() {
									popWin.dismiss();
								}
							});
						} else if(obj.getInt("code") == -400){//验证码信息不正确
							flag = 1;
//							showInfo(obj.getString("desc"));
							LoginActivity.setPicPathBigImage(null);
							LoginActivity.setPicPathBlockImage(null);
							JSONObject objRes = obj.getJSONObject("result");
							SliderInfo info = FastJsonUtils.getSingleBean(objRes.toString(),
									SliderInfo.class);
							bigImage = info.getBigImage();
							blockImage = info.getBlockImage();
							top = AESEncryptor.decrypt(AESEncryptor.KEY, info.getCaptchaParam());

							ImagePathFromTxyUtil.loadImage(LoginActivity.this, bigImage,"1");
							ImagePathFromTxyUtil.loadImage(LoginActivity.this, blockImage,"2");

							Thread.sleep(1500);

							if(LoginActivity.getPicPathBigImage() != null && LoginActivity.getPicPathBlockImage() !=null){
								runOnUiThread(new Runnable() {
									public void run() {
										showCaptcha();
									}
								});
							}
						} else {
							runOnUiThread(new Runnable() {
								public void run() {
									popWin.dismiss();
								}
							});
							showInfo(obj.getString("desc"));
						}
					} else {
						showInfo(result.getResult());
					}
				} catch (Exception e) {
					showInfo("网络请求失败");
					Log.e("LoginActivity", "initLogin", e);
					LogUmeng.reportError(LoginActivity.this, e);
				} finally {
					if (!btnLogin.isClickable()) {
						btnLogin.setClickable(true);
					}

					if (transDialog != null && transDialog.isShowing()) {
						transDialog.dismiss();
					}
				}
			}
		});
	}
	
	private void initSwitch() {
		GlobalPara.mySwitchList = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "switch/queryAll", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				ArrayOfSwitch arrayOfSwitch = FastJsonUtils.getSingleBean(obj.toString(), ArrayOfSwitch.class);
				if(arrayOfSwitch.getCode() == 1){
					GlobalPara.mySwitchList = arrayOfSwitch.getSwitchConfigs();
					
					if(!UserUtil.judgeAuthentication() && GlobalPara.getCanAutoVerified()){
						initTimes();
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	private void initTimes(){
		GlobalPara.remainTimes = 0;
		GlobalPara.maxTimes = 0;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "fc/queryRemainTimes", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				RemainTimes remainTimes = FastJsonUtils.getSingleBean(obj.toString(), RemainTimes.class);
				if(remainTimes.getCode() != null && remainTimes.getCode() == 1){
					GlobalPara.remainTimes = remainTimes.getRmTimes();
					GlobalPara.maxTimes = remainTimes.getMaxTimes();
				}
			}
		} catch (Exception e) {
		}
	}

	protected void initAddress() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"user/queryUserAddress", params);
					if (result.getSuccess()) {
						ArrayOfUserAddress arrayOfUserAddress = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfUserAddress.class);
						if (arrayOfUserAddress.getCode() == 1) {
							if(arrayOfUserAddress.getResult() != null && arrayOfUserAddress.getResult().size() > 0){
								GlobalPara.userAddressList = arrayOfUserAddress.getResult();
							} 
						} 
					}
				} catch (Exception e) {
					Log.e("LoginActivity", "initAddress", e);
					LogUmeng.reportError(LoginActivity.this, e);
				}
			}
		});
		
	}

	protected void upDateUser() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userJson", makeUserJson());// userJson
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "user/updateUser", params);
			if (result.getSuccess()) {}
		} catch (Exception e) {
			Log.e("LoginActivity", "upDateUser", e);
			LogUmeng.reportError(LoginActivity.this, e);
		}
	}

	private String makeUserJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("deviceTokens", device_token);
			return json.toString();
		} catch (Exception e) {
			Log.e("UserInfoFragment", "makeJsonPost", e);
		}
		return null;
	}
	
	private void initUserRz() {
		GlobalPara.outUserRz = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/queryUserInfo", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					JSONObject objRes = obj.getJSONObject("result");
					JSONObject objRetRes = objRes.getJSONObject("returnResult");
					GlobalPara.outUserRz = FastJsonUtils.getSingleBean(objRetRes.toString(), OutUserRz.class);
					if(UserUtil.judgeAuthentication()){
						
						initCardBanck();//银行卡
						
						if(GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getAppCount() != null && GlobalPara.getOutUserRz().getAppCount() == 1){
							//已上传app列表
						} else {
							upAPPName();//上传app列表
						}
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	private void initCardBanck() {
		GlobalPara.outUserBank = null;
		
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/queryCard", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					JSONObject objRes = obj.getJSONObject("result");
					GlobalPara.outUserBank = FastJsonUtils.getSingleBean(objRes.toString(), OutUserBank.class);
				}
			}
		} catch (Exception e) {
		}
	}
	
	private String makeJsonPost() {
		OutContact outContact = new OutContact();
		outContact.setAppList(GlobalPara.getAppNameList());
		return FastJsonUtils.getJsonString(outContact);
	}

	private void upAPPName() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userContactJson", makeJsonPost());
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,"usercredit/postUserInstallApp", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					GlobalPara.getOutUserRz().setAppCount(1);
				}
			}
		} catch (Exception e) {
		}
	}

	private void doGoto() {
		runOnUiThread(new Runnable() {
			public void run() {
//				ViewUtil.backHomeActivity(LoginActivity.this);
				LoginActivity.this.finish();
			}
		});
	}

	/** pop */
	private View pop;
	private PopupWindow popWin;
	/** 初始化Pop */
	private void initPop() {
		pop = LayoutInflater.from(LoginActivity.this).inflate(R.layout.view_pop_captcha_show, null);
		popWin = new PopupWindow(pop, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		popWin.setFocusable(true);
	}

	private void showCaptcha(){
		final Captcha captcha = (Captcha)pop.findViewById(R.id.captCha);
		ImageView ivCancel = (ImageView)pop.findViewById(R.id.ivCancel);
		captcha.setSeekBarStyle(R.drawable.po_seekbar,R.drawable.thumb_bg);

		new Thread(new Runnable() {
			@Override
			public void run() {
				getCaptcha(captcha);
			}
		}).start();

		captcha.setCaptchaListener(new Captcha.CaptchaListener() {
			@Override
			public String onAccess(long time) {
//				Toast.makeText(LoginActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
				double value = Long.valueOf(time).doubleValue()/ 1000;

//				if(flag != 2){
//					popWin.dismiss();//点击pop外围消失
//				}

				try {
					encryTop = ThreeDES.encryptDESCBC(top);
					encrtLeft = ThreeDES.encryptDESCBC(LoginActivity.getLeft());

				} catch (Exception e) {
					e.printStackTrace();
				}
				initLogin();

				return String.format("耗时%1$.1f秒", value);

			}

			@Override
			public String onFailed(int count) {
				Toast.makeText(LoginActivity.this, "验证失败,失败次数" + count, Toast.LENGTH_SHORT).show();
				return "验证失败";
			}

			@Override
			public String onMaxFailed() {
				Toast.makeText(LoginActivity.this, "验证超过次数，你的帐号被封锁", Toast.LENGTH_SHORT).show();
				return "可以走了";
			}
		});

		ivCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				doGoto();
				popWin.dismiss();//点击pop外围消失
			}
		});

		Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
			@Override
			public boolean queueIdle() {
				popWin.showAtLocation(pop,Gravity.CENTER, 0, 0);
				return false;
			}
		});
	}

	private  void getCaptcha(final Captcha captcha) {
		try {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						captcha.setBitmap(LoginActivity.getPicPathBigImage(), LoginActivity.getPicPathBlockImage(), Integer.valueOf(top));
					}
				});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Bitmap decodeImage(String bmMsg){
		byte [] input = Base64.decode(bmMsg, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(input, 0, input.length);
		return  bitmap;
	}

	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(LoginActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
