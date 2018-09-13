package com.il360.xiaofeiyu.activity.order;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.WelcomeActivity;
import com.il360.xiaofeiyu.activity.main.MainActivity_;
import com.il360.xiaofeiyu.activity.user.DeliveryAddressActivity_;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.hua.ArrayOfCardConfig;
import com.il360.xiaofeiyu.model.hua.CardConfig;
import com.il360.xiaofeiyu.model.order.Order;
import com.il360.xiaofeiyu.model.order.Stages;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.FileUtil;
import com.il360.xiaofeiyu.util.PicUtil;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.SystemUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_leaseapplication)
public class OrderApplicationActivity extends BaseWidgetActivity {
	/***/
	@ViewById
	TextView tvRecoveryPrice, tvServiceCharge, tvBankName, tvBankCard, tvDevicePrice,
				tvDepositPrice, tvDailyRents, tvTimeoutCost, tvFirstFee;
	
	@ViewById 
	RelativeLayout rlAddress;
	@ViewById
	TextView tvName;
	@ViewById
	TextView tvPhone;
	@ViewById
	TextView tvAddress;
	@ViewById
	TextView tvAddAddress;
	@ViewById
	TextView tvFirstRemind;
	@ViewById
	CheckBox cbAgree;
	/**租赁各种协议*/
	@ViewById
	TextView tvRule1;
	/**签署协议*/
	@ViewById
	TextView tvToSign;
	/**提交申请*/
	@ViewById
	TextView tvToSubmit;
	/**电子签名*/
	@ViewById
	ImageView ivSignature;
	DecimalFormat df = new DecimalFormat("#0.00");
	
	@Extra Order order;
	
	private List<CardConfig> configList = null;
	protected ProgressDialog transDialog;
	private String ext2;
	
	private List<Stages> list = new ArrayList<Stages>();
	
	double deposit, valuationFee, dayRent, penaltyRate, firstNeed;
	
	private String addressId = null;
	
	private String signNamePic = null;
	private String cosPath = "";
	private String srcPath = "";
	private String sourceURL = "";
	
	@AfterViews
	void init(){
		initData();
		initAddress();
	}
	
	private void initData() {
		queryConfig();
	}
	
	private void initAddress() {
		if (GlobalPara.getUserAddressList() != null && GlobalPara.getUserAddressList().size() > 0) {
			tvAddAddress.setVisibility(View.GONE);
			tvName.setText(GlobalPara.getUserAddressList().get(0).getName());
			tvPhone.setText(GlobalPara.getUserAddressList().get(0).getPhone());
			tvAddress.setText(GlobalPara.getUserAddressList().get(0).getProvince() + GlobalPara.getUserAddressList().get(0).getCity()
					 + GlobalPara.getUserAddressList().get(0).getArea() + GlobalPara.getUserAddressList().get(0).getAddress());
			addressId = GlobalPara.getUserAddressList().get(0).getAddressId();
			ext2 = GlobalPara.getUserAddressList().get(0).getName() + ";" + GlobalPara.getUserAddressList().get(0).getPhone()
					+ ";" + GlobalPara.getUserAddressList().get(0).getProvince() + GlobalPara.getUserAddressList().get(0).getCity()
					+ GlobalPara.getUserAddressList().get(0).getArea() + GlobalPara.getUserAddressList().get(0).getAddress();
		} else {
			tvAddAddress.setVisibility(View.VISIBLE);
		}
	}
	
	private void queryConfig() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"card/queryConfig", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							JSONObject objRes = obj.getJSONObject("result");
							JSONObject objRetRes = objRes.getJSONObject("returnResult");
							ArrayOfCardConfig arrayOfCardConfig = FastJsonUtils.getSingleBean(objRetRes.toString(),ArrayOfCardConfig.class);
							if (arrayOfCardConfig.getList() != null && arrayOfCardConfig.getList().size() > 0) {
								configList = arrayOfCardConfig.getList();
								GlobalPara.cardConfigList = arrayOfCardConfig.getList();

								for (int i = 0; i < configList.size(); i++) {
									if (configList.get(i).getConfigGroup().equals("penaltyfee") && configList.get(i).getConfigName().equals("deposit")) {
										deposit = Double.parseDouble(configList.get(i).getConfigValue());
									} else if (configList.get(i).getConfigGroup().equals("penaltyfee") && configList.get(i).getConfigName().equals("valuationFee")) {
										valuationFee = Double.parseDouble(configList.get(i).getConfigValue());
									} else if (configList.get(i).getConfigGroup().equals("penaltyfee") && configList.get(i).getConfigName().equals("dayRent")) {
										dayRent = Double.parseDouble(configList.get(i).getConfigValue());
									} else if(configList.get(i).getConfigGroup().equals("penaltyfee") && configList.get(i).getConfigName().equals("penaltyfee")){
										penaltyRate = Double.parseDouble(configList.get(i).getConfigValue());
									}  else if(configList.get(i).getConfigGroup().equals("penaltyfee") && configList.get(i).getConfigName().equals("firstNeed")){
										firstNeed = Double.parseDouble(configList.get(i).getConfigValue());
									} 
								}
							}
						} else {
							showInfo(obj.getString("desc"));
						}
					}
				} catch (Exception e) {
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
								tvRecoveryPrice.setText(df.format(Double.parseDouble(order.getMoney())) + "元");
								tvServiceCharge.setText(df.format(valuationFee) + "元");
								tvBankName.setText(GlobalPara.getOutUserBank().getBankName());
								tvBankCard.setText("(" + GlobalPara.getOutUserBank().getBankNo() + ")");
								tvDevicePrice.setText(df.format(Double.parseDouble(order.getMoney())) + "元");
								tvDepositPrice.setText(df.format(Double.parseDouble(order.getMoney()) * deposit) + "元");
								tvDailyRents.setText(df.format(Double.parseDouble(order.getMoney()) * dayRent) + "元");
								tvTimeoutCost.setText(df.format(Double.parseDouble(order.getMoney()) * penaltyRate) + "元");
								tvFirstFee.setText(df.format(Double.parseDouble(order.getMoney()) * dayRent * firstNeed) + "元");
								tvFirstRemind.setText("*租赁时间越长，日租费用越低，首次租期" + (int)firstNeed +"天");
							}
					});
					}
					
				}
		});
	}

	@Click
	void tvRule1(){
		Intent intent = new Intent(OrderApplicationActivity.this,DealRuleActivity_.class);
		startActivityForResult(intent, 0);
	}
	
	@Click
	void tvToSign(){
		Intent intent = new Intent(OrderApplicationActivity.this,DealRuleActivity_.class);
		startActivityForResult(intent, 0);
	}
	
	@Click
	void tvToSubmit(){
		if(isOk()){
			initSign();
		}
	}

	private boolean isOk() {
		if (TextUtils.isEmpty(addressId)) {
			showInfo("请先选择添加收货地址");
		} else if (!cbAgree.isChecked()) {
			showInfo("请先阅读协议并同意");
		}  else if (!(GlobalPara.getOutUserBank() != null && GlobalPara.getOutUserBank().getStatus() == 1)) {
			showInfo("请先通过银行卡认证");
		} else {
			return true;
		}
		return false;
	}
	
	@Click
	void rlAddress() {
		Intent intent = new Intent(OrderApplicationActivity.this, DeliveryAddressActivity_.class);
		intent.putExtra("code", 1001);
		startActivityForResult(intent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1001 && data != null) {
			tvAddAddress.setVisibility(View.GONE);
			tvName.setText(data.getExtras().getString("name"));
			tvPhone.setText(data.getExtras().getString("phone"));
			tvAddress.setText(data.getExtras().getString("address"));
			addressId = data.getExtras().getString("addressId");
			ext2 = data.getExtras().getString("name") + ";" + data.getExtras().getString("phone") + ";"
					+ data.getExtras().getString("address");
		} else if (resultCode == RESULT_CANCELED && data != null) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						Bitmap signBitmap = PicUtil.getSmallBitmap(data.getExtras().getString("signNamePic"));
						ivSignature.setImageBitmap(signBitmap);
						signNamePic = PicUtil.bitmapToString(signBitmap, 30);
						srcPath = data.getExtras().getString("signNamePic");
						cbAgree.setChecked(true);
					} catch (Exception e) {
						Log.e("PlaceOrderActivity", "onActivityResult", e);
					}
				}
			});
		}
	}
	
	protected void initOrder() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					order.setExt2(ext2);
					Map<String, String> params = new HashMap<String, String>();
					params.put("orderJson", FastJsonUtils.getJsonString(order));
					params.put("token", UserUtil.getToken());
					params.put("imei", SystemUtil.getIMEI(getBaseContext()));
					params.put("txyUrl", sourceURL);
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"lease/insertLeaseOrder", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(OrderApplicationActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							final JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								showInfo("订单提交成功！");
								Intent intent = new Intent(OrderApplicationActivity.this, MainActivity_.class);
								intent.putExtra("mShowTabIndex", 3);
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
				} 
			}
		});
		
	}
	
	protected void initSign() {
		tvToSubmit.setClickable(false);
		transDialog = ProgressDialog.show(OrderApplicationActivity.this, null, getString(R.string.C16), true);
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {

				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("type", "5");// 3身份证，5签名，6头像
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign",
							params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							upSignPic(obj.getString("result"));
						} else {
							showInfo(obj.getString("desc"));
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("PlaceOrderActivity", "initSign()", e);
					LogUmeng.reportError(OrderApplicationActivity.this, e);
				} finally {
					if (transDialog != null && transDialog.isShowing()) {
						transDialog.dismiss();
					}
					tvToSubmit.setClickable(true);
				}
			}
		});
	}
	
	private void upSignPic(final String sign) {
		PutObjectRequest putObjectRequest = new PutObjectRequest();
		putObjectRequest.setBucket(GlobalPara.getCosName());

		cosPath = "/sign/" + FileUtil.cosFileName("userSign", UserUtil.getUserInfo().getUserId()) + ".png";
		putObjectRequest.setCosPath(cosPath);
		putObjectRequest.setSrcPath(srcPath);
		putObjectRequest.setSign(sign);
		putObjectRequest.setInsertOnly("1");// 0允许覆盖，1不允许覆盖

		putObjectRequest.setListener(new IUploadTaskListener() {
			@Override
			public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

				PutObjectResult result = (PutObjectResult) cosResult;
				if (result.code == 0) {
					sourceURL = result.source_url;
					// PicUtil.deleteTempFile(srcPath);
					initOrder();
				}
			}

			@Override
			public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
				sourceURL = "";
				showInfo("签名上传失败");
			}

			@Override
			public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
			}

			@Override
			public void onCancel(COSRequest arg0, COSResult arg1) {
			}
		});
		WelcomeActivity.getCOSClient().putObject(putObjectRequest);
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(OrderApplicationActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
