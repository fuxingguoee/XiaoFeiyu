package com.il360.xiaofeiyu.activity.mydata;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.BindingBankCardActivity;
import com.il360.xiaofeiyu.adapter.AssessBankAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.user.ArrayOfAssessBank;
import com.il360.xiaofeiyu.model.user.AssessBank;
import com.il360.xiaofeiyu.util.FastJsonUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@EActivity(R.layout.act_bank_list)
public class OrderBankListActivity extends BaseWidgetActivity {

	protected ProgressDialog transDialog;

	private List<AssessBank> list = new ArrayList<AssessBank>();
	private AssessBankAdapter adapter;

	@ViewById
	ListView bankList;

	@AfterViews
	void init() {
		transDialog = ProgressDialog.show(OrderBankListActivity.this, null, "请稍等...", true);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					initData();
				} catch (Exception e) {
					Log.e(TAG, "init", e);
					LogUmeng.reportError(OrderBankListActivity.this, e);
					showInfo(getString(R.string.A2));
				}
			}
		});
	}

	private void initData() {
		try {
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "credit/getAssessBank",
					null);
			if (result.getSuccess()) {
				ArrayOfAssessBank response = FastJsonUtils.getSingleBean(result.getResult().toString(),
						ArrayOfAssessBank.class);
				if (response.getCode() == 1) {
					if (response.getResult() != null && response.getResult().size() > 0) {
						list = response.getResult();
					} else {
						showInfo(getResources().getString(R.string.no_data));
					}
				} else {
					showInfo(response.getDesc());
				}
			} else {
				showInfo(result.getResult());
			}
		} catch (Exception e) {
			Log.e(TAG, "initData", e);
			LogUmeng.reportError(OrderBankListActivity.this, e);
			showInfo(getString(R.string.A7));
		} finally {
			runOnUiThread(new Runnable() {
				public void run() {

					adapter = new AssessBankAdapter(list, OrderBankListActivity.this);
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
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putString("bankName", list.get(position).getBankName());
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
				Toast.makeText(OrderBankListActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
