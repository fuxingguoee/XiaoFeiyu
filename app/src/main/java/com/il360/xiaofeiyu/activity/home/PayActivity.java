package com.il360.xiaofeiyu.activity.home;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.activity.user.MyBankCardActivity_;
import com.il360.xiaofeiyu.adapter.PayAdapter;
import com.il360.xiaofeiyu.alipay.AliPayActivity_;
import com.il360.xiaofeiyu.alipay.PaySuccessActivity_;
import com.il360.xiaofeiyu.common.Constants;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.hua.ArrayOfPayWay;
import com.il360.xiaofeiyu.model.hua.PayWay;
import com.il360.xiaofeiyu.model.order.ArrayOfPayFeeOrder;
import com.il360.xiaofeiyu.model.order.FrozenFeeOrder;
import com.il360.xiaofeiyu.model.order.LeaseOrder;
import com.il360.xiaofeiyu.model.order.PayFeeOrder;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.NumReplaceUtil;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.llpay.BaseHelper;
import com.il360.xiaofeiyu.util.llpay.MobileSecurePayer;
import com.il360.xiaofeiyu.view.CustomDialog;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_pay) 
public class PayActivity extends BaseWidgetActivity{
	//1支付宝2连连支付3微信4银行卡
	private static final String ALI_PAY = "1";
	private static final String WUKA_PAY = "2";
	private static final String WX_PAY = "3";
	private static final String BANKCARD_PAY = "4";
	private static final String KUAIJIE_PAY = "5";
	
	@ViewById TextView tvMoney;
	@ViewById ListView payList;
	
	@ViewById TextView tvSubmit;
	
	protected ProgressDialog transDialog;
	
	DecimalFormat df = new DecimalFormat("0.00");
	
	@Extra LeaseOrder leaseOrder;
	@Extra String type;
	@Extra FrozenFeeOrder frozenFeeOrder;
	
	private String money = "0";
	private String payFeeId;
	
	private ArrayOfPayWay arrayOfPayWay;
	private PayAdapter adapter;
	private List<PayWay> list = new ArrayList<PayWay>();
	
	private ArrayOfPayFeeOrder arrayOfPayFeeOrder;
	private List<PayFeeOrder> list2 = new ArrayList<PayFeeOrder>();

	private int myPosition = 0;
	
	
	@AfterViews
	void init(){
		if(type.equals("1") || type.equals("2") || type.equals("3")){
			initMoney();
		} else if(type.equals("-3")){
			tvMoney.setText("￥" + frozenFeeOrder.getThisFee());
		}

		initPayWay();
	}

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
							Intent intent = new Intent(PayActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							arrayOfPayFeeOrder = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfPayFeeOrder.class);
							if(arrayOfPayFeeOrder.getCode() == 1){
								list2 = arrayOfPayFeeOrder.getResult();
								for(int i = 0; i < list2.size(); i++){
									money  = list2.get(i).getFee() + "";
									payFeeId = list2.get(i).getPayFeeId() + "";
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
					LogUmeng.reportError(PayActivity.this, e);
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							tvMoney.setText("￥" + money);
						}
					});
				}
			}
		});
	}

	private void initPayWay() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/payTypeList",params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(PayActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							arrayOfPayWay = FastJsonUtils.getSingleBean(result.getResult().toString(), ArrayOfPayWay.class);
							if(arrayOfPayWay.getCode() == 1){


								if (arrayOfPayWay.getResult() != null && arrayOfPayWay.getResult().size() > 0) {
									for (int i = 0; i < arrayOfPayWay.getResult().size(); i++) {
										if (arrayOfPayWay.getResult().get(i).getStatus() == 1) {
											if(arrayOfPayWay.getResult().get(i).getType().intValue() == 1){
												list.add(arrayOfPayWay.getResult().get(i));
											}
//											else if( arrayOfPayWay.getResult().get(i).getType().intValue() == 2){
//												list.add(arrayOfPayWay.getResult().get(i));
//											}
										}
									}
								}
							} else {
								showInfo(arrayOfPayWay.getDesc());
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("PayActivity", "initPayWay()", e);
					LogUmeng.reportError(PayActivity.this, e);
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if(list != null && list.size() > 0){
								adapter = new PayAdapter(list, PayActivity.this, myPosition);
								payList.setAdapter(adapter);
								payList.setOnItemClickListener(new OnItemClickListener());
								adapter.notifyDataSetChanged();
								
								tvSubmit.setVisibility(View.VISIBLE);
							} else {
								tvSubmit.setVisibility(View.GONE);
								showInfo("暂无支付方式");
							}
						}
					});
				}
			}
		});
	}
	
	
	class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			myPosition = position;
			adapter = new PayAdapter(list, PayActivity.this, myPosition);
			payList.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}
	
	@Click
	void tvSubmit(){
		tvSubmit.setClickable(false);
		
		pay();
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				tvSubmit.setClickable(true);
			}
		};
		timer.schedule(task, 1000 * 2);
	}
	
	
	private void pay(){
		
		if (list.get(myPosition).getType() == 1) {
			start(ALI_PAY);
		} else if (list.get(myPosition).getType() == 2) {
			start(WUKA_PAY);
		} else if (list.get(myPosition).getType() == 3) {
			showInfo("暂不支持微信支付");
		} else if (list.get(myPosition).getType() == 4) {
			if(UserUtil.judgeBankCard()){
				showDailog2();
			} else {
				showDailog();
			}
		} else if (list.get(myPosition).getType() == 5) {
			if(UserUtil.judgeBankCard()){
				start(KUAIJIE_PAY);
			} else {
				showDailog();
			}
		} else {
			showInfo("请选择支付方式");
		}
	}

	private void initQuickPayCode() {
		transDialog = ProgressDialog.show(PayActivity.this, null, "签约中...", true, false);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"card/quickPayCode", params);
					if (result.getSuccess()) {
						JsonObject obj = new JsonParser().parse(result.getResult()).getAsJsonObject();
						if (obj.get("code").getAsInt() == 1) {
							showDialog4(obj.get("desc").getAsString());
						} else {
							showInfo(obj.get("desc").getAsString());
						}
					}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}

						}
					});
				}
			}
		});
	}
	
	private void initQuickCode(final String sn, final String smsCode) {
		transDialog = ProgressDialog.show(PayActivity.this, null, "提交中...", true, false);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("sn", sn);
					params.put("voidcode", smsCode);
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"card/quickCode", params);
					if (result.getSuccess()) {
						JsonObject obj = new JsonParser().parse(result.getResult()).getAsJsonObject();
						if (obj.get("code").getAsInt() == 1) {
//							showInfo("签约成功");
							placeOrder(KUAIJIE_PAY);
						} else {
							showInfo(obj.get("desc").getAsString());
						}
					}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				}
			}
		});
	}

	private void start(String channel) {
		if (TextUtils.isEmpty(UserUtil.getToken())) {
			showInfo("长时间未登录，请先登录");
		} else {
			transDialog = ProgressDialog.show(PayActivity.this, null, "提交中...", true, false);
			if(type.equals("1") || type.equals("2") || type.equals("3")){
				placeOrder(channel);
			} else if(type.equals("-3")){
				placeOrder2(channel);
			}

		}
	}

	private void placeOrder(final String channel) {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("payFeeId", payFeeId);
					params.put("token", UserUtil.getToken());
					params.put("busType", "1");//1支付（用于下订单时支付首付金额）2还款 3全额还款
					params.put("type", channel);
//					if(buyType.equals("2")){
//						params.put("number",  number);
//					} 
//					params.put("bizFrom", "2");//buyType=2,3的时候传1奖励金抵用或2不抵用。buyType=1时传入2
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"pay/generatePayOrders", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(PayActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							JsonObject obj = new JsonParser().parse(result.getResult()).getAsJsonObject();
							if (obj.get("code").getAsInt() == 1) {
								if (WUKA_PAY.equals(channel)) {
									String resp = obj.get("result").getAsString();
									sendWuKaPay(resp);
								} else if (ALI_PAY.equals(channel)){
									sendAliPay(obj.get("result").getAsJsonObject());
								} else if (BANKCARD_PAY.equals(channel) || KUAIJIE_PAY.equals(channel)){
									sendBanCard(obj.get("result").getAsJsonObject());
								} else {
									showInfo("调用失败");
								}
							} else if(obj.get("code").getAsInt() == 9999) {
								
								runOnUiThread(new Runnable() {
									public void run() {
										if (transDialog != null && transDialog.isShowing()) {
											transDialog.dismiss();
										}
										showDailog3();
									}
								});
							} else {
								showInfo(obj.get("desc").getAsString());
							}
						}
						}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
						}
					});
				}
			}
		});

	}

	private void placeOrder2(final String channel) {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("frozenId", frozenFeeOrder.getFrozenId() + "");
					params.put("token", UserUtil.getToken());
					params.put("busType", "1");//1支付（用于下订单时支付首付金额）2还款 3全额还款
					params.put("type", channel);
//					if(buyType.equals("2")){
//						params.put("number",  number);
//					}
//					params.put("bizFrom", "2");//buyType=2,3的时候传1奖励金抵用或2不抵用。buyType=1时传入2
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"pay/generatePayOrders2", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(PayActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							JsonObject obj = new JsonParser().parse(result.getResult()).getAsJsonObject();
							if (obj.get("code").getAsInt() == 1) {
								if (WUKA_PAY.equals(channel)) {
									String resp = obj.get("result").getAsString();
									sendWuKaPay(resp);
								} else if (ALI_PAY.equals(channel)){
									sendAliPay(obj.get("result").getAsJsonObject());
								} else if (BANKCARD_PAY.equals(channel) || KUAIJIE_PAY.equals(channel)){
									sendBanCard(obj.get("result").getAsJsonObject());
								} else {
									showInfo("调用失败");
								}
							} else if(obj.get("code").getAsInt() == 9999) {

								runOnUiThread(new Runnable() {
									public void run() {
										if (transDialog != null && transDialog.isShowing()) {
											transDialog.dismiss();
										}
										showDailog3();
									}
								});
							} else {
								showInfo(obj.get("desc").getAsString());
							}
						}
						}
				} catch (Exception e) {
					showInfo(getResources().getString(R.string.A2));
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}
						}
					});
				}
			}
		});

	}
	
	
	//连连支付
	private void sendWuKaPay(String node) {
		MobileSecurePayer msp = new MobileSecurePayer();
		boolean bRet = msp.payAuth(node, mHandler, Constants.RQF_PAY, PayActivity.this, false);
	}
	
    private Handler mHandler = createHandler();

    private Handler createHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {
                String strRet = (String) msg.obj;
                switch (msg.what) {
                    case Constants.RQF_PAY: {
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");

                        // 成功
                        if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
                            // TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
//                                BaseHelper.showDialog(PayActivity.this, "提示",
//                                        "支付成功，交易状态码：" + retCode,android.R.drawable.ic_dialog_alert);
                                
                                
                                Intent intent = new Intent(PayActivity.this, PaySuccessActivity_.class);
                                intent.putExtra("desc", "支付成功");
                                PayActivity.this.startActivity(intent);
    							PayActivity.this.finish();
                            
    
                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                            // TODO 处理中，掉单的情形
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING.equalsIgnoreCase(resulPay)) {
                                BaseHelper.showDialog(PayActivity.this, "提示",
                                        objContent.optString("ret_msg") + "交易状态码："+ retCode,
                                        android.R.drawable.ic_dialog_alert);
                            }

                        } else {
                            // TODO 失败
                            BaseHelper.showDialog(PayActivity.this, "提示", retMsg
                                    + "，交易状态码:" + retCode,android.R.drawable.ic_dialog_alert);
                        }
                    }
                        break;
                }
                super.handleMessage(msg);
            }
        };

    }
	
	private void sendAliPay(JsonObject node) {
		String repInfo = node.get("payOrderResp").toString().replace("\\", "");
		String payInfo = repInfo.substring(1, repInfo.length() - 1);
		
		Intent intent = new Intent(PayActivity.this,AliPayActivity_.class);
		intent.putExtra("paySn", node.get("sendTn").getAsString());
		intent.putExtra("payInfo", payInfo);
		intent.putExtra("type", type);
		startActivityForResult(intent, 0);
	}
	
	private void sendBanCard(JsonObject node) {
		Intent intent = new Intent(PayActivity.this, PaySuccessActivity_.class);
        intent.putExtra("desc", "支付成功");
        PayActivity.this.startActivity(intent);
		PayActivity.this.finish();
	}
	
	private void showDailog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(PayActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage("请先通过银行卡认证！");
		builder.setPositiveButton("去认证", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(PayActivity.this,MyBankCardActivity_.class);
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
	
	private void showDailog2() {
		CustomDialog.Builder builder = new CustomDialog.Builder(PayActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(NumReplaceUtil.newBankNum(GlobalPara.getOutUserBank().getBankNo()) + "\n是否同意以代扣方式支付");
		builder.setPositiveButton("同意", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				start(BANKCARD_PAY);
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
	
	private void showDailog3() {
		CustomDialog.Builder builder = new CustomDialog.Builder(PayActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage("您绑定的银行卡未签约快捷支付，是否签约？");
		builder.setPositiveButton("签约", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				initQuickPayCode();
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
	
	private void showDialog4(final String sn) {
		runOnUiThread(new Runnable() {
			public void run() {
				final CustomDialog.Builder builder = new CustomDialog.Builder(PayActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage("请输入短信验证码");
				builder.setEtMessageHint1("请输入短信验证码");
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String smsCode = builder.getEtMessage1();
						if (smsCode != null && smsCode.length() > 0) {
							if (transDialog != null && transDialog.isShowing()) {
								transDialog.dismiss();
							}

							initQuickCode(sn,smsCode);
							dialog.dismiss();
						} else {
							showInfo("输入的验证码不能为空");
						}
					}
				});
				
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (transDialog != null && transDialog.isShowing()) {
							transDialog.dismiss();
						}
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(PayActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
