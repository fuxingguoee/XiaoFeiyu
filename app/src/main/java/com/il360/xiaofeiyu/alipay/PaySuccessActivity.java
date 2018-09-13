package com.il360.xiaofeiyu.alipay;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.main.MainActivity_;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

@EActivity(R.layout.act_pay_success)  //支付成功的页面
public class PaySuccessActivity extends BaseWidgetActivity {
	
	@ViewById
	public TextView tvPaySuc_orderNo;
	@ViewById
	public TextView pay_btn_desc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@AfterViews
	void init() {
		super.header_image_return.setVisibility(View.INVISIBLE);
//		super.header_image_close.setVisibility(View.VISIBLE);
//		
//		super.header_image_close.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				PaySuccessActivity.this.finish();
//			}
//		});
		
		Bundle bun = this.getIntent().getExtras();
		String desc = bun.getString("desc");
		
		if(TextUtils.isEmpty(desc)){
			desc = "支付成功";
		}
		pay_btn_desc.setText(desc);
	}
	
	@Click(R.id.btnPaySuc_backHome)
	void btnPaySuc_backHome(View view) {
			Intent intent = new Intent(this, MainActivity_.class);
			intent.putExtra("mShowTabIndex", 3);
//			intent.addCategory(Intent.CATEGORY_HOME);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		
	}
}
