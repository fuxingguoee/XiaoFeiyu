package com.il360.xiaofeiyu.alipay;

import java.util.HashMap;
import java.util.Map;

import com.alipay.sdk.app.PayTask;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.main.MainActivity_;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.activity_alipay)
public class AliPayActivity extends BaseWidgetActivity {

//	// 商户PID
//	public static final String PARTNER = "2088121970017668";
//	// 商户收款账号
//	public static final String SELLER = "2421081747@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	@ViewById(R.id.alipay_ll_result)
	public LinearLayout alipay_ll_result;
	@ViewById(R.id.alipay_tv_order_no)
	public TextView alipay_tv_order_no;

	private String payInfo;
	private String paySn;
	private String type;

	protected ProgressDialog transDialog;

	@AfterViews
	void init() {
		super.tvActionTitle.setText("支付宝支付");
		super.header_image_return.setVisibility(View.INVISIBLE);

		Bundle bun = getIntent().getExtras();
		if (bun != null) {
			payInfo = bun.getString("payInfo");
			paySn = bun.getString("paySn");
			type = bun.getString("type");
		}
		if (TextUtils.isEmpty(payInfo) || TextUtils.isEmpty(paySn)) {
			returnErrResult("请求参数错误");
		} else {
			check();
		}
	}

	@Click(R.id.alipay_btn_backhome)
	void alipay_btn_backhome(View view) {
		Intent intent = new Intent(this, MainActivity_.class);
		startActivity(intent);
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				// String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
				if (TextUtils.equals(resultStatus, "9000")
						|| TextUtils.equals(resultStatus, "8000")) {
					if(type.equals("-3")){
						comfirmPay2();
					} else {
						comfirmPay();
					}
				} else {
					cancelPay();
					// 判断resultStatus 为非“9000”则代表可能支付失败
					if (TextUtils.equals(resultStatus, "4000")) {
						showPayResult("订单支付失败");
					} else if (TextUtils.equals(resultStatus, "6001")) {
						showPayResult("用户中途取消");
					} else if (TextUtils.equals(resultStatus, "6002")) {
						showPayResult("网络连接出错");
					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
//				if ((Boolean) msg.obj) {
					pay();
//				} else {
//					showPayResult("请先安装支付宝应用");
//				}
				break;
			}
			default:
				break;
			}
		};
	};
	
	private void cancelPay() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("tn", paySn);
			        HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, 
			        		"pay/insurePayResult", params);
				} catch (Exception e) {

				} 
			}

		});
	}
	
	private void comfirmPay() {
		transDialog = ProgressDialog.show(AliPayActivity.this, null, "确认中...",
				true, true);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("tn", paySn);
			        TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, 
			        		"pay/insurePayResult", params);
			        if(result.getSuccess()) {
//			        	JSONObject obj = new JSONObject(result.getResult());
//						if (obj.getInt("code") == 1) {
//							Intent intent = new Intent(AliPayActivity.this, PaySuccessActivity_.class);
//							intent.putExtra("desc", obj.getString("desc"));
//							startActivity(intent);
//							AliPayActivity.this.finish();
//						} else {
//							showPayResult(obj.getString("desc"));
//						}
			        	JsonObject obj = new JsonParser().parse(result.getResult()).getAsJsonObject();
			        	if(obj.get("code").getAsInt() == 1) {
			        		Intent intent = new Intent(AliPayActivity.this, PaySuccessActivity_.class);
							intent.putExtra("desc", obj.get("desc").getAsString());
							startActivity(intent);
							AliPayActivity.this.finish();
			        	} else {
			        		showPayResult(obj.get("desc").getAsString());
			        	}
			        } else {
			        	showPayResult("暂时无法获取付款结果，稍后请至记录查看");
			        }
				} catch (Exception e) {
					showPayResult("暂时无法获取付款结果，稍后请至记录查看");
				} finally {
					transDialog.dismiss();
				}
			}

		});
	}

	private void comfirmPay2() {
		transDialog = ProgressDialog.show(AliPayActivity.this, null, "确认中...",
				true, true);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("tn", paySn);
			        TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
			        		"pay/insurePayResult2", params);
			        if(result.getSuccess()) {
//			        	JSONObject obj = new JSONObject(result.getResult());
//						if (obj.getInt("code") == 1) {
//							Intent intent = new Intent(AliPayActivity.this, PaySuccessActivity_.class);
//							intent.putExtra("desc", obj.getString("desc"));
//							startActivity(intent);
//							AliPayActivity.this.finish();
//						} else {
//							showPayResult(obj.getString("desc"));
//						}
			        	JsonObject obj = new JsonParser().parse(result.getResult()).getAsJsonObject();
			        	if(obj.get("code").getAsInt() == 1) {
			        		Intent intent = new Intent(AliPayActivity.this, PaySuccessActivity_.class);
							intent.putExtra("desc", obj.get("desc").getAsString());
							startActivity(intent);
							AliPayActivity.this.finish();
			        	} else {
			        		showPayResult(obj.get("desc").getAsString());
			        	}
			        } else {
			        	showPayResult("暂时无法获取付款结果，稍后请至记录查看");
			        }
				} catch (Exception e) {
					showPayResult("暂时无法获取付款结果，稍后请至记录查看");
				} finally {
					transDialog.dismiss();
				}
			}

		});
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay() {

		// 完整的符合支付宝参数规范的订单信息
		final String payReq = payInfo;

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(AliPayActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payReq);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);

			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(AliPayActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private void returnErrResult(String errMsg) {
		Intent intent = new Intent();
		intent.putExtra("errMsg", errMsg);
		// 通过调用setResult方法返回结果给前一个activity。
		AliPayActivity.this.setResult(RESULT_OK, intent);
		// 关闭当前activity
		AliPayActivity.this.finish();
	}

	private void showPayResult(final String resp) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(AliPayActivity.this);
				builder.setTitle("支付结果");
				builder.setMessage(resp);
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AliPayActivity.this.finish();

					}
				});
				builder.show();
			}
		});
		
	}
	
	@Override
	public void onBackPressed() {
		AliPayActivity.this.finish();
	}

}
