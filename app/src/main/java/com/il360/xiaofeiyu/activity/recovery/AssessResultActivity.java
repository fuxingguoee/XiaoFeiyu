package com.il360.xiaofeiyu.activity.recovery;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.hua.ArrayOfCardConfig;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.UserUtil;

import android.content.Intent;
import android.widget.TextView;

@EActivity(R.layout.act_assess_result)
public class AssessResultActivity extends BaseWidgetActivity {

	@ViewById
	TextView tvPhoneName;
	@ViewById
	TextView tvAmount;
	@ViewById
	TextView tvRestart;
	@ViewById
	TextView tvResult;
	@ViewById
	TextView tvAssess;
	
	private double ratio = 1;//默认原价
	private double amountDou = 0.00;
	private int newAmount = 0;

	DecimalFormat df = new DecimalFormat("0");
	
	@Extra
	String name, amount, details;
	@Extra
	Integer iphonetype;

	@AfterViews
	void init() {
		tvPhoneName.setText(name + "参考价格");
		amountDou = Double.valueOf(amount);
		tvResult.setText("评估详情：" + details);
		
		if(GlobalPara.getCardConfigList() != null && GlobalPara.getCardConfigList().size() > 0){
			for(int i = 0 ; i < GlobalPara.getCardConfigList().size(); i++){
				if(GlobalPara.getCardConfigList().get(i).getConfigGroup().equals("recovery") 
						&& GlobalPara.getCardConfigList().get(i).getConfigName().equals("ratio")){
					ratio = Double.valueOf(GlobalPara.getCardConfigList().get(i).getConfigValue());
				}
			} 
		} else {
			initConfig();
		}
		
		newAmount = (int)(amountDou * ratio + 0.5);
		tvAmount.setText(newAmount + "元");
	}
	
	private void initConfig() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"card/queryConfig", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							JSONObject objRes = obj.getJSONObject("result");
							JSONObject objRetRes = objRes.getJSONObject("returnResult");
							ArrayOfCardConfig arrayOfCardConfig = FastJsonUtils.getSingleBean(objRetRes.toString(),ArrayOfCardConfig.class);
							if (arrayOfCardConfig.getList() != null && arrayOfCardConfig.getList().size() > 0) {
								GlobalPara.cardConfigList = arrayOfCardConfig.getList();
								
								for(int i = 0 ; i < GlobalPara.getCardConfigList().size(); i++){
									if(GlobalPara.getCardConfigList().get(i).getConfigGroup().equals("recovery") 
											&& GlobalPara.getCardConfigList().get(i).getConfigName().equals("ratio")){
										ratio = Double.valueOf(GlobalPara.getCardConfigList().get(i).getConfigValue());
										newAmount = (int)(amountDou * ratio + 0.5);
									}
								} 
							}
						} 
					}
				} catch (Exception e) {
				}finally {
					runOnUiThread(new Runnable() {
						public void run() {
							tvAmount.setText(newAmount + "元");
						}
					});
				}
			}
		});
	}

	@Click
	void tvRestart() {
		AssessResultActivity.this.finish();
	}

	@Click
	void tvAssess() {
		if (UserUtil.judgeUserInfo()) {
			Intent intent = new Intent(AssessResultActivity.this, ReclaimTypeActivity_.class);
			intent.putExtra("name", name);
			intent.putExtra("amount", newAmount + "");
			intent.putExtra("details", details);
			intent.putExtra("iphonetype", iphonetype);
			startActivity(intent);
		} else {
			Intent intent = new Intent(AssessResultActivity.this, LoginActivity_.class);
			startActivity(intent);
		}
	}

}
