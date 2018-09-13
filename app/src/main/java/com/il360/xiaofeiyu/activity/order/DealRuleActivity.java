package com.il360.xiaofeiyu.activity.order;

import java.text.DecimalFormat;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.home.SignNameActivity;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.util.ChangeAmountUtil;
import com.il360.xiaofeiyu.util.UserUtil;

import android.content.Intent;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
@EActivity(R.layout.act_deal_rule)
public class DealRuleActivity extends BaseWidgetActivity {
	@ViewById
	WebView wv_show;
	
	@ViewById
	TextView tvBack;
	@ViewById
	TextView tvSign;
	
//	@ViewById LinearLayout llSignature;
//	@ViewById ImageView ivSignature;
//	@ViewById RelativeLayout rlSignature;
//	@ViewById Button btnSubmit;
//	
//	private String signNamePic = null;
//	protected ProgressDialog transDialog;
//	
//	private String cosPath = "";
//	private String srcPath = "";
//	private String sourceURL = "";
//	private String dealPwd;
	private String ChangeAmount;
	DecimalFormat df = new DecimalFormat("#0.00");
	
//	@Extra Order order;
	@Extra int type;
	
	
	@AfterViews
	void init(){
		ChangeAmount = ChangeAmountUtil.change(Double.parseDouble(UserUtil.getValuationInfo().getMoney()) * 0.25);
//		transDialog = ProgressDialog.show(DealRuleActivity.this, null, "加载中...", true);
		wv_show.requestFocus(); // 请求焦点
		wv_show.getSettings().setJavaScriptEnabled(true); // 设置是否支持JavaScript
		wv_show.getSettings().setSupportZoom(true); // 设置是否支持缩放
		wv_show.getSettings().setBuiltInZoomControls(false); // 设置是否显示内建缩放工具
		wv_show.setHorizontalScrollBarEnabled(false);//滚动条水平是否显示
		wv_show.setVerticalScrollBarEnabled(false); //滚动条垂直是否显示
		wv_show.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		wv_show.loadUrl("http://www.qianjb.com/xfy/shouhouhuizu.html"
				+ "?name=" + GlobalPara.getOutUserRz().getName() 
				+ "&orderNo=" + UserUtil.getValuationInfo().getOrderNo()
				+ "&principal=" + df.format(Double.parseDouble(UserUtil.getValuationInfo().getMoney()) * 0.25) + "元"
				+ "&principal1=" + ChangeAmount ); // 加载在线网页
		wv_show.setWebViewClient(new MyWebViewClient());
	}
	
	
	private class MyWebViewClient extends WebViewClient {

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			
//			llSignature.setVisibility(View.VISIBLE);
//			btnSubmit.setVisibility(View.VISIBLE);
//			
//			if (transDialog != null && transDialog.isShowing()) {
//				transDialog.dismiss();
//			}
			return true;
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
		}
	}
	
	@Click
	void tvBack(){
		DealRuleActivity.this.finish();
	}
	
	@Click
	void tvSign(){
		Intent intent = new Intent(DealRuleActivity.this, SignNameActivity.class);
		startActivityForResult(intent, 0);
	}
	
	
//	final class MyWebViewClient extends WebViewClient {
//		/**
//		 * 拦截 url 跳转,在里边添加点击链接跳转或者操作
//		 */
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			return super.shouldOverrideUrlLoading(view, url);
//		}
//
//		@Override
//		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//			handler.proceed();
//		}
//
//		// 页面载入前调用
//		@Override
//		public void onPageStarted(WebView view, String url, Bitmap favicon) {
//			super.onPageStarted(view, url, favicon);
//		}
//
//		// 页面载入完成后调用
//		@Override
//		public void onPageFinished(WebView view, String url) {
//			
//			llSignature.setVisibility(View.VISIBLE);
//			btnSubmit.setVisibility(View.VISIBLE);
//			
//			if (transDialog != null && transDialog.isShowing()) {
//				transDialog.dismiss();
//			}
////			String js = "document.getElementById('name').innerHTML =" + "杨利强" + ";";
////			view.loadUrl("javascript:" + js);
//			super.onPageFinished(view, url);
//		}
//
//		@SuppressWarnings("deprecation")
//		@Override
//		/**
//		 * 在每一次请求资源时，都会通过这个函数来回调
//		 */
//		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//			Log.d(TAG, "shouldInterceptRequest " + url);
//			return super.shouldInterceptRequest(view, url);
//		}
//
//	}
	
//	@Click
//	void rlSignature(){
//		Intent intent = new Intent(DealRuleActivity.this, SignNameActivity.class);
//		startActivityForResult(intent, 0);
//	}
//	
//	@Click
//	void btnSubmit() {
//		if (isOk()) {
//			showDialog("请输入交易密码");
//		}
//	}
//	
//	private boolean isOk() {
//		if (signNamePic == null) {
//			showInfo("请签署电子合同");
//		} else {
//			return true;
//		}
//		return false;
//	}
//
//	protected void initSign() {
//		btnSubmit.setClickable(false);
//		transDialog = ProgressDialog.show(DealRuleActivity.this, null, getString(R.string.C16), true);
//		ExecuteTask.execute(new Runnable() {
//
//			@Override
//			public void run() {
//
//				try {
//					Map<String, String> params = new HashMap<String, String>();
//					params.put("token", UserUtil.getToken());
//					params.put("type", "5");// 3身份证，5签名，6头像
//					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign",
//							params);
//					if (result.getSuccess()) {
//						JSONObject obj = new JSONObject(result.getResult());
//						if (obj.getInt("code") == 1) {
//							upSignPic(obj.getString("result"));
//						} else {
//							showInfo(obj.getString("desc"));
//						}
//					} else {
//						showInfo(getString(R.string.A6));
//					}
//				} catch (Exception e) {
//					showInfo(getString(R.string.A2));
//					Log.e("DealRuleActivity", "initSign()", e);
//					LogUmeng.reportError(DealRuleActivity.this, e);
//				} finally {
//					if (transDialog != null && transDialog.isShowing()) {
//						transDialog.dismiss();
//					}
//					btnSubmit.setClickable(true);
//				}
//			}
//		});
//	}
//	
//	private void upSignPic(final String sign){
//		PutObjectRequest putObjectRequest = new PutObjectRequest();
//		putObjectRequest.setBucket(GlobalPara.getCosName());
//
//		cosPath = "/sign/" + FileUtil.cosFileName("userSign", UserUtil.getUserInfo().getUserId()) + ".png";
//		putObjectRequest.setCosPath(cosPath);
//		putObjectRequest.setSrcPath(srcPath);
//		putObjectRequest.setSign(sign);
//		putObjectRequest.setInsertOnly("1");//0允许覆盖，1不允许覆盖
//
//		putObjectRequest.setListener(new IUploadTaskListener() {
//			@Override
//			public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
//
//				PutObjectResult result = (PutObjectResult) cosResult;
//				if (result.code == 0) {
//					sourceURL = result.source_url;
////					PicUtil.deleteTempFile(srcPath);
//					initOrder();
//				}
//			}
//
//			@Override
//			public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
//				sourceURL = "";
//				showInfo("签名上传失败");
//			}
//
//			@Override
//			public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
//			}
//
//			@Override
//			public void onCancel(COSRequest arg0, COSResult arg1) {
//			}
//		});
//		WelcomeActivity.getCOSClient().putObject(putObjectRequest);
//	}
//	
//	protected void initOrder() {
//		ExecuteTask.execute(new Runnable() {
//
//			@Override
//			public void run() {
//				try {
//					Map<String, String> params = new HashMap<String, String>();
//					params.put("orderJson", makeJson());
//					params.put("token", UserUtil.getToken());
//					params.put("type", type + "");
//					params.put("payPwd", AESEncryptor.encrypt(ThreeDES.encryptDESCBC(dealPwd)));
//					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
//							"order/insertOrder", params);
//					if (result.getSuccess()) {
//						final JSONObject obj = new JSONObject(result.getResult());
//						if (obj.getInt("code") == 1) {
//							if (type == 1) {
//								Intent intent = new Intent(DealRuleActivity.this, PayActivity_.class);
//								intent.putExtra("number", "0");
//								intent.putExtra("orderNo", obj.getString("result"));
//								intent.putExtra("buyType", "1");
//								intent.putExtra("money", order.getFirstPay() + "");
//								startActivity(intent);
//							} else {
//								showInfo("下单成功!");
//								Intent intent = new Intent(DealRuleActivity.this, RepaymentActivity_.class);
//								startActivity(intent);
//							}
//						} else {
//							showInfo(obj.getString("desc"));
//						}
//					} else {
//						showInfo(getString(R.string.A6));
//					}
//				} catch (Exception e) {
//					showInfo(getString(R.string.A2));
//					Log.e("PlaceOrderActivity", "initOrder", e);
//				} 
//			}
//		});
//		
//	}
//	
//	
//	protected String makeJson() {
//		order.setTxySignPic(sourceURL);
//		return FastJsonUtils.getJsonString(order);
//	}
//
//	private void showDialog(final String message) {
//
//		final DealPwdDialog.Builder builder = new DealPwdDialog.Builder(DealRuleActivity.this);
//		builder.setTitle(R.string.app_name);
//		builder.setMessage(message);
//		builder.setEtMessageHint1("请输入6位交易密码");
//		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dealPwd = builder.getEtMessage1();
//				if (!TextUtils.isEmpty(dealPwd)) {
//					if(dealPwd.length() == 6){
//						initSign();
//						dialog.dismiss();
//					} else {
//						showInfo("交易密码为6位数字");
//					}
//				} else {
//					showInfo("输入的交易密码不能为空");
//				}
//			}
//		});
//
//		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//
//	}
//	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED && data != null) {
			setResult(RESULT_CANCELED, data);
			DealRuleActivity.this.finish();
		}
	}
//	
//	private void showInfo(final String info) {
//		runOnUiThread(new Runnable() {
//			public void run() {
//				if (transDialog != null && transDialog.isShowing()) {
//					transDialog.dismiss();
//				}
//				Toast.makeText(DealRuleActivity.this, info, Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
	
}
