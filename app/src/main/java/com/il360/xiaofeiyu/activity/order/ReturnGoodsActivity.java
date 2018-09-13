package com.il360.xiaofeiyu.activity.order;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.recovery.ExpressListActivity_;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.order.OrderRecovery;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.SIMCardUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_return_goods)
public class ReturnGoodsActivity extends BaseWidgetActivity {

	@ViewById TextView tvOrderNo;
	@ViewById EditText etExpressNo;
	@ViewById TextView tvExpressCompany;
	@ViewById EditText etUserPhone;
	@ViewById EditText etReturnReason;
	@ViewById TextView tvSubmit;
	@ViewById
	RelativeLayout rlExpressCompany;
	
	@Extra String orderNo;
	
	private String expressCompany;
	
	protected ProgressDialog transDialog;
	
	@AfterViews
	void init() {
		if(orderNo != null){
			tvOrderNo.setText(orderNo);
			etUserPhone.setText(UserUtil.getUserInfo().getLoginName() != null ? UserUtil.getUserInfo().getLoginName() : "");
		} else {
			showInfo("内部传参错误！");
		}
	}
	
	@Click
	void rlExpressCompany(){
		Intent intent = new Intent();
		intent.setClass(this, ExpressListActivity_.class);
		startActivityForResult(intent, 0);
	}
	
	@Click
	void tvSubmit() {
		if(isOk()){
			tvSubmit.setClickable(false);
			transDialog = ProgressDialog.show(ReturnGoodsActivity.this, null, "提交中...", true);
			initReturnGoods();
		}
	}
	
	private void initReturnGoods() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("orderRecoveryJson", makeJson());
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"order/applyReturn", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							showInfo("提交成功！");
							setResult(101);
							ReturnGoodsActivity.this.finish();
						} else {
							showInfo(obj.getString("desc"));
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					Log.e("ReturnGoodsActivity", "initReturnGoods()", e);
					LogUmeng.reportError(ReturnGoodsActivity.this, e);
					showInfo(getString(R.string.A2));
				}finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
							tvSubmit.setClickable(true);
						}
					});
				}
			}
		});
	}
	
	private String makeJson(){
		OrderRecovery orderRecovery = new OrderRecovery();
		orderRecovery.setExpressName(ViewUtil.getText(tvExpressCompany));
		orderRecovery.setExpressNo(ViewUtil.getText(etExpressNo));
		orderRecovery.setOrderNo(orderNo);
		orderRecovery.setPhone(ViewUtil.getText(etUserPhone));
		orderRecovery.setRecoveryDesc(ViewUtil.getText(etReturnReason));
		return FastJsonUtils.getJsonString(orderRecovery);
	}

	private boolean isOk() {
		if(TextUtils.isEmpty(ViewUtil.getText(etExpressNo))){
			showInfo("请输入快递单号");
		} else if(TextUtils.isEmpty(ViewUtil.getText(etUserPhone))){
			showInfo("请输入您的手机号码");
		} else if(!SIMCardUtil.isMobileNo(ViewUtil.getText(etUserPhone))){
			showInfo("您输入的手机号码格式有误");
		} else if(TextUtils.isEmpty(ViewUtil.getText(tvExpressCompany))){
			showInfo("请选择快递公司名称");
		} else if(TextUtils.isEmpty(ViewUtil.getText(etReturnReason))){
			showInfo("请输入您的退货理由");
		} else {
			return true;
		}
		return false;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 101) {
			Bundle b = data.getExtras();
			if(b != null){
				expressCompany = b.getString("expressName");
				tvExpressCompany.setText(expressCompany);
			}
		} 
	}
	
	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(ReturnGoodsActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
