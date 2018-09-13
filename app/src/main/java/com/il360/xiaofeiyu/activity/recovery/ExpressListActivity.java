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
import com.il360.xiaofeiyu.adapter.ExpressAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.recovery.ArrayOfExpress;
import com.il360.xiaofeiyu.model.recovery.Express;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.UserUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@EActivity(R.layout.act_express_list)
public class ExpressListActivity extends BaseWidgetActivity {
	@ViewById
	ListView lvExpress;
	private List<Express> list = new ArrayList<Express>();
	protected ProgressDialog transDialog;
	private ExpressAdapter adapter;
	
	private String expressName;
	
	
	@AfterViews
	void init(){
		initExpressList();
	}

	private void initExpressList() {
		transDialog = ProgressDialog.show(ExpressListActivity.this, null, "请稍等...", true);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				Map<String, String> params;
				try {
					params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/queryExpressList",
							params);
					if (result.getSuccess()) {
						ArrayOfExpress response = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfExpress.class);
						if (response.getCode() == 1) {
							if(response.getResult() != null && response.getResult().size() > 0){
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
					LogUmeng.reportError(ExpressListActivity.this, e);
					showInfo(getString(R.string.A7));
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {

							adapter = new ExpressAdapter(list, ExpressListActivity.this);
							lvExpress.setAdapter(adapter);
							lvExpress.setOnItemClickListener(new OnItemClickListener());

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
			expressName = list.get(position).getExpress();
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putString("expressName", expressName);
			intent.putExtras(b);
			setResult(101, intent);
			finish();
		}
	}
	
	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(ExpressListActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
