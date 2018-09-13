package com.il360.xiaofeiyu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.il360.xiaofeiyu.activity.home.CheckResultActivity_;
import com.il360.xiaofeiyu.activity.home.GuideActivity_;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.activity.user.RecommendActivity_;
import com.il360.xiaofeiyu.adapter.HomeHotPhoneAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.goods.Goods;
import com.il360.xiaofeiyu.model.goods.ResultOfGoods;
import com.il360.xiaofeiyu.model.home.OutContact;
import com.il360.xiaofeiyu.model.order.ArrayOfLeaseOrder;
import com.il360.xiaofeiyu.model.order.LeaseOrder;
import com.il360.xiaofeiyu.model.user.OutUserRz;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.SystemUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

@EFragment(R.layout.fra_home)
public class HomeFragment extends MyFragment {

	@ViewById TextView tvPhoneName,tvToCheck, tvValue;
	
	@ViewById PullToRefreshScrollView pull_refresh_scrollview;
	
/*	@ViewById ViewPager vp_adv_change;
	@ViewById LinearLayout ll_adv_circle;*/
	@ViewById ImageView ivHomeBanner;
	
	private List<LeaseOrder> myLeaseOrders = new ArrayList<LeaseOrder>();
	private ArrayOfLeaseOrder arrayOfLeaseOrder;
//	private ArrayOfAdvert arrayOfAdvert;
	private ResultOfGoods resultOfGoods;//返回首页评估信息
	protected ProgressDialog transDialog;
	private int flag = 0;
	
//	@ViewById MyGridView gvHotPhone;
	private HomeHotPhoneAdapter homeHotPhoneAdapter;
	private List<Goods> hotPhonelist = new ArrayList<Goods>();
	private String phoneName = SystemUtil.getSystemModel();
	private String memory;
	private String ramMemory;
	private String totalMemory;
	private String defaultAmount;
	
	@AfterViews
	void init() {
		pull_refresh_scrollview.scrollTo(0, 0);
//		initAdvert();
		tvPhoneName.setText(phoneName);
		memory = SystemUtil.getInternalMemorySize(getContext());
		Pattern p = Pattern.compile("[^0-9.]");
		Matcher m = p.matcher(memory);
		memory = m.replaceAll("".trim());
		if (Double.parseDouble(memory) < 8) {
			memory = "8GB";
		} else if (Double.parseDouble(memory) > 8 && Double.parseDouble(memory) < 16) {
			memory = "16GB";
		} else if (Double.parseDouble(memory) > 16 && Double.parseDouble(memory) < 32) {
			memory = "32GB";
		} else if (Double.parseDouble(memory) > 32 && Double.parseDouble(memory) < 64) {
			memory = "64GB";
		} else if (Double.parseDouble(memory) > 32 && Double.parseDouble(memory) < 128) {
			memory = "128GB";
		} else if (Double.parseDouble(memory) > 128 && Double.parseDouble(memory) < 256) {
			memory = "256GB";
		}
		ramMemory = SystemUtil.getTotalRam(getContext());
		totalMemory = ramMemory + "+" + memory;
		initPull();
//		initHotPhone();
		initValuationPrive();

		if(UserUtil.getValuationInfo() != null){
			tvValue.setText(UserUtil.getValuationInfo().getMoney());
		}
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
					if (myLeaseOrders.size() == 0){
						if(UserUtil.getValuationInfo() == null) {
							Intent intent = new Intent(getActivity(), CheckActivity2_.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(getActivity(), CheckResultActivity_.class);
							startActivity(intent);
						}
					} else if(myLeaseOrders.size() > 0 && (myLeaseOrders.get(0).getStatus() == 3 || myLeaseOrders.get(0).getStatus() == 5
							|| myLeaseOrders.get(0).getStatus() == -1 || myLeaseOrders.get(0).getStatus() == -2
							|| myLeaseOrders.get(0).getStatus() == 6)){
						if(UserUtil.getValuationInfo() == null) {
							Intent intent = new Intent(getActivity(), CheckActivity2_.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(getActivity(), CheckResultActivity_.class);
							startActivity(intent);
						}
					} else if(myLeaseOrders.size() > 0 && (myLeaseOrders.get(0).getStatus() == 1
							|| myLeaseOrders.get(0).getStatus() == 2 || myLeaseOrders.get(0).getStatus() == 4
							|| myLeaseOrders.get(0).getStatus() == 0) || myLeaseOrders.get(0).getStatus() == -3){
						showInfo("您还有未完成的订单,暂时无法进行租赁!");
//						Intent intent = new Intent(getActivity(), OrderActivity_.class);
//						startActivity(intent);
					}
				}
			}
		});
	}

	private void initPull() {
		pull_refresh_scrollview.setMode(Mode.PULL_FROM_START);
		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				initHotPhone();
				initValuationPrive();
				if (UserUtil.judgeUserInfo()) {
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

	private void initValuationPrive() {
		ExecuteTask.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("goodsSys", phoneName);
					params.put("goodsType", totalMemory);
					if (UserUtil.judgeUserInfo()) {
						params.put("userId", UserUtil.getUserInfo().getUserId() + "");
					}
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"valuation/queryTheAmount", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							resultOfGoods = FastJsonUtils.getSingleBean(obj.toString(),ResultOfGoods.class);
						} else if(obj.getInt("code") == -1){
							defaultAmount = obj.getString("result");
//							showInfo(obj.getString("desc"));
						}
					}
				} catch (Exception e) {
				} finally {
					FragmentActivity fragAct = getActivity();
					if (fragAct != null) {
						fragAct.runOnUiThread(new Runnable() {
							@SuppressLint("ResourceAsColor")
							public void run() {
								if(resultOfGoods != null){
									tvValue.setText(resultOfGoods.getResult().getAmount());
								} else {
									tvValue.setText(defaultAmount);
								}
							}
						});
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
							GlobalPara.outUserRz = null;
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								JSONObject objRes = obj.getJSONObject("result");
								JSONObject objRetRes = objRes.getJSONObject("returnResult");
								GlobalPara.outUserRz = FastJsonUtils.getSingleBean(objRetRes.toString(), OutUserRz.class);
								
								if(GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getAppCount() != null && GlobalPara.getOutUserRz().getAppCount() == 1){
									//已上传app列表
								} else {
									upAPPName();//上传app列表
								}
							}
						}
					}
				} catch (Exception e) {
					Log.e("HomeFragment", "initUserReg", e);
					LogUmeng.reportError(getActivity(), e);
				}finally {
					FragmentActivity fragAct = getActivity();
					if (fragAct != null) {
						fragAct.runOnUiThread(new Runnable() {
							public void run() {
								pull_refresh_scrollview.onRefreshComplete();
							}
						});
					}
				}
			}
		});
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
				if (ResultUtil.isOutTime(result.getResult()) != null) {
					showInfo(ResultUtil.isOutTime(result.getResult()));
					Intent intent = new Intent(getActivity(), LoginActivity_.class);
					startActivity(intent);
				} else {
					JSONObject obj = new JSONObject(result.getResult());
					if (obj.getInt("code") == 1) {
						GlobalPara.getOutUserRz().setAppCount(1);
					}
				}
			}
		} catch (Exception e) {
		}
	}
	
//	private void initAdvert() {
//		ExecuteTask.execute(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Map<String, String> params = new HashMap<String, String>();
//					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
//							"card/queryAdvertList", params);
//					if (result.getSuccess()) {
//						JSONObject obj = new JSONObject(result.getResult());
//						if (obj.getInt("code") == 1) {
//							JSONObject objRes = obj.getJSONObject("result");
//							JSONObject objRetRes = objRes.getJSONObject("returnResult");
//							arrayOfAdvert = FastJsonUtils.getSingleBean(objRetRes.toString(), ArrayOfAdvert.class);
//						} else {
//							showInfo(obj.getString("desc"));
//						}
//					} else {
//						showInfo(getString(R.string.A6));
//					}
//				} catch (Exception e) {
//					Log.e("HomeFragment", "initAdvert", e);
//				} finally {
//					FragmentActivity fragAct = getActivity();
//					if (fragAct != null) {
//						fragAct.runOnUiThread(new Runnable() {
//							public void run() {
//								if (arrayOfAdvert != null && arrayOfAdvert.getList() != null
//										&& arrayOfAdvert.getList().size() > 0) {
//									List<String> listUrl = new ArrayList<String>();
//									List<String> nameList = new ArrayList<String>();
//									List<String> linkList = new ArrayList<String>();
//									for (Advert adv : arrayOfAdvert.getList()) {
//										listUrl.add(adv.getPicUrl());
//										nameList.add(adv.getTitle());
//										linkList.add(adv.getWebUrl());
//									}
//									new SlideViewLayout(getActivity(), ll_adv_circle, vp_adv_change, listUrl, nameList,
//											linkList, Variables.APP_BASE_URL);
//								} else {
//									setting();
//								}
//							}
//						});
//					}
//				}
//			}
//		});
//	}
//	
//	private void setting() {
//		List<Integer> imgResId = new ArrayList<Integer>();
//		imgResId.add(R.drawable.bg_my);
//		new SlideViewLayout(getActivity(), ll_adv_circle, vp_adv_change, imgResId);
//	}
	
	@Click
	void tvToCheck(){
			if (UserUtil.judgeUserInfo()) {
//				if( flag == 0 ){
					initData();
//				} else if (flag == 1){
//					initData();
//				}
			} else {
				Intent intent = new Intent(getActivity(), LoginActivity_.class);
				startActivity(intent);
			}
		
	}
	
	@Click
	void ivShare() {
		if (UserUtil.judgeUserInfo()) {
			Intent intent = new Intent(getActivity(), RecommendActivity_.class);
			startActivity(intent);
		} else {
			Intent intent = new Intent(getActivity(), LoginActivity_.class);
			startActivity(intent);
		}
	}
	
	@Click
	void ivHomeBanner(){
		Intent intent = new Intent(getActivity(), GuideActivity_.class);
		startActivity(intent);
	}
	
//	class OnItemClickListener implements
//	android.widget.AdapterView.OnItemClickListener {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position,
//				long id) {
//			try {
//				Intent intent = null;
//				switch (position) {
//				
//				case 0: // 二手机列表
//					intent = new Intent(getActivity(), IPhoneActivity_.class);
//					intent.putExtra("goodCode", 2);
//					break;
//				
//				case 1: // 回收
//					((MainActivity) getActivity()).changeToRecoveryFragment();
//					break;
//				case 2: // 查询额度
//					if(UserUtil.judgeUserInfo()){
//						((MainActivity) getActivity()).changeToAuthenFragment();
//					}else{
//						intent = new Intent(getActivity(), LoginActivity_.class);
//					}
//					
//					break;
//				case 3: // 订单查询
//					if(UserUtil.judgeUserInfo()){
//						intent = new Intent(getActivity(), OrderCenterActivity_.class);
//					}else{
//						intent = new Intent(getActivity(), LoginActivity_.class);
//					}
//					
//					break;
//				default:
//					showInfo("正在火热开发中...");
//				}
//				if (null == intent) {
//					return;
//				}
//				getActivity().startActivityForResult(intent, 3);
//			} catch (Exception e) {
//			}
//		}
//	};

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
}
