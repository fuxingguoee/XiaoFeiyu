package com.il360.xiaofeiyu.activity.user;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.umeng.socialize.UMShareAPI;

import android.content.Intent;
import android.widget.TextView;

@EActivity(R.layout.act_recommend)
public class RecommendActivity extends BaseWidgetActivity {
	
	@ViewById TextView tvTextClick;

	@ViewById TextView tvRecommend;
	
	@AfterViews
	void init() {
		tvTextClick.setHeight(24);
		tvTextClick.setWidth(24);
		tvTextClick.setBackgroundResource(R.drawable.ic_share);
	}
	
	@Click
	void tvTextClick() {
		platformShare();
	}

	@Click
	void tvRecommend() {
		platformShare();
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
