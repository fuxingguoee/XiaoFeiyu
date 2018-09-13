package com.il360.xiaofeiyu.activity.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.address.ProvinceActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.address.UserAddress;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.SIMCardUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_address_modify)
public class AddressModifyActivity extends BaseWidgetActivity {

	@ViewById
	TextView tvTextClick;
	@ViewById
	TextView tvName;
	@ViewById
	TextView tvPhone;
	@ViewById
	EditText etAddress;
	@ViewById
	RadioButton rbCheckOK;
	@ViewById
	RadioButton rbCheckNO;
	@ViewById
	TextView tvSubmit;
	
	@ViewById 
	RelativeLayout rlAddress;
	@ViewById 
	TextView tvAddress;
	
	private int status = 1;
	
	@Extra UserAddress userAddress;
	
	private String myProvince;
	private String myCity;
	private String myArea;

	@AfterViews
	void init() {
		
		tvName.setText(GlobalPara.getOutUserRz().getName());
		tvPhone.setText(UserUtil.getUserInfo().getLoginName());
		
		if(userAddress != null && userAddress.getAddressId() != null){
			tvTextClick.setText("删除");
			myProvince = userAddress.getProvince() != null ? userAddress.getProvince() : "";
			myCity = userAddress.getCity() != null ? userAddress.getCity() : "";
			myArea = userAddress.getArea() != null ? userAddress.getArea() : "";
			tvAddress.setText(myProvince + " " + myCity + " " + myArea + " ");
			etAddress.setText(userAddress.getAddress());
			if(userAddress.getIsDefault() != null && userAddress.getIsDefault() != 1){
				rbCheckNO.setChecked(true);
				rbCheckOK.setChecked(false);
			} else {
				rbCheckOK.setChecked(true);
				rbCheckNO.setChecked(false);
			}
		} else {
			tvTextClick.setText("");
			rbCheckOK.setChecked(true);
			rbCheckNO.setChecked(false);
		}
	}
	
	@Click
	void rlAddress() {
		Intent intent = new Intent(AddressModifyActivity.this,ProvinceActivity_.class);
		startActivityForResult(intent, 0);
	}
	
	@Click
	void tvTextClick() {
		status = 0;
		initData();
	}

	@Click
	void tvSubmit() {
		status = 1;
		if(isOk()){
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
					params.put("addressJson", makeJsonPost());
					TResult<Boolean, String> result;
					if(userAddress != null && userAddress.getAddressId() != null){
						result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "user/updateUserAddress", params);
					} else {
						result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "user/addUserAddress", params);
					}
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(AddressModifyActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								showInfo(obj.getString("desc"));
								setResult(101);
								AddressModifyActivity.this.finish();
							}
						}
					}
				} catch (Exception e) {
					Log.e("AddressModifyActivity", "initData", e);
					LogUmeng.reportError(AddressModifyActivity.this, e);
				}
			}
		});
	}

	private boolean isOk() {
		if(TextUtils.isEmpty(ViewUtil.getText(tvName))){
			showInfo("收货人姓名不能为空！");
		} else if(TextUtils.isEmpty(ViewUtil.getText(tvPhone))){
			showInfo("收货人手机号不能为空！");
		} else if(!SIMCardUtil.isMobileNo(ViewUtil.getText(tvPhone))){
			showInfo("手机号码格式有误！");
		} else if(TextUtils.isEmpty(ViewUtil.getText(tvAddress))){
			showInfo("请选择收货地址的省市区!");
		} else if(TextUtils.isEmpty(ViewUtil.getText(etAddress))){
			showInfo("收货地址不能为空！");
		} else {
			return true;
		}
		return false;
	}

	/**
	 * 生成注册POST信息
	 * 
	 * @return String userJson
	 */
	private String makeJsonPost() {
		UserAddress myUserAddress = new UserAddress();
		if(userAddress != null && userAddress.getAddressId() != null){
			myUserAddress.setAddressId(userAddress.getAddressId());
		}
		myUserAddress.setProvince(myProvince);
		myUserAddress.setCity(myCity);
		myUserAddress.setArea(myArea);
		myUserAddress.setAddress(ViewUtil.getText(etAddress));
		myUserAddress.setName(ViewUtil.getText(tvName));
		myUserAddress.setPhone(ViewUtil.getText(tvPhone));
		myUserAddress.setStatus(status);
		myUserAddress.setUserId(UserUtil.getUserInfo().getUserId());
		if(rbCheckNO.isChecked()){
			myUserAddress.setIsDefault(0);
		} else {
			myUserAddress.setIsDefault(1);
		}
		return FastJsonUtils.getJsonString(myUserAddress);
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Variables.ADDRESS_STATUS_CODE_SECCESS){
			Bundle b = data.getExtras();
			if(b != null){
				myProvince = b.getString("province");
				myCity = b.getString("city");
				myArea = b.getString("area");
				tvAddress.setText(myProvince + " " + myCity + " " + myArea + " ");
			}
		} 
	}
	
	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
//				if (transDialog != null && transDialog.isShowing()) {
//					transDialog.dismiss();
//				}
				Toast.makeText(AddressModifyActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
