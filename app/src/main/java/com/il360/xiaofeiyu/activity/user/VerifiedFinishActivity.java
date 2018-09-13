package com.il360.xiaofeiyu.activity.user;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;

import android.annotation.SuppressLint;
import android.widget.TextView;

@EActivity(R.layout.act_verified_finish)
public class VerifiedFinishActivity extends BaseWidgetActivity{
	
	@ViewById TextView user_name;
	@ViewById TextView user_login_name;
	@ViewById TextView submit_time;
	
	@Extra String realName;

	@SuppressLint("SimpleDateFormat")
	@AfterViews
	void init(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		user_name.setText("尊敬的  "+realName+":");
		user_login_name.setText(UserUtil.getUserInfo().getLoginName());
		submit_time.setText(df.format(new Date()));
	}
	@Click
	void submit(){
		ViewUtil.backHomeActivity(this);
	}
}
