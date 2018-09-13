package com.il360.xiaofeiyu.activity.user;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;

import android.content.Intent;
import android.widget.RelativeLayout;

@EActivity(R.layout.act_manage)
public class ManageActivity extends BaseWidgetActivity {

	@ViewById
	RelativeLayout rlLoginPwd;
//	@ViewById
//	RelativeLayout rlDealPwd;
	@ViewById
	RelativeLayout rlAddress;

	@Click
	void rlLoginPwd() {
		Intent intent = new Intent(ManageActivity.this, PasswordModifyActivity_.class);
		startActivity(intent);
	}

//	@Click
//	void rlDealPwd() {
//		Intent intent = new Intent(ManageActivity.this, DealPwdModifyActivity_.class);
//		startActivity(intent);
//	}

	@Click
	void rlAddress() {
		Intent intent = new Intent(ManageActivity.this, DeliveryAddressActivity_.class);
		startActivity(intent);
	}

}
