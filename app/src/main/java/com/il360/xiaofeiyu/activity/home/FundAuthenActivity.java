package com.il360.xiaofeiyu.activity.home;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.home.PhoneInfo;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.CustomDialog;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

@EActivity(R.layout.act_phone_authen)
public class FundAuthenActivity extends BaseWidgetActivity {
	
	@Extra String fund,fundPwd,forceRemove,cityCode;
	
	private String picCode, smsCode;
	
	private int log = 0;//1只有短信验证码；2只有图片验证码；3都有
	private String picUrl = null;//记录上次图片验证码URL
	private String picCodeString = null;//记录上次验证码图片
	
	protected ProgressDialog transDialog;
	
	private PhoneInfo phoneInfo;
	
	
	/** 倒计时器 */
	private Timer timer = new Timer();
	private static final int TIME_DOWN_COUNT = 30;//倒计时30秒
	private int time = 0;//倒计时计时数,初始为0
	
	@AfterViews
	void init(){
		picCode = "";
		smsCode = "";
		initData();
		initTimeDown();
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
			if (msg.what == 1) {
				if (!FundAuthenActivity.this.isFinishing()) {
					if (phoneInfo == null || (phoneInfo != null && phoneInfo.getCode() != 1)) {
						showDialog4("您现在可以返回浏览其他页面也可以继续等待结果");
					}
				}
			}
		};
	};

	private void initData() {
		transDialog = ProgressDialog.show(FundAuthenActivity.this, null, "认证中...", true);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("gjjAccount", fund);
					params.put("gjjPwd", fundPwd);
					params.put("gjjCityCode", cityCode);
					params.put("forceRemove", forceRemove);
					params.put("picCode", picCode);
					params.put("smsCode", smsCode);
					params.put("platform", "2");
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest3(UrlEnum.BIZ_URL,
							"credit/getGJJInfo", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							phoneInfo = null;
							JSONObject objRes = obj.getJSONObject("result");
							phoneInfo = FastJsonUtils.getSingleBean(objRes.toString(), PhoneInfo.class);
							if (!FundAuthenActivity.this.isFinishing()) {
								editCode();
							} else {
								GlobalPara.canResubmitFund = true;
								Log.i("FundAuthenActivity", phoneInfo.toString());
							}
						} else {
							showDialog(obj.getString("desc"));
						}
					} else {
						showDialog(getString(R.string.A6));
					}
				} catch (Exception e) {
					Log.e("FundAuthenActivity", "initData()", e);
					LogUmeng.reportError(FundAuthenActivity.this, e);
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
		if (phoneInfo.getCode() == 2002) {// 短信
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
		}
		else if (phoneInfo.getCode() == 2011) {// 短信+图片
			log = 3;
			picUrl = phoneInfo.getObject();
			picCodeString = phoneInfo.getPicCode();
			showDialog3(phoneInfo.getDesc());
		}  else if (phoneInfo.getCode() == 1003) {// 提示特殊处理
			showDialog("认证结果：等待评估(" + phoneInfo.getCode() + ")");
		} else {
			showDialog("认证结果：" + phoneInfo.getDesc() + "(" + phoneInfo.getCode() + ")");
		}
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(FundAuthenActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void showDialog(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				CustomDialog.Builder builder = new CustomDialog.Builder(FundAuthenActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage(message);
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (transDialog != null && transDialog.isShowing()) {
							transDialog.dismiss();
						}
						setResult(101);
						FundAuthenActivity.this.finish();
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
				final CustomDialog.Builder builder = new CustomDialog.Builder(FundAuthenActivity.this);
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
							forceRemove = "false";
							initData();
							isLast();
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
						FundAuthenActivity.this.finish();
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
				final CustomDialog.Builder builder = new CustomDialog.Builder(FundAuthenActivity.this);
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
							forceRemove = "false";
							initData();
							isLast();
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
						FundAuthenActivity.this.finish();
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
				final CustomDialog.Builder builder = new CustomDialog.Builder(FundAuthenActivity.this);
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
							forceRemove = "false";
							initData();
							isLast();
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
						FundAuthenActivity.this.finish();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	private void showDialog4(String message) {//认证提示
		CustomDialog.Builder builder = new CustomDialog.Builder(FundAuthenActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(message);
		builder.setPositiveButton("继续等待", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				GlobalPara.canResubmitFund = true;
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("现在返回", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				
				setResult(102);
				FundAuthenActivity.this.finish();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	
	private void isLast() {
		if(phoneInfo != null && phoneInfo.getIsLastStep() != null 
				&& phoneInfo.getIsLastStep().endsWith("true")){            //最后一步
			time = TIME_DOWN_COUNT;
			
			GlobalPara.canResubmitFund = false;
			
			//设置参数
			setAuthenFund(getAuthenFund());
		}
	}
	
	public void setAuthenFund(String authenFund) {
		try {
			SharedPreferences sp = FundAuthenActivity.this.getSharedPreferences("SP", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("authen_fund", authenFund + ";" + fund);
			editor.commit();
		} catch (Exception e) {
			Log.e("FundAuthenActivity", "setAuthenFund", e);
		}
	}
	
	public String getAuthenFund() {
		String authenFund = null;
		try {
			SharedPreferences sp = FundAuthenActivity.this.getSharedPreferences("SP", MODE_PRIVATE);
			authenFund = sp.getString("authen_fund", "");
			return authenFund;
		} catch (Exception e) {
			Log.e("FundAuthenActivity", "getAuthenFund", e);
		}
		return authenFund;
	}
	
}
