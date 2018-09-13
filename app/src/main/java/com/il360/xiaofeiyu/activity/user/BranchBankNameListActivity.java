package com.il360.xiaofeiyu.activity.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.adapter.BranchBankAdapter;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

@EActivity(R.layout.act_bank_list)
public class BranchBankNameListActivity extends BaseWidgetActivity{
	private Context context = this;
	private List<Bank> list = new ArrayList<Bank>();
	protected ProgressDialog transDialog;
	private BranchBankAdapter adapter;	
	
	private String bankbranchname=null;
	private String bankbranchno=null;
	private String bankCode = null;
	
	@ViewById ListView bankList;
	
	/** 传递过来的参数 */
	@Extra String bankname;
	@Extra String searchcondition;
	
	@AfterViews
	void init() {
		transDialog = ProgressDialog.show(BranchBankNameListActivity.this, null, "请稍等...", true);
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					initData();
				} catch (Exception e) {
					LogUmeng.reportError(BranchBankNameListActivity.this, e);
					showInfo(getString(R.string.A7));
				}
			}
		});
	}
	private void initData() {		
		Map<String, String> params;
		try {
			params = new HashMap<String, String>();
			params.put("bankName", bankname);
			params.put("branchName", searchcondition);
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/findBank",
					params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					JSONObject objRes = obj.getJSONObject("result");
					JSONObject objReslt = objRes.getJSONObject("returnResult");
					OutPaginationBank response = FastJsonUtils.getSingleBean(objReslt.toString(), OutPaginationBank.class);
					if (response.getList() != null && response.getList().size() > 0) {
						list = response.getList();
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
			LogUmeng.reportError(BranchBankNameListActivity.this, e);
			showInfo(getString(R.string.A7));
		} finally {
			runOnUiThread(new Runnable() {
				public void run() {

					adapter = new BranchBankAdapter(list, context);
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
			bankbranchname = list.get(position).getBranchName();
			bankbranchno = list.get(position).getBankNo();
			bankCode = list.get(position).getBankCode();
			Intent intent = new Intent();
			Bundle b = new Bundle();
			b.putString("bankbranchname", bankbranchname);
			b.putString("bankbranchno", bankbranchno);
			b.putString("bankCode", bankCode);
			intent.putExtras(b);
			setResult(BindingBankCardActivity.BANK_BRANCH_CODE_SECCESS, intent);
			finish();
		}

	}

	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(BranchBankNameListActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
