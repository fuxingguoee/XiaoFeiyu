package com.il360.xiaofeiyu.activity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.adapter.OrderRecordAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.order.ArrayOfRecordOrder;
import com.il360.xiaofeiyu.model.order.RecordOrder;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.ListViewForScrollView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.Toast;

@EActivity(R.layout.act_order_record)
public class RecordActivity extends BaseWidgetActivity {
	
	@ViewById PullToRefreshScrollView pull_refresh_scrollview;
	
	@ViewById ListViewForScrollView myList;
	
	
	private List<RecordOrder> myRecordOrders = new ArrayList<RecordOrder>();
	private OrderRecordAdapter myAdapter;
	
	/** 当前页码 **/
	private int pageNo = 1;
	/** 默认每页加载个数 **/
	private int pageSize = 20;
	
	private ArrayOfRecordOrder arrayOfRecordOrder;
	protected ProgressDialog transDialog;
	
	@AfterViews
	void init() {
		pull_refresh_scrollview.setMode(Mode.PULL_FROM_START);
		initRefreshListener();
		initData();
	}
	
	private void initData() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("pageNo", pageNo + "");
					params.put("pageSize", pageSize + "");
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"lease/queryUnionPayList", params);
					if (result.getSuccess()) {
						myRecordOrders.clear();
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(RecordActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							arrayOfRecordOrder = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfRecordOrder.class);
							if (arrayOfRecordOrder.getCode() == 1) {
								if(arrayOfRecordOrder != null && arrayOfRecordOrder.getResult() != null 
										&& arrayOfRecordOrder.getResult().size() > 0){
									if (pageNo == 1) {
										myRecordOrders.clear();
									}
									myRecordOrders.addAll(arrayOfRecordOrder.getResult());
								}
							} else {
								showInfo(arrayOfRecordOrder.getDesc());
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
				} finally {
					RecordActivity.this.runOnUiThread(new Runnable() {
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
		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				myList.postDelayed(new Runnable() {
					@Override
					public void run() {
						pageNo = 1;
						initData();
					}
				}, 500);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (isLast()) {
					myList.postDelayed(new Runnable() {
						@Override
						public void run() {
							pull_refresh_scrollview.onRefreshComplete();
							showInfo("已经到底部了！");
						}
					}, 1000);
				} else {
					pageNo++;
					initData();
				}
			}
		});
	}
	
	protected boolean isLast() {
		if (arrayOfRecordOrder != null) {
			return false;
		} else {
			return true;
		}
	}
	
	protected void showList() {
		myAdapter = new OrderRecordAdapter(myRecordOrders, RecordActivity.this);
		myList.setAdapter(myAdapter);
		myList.setOnItemClickListener(new myListClickListener());
	}
	
	class myListClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			Intent intent = new Intent(RecordActivity.this,RecordDetailsActivity_.class);
//			intent.putExtra("recordOrder", myRecordOrders.get(position));
//			startActivity(intent);
		}
	}
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
					Toast.makeText(RecordActivity.this, info, Toast.LENGTH_SHORT).show();
				}
			});
	}
	
}
