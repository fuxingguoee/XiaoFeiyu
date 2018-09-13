package com.il360.xiaofeiyu.activity.order;

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
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.adapter.OrderRenewAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.order.ArrayOfRenew;
import com.il360.xiaofeiyu.model.order.LeaseOrder;
import com.il360.xiaofeiyu.model.order.Renew;
import com.il360.xiaofeiyu.model.order.Stages;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_order_renew)
public class RenewActivity extends BaseWidgetActivity {
	
	@ViewById ListView renewList;
	@ViewById TextView tvToPay;
	
	private List<Stages> myRenew = new ArrayList<Stages>();
	private OrderRenewAdapter myAdapter;
	private ArrayOfRenew arrayOfRenew;
	
	private int myPosition = 0;
	private String stagesId;
	private String stagesNumber;
	
	protected ProgressDialog transDialog;
	
	Renew toPay;
	
	@Extra LeaseOrder leaseOrder;
	
	@AfterViews
	void init() {
		initViews();
	}
	
	private void initViews() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"lease/queryStages", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(RenewActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							arrayOfRenew = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfRenew.class);
							if (arrayOfRenew.getCode() == 1) {
								if(arrayOfRenew != null && arrayOfRenew.getResult() != null && arrayOfRenew.getResult().size() > 0){
									myRenew.clear();
									myRenew.addAll(arrayOfRenew.getResult());
								} else {
									showInfo("没有数据");
								}
							} else {
								showInfo(arrayOfRenew.getDesc());
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
				} finally {
					RenewActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
							stagesId = myRenew.get(0).getId() + "";
							stagesNumber = myRenew.get(0).getStagesNumber() + "";
							showList();
						}
					});
					}
			}
		});
		
	}
	
	protected void showList() {
		myAdapter = new OrderRenewAdapter(myRenew, RenewActivity.this, myPosition, leaseOrder);
		renewList.setAdapter(myAdapter);
		renewList.setOnItemClickListener(new myListClickListener());
		myAdapter.notifyDataSetChanged();
	}
	
	class myListClickListener implements android.widget.AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			myPosition = position;
			myAdapter = new OrderRenewAdapter(myRenew, RenewActivity.this, myPosition, leaseOrder);
			renewList.setAdapter(myAdapter);
			stagesId = myRenew.get(position).getId() + "";
			stagesNumber = myRenew.get(position).getStagesNumber() + "";
			myAdapter.notifyDataSetChanged();
		}
	}
	
	@Click
	void tvToPay(){
		ToSubmit();
	}
	
	private void ToSubmit() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("type", "1");
					params.put("stageId", stagesId);
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"lease/PostPayFeeOrder", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(RenewActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							final JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								Intent intent = new Intent(RenewActivity.this, BillActivity_.class);
								intent.putExtra("type", "1");
								intent.putExtra("stagesNumber", stagesNumber);
								intent.putExtra("leaseOrder", leaseOrder);
								startActivity(intent);
							} else {
								showInfo(obj.getString("desc"));
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("PlaceOrderActivity", "initOrder", e);
				} finally {
				}
			}
		});
		
	}

	public void showInfo(final String info) {
		this.runOnUiThread(new Runnable() {
				public void run() {
					if (transDialog != null && transDialog.isShowing()) {
						transDialog.dismiss();
					}
					Toast.makeText(RenewActivity.this, info, Toast.LENGTH_SHORT).show();
				}
			});
	}
	
}
