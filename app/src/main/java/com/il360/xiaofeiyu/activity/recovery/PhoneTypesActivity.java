package com.il360.xiaofeiyu.activity.recovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.adapter.PhoneAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.recovery.ArrayOfCreditAmount;
import com.il360.xiaofeiyu.model.recovery.CreditAmount;
import com.il360.xiaofeiyu.util.FastJsonUtils;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@EActivity(R.layout.act_phone_types)
public class PhoneTypesActivity extends BaseWidgetActivity {

	@ViewById
	ListView phoneList;
	
	private PhoneAdapter adapter;
	private List<CreditAmount> list = new ArrayList<CreditAmount>();

	@AfterViews
	void init() {
		initViews();
		initdata();
	}
	
	private void initdata() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"usercredit/queryCreditAmount", params);
					if (result.getSuccess()) {
						ArrayOfCreditAmount arrayOfCreditAmount = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfCreditAmount.class);
						if (arrayOfCreditAmount.getCode() == 1) {
							if (arrayOfCreditAmount != null && arrayOfCreditAmount.getResult() != null && arrayOfCreditAmount.getResult().size() > 0) {
								list.clear();
								list.addAll(arrayOfCreditAmount.getResult());
							} else {
								showInfo("暂无数据");
							}
						} else {
							showInfo(arrayOfCreditAmount.getDesc());
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("PhoneTypesActivity", "initData", e);
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							 adapter.notifyDataSetChanged();
						}
					});
				}
			}
		});
	}

	private void initViews() {
		adapter = new PhoneAdapter(list, PhoneTypesActivity.this);
		phoneList.setAdapter(adapter);
		phoneList.setOnItemClickListener(new OnItemClickListener());
		adapter.notifyDataSetChanged();
	}
	
	class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(PhoneTypesActivity.this, PhoneAssessActivity_.class);
			intent.putExtra("creditAmount", list.get(position));
			startActivity(intent);
		}
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(PhoneTypesActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
