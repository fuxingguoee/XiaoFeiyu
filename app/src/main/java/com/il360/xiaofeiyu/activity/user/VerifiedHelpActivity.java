package com.il360.xiaofeiyu.activity.user;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;

import android.view.View;
import android.widget.LinearLayout;

@EActivity(R.layout.act_verified_help)
public class VerifiedHelpActivity extends BaseWidgetActivity {

	@ViewById
	LinearLayout lyTishi1;
	@ViewById
	LinearLayout lyTishi2;
	@ViewById
	LinearLayout lyTishi3;
	@ViewById
	LinearLayout lyTishi4;
	@ViewById
	LinearLayout lyTishi5;
	@ViewById
	LinearLayout lyTishi6;
	@ViewById
	LinearLayout lyTishi7;

	@Extra
	String flag;

	@AfterViews
	void init() {
		if (flag.equals("VerifiedActivity")) {
			lyTishi1.setVisibility(View.VISIBLE);
			lyTishi2.setVisibility(View.VISIBLE);
			lyTishi3.setVisibility(View.VISIBLE);
			lyTishi4.setVisibility(View.GONE);
			lyTishi5.setVisibility(View.GONE);
			lyTishi6.setVisibility(View.GONE);
			lyTishi7.setVisibility(View.GONE);
		} else if (flag.equals("CreditCardVerifiedActivity")) {
			lyTishi1.setVisibility(View.VISIBLE);
			lyTishi2.setVisibility(View.VISIBLE);
			lyTishi3.setVisibility(View.GONE);
			lyTishi4.setVisibility(View.VISIBLE);
			lyTishi5.setVisibility(View.VISIBLE);
			lyTishi6.setVisibility(View.GONE);
			lyTishi7.setVisibility(View.GONE);
		} else if (flag.equals("BindingBankCardActivity")) {
			lyTishi1.setVisibility(View.GONE);
			lyTishi2.setVisibility(View.GONE);
			lyTishi3.setVisibility(View.GONE);
			lyTishi4.setVisibility(View.GONE);
			lyTishi5.setVisibility(View.GONE);
			lyTishi6.setVisibility(View.VISIBLE);
			lyTishi7.setVisibility(View.VISIBLE);
		}
	}
}
