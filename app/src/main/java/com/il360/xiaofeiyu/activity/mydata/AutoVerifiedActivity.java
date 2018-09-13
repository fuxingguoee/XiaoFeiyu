package com.il360.xiaofeiyu.activity.mydata;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.authreal.api.AuthBuilder;
import com.authreal.api.OnResultListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.activity.user.MyInfoActivity_;
import com.il360.xiaofeiyu.activity.user.VerifiedActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.common.URLFactory;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.home.ArrayOfSwitch;
import com.il360.xiaofeiyu.model.home.AutoVerified;
import com.il360.xiaofeiyu.model.home.RemainTimes;
import com.il360.xiaofeiyu.model.user.OutUserRz;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;
import com.il360.xiaofeiyu.view.CustomDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
@EActivity(R.layout.act_auto_verified)
public class AutoVerifiedActivity extends BaseWidgetActivity {
	
	@ViewById
	PullToRefreshScrollView pull_refresh_scrollview;
	
	/**
	 * 标题右侧文字
	 */
	@ViewById TextView tvTextClick;
	@ViewById ImageView ivAutoVerified;
	
	@ViewById TextView tvTimes;
	
    //商户pub_key ： 开户时通过邮件发送给商户
    private String authKey = "ece7d9d5-7cda-4cda-85ce-1bc4cc6528cf";
    //异步通知接口地址(非必传)
    private String urlNotify = URLFactory.DIANXIN_URL + "fc/notifyResult.html";
    
	protected ProgressDialog transDialog;
	
	private String orderId = "";
	
	@AfterViews
	void init() {
		
		tvTextClick.setText("人工认证");
		initPull();
		initData();
	}
	
	private void initAutoTimes(){
		runOnUiThread(new Runnable() {
			public void run() {
				tvTimes.setText(Html.fromHtml("<font color='black'>"+"每人最多能认证"+"</font>"
			+"<font color='red'>"+ GlobalPara.getMaxTimes() +"</font>" + "<font color='black'>"+"次，您还有"+"</font>"
			+"<font color='red'>"+ GlobalPara.getRemainTimes() +"</font>" + "<font color='black'>"+"次认证机会"+"</font>"));
			}
		});
	}

	private void initPull() {
		pull_refresh_scrollview.setMode(Mode.PULL_FROM_START);
		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				initData();
			}
		});
	}
	
	private void initData() {
		transDialog = ProgressDialog.show(AutoVerifiedActivity.this, null, "努力加载中...", true, false);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					initUserRz();
					initSwitch();
				}catch (Exception e) {
					Log.e("AutoVerifiedActivity", "initData", e);
					showInfo(getResources().getString(R.string.A2));
					LogUmeng.reportError(AutoVerifiedActivity.this, e);
				}finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
							
							pull_refresh_scrollview.onRefreshComplete();
						}
					});
				}
			}
		});
	}

	protected void initUserRz() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
					"card/queryUserInfo", params);
			if (result.getSuccess()) {
				if (ResultUtil.isOutTime(result.getResult()) != null) {
					showInfo(ResultUtil.isOutTime(result.getResult()));
					Intent intent = new Intent(AutoVerifiedActivity.this, LoginActivity_.class);
					startActivity(intent);
				} else {
					GlobalPara.outUserRz = null;
					JSONObject obj = new JSONObject(result.getResult());
					if (obj.getInt("code") == 1) {
						JSONObject objRes = obj.getJSONObject("result");
						JSONObject objRetRes = objRes.getJSONObject("returnResult");
						GlobalPara.outUserRz = FastJsonUtils.getSingleBean(objRetRes.toString(), OutUserRz.class);
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
	private void initSwitch() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "switch/queryAll", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				ArrayOfSwitch arrayOfSwitch = FastJsonUtils.getSingleBean(obj.toString(), ArrayOfSwitch.class);
				if(arrayOfSwitch.getCode() == 1){
					GlobalPara.mySwitchList = null;
					GlobalPara.mySwitchList = arrayOfSwitch.getSwitchConfigs();
					
					if(GlobalPara.getCanAutoVerified()){
						initTimes();
					}
				}
			}
		} catch (Exception e) {
		}finally {
			initAutoTimes();
		}
	}
	
	private void initTimes(){
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "fc/queryRemainTimes", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				final RemainTimes remainTimes = FastJsonUtils.getSingleBean(obj.toString(), RemainTimes.class);
				if(remainTimes.getCode() != null && remainTimes.getCode() == 1){
					GlobalPara.remainTimes = 0;
					GlobalPara.maxTimes = 0;
					GlobalPara.remainTimes = remainTimes.getRmTimes();
					GlobalPara.maxTimes = remainTimes.getMaxTimes();
				}
			}
		} catch (Exception e) {
		}
	}
	
	@Click
	void ivAutoVerified() {

		if (isOK()) {
			orderId = UserUtil.getUserInfo().getUserId() + "_" + new Date().getTime();// 订单号
			AuthBuilder mAuthBuilder = new AuthBuilder(orderId, authKey, urlNotify, new OnResultListener() {
				@Override
				public void onResult(String s) {
					try {
						AutoVerified autoVerified = FastJsonUtils.getSingleBean(s, AutoVerified.class);
						if (autoVerified != null && autoVerified.getRet_code()!= null && autoVerified.getRet_code().equals("000000")) {

							initAutoVerified(autoVerified);
						} else {
							showInfo((autoVerified != null && autoVerified.getRet_msg() != null) ? autoVerified.getRet_msg() : "认证数据获取失败!");
						}
						// Log.d("活体认证","result " + s);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			// 下文调用方法做为范例，请以对接文档中的调用方法为准
			mAuthBuilder.faceAuth(AutoVerifiedActivity.this);
		}
	}

	@Click
	void tvTextClick(){
		if (UserUtil.judgeAuthentication()) {
			Intent intent = new Intent(AutoVerifiedActivity.this, MyInfoActivity_.class);
			startActivity(intent);
		} else if(GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getNameRz() != null 
				&& GlobalPara.getOutUserRz().getNameRz() == 3){
			showDialog(AutoVerifiedActivity.this.getResources().getString(R.string.black_list_show));
		} else {
			Intent intent = new Intent(AutoVerifiedActivity.this, VerifiedActivity_.class);
			startActivity(intent);
			AutoVerifiedActivity.this.finish();
		}
	}


	private boolean isOK() {
		if(UserUtil.judgeAuthentication()){
			Intent intent = new Intent(AutoVerifiedActivity.this, MyInfoActivity_.class);
			startActivity(intent);
		} else if(GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getNameRz() != null 
				&& GlobalPara.getOutUserRz().getNameRz() == 2){
			showDialog("正在审核中，请耐心等待！");
		} else if(GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getNameRz() != null 
				&& GlobalPara.getOutUserRz().getNameRz() == 3){
			showDialog(AutoVerifiedActivity.this.getResources().getString(R.string.black_list_show));
		} else if(!GlobalPara.getCanAutoVerified() || GlobalPara.getRemainTimes() == 0){
			showDialog("您已无自动认证次数了，请转人工认证！");
		} else {
			return true;
		}
		return false;
	}

	protected void initAutoVerified(final AutoVerified autoVerified) {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("jsonStr", makeJson(autoVerified));
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"fc/faceRecognition", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(AutoVerifiedActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							JSONObject obj = new JSONObject(result.getResult());
							RemainTimes remainTimes = FastJsonUtils.getSingleBean(obj.toString(), RemainTimes.class);
//							if (remainTimes.getRmTimes() != null) {
//								GlobalPara.remainTimes = remainTimes.getRmTimes();
//							}
							if (remainTimes.getCode() != null && remainTimes.getCode() == 1) {
								if(autoVerified.getResult_auth() != null && autoVerified.getResult_auth().equals("T")){
									showInfo("认证成功！");
									runOnUiThread(new Runnable() {
										public void run() {
											ViewUtil.backHomeActivity(AutoVerifiedActivity.this);
											AutoVerifiedActivity.this.finish();
										}
									});
								} else {
									if(GlobalPara.getRemainTimes() > 0){
										GlobalPara.remainTimes = GlobalPara.getRemainTimes() - 1;
									} else {
										GlobalPara.remainTimes = 0;
									}
									initAutoTimes();
									showInfo("认证失败！");
								}
							} else {
								showInfo(remainTimes.getDesc());
							}
						}
					}
				} catch (Exception e) {
					Log.e("AutoVerifiedActivity", "initAutoVerified", e);
					LogUmeng.reportError(AutoVerifiedActivity.this, e);
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
	
	private String makeJson(AutoVerified autoVerified) {
		JSONObject json = new JSONObject();
		try {
			json.put("orderId", orderId);
			json.put("token", UserUtil.getToken());
			json.put("authResult", autoVerified.getResult_auth());
			json.put("name", autoVerified.getId_name());
			json.put("sex", autoVerified.getFlag_sex());
			json.put("birth", autoVerified.getDate_birthday());
			json.put("idNo", autoVerified.getId_no());
			json.put("idAddress", autoVerified.getAddr_card());
			json.put("frontcardUrl", autoVerified.getUrl_frontcard());
			json.put("backcardUrl", autoVerified.getUrl_backcard());
			json.put("photogetUrl", autoVerified.getUrl_photoget());
			return json.toString();
		} catch (Exception e) {
			Log.e("AutoVerifiedActivity", "makeJson", e);
		}
		return null;
	}
	
	private void showDialog(String message) {
		CustomDialog.Builder builder = new CustomDialog.Builder(AutoVerifiedActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(AutoVerifiedActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
