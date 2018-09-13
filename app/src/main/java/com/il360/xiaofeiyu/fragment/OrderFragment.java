package com.il360.xiaofeiyu.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.ViewById;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.order.OrderDetailsActivity_;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.adapter.OrderBuyAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.order.ArrayOfFrozenFeeOrder;
import com.il360.xiaofeiyu.model.order.ArrayOfLeaseOrder;
import com.il360.xiaofeiyu.model.order.FrozenFeeOrder;
import com.il360.xiaofeiyu.model.order.LeaseOrder;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.ListViewForScrollView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

@EFragment(R.layout.fra_order)
public class OrderFragment extends MyFragment {

	@ViewById PullToRefreshScrollView pull_refresh_scrollview;
	
	@ViewById ListViewForScrollView myList;
	
	@ViewById ImageView ivNoOrder;
	
	private List<LeaseOrder> myLeaseOrders = new ArrayList<LeaseOrder>();
	private OrderBuyAdapter myAdapter;
	
	private ArrayOfLeaseOrder arrayOfLeaseOrder;
	protected ProgressDialog transDialog;

	private ArrayOfFrozenFeeOrder arrayOfPayFeeOrder;
	private List<FrozenFeeOrder> list = new ArrayList<FrozenFeeOrder>();
	private FrozenFeeOrder frozenFeeOrder;
	
	@AfterViews
	void init() {
		
		pull_refresh_scrollview.setMode(Mode.PULL_FROM_START);
		initRefreshListener();
		refreshOrder();
	}

	private void initFrozenFee() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"lease/queryFrozenFeeList", params);
					if (result.getSuccess()) {
						myLeaseOrders.clear();
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(getActivity(), LoginActivity_.class);
							startActivity(intent);

						} else {
//							JSONObject obj = new JSONObject(result.getResult());
							arrayOfPayFeeOrder = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfFrozenFeeOrder.class);
							if(arrayOfPayFeeOrder.getCode() == 1){
								list = arrayOfPayFeeOrder.getResult();
								for(int i = 0; i < list.size(); i++){
									frozenFeeOrder = list.get(i);
								}
							} else {
//								showInfo(arrayOfPayFeeOrder.getDesc());
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
//							ivNoOrder.setVisibility(View.GONE);
							arrayOfLeaseOrder = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfLeaseOrder.class);
							if (arrayOfLeaseOrder.getCode() == 1) {
								if(arrayOfLeaseOrder != null && arrayOfLeaseOrder.getResult() != null && arrayOfLeaseOrder.getResult().size() > 0){
									myLeaseOrders.addAll(arrayOfLeaseOrder.getResult());
								}
							} else {
								showInfo(arrayOfLeaseOrder.getDesc());
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
				} finally {
					getActivity().runOnUiThread(new Runnable() {
							public void run() {
								if (transDialog != null && transDialog.isShowing()) {
									transDialog.dismiss();
								}
								
								if(myLeaseOrders != null && myLeaseOrders.size() > 0){
									myList.setVisibility(View.VISIBLE);
									ivNoOrder.setVisibility(View.GONE);
									showList();
									myAdapter.notifyDataSetChanged();
								} else {
									myList.setVisibility(View.GONE);
									ivNoOrder.setVisibility(View.VISIBLE);
								}
								
								
								pull_refresh_scrollview.onRefreshComplete();
							}
						});
					}
			}
		});
	}

	private void initRefreshListener() {
		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener<ScrollView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				myList.postDelayed(new Runnable() {
					@Override
					public void run() {
						initData();
						initFrozenFee();
					}
				}, 500);
			}
		});
	}
	
	protected void showList() {
		myAdapter = new OrderBuyAdapter(myLeaseOrders, getActivity(), frozenFeeOrder);
		myList.setAdapter(myAdapter);
		myList.setOnItemClickListener(new myListClickListener());
	}
	
	class myListClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity(),OrderDetailsActivity_.class);
			intent.putExtra("leaseOrder", myLeaseOrders.get(position));
			intent.putExtra("frozenFeeOrder",  frozenFeeOrder);
			startActivity(intent);
		}
	}
//	
//	private void initMyList() {
//		transDialog = ProgressDialog.show(OrderActivity.this, null, "加载中...", true);
//		ExecuteTask.execute(new Runnable() {
//			@Override
//			public void run() {
//				try {
////					Map<String, String> params = new HashMap<String, String>();
////					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
////							"order/queryOrderList", params);
////					if (result.getSuccess()) {
////						JSONObject obj = new JSONObject(result.getResult());
////						if (obj.getInt("code") == 1) {
////							JSONObject objRes = obj.getJSONObject("result");
////							JSONObject objResult = objRes.getJSONObject("returnResult");
////							arrayOfOrder = FastJsonUtils.getSingleBean(objResult.toString(), ArrayOfOrder.class);
////							if (arrayOfOrder != null && arrayOfOrder.getList() != null && arrayOfOrder.getList().size() > 0) {
////								myOrder.addAll(arrayOfOrder.getList());
////							}
////						} else {
////							showInfo(obj.getString("desc"));
////						}
////					} else {
////						showInfo(getString(R.string.A6));
////					}
////				} catch (Exception e) {
////					Log.e("OrderBuyFragment", "initMyList", e);
////					LogUmeng.reportError(OrderActivity.this, e);
//				} finally {
//					OrderActivity.this.runOnUiThread(new Runnable() {
//							public void run() {
//
//								if (transDialog != null && transDialog.isShowing()) {
//									transDialog.dismiss();
//								}
//								myAdapter.notifyDataSetChanged();
//								pull_refresh_scrollview.onRefreshComplete();
//							}
//						});
//
//					}
//			}
//		});
//	}
	
	public void showInfo(final String info) {
		getActivity().runOnUiThread(new Runnable() {
				public void run() {
					if (transDialog != null && transDialog.isShowing()) {
						transDialog.dismiss();
					}
					Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT).show();
				}
			});
	}
	
	public void refreshOrder(){
		if (UserUtil.judgeUserInfo()) {
			myList.setVisibility(View.VISIBLE);
			ivNoOrder.setVisibility(View.GONE);
			initData();
			initFrozenFee();
		} else {
			myLeaseOrders.clear();
			myList.setVisibility(View.GONE);
			ivNoOrder.setVisibility(View.VISIBLE);
		}
	}
}
