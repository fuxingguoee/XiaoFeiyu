package com.il360.xiaofeiyu.activity.user;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.GlobalPara;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.TextView;
@EActivity(R.layout.act_about_us)
public class AboutUsActivity extends BaseWidgetActivity{
	private Context context = this;
	
	@ViewById TextView tvVersion;
	@ViewById TextView tvIntroduce;
	@ViewById TextView tvHotline;
	@ViewById TextView tvCompany;
	@ViewById TextView tvCopyright;
	
	private String company = "";
	private String copyright = "";
	private String telephone = "";
	
	
	@AfterViews
	void initView() {
		
		if(GlobalPara.getCardConfigList() != null && GlobalPara.getCardConfigList().size() > 0){
			for (int i = 0; i < GlobalPara.getCardConfigList().size(); i++) {
				if(GlobalPara.getCardConfigList().get(i).getConfigGroup().equals("app") && GlobalPara.getCardConfigList().get(i).getConfigName().equals("company")){
					company = GlobalPara.getCardConfigList().get(i).getConfigValue();
					tvCompany.setText(company + "\t版权所有");
				} else if(GlobalPara.getCardConfigList().get(i).getConfigGroup().equals("app") && GlobalPara.getCardConfigList().get(i).getConfigName().equals("copyright")){
					copyright = GlobalPara.getCardConfigList().get(i).getConfigValue();
					tvCopyright.setText(copyright);
				} else if(GlobalPara.getCardConfigList().get(i).getConfigGroup().equals("app") && GlobalPara.getCardConfigList().get(i).getConfigName().equals("telephone")){
					
				}
			}
		}
		telephone = GlobalPara.getTelephone();
		tvHotline.setText(telephone);
		
		tvIntroduce.setText("\t\t"+AboutUsActivity.this.getResources().getString(R.string.app_name) + "，是由"+ company +
				AboutUsActivity.this.getResources().getString(R.string.about_us_tp1));
		
		tvHotline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //下划线
		tvHotline.getPaint().setAntiAlias(true);//去锯齿
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			tvVersion.setText(getResources().getString(R.string.version) + pInfo.versionName +" (Build 180130)");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Click
	void tvHotline() {
		if(!TextUtils.isEmpty(telephone)){
			Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + tvHotline.getText()));
			startActivity(intent);
		}
	}

}
