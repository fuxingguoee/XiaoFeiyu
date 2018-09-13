package com.il360.xiaofeiyu.fragment;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.WelcomeActivity;
import com.il360.xiaofeiyu.activity.mydata.AutoVerifiedActivity_;
import com.il360.xiaofeiyu.activity.order.RecordActivity_;
import com.il360.xiaofeiyu.activity.user.AboutUsActivity_;
import com.il360.xiaofeiyu.activity.user.AccountPictureActivity_;
import com.il360.xiaofeiyu.activity.user.FeedbackActivity_;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.activity.user.ManageActivity_;
import com.il360.xiaofeiyu.activity.user.MyInfoActivity_;
import com.il360.xiaofeiyu.activity.user.VerifiedActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.fragment.MyFragment;
import com.il360.xiaofeiyu.model.home.ArrayOfSwitch;
import com.il360.xiaofeiyu.model.home.OutContact;
import com.il360.xiaofeiyu.model.home.RemainTimes;
import com.il360.xiaofeiyu.model.user.OutUserRz;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.FileUtil;
import com.il360.xiaofeiyu.util.PicUtil;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.SDCardUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;
import com.il360.xiaofeiyu.view.CircleImageView;
import com.il360.xiaofeiyu.view.CustomDialog1;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.GetObjectRequest;
import com.tencent.cos.task.listener.IDownloadTaskListener;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
@EFragment(R.layout.fra_user)
public class UserFragment extends MyFragment {
	
	@ViewById PullToRefreshScrollView pull_refresh_scrollview;
	
	@ViewById ImageView iv_share;
	@ViewById ImageView iv_setting;

	@ViewById RelativeLayout rlUserInfo;//已登录
	@ViewById
	CircleImageView userImage;
	@ViewById TextView userName;
	@ViewById TextView loginName;
	@ViewById ImageView isVerified;
	
	@ViewById RelativeLayout rlNoLogin;//未登录
	@ViewById CircleImageView noLoginImage;
	@ViewById TextView tvLogin;
	
//	@ViewById RelativeLayout rlOrder;
	@ViewById RelativeLayout rlServiceNum;//客服电话
	@ViewById RelativeLayout rlAccount;//账户管理
	@ViewById RelativeLayout rlClear;//清理缓存
	@ViewById TextView tvEditClear;
	@ViewById TextView tvEditServiceNum;
	@ViewById RelativeLayout rlAboutUs;//关于我们
	@ViewById RelativeLayout rlFeedback;//用户反馈
	@ViewById RelativeLayout rlRecord;//支付记录列表
	
	@ViewById RelativeLayout rlQQNum1;
	@ViewById TextView tvEditQQNum1;
	
	@ViewById TextView tvLoginOut;//退出登录
	
	public static final int CODE_SECCESS = 1011;// 成功
	public static final int CODE_NEED_BACK = 1012;// 需要返回结果
	
	final File file = new File(Variables.APP_CACHE_SDPATH);
	
	DecimalFormat df = new DecimalFormat("0.00");
	
	protected ProgressDialog transDialog;
	
	private Handler handler = null;
	
	private String picPath = "";
	
	@Override
	public void onResume() {
		super.onResume();
		init();
	}
	
	@AfterViews
	void initViews(){
		
		//创建属于主线程的handler  
		handler = new Handler();
		
	}
	
	
	private void initPull() {
		pull_refresh_scrollview.setMode(Mode.PULL_FROM_START);
		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (UserUtil.judgeUserInfo()) {
					initSwitch();
					initUserRz();
				} else {
					try {
						Thread.sleep(1000);
						pull_refresh_scrollview.onRefreshComplete();
					} catch (Exception e) {
					}
				}

			}
		});
	}
	
	protected void initUserRz() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"card/queryUserInfo", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(getActivity(), LoginActivity_.class);
							startActivity(intent);
						} else {
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								GlobalPara.outUserRz = null;
								JSONObject objRes = obj.getJSONObject("result");
								JSONObject objRetRes = objRes.getJSONObject("returnResult");
								GlobalPara.outUserRz = FastJsonUtils.getSingleBean(objRetRes.toString(), OutUserRz.class);
								if(UserUtil.judgeAuthentication()){
									if(GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getAppCount() != null && GlobalPara.getOutUserRz().getAppCount() == 1){
										//已上传app列表
									} else {
										upAPPName();//上传app列表
									}
								}
							}
						}
					}
				} catch (Exception e) {
					Log.e("UserFragment", "initUserReg", e);
					LogUmeng.reportError(getActivity(), e);
				}finally {
					FragmentActivity fragAct = getActivity();
					if (fragAct != null) {
						fragAct.runOnUiThread(new Runnable() {
							public void run() {
								init();
								pull_refresh_scrollview.onRefreshComplete();
							}
						});
					}
				}
			}
		});

	}
	
	private void initSwitch() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"switch/queryAll", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						ArrayOfSwitch arrayOfSwitch = FastJsonUtils.getSingleBean(obj.toString(), ArrayOfSwitch.class);
						if (arrayOfSwitch.getCode() == 1) {
							GlobalPara.mySwitchList = null;
							GlobalPara.mySwitchList = arrayOfSwitch.getSwitchConfigs();

							if (!UserUtil.judgeAuthentication() && GlobalPara.getCanAutoVerified()) {
								initTimes();
							}
						}
					}
				} catch (Exception e) {
				}
			}
		});
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
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
					"usercredit/postUserInstallApp", params);
			if (result.getSuccess()) {
					JSONObject obj = new JSONObject(result.getResult());
					if (obj.getInt("code") == 1) {
						GlobalPara.getOutUserRz().setAppCount(1);
				}
			}
		} catch (Exception e) {
		}
	}


	void init() {
		if (UserUtil.judgeUserInfo()) { // 是否登录
			initUserHead();
			initPull();
			tvLoginOut.setVisibility(View.VISIBLE);
			rlUserInfo.setVisibility(View.VISIBLE);
			rlNoLogin.setVisibility(View.GONE);
			if (UserUtil.judgeAuthentication()) { // 是否实名
				userName.setText(GlobalPara.getOutUserRz().getName());
				userName.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
				isVerified.setBackgroundResource(R.drawable.ic_verified);
			} else {
				userName.setTextColor(ContextCompat.getColor(getActivity(), R.color.deepskyblue));
				userName.setText("点击实名认证");
				userName.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
				userName.getPaint().setAntiAlias(true);// 抗锯齿
				isVerified.setBackgroundResource(R.drawable.ic_unverified);
			}
			loginName.setText(UserUtil.getUserInfo().getLoginName());
		} else {
			rlNoLogin.setVisibility(View.VISIBLE);
			tvLoginOut.setVisibility(View.GONE);
			rlUserInfo.setVisibility(View.GONE);
			noLoginImage.setImageResource(R.drawable.ic_touxiang);
		}
		if (GlobalPara.getTelephone() != null) {
			tvEditServiceNum.setText(GlobalPara.getTelephone());
		}
		float s = file.length() / 1024;
		tvEditClear.setText(Float.toString(s) + "KB");
	}

	/** 显示用户头像 */
	private void initUserHead() {
		FragmentActivity fragAct = getActivity();
		if (fragAct != null) {
			fragAct.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (UserUtil.getUserInfo() != null &&  !TextUtils.isEmpty(UserUtil.getUserInfo().getTxyUserPic())
							&& SDCardUtil.hasSDCard(getActivity())) {
						initSignForUrl();
					} else {
						userImage.setImageResource(R.drawable.ic_touxiang);
					}
				}
			});
		}
	}
	
	protected void initSignForUrl() {
		transDialog = ProgressDialog.show(getActivity(), null, "加载中...", true);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("picurl", UserUtil.getUserInfo().getTxyUserPic());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(getActivity(), LoginActivity_.class);
							startActivity(intent);
						} else {
							final JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								loadPic(obj.getString("result"));
							} else {
								showInfo(obj.getString("desc"));
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("UserFragment", "initSignForUrl()", e);
					LogUmeng.reportError(getActivity(), e);
				}
			}
		});
	}
	
	private void loadPic(String sign) {

		final String headUrl = UserUtil.getUserInfo().getTxyUserPic();
		final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "load";

		GetObjectRequest getObjectRequest = new GetObjectRequest(headUrl, savePath);
		getObjectRequest.setSign(sign);
		getObjectRequest.setListener(new IDownloadTaskListener() {
			@Override
			public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
			}

			@Override
			public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

				new Thread() {
					public void run() {
						String fileName = getNameFromUrl(headUrl);
						picPath = savePath + File.separator + fileName;
						handler.post(runnableUi);
					}
				}.start();

				Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
			}

			@Override
			public void onFailed(COSRequest COSRequest, COSResult cosResult) {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
			}

			@Override
			public void onCancel(COSRequest arg0, COSResult arg1) {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
			}
		});

		WelcomeActivity.getCOSClient().getObject(getObjectRequest);
	}
	
	
	 // 构建Runnable对象，在runnable中更新界面  
    Runnable  runnableUi=new  Runnable(){  
        @Override  
        public void run() {  
            //更新界面  
			Bitmap signBitmap = PicUtil.getSmallBitmap(picPath);
			userImage.setImageBitmap(signBitmap);
			if (transDialog != null && transDialog.isShowing()) {
				transDialog.dismiss();
			}
        }  
          
    }; 

	@Click
	void iv_share() {
//		if (UserUtil.judgeUserInfo()) {
			platformShare();
//		} else {
//			Intent intent = new Intent(getActivity(), LoginActivity_.class);
//			startActivity(intent);
//		}
	}
	
	@Click
	void iv_setting(){
//		Intent intent = new Intent(getActivity(), SettingActivity_.class);
//		startActivity(intent);
	}
	
	@Click
	void rlClear(){
		showInfo(getResources().getString(R.string.deleting));
		FileUtil.RecursionDeleteFile(file);
		if(!file.exists()){
			showInfo(getResources().getString(R.string.clean_cache_sucess));
			tvEditClear.setText("0.0"+"KB");
		}
	}
	
	@Click
	void userName(){
		if(!UserUtil.judgeAuthentication()){
			gotoVerified();
		} else {
			Intent intent = new Intent(getActivity(), MyInfoActivity_.class);
			startActivity(intent);
		}
	}
	
	private void gotoVerified() {
		Intent intent = new Intent();
		if (GlobalPara.getCanAutoVerified() && GlobalPara.getRemainTimes() > 0 && GlobalPara.getOutUserRz() != null
				&& GlobalPara.getOutUserRz().getNameRz() != null && GlobalPara.getOutUserRz().getNameRz() == 0) {
			intent.setClass(getActivity(), AutoVerifiedActivity_.class);
		} else {
			intent.setClass(getActivity(), VerifiedActivity_.class);
		}
		startActivity(intent);
	}
	
	@Click
	void userImage() {//修改头像
		Intent intent = new Intent(getActivity(), AccountPictureActivity_.class);
		startActivity(intent);
	}
	
	@Click
	void tvLogin(){//登录
		Intent intent = new Intent(getActivity(), LoginActivity_.class);
		startActivity(intent);
	}
	
	@Click
	void tvLoginOut(){//退出登录
		GlobalPara.clean();
		UserUtil.clearUserInfo();
		Intent intent = new Intent(getActivity(), LoginActivity_.class);
		startActivity(intent);
	}
//	
//	@Click
//	void rlRepay() {
//		if (UserUtil.judgeUserInfo()) {
//			Intent intent = new Intent(getActivity(), RepaymentActivity_.class);
//			startActivity(intent);
//		} else {
//			Intent intent = new Intent(getActivity(), LoginActivity_.class);
//			startActivity(intent);
//		}
//	}
//	
//	@Click
//	void rlOrder() {
//		if (UserUtil.judgeUserInfo()) {
//			Intent intent = new Intent(getActivity(), OrderActivity_.class);
//			startActivity(intent);
//		} else {
//			Intent intent = new Intent(getActivity(), LoginActivity_.class);
//			startActivity(intent);
//		}
//	}
//	
	@Click
	void rlAccount(){
		if (UserUtil.judgeUserInfo()) {
			Intent intent = new Intent(getActivity(), ManageActivity_.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}

	@Click
	void rlFeedback(){
		if (UserUtil.judgeUserInfo()) {
			Intent intent = new Intent(getActivity(), FeedbackActivity_.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}
	
	@Click
	void rlRecord(){
		if (UserUtil.judgeUserInfo()) {
			Intent intent = new Intent(getActivity(), RecordActivity_.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}
	
	@Click
	void rlServiceNum(){
		if(!TextUtils.isEmpty(ViewUtil.getText(tvEditServiceNum))){
			Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + tvEditServiceNum.getText()));
			startActivity(intent);
		}
	}
	
	@Click
	void rlAboutUs(){
		Intent intent = new Intent(getActivity(), AboutUsActivity_.class);
		startActivity(intent);
	}
	
	@Click
	void rlQQNum1() {
		showDialog(ViewUtil.getText(tvEditQQNum1));
	}
	
	private void showDialog(final String qqNum) {
		CustomDialog1.Builder builder = new CustomDialog1.Builder(getActivity());
		builder.setTitle("客服助手");
		builder.setMessage("是否打开QQ联系客服");
		builder.setPositiveButton("打开QQ", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startQQ(qqNum);
				dialog.dismiss();
			}
		});
		
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	private void startQQ(String qqNum){
		try {
			// 利用Intent打开QQ
			Uri uri = Uri.parse("mqq://im/chat?chat_type=wpa&uin=" + qqNum + "&version=1&src_type=web");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		} catch (Exception e) {
			// 若无法正常跳转，在此进行错误处理
			showInfo("无法跳转到QQ，请检查您是否安装了QQ！");
		}
	}
	
	private void showInfo(final String info) {
		FragmentActivity fragAct = getActivity();
		if (fragAct != null) {
			fragAct.runOnUiThread(new Runnable() {
				public void run() {
					
					if (transDialog != null && transDialog.isShowing()) {
						transDialog.dismiss();
					}
					Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	private String getNameFromUrl(String loadUrl) {
		String[] a = loadUrl.split("/");
		String s = a[a.length - 1];
		return s;
	}
}
