package com.il360.xiaofeiyu.activity.user;

import java.io.File;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.util.FileUtil;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_setting)
public class SettingActivity extends BaseWidgetActivity {
	
	@ViewById
	TextView tvVersionCount;

	@ViewById 
	RelativeLayout rlClear;
	@ViewById
	TextView tvClearCount;
	
	@ViewById 
	RelativeLayout rlHelp;
	@ViewById 
	RelativeLayout rlAgreement;
	@ViewById 
	RelativeLayout rlAbout;
	@ViewById 
	RelativeLayout rlServerHot;
	@ViewById
	TextView tvServerHotNum;
	@ViewById 
	RelativeLayout rlBusiness;
	
	final File file = new File(Variables.APP_CACHE_SDPATH);
	
	@AfterViews
	void init(){
		
		 try {
			PackageInfo pInfo = SettingActivity.this.getPackageManager().getPackageInfo(SettingActivity.this.getPackageName(),0);
			tvVersionCount.setText(pInfo.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		float s=file.length()/1024;
		tvClearCount.setText(Float.toString(s)+"KB");
	}
	
	@Click
	void rlClear(){
		showInfo(getResources().getString(R.string.deleting));
		FileUtil.RecursionDeleteFile(file);
		if(!file.exists()){
			showInfo(getResources().getString(R.string.clean_cache_sucess));
			tvClearCount.setText("0.0"+"KB");
		}
	}
	
	@Click
	void rlHelp(){
//		Intent intent = new Intent(SettingActivity.this,UrlToWebActivity_.class);
//		intent.putExtra("supportZoom", false);
//		intent.putExtra("title", "帮助中心");
//		intent.putExtra("url", "http://139.129.166.164:8080/onebuy/help.htm");
//		startActivity(intent);
	}
	
	@Click
	void rlAgreement(){
//		Intent intent = new Intent(SettingActivity.this,UrlToWebActivity_.class);
//		intent.putExtra("supportZoom", false);
//		intent.putExtra("title", "服务协议 ");
//		intent.putExtra("url", "http://139.129.166.164:8080/onebuy/agreement.htm");
//		startActivity(intent);
	}
	
	@Click
	void rlAbout(){
		Intent intent = new Intent(SettingActivity.this,AboutUsActivity_.class);
		startActivity(intent);
	}
	
	@Click
	void rlServerHot(){
		Intent intent = new Intent("android.intent.action.CALL",
				Uri.parse("tel:" + tvServerHotNum.getText()));
		startActivity(intent);
	}
	
	@Click
	void rlBusiness(){
		
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(SettingActivity.this, info, Toast.LENGTH_SHORT).show();
				}
			});
		}
}
