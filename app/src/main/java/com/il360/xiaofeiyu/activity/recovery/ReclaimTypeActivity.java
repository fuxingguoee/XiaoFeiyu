package com.il360.xiaofeiyu.activity.recovery;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;

import android.content.Intent;
import android.widget.RelativeLayout;

@EActivity(R.layout.act_reclaim_type)
public class ReclaimTypeActivity extends BaseWidgetActivity {

	@ViewById
	RelativeLayout rlMail;

	@Extra
	String name, amount, details;
	@Extra
	Integer iphonetype;
	
	@AfterViews
	void init() {

	}

	@Click
	void rlMail() {
		Intent intent = new Intent(ReclaimTypeActivity.this, ReclaimOrderActivity_.class);
		intent.putExtra("name", name);
		intent.putExtra("amount", amount);
		intent.putExtra("details", details);
		intent.putExtra("iphonetype", iphonetype);
		startActivity(intent);
	}

}
