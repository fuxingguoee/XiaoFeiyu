package com.il360.xiaofeiyu.activity.home;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.main.MainActivity_;
import com.il360.xiaofeiyu.activity.order.OrderApplicationActivity_;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.model.order.Order;
import com.il360.xiaofeiyu.util.SystemUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.CustomDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_check_result)
public class CheckResultActivity extends BaseWidgetActivity {
	/** 重新检测 立即换钱 */
	@ViewById
	TextView tvRestart, tvRecovery;
	/** 手机各种属性 */
	@ViewById
	TextView tvBrandmodel, tvSystemVersion, tvMemory, tvOperator, tvSIMCard, tvIMEI, tvDNS, tvWIFI, tvCharge, tvUsage,
			tvService, tvCity;

	@ViewById
	TextView tvAvailable;

	private Order order;
	
	protected ProgressDialog transDialog;

	@AfterViews
	void init() {
		order = UserUtil.getValuationInfo();
		initAttribute();
	}

	private void initAttribute() {
		tvBrandmodel.setText(SystemUtil.getSystemModel());
		tvSystemVersion.setText(SystemUtil.getSystemVersion());
		String memory = SystemUtil.getInternalMemorySize(getBaseContext());
		Pattern p = Pattern.compile("[^0-9.]");
		Matcher m = p.matcher(memory);
		memory = m.replaceAll("".trim());
		if (Double.parseDouble(memory) < 8) {
			memory = "8GB";
		} else if (Double.parseDouble(memory) > 8 && Double.parseDouble(memory) < 16) {
			memory = "16GB";
		} else if (Double.parseDouble(memory) > 16 && Double.parseDouble(memory) < 32) {
			memory = "32GB";
		} else if (Double.parseDouble(memory) > 32 && Double.parseDouble(memory) < 64) {
			memory = "64GB";
		} else if (Double.parseDouble(memory) > 32 && Double.parseDouble(memory) < 128) {
			memory = "128GB";
		} else if (Double.parseDouble(memory) > 128 && Double.parseDouble(memory) < 256) {
			memory = "256GB";
		}
		tvMemory.setText(memory);
		if (SystemUtil.getOperators(getBaseContext()) != null){
			tvOperator.setText(SystemUtil.getOperators(getBaseContext()));
		} else {
			tvOperator.setText("未插卡");
		}
		String simCard = null;
		if (SystemUtil.hasSimCard(getBaseContext()) == true) {
			simCard = "正常";
		} else {
			simCard = "异常";
		}
		tvSIMCard.setText(simCard);
		tvIMEI.setText(SystemUtil.getIMEI(getBaseContext()));
		tvDNS.setText(SystemUtil.getLocalDNS());
		String wifi = null;
		if (SystemUtil.isWiFiActive(getBaseContext()) == true) {
			wifi = "正常";
		} else {
			wifi = "异常";
		}
		tvWIFI.setText(wifi);
		int charge = order.getCharge();
		if(charge == 1){
			tvCharge.setText("充电正常");
		} else if (charge == 2){
			tvCharge.setText("充电无反应/接触不良");
		}
		int usage = order.getAppearance();
		if(usage == 1){
			tvUsage.setText("正常使用痕迹");
		} else if (usage == 2){
			tvUsage.setText("破损/掉漆/弯曲变形");
		}
		int service = order.getRepair();
		if(service == 1){
			tvService.setText("无拆无修");
		} else if (service == 2){
			tvService.setText("屏幕维修");
		} else if (service == 3){
			tvService.setText("主板维修/多处维修");
		}  
		tvCity.setText(order.getCity());
		tvAvailable.setText(order.getMoney());
	}

	@Click
	void tvRestart() {
		Intent intent = new Intent(CheckResultActivity.this, CheckActivity2_.class);
		startActivity(intent);
	}

	@Click
	void tvRecovery() {
		if (UserUtil.judgeUserInfo()) {
			if (UserUtil.judgeAuthentication() && UserUtil.judgeOperator() && UserUtil.judgeTaoBao()
					&& UserUtil.judgeBankCard()) {
					Intent intent = new Intent(CheckResultActivity.this, OrderApplicationActivity_.class);
					intent.putExtra("order", order);
					startActivity(intent);
			} else {
				showDialog2("请先完成认证！");
			}
		} else {
			Intent intent = new Intent(CheckResultActivity.this, LoginActivity_.class);
			startActivity(intent);
		}
	}

	private void showDialog2(String message) {
		CustomDialog.Builder builder = new CustomDialog.Builder(CheckResultActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(message);
		builder.setPositiveButton("去认证", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(CheckResultActivity.this, MainActivity_.class);
				intent.putExtra("mShowTabIndex", 1);
				startActivity(intent);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	public void showInfo(final String info) {
		this.runOnUiThread(new Runnable() {
				public void run() {
					if (transDialog != null && transDialog.isShowing()) {
						transDialog.dismiss();
					}
					Toast.makeText(CheckResultActivity.this, info, Toast.LENGTH_SHORT).show();
				}
			});
	}
}
