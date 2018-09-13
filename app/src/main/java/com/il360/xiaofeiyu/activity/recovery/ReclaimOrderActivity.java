package com.il360.xiaofeiyu.activity.recovery;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.main.UrlToWebActivity_;
import com.il360.xiaofeiyu.activity.user.MyBankCardActivity_;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.asynccache.ImageLoader;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.hua.OutUserBank;
import com.il360.xiaofeiyu.model.recovery.UserRecovery;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.NumReplaceUtil;
import com.il360.xiaofeiyu.util.SIMCardUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_reclaim_order)
public class ReclaimOrderActivity extends BaseWidgetActivity {
	
	@ViewById
	TextView tvPhoneName;
	@ViewById 
	TextView tvPhoneAmount;
	@ViewById
	TextView tvAssessDetails;
	
	@ViewById
	EditText etName;
	@ViewById
	EditText etPhone;
	
	@ViewById
	RelativeLayout rlBank;
	@ViewById
	ImageView ivBank;
	@ViewById
	TextView tvBankName;
	@ViewById
	TextView tvCardNo;
	@ViewById
	TextView tvCardStatus;
	
	@ViewById
	CheckBox cbAgree;
	@ViewById
	TextView tvRule;
	@ViewById
	TextView tvSubmit;
	

	@Extra
	String name, amount, details;
	@Extra 
	Integer iphonetype;
	
	private BigDecimal assessAmount;
	
	@AfterViews
	void init() {
		initViews();
		initBankCard();
	}
	
	private void initViews() {
		tvRule.setText("《" + ReclaimOrderActivity.this.getResources().getString(R.string.recovery_rule) + "》");
		etName.setText(UserUtil.getUserInfo().getUserName());
		etPhone.setText(UserUtil.getUserInfo().getLoginName());
		assessAmount = new BigDecimal(amount);
		assessAmount = assessAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
		tvPhoneName.setText(name);
		tvPhoneAmount.setText("￥"+ assessAmount);
		tvAssessDetails.setText(details);
	}

	private void initBankCard(){
		if(GlobalPara.getOutUserBank() != null && GlobalPara.getOutUserBank().getBankName() != null && GlobalPara.getOutUserBank().getStatus() != null){
			try {
				ImageLoader.getInstances().DisplayImage(
						Variables.CARG_BANK_PICTURE_URL + URLEncoder.encode(GlobalPara.getOutUserBank().getBankName(),"utf-8")+".png", ivBank);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			tvBankName.setText(GlobalPara.getOutUserBank().getBankName());
			tvCardNo.setText("尾号" + NumReplaceUtil.lastNum(GlobalPara.getOutUserBank().getBankNo(), 4));
			
			if(GlobalPara.getOutUserBank().getStatus() == 1){
				tvCardStatus.setText("认证通过");
				tvCardStatus.setTextColor(ContextCompat.getColor(ReclaimOrderActivity.this, R.color.text_green));
			} else if(GlobalPara.getOutUserBank().getStatus() == -1) {
				tvCardStatus.setText("未认证通过");
				tvCardStatus.setTextColor(ContextCompat.getColor(ReclaimOrderActivity.this, R.color.red));
			} else {
				tvCardStatus.setText("正在认证中");
				tvCardStatus.setTextColor(ContextCompat.getColor(ReclaimOrderActivity.this, R.color.red));
			}
		} else {
			tvBankName.setText("");
			tvCardNo.setText("");
			tvCardStatus.setText("未绑定银行卡");
			tvCardStatus.setTextColor(ContextCompat.getColor(ReclaimOrderActivity.this, R.color.red));
		}
	}
	
	@Click
	void rlBank() {
		if (UserUtil.judgeAuthentication()) {
			Intent intent = new Intent(ReclaimOrderActivity.this, MyBankCardActivity_.class);
			startActivityForResult(intent, 1);
		} else {
			showInfo(getResources().getString(R.string.H2));
		}
	}
	
	@Click
	void tvRule() {
		Intent intent = new Intent(ReclaimOrderActivity.this, UrlToWebActivity_.class);
		intent.putExtra("title", ReclaimOrderActivity.this.getResources().getString(R.string.recovery_rule));
		intent.putExtra("supportZoom", false);
		intent.putExtra("url", "http://www.ycaomei.com/cmfq/recycling-agreement.html");
		startActivity(intent);
	}
	
	@Click
	void tvSubmit() {
		if(isOk()){
			initSubmit();
		}
	}

	private void initSubmit() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("userRecoveryJson", makeJsonPost());
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "recovery/applyRecovery", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if(obj.getInt("code") == 1){
							Intent intent = new Intent(ReclaimOrderActivity.this,LogisticsInfoActivity_.class);
							intent.putExtra("orderNo", obj.getString("result"));
							startActivity(intent);
						} else {
							showInfo(obj.getString("desc"));
						}
					} else {
						showInfo(result.getResult());
					}
				} catch (Exception e) {
					showInfo("网络请求失败");
					Log.e("ReclaimOrderActivity", "initSubmit", e);
					LogUmeng.reportError(ReclaimOrderActivity.this, e);
				}
			}
		});
	}
	
	private String makeJsonPost() {
		UserRecovery userRecovery = new UserRecovery();
		userRecovery.setUserId(UserUtil.getUserInfo().getUserId());
		userRecovery.setAssessmentDetails(details);
		userRecovery.setPhoneName(name);
		userRecovery.setPhone(ViewUtil.getText(etPhone));
		userRecovery.setRecoveryType("邮寄回收");
		userRecovery.setAmount(assessAmount);
		userRecovery.setIphonetype(iphonetype);
		return FastJsonUtils.getJsonString(userRecovery);
	}

	private boolean isOk() {
		if(TextUtils.isEmpty(ViewUtil.getText(etName))){
			showInfo("请输入寄件人姓名");
		} else if(TextUtils.isEmpty(ViewUtil.getText(etPhone))){
			showInfo("请输入寄件人手机号码");
		} else if(!SIMCardUtil.isMobileNo(ViewUtil.getText(etPhone))){
			showInfo("输入的寄件人手机号码格式有误");
		} else if(!(GlobalPara.getOutUserBank() != null && GlobalPara.getOutUserBank().getStatus() == 1)){
			showInfo("请先通过到账银行卡认证");
		} else if(!cbAgree.isChecked()){
			showInfo("请先阅读并同意《" + ReclaimOrderActivity.this.getResources().getString(R.string.recovery_rule) + "》");
		} else {
			return true;
		}
		return false;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			initData();
		} 
	}
	
	private void initData() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/queryCard",params);
					if (!result.getSuccess()) {
						showInfo(result.getResult());
						return;
					}
					JSONObject obj = new JSONObject(result.getResult());
					if (obj.getInt("code") == 1) {
						JSONObject objRes = obj.getJSONObject("result");
						GlobalPara.outUserBank = FastJsonUtils.getSingleBean(objRes.toString(), OutUserBank.class);
					} else {
						GlobalPara.outUserBank = null;
					}
				} catch (Exception e) {
					Log.e("ReclaimOrderActivity", "initData", e);
					LogUmeng.reportError(ReclaimOrderActivity.this, e);
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							initBankCard();
						}
					});
				}
			}
		});
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(ReclaimOrderActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
