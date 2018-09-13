package com.il360.xiaofeiyu.activity.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.adapter.BankAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.user.Bank;
import com.il360.xiaofeiyu.model.user.OutPaginationBank;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.UserUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@EActivity(R.layout.act_bank_list)
public class BankNameListActivity extends BaseWidgetActivity {
	private Context context = this;
	private List<Bank> list = new ArrayList<Bank>();
	private List<Bank> list1 = new ArrayList<Bank>();
	protected ProgressDialog transDialog;
	private BankAdapter adapter;

	private String bankname = null;

	@ViewById
	ListView bankList;

	@AfterViews
	void init() {
		transDialog = ProgressDialog.show(BankNameListActivity.this, null, "请稍等...", true);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					initData();
				} catch (Exception e) {
					Log.e(TAG, "init", e);
					LogUmeng.reportError(BankNameListActivity.this, e);
					showInfo(getString(R.string.A7));
				}
			}
		});
	}

	private void initData() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/queryBankList",
					params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					JSONObject objRes = obj.getJSONObject("result");
					JSONObject objReslt = objRes.getJSONObject("returnResult");
					OutPaginationBank response = FastJsonUtils.getSingleBean(objReslt.toString(), OutPaginationBank.class);
					if (response.getList() != null && response.getList().size() > 0) {
						list1 = response.getList();
						for (int i = 0; i < list1.size(); i++) {
							if (list1.get(i).getBankName() != null && list1.get(i).getBankName().length() > 0) {
								list.add(list1.get(i));
							}
						}
					} else {
						showInfo(getResources().getString(R.string.no_data));
					}
				}else{
					showInfo(obj.getString("desc"));
				}
			} else {
				showInfo(result.getResult());
			}
		} catch (Exception e) {
			Log.e(TAG, "initData", e);
			LogUmeng.reportError(BankNameListActivity.this, e);
			showInfo(getString(R.string.A7));
		} finally {
			runOnUiThread(new Runnable() {
				public void run() {

					adapter = new BankAdapter(list, context);
					bankList.setAdapter(adapter);
					bankList.setOnItemClickListener(new OnItemClickListener());

					if (transDialog != null && transDialog.isShowing()) {
						transDialog.dismiss();
					}
				}
			});
		}
	}

	class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			bankname = list.get(position).getBankName();
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putString("bankname", bankname);
			intent.putExtras(b);
			setResult(BindingBankCardActivity.BANK_CODE_SECCESS, intent);
			finish();
		}

	}

	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(BankNameListActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
