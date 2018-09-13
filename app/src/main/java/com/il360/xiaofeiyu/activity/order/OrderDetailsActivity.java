package com.il360.xiaofeiyu.activity.order;

import java.text.SimpleDateFormat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.home.PayActivity_;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.model.order.FrozenFeeOrder;
import com.il360.xiaofeiyu.model.order.LeaseOrder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_order_details)
public class OrderDetailsActivity extends BaseWidgetActivity {

	protected ProgressDialog transDialog;

	@ViewById TextView tvTime;
	@ViewById TextView tvDeviceName;
	@ViewById TextView tvDevicePrice;
	@ViewById TextView tvDeposit;
	@ViewById TextView tvTureMoney;
	@ViewById TextView tvOrderStatus;
	@ViewById TextView tvUser;
	@ViewById TextView tvPhone;
	@ViewById TextView tvAddress;
	@ViewById TextView tvRedeem;
	@ViewById TextView tvRenew;
	@ViewById TextView tvOverdueFee;
	@ViewById TextView tvRecovery;
	@ViewById TextView tvHotline;
	@ViewById TextView tvFrozenFee;
	@ViewById RelativeLayout rlRemind;
	@ViewById LinearLayout llButton;

	@Extra LeaseOrder leaseOrder;
	@Extra FrozenFeeOrder frozenFeeOrder;

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private String[] infos;
	private String telephone;

	@AfterViews
	void init() {
		initData();
	}

	@SuppressWarnings("deprecation")
	private void initData() {
		telephone = GlobalPara.getTelephone();
		tvHotline.setText(telephone);
		tvHotline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //下划线
		tvHotline.getPaint().setAntiAlias(true);//去锯齿
		String info = leaseOrder.getExt2();
		infos = info.split(";");
		tvUser.setText(infos[0]);
		tvPhone.setText(infos[1]);
		tvAddress.setText(infos[2]);
		tvTime.setText(sdf.format(Long.parseLong(leaseOrder.getCreateTime())));
		if(leaseOrder.getGoodsSysDetail() != null){
			tvDeviceName.setText(leaseOrder.getGoodsSysDetail());
		} else {
			tvDeviceName.setText("其他机型");
		}
		tvDevicePrice.setText(leaseOrder.getMoney() + "元");
		tvDeposit.setText(leaseOrder.getDeposit() + "元");
		tvTureMoney.setText(leaseOrder.getGetMoney() + "元");

		if (leaseOrder.getStatus() != null && leaseOrder.getStatus() == 0) {
			tvOrderStatus.setText("审核中");
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textChecking));
		} else if (leaseOrder.getStatus() != null && leaseOrder.getStatus() == -1) {
			tvOrderStatus.setText("未通过");
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.text_gray));
		} else if (leaseOrder.getStatus() != null && leaseOrder.getStatus() == 1) {
			tvOrderStatus.setText("审核通过，等待下款");
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textChecking));
		} else if (leaseOrder.getStatus() != null && leaseOrder.getStatus() == 2) {
			if (leaseOrder.getExt1() != null && (Integer.parseInt(leaseOrder.getExt1()) > 0
					|| Integer.parseInt(leaseOrder.getExt1()) == 0)) {
				llButton.setVisibility(View.VISIBLE);
				tvOverdueFee.setVisibility(View.GONE);
				tvRecovery.setVisibility(View.GONE);
				tvFrozenFee.setVisibility(View.GONE);
				tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textExpire));
				if(leaseOrder.getExt1().equals("0")){
					tvOrderStatus.setText( "今天到期");
				} else if (leaseOrder.getExt1().equals("1")){
					tvOrderStatus.setText( "明天到期");
				} else {
					tvOrderStatus.setText( leaseOrder.getExt1() + "天后到期");
				}
			} else if (leaseOrder.getExt1() != null && Integer.parseInt(leaseOrder.getExt1()) < 0) {
				llButton.setVisibility(View.VISIBLE);
				rlRemind.setVisibility(View.VISIBLE);
				tvRenew.setVisibility(View.GONE);
				tvRedeem.setVisibility(View.GONE);
				tvRecovery.setVisibility(View.GONE);
				tvFrozenFee.setVisibility(View.GONE);
				tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textOverTime));
				tvOrderStatus.setText("已超时" + Integer.parseInt(leaseOrder.getExt1())/-1 + "天");
			}
		} else if (leaseOrder.getStatus() != null && leaseOrder.getStatus() == 3) {
			tvOrderStatus.setText("已赎回");
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textChecking));
		} else if (leaseOrder.getStatus() != null && leaseOrder.getStatus() == 4) {
			llButton.setVisibility(View.VISIBLE);
			tvRenew.setVisibility(View.GONE);
			tvRedeem.setVisibility(View.GONE);
			tvOverdueFee.setVisibility(View.GONE);
			tvRecovery.setVisibility(View.VISIBLE);
			tvFrozenFee.setVisibility(View.GONE);
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textOverTime));
			tvOrderStatus.setText("可以寄回");
		} else if (leaseOrder.getStatus() != null && leaseOrder.getStatus() == 5) {
			tvOrderStatus.setText("已寄回");
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textChecking));
		}  else if(leaseOrder.getStatus() != null && leaseOrder.getStatus() == 6){
			tvOrderStatus.setText("手机归还");
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textChecking));
		} else if(leaseOrder.getStatus() != null && leaseOrder.getStatus() == -2){
			tvOrderStatus.setText("非正常关闭");
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textChecking));
		} else if(leaseOrder.getStatus() != null && leaseOrder.getStatus() == -3){
			tvOrderStatus.setText("订单冻结");
			llButton.setVisibility(View.VISIBLE);
			tvRenew.setVisibility(View.GONE);
			tvRedeem.setVisibility(View.GONE);
			tvRecovery.setVisibility(View.GONE);
			tvOverdueFee.setVisibility(View.GONE);
			if(frozenFeeOrder == null){
				llButton.setVisibility(View.GONE);
			}
			tvOrderStatus.setTextColor(getBaseContext().getResources().getColor(R.color.textChecking));
		}
	}

	@Click
	void tvRedeem() {
		Intent intent = new Intent(OrderDetailsActivity.this, RedeemActivity_.class);
		intent.putExtra("leaseOrder", leaseOrder);
		startActivity(intent);
	}

	@Click
	void tvRenew() {
		Intent intent = new Intent(OrderDetailsActivity.this, RenewActivity_.class);
		intent.putExtra("leaseOrder", leaseOrder);
		startActivity(intent);
	}

	@Click
	void tvOverdueFee() {
		Intent intent = new Intent(OrderDetailsActivity.this, BillActivity_.class);
		intent.putExtra("type", "3");
		intent.putExtra("leaseOrder", leaseOrder);
		startActivity(intent);
	}

	@Click
	void tvRecovery() {
		Intent intent = new Intent(OrderDetailsActivity.this, LogisticsInfoActivity2_.class);
		intent.putExtra("leaseOrder", leaseOrder);
		startActivity(intent);
	}

	@Click
	void tvFrozenFee() {
		Intent intent = new Intent(OrderDetailsActivity.this,PayActivity_.class);
		intent.putExtra("type", "-3");
		intent.putExtra("frozenFeeOrder", frozenFeeOrder);
		startActivity(intent);
	}
	
	@Click
	void tvHotline() {
		if(!TextUtils.isEmpty(telephone)){
			Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + tvHotline.getText()));
			startActivity(intent);
		}
	}

	public void showInfo(final String info) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(OrderDetailsActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
