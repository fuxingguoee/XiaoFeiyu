package com.il360.xiaofeiyu.activity.recovery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.adapter.AssessAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.recovery.ArrayOfAssessTitle;
import com.il360.xiaofeiyu.model.recovery.AssessTitle;
import com.il360.xiaofeiyu.model.recovery.CreditAmount;
import com.il360.xiaofeiyu.util.FastJsonUtils;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_phone_assess)
public class PhoneAssessActivity extends BaseWidgetActivity {

	@ViewById TextView tvPhoneName;
	@ViewById TextView tvTimes;
	
	@ViewById TextView tvRestart;
	@ViewById TextView tvResult;
	
	@ViewById ListView assessList;
	private AssessAdapter adapter;
	private List<AssessTitle> list = new ArrayList<AssessTitle>();
	
	private int checked = 0;
	private int allCheck = 0;
	
	private ArrayOfAssessTitle response;
	
	private int initialAmount;//初始金额
	private int minimumAmount;//最低金额
	private int lastAmount;//最终金额
	private String assessDetails = "";
	
	@Extra CreditAmount creditAmount;
	
	@AfterViews
	void init() {
		tvPhoneName.setText(creditAmount.getPhoneGeneration());
		initialAmount = creditAmount.getAmount();
		minimumAmount = initialAmount;
		initData();
	}

	private void initData() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("iphonetype", creditAmount.getIphonetype() + "");
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"recovery/queryPhoneValuation", params);
					if (result.getSuccess()) {
						response = FastJsonUtils.getSingleBean(result.getResult(),ArrayOfAssessTitle.class);
						if (response.getCode() == 1) {
							if(response.getResult() != null && response.getResult().size() > 0){
								list.clear();
								list.addAll(response.getResult());
								for (int i = 0; i < list.size(); i++) {
									if (list.get(i).getType() == 0) {
										allCheck = allCheck + 1;
									}
								}
							} else {
								showInfo("暂无数据");
							}
						} else {
							showInfo(response.getDesc());
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("PhoneAssessActivity", "initData", e);
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							tvTimes.setText(checked + "/" + allCheck);
							adapter = new AssessAdapter(list, PhoneAssessActivity.this,new ListCallback());
							assessList.setAdapter(adapter);
							adapter.notifyDataSetChanged();
//							pull_refresh_scrollview.onRefreshComplete();
						}
					});
				}
			}
		});
	}
	
	//Handler类
	private Handler handler = new Handler();

	public class ListCallback {

		public void refreshAdapter( int i,  int j, boolean isChecked) {
			// Handler类
			list.get(i).getList().get(0).setChoicePosition(j);
			if(!isChecked){
				checked = checked + 1;
				tvTimes.setText(checked + "/" + allCheck);
			}
			adapter.notifyDataSetChanged(assessList, i);
			assessList.smoothScrollToPositionFromTop(i + 1, 0);
//			handler.post(runnableUi);
		}
	}

	// 构建Runnable对象，在runnable中更新界面
	Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			// 更新界面
			adapter.notifyDataSetChanged();
		}
	};
	
	@Click
	void tvRestart() {
		checked = 0;
		tvTimes.setText(checked + "/" + allCheck);
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getType() == 1){
				for(int j = 0 ; j < list.get(i).getList().size();j++){
					list.get(i).getList().get(j).setCheck(false);
				}
			} else {
				list.get(i).getList().get(0).setChoicePosition(-1);
			}
		}
		assessList.smoothScrollToPositionFromTop(0, 0);
		adapter.notifyDataSetChanged();
	}
	
	@Click
	void tvResult() {
		
		initialAmount = creditAmount.getAmount();
		minimumAmount = initialAmount;
		assessDetails = "";
		
		if(allCheck > 0 && checked != allCheck){
			showInfo("还有选项未填选哦！");
		} else {
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).getType() == 1){
					for(int j = 0 ; j < list.get(i).getList().size();j++){
						if(list.get(i).getList().get(j).isCheck() != null && list.get(i).getList().get(j).isCheck()){
							initialAmount = initialAmount - list.get(i).getList().get(j).getAmount();
							if(list.get(i).getList().get(j).getLowAmount() != null && list.get(i).getList().get(j).getLowAmount() > 0
									&& list.get(i).getList().get(j).getLowAmount() < minimumAmount){
								minimumAmount = list.get(i).getList().get(j).getLowAmount();
							}
							assessDetails = assessDetails + "| " + list.get(i).getList().get(j).getAnswerDesc() + " ";
						}
					}
				} else {
					initialAmount = initialAmount - list.get(i).getList().get(list.get(i).getList().get(0).getChoicePosition()).getAmount();
					if(list.get(i).getList().get(list.get(i).getList().get(0).getChoicePosition()).getLowAmount() != null && 
							list.get(i).getList().get(list.get(i).getList().get(0).getChoicePosition()).getLowAmount() > 0 &&
							list.get(i).getList().get(list.get(i).getList().get(0).getChoicePosition()).getLowAmount() < minimumAmount){
						minimumAmount = list.get(i).getList().get(list.get(i).getList().get(0).getChoicePosition()).getLowAmount();
					}
					assessDetails = assessDetails + "| " + list.get(i).getList().get(list.get(i).getList().get(0).getChoicePosition()).getAnswerDesc() + " ";
				}
			}
			
			assessDetails = assessDetails.substring(1);
			lastAmount = initialAmount < minimumAmount ? initialAmount : minimumAmount;
			
			Intent intent = new Intent(PhoneAssessActivity.this, AssessResultActivity_.class);
			intent.putExtra("name", creditAmount.getPhoneGeneration());
			intent.putExtra("amount", (lastAmount > 0 ? lastAmount : 0) + "");
			intent.putExtra("details", assessDetails);
			intent.putExtra("iphonetype", creditAmount.getIphonetype());
			startActivity(intent);
		}
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(PhoneAssessActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
