package com.il360.xiaofeiyu.fragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.json.JSONObject;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.ViewById;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.home.CheckActivity2_;
import com.il360.xiaofeiyu.activity.mydata.AutoVerifiedActivity_;
import com.il360.xiaofeiyu.activity.mydata.OperatorInfoActivity_;
import com.il360.xiaofeiyu.activity.mydata.TaoBaoActivity_;
import com.il360.xiaofeiyu.activity.order.OrderApplicationActivity_;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.activity.user.MyBankCardActivity_;
import com.il360.xiaofeiyu.activity.user.MyInfoActivity_;
import com.il360.xiaofeiyu.activity.user.VerifiedActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.home.ArrayOfSwitch;
import com.il360.xiaofeiyu.model.home.RemainTimes;
import com.il360.xiaofeiyu.model.home.TuthenticationChannel;
import com.il360.xiaofeiyu.model.order.ArrayOfLeaseOrder;
import com.il360.xiaofeiyu.model.order.LeaseOrder;
import com.il360.xiaofeiyu.model.user.OutUserRz;
import com.il360.xiaofeiyu.model.user.UserAmount;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.CustomDialog;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.fraudmetrix.octopus.aspirit.main.OctopusManager;
import cn.fraudmetrix.octopus.aspirit.main.OctopusTaskCallBack;

@EFragment(R.layout.fra_authen)
public class AuthenFragment extends MyFragment {
	
    private String mUserId = "";                                  //合作方系统中的客户ID
//    private String mApiKey = "c442630ef8034d84a0bb6eb216f851ac";      //获取任务状态时使用测试
//    private String mApiKey = "5f7fec735a2b4f59b2cd47cabe4b1d22";      //获取任务状态时使用正式
    private static final String TAG = "MoxieSDK";
  
    MxParam mxParam = new MxParam();
	
	@ViewById PullToRefreshScrollView pull_refresh_scrollview;
	
	@ViewById TextView tvAvailable;//手机估值
	@ViewById TextView tvToChange;//去换钱
	@ViewById TextView tvRecheck;//重新检测
	
	@ViewById
	RelativeLayout rlIdentify, rlTaoBao, rlPhone, rlBankCard;
	@ViewById
	ImageView ivIdentifyStatus, ivTaoBaoStatus, ivPhoneStatus,ivBankCardStatus;

	protected ProgressDialog transDialog;
	
	private List<LeaseOrder> myLeaseOrders = new ArrayList<LeaseOrder>();
	private ArrayOfLeaseOrder arrayOfLeaseOrder;	

	private UserAmount userAmount;
	DecimalFormat df = new DecimalFormat("0.00");
	
	int flag = 0;// 1是重新检测,2是去换钱
	
	@Override
	public void onResume() {
		super.onResume();
		if (!UserUtil.judgeUserInfo()) {
			initStatus();
			tvAvailable.setText("-.--");
		} else {
			initSwitch();
//			initCreditLine();
			initUserRz();
			if(UserUtil.getValuationInfo() != null){
				tvAvailable.setText(UserUtil.getValuationInfo().getMoney());
			} else {
				tvAvailable.setText("-.--");
			}
		}
	}

	@AfterViews
	void init() {
		initPull();
	}
	
	private void initPull() {
		pull_refresh_scrollview.setMode(Mode.PULL_FROM_START);
		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (UserUtil.judgeUserInfo()) {
					initSwitch();
//					initCreditLine();
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
	
	private void initData() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"lease/queryLeaseOrderList", params);
					if (result.getSuccess()) {
						myLeaseOrders.clear();
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(getActivity(), LoginActivity_.class);
							startActivity(intent);
						} else {
//							JSONObject obj = new JSONObject(result.getResult());
							arrayOfLeaseOrder = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfLeaseOrder.class);
							if (arrayOfLeaseOrder.getCode() == 1) {
								if(arrayOfLeaseOrder != null && arrayOfLeaseOrder.getResult() != null && arrayOfLeaseOrder.getResult().size() > 0){
									myLeaseOrders.addAll(arrayOfLeaseOrder.getResult());
								} else {
								}
							} else {
//								showInfo(arrayOfLeaseOrder.getDesc());
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
				} finally {
					if( flag == 1){
							if (myLeaseOrders.size() == 0){
								Intent intent = new Intent(getActivity(), CheckActivity2_.class);
								startActivity(intent);
						} else if(myLeaseOrders.size() > 0 && (myLeaseOrders.get(0).getStatus() == 3 || myLeaseOrders.get(0).getStatus() == 5
								|| myLeaseOrders.get(0).getStatus() == -1 || myLeaseOrders.get(0).getStatus() == -2
								|| myLeaseOrders.get(0).getStatus() == 6)){
								Intent intent = new Intent(getActivity(), CheckActivity2_.class);
								startActivity(intent);
						} else if(myLeaseOrders.size() > 0 && (myLeaseOrders.get(0).getStatus() == 1
								|| myLeaseOrders.get(0).getStatus() == 2 || myLeaseOrders.get(0).getStatus() == 4
								|| myLeaseOrders.get(0).getStatus() == 0)){
							showInfo("您还有未完成的订单!");
	//						Intent intent = new Intent(getActivity(), OrderActivity_.class);
	//						startActivity(intent);
						}
					} else if (flag == 2){
						if (myLeaseOrders.size() == 0){
								Intent intent = new Intent(getActivity(), OrderApplicationActivity_.class);
								intent.putExtra("order", UserUtil.getValuationInfo());
								startActivity(intent);
						} else if(myLeaseOrders.size() > 0 && (myLeaseOrders.get(0).getStatus() == 3 || myLeaseOrders.get(0).getStatus() == 5
								|| myLeaseOrders.get(0).getStatus() == -1 || myLeaseOrders.get(0).getStatus() == -2
								|| myLeaseOrders.get(0).getStatus() == 6)){
								Intent intent = new Intent(getActivity(), OrderApplicationActivity_.class);
								intent.putExtra("order", UserUtil.getValuationInfo());
								startActivity(intent);
						} else if(myLeaseOrders.size() > 0 && (myLeaseOrders.get(0).getStatus() == 1
								|| myLeaseOrders.get(0).getStatus() == 2 || myLeaseOrders.get(0).getStatus() == 4
								|| myLeaseOrders.get(0).getStatus() == 0 || myLeaseOrders.get(0).getStatus() == -3)){
							showInfo("您还有未完成的订单!");
//							Intent intent = new Intent(getActivity(), OrderActivity_.class);
//							startActivity(intent);
						}
					}
					}
				}
		});
	}

	protected void initUserRz() {
		transDialog = ProgressDialog.show(getActivity(), null, "努力加载中...", true, false);
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
					Log.e("AuthenFragment", "initUserRz", e);
					showInfo(getResources().getString(R.string.A2));
					LogUmeng.reportError(getActivity(), e);
				} finally {
					FragmentActivity fragAct = getActivity();
					if (fragAct != null) {
						fragAct.runOnUiThread(new Runnable() {
							public void run() {
								if (transDialog != null && transDialog.isShowing()) {
									transDialog.dismiss();
								}
								if (GlobalPara.outUserRz != null) {
									initViews();
								}
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

	//type:1淘宝 ,2支付宝, 3京东
	private void tongdun(final String taskId) {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("taskId", taskId);
					params.put("token", UserUtil.getToken());
					params.put("platform", "2");
					params.put("type", "1");
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "card/uploadTdTbData", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
						} else {
							final JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								showInfo(obj.getString("desc"));
							} else {
								showInfo(obj.getString("desc"));
							}
						}
					} else {
						showInfo(getString(R.string.A6));
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
	
//	private void initCreditLine() {
//		ExecuteTask.execute(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Map<String, String> params = new HashMap<String, String>();
//					params.put("token", UserUtil.getToken());
//					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
//							"user/queryUserAmount", params);
//					if (result.getSuccess()) {
//						JSONObject obj = new JSONObject(result.getResult());
//						if (obj.getInt("code") == 1) {
//							JSONObject objRes = obj.getJSONObject("result");
//							userAmount = FastJsonUtils.getSingleBean(objRes.toString(), UserAmount.class);
//						} else {
//							showInfo(obj.getString("desc"));
//						}
//					}
//				} catch (Exception e) {
//					Log.e("AuthenFragment", "initCreditLine", e);
//					LogUmeng.reportError(getActivity(), e);
//				} finally {
//					FragmentActivity fragAct = getActivity();
//					if (fragAct != null) {
//						fragAct.runOnUiThread(new Runnable() {
//							public void run() {
//								if (userAmount != null) {
//									tvAvailable.setText(
//											df.format(userAmount.getAllAmount().subtract(userAmount.getUseAmount())));
//									if(userAmount.getUseAmount().doubleValue() > 0){
//									} else {
//									}
//								}
//							}
//						});
//					}
//				}
//			}
//		});
//	}

	private void initViews() {
		initStatus();
		
		if (UserUtil.judgeAuthentication()) {
			ivIdentifyStatus.setBackgroundResource(R.drawable.ic_my_hua_ok);
			initBankCard();
			initTaoBao();
			initPhone();
			
		} else {
			if (GlobalPara.getOutUserRz().getNameRz() != null && GlobalPara.getOutUserRz().getNameRz() == -1) {
				ivIdentifyStatus.setBackgroundResource(R.drawable.ic_my_hua_failed);
			} else if (GlobalPara.getOutUserRz().getNameRz() != null && GlobalPara.getOutUserRz().getNameRz() == 2) {
				ivIdentifyStatus.setBackgroundResource(R.drawable.ic_my_hua_audit);
			} else {
				ivIdentifyStatus.setBackgroundResource(R.drawable.ic_my_hua_no);
			}
		}

	}

	private void initStatus() {
		ivIdentifyStatus.setBackgroundResource(R.drawable.ic_my_hua_no);
		ivBankCardStatus.setBackgroundResource(R.drawable.ic_my_hua_no);
		ivTaoBaoStatus.setBackgroundResource(R.drawable.ic_my_hua_no);
		ivPhoneStatus.setBackgroundResource(R.drawable.ic_my_hua_no);
	}

	private void initTaoBao() {
		if (GlobalPara.getOutUserRz().getTaobaoRz() != null && GlobalPara.getOutUserRz().getTaobaoRz() == -1) {
			ivTaoBaoStatus.setBackgroundResource(R.drawable.ic_my_hua_failed);
		} else if (GlobalPara.getOutUserRz().getTaobaoRz() != null && GlobalPara.getOutUserRz().getTaobaoRz() == 1) {
			ivTaoBaoStatus.setBackgroundResource(R.drawable.ic_my_hua_ok);
		} else if (GlobalPara.getOutUserRz().getTaobaoRz() != null && GlobalPara.getOutUserRz().getTaobaoRz() == 2) {
			ivTaoBaoStatus.setBackgroundResource(R.drawable.ic_my_hua_audit);
		} else {
			ivTaoBaoStatus.setBackgroundResource(R.drawable.ic_my_hua_no);
		}
	}

	private void initPhone() {
		if (GlobalPara.getOutUserRz().getPhoneRz() != null && GlobalPara.getOutUserRz().getPhoneRz() == -1) {
			ivPhoneStatus.setBackgroundResource(R.drawable.ic_my_hua_failed);
		} else if (GlobalPara.getOutUserRz().getPhoneRz() != null && GlobalPara.getOutUserRz().getPhoneRz() == 1) {
			ivPhoneStatus.setBackgroundResource(R.drawable.ic_my_hua_ok);
		} else if (GlobalPara.getOutUserRz().getPhoneRz() != null && GlobalPara.getOutUserRz().getPhoneRz() == 2) {
			ivPhoneStatus.setBackgroundResource(R.drawable.ic_my_hua_audit);
		} else if (GlobalPara.getOutUserRz().getPhoneRz() != null && GlobalPara.getOutUserRz().getPhoneRz() == 3) {
			ivPhoneStatus.setBackgroundResource(R.drawable.ic_my_hua_audit);
		} else {
			ivPhoneStatus.setBackgroundResource(R.drawable.ic_my_hua_no);
		}
	}

	private void initBankCard() {
		if (GlobalPara.getOutUserRz().getBankRz() != null && GlobalPara.getOutUserRz().getBankRz() == -1) {
			ivBankCardStatus.setBackgroundResource(R.drawable.ic_my_hua_failed);
		} else if (GlobalPara.getOutUserRz().getBankRz() != null && GlobalPara.getOutUserRz().getBankRz() == 1) {
			ivBankCardStatus.setBackgroundResource(R.drawable.ic_my_hua_ok);
		} else if (GlobalPara.getOutUserRz().getBankRz() != null && GlobalPara.getOutUserRz().getBankRz() == 2) {
			ivBankCardStatus.setBackgroundResource(R.drawable.ic_my_hua_audit);
		} else {
			ivBankCardStatus.setBackgroundResource(R.drawable.ic_my_hua_no);
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
	void rlIdentify() {
		if (UserUtil.judgeUserInfo()) {
			if (UserUtil.judgeAuthentication()) {
				Intent intent = new Intent(getActivity(), MyInfoActivity_.class);
				startActivity(intent);
			} else {
				gotoVerified();
			}
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}

	}
	
	
	@Click
	void rlBankCard() {
		if (UserUtil.judgeUserInfo()) {
			if (UserUtil.judgeAuthentication()) {
				Intent intent = new Intent(getActivity(), MyBankCardActivity_.class);
				startActivity(intent);
			} else {
				showDialog(getResources().getString(R.string.H2));
			}
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}

	@Click
	void rlTaoBao() {
		if (UserUtil.judgeUserInfo()) {
			if (UserUtil.judgeAuthentication()) {
				if(UserUtil.judgeBankCard()){
					if (GlobalPara.getOutUserRz().getTaobaoRz() != null && GlobalPara.getOutUserRz().getTaobaoRz() == -1) {
						showDialog2("淘宝认证失败！");
					} else if (GlobalPara.getOutUserRz().getTaobaoRz() != null && GlobalPara.getOutUserRz().getTaobaoRz() == 1) {
						showDialog2("淘宝认证已通过！");
					} else if (GlobalPara.getOutUserRz().getTaobaoRz() != null && GlobalPara.getOutUserRz().getTaobaoRz() == 2) {
						showDialog2("正在等待认证结果，请耐心等待！");
					} else {
//						tongdunlink();
						initType(2);
					}
				} else {
					showDialog3();
				}
			} else {
				showDialog(getResources().getString(R.string.H2));
			}
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}

	@Click
	void rlPhone() {
		if (UserUtil.judgeUserInfo()) {
			if (UserUtil.judgeAuthentication()) {
				if(UserUtil.judgeBankCard()){
					if (GlobalPara.getOutUserRz().getPhoneRz() != null && GlobalPara.getOutUserRz().getPhoneRz() == -1) {
						showDialog2("运营商认证失败！");
					} else if (GlobalPara.getOutUserRz().getPhoneRz() != null && GlobalPara.getOutUserRz().getPhoneRz() == 1) {
						showDialog2("运营商认证已通过！");
					} else if(GlobalPara.getOutUserRz().getPhoneRz() != null && (GlobalPara.getOutUserRz().getPhoneRz() == 2 
							|| GlobalPara.getOutUserRz().getPhoneRz() == 3)){
						showDialog2("正在等待认证结果，请耐心等待！");
					} else {
						Intent intent = new Intent(getActivity(), OperatorInfoActivity_.class);
						startActivity(intent);
					}
				} else {
					showDialog3();
				}
			} else {
				showDialog(getResources().getString(R.string.H2));
			}
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}

	
	@Click
	void tvToChange(){
		if (UserUtil.judgeUserInfo()) {
			if(UserUtil.judgeAuthentication() && UserUtil.judgeOperator()
					&& UserUtil.judgeTaoBao() && UserUtil.judgeBankCard()){
				if(UserUtil.getValuationInfo() == null){
					showDialog2("请先在首页完成评估！");
				} else {
					flag = 2;
					initData();
				}
			} else {
				showDialog2("请先完成认证！");
			}
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}
	
	@Click
	void tvRecheck(){
		if(UserUtil.judgeUserInfo()){
			if(UserUtil.getValuationInfo() == null){
				showDialog2("请先在首页完成评估！");
			} else {
				flag = 1;
				initData();
			}
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}
	
	private void tongdunlink(){
		OctopusManager.getInstance().setNavImgResId(R.drawable.ic_back);//
		OctopusManager.getInstance().setPrimaryColorResId(R.color.white);//
//		OctopusManager.getInstance().setTitleColorResId(R.color.black);//
//		OctopusManager.getInstance().setTitleSize(14);// sp
		OctopusManager.getInstance().setShowWarnDialog(true);
		OctopusManager.getInstance().setStatusBarBg(R.color.black);
		OctopusManager.getInstance().getChannel(getActivity(), "005003", new OctopusTaskCallBack() {
			@Override
			public void onCallBack(int code, String taskId) {
				String msg = "success:";
				if (code == 0) {// code
					// String taskId =
					// data.getStringExtra(OctopusConstants.OCTOPUS_TASK_RESULT_TASKID);//
					msg += taskId;
					if(msg != null){
						tongdun(taskId);
					}
				} else {
					msg = "failure：" + code;
				}
			}
		});
	}
	
	private void initType(final int type) {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("type", type + "");
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,"card/queryTuthenticationChannel", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(getActivity(), LoginActivity_.class);
							startActivity(intent);
						} else {
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								JSONObject objRes = obj.getJSONObject("result");
								TuthenticationChannel tc = FastJsonUtils.getSingleBean(objRes.toString(),TuthenticationChannel.class);
								if (type == 1) {// 支付宝
//									if (tc.getTuthenticationMethod().intValue() == 1) {
//										Intent intent = new Intent(getActivity(), HuaBeiActivity_.class);
//										startActivity(intent);
//									} else if (tc.getTuthenticationMethod().intValue() == 2) {
//										mUserId = UserUtil.getUserInfo().getPhone() + "," + "6";
//								        mxParam.setUserId(mUserId);
//								        mxParam.setApiKey(Variables.MX_APIKEY);
//										mxParam.setFunction(MxParam.PARAM_FUNCTION_ALIPAY);
//										moxieCallback();
//									}
								} else if (type == 2) {// 淘宝
									if (tc.getTuthenticationMethod().intValue() == 1) {
										Intent intent = new Intent(getActivity(), TaoBaoActivity_.class);
										startActivity(intent);
									} else if (tc.getTuthenticationMethod().intValue() == 2) {
										mUserId = UserUtil.getUserInfo().getPhone() + "," + "6";
								        mxParam.setUserId(mUserId);
								        mxParam.setApiKey(Variables.MX_APIKEY);
										mxParam.setFunction(MxParam.PARAM_FUNCTION_TAOBAO);
										moxieCallback();
									}
								} else if (type == 3) {// 京东
//									if (tc.getTuthenticationMethod().intValue() == 1) {
//										Intent intent = new Intent(getActivity(), JingDongActivity_.class);
//										startActivity(intent);
//									} else if (tc.getTuthenticationMethod().intValue() == 2) {
//										mUserId = UserUtil.getUserInfo().getPhone() + "," + "6";
//								        mxParam.setUserId(mUserId);
//								        mxParam.setApiKey(Variables.MX_APIKEY);
//										mxParam.setFunction(MxParam.PARAM_FUNCTION_JINGDONG);
//										moxieCallback();
//									}
								}
							} else {
								showInfo(obj.getString("desc"));
							}
						}
					}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				}
			}
		});
	}

	private void showDialog(String message) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle(R.string.app_name);
		builder.setMessage(message);
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				gotoVerified();
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
	
	
	private void showDialog3() {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle(R.string.app_name);
		builder.setMessage("当前用户尚未通过银行卡认证，请先去银行卡认证");
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(getActivity(), MyBankCardActivity_.class);
				startActivity(intent);
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
	
	private void showDialog2(String message) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
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
	
	public void showInfo(final String info) {
		FragmentActivity fragAct = getActivity();
		if (fragAct != null) {
			fragAct.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
	
	private void moxieCallback(){
		MoxieSDK.getInstance().start(getActivity(), mxParam, new MoxieCallBack() {
            /**
             *
             *  物理返回键和左上角返回按钮的back事件以及sdk退出后任务的状态都通过这个函数来回调
             *
             * @param moxieContext       可以用这个来实现在魔蝎的页面弹框或者关闭魔蝎的界面
             * @param moxieCallBackData  我们可以根据 MoxieCallBackData 的code来判断目前处于哪个状态，以此来实现自定义的行为
             * @return                   返回true表示这个事件由自己全权处理，返回false会接着执行魔蝎的默认行为(比如退出sdk)
             *
             *   # 注意，假如设置了MxParam.setQuitOnLoginDone(MxParam.PARAM_COMMON_YES);
             *   登录成功后，返回的code是MxParam.ResultCode.IMPORTING，不是MxParam.ResultCode.IMPORT_SUCCESS
             */
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                /**
                 *  # MoxieCallBackData的格式如下：
                 *  1.1.没有进行账单导入，未开始！(后台没有通知)
                 *      "code" : MxParam.ResultCode.IMPORT_UNSTART, "taskType" : "mail", "taskId" : "", "message" : "", "account" : "", "loginDone": false, "businessUserId": ""
                 *  1.2.平台方服务问题(后台没有通知)
                 *      "code" : MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR, "taskType" : "mail", "taskId" : "", "message" : "", "account" : "xxx", "loginDone": false, "businessUserId": ""
                 *  1.3.魔蝎数据服务异常(后台没有通知)
                 *      "code" : MxParam.ResultCode.MOXIE_SERVER_ERROR, "taskType" : "mail", "taskId" : "", "message" : "", "account" : "xxx", "loginDone": false, "businessUserId": ""
                 *  1.4.用户输入出错（密码、验证码等输错且未继续输入）
                 *      "code" : MxParam.ResultCode.USER_INPUT_ERROR, "taskType" : "mail", "taskId" : "", "message" : "密码错误", "account" : "xxx", "loginDone": false, "businessUserId": ""
                 *  2.账单导入失败(后台有通知)
                 *      "code" : MxParam.ResultCode.IMPORT_FAIL, "taskType" : "mail",  "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx", "loginDone": false, "businessUserId": ""
                 *  3.账单导入成功(后台有通知)
                 *      "code" : MxParam.ResultCode.IMPORT_SUCCESS, "taskType" : "mail",  "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx", "loginDone": true, "businessUserId": "xxxx"
                 *  4.账单导入中(后台有通知)
                 *      "code" : MxParam.ResultCode.IMPORTING, "taskType" : "mail",  "taskId" : "ce6b3806-57a2-4466-90bd-670389b1a112", "account" : "xxx", "loginDone": true, "businessUserId": "xxxx"
                 *
                 *  code           :  表示当前导入的状态
                 *  taskType       :  导入的业务类型，与MxParam.setFunction()传入的一致
                 *  taskId         :  每个导入任务的唯一标识，在登录成功后才会创建
                 *  message        :  提示信息
                 *  account        :  用户输入的账号
                 *  loginDone      :  表示登录是否完成，假如是true，表示已经登录成功，接入方可以根据此标识判断是否可以提前退出
                 *  businessUserId :  第三方被爬取平台本身的userId，非商户传入，例如支付宝的UserId
                 */
                if (moxieCallBackData != null) {
                    Log.d("BigdataFragment", "MoxieSDK Callback Data : "+ moxieCallBackData.toString());
                    switch (moxieCallBackData.getCode()) {
                        /**
                         * 账单导入中
                         *
                         * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
                         * 魔蝎后台会向贵方后台推送Task通知和Bill通知
                         * Task通知：登录成功/登录失败
                         * Bill通知：账单通知
                         */
                        case MxParam.ResultCode.IMPORTING:
                            if(moxieCallBackData.isLoginDone()) {
                                //状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
                                Log.d(TAG, "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

                            } else {
                                //状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
                                Log.d(TAG, "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            }
                            break;
                        /**
                         * 任务还未开始
                         *
                         * 假如有弹框需求，可以参考 {@link BigdataFragment#showDialog(MoxieContext)}
                         *
                         * example:
                         *  case MxParam.ResultCode.IMPORT_UNSTART:
                         *      showDialog(moxieContext);
                         *      return true;
                         * */
                        case MxParam.ResultCode.IMPORT_UNSTART:
                            Log.d(TAG, "任务未开始");
                            break;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
                            Toast.makeText(getContext(), "导入失败(平台方服务问题)", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
                            Toast.makeText(getContext(), "导入失败(魔蝎数据服务异常)", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.USER_INPUT_ERROR:
                            Toast.makeText(getContext(), "导入失败(" + moxieCallBackData.getMessage() + ")", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.IMPORT_FAIL:
                            Toast.makeText(getContext(), "导入失败", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            Log.d(TAG, "任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

                            //根据taskType进行对应的处理
                            switch (moxieCallBackData.getTaskType()) {
                                case MxParam.PARAM_FUNCTION_ALIPAY:
//                                    Toast.makeText(getContext(), "支付宝验证成功", Toast.LENGTH_SHORT).show();
                                	updateStatus(1);
                                    break;
                                case MxParam.PARAM_FUNCTION_TAOBAO:
//                                    Toast.makeText(getContext(), "淘宝验证成功", Toast.LENGTH_SHORT).show();
                                	updateStatus(2);
                                    break;
                                case MxParam.PARAM_FUNCTION_JINGDONG:
//                                	Toast.makeText(getContext(), "京东验证成功", Toast.LENGTH_SHORT).show();
                                	updateStatus(3);
                                	break;
                                default:
                                    Toast.makeText(getContext(), "导入成功", Toast.LENGTH_SHORT).show();
                            }
                            moxieContext.finish();
                            return true;
                    }
                }
                return false;
            }
        });
	}
	
	private void updateStatus(final int type) {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("type", type + "");
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"card/updateUserRz", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							FragmentActivity fragAct = getActivity();
							if (fragAct != null) {
								fragAct.runOnUiThread(new Runnable() {
									public void run() {
										if (type == 1) {
//											ivZhiFuBaoStatus.setBackgroundResource(R.drawable.ic_my_hua_audit);
										} else if (type == 2) {
											ivTaoBaoStatus.setBackgroundResource(R.drawable.ic_my_hua_audit);
										} else if (type == 3) {
//											ivJingDongStatus.setBackgroundResource(R.drawable.ic_my_hua_audit);
										}
									}
								});
							}
						}
					}
				} catch (Exception e) {
				}
			}
		});
	}
	
}
