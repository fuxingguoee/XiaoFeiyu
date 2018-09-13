package com.il360.xiaofeiyu.activity.order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.model.order.RecordOrder;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_order_record_details)
public class RecordDetailsActivity extends BaseWidgetActivity {

	protected ProgressDialog transDialog;
	
	@ViewById TextView tvTime;
	@ViewById TextView tvPayOrderNo;//缴费订单号
	@ViewById TextView tvPayType;//缴费类型
	
	@ViewById TextView tvPhoneMoney; //设备估值
	@ViewById RelativeLayout rlPhoneMoney; //设备估值
	
	@ViewById TextView tvReneedFee; //赎回费用
	@ViewById RelativeLayout rlReneedFee; //赎回费用
	
	@ViewById TextView tvStartTime; 
	@ViewById RelativeLayout rlStartTime; 
	
	@ViewById TextView tvEndTime; 
	@ViewById RelativeLayout rlEndTime; 
	
	@ViewById TextView tvStages; //套餐
	@ViewById RelativeLayout rlStages; //套餐
	
	@ViewById TextView tvRent; //租金
	@ViewById RelativeLayout rlRent; //租金
	
	@ViewById TextView tvRentReduction; //租金减免
	@ViewById RelativeLayout rlRentReduction; //租金减免
	
	@ViewById TextView tvOvertimeFee; //超时费
	@ViewById RelativeLayout rlOvertimeFee; //超时费
	
	@ViewById TextView tvOvertimeFeeNo; //超时费减免
	@ViewById RelativeLayout rlOvertimeFeeNo; //超时费减免
	
	@ViewById TextView tvAllmoney;//总费用
	@ViewById RelativeLayout rlAllmoney;//总费用
	
	@ViewById TextView tvPayMoney; //支付金额
	@ViewById TextView tvPayMethod; //支付方式
	@ViewById TextView tvPayTime; //支付时间
	@ViewById TextView tvPayResult; //支付结果
	
	@Extra RecordOrder recordOrder;

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DecimalFormat df = new DecimalFormat("#0.00");

	@AfterViews
	void init() {
		initData();
	}

	private void initData() {
		tvTime.setText(sdf2.format(Long.parseLong(recordOrder.getOrderSendTime())));
		tvPayOrderNo.setText(recordOrder.getOrderNo());
		tvPayMoney.setText(df.format(Double.parseDouble(recordOrder.getOrderSendAmt())/100));
		if(recordOrder.getChannelType().equals("1")){
			tvPayMethod.setText("支付宝");
		} else if(recordOrder.getChannelType().equals("2")) {
			tvPayMethod.setText("无卡支付");
		}
		tvPayTime.setText(sdf2.format(Long.parseLong(recordOrder.getOrderResultTime())));
		if(recordOrder.getStatus().equals("0")){
			tvPayResult.setText("未支付");
		} else if(recordOrder.getStatus().equals("-1")){
			tvPayResult.setText("支付失败");
		} else if(recordOrder.getStatus().equals("1")){
			tvPayResult.setText("支付成功");
		} else if(recordOrder.getStatus().equals("2")){
			tvPayResult.setText("处理中");
		}

		if(recordOrder.getNumber() .equals("2")){
			tvPayType.setText("冻结订单");
		} else if(recordOrder.getNumber() .equals("1")){
			if(recordOrder.getType().equals("1")){
				tvPayType.setText("续租");
				rlStartTime.setVisibility(View.VISIBLE);
				rlEndTime.setVisibility(View.VISIBLE);
				rlStages.setVisibility(View.VISIBLE);
				rlAllmoney.setVisibility(View.VISIBLE);
				tvStartTime.setText(sdf.format(Long.parseLong(recordOrder.getStartDay())));
				tvEndTime.setText(sdf.format(Long.parseLong(recordOrder.getEndDay())));
				tvStages.setText(recordOrder.getStagesNumber() + "天");
				tvAllmoney.setText(df.format(Double.parseDouble(recordOrder.getFee())));
			} else if(recordOrder.getType().equals("2")){
				tvPayType.setText("赎回");
				rlPhoneMoney.setVisibility(View.VISIBLE);
				rlReneedFee.setVisibility(View.VISIBLE);
				tvPhoneMoney.setText(df.format(Double.parseDouble(recordOrder.getMoney())));
				tvReneedFee.setText(df.format(Double.parseDouble(recordOrder.getFee())));
			} else if(recordOrder.getType().equals("3")){
				tvPayType.setText("超时缴费");
				rlStartTime.setVisibility(View.VISIBLE);
				rlEndTime.setVisibility(View.VISIBLE);
				rlRent.setVisibility(View.VISIBLE);
				rlRentReduction.setVisibility(View.VISIBLE);
				rlOvertimeFee.setVisibility(View.VISIBLE);
				rlOvertimeFeeNo.setVisibility(View.VISIBLE);
				rlAllmoney.setVisibility(View.VISIBLE);
				tvStartTime.setText(sdf.format(Long.parseLong(recordOrder.getStartDay())));
				tvEndTime.setText(sdf.format(Long.parseLong(recordOrder.getEndDay())));
				tvRent.setText(df.format(Double.parseDouble(recordOrder.getRentFee())));
				tvRentReduction.setText(df.format(Double.parseDouble(recordOrder.getRentReduction())));
				tvOvertimeFee.setText(df.format(Double.parseDouble(recordOrder.getOverFee())));
				tvOvertimeFeeNo.setText(df.format(Double.parseDouble(recordOrder.getOverReduction())));
				tvAllmoney.setText(df.format(Double.parseDouble(recordOrder.getFee())));
			}
		}

	}

	public void showInfo(final String info) {
		this.runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(RecordDetailsActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
