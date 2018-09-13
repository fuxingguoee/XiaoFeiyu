package com.il360.xiaofeiyu.activity.order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.home.PayActivity_;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.adapter.NotPayBackAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.order.ArrayOfOrderPeriodsExt;
import com.il360.xiaofeiyu.model.order.OrderPeriodsExt;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.UserUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

@EActivity(R.layout.act_not_pay_back)
public class NotPayBackActivity extends BaseWidgetActivity {
	@ViewById
	PullToRefreshScrollView pull_refresh_scrollview;

	@ViewById
	ListView myList;
	
	@ViewById LinearLayout llAllPayBack;

	private NotPayBackAdapter adapter;
	private List<OrderPeriodsExt> list = new ArrayList<OrderPeriodsExt>();
	protected ProgressDialog transDialog;
	
	DecimalFormat df = new DecimalFormat("0.00");

	/** 当前页码 **/
	private int pageNo = 1;
	/** 默认每页加载个数 **/
	private int pageSize = 100;

	private ArrayOfOrderPeriodsExt arrayOfOrderPeriodsExt;
	private double allMoney = 0.00;

	@Extra
	String status, orderNo;

	@AfterViews
	void init() {
		initViews();
		initData();
		initRefreshListener();
	}

	private void initViews() {
		adapter = new NotPayBackAdapter(list, NotPayBackActivity.this);
		myList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
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
		if (arrayOfOrderPeriodsExt != null && pageNo * pageSize < arrayOfOrderPeriodsExt.getTotalCount()) {
			return false;
		} else {
			return true;
		}
	}

	private void initData() {
		transDialog = ProgressDialog.show(NotPayBackActivity.this, null, "加载中...", true);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("pageNo", pageNo + "");
					params.put("pageSize", pageSize + "");
					params.put("token", UserUtil.getToken());
					params.put("orderNo", orderNo);
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"order/queryOrderPeriodsExt", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							JSONObject objRes = obj.getJSONObject("result");
							JSONObject objResult = objRes.getJSONObject("returnResult");
							arrayOfOrderPeriodsExt = FastJsonUtils.getSingleBean(objResult.toString(),
									ArrayOfOrderPeriodsExt.class);
							if (arrayOfOrderPeriodsExt != null) {
								if (pageNo == 1) {
									list.clear();
									allMoney = 0.00;
								}
								list.addAll(arrayOfOrderPeriodsExt.getList());
							}
						} else {
							showInfo(obj.getString("desc"));
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					Log.e("NotPayBackActivity", "initData", e);
					LogUmeng.reportError(NotPayBackActivity.this, e);
				} finally {

					runOnUiThread(new Runnable() {
						public void run() {

							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
							
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i).getStatus() == 0) {
									allMoney = allMoney + list.get(i).getAmount().doubleValue() + list.get(i).getFee().doubleValue() + list.get(i).getOverdueFee().doubleValue();
								}
							}
							
							adapter.notifyDataSetChanged();
							pull_refresh_scrollview.onRefreshComplete();
						}
					});

				}
			}
		});
	}
	
	@Click
	void llAllPayBack() {
		Intent intent = new Intent(NotPayBackActivity.this,PayActivity_.class);
		intent.putExtra("number", "0");
		intent.putExtra("orderNo", orderNo);
		intent.putExtra("buyType", "3");
		intent.putExtra("money", df.format(allMoney));
		startActivity(intent);
	}

	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(NotPayBackActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
