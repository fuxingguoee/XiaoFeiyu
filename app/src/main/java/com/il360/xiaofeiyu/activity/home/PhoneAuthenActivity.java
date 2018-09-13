package com.il360.xiaofeiyu.activity.home;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.home.PhoneInfo;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.CustomDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

@EActivity(R.layout.act_phone_authen)
public class PhoneAuthenActivity extends BaseWidgetActivity {
	
	@Extra String serviceNumber,forceRemove;
	
	private String picCode, smsCode;
	
	private int log = 0;//1只有短信验证码；2只有图片验证码；3都有
	private String picUrl = null;//记录上次图片验证码URL
	private String picCodeString = null;//记录上次验证码图片
	
	protected ProgressDialog transDialog;
	
	private PhoneInfo phoneInfo;

	
	@AfterViews
	void init(){
		picCode = "";
		smsCode = "";
		initData();
	}

	private void initData() {
		transDialog = ProgressDialog.show(PhoneAuthenActivity.this, null, "认证中...", true);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("serviceNumber", serviceNumber);
					params.put("forceRemove", forceRemove);
					params.put("picCode", picCode);
					params.put("smsCode", smsCode);
					params.put("catchPhase", "");
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest3(UrlEnum.BIZ_URL,"card/initiategrab", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(PhoneAuthenActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							JSONObject obj = new JSONObject(result.getResult());
							phoneInfo = null;
							phoneInfo = FastJsonUtils.getSingleBean(obj.toString(), PhoneInfo.class);
							if (!PhoneAuthenActivity.this.isFinishing()) {
								editCode();
							} else {
								GlobalPara.canResubmitPhone = true;
								Log.i("PhoneAuthenActivity", phoneInfo.toString());
							}
						}
					} else {
						showDialog(getString(R.string.A6));
					}
				} catch (Exception e) {
					Log.e("PhoneAuthenActivity", "initData()", e);
					showDialog(getString(R.string.A2));
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
	
	private void editCode(){
		if (phoneInfo.getCode() == 2000 || phoneInfo.getCode() == 2002 || phoneInfo.getCode() == 2017) {// 短信
			log = 1;
			picUrl = null;
			picCodeString= null;
			showDialog1(phoneInfo.getDesc());
		} else if (phoneInfo.getCode() == 2001 || phoneInfo.getCode() == 2003) {// 图片
			log = 2;
			picUrl = phoneInfo.getObject();
			picCodeString = phoneInfo.getPicCode();
			showDialog2(phoneInfo.getDesc());
		} else if (phoneInfo.getCode() == 2004 ||phoneInfo.getCode() == 2005 
				||phoneInfo.getCode() == 2006 || phoneInfo.getCode() == 2016) {// 未知
			if (log == 1) {// 短信
				showDialog1(phoneInfo.getDesc());
			} else if (log == 2) {// 图片
				showDialog2(phoneInfo.getDesc());
			} else if (log == 3) {// 短信+图片
				showDialog3(phoneInfo.getDesc());
			}

		} else if (phoneInfo.getCode() == 2011) {// 短信+图片								
			log = 3;
			picUrl = phoneInfo.getObject();
			picCodeString = phoneInfo.getPicCode();
			showDialog3(phoneInfo.getDesc());
		} else if (phoneInfo.getCode() == 1003) {// 提示特殊处理
			showDialog("认证结果：等待评估(" + phoneInfo.getCode() + ")");
		} else if (phoneInfo.getCode() == 2) {//提交成功
			showDialog4(phoneInfo.getDesc());
		} else {
			showDialog("认证结果：" + phoneInfo.getDesc() + "(" + phoneInfo.getCode() + ")");
		}
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(PhoneAuthenActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void showDialog(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				CustomDialog.Builder builder = new CustomDialog.Builder(PhoneAuthenActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage(message);
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (transDialog != null && transDialog.isShowing()) {
							transDialog.dismiss();
						}
						setResult(101);
						PhoneAuthenActivity.this.finish();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	private void showDialog1(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				final CustomDialog.Builder builder = new CustomDialog.Builder(PhoneAuthenActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage(message);
				builder.setEtMessageHint1("请输入短信验证码");
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						smsCode = builder.getEtMessage1();
						if (smsCode != null && smsCode.length() > 0) {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
							picCode = "";
							forceRemove = "0";
							initData();
							dialog.dismiss();
						} else {
							showInfo("输入的验证码不能为空");
						}
					}
				});
				
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (transDialog != null && transDialog.isShowing()) {
							transDialog.dismiss();
						}
						PhoneAuthenActivity.this.finish();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	private void showDialog2(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				final CustomDialog.Builder builder = new CustomDialog.Builder(PhoneAuthenActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage(message);
				if(!TextUtils.isEmpty(picCodeString)){
					builder.setPicCode(picCodeString);
				}
				builder.setEtMessageHint("请输入图片验证码");
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						picCode = builder.getEtMessage();
						if (picCode != null && picCode.length() > 0) {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
							smsCode = "";
							forceRemove = "0";
							initData();
							dialog.dismiss();
						} else {
							showInfo("输入的验证码不能为空");
						}
					}
				});
				
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (transDialog != null && transDialog.isShowing()) {
							transDialog.dismiss();
						}
						PhoneAuthenActivity.this.finish();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}

	private void showDialog3(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				final CustomDialog.Builder builder = new CustomDialog.Builder(PhoneAuthenActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage(message);
				builder.setEtMessageHint1("请输入短信验证码");
				if(!TextUtils.isEmpty(picCodeString)){
					builder.setPicCode(picCodeString);
				}
				builder.setEtMessageHint("请输入图片验证码");
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						smsCode = builder.getEtMessage1();
						picCode = builder.getEtMessage();
						if (smsCode != null && smsCode.length() > 0 && picCode != null && picCode.length() > 0) {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
							forceRemove = "0";
							initData();
							dialog.dismiss();
						} else {
							showInfo("输入的验证码不能为空");
						}
					}
				});
				
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (transDialog != null && transDialog.isShowing()) {
							transDialog.dismiss();
						}
						PhoneAuthenActivity.this.finish();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	private void showDialog4(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				CustomDialog.Builder builder = new CustomDialog.Builder(PhoneAuthenActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage(message);
				builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (transDialog != null && transDialog.isShowing()) {
							transDialog.dismiss();
						}

						setResult(102);
						PhoneAuthenActivity.this.finish();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	

	
//	public void setAuthenPhone(String authenPhone) {
//		try {
//			SharedPreferences sp = PhoneAuthenActivity.this.getSharedPreferences("SP", MODE_PRIVATE);
//			Editor editor = sp.edit();
//			editor.putString("hua_authen_phone", authenPhone + ";" + UserUtil.getUserInfo().getLoginName());
//			editor.commit();
//		} catch (Exception e) {
//			Log.e("PhoneAuthenActivity", "setAuthenPhone", e);
//		}
//	}
//
//	public String getAuthenPhone() {
//		String authenPhone = null;
//		try {
//			SharedPreferences sp = PhoneAuthenActivity.this.getSharedPreferences("SP", MODE_PRIVATE);
//			authenPhone = sp.getString("hua_authen_phone", "");
//			return authenPhone;
//		} catch (Exception e) {
//			Log.e("PhoneAuthenActivity", "getAuthenPhone", e);
//		}
//		return authenPhone;
//	}
	
}
