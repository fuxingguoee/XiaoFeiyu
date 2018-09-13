package com.il360.xiaofeiyu.activity.order;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.home.PayActivity_;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.order.LeaseOrder;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_order_redeem)
public class RedeemActivity extends BaseWidgetActivity {
	
	/**设备金额*/
	@ViewById
	TextView tvDevicePrice;
	/**押金*/
	@ViewById
	TextView tvDepositPrice;
	/**赎金*/
	@ViewById
	TextView tvRansom;
	/**赎回按钮*/
	
	@ViewById
	TextView tvToRedeem;
	/**客服热线*/
	@ViewById 
	TextView tvHotline;
	
	@Extra LeaseOrder leaseOrder;
	
	private String telephone = "";
	
	double money;
	DecimalFormat df = new DecimalFormat("#0.00");
	
	@AfterViews
	void init(){
		tvDevicePrice.setText(leaseOrder.getMoney() + "元");
		tvDepositPrice.setText(leaseOrder.getDeposit() + "元");
		if(leaseOrder.getBackDeposit().equals("1")){
			money = leaseOrder.getMoney().doubleValue() - leaseOrder.getDeposit().doubleValue();
		} else if(leaseOrder.getBackDeposit().equals("0")){
			if(leaseOrder.getIsEverYuqi().equals("0")){
				money = leaseOrder.getMoney().doubleValue() - leaseOrder.getDeposit().doubleValue();
			} else if(leaseOrder.getIsEverYuqi().equals("1")){
				tvDepositPrice.setText("您有违约记录，押金不退还");
				money = leaseOrder.getMoney().doubleValue();
			}
		}

		tvRansom.setText(df.format(money) + "元");
		
		telephone = GlobalPara.getTelephone();
		tvHotline.setText(telephone);
		tvHotline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //下划线
		tvHotline.getPaint().setAntiAlias(true);//去锯齿
	}
	
	@Click
	void tvToRedeem(){
		ToSubmit();
	}
	
	@Click
	void tvHotline() {
		if(!TextUtils.isEmpty(telephone)){
			Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + tvHotline.getText()));
			startActivity(intent);
//			try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			ViewUtil.backHomeActivity(RedeemActivity.this);
		}
	}
	
	private void ToSubmit() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("type", "2");
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"lease/PostPayFeeOrder", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(RedeemActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							final JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								Intent intent = new Intent(RedeemActivity.this, PayActivity_.class);
								intent.putExtra("type", "2");
								intent.putExtra("leaseOrder", leaseOrder);
								startActivity(intent);
							} else {
								showInfo(obj.getString("desc"));
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("PlaceOrderActivity", "initOrder", e);
				} finally {

				}
			}
		});
		
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(RedeemActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
