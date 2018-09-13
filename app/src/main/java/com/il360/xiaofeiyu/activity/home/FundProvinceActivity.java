package com.il360.xiaofeiyu.activity.home;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.adapter.FundProvinceAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.home.FundCityResult;
import com.il360.xiaofeiyu.model.home.FundProvince;
import com.il360.xiaofeiyu.util.FastJsonUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@EActivity(R.layout.act_fund_provice)
public class FundProvinceActivity extends BaseWidgetActivity {
	@ViewById
	ListView lvProvince;
	private List<FundProvince> list = new ArrayList<FundProvince>();
	private FundProvinceAdapter adapter;
	
	protected ProgressDialog transDialog;

	@AfterViews
	void init() {
		transDialog = ProgressDialog.show(FundProvinceActivity.this, null, "请稍等...", true);
		initData();
	}

	private void initData() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"credit/getGJJCity", null);
					if (result.getSuccess()) {
						FundCityResult fundCityResult = FastJsonUtils.getSingleBean(result.getResult().toString(),
								FundCityResult.class);
						if (fundCityResult.getCode() == 1) {
							if(fundCityResult.getResult() != null && fundCityResult.getResult().size() > 0){
								list = fundCityResult.getResult();
							} else {
								showInfo(getResources().getString(R.string.no_data));
							}
						} else {
							showInfo(fundCityResult.getDesc());
						}
					} else {
						showInfo(result.getResult());
					}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {

							adapter = new FundProvinceAdapter(list, FundProvinceActivity.this);
							lvProvince.setAdapter(adapter);
							lvProvince.setOnItemClickListener(new OnItemClickListener());

							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
						}
					});
				}
			}

		});
	}
	
	class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(list.get(position).getCityList() != null && list.get(position).getCityList().size() > 0){
				Intent intent = new Intent(FundProvinceActivity.this , FundCityActivity_.class);
				intent .putExtra("cityList", list.get(position));
				startActivityForResult(intent, 0);
			} else {
				showInfo(getResources().getString(R.string.no_data));
			}
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Variables.ADDRESS_STATUS_CODE_SECCESS){
			setResult(Variables.ADDRESS_STATUS_CODE_SECCESS,data);
			FundProvinceActivity.this.finish();
		}
	}
	
	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(FundProvinceActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
