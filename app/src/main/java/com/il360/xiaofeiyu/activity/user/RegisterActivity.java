package com.il360.xiaofeiyu.activity.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;


import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.main.UrlToWebActivity_;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.user.User;
import com.il360.xiaofeiyu.model.user.UserCommonMessage;
import com.il360.xiaofeiyu.util.AESEncryptor;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ThreeDES;
import com.il360.xiaofeiyu.util.ViewUtil;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_register)
public class RegisterActivity extends BaseWidgetActivity {

	private Context mContext = this;
	/** 注册账号 **/
	@ViewById
	EditText etAccount;
	/** 注册密码 **/
	@ViewById
	EditText etPassword;
	/** 确认密码 **/
	@ViewById
	EditText etPassword2;
	/** 手机验证码 **/
	@ViewById
	EditText etCode;
	/** 获取验证码 **/
	@ViewById
	TextView btnGetCode;
	/** 注册 **/
	@ViewById
	Button btnRegister;
	Pattern pt = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
	
//	@ViewById
//	EditText etVerificaImage;
//	@ViewById
//	ImageView ivImage;
//	
//    private CodeUtils codeUtils;
//    private String codeStr;//输入的验证码

	@ViewById
	TextView tvHotline;
	
	@ViewById
	CheckBox cbAgree;
	@ViewById
	TextView tvRule;
	@ViewById
	TextView tvSubmit;

	protected ProgressDialog transDialog;

	/** 倒计时器 */
	private Timer timer = new Timer();
	private static final int TIME_DOWN_COUNT = 60;// 倒计时60秒
	private int time = 0;// 倒计时计时数,初始为0

	@AfterViews
	void init() {
		
		tvRule.setText("《" + RegisterActivity.this.getResources().getString(R.string.register_rule) + "》");
		
		if (GlobalPara.getTelephone() != null) {
			tvHotline.setText(GlobalPara.getTelephone());
		}
		tvHotline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		tvHotline.getPaint().setAntiAlias(true);// 去锯齿
		initTimeDown();
		//图片验证码
		 
//		getImage();
	}
	
//	private void getImage(){
//        codeUtils = CodeUtils.getInstance();  
//        Bitmap bitmap = codeUtils.createBitmap();  
//        ivImage.setImageBitmap(bitmap);
//	}

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
				btnGetCode.setText(msg.what + "秒后重新获取");
				btnGetCode.setEnabled(false);
			} else {
				if (!ViewUtil.getText(etAccount).equals("")) {
					if (pt.matcher(ViewUtil.getText(etAccount)).matches()) {
						btnGetCode.setText("获取验证码");
						btnGetCode.setEnabled(true);
					} else {
						btnGetCode.setEnabled(false);
					}
				} else {
					btnGetCode.setEnabled(false);
				}
			}
		};
	};

	@AfterTextChange
	void etAccount() {
		validateRequired();
		validateAccount();
	}

	@AfterTextChange
	void etCode() {
		validateRequired();
	}

	@AfterTextChange
	void etPassword() {
		validateRequired();
	}

	@AfterTextChange
	void etPassword2() {
		validateRequired();
	}
	
	@AfterTextChange
	void etVerificaImage() {
		validateRequired();
	}
	
	@Click
	void cbAgree(){
		validateRequired();
	}
	
	@Click
	void tvRule() {
		Intent intent = new Intent(RegisterActivity.this, UrlToWebActivity_.class);
		intent.putExtra("title", RegisterActivity.this.getResources().getString(R.string.register_rule));
		intent.putExtra("supportZoom", false);
		intent.putExtra("url", "http://www.qianjb.com/xfy/service.html");
		startActivity(intent);
	}
	
//	@Click
//	void ivImage() {
//		getImage();
//	}

	@Click
	void btnGetCode() {
		etAccount.setEnabled(false);
		btnGetCode.setClickable(false);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("phone", etAccount.getText().toString());
					params.put("verifyType", "1");// 1注册手机验证码
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
							Log.e("RegisterActivity", "btnGetCode", e);
							LogUmeng.reportError(RegisterActivity.this, e);
						}
					} else {
						showInfo(getResources().getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				} finally {
					if (!btnGetCode.isClickable()) {
						btnGetCode.setClickable(true);
					}
				}
			}
		});
	}

	private void initDataVerificationCode() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("verifyType", "1");// 1注册手机验证码
					params.put("phone", etAccount.getText().toString());
					params.put("verifyCode", etCode.getText().toString());
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"user/validUserVerifyCode", params);
					if (result.getSuccess()) {
						try {
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								JSONObject objRes = obj.getJSONObject("result");
								if (objRes.getInt("returnCode") == 1) {
									initRegister();
								} else {
									showInfo(objRes.getString("returnMessage"));
								}
							} else {
								showInfo(obj.getString("result"));
							}
						} catch (JSONException e) {
							Log.e("RegisterActivity", "initDataVerificationCode", e);
							LogUmeng.reportError(RegisterActivity.this, e);
						}
					} else {
						showInfo(getResources().getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
						}
					});
				}
			}
		});
	}

	@Click
	void btnRegister() {
//		String code = codeUtils.getCode();
		if (validateConfirm()) {
//			if(!code.equalsIgnoreCase(codeStr)){
//				showInfo("图片验证错误");
//				etVerificaImage.setText("");
//				getImage();
//			} else {
				transDialog = ProgressDialog.show(RegisterActivity.this, null, "请稍等...", true);
				initDataVerificationCode();
//			}
		}
	}

	private void initRegister() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userJson", makeJsonPost());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "user/registerUser",
					params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					JSONObject objRes = obj.getJSONObject("result");
					UserCommonMessage response = FastJsonUtils.getSingleBean(objRes.toString(),
							UserCommonMessage.class);
					if (response.getReturnCode() == 1) {
						showInfo(getResources().getString(R.string.reg_suceess));
						RegisterActivity.this.finish();
					} else {
						showInfo(response.getReturnMessage());
					}
				} else {
					showInfo(obj.getString("desc"));
				}
			} else {
				showInfo(result.getResult());
			}
		} catch (Exception e) {
			showInfo(getResources().getString(R.string.A2));
		}

	}

	/**
	 * 验证必要信息是否输入 此方法主要为了用户体验
	 * 
	 * @return true:通过； false:不通过
	 */
	private void validateRequired() {
//		codeStr = etVerificaImage.getText().toString().trim();
		if (!ViewUtil.getText(etAccount).equals("") && !ViewUtil.getText(etPassword).equals("")
				&& !ViewUtil.getText(etPassword2).equals("")) {
			if (pt.matcher(ViewUtil.getText(etAccount)).matches() && ViewUtil.getText(etAccount).length() == 11
					&& ViewUtil.getText(etCode).length() > 3 && ViewUtil.getText(etPassword).length() > 5
					&& cbAgree.isChecked()) {
					btnRegister.setEnabled(true);
			} else {
				btnRegister.setEnabled(false);
			}
		} else {
			btnRegister.setEnabled(false);
		}
	}

	private void validateAccount() {
		if (!ViewUtil.getText(etAccount).equals("")) {
			if (pt.matcher(ViewUtil.getText(etAccount)).matches()) {
				btnGetCode.setEnabled(true);
			} else {
				btnGetCode.setEnabled(false);
			}
		} else {
			btnGetCode.setEnabled(false);
		}
	}

	/**
	 * 验证密码是否一致，可选信息如果填写，格式是否正确
	 * 
	 * @return boolean true:通过; false:不通过
	 */
	private boolean validateConfirm() {
		if (!pt.matcher(ViewUtil.getText(etAccount)).matches()) {
			showInfo(getString(R.string.account_notice));
		} else if (ViewUtil.getText(etPassword).length() < 6) {
			showInfo("密码至少6位");
		} else if (!ViewUtil.getText(etPassword).equals(ViewUtil.getText(etPassword2))) {
			showInfo(getString(R.string.password_different));
		} else if(!cbAgree.isChecked()){
			showInfo("请先阅读并同意《" + RegisterActivity.this.getResources().getString(R.string.register_rule) + "》");
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 生成注册POST信息
	 * 
	 * @return String userJson
	 */
	private String makeJsonPost() {
		User user = new User();
		user.setLoginName(ViewUtil.getText(etAccount));
		user.setLastTag("2");
		try {
			user.setLoginPwd(AESEncryptor.encrypt(ThreeDES.encryptDESCBC(ViewUtil.getText(etPassword))));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return FastJsonUtils.getJsonString(user);
	}

	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(RegisterActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Click
	void tvHotline() {
		if(!TextUtils.isEmpty(ViewUtil.getText(tvHotline))){
			Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + tvHotline.getText()));
			startActivity(intent);
		}
	}
}
