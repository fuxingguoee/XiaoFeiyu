package com.il360.xiaofeiyu.activity.mydata;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.alipay.AliBank;
import com.il360.xiaofeiyu.model.alipay.AliUserInfo;
import com.il360.xiaofeiyu.model.alipay.AlipayInfo;
import com.il360.xiaofeiyu.model.alipay.ArrayOfBankInfo;
import com.il360.xiaofeiyu.model.alipay.BankInfo;
import com.il360.xiaofeiyu.model.alipay.BillItem;
import com.il360.xiaofeiyu.model.alipay.BillList;
import com.il360.xiaofeiyu.model.alipay.RsaUi;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.FileHelper;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.alipay.HttpUtils;
import com.il360.xiaofeiyu.view.CustomDialog;
import com.il360.xiaofeiyu.view.CustomDialog1;
import com.il360.xiaofeiyu.view.TextProgressBar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 上传日志错误步骤 1登录；2余额宝；3个人信息；4花呗额度；5银行卡；6获取消费记录token；7获取消费；8提交数据
 */

@SuppressLint("SimpleDateFormat")
@EActivity(R.layout.act_huabei_info)
public class HuaBeiActivity extends BaseWidgetActivity {

	@ViewById
	WebView wv_show;
	@ViewById
	TextView tvTextClick;
	@ViewById
	LinearLayout llAuthen;
	@ViewById
	TextProgressBar pbAuthen;
	@ViewById
	ImageView header_image_return;
	@ViewById
	LinearLayout llLongTime;
	@ViewById
	TextView tvTouchMe;

	private Handler mHandler = new Handler();

	protected ProgressDialog transDialog;
	private String mCtoken = "";
	private String mBeginDate = "";
	private String mEndDate = "";
	private int listSize = 0;
	private int pagNum = 0;
	private boolean isNeedCheck = false;
	private boolean isGetqrCode = false;
	private boolean isLastPage = false;
	private int type = 0;

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM");
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
	private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private final SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy年MM月dd日");

	private static final String loginUrl = "https://auth.alipay.com/login/index.htm";
	private static final String afterLoginUrl = "https://my.alipay.com/portal/i.htm";
	private static final String afterLoginUrl2 = "https://personalweb.alipay.com/portal/i.htm";
	private static final String shangHuUrl = "https://shanghu.alipay.com/i.htm";
	private static final String qiYeUrl = "https://uemprod.alipay.com/user/ihome.htm";
	// private static final String verifyIdentityUrl = "https://authem14.alipay.com/login/checkSecurity.htm";//手机短信验证身份
	private static final String verifyIdentityUrl2 = "https://authet15.alipay.com/login/checkSecurity.htm";
	// private static final String verifyIdentityUrl3 = "https://authsu18.alipay.com/login/checkSecurity.htm";
	private static final String verifyIdentityUrl4 = "https://memberprod.alipay.com/account/reg/complete/complete.htm?scene=havanaComplete";

	private static final String consumeUrl = "https://consumeprod.alipay.com/record/standard.htm";
	private static final String consumeUrl2 = "https://consumeprod.alipay.com/record/advanced.htm";
	private static final String huabeiUrl = "https://my.alipay.com/portal/huabeiWithDynamicFont.json";
	private static final String hbZhangdanUrl = "https://my.alipay.com/portal/i.htm";
	private static final String yuebaoUrl = "https://my.alipay.com/portal/mfundWithDynamicFont.json";
	private static final String yueUrl = "https://my.alipay.com/portal/accountWithDynamicFont.json";
	private static final String userInfoUrl = "https://custweb.alipay.com/account/index.htm";
	private final String bankListUrl = String.format("https://zht.alipay.com/asset/bindQuery.json?_input_charset=utf-8&providerType=BANK&t=%s&ctoken=%s", System.currentTimeMillis(), mCtoken);
	private final String loginRQCodeUrl = "https://qr.alipay.com/_d?_b=PAI_LOGIN_DY&amp;securityId=";
	private final String verifyRQCodeUrl = "https://qr.alipay.com/_d?_b=PAI_UNSIGN&amp;securityId=";

	private static final Pattern huabeibalp = Pattern.compile("creditAmountStr.*?>([^<>]*?)<span.*?>([^<>]*?)</span>.*?availableAmountStr.*?>([^<>]*?)<span.*?>([^<>]*?)</span>");
	private static final Pattern zhangdanp = Pattern.compile("<p class=\"ft-gray\">本月账单金额</p>.*?class=\"amount\">([^<>]*?)<span.*?>([^<>]*?)</span>");
	private static final Pattern balancep = Pattern.compile("balanceAmountStr.*?>([^<>]*?)<span.*?>([^<>]*?)</span>");
	private static final Pattern yuep = Pattern.compile("accountAmountStr.*?>([^<>]*?)<span.*?>([^<>]*?)</span>");
	private static final Pattern userInfop = Pattern.compile("<tr><th>真实姓名</th><td><span[^>]*?>([^<>]*?)</span><span[^>]*?>([^<>]*?)</span><span[^>]*?>([^<>]*?)</span><span[^>]*?>([^<>]*?)</span></td><td[^>]*?>.*?</td></tr><tr><th>邮箱</th><td><span[^>]*?>([^<>]*?)</span></td><td[^>]*?>.*?</td></tr><tr><th>手机</th><td><span[^>]*?>([^<>]*?)</span></span></td><td[^>]*?>.*?</td></tr><tr><th>淘宝会员名</th><td>([^<>]*?)</td><td[^>]*?>.*?</td></tr>.*<tr><th>注册时间</th><td>([^<>]*?)</td><td class=\"last\"><span><a[^>]*?>注销账户</a></span></td></tr>");
	private static final Pattern loginRQCodep = Pattern.compile("<input type=\"hidden\" name=\"qrCodeSecurityId\" value=\"([^<>]*?)\">");
	private static final Pattern verifyRQCodep = Pattern.compile("<input type=\"hidden\" name=\"securityId\" value=\"([^<>]*?)\">");

	private static Pattern p1 = Pattern.compile("<td class=\"name\"><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
	private static Pattern p2 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
	private static Pattern p3 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?><span>([^<>]*?)</span></p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
	private static Pattern p4 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><p[^>]*?>[^>]*?</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");

	private static Pattern p11 = Pattern.compile("<td class=\"name\"><p class=\"consume-title\"><a[^>]*?>([^<>]*?)</a></p></td>.*?<td class=\"other\"><p class=\"name\">([^<>]*?)</p>.*?<td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p>([^<>]*?)</p>.*?<option[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</option>");
	private static Pattern p12 = Pattern.compile("<td class=\"name\"><p class=\"consume-title\">([^<>]*?)</p></td>.*?<td class=\"other\"><p class=\"name\">([^<>]*?)</p>.*?<td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p>([^<>]*?)</p>.*?<option[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</option>");

	// 两个个月记录数
	private static Pattern p = Pattern.compile("<div class=\"page-link\">.*?共([^<>]*?)条.*?</div>");
	private static Pattern pPage = Pattern.compile("<a[^>]*?><span class=\"ui-button-text\">([^<>]*?)</span></a></div></div></div></div></div>");

	// <title>安全校验 - 支付宝</title>
	private static Pattern webTitle = Pattern.compile("<title>([^<>]*?)</title>");

	private List<BankInfo> bankInfoList = new ArrayList<BankInfo>();
	private List<AliBank> aliBankList = new ArrayList<AliBank>();
	private AliUserInfo aliUserInfo = new AliUserInfo();

	private List<BillItem> billTtem1 = new ArrayList<BillItem>();
	private List<BillItem> billTtem2 = new ArrayList<BillItem>();

	private String monthId1 = "";
	private String monthId2 = "";

	private BillList billList1 = new BillList();
	private BillList billList2 = new BillList();

	private int huaBeiAmount = 0;

	/**
	 * 手机信息 型号+sdk版本+版本号
	 */
	private String phoneInfo = "";

	private String mRdsToken = "";
	private String mRdsUa = "";

	@AfterViews
	void init() {
		// tvTextClick.setText("人工审核");
		// tvTextClick.setText("扫码登录");

		// showMessage("\t\t因支付宝限制相册扫码登录或验证身份，所以在支付宝认证前请准备第二个手机(可以借用朋友)并登录您的支付宝以便扫码登录或扫码确认；"
		// + "如果没有第二个手机请选择人工审核提交账号密码，让客服帮您认证！给您带来的不便，我们深感抱歉，谢谢合作！");

		initViews();
		initWebView();
		// getZFBtoken();
	}

	@SuppressWarnings("deprecation")
	private void initViews() {

		phoneInfo = android.os.Build.MODEL + "," + android.os.Build.VERSION.SDK + ","
				+ android.os.Build.VERSION.RELEASE;

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -2);// 得到前2个月
		Date formNow3Month = calendar.getTime();
		mBeginDate = sdf1.format(formNow3Month) + ".01";
		mEndDate = sdf1.format(new Date()) + ".01";

		monthId2 = sdf2.format(formNow3Month);
		calendar.add(Calendar.MONTH, +1);// 得到前1个月
		Date formNow2Month = calendar.getTime();
		monthId1 = sdf2.format(formNow2Month);

		llAuthen.setVisibility(View.GONE);

	}

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void initWebView() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {

				wv_show.requestFocus(); // 请求焦点
				wv_show.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
				wv_show.getSettings().setJavaScriptEnabled(true); // 设置是否支持JavaScript
				wv_show.getSettings().setSupportZoom(true); // 设置是否支持缩放
				wv_show.getSettings().setBuiltInZoomControls(true); // 设置是否显示内建缩放工具
				wv_show.setHorizontalScrollBarEnabled(true);// 滚动条水平是否显示
				wv_show.setVerticalScrollBarEnabled(true); // 滚动条垂直是否显示
				wv_show.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放
				wv_show.getSettings().setDomStorageEnabled(true); // 设置支持DomStorage
				// 这里是注入Java对象
				wv_show.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

				wv_show.loadUrl(loginUrl); // 加载在线网页
				// 触摸焦点起作用
				wv_show.requestFocus();// 如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事

				// Allow third party cookies for Android Lollipop
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
					CookieManager cookieManager = CookieManager.getInstance();
					cookieManager.setAcceptThirdPartyCookies(wv_show, true);
				}

				wv_show.setWebViewClient(new MyWebViewClient());
			}
		});
	}

	final class InJavaScriptLocalObj {
		@JavascriptInterface
		public void showSource(String html, int i) throws Exception {
			// if(isGetqrCode && i == 1){
			// isGetqrCode = false;
			// String htmlStr = HttpUtils.replRtnSpace(html);
			// String qrCodeStr = HttpUtils.getValue(htmlStr,
			// loginRQCodep).replaceAll("\\|", "%257C");
			// String qrURL = loginRQCodeUrl + qrCodeStr;
			// getZFBqrCode(qrURL);
			// } else if(i == 2 || i == 3 || i==4){
			// String htmlStr = HttpUtils.replRtnSpace(html);
			// String qrCodeStr = HttpUtils.getValue(htmlStr,
			// verifyRQCodep).replaceAll("\\|", "%257C");
			// String qrURL = verifyRQCodeUrl + qrCodeStr;
			// getZFBqrCode(qrURL);
			// }
		}
	}

	// 文件存储根目录
	private String getFileRoot(Context context) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File external = context.getExternalFilesDir(null);
			if (external != null) {
				return external.getAbsolutePath();
			}
		}

		return context.getFilesDir().getAbsolutePath();
	}

	final class MyWebViewClient extends WebViewClient {
		// 在WebView中而不是系统默认浏览器中显示页面

		/**
		 * 拦截 url 跳转,在里边添加点击链接跳转或者操作
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (url.startsWith(afterLoginUrl) || url.equals(afterLoginUrl2) || url.equals(consumeUrl)
					|| url.equals(consumeUrl2) || url.equals(qiYeUrl)) {
				wv_show.setVisibility(View.GONE);
				llAuthen.setVisibility(View.VISIBLE);
			}
			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
		}

		// 页面载入前调用
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			tvTextClick.setVisibility(View.GONE);
			llLongTime.setVisibility(View.GONE);

			if (url.startsWith(afterLoginUrl) || url.equals(afterLoginUrl2) || url.equals(consumeUrl)
					|| url.equals(consumeUrl2) || url.equals(qiYeUrl)) {
				wv_show.setVisibility(View.GONE);
				llAuthen.setVisibility(View.VISIBLE);
			} else if (url.equals(verifyIdentityUrl4)) {
				showInfo("请到支付宝官网完善个人信息！");
				view.loadUrl(loginUrl);
			} else if (url.equals(qiYeUrl)) {
				showInfo("请使用个人账号申请认证！");
				view.loadUrl(loginUrl);
			} else if (url.startsWith(
					"https://consumeprod.alipay.com/errorSecurity.htm?functionCode=FUN.SECURITY_POLICY&causeCode=SECURITY_POLICY.web")) {
				showErrorAndOut("用户已取消认证！");// 消费记录二维码确认时点击取消
			} 
			super.onPageStarted(view, url, favicon);
		}

		// 页面载入完成后调用
		@Override
		public void onPageFinished(WebView view, String url) {

			// 注入js
			CookieManager cookieManager = CookieManager.getInstance();
			final String cokieStr = cookieManager.getCookie(url);

			if (url.startsWith(afterLoginUrl) || url.equals(afterLoginUrl2) || url.equals(consumeUrl)
					|| url.equals(consumeUrl2) || url.equals(qiYeUrl)) {
				wv_show.setVisibility(View.GONE);
				llAuthen.setVisibility(View.VISIBLE);
			} else {
				wv_show.setVisibility(View.VISIBLE);
				llAuthen.setVisibility(View.GONE);
			}

			if (url.endsWith("alipay.com/login/checkSecurity.htm?full_redirect=true")) {
				showInfo("账号登录失败，请使用扫码登录！");
				upLogInfo("1", "登录后未跳转到首页", url);
				view.loadUrl(loginUrl);
			} else if (url.equals(shangHuUrl)) {
				view.loadUrl("https://shanghu.alipay.com/home/switchPersonal.htm");
			} else if (url.equals(loginUrl)) {
				type = 1;
				showProgress(10);
				
				String js = "document.getElementById('J-loginMethod-tabs').children[1].click();";
				view.loadUrl("javascript:" + js);
				String js2 = "function removeClassName(name){var eles = document.getElementsByClassName(name);var i;for (i = 0; i < eles.length; i++) {eles[i].remove();}};"
						+ "function setupLoginHomeUI(){removeClassName('authcenter-head');removeClassName('ui-form-other ui-form-other-fg');removeClassName('ui-form-other');};"
						+ "setupLoginHomeUI();";
				view.loadUrl("javascript:" + js2);
//				if (isGetqrCode) {
//					String js = "document.getElementById('J-loginMethod-tabs').firstElementChild.click();";
//					view.loadUrl("javascript:" + js);
//					view.loadUrl(
//							"javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>', 1);");
//				}
//				tvTextClick.setVisibility(View.VISIBLE);
			} else if (url.startsWith(verifyIdentityUrl2)) {// 帐号密码登录时二维码身份认证
				type = 2;
				// tvTextClick.setText("扫码确认");
				// tvTextClick.setVisibility(View.VISIBLE);
				llLongTime.setVisibility(View.VISIBLE);
				view.loadUrl(
						"javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>', 2);");
			} else if (type == 3
					&& url.startsWith("https://consumeprod.alipay.com/record/checkSecurity.htm?securityId=web")) {
				// tvTextClick.setText("扫码确认");
				// tvTextClick.setVisibility(View.VISIBLE);
				llLongTime.setVisibility(View.VISIBLE);
				view.loadUrl(
						"javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>', 3);");
			} else if (type == 4
					&& url.startsWith("https://consumeprod.alipay.com/record/checkSecurity.htm?securityId=web")) {
				// tvTextClick.setText("扫码确认");
				// tvTextClick.setVisibility(View.VISIBLE);
				llLongTime.setVisibility(View.VISIBLE);
				view.loadUrl(
						"javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>', 4);");
			} else if (cokieStr != null && (url.startsWith(afterLoginUrl) || url.equals(afterLoginUrl2))) {
				ExecuteTask.execute(new Runnable() {
					@Override
					public void run() {
						captureData(cokieStr);
					}
				});
//			} else if (!isNeedCheck && url.equals(consumeUrl)) {
//				ExecuteTask.execute(new Runnable() {
//					@Override
//					public void run() {
//						boolean consumeb = getConsume(consumeUrl, cokieStr, 1);// 第一页消费记录（主要获取2个月的总条数）
//						if (consumeb) {
//							showProgress(40);
//							if (listSize % 20 > 0) {
//								pagNum = (listSize / 20) + 1;
//							} else {
//								pagNum = listSize / 20;
//							}
//
//							for (int i = 1; i < pagNum; i++) { // 第二页开始获取所有消费记录
//								if(!HuaBeiActivity.this.isFinishing()){
//									try {
//										Thread.sleep(1000);// 停1秒
//									} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//	
//									boolean consumeb2 = getConsume(consumeUrl, cokieStr, i + 1);
//									if (consumeb2) {
//										int j = (50 * i) / (pagNum - 1) + 40 - pbAuthen.getProgress();
//										addProgress(j);//
//										
//										if(pagNum == (i+1)){
//											showProgress(90);
//											initPostZfbData();
//										}
//									} else {
//										if (isNeedCheck) {
//											initConsume();
//										} else {
//											showErrorAndOut("获取数据失败，请重试！");
//										}
//										break;
//									}
//								}
//							}
//						} else {
//							if (isNeedCheck) {
//								initConsume();
//							} else {
//								showErrorAndOut("获取数据失败，请重试！");
//							}
//						}
//					}
//				});
//			} else if (isNeedCheck && url.equals(consumeUrl)) {
//				type = 3;
//				isNeedCheck = false;
//				String js = "document.getElementById(\"J-three-month\").click();";
//				Log.w("ATTACK", "inject js, js= " + js);
//				view.loadUrl("javascript:" + js);
//			} else if (isNeedCheck && url.equals(consumeUrl2)) {
//				type = 4;
//				isNeedCheck = false;
//				String js = "document.getElementById(\"J-set-query-form\").click();";
//				Log.w("ATTACK", "inject js, js= " + js);
//				view.loadUrl("javascript:" + js);	
			}

			super.onPageFinished(view, url);
		}

		@SuppressWarnings("deprecation")
		@Override
		/**
		 * 在每一次请求资源时，都会通过这个函数来回调
		 */
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
			Log.d(TAG, "shouldInterceptRequest " + url);
			return super.shouldInterceptRequest(view, url);
		}
	}

	private void captureData(String cookie) {
		mCtoken = getCtoken(cookie);
		boolean yuebaob = getBalance(yuebaoUrl, cookie);// 余额宝
		if (yuebaob) {
			addProgress(10);// +10%
		}
		
		boolean yue = getYue(yueUrl, cookie);// 账户余额
		if (yue) {
			addProgress(10);// +10%
		}
		
		boolean hbZhangdan = getHbZhangdan(hbZhangdanUrl, cookie);// 花呗账单
		if (hbZhangdan) {
			addProgress(10);// +10%
		}
		
		boolean huabeib = getHuaBei(huabeiUrl, cookie);// 花呗额度
		if (huabeib) {
			addProgress(10);// +10%
		}
		
		boolean userinfob = getUserInfo(userInfoUrl, cookie);// 个人信息
		if (userinfob) {
			addProgress(10);// +10%

			boolean bankListb = getBankList(bankListUrl, cookie);// 银行卡
			if (bankListb) {
				addProgress(10);// +10%
			}
			 initPostZfbData();
		} else {
			showErrorAndOut("获取数据失败，请重试！");
		}

	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initConsume() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (billTtem1 != null && billTtem1.size() > 0) {
					billTtem1.clear();
				}
				if (billTtem2 != null && billTtem2.size() > 0) {
					billTtem2.clear();
				}
				wv_show.loadUrl(consumeUrl);
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initConsume2() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				if (billTtem1 != null && billTtem1.size() > 0) {
					billTtem1.clear();
				}
				if (billTtem2 != null && billTtem2.size() > 0) {
					billTtem2.clear();
				}
				wv_show.loadUrl(consumeUrl2);
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initLoginqrCode() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				wv_show.loadUrl(loginUrl);
			}
		});
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initAfterLogin() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				wv_show.loadUrl(afterLoginUrl);
			}
		});
	}

	private void initPostZfbData() {
		if (!HuaBeiActivity.this.isFinishing()) {
			try {
				Map<String, String> params = new HashMap<String, String>();
				params.put("zfbjson", assembleData());
				params.put("token", UserUtil.getToken());
				params.put("amount", huaBeiAmount + "");
				TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "card/postZfbData",
						params);
				if (result.getSuccess()) {
					JSONObject obj = new JSONObject(result.getResult());
					if (obj.getInt("code") == 1) {
						addProgress(20);// +30%
					} else {
						upLogInfo("8", "card/postZfbData接口返回obj.getInt(\"code\")为" + obj.getInt("code"),
								result.getResult());
						showErrorAndOut(obj.getString("desc"));
					}
				} else {
					upLogInfo("8", "card/postZfbData接口返回result.getSuccess()为false", result.getResult());
					showErrorAndOut("认证失败，请重试！");
				}
			} catch (Exception e) {
				upLogInfo("8", "方法initPostZfbData报异常", e.getMessage());
				showErrorAndOut("认证失败，请重试！");
			} finally {
				if (!HuaBeiActivity.this.isFinishing()) {
					HuaBeiActivity.this.finish();
				}
			}
		}
	}

	private String assembleData() {

		AlipayInfo alipayInfo = new AlipayInfo();

		billList1.setMonthId(monthId1);
		billList1.setBillList(billTtem1);

		billList2.setMonthId(monthId2);
		billList2.setBillList(billTtem2);

		List<BillList> AlibillList = new ArrayList<BillList>();
		AlibillList.clear();
		AlibillList.add(billList1);
		AlibillList.add(billList2);

		alipayInfo.setBankList(aliBankList);
		alipayInfo.setBillList(AlibillList);
		alipayInfo.setUserInfo(aliUserInfo);

		addProgress(5);// +5%

		return FastJsonUtils.getJsonString(alipayInfo);
	}

	private String getCtoken(String cookie) {
		String ctoken = "";
		if (cookie != null) {
			String[] s = cookie.split(";");
			for (int i = 0; i < s.length; i++) {
				if (s[i].startsWith(" ctoken=")) {
					ctoken = s[i].replaceAll(" ctoken=", "");
				}
			}
		}
		addProgress(5);// 登录成功+5%
		return ctoken;
	}

	// 余额宝
	private boolean getBalance(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "*/*");
			// heades.put("Accept-Encoding", "gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", "my.alipay.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://my.alipay.com/portal/i.htm");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");
			heades.put("X-Requested-With", "XMLHttpRequest");

			Map<String, String> params = new HashMap<String, String>();

			params.put("className", "accountWithDynamicFontClass");
			params.put("encrypt", "true");
			params.put("_input_charset", "utf-8");
			params.put("ctoken", mCtoken);
			params.put("_output_charset", "utf-8");

			String result = HttpUtils.get(url, params, heades, Charset.defaultCharset());

			// Log.i("支付宝余额查询响应", result);
			String html = HttpUtils.replRtnSpace(result);

			List<String[]> banleanceList = HttpUtils.getListArray(html, balancep);
			if (banleanceList != null && banleanceList.size() > 0) {
				String ebaoBal = banleanceList.get(0)[0] + banleanceList.get(0)[1];
				aliUserInfo.setEbaoBal(new BigDecimal(ebaoBal.trim()));
			} else {
				aliUserInfo.setEbaoBal(new BigDecimal("0.00"));
				upLogInfo("2", "余额宝获取失败，默认传0", html);
			}

			return true;

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("2", "方法getBalance报异常", e.getMessage());
			return false;
		}
	}

	// 账户余额
	private boolean getYue(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "*/*");
			// heades.put("Accept-Encoding", "gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", "my.alipay.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://my.alipay.com/portal/i.htm");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");
			heades.put("X-Requested-With", "XMLHttpRequest");

			Map<String, String> params = new HashMap<String, String>();

			params.put("className", "mfundWithDynamicFontClass");
			params.put("encrypt", "true");
			params.put("_input_charset", "utf-8");
			params.put("ctoken", mCtoken);
			params.put("_output_charset", "utf-8");

			String result = HttpUtils.get(url, params, heades, Charset.defaultCharset());

			String html = HttpUtils.replRtnSpace(result);

			List<String[]> yueList = HttpUtils.getListArray(html, yuep);
			if (yueList != null && yueList.size() > 0) { // 账户余额
				String accBal = yueList.get(0)[0] + yueList.get(0)[1];
				aliUserInfo.setAccBal(new BigDecimal(accBal.trim()));
			} else {
				aliUserInfo.setAccBal(new BigDecimal("0.00"));
				upLogInfo("2", "账户余额获取失败，默认传0", html);
			}
			return true;

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("2", "方法getYue报异常", e.getMessage());
			return false;
		}
	}

	private FileHelper helper;

	private boolean getHbZhangdan(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Host", "my.alipay.com");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Referer", "https://authgtj.alipay.com/login/certCheck.htm");
			heades.put("Upgrade-Insecure-Requests", "1");
			heades.put("Cookie", cookie);
			heades.put("Connection", "keep-alive");
			heades.put("Pragma", "no-cache");
			heades.put("Cache-Control", "max-age=0, no-cache");
			
			String result = HttpUtils.get(url, heades, Charset.defaultCharset());
			
			String html = HttpUtils.replRtnSpace(result);

			List<String[]> zhangdanList = HttpUtils.getListArray(html, zhangdanp);
			if (zhangdanList != null && zhangdanList.size() > 0) {
				String zhangdan = zhangdanList.get(0)[0] + zhangdanList.get(0)[1];
				aliUserInfo.setBillAmount(new BigDecimal(zhangdan.trim()));
			} else {
				upLogInfo("4", "支付宝花呗账单获取失败（可能未出账单），默认传空", html);
			}
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("4", "方法getHbZhangdan报异常", e.getMessage());
			return false;
		}
	}

	private boolean getHuaBei(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Host", "my.alipay.com");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");
			heades.put("Accept", "*/*");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Referer", "https://my.alipay.com/portal/i.htm");
			heades.put("X-Requested-With", "XMLHttpRequest");
			heades.put("Cookie", cookie);
			heades.put("Connection", "keep-alive");
			heades.put("Pragma", "no-cache");
			heades.put("Cache-Control", "no-cache");

			Map<String, String> params = new HashMap<String, String>();

			params.put("className", "huabeiWithDynamicFontClass");
			params.put("encrypt", "true");
			params.put("_input_charset", "utf-8");
			params.put("ctoken", mCtoken);
			params.put("_output_charset", "utf-8");

			String result = HttpUtils.get(url, params, heades, Charset.defaultCharset());

			// Log.i("支付宝花呗查询响应", result);
			String html = HttpUtils.replRtnSpace(result);

			// helper = new FileHelper(getApplicationContext());
			// try {
			// helper.createSDFile("test.txt");
			// helper.writeSDFile(html, "test.txt");
			// } catch (IOException e) {
			// e.printStackTrace();
			// }

			List<String[]> huabeiList = HttpUtils.getListArray(html, huabeibalp);
			if (huabeiList != null && huabeiList.size() > 0) { // 与是否开通花呗有关
				String available = huabeiList.get(0)[2] + huabeiList.get(0)[3];
				aliUserInfo.setUsedAmount(new BigDecimal(available.trim()));

				String total = huabeiList.get(0)[0] + huabeiList.get(0)[1];
				huaBeiAmount = (int) ((Double.valueOf(total) * 100) / 100);
				aliUserInfo.setAmount(new BigDecimal(total.trim()));
			} else {
				upLogInfo("4", "花呗额度获取失败（可能未开通）", html);
				return false;
			}
			return true;

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("4", "方法getHuaBei报异常", e.getMessage());
			return false;
		}
	}

	private boolean getUserInfo(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Host", "custweb.alipay.com");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			heades.put("Referer","https://my.alipay.com/portal/i.htm?referer=https%3A%2F%2Fauth.alipay.com%2Flogin%2Findex.htm");
			heades.put("Cookie", cookie);
			heades.put("Connection", "keep-alive");
			heades.put("Upgrade-Insecure-Requests", "1");
			heades.put("Cache-Control", "max-age=0");
			String result = HttpUtils.get(url, heades, Charset.defaultCharset());
			// Log.i("支付宝个人信息查询响应", result);
			String html = HttpUtils.replRtnSpace(result);
			
			 helper = new FileHelper(getApplicationContext());
			 try {
				 helper.createSDFile("test.txt");
				 helper.writeSDFile(html, "test.txt");
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
			
			List<String[]> accAmtList = HttpUtils.getListArray(html, userInfop);
			if (accAmtList != null && accAmtList.size() > 0) {
				aliUserInfo.setUserName(accAmtList.get(0)[0].trim());
				aliUserInfo.setIdNum(accAmtList.get(0)[2].trim());
				aliUserInfo.setEmail(accAmtList.get(0)[4].equals("未添加邮箱账户名") ? "" : accAmtList.get(0)[4].trim());
				aliUserInfo.setPhone(accAmtList.get(0)[5].trim());
				aliUserInfo.setTbAccount(accAmtList.get(0)[6].equals("未绑定淘宝账户") ? "" : accAmtList.get(0)[6].trim());
				String regTime = accAmtList.get(0)[7].trim();
				Date date = sdf4.parse(regTime);
				String newTime = sdf3.format(date);
				aliUserInfo.setRegTime(newTime);
			} else {
				upLogInfo("3", "支付宝个人信息获取失败", html);
				return false;
			}
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("3", "方法getUserInfo报异常", e.getMessage());
			return false;
		}
	}

	private boolean getBankList(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Host", "zht.alipay.com");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
			heades.put("Accept", "*/*");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			heades.put("Referer", "https://zht.alipay.com/asset/bankList.htm");
			heades.put("X-Requested-With", "XMLHttpRequest");
			heades.put("Cookie", cookie);
			heades.put("Connection", "keep-alive");
			String result = HttpUtils.get(url, heades, Charset.forName("GBK"));
			// Log.i("支付宝获取银行卡返回：", result);
			ArrayOfBankInfo arrayOfBankInfo = FastJsonUtils.getSingleBean(result, ArrayOfBankInfo.class);
			if (arrayOfBankInfo.getStat().equals("ok")) {
				if (arrayOfBankInfo.getResults() != null && arrayOfBankInfo.getResults().size() > 0) {
					bankInfoList = arrayOfBankInfo.getResults();
					aliBankList.clear();
					for (int i = 0; i < bankInfoList.size(); i++) {
						AliBank alibank = new AliBank();
						alibank.setBankId(bankInfoList.get(i).getPartnerId());
						alibank.setBankName(bankInfoList.get(i).getProviderName());
						alibank.setCardSuffix(bankInfoList.get(i).getProviderUserName());
						alibank.setCardTypeName(bankInfoList.get(i).getCardTypeName());
						alibank.setUserName(bankInfoList.get(i).getShowUserName());

						aliBankList.add(alibank);
					}
				}
				return true;
			} else {
				upLogInfo("5", "支付宝绑定银行卡获取失败", result);
				return false;
			}
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("5", "方法getBankList报异常", e.getMessage());
			return false;
		}
	}

	private void getZFBtoken() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/queryrsaUi",
							params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							JSONObject objRes = obj.getJSONObject("result");
							RsaUi rsaUi = FastJsonUtils.getSingleBean(objRes.toString(), RsaUi.class);
							if (rsaUi != null && !TextUtils.isEmpty(rsaUi.getRdsToken())
									&& !TextUtils.isEmpty(rsaUi.getRsaUi())) {
								mRdsToken = rsaUi.getRdsToken();
								mRdsUa = rsaUi.getRsaUi();
							} else {
								upLogInfo("6", "card/queryrsaUi接口返回有空值", result.getResult());
								showErrorAndOut("内部错误，请重试！");
							}
						} else {
							upLogInfo("6", "card/queryrsaUi接口返回obj.getInt(\"code\")为" + obj.getInt("code"),
									result.getResult());
							showErrorAndOut("内部错误，请重试！");
						}
					} else {
						upLogInfo("6", "card/queryrsaUi接口返回result.getSuccess()为false", result.getResult());
						showErrorAndOut("内部错误，请重试！");
					}
				} catch (Exception e) {
					upLogInfo("6", "方法getZFBtoken报异常", e.getMessage());
					showErrorAndOut("内部错误，请重试！");
				}
			}
		});
	}

	/**
	 * 获取帐号1426840839@qq.com 获取时间2017.8.30 16：15
	 **/
	// private String mRdsToken="LU1kACs0yxG50MraF8DTTF7Opj9EH6rb";
	// private String
	// mRdsUa="117n+qZ9mgNqgJnCG0Yus65w7XCvsi/yLDVddU=|nOiH84v4j/yH9I/4j/mN+Fg=|neiHGXz1WexE7V/kge6L84vugeR+0mPEdsx6H78f|mu6b9JEehvJc2F7omyecFm4djTicFW7pfulgGog7lupi6ZAlgOVF|m++T/GIRfglxCWYSZglsH2UMdQ1hDbMKrht+3n4=|mOOM6Yws|meWK74oq|luKW+WcCqR6pGqzRo9F3xXTCespi0WTWcQCkC6cUsAx922nZqx62EKIXsMJ0w2fWcdVgx7Ydqhp/EHUQsBA=|l+GOEGwadQZpHWESZAt/C3AEax9jFmUKfg12BWoeYhdkC38MewioCA==|lO6BH3ofcANsGGMZbc1t|leKNE3YTfAd/C2QSaRJ9CH8LfwenBw==|kuWKFHH8jDuQJY0lVeVX5lH6n/CM9IHum+OW+Yz7g/WNLY0=|k+uEGn/ygjWeK4MrW+tZ6F/0kf5gHGQdcgdwDGMfaBJjG3QBdgFuFmINeA+vwLPcuRKnD6Ymiy+WMoQilzuIP1chpc27Pls0QTZPNUzsTA==|kOqFG34bdABvGm0Ubxa2Fg==|keuEGn/ygjWeK4MrW+tZ6F/0kf6K5ZDnnuWZOZk=|jvmK+Zbliv+Q5JPniP6G/JPrl/iC+pXhleKN+Zbhkv2E65P8iP6R557xh/9f";

	private boolean getConsume(String url, String cookie, int page) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Host", "consumeprod.alipay.com");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			heades.put("Referer", "https://consumeprod.alipay.com/record/advanced.htm");
			heades.put("Content-Type", "application/x-www-form-urlencoded");
			heades.put("Cookie", cookie);
			heades.put("Connection", "keep-alive");
			heades.put("Upgrade-Insecure-Requests", "1");

			Map<String, String> params = new HashMap<String, String>();
			params.put("rdsToken", mRdsToken);
			params.put("rdsUa", mRdsUa);
			params.put("beginDate", mBeginDate);
			params.put("beginTime", "00:00");
			params.put("endDate", mEndDate);
			params.put("endTime", "00:00");
			params.put("dateRange", "customDate");
			params.put("status", "all");
			params.put("keyword", "bizNo");
			params.put("keyValue", "");
			params.put("dateType", "createDate");
			params.put("minAmount", "");
			params.put("maxAmount", "");
			params.put("fundFlow", "all");
			params.put("tradeType", "ALL");
			params.put("categoryId", "");
			params.put("pageNum", page + "");

			String result = HttpUtils.post(url, params, heades, Charset.forName("GBK"));
			if (TextUtils.isEmpty(result)) {
				showErrorAndOut("操作过于频繁，请过几分钟再试！");
			}

			String html = HttpUtils.replRtnSpace(result);
			String title = HttpUtils.getValue(html, webTitle);

			// helper = new FileHelper(getApplicationContext());
			// try {
			// helper.createSDFile("test.txt");
			// helper.writeSDFile(html, "test.txt");
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			List<String[]> consumeList1 = HttpUtils.getListArray(html, p1);
			List<String[]> consumeList2 = HttpUtils.getListArray(html, p2);
			List<String[]> consumeList3 = HttpUtils.getListArray(html, p3);
			List<String[]> consumeList4 = HttpUtils.getListArray(html, p4);

			if (listSize == 0) {
				String listSizeStr = HttpUtils.getValue(html, p);
				if (!TextUtils.isEmpty(listSizeStr)) {
					listSize = Integer.parseInt(listSizeStr.trim());
				} else {
					if (!TextUtils.isEmpty(title) && title.equals("我的账单 - 支付宝")) {
						upLogInfo("7", "获取第一页消费记录以及总条数失败", html);
						isNeedCheck = false;
					} else if (!TextUtils.isEmpty(title) && title.equals("安全校验 - 支付宝")) {
						isNeedCheck = true;
					}
					return false;
				}
			}

			List<String[]> consumeListItem = new ArrayList<String[]>();
			consumeListItem.addAll(consumeList1);
			consumeListItem.addAll(consumeList2);
			consumeListItem.addAll(consumeList3);
			consumeListItem.addAll(consumeList4);

			if (consumeListItem.size() > 0) {
				for (int i = 0; i < consumeListItem.size(); i++) {
					BillItem b = new BillItem();
					b.setBillTitle(consumeListItem.get(i)[0]);
					b.setOtherName(consumeListItem.get(i)[1].trim().substring(0,
							consumeListItem.get(i)[1].trim().length() - 1));
					String money = consumeListItem.get(i)[2];
					if (money.startsWith("-") || money.startsWith("+")) {
						b.setAmount(new BigDecimal(money.substring(1, money.length()).trim()));
					} else {
						b.setAmount(new BigDecimal(money.trim()));
					}
					b.setStatusName(consumeListItem.get(i)[3]);
					b.setBillSn(consumeListItem.get(i)[5]);
					b.setBillTypeName(consumeListItem.get(i)[6]);

					String billTime = consumeListItem.get(i)[4];

					Long lo = sdf.parse(billTime).getTime();

					String billTimeStr = sdf3.format(lo);
					b.setBillTime(billTimeStr);

					String monthId = sdf2.format(lo);
					if (monthId.equals(monthId1)) {
						billTtem1.add(b);
					} else if (monthId.equals(monthId2)) {
						billTtem2.add(b);
					}
				}
			} else {
				if (!TextUtils.isEmpty(title) && title.equals("我的账单 - 支付宝")) {
					upLogInfo("7", "获取第" + page + "页消费记录失败", html);
					isNeedCheck = false;
				} else if (!TextUtils.isEmpty(title) && title.equals("安全校验 - 支付宝")) {
					isNeedCheck = true;
				}
				return false;
			}
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("7", "方法getConsume报异常", e.getMessage());
			return false;
		}
	}

	private boolean getConsume2(String url, String cookie, int page) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Host", "consumeprod.alipay.com");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			heades.put("Referer", "https://consumeprod.alipay.com/record/advanced.htm");
			heades.put("Content-Type", "application/x-www-form-urlencoded");
			heades.put("Cookie", cookie);
			heades.put("Connection", "keep-alive");
			heades.put("Upgrade-Insecure-Requests", "1");

			Map<String, String> params = new HashMap<String, String>();
			params.put("rdsToken", mRdsToken);
			params.put("rdsUa", mRdsUa);
			params.put("beginDate", mBeginDate);
			params.put("beginTime", "00:00");
			params.put("endDate", mEndDate);
			params.put("endTime", "00:00");
			params.put("dateRange", "customDate");
			params.put("status", "all");
			params.put("keyword", "bizNo");
			params.put("keyValue", "");
			params.put("dateType", "createDate");
			params.put("minAmount", "");
			params.put("maxAmount", "");
			params.put("tradeType", "ALL");
			params.put("categoryId", "");
			params.put("pageNum", page + "");

			String result = HttpUtils.post(url, params, heades, Charset.forName("GBK"));
			if (TextUtils.isEmpty(result)) {
				showErrorAndOut("操作过于频繁，请过几分钟再试！");
			}

			String html = HttpUtils.replRtnSpace(result);
			String title = HttpUtils.getValue(html, webTitle);

			// helper = new FileHelper(getApplicationContext());
			// try {
			// helper.createSDFile("test.txt");
			// helper.writeSDFile(html, "test.txt");
			// } catch (Exception e) {
			// e.printStackTrace();
			// }

			List<String[]> consumeList11 = HttpUtils.getListArray(html, p11);
			List<String[]> consumeList12 = HttpUtils.getListArray(html, p12);

			List<String[]> consumeListItem = new ArrayList<String[]>();
			consumeListItem.addAll(consumeList11);
			consumeListItem.addAll(consumeList12);

			if (consumeListItem.size() > 0) {

				if (!TextUtils.isEmpty(title) && title.equals("我的账单 - 支付宝")) {
					String pageName = HttpUtils.getValue(html, pPage);
					if (TextUtils.isEmpty(pageName) || pageName.endsWith("上一页")) {
						isLastPage = true;
					} else {
						isLastPage = false;
					}
				}

				for (int i = 0; i < consumeListItem.size(); i++) {
					BillItem b = new BillItem();
					b.setBillTitle(consumeListItem.get(i)[0].trim());
					b.setOtherName(consumeListItem.get(i)[1].trim());
					String money = consumeListItem.get(i)[2];
					if (money.startsWith("-") || money.startsWith("+")) {
						b.setAmount(new BigDecimal(money.substring(1, money.length()).trim()));
					} else {
						b.setAmount(new BigDecimal(money.trim()));
					}
					b.setStatusName(consumeListItem.get(i)[3]);
					b.setBillSn(consumeListItem.get(i)[5]);
					b.setBillTypeName(consumeListItem.get(i)[6]);

					String billTime = consumeListItem.get(i)[4];

					Long lo = sdf.parse(billTime).getTime();

					String billTimeStr = sdf3.format(lo);
					b.setBillTime(billTimeStr);

					String monthId = sdf2.format(lo);
					if (monthId.equals(monthId1)) {
						billTtem1.add(b);
					} else if (monthId.equals(monthId2)) {
						billTtem2.add(b);
					}
				}
			} else {
				if (!TextUtils.isEmpty(title) && title.equals("我的账单 - 支付宝")) {
					upLogInfo("7", "获取第" + page + "页消费记录失败", html);
					isNeedCheck = false;
				} else if (!TextUtils.isEmpty(title) && title.equals("安全校验 - 支付宝")) {
					isNeedCheck = true;
				}
				return false;
			}
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("7", "方法getConsume报异常", e.getMessage());
			return false;
		}
	}

	private void upLogInfo(final String phase, final String expInfo, final String content) {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("userId", UserUtil.getUserInfo().getUserId().toString());
					params.put("phoneType", "1");
					params.put("type", phoneInfo);
					params.put("phase", phase);
					params.put("expInfo", expInfo);
					params.put("content", TextUtils.isEmpty(content) ? "" : content);
					// params.put("tengxunUrl", "");
					params.put("styleType", "0");
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.LOG_URL, "log/appLog", params);
					if (result.getSuccess()) {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Click
	void tvTextClick() {
		// if (type == 1) {
		// isGetqrCode = true;
		// initLoginqrCode();
		// } else if (type == 2 || type == 3 || type == 4) {
		// showDialog2();
		// }
	}

	@Click
	void header_image_return() {
		showDialog();
	}

	@Click
	void tvTouchMe() {
		if (type == 2) {
			initAfterLogin();
		} else if (type == 3) {
			initConsume();
		} else if (type == 4) {
			initConsume2();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showDialog();
			return true;
		}
		return false;
	}

	private void showDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(HuaBeiActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage("离开认证会失败，需要重新认证，确定离开？");
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				HuaBeiActivity.this.finish();
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

	private void showMessage(String message) {
		CustomDialog.Builder builder = new CustomDialog.Builder(HuaBeiActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage(message);
		builder.setPositiveButton("我知道了", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("人工审核", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}

	private void showDialog2() {
		CustomDialog1.Builder builder = new CustomDialog1.Builder(HuaBeiActivity.this);
		builder.setTitle("扫码助手");
		builder.setMessage("1.已帮您的二维码存入相册\n2.将自动跳转支付宝扫一扫\n3.点击右上角在相册里选中二维码\n4.根据提示点击确定登录或验证身份");
		builder.setPositiveButton("打开支付宝", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					// 利用Intent打开支付宝
					Uri uri = Uri.parse("alipayqr://platformapi/startapp?saId=10000007");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				} catch (Exception e) {
					// 若无法正常跳转，在此进行错误处理
					Toast.makeText(HuaBeiActivity.this, "无法跳转到支付宝，请检查您是否安装了支付宝！", Toast.LENGTH_SHORT).show();
				}
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

	private void addProgress(final int diff) {
		runOnUiThread(new Runnable() {
			public void run() {
				pbAuthen.incrementProgressBy(diff);
			}
		});
	}

	private void showProgress(final int diff) {
		runOnUiThread(new Runnable() {
			public void run() {
				pbAuthen.setProgress(diff);
			}
		});
	}

	private void showErrorAndOut(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (!HuaBeiActivity.this.isFinishing()) {
					Toast.makeText(HuaBeiActivity.this, info, Toast.LENGTH_LONG).show();
					HuaBeiActivity.this.finish();
				}
			}
		});
	}

	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (!HuaBeiActivity.this.isFinishing()) {
					Toast.makeText(HuaBeiActivity.this, info, Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
