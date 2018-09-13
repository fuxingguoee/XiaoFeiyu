package com.il360.xiaofeiyu.activity.recovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.adapter.PromoteAmountAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.recovery.ArrayOfPromoteAmount;
import com.il360.xiaofeiyu.model.recovery.PromoteAmount;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.ListViewForScrollView;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

@EActivity(R.layout.act_promote_amount)
public class PromoteAmountActivity extends BaseWidgetActivity {
	@ViewById PullToRefreshScrollView pull_refresh_scrollview;
	@ViewById ListViewForScrollView myList;
	
	/** 当前页码 **/
	private int pageNo = 1;
	/** 默认每页加载个数 **/
	private int pageSize = 20;
	private List<PromoteAmount> list = new ArrayList<PromoteAmount>();
	protected ProgressDialog transDialog;
	private PromoteAmountAdapter adapter;
	private ArrayOfPromoteAmount response;
	
	@AfterViews
	void init(){
		initRefreshListener();
		initViews();
		initData();
	}

	private void initViews() {
		adapter = new PromoteAmountAdapter(list, PromoteAmountActivity.this);
		myList.setAdapter(adapter);
//		myList.setOnItemClickListener(new OnItemClickListener());
		adapter.notifyDataSetChanged();
	}

	private void initRefreshListener() {
		pull_refresh_scrollview.setMode(Mode.BOTH);
		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				myList.postDelayed(new Runnable() {
					@Override
					public void run() {
						pageNo = 1;
						initData();
					}
				}, 1000);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (response != null && pageNo * pageSize >= response.getTotalCount()) {
					myList.postDelayed(new Runnable() {
						@Override
						public void run() {
							pull_refresh_scrollview.onRefreshComplete();
							showInfo("已经到底部了");
						}
					}, 1000);
				} else {
					pageNo++;
					initData();
				}
			}
		});
	}
	
	
	protected void initData() {
		transDialog = ProgressDialog.show(PromoteAmountActivity.this, null,"正在加载...", true);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("pageNo", pageNo + "");
					params.put("pageSize", pageSize + "");
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"user/queryPromoteAmount", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							JSONObject objRes = obj.getJSONObject("result");
							JSONObject objResRes = objRes.getJSONObject("returnResult");
							response = FastJsonUtils.getSingleBean(objResRes.toString(), ArrayOfPromoteAmount.class);
							if (response != null && response.getList()!= null) {
								if (pageNo == 1) {
									list.clear();
								}
								list.addAll(response.getList());
							}else{
								showInfo("暂无数据");
							}
						}else {
							showInfo(obj.getString("desc"));
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("MerchantPosListActivity", "initData", e);
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if(transDialog!=null && transDialog.isShowing()){
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
				if(transDialog!=null && transDialog.isShowing()){
					transDialog.dismiss();
				}
				Toast.makeText(PromoteAmountActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
