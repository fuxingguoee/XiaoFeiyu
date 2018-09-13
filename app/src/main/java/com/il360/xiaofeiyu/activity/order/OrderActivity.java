package com.il360.xiaofeiyu.activity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.adapter.OrderBuyAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.order.ArrayOfLeaseOrder;
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

@EActivity(R.layout.act_order_all)
public class OrderActivity extends BaseWidgetActivity {
	
	@ViewById PullToRefreshScrollView pull_refresh_scrollview;
	
	@ViewById ListViewForScrollView myList;
	
	@ViewById ImageView ivNoOrder;
	
	private List<LeaseOrder> myLeaseOrders = new ArrayList<LeaseOrder>();
	private OrderBuyAdapter myAdapter;
	
	private ArrayOfLeaseOrder arrayOfLeaseOrder;
	protected ProgressDialog transDialog;
	
	@Extra int i ;
	
	@Override
	protected void onResume() {
		super.onResume();
		if (i == 2000) {
			initData();
		}
	}
	
	@AfterViews
	void init() {
		pull_refresh_scrollview.setMode(Mode.PULL_FROM_START);
		initRefreshListener();
		initFrozenFee();
		initData();
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
							Intent intent = new Intent(OrderActivity.this, LoginActivity_.class);
							startActivity(intent);

						} else {
//							JSONObject obj = new JSONObject(result.getResult());

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
							Intent intent = new Intent(OrderActivity.this, LoginActivity_.class);
							startActivity(intent);
							
						} else {
//							JSONObject obj = new JSONObject(result.getResult());
							arrayOfLeaseOrder = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfLeaseOrder.class);
							if (arrayOfLeaseOrder.getCode() == 1) {
								if(arrayOfLeaseOrder != null && arrayOfLeaseOrder.getResult() != null && arrayOfLeaseOrder.getResult().size() > 0){
									myLeaseOrders.addAll(arrayOfLeaseOrder.getResult());
								}
							} else {
								showInfo(arrayOfLeaseOrder.getDesc());
								ivNoOrder.setVisibility(View.VISIBLE);
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
				} finally {
					OrderActivity.this.runOnUiThread(new Runnable() {
							public void run() {

								if (transDialog != null && transDialog.isShowing()) {
									transDialog.dismiss();
								}
								showList();
								myAdapter.notifyDataSetChanged();
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
					}
				}, 500);
			}
		});
	}
	
	protected void showList() {
//		myAdapter = new OrderBuyAdapter(myLeaseOrders, OrderActivity.this);
//		myList.setAdapter(myAdapter);
//		myList.setOnItemClickListener(new myListClickListener());
	}
	
	class myListClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(OrderActivity.this,OrderDetailsActivity_.class);
			intent.putExtra("leaseOrder", myLeaseOrders.get(position));
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
		this.runOnUiThread(new Runnable() {
				public void run() {
					if (transDialog != null && transDialog.isShowing()) {
						transDialog.dismiss();
					}
					Toast.makeText(OrderActivity.this, info, Toast.LENGTH_SHORT).show();
				}
			});
	}
	
}
