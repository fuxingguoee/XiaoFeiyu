package com.il360.xiaofeiyu.activity.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.adapter.PayBackAdapter;
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
import android.util.Log;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

@EActivity(R.layout.act_pay_back)
public class PayBackActivity extends BaseWidgetActivity {

	@ViewById
	PullToRefreshScrollView pull_refresh_scrollview;

	@ViewById
	ListView myList;

	private PayBackAdapter adapter;
	private List<OrderPeriodsExt> list = new ArrayList<OrderPeriodsExt>();
	protected ProgressDialog transDialog;

	/** 当前页码 **/
	private int pageNo = 1;
	/** 默认每页加载个数 **/
	private int pageSize = 20;

	private ArrayOfOrderPeriodsExt arrayOfOrderPeriodsExt;

	@Extra
	String status, orderNo;

	@AfterViews
	void init() {
		initViews();
		initData();
		initRefreshListener();
	}

	private void initViews() {
		adapter = new PayBackAdapter(list, PayBackActivity.this);
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
		transDialog = ProgressDialog.show(PayBackActivity.this, null, "加载中...", true);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("pageNo", pageNo + "");
					params.put("pageSize", pageSize + "");
					params.put("token", UserUtil.getToken());
					params.put("status", status);
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
					Log.e("PayBackActivity", "initData", e);
					LogUmeng.reportError(PayBackActivity.this, e);
				} finally {

					runOnUiThread(new Runnable() {
						public void run() {

							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
							adapter.notifyDataSetChanged();
							pull_refresh_scrollview.onRefreshComplete();
						}
					});

				}
			}
		});
	}

	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(PayBackActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
