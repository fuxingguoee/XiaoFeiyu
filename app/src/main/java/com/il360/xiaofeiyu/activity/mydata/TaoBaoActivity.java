package com.il360.xiaofeiyu.activity.mydata;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.tb.Address;
import com.il360.xiaofeiyu.model.tb.ArrayOfTbOrder;
import com.il360.xiaofeiyu.model.tb.BasicInfo;
import com.il360.xiaofeiyu.model.tb.ConsumeInfo;
import com.il360.xiaofeiyu.model.tb.MainOrders;
import com.il360.xiaofeiyu.model.tb.Order;
import com.il360.xiaofeiyu.model.tb.OrderItem;
import com.il360.xiaofeiyu.model.tb.OrderLogistics;
import com.il360.xiaofeiyu.model.tb.Orders;
import com.il360.xiaofeiyu.model.tb.SubOrders;
import com.il360.xiaofeiyu.model.tb.TaoBaoInfo;
import com.il360.xiaofeiyu.model.tb.Taobao1Detials;
import com.il360.xiaofeiyu.model.tb.TaobaoValue;
import com.il360.xiaofeiyu.model.tb.TbAlipayInfo;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.FileHelper;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.alipay.HttpUtils;
import com.il360.xiaofeiyu.view.CustomDialog;
import com.il360.xiaofeiyu.view.TextProgressBar;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
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
 * 上传日志错误步骤 1登录；2淘宝id信息；3淘宝邮箱电话；4淘宝个人信息；5淘气值；6支付宝信息；7地址；8消费订单数据；9提交数据
 */

@SuppressLint("SimpleDateFormat")
@EActivity(R.layout.act_huabei_info)
public class TaoBaoActivity extends BaseWidgetActivity {

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
//	private AliUserInfo aliUserInfo = new AliUserInfo();
	private String aliUrl = "";

	private static final String aliUrl2 = "https://my.alipay.com/portal/i.htm";// 登陆网页
	private static final String loginUrl = "https://login.taobao.com/member/login.jhtml";// 登陆网页
	private static final String loginUrl2 = "https://login.m.taobao.com/login.htm";// 登陆网页2
	private static final String loginUrl3 = "https://login.m.taobao.com/msg_login.htm";// 短信网页2
	private static final String afterLoginUrl = "http://h5.m.taobao.com/mlapp/mytaobao.html";
	private static final String afterLoginUrl2 = "intent://m.taobao.com/tbopen/index.html";
	private static final String afterLoginUrl3 = "https://i.taobao.com/my_taobao.htm?";
	private static final String afterLoginUrl4 = "https://www.taobao.com";
	private static final String addressUrl = "https://member1.taobao.com/member/fresh/deliver_address.htm";// 收货地址链接
	private static final String consumeUrl = "https://buyertrade.taobao.com/trade/itemlist/asyncBought.htm";// 所有订单链接
	private static final String basicInfo1Url = "https://i.taobao.com/my_taobao.htm";
	private static final String basicInfo2Url = "https://member1.taobao.com/member/fresh/account_security.htm";
	private static final String basicInfo3Url = "https://member1.taobao.com/member/fresh/certify_info.htm";
	private static final String basicInfo4Url = "https://vip.taobao.com/ajax/getGoldUser.do";// 获取淘气值
	private static final String alipayInfo1Url = "https://member1.taobao.com/member/fresh/account_management.htm";
	private static final String alipayInfo2Url = "https://i.taobao.com/my_taobao_api/alipay_blance.json";
	private static final String xiangqingUrl1 = "//trade.tmall";// 订单详情天猫
	private static final String xiangqingUrl2 = "//buyertrade.taobao.com";// 订单详情淘宝
	private static final String xiangqingUrl3 = "//tradearchive.taobao.com";// 订单详情淘宝2
	private static final String huabeiUrl = "https://my.alipay.com/portal/huabeiWithDynamicFont.json";
	private static final String yuebaoUrl = "https://my.alipay.com/portal/mfundWithDynamicFont.json";
	private static final String yueUrl = "https://my.alipay.com/portal/accountWithDynamicFont.json";

	private static final Pattern huabeibalp = Pattern.compile(
			"creditAmountStr.*?>([^<>]*?)<span.*?>([^<>]*?)</span>.*?availableAmountStr.*?>([^<>]*?)<span.*?>([^<>]*?)</span>");
	private static final Pattern addressp1 = Pattern.compile(
			"<tr class=\"thead-tbl-address([^<>]*?)\"><td>([^<>]*?)</td><td>([^<>]*?)</td><td>([^<>]*?)</td><td>([^<>]*?)</td><td>([^<>]*?)</td>");
	private static final Pattern addressp2 = Pattern.compile(
			"<tr class=\"thead-tbl-address([^<>]*?)\"><td>([^<>]*?)</td><td>([^<>]*?)</td><td>([^<>]*?)</td><td>([^<>]*?)</td><td>([^<>]*?)<br/>([^<>]*?)</td>");
	private static final Pattern basicInfo1 = Pattern.compile(
			"<input id=\"mtb-nickname\"[^>]*?value=\"([^<>]*?)\"/><input id=\"mtb-userid\"[^>]*?value=\"([^<>]*?)\"/>");
	private static final Pattern basicInfo11 = Pattern
			.compile("<div class=\"s-icon-main\" >极速退款</div>.*?<em>([^<>]*?)</em>");
	private static final Pattern basicInfo2 = Pattern.compile(
			"<span class=\"t\">登 录 邮 箱：</span><span class=\"default grid-msg \">([^<>]*?)</span>.*?<span class=\"t\">绑 定 手 机：</span><span class=\"default grid-msg\">([^<>]*?)</span>");
	private static final Pattern basicInfo3 = Pattern.compile(
			"<div class=\"msg-tit msg-vm\">([^<>]*?)</div>.*?<span>姓名：</span><div class=\"left\">([^<>]*?)</div></div><div class=\"explain-info\"><span>18位身份证号：</span><div class=\"left\">([^<>]*?)</div>");
	private static final Pattern tbAlipyInfo1 = Pattern
			.compile("<h3 class=\"ui-tipbox-title\">已绑定支付宝账户: <span class=\"red\">([^<>]*?)</span>");
	private static final Pattern dingdanurip = Pattern.compile("//([^<>]*?)/");
	private static final Pattern huabeip = Pattern
			.compile("花呗 可用额度： <em>([^<>]*?)</em>.*?花呗 消费额度.*?<span>([^<>]*?)元</span>");
	private static final Pattern huabeip2 = Pattern.compile("花呗 预计消费额度.*?<em>([^<>]*?)</em>");
	private static final Pattern alip = Pattern.compile("href=\"([^<>]*?)\" >我的支付宝</a>");
	private static final Pattern balancep = Pattern.compile("balanceAmountStr.*?>([^<>]*?)<span.*?>([^<>]*?)</span>");
	private static final Pattern yuep = Pattern.compile("accountAmountStr.*?>([^<>]*?)<span.*?>([^<>]*?)</span>");

	private static final Pattern TmalldingdanDetial1 = Pattern.compile(
			"\"basic\":[^>]*?\"lists\":[^>]*?\"content\":[^>]*?\"text\":\"(.*?)\",\"type\":\"label\"[^>]*?,\"key\":\"收货地址\"");
	private static final Pattern TmalldingdanDetial2 = Pattern
			.compile("\"logistic\".*?\"companyName\":\"(.*?)\",\"mailNo\":\"(.*?)\",\"text\":.*?\"type\":\"(.*?)\"");
	private static final Pattern Taobao1dingdan = Pattern.compile("<script> var data = [^>]*?\'(.*?)\'[^>]*?</script>");
	private static final Pattern Taobao2dingdan = Pattern.compile(
			"<td class=\"label\">收货地址：</td><td>([^<>]*?)</td>.*?运送方式：</td><td>([^<>]*?)</td><td></td></tr><tr><td class=\"label\">物流公司：</td><td>([^<>]*?)</td><td></td></tr><tr><td class=\"label\">运单号：</td><td>([^<>]*?)</td>");

	private BasicInfo basicInfo = new BasicInfo();
	private TbAlipayInfo tbAlipayInfo = new TbAlipayInfo();
	private List<Address> addressList = new ArrayList<Address>();

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
	private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMM");

	private long endTime = 0;
	private long beginTime = 0;

	private String monthId1 = "";
	private String monthId2 = "";
	private String monthId3 = "";
	private List<Orders> orderList1 = new ArrayList<Orders>();
	private List<Orders> orderList2 = new ArrayList<Orders>();
	private List<Orders> orderList3 = new ArrayList<Orders>();
	private ArrayOfTbOrder arrayOfTbOrder1 = new ArrayOfTbOrder();
	private ArrayOfTbOrder arrayOfTbOrder2 = new ArrayOfTbOrder();
	private ArrayOfTbOrder arrayOfTbOrder3 = new ArrayOfTbOrder();

	private String phoneInfo = "";
	private int huaBeiAmount = 0;

	@AfterViews
	void init() {

		initViews();
		initWebView();
	}

	private void initViews() {

		try {
			phoneInfo = android.os.Build.MODEL + "," + android.os.Build.VERSION.SDK + ","
					+ android.os.Build.VERSION.RELEASE;

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MONTH, -3);// 得到前2个月
			Date formNow3Month = calendar.getTime();
			String mBeginDate = sdf2.format(formNow3Month) + "-01 00:00:00";
			Date date1 = sdf1.parse(mBeginDate);
			beginTime = date1.getTime();
			monthId3 = sdf3.format(formNow3Month);// 3月前的月份
			calendar.add(Calendar.MONTH, +1);
			Date formNow2Month = calendar.getTime();
			monthId2 = sdf3.format(formNow2Month);// 2月前的月份
			calendar.add(Calendar.MONTH, +1);
			Date formNow1Month = calendar.getTime();
			monthId1 = sdf3.format(formNow1Month);// 1月前的月份

			String mEndDate = sdf2.format(new Date()) + "-01 23:59:59";
			Date date2 = sdf1.parse(mEndDate);
			calendar.setTime(date2);
			calendar.add(Calendar.DATE, -1);
			Date newEndDate = calendar.getTime();
			endTime = newEndDate.getTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				wv_show.getSettings().setUserAgentString(
						"Mozilla/5.0(Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101Firefox/56.0");
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
					cookieManager.setAcceptThirdPartyCookies(wv_show, false);
				}

				wv_show.setWebViewClient(new MyWebViewClient());
			}
		});
	}

	private FileHelper helper;

	final class InJavaScriptLocalObj {
		@JavascriptInterface
		public void showSource(String html, int i) throws Exception {

		}
	}

	final class MyWebViewClient extends WebViewClient {
		// 在WebView中而不是系统默认浏览器中显示页面

		/**
		 * 拦截 url 跳转,在里边添加点击链接跳转或者操作
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith(afterLoginUrl) || url.startsWith(afterLoginUrl2)) {
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

			if (url.startsWith(afterLoginUrl) || url.startsWith(afterLoginUrl2) || url.startsWith(afterLoginUrl3)
					|| url.startsWith(afterLoginUrl4)) {
				wv_show.setVisibility(View.GONE);
				llAuthen.setVisibility(View.VISIBLE);
			}
			super.onPageStarted(view, url, favicon);
		}

		// 页面载入完成后调用
		@Override
		public void onPageFinished(WebView view, String url) {

			// 获取cookie
			CookieManager cookieManager = CookieManager.getInstance();
			final String cokieStr = cookieManager.getCookie(url);

			if (url.startsWith(afterLoginUrl) || url.startsWith(afterLoginUrl2) || url.startsWith(afterLoginUrl3)
					|| url.startsWith(afterLoginUrl4)) {
				wv_show.setVisibility(View.GONE);
				llAuthen.setVisibility(View.VISIBLE);
			} else {
				wv_show.setVisibility(View.VISIBLE);
				llAuthen.setVisibility(View.GONE);
			}

			if (url.equals(loginUrl) || url.startsWith(loginUrl2) || url.startsWith(loginUrl3)) {
				if (url.startsWith(loginUrl)) {
					
					String javascript = "function getClass(parent,sClass){var aEle=parent.getElementsByTagName('div');"
							+ "var aResult=[];var i=0;for(i<0;i<aEle.length;i++){if(aEle[i].className==sClass)"
							+ "{aResult.push(aEle[i]);}};"
							+ "return aResult;}"
							+ "function removeClassName(name){var eles = document.getElementsByClassName(name);var i;"
							+ "for (i = 0; i < eles.length; i++) {eles[i].remove();}};"
							+ "function hideOther() {"
							+ "getClass(document,'clearfix')[0].style.display='none';"
							+ "getClass(document,'login-adlink')[0].style.display='none';"
							+ "getClass(document,'login-links')[0].style.display='none';"
							+ "getClass(document,'login-newbg')[0].style.display='none';"
							+ "getClass(document,'footer')[0].style.display='none';"
							+ "getClass(document,'content-layout')[0].style.width='410px';"
							+ "getClass(document,'content-layout')[0].style.height='450px';};"
							+ "function setupLoginHomeUI(){"
							+ "document.getElementsByClassName('forget-pwd J_Quick2Static')[0].click();"
							+ "removeClassName('light-link');"
							+ "var elem = document.getElementsByClassName('ft-gray');"
							+ "if(elem.length>0){var label = elem[0];label.innerText = '打开手机淘宝|手机天猫\n';}"
							+ "removeClassName('login-adlink');"
							+ "removeClassName('login-links');"
							+ "removeClassName('other-login');"
							+ "removeClassName('register');"
							+ "removeClassName('login-newbg');removeClassName('footer');};"
							+ "hideOther();"
							+ "setupLoginHomeUI();";
//					String javascript = "function removeClassName(name){var eles = document.getElementsByClassName(name);var i;"
//								+ "for (i = 0; i < eles.length; i++) {eles[i].remove();}};"
//								+ "function setupLoginHomeUI(){"
//								+ "removeClassName('clearfix');"
//								+ "removeClassName('login-adlink');"
//								+ "removeClassName('login-links');"
//								+ "removeClassName('other-login');"
//								+ "removeClassName('register');"
//								+ "removeClassName('login-newbg');removeClassName('footer');};"
//								+ "setupLoginHomeUI();"
					view.loadUrl("javascript:" + javascript);
//					view.loadUrl("javascript:hideOther()");
//					view.loadUrl("javascript:clickOther()");
					
				} else if (url.startsWith(loginUrl2) || url.startsWith(loginUrl3)) {
					String js = "function removeClassName(name){var eles = document.getElementsByClassName(name);var i;for (i = 0; i < eles.length; i++) {eles[i].remove();}};"
							+ "function setupLoginHomeUI(){removeClassName('am-field am-footer');};"
							+ "setupLoginHomeUI();";
					view.loadUrl("javascript:" + js);
				}
				showProgress(5);
			} else if (cokieStr != null && (url.equals(afterLoginUrl) || url.startsWith(afterLoginUrl2)
					|| url.startsWith(afterLoginUrl3) || url.startsWith(afterLoginUrl4))) {
				ExecuteTask.execute(new Runnable() {
					@Override
					public void run() {
						try {
							captureData(cokieStr);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			} else if (url.startsWith("https://authgtj.alipay.com/")) {
				wv_show.setVisibility(View.GONE);
				llAuthen.setVisibility(View.VISIBLE);
			} else if (url.startsWith(aliUrl2)) {
				// String js = "var e=document.createEvent('MouseEvents');"
				// +"e.initEvent('mouseover',true,true);"
				// +"document.getElementById('J_MyAlipayInfo').dispatchEvent(e);"
				// +"document.getElementsByClassName('highlight alipay-trigger
				// J_AlipayTrigger')[0].click();";
				// view.loadUrl("javascript:" + js);
				// try {
				// Thread.sleep(2000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// view.loadUrl(
				// "javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>',
				// 1);");
				wv_show.setVisibility(View.GONE);
				llAuthen.setVisibility(View.VISIBLE);
				
				final String cokieStr2 = cookieManager.getCookie(url);

				ExecuteTask.execute(new Runnable() {
					@Override
					public void run() {
						try {
							
							boolean getalipayInfo2 = getalipayInfo2(huabeiUrl, cokieStr2);
							if (getalipayInfo2) {
								addProgress(5);// +5%
							}
							boolean getBalance = getBalance(yuebaoUrl, cokieStr2);
							if (getBalance) {
								addProgress(5);// +5%
							}
							boolean getYue = getYue(yueUrl, cokieStr2);
							if (getYue) {
								addProgress(5);// +5%
							}

							addProgress(5);
							// 提交数据
							initPostTbData();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

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

	@SuppressLint("SetJavaScriptEnabled")
	private void initAfterLogin4() {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				wv_show.loadUrl(aliUrl);
			}
		});
	}

	private void captureData(String cookie) throws InterruptedException {
		mCtoken = getCtoken(cookie);

		boolean basicInfo1 = getbasicInfo1(basicInfo1Url, cookie);// 获取淘宝id,淘宝账号,极速退款额度
		if (basicInfo1) {
			addProgress(5);// +5%
		}
		boolean basicInfo2 = getbasicInfo2(basicInfo2Url, cookie);// 获取登陆邮箱和电话号码
		if (basicInfo2) {
			addProgress(5);
		}
		boolean basicInfo3 = getbasicInfo3(basicInfo3Url, cookie);// 获取真实姓名和身份证
		if (basicInfo3) {
			addProgress(5);
		}
		boolean basicInfo4 = getbasicInfo4(basicInfo4Url, cookie);// 获取淘气值
		if (basicInfo4) {
			addProgress(5);
		}

		boolean alipayInfo1 = getalipayInfo1(alipayInfo1Url, cookie);// 获取支付宝账户
		if (alipayInfo1) {
			addProgress(5);
		}
		// boolean alipayInfo2 = getalipayInfo2(huabeiUrl, cookie);//
		// 获取支付宝账户余额,余额宝信息
		// if (alipayInfo2) {
		// addProgress(5);
		// }
		boolean address = getAddress(addressUrl, cookie);// 淘宝地址
		if (address) {
			addProgress(5);
		}

		int pages = getConsume0(consumeUrl, cookie);// 3个月有几页
		if (pages > 0) {
			for (int i = 1; i <= pages; i++) {
				boolean consume = getConsume(consumeUrl, cookie, i);// 淘宝订单数据
				if (consume) {
					addProgress(2);
				}
				Thread.sleep(1000);
			}
		}

		initAfterLogin4();
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

	private void initPostTbData() {
		if (!TaoBaoActivity.this.isFinishing()) {
			try {
				Map<String, String> params = new HashMap<String, String>();
				params.put("tbjson", assembleData());
				params.put("token", UserUtil.getToken());
				TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "card/postTbData",
						params);
				if (result.getSuccess()) {
					if (ResultUtil.isOutTime(result.getResult()) != null) {
						showInfo(ResultUtil.isOutTime(result.getResult()));
						Intent intent = new Intent(TaoBaoActivity.this, LoginActivity_.class);
						startActivity(intent);
					} else {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							addProgress(10);
						} else {
							upLogInfo("9", "接口返回obj.getInt(\"code\")为" + obj.getInt("code"), result.getResult());
							showErrorAndOut(obj.getString("desc"));
						}
					}
				} else {
					upLogInfo("9", "card/postTbData接口返回result.getSuccess()为false", result.getResult());
					showErrorAndOut("认证失败，请重试！");
				}
			} catch (Exception e) {
				upLogInfo("9", "方法initPostTbData报异常", e.getMessage());
				showErrorAndOut("认证失败，请重试！");
			} finally {
				if (!TaoBaoActivity.this.isFinishing()) {
					clearWebViewCache();
					TaoBaoActivity.this.finish();
				}
			}
		}
	}

	private String assembleData() {
		TaoBaoInfo taoBaoInfo = new TaoBaoInfo();
		taoBaoInfo.setBasicInfo(basicInfo);
		taoBaoInfo.setAddressList(addressList);
		taoBaoInfo.setTbAlipayInfo(tbAlipayInfo);

		arrayOfTbOrder1.setMonthId(monthId1);
		arrayOfTbOrder1.setOrderList(orderList1);
		arrayOfTbOrder2.setMonthId(monthId2);
		arrayOfTbOrder2.setOrderList(orderList2);
		arrayOfTbOrder3.setMonthId(monthId3);
		arrayOfTbOrder3.setOrderList(orderList3);

		List<ArrayOfTbOrder> monthOrderList = new ArrayList<ArrayOfTbOrder>();
		monthOrderList.clear();
		monthOrderList.add(arrayOfTbOrder1);
		monthOrderList.add(arrayOfTbOrder2);
		monthOrderList.add(arrayOfTbOrder3);
		taoBaoInfo.setMonthOrderList(monthOrderList);

		showProgress(90);

		return FastJsonUtils.getJsonString(taoBaoInfo);
	}

	// 获取淘宝id,淘宝账号,极速退款额度
	private boolean getbasicInfo1(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// heades.put("Accept-Encoding", " gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "max-age=0, no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", " i.taobao.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
			heades.put("Upgrade-Insecure-Requests", "1");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");

			String result = HttpUtils.get(url, heades, Charset.forName("UTF-8"));

			if (TextUtils.isEmpty(result)) {
				upLogInfo("2", "地址信息获取失败2", "");
				return false;
			} else {
				String html = HttpUtils.replRtnSpace(result);

				// helper = new FileHelper(getApplicationContext());
				// try {
				// helper.createSDFile("test.txt");
				// helper.writeSDFile(html, "test.txt");
				// } catch (IOException e) {
				// e.printStackTrace();
				// };

				aliUrl = "https:" + HttpUtils.getValue(html, alip);

				List<String[]> list = HttpUtils.getListArray(html, basicInfo1);
				if (list != null && list.size() > 0) {
					// basicInfo.setUserId(list.get(0)[1]);
					basicInfo.setUserName(list.get(0)[0]);
				}
				String tbValue = HttpUtils.getValue(html, basicInfo11);
				if (!tbValue.isEmpty()) {
					basicInfo.setFastRefundAmt(Integer.parseInt(tbValue) * 100);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("2", "方法getbasicInfo1报异常", e.getMessage());
			return false;
		}
	}

	// 获取登陆邮箱和电话号码
	private boolean getbasicInfo2(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// heades.put("Accept-Encoding", "gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", "member1.taobao.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://login.taobao.com/member/login.jhtml");
			heades.put("Upgrade-Insecure-Requests", "1");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");

			String result = HttpUtils.get(url, heades, Charset.forName("GBK"));

			if (TextUtils.isEmpty(result)) {
				upLogInfo("3", "地址信息获取失败3", "");
				return false;
			} else {
				String html = HttpUtils.replRtnSpace(result);
				List<String[]> list = HttpUtils.getListArray(html, basicInfo2);
				if (list != null && list.size() > 0) {
					basicInfo.setLoginEmail(list.get(0)[0]);
					basicInfo.setPhoneNo(list.get(0)[1]);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("3", "方法getbasicInfo2报异常", e.getMessage());
			return false;
		}
	}

	// 获取真实姓名和身份证
	private boolean getbasicInfo3(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// heades.put("Accept-Encoding", "gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", "member1.taobao.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://member1.taobao.com/member/fresh/account_security.htm");
			heades.put("Upgrade-Insecure-Requests", "1");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");

			String result = HttpUtils.get(url, heades, Charset.forName("GBK"));

			if (TextUtils.isEmpty(result)) {
				upLogInfo("4", "地址信息获取失败4", "");
				return false;
			} else {
				String html = HttpUtils.replRtnSpace(result);
				List<String[]> list = HttpUtils.getListArray(html, basicInfo3);
				if (list != null && list.size() > 0) {
					basicInfo.setRealName(list.get(0)[1]);
					basicInfo.setIdCard(list.get(0)[2]);

					tbAlipayInfo.setAuthStatus(list.get(0)[0]);
					tbAlipayInfo.setRealName(list.get(0)[1]);
					tbAlipayInfo.setIdCard(list.get(0)[2]);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("4", "方法getbasicInfo3报异常", e.getMessage());
			return false;
		}
	}

	// 获取淘气值
	private boolean getbasicInfo4(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "*/*");
			// heades.put("Accept-Encoding", "gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", "vip.taobao.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://member1.taobao.com/member/fresh/certify_info.htm");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");

			String result = HttpUtils.get(url, heades, Charset.forName("GBK"));

			if (TextUtils.isEmpty(result)) {
				upLogInfo("5", "地址信息获取失败5", "");
				return false;
			} else {
				TaobaoValue tv = FastJsonUtils.getSingleBean(result, TaobaoValue.class);
				basicInfo.setNaughtyValue(tv.getData().getTaoScore());
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("5", "方法getbasicInfo4报异常", e.getMessage());
			return false;
		}
	}

	// 获取支付宝账户
	private boolean getalipayInfo1(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// heades.put("Accept-Encoding", "gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", "member1.taobao.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://member1.taobao.com/member/fresh/certify_info.htm");
			heades.put("Upgrade-Insecure-Requests", "1");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");

			String result = HttpUtils.get(url, heades, Charset.forName("GBK"));

			if (TextUtils.isEmpty(result)) {
				upLogInfo("6", "地址信息获取失败61", "");
				return false;
			} else {
				String html = HttpUtils.replRtnSpace(result);
				String account = HttpUtils.getValue(html, tbAlipyInfo1);
				tbAlipayInfo.setAccount(account);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("6", "方法getalipayInfo1报异常", e.getMessage());
			return false;
		}
	}

	// 获取支付宝账户余额,余额宝信息
	private boolean getalipayInfo2(String url, String cookie) {
		try {
			
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "*/*");
			// heades.put("Accept-Encoding", "gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Pragma", "no-cache");
			heades.put("Host", "my.alipay.com");
			heades.put("Referer", "https://my.alipay.com/portal/i.htm");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0");
			heades.put("X-Requested-With", "XMLHttpRequest");

			// Map<String, String> params = new HashMap<String, String>();
			// params.put("_input_charset", "utf-8");
			// params.put("_output_charset", "utf-8");
			// params.put("className", "huabeiWithDynamicFontClass");
			// params.put("ctoken", mCtoken);
			// params.put("encrypt", "true");

			String result = HttpUtils.get(url, heades, Charset.forName("GBK"));

			String html = HttpUtils.replRtnSpace(result);

			List<String[]> huabeiList = HttpUtils.getListArray(html, huabeibalp);
			if (huabeiList != null && huabeiList.size() > 0) { // 与是否开通花呗有关
				String available = huabeiList.get(0)[2] + huabeiList.get(0)[3];
				tbAlipayInfo.setHbUsableAmt((int)(Double.parseDouble(available.trim())*100));

				String total = huabeiList.get(0)[0] + huabeiList.get(0)[1];
				tbAlipayInfo.setHbAmount((int)(Double.parseDouble(total.trim())*100));
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
		// try {
		// Map<String, String> heades = new HashMap<String, String>();
		// heades.put("Accept", "application/json, text/javascript, */*;
		// q=0.01");
		// // heades.put("Accept-Encoding", "gzip, deflate, br");
		// heades.put("Accept-Language",
		// "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		// heades.put("Cache-Control", "no-cache");
		// heades.put("Connection", "keep-alive");
		// heades.put("Content-Type", "application/x-www-form-urlencoded;
		// charset=UTF-8");
		// heades.put("Cookie", cookie);
		// heades.put("Host", "i.taobao.com");
		// heades.put("Pragma", "no-cache");
		// heades.put("Referer", "https://i.taobao.com/my_taobao.htm");
		// heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64;
		// rv:57.0) Gecko/20100101 Firefox/57.0");
		// heades.put("X-Requested-With", "XMLHttpRequest");
		//
		// String result = HttpUtils.get(url, heades, Charset.forName("GBK"));
		//
		// if (TextUtils.isEmpty(result)) {
		// upLogInfo("6", "地址信息获取失败62", "");
		// return false;
		// } else {
		// TbAlipayInfo2 info2 = FastJsonUtils.getSingleBean(result,
		// TbAlipayInfo2.class);
		// if (!info2.getData().getBalance().equals("")) {
		// tbAlipayInfo.setAccBal((int)
		// (Double.parseDouble(info2.getData().getBalance()) * 100));
		// }
		// if (!info2.getData().getTotalProfit().equals("")) {
		// tbAlipayInfo.setTotalEarningsAmt((int)
		// (Double.parseDouble(info2.getData().getTotalProfit()) * 100));
		// }
		// if (!info2.getData().getTotalQuotient().equals("")) {
		// tbAlipayInfo.setEbaoBal((int)
		// (Double.parseDouble(info2.getData().getTotalQuotient()) * 100));
		// }
		// return true;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// upLogInfo("6", "方法getalipayInfo2报异常", e.getMessage());
		// return false;
		// }
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

//			Map<String, String> params = new HashMap<String, String>();
//
//			params.put("className", "accountWithDynamicFontClass");
//			params.put("encrypt", "true");
//			params.put("_input_charset", "utf-8");
//			params.put("ctoken", mCtoken);
//			params.put("_output_charset", "utf-8");

			String result = HttpUtils.get(url, heades, Charset.defaultCharset());

			// Log.i("支付宝余额查询响应", result);
			String html = HttpUtils.replRtnSpace(result);

			List<String[]> banleanceList = HttpUtils.getListArray(html, balancep);
			if (banleanceList != null && banleanceList.size() > 0) {
				String ebaoBal = banleanceList.get(0)[0] + banleanceList.get(0)[1];
				tbAlipayInfo.setEbaoBal((int)(Double.parseDouble(ebaoBal.trim())*100));
			} else {
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

//			Map<String, String> params = new HashMap<String, String>();
//
//			params.put("className", "mfundWithDynamicFontClass");
//			params.put("encrypt", "true");
//			params.put("_input_charset", "utf-8");
//			params.put("ctoken", mCtoken);
//			params.put("_output_charset", "utf-8");

			String result = HttpUtils.get(url, heades, Charset.defaultCharset());

			String html = HttpUtils.replRtnSpace(result);

			List<String[]> yueList = HttpUtils.getListArray(html, yuep);
			if (yueList != null && yueList.size() > 0) { // 账户余额
				String accBal = yueList.get(0)[0] + yueList.get(0)[1];
				tbAlipayInfo.setAccBal((int)(Double.parseDouble(accBal.trim()))*100);
			} else {
				upLogInfo("2", "账户余额获取失败，默认传0", html);
			}
			return true;

		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			upLogInfo("2", "方法getYue报异常", e.getMessage());
			return false;
		}
	}

	// 获取收货地址
	private boolean getAddress(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", " text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// heades.put("Accept-Encoding", " gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "max-age=0");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", "member1.taobao.com");
			// heades.put("Referer", "https://i.taobao.com/my_taobao.htm");
			heades.put("Upgrade-Insecure-Requests", "1");
			heades.put("User-Agent", " Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");

			String result = HttpUtils.get(url, heades, Charset.forName("GBK"));

			if (TextUtils.isEmpty(result)) {
				upLogInfo("7", "地址信息获取失败7", "");
				return false;
			} else {
				String html = HttpUtils.replRtnSpace(result);

				List<String[]> list1 = HttpUtils.getListArray(html, addressp1);
				List<String[]> list2 = HttpUtils.getListArray(html, addressp2);

				List<String[]> listAll = new ArrayList<String[]>();
				listAll.addAll(list1);
				listAll.addAll(list2);

				if (listAll != null && listAll.size() > 0) {
					for (int i = 0; i < listAll.size(); i++) {
						Address adr = new Address();
						if (listAll.get(i)[0].equals("address-default") || listAll.get(i)[0].equals("need-update")) {
							adr.setIsDefault("true");
						} else {
							adr.setIsDefault("false");
						}
						adr.setReceiverName(listAll.get(i)[1]);
						adr.setDistrictName(listAll.get(i)[2]);
						adr.setDetailAddress(listAll.get(i)[3]);
						adr.setPostcode(listAll.get(i)[4]);
						if (listAll.get(i).length == 6) {
							adr.setPhone(listAll.get(i)[5]);
						} else {
							adr.setPhone(listAll.get(i)[5] + "," + listAll.get(i)[6]);
						}

						addressList.add(adr);
					}
				}
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("7", "方法getAddress报异常", e.getMessage());
			return false;
		}
	}

	// 获取3个月的淘宝订单页数
	private int getConsume0(String url, String cookie) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "application/json, text/javascript, */*; q=0.01");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			heades.put("Cookie", cookie);
			heades.put("Host", "buyertrade.taobao.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
			heades.put("X-Requested-With", "XMLHttpRequest");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");

			Map<String, String> params = new HashMap<String, String>();

			params.put("_input_charset", "utf8");
			params.put("action", "itemlist/BoughtQueryAction");
			params.put("event_submit_do_query", String.valueOf(1));
			params.put("dateBegin", String.valueOf(beginTime));
			params.put("dateEnd", String.valueOf(endTime));
			params.put("pageNum", String.valueOf(1));
			params.put("pageSize", String.valueOf(15));
			params.put("prePageNo", String.valueOf(1));

			String result = HttpUtils.post(url, params, heades, Charset.forName("GBK"));

			if (TextUtils.isEmpty(result)) {
				upLogInfo("8", "地址信息获取失败81", "");
				return -1;
			} else {
				ConsumeInfo consumeInfo = FastJsonUtils.getSingleBean(result, ConsumeInfo.class);
				if (consumeInfo != null) {
					if (consumeInfo.getPage().getTotalPage() != null) {
						return Integer.parseInt(consumeInfo.getPage().getTotalPage());
					} else {
						return -1;
					}
				} else {
					return -1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("8", "方法getConsume0报异常", e.getMessage());
			return -2;
		}
	}

	// 获取淘宝订单列表
	private boolean getConsume(String url, String cookie, int page) {
		try {
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "application/json, text/javascript, */*; q=0.01");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			heades.put("Cookie", cookie);
			heades.put("Host", "buyertrade.taobao.com");
			heades.put("Pragma", "no-cache");
			heades.put("Referer", "https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
			heades.put("X-Requested-With", "XMLHttpRequest");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");

			Map<String, String> params = new HashMap<String, String>();

			params.put("_input_charset", "utf8");
			params.put("action", "itemlist/BoughtQueryAction");
			params.put("event_submit_do_query", String.valueOf(1));
			params.put("dateBegin", String.valueOf(beginTime));
			params.put("dateEnd", String.valueOf(endTime));
			params.put("pageNum", String.valueOf(page));
			params.put("pageSize", String.valueOf(15));

			String result = HttpUtils.post(url, params, heades, Charset.forName("GBK"));

			if (TextUtils.isEmpty(result)) {
				upLogInfo("8", "地址信息获取失败82", "");
				return false;
			} else {
				ConsumeInfo consumeInfo = FastJsonUtils.getSingleBean(result, ConsumeInfo.class);

				if (consumeInfo.getMainOrders() != null && consumeInfo.getMainOrders().size() > 0) {

					List<MainOrders> mainOrders = consumeInfo.getMainOrders();

					for (int i = 0; i < mainOrders.size(); i++) {
						// Log.i("id"+i, mainOrders.get(i).getId());
						String id = mainOrders.get(i).getId();

						Order td = new Order();
						List<OrderItem> dds = new ArrayList<OrderItem>();
						OrderLogistics dw = new OrderLogistics();
						Orders ol = new Orders();

						td.setTbOrderId(id);
						td.setOrderTime(sdf.format(mainOrders.get(i).getOrderInfo().getCreateTime()));
						td.setAmount((int) (Double.parseDouble(mainOrders.get(i).getPayInfo().getActualFee()) * 100));
						td.setStatusName(mainOrders.get(i).getStatusInfo().getText());
						if (mainOrders.get(i).getSeller().getNick() != null) {
							td.setShopName(mainOrders.get(i).getSeller().getNick());
						}

						if (mainOrders.get(i).getPayInfo().getPostType() != null) {
							td.setItemType("2");
						} else if (mainOrders.get(i).getPayInfo().getPostFees() != null) {
							td.setItemType("1");
						} else {
							td.setItemType("0");
						}
						if (mainOrders.get(i).getPayInfo().getIcons() == null) {
							td.setPaymentMethod("0");
						} else {
							if (mainOrders.get(i).getPayInfo().getIcons().get(0).getType() == "1") {
								td.setPaymentMethod("1");
							} else {
								td.setPaymentMethod("0");
							}
						}

						List<SubOrders> sos = mainOrders.get(i).getSubOrders();
						for (int j = 0; j < sos.size(); j++) {
							OrderItem dd = new OrderItem();
							dd.setGoodsId(sos.get(j).getItemInfo().getId());
							dd.setGoodsName(sos.get(j).getItemInfo().getTitle());
							dd.setGoodsUrl(sos.get(j).getItemInfo().getItemUrl());
							dd.setPrice((int) Double.parseDouble(sos.get(j).getPriceInfo().getRealTotal()) * 100);
							Pattern p = Pattern.compile("[^0-9]");
							Matcher m = p.matcher(sos.get(j).getQuantity());
							dd.setQuantity(m.replaceAll("".trim()));
							dds.add(dd);
						}

						// 获取订单详情url
						String dingdanurl = mainOrders.get(i).getStatusInfo().getOperations().get(0).getUrl();

						if (!mainOrders.get(i).getStatusInfo().getOperations().get(0).getText().equals("订单详情")) {
							dingdanurl = mainOrders.get(i).getStatusInfo().getOperations().get(1).getUrl();
						}

						String result2 = null;
						String host = HttpUtils.getValue(dingdanurl, dingdanurip);
						if (dingdanurl.startsWith(xiangqingUrl1) || dingdanurl.startsWith(xiangqingUrl2)
								|| dingdanurl.startsWith(xiangqingUrl3)) {
							String dingdanurl2 = "https:" + dingdanurl;
							result2 = getDetail(dingdanurl2, cookie, host);
							if (result2 != null) {
								if (dingdanurl.startsWith(xiangqingUrl1)) {

									String html = HttpUtils.replRtnSpace(result2);
									String result3 = HttpUtils.getValue(html, TmalldingdanDetial1);
									if (!TextUtils.isEmpty(result3)) {
										String[] array = result3.split(",|，");
										getTmDwA(array, dw);
									}
									List<String[]> result4 = HttpUtils.getListArray(html, TmalldingdanDetial2);
									if (result4.size() > 0) {
										dw.setExpressCompany(result4.get(0)[0]);
										dw.setExpressNum(result4.get(0)[1]);
										dw.setDeliverType(result4.get(0)[2]);
									}

								} else if (dingdanurl.startsWith(xiangqingUrl2)) {

									String html = HttpUtils.replRtnSpace(result2);
									String json = HttpUtils.getValue(html, Taobao1dingdan).replace("\\", "");
									if (json != "") {
										Taobao1Detials t1d = FastJsonUtils.getSingleBean(json, Taobao1Detials.class);
										dw.setExpressCompany(t1d.getDeliveryInfo().getLogisticsName());
										if (t1d.getDeliveryInfo().getAddress() != null) {
											String[] array = t1d.getDeliveryInfo().getAddress().split(",|，");
											getTmDwA(array, dw);
										}
										dw.setDeliverType(t1d.getDeliveryInfo().getShipType());
										dw.setExpressNum(t1d.getDeliveryInfo().getLogisticsNum());
									}

								} else if (dingdanurl.startsWith(xiangqingUrl3)) {
									String html = HttpUtils.replRtnSpace(result2);
									List<String[]> result3 = HttpUtils.getListArray(html, Taobao2dingdan);
									if (result3.size() > 0) {
										if (result3.get(0)[0] != null) {
											String[] array = result3.get(0)[0].split(",|，");
											getTmDwA(array, dw);
										}
										dw.setDeliverType(result3.get(0)[1]);
										dw.setExpressCompany(result3.get(0)[2]);
										dw.setExpressNum(result3.get(0)[3]);
									}
								}
							}
						}

						ol.setOrder(td);
						ol.setOrderItemList(dds);
						if (dw.getReceiveAddress() != null) {
							ol.setOrderLogistics(dw);
						} else {
							ol.setOrderLogistics(null);
						}

						String orderTime = sdf3.format(mainOrders.get(i).getOrderInfo().getCreateTime());
						if (orderTime.equals(monthId1)) {
							orderList1.add(ol);
						} else if (orderTime.equals(monthId2)) {
							orderList2.add(ol);
						} else if (orderTime.equals(monthId3)) {
							orderList3.add(ol);
						}
					}
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("8", "方法getConsume报异常", e.getMessage());
			return false;
		}
	}

	private String getDetail(String url, String cookie, String host) {
		try {
			String result = null;
			Map<String, String> heades = new HashMap<String, String>();
			heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			// heades.put("Accept-Encoding", "gzip, deflate, br");
			heades.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			heades.put("Cache-Control", "max-age=0, no-cache");
			heades.put("Connection", "keep-alive");
			heades.put("Cookie", cookie);
			heades.put("Host", host);
			heades.put("Pragma", "no-cache");
			// heades.put("Referer","https://buyertrade.taobao.com/trade/itemlist/list_bought_items.htm");
			heades.put("Upgrade-Insecure-Requests", "1");
			heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:57.0) Gecko/20100101 Firefox/57.0");

			result = HttpUtils.get(url, heades, Charset.forName("gbk"));
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			upLogInfo("8", "方法getDetail报异常", e.getMessage());
			return null;
		}
	}

	// 订单详情地址分割
	private void getTmDwA(String[] array, OrderLogistics dw) {
		if (array.length == 0) {
		} else if (array.length == 1) {
			dw.setReceiveName(array[0]);
		} else if (array.length == 2) {
			dw.setReceiveName(array[0]);
			dw.setReceiveMobile(array[1]);
		} else if (array.length > 2 && Pattern.matches("[/d/s-]", array[2])) {
			dw.setReceiveName(array[0]);
			dw.setReceiveMobile(array[1] + array[2]);
			dw.setReceiveAddress(array[3]);
		} else {
			dw.setReceiveName(array[0]);
			dw.setReceiveMobile(array[1]);
			dw.setReceiveAddress(array[2]);
		}
	}

	// 上传日志
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
					params.put("styleType", "1");
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.LOG_URL, "log/appLog",
							params);
					if (result.getSuccess()) {
					}
				} catch (Exception e) {
				}
			}
		});
	}

	@Click
	void header_image_return() {
		showDialog();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			showDialog();
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public void clearWebViewCache() {  
		// 清除cookie即可彻底清除缓存  
		CookieSyncManager.createInstance(TaoBaoActivity.this);  
		CookieManager.getInstance().removeAllCookie();  
		}
	
	private void showDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(TaoBaoActivity.this);
		builder.setTitle(R.string.app_name);
		builder.setMessage("离开认证会失败，需要重新认证，确定离开？");
		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				TaoBaoActivity.this.finish();
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
				if (!TaoBaoActivity.this.isFinishing()) {
					Toast.makeText(TaoBaoActivity.this, info, Toast.LENGTH_SHORT).show();
					TaoBaoActivity.this.finish();
				}
			}
		});
	}

	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (!TaoBaoActivity.this.isFinishing()) {
					Toast.makeText(TaoBaoActivity.this, info, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
