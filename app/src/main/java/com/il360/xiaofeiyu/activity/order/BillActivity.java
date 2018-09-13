package com.il360.xiaofeiyu.activity.order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.il360.xiaofeiyu.model.order.ArrayOfPayFeeOrder;
import com.il360.xiaofeiyu.model.order.LeaseOrder;
import com.il360.xiaofeiyu.model.order.PayFeeOrder;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_order_bill)
public class BillActivity extends BaseWidgetActivity {
	
	@ViewById TextView tvExpireTime;
	@ViewById TextView tvDayFee;
	@ViewById TextView tvOverTimeFee;
	@ViewById TextView tvALLFee;
	@ViewById TextView tvToPay;
	@ViewById TextView tvHotline;
	@ViewById RelativeLayout rlOverFee;
	@ViewById RelativeLayout rlRemind;
	
	@Extra LeaseOrder leaseOrder;
	@Extra String type;
	@Extra String stagesId;
	@Extra String stagesNumber;
	
	private String telephone;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	DecimalFormat df = new DecimalFormat("#0.00");
	
	private String money;
	private String overFee;
	
	private ArrayOfPayFeeOrder arrayOfPayFeeOrder;
	private List<PayFeeOrder> list = new ArrayList<PayFeeOrder>();
	
	@AfterViews
	void init() {
		initData();
	}

	private void initData() {
		initMoney();
	}
	
	@Click
	void tvHotline() {
		if(!TextUtils.isEmpty(telephone)){
			Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + tvHotline.getText()));
			startActivity(intent);
		}
	}
	
	@Click
	void tvToPay(){
		if(type.equals("3")){
			Intent intent = new Intent(BillActivity.this, PayActivity_.class);
			intent.putExtra("type", type);
			intent.putExtra("leaseOrder", leaseOrder);
			startActivity(intent);
		} else if(type.equals("1")){
			Intent intent = new Intent(BillActivity.this, PayActivity_.class);
			intent.putExtra("type", type);
			intent.putExtra("leaseOrder", leaseOrder);
			
			startActivity(intent);
		}
	}
	//获取后台订单金额
	private void initMoney() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("type", type);
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "lease/queryPayFeeList",params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(BillActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							arrayOfPayFeeOrder = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfPayFeeOrder.class);
							if(arrayOfPayFeeOrder.getCode() == 1){
								list = arrayOfPayFeeOrder.getResult();
								for(int i = 0; i < list.size(); i++){
									money  = list.get(i).getFee() + "";
									overFee = list.get(i).getOverFee() + "";
								}
							} else {
								showInfo(arrayOfPayFeeOrder.getDesc());
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if(type.equals("3")){
								telephone = GlobalPara.getTelephone();
								tvHotline.setText(telephone);
								tvHotline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //下划线
								tvHotline.getPaint().setAntiAlias(true);//去锯齿
								tvExpireTime.setText(sdf.format(Long.parseLong(leaseOrder.getExpireTime())));
								tvDayFee.setText(df.format((Double.parseDouble(money) - Double.parseDouble(overFee)))  + "元"
										+ "/" + Integer.parseInt(leaseOrder.getExt1())/-1 + "天");
								tvOverTimeFee.setText(overFee + "元");
								tvALLFee.setText(money + "元");
								rlRemind.setVisibility(View.VISIBLE);
							} else if(type.equals("1")){
								tvExpireTime.setText(sdf.format(Long.parseLong(leaseOrder.getExpireTime())));
								tvDayFee.setText(money + "元" + "/" + stagesNumber + "天");
								rlOverFee.setVisibility(View.GONE);
								tvALLFee.setText(money + "元"); 
							}
						}
					});
				}
			}
		});
	}
	
	
	public void showInfo(final String info) {
		this.runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(BillActivity.this, info, Toast.LENGTH_SHORT).show();
				}
			});
	}
}
//