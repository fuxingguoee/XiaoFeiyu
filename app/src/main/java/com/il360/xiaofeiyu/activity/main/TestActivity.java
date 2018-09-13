package com.il360.xiaofeiyu.activity.main;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.alipay.LoginCallbackListence;
import com.il360.xiaofeiyu.alipay.LoginParamsJSInterface;
import com.il360.xiaofeiyu.model.alipay.AliBank;
import com.il360.xiaofeiyu.model.alipay.AliUserInfo;
import com.il360.xiaofeiyu.model.alipay.ArrayOfBankInfo;
import com.il360.xiaofeiyu.model.alipay.BankInfo;
import com.il360.xiaofeiyu.model.alipay.BillItem;
import com.il360.xiaofeiyu.model.alipay.BillList;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.FileHelper;
import com.il360.xiaofeiyu.util.alipay.HttpUtils;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
@EActivity(R.layout.act_url_to_web)
public class TestActivity extends BaseWidgetActivity {
	@ViewById
	TextView tv_noInfo;
	@ViewById
	WebView wv_show;
	@ViewById
	WebView wv_show2;
	
	private LoginParamsJSInterface loginParamsJSInterface;
    
	private List<BankInfo> bankInfoList = new ArrayList<BankInfo>();
	private List<AliBank> aliBankList = new ArrayList<AliBank>();
	
	private AliUserInfo aliUserInfo = new AliUserInfo();

    private Handler mHandler = new Handler();

    private Timer timer = new Timer(true);

    private boolean isPass = false;
    private boolean isFirst = true;
    private boolean canload = false;
    private ProgressDialog mDialog;
    
    private String mCtoken = "";
    private String mFormTk = "";
    private String mJsonUa = "";
    private String mBeginDate = "";
    private String mEndDate = "";
    
    private final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd");
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");
    private final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    private static Pattern p1 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?bizType=([^<>]*?)&bizInNo[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p>.*?<div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\"[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>[^>]*?</a>.*?</div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p>.*?<td[^>]*?><div[^>]*?>.*?</div></td></tr>");
	private static Pattern p2 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\">[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p><td[^>]*?><div[^>]*?><ul[^>]*?><li[^>]*?bizType=([^<>]*?)\"[^>]*?>.*?</div></td></tr>");
	private static Pattern p3 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?bizType=([^<>]*?)&[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\"[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>[^>]*?</a></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p></td><td[^>]*?><div[^>]*?>.*?</div></td></tr>");
	private static Pattern p4 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?bizType=([^<>]*?)&[^>]*?>([^<>]*?)</a></p><p[^>]*?><span>([^<>]*?)</span></p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\"[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>[^>]*?</a><span[^>]*?>[^>]*?</span></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p></td><td[^>]*?><div[^>]*?>.*?</div>.*?</td></tr>");
	private static Pattern p5 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>([^<>]*?)<a[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><span[^>]*?>[^>]*?</span></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p><td[^>]*?><div[^>]*?><ul[^>]*?><li[^>]*?bizInNo=([^<>]*?)&[^>]*?&bizType=([^<>]*?)\"[^>]*?>备注</li><li[^>]*?>删除</li></ul></div></td></tr>");
	private static Pattern p6 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><span[^>]*?>[^>]*?</span></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p></td><td[^>]*?><div[^>]*?><ul[^>]*?><li[^>]*?bizInNo=([^<>]*?)&[^>]*?&[^>]*?bizType=([^<>]*?)\"[^>]*?>备注<span[^>]*?>[^>]*?</span></li><li[^>]*?>删除</li></ul></div></td></tr>");
	
//    private static Pattern p1 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?bizType=([^<>]*?)&bizInNo[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p>.*?<div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\"[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>[^>]*?</a>.*?</div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p>.*?<td[^>]*?><div[^>]*?>.*?</div></td></tr>");
//	private static Pattern p2 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\">[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p><td[^>]*?><div[^>]*?><ul[^>]*?><li[^>]*?bizType=([^<>]*?)\"[^>]*?>.*?</div></td></tr>");
//	private static Pattern p3 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?bizType=([^<>]*?)&[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\"[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>[^>]*?</a></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p></td><td[^>]*?><div[^>]*?>.*?</div></td></tr>");
//	private static Pattern p4 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?bizType=([^<>]*?)&[^>]*?>([^<>]*?)</a></p><p[^>]*?><span>([^<>]*?)</span></p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\"[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>[^>]*?</a>.*?</div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p></td><td[^>]*?><div[^>]*?>.*?</div>.*?</td></tr>");
//	private static Pattern p5 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>([^<>]*?)<a[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><span[^>]*?>[^>]*?</span></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p><td[^>]*?><div[^>]*?><ul[^>]*?><li[^>]*?bizInNo=([^<>]*?)&[^>]*?&bizType=([^<>]*?)\"[^>]*?>备注</li><li[^>]*?>删除</li></ul></div></td></tr>");
//	private static Pattern p6 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?>.*?</div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p></td><td[^>]*?><div[^>]*?><ul[^>]*?>.*?<li[^>]*?bizInNo=([^<>]*?)&[^>]*?bizType=([^<>]*?)\"[^>]*?>备注.*?</li><li[^>]*?>删除</li></ul>.*?</div></td></tr>");
//	private static Pattern p7 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?bizType=([^<>]*?)&[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\"[^>]*?>[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>[^>]*?</a></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p></td><td[^>]*?><div[^>]*?>.*?</div></td></tr>");

//	private static Pattern p8 = Pattern.compile("<td class=\"name\"><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p><p class=\"text-muted\"></p><p class=\"ft-gray\"></p></td>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
//	private static Pattern p9 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p><p class=\"text-muted\"></p><p class=\"ft-gray\"></p></td>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
//	private static Pattern p10 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?><span>([^<>]*?)</span></p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p><p class=\"text-muted\"></p><p class=\"ft-gray\"></p></td>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
//	private static Pattern p11 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><p[^>]*?>[^>]*?</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p><p class=\"text-muted\"></p><p class=\"ft-gray\"></p></td>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
	
	private static Pattern p8 = Pattern.compile("<td class=\"name\"><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
	private static Pattern p9 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
	private static Pattern p10 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?><span>([^<>]*?)</span></p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
	private static Pattern p11 = Pattern.compile("<td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><p[^>]*?>[^>]*?</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>[^>]*?</a></div></td><td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p>.*?<li[^>]*?record=([^<>]*?)~([^<>]*?)~([^<>]*?)\"[^>]*?>删除</li>");
	
	
//    private static final String consumeUrl2 = "https://consumeprod.alipay.com/record/advanced.htm";
    private static final String consumeUrl3 = "https://consumeprod.alipay.com/record/standard.htm";
    private static final Pattern consumep2 = Pattern.compile("<tr[^>]*?><td class=\"time\"><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td class=\"memo\"></td><td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p></td><td[^>]*?><p>([^<>]*?)</p></td><td class=\"other\"><p[^>]*?>([^<>]*?)</p><p[^>]*?></p></td><td class=\"amount\"><span[^>]*?>([^<>]*?)</span></td><td class=\"detail\"><a[^>]*?></a></td><td class=\"status\"><p>([^<>]*?)</p><p></p><p[^>]*?></p></td><td class=\"action\"><select[^>]*?>.*?</select><div[^>]*?><div[^>]*?><span[^>]*?></span><span[^>]*?> </span></div></div></td></tr>");
    private static final Pattern consumep3 = Pattern.compile("<td class=\"time\"><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td class=\"memo\">.*?</td><td class=\"name\"><p[^>]*?><a[^>]*?>([^<>]*?)</a></p></td><td class=\"tradeNo ft-gray\"><p>([^<>]*?)</p></td><td class=\"other\"><p[^>]*?>([^<>]*?)</p><p[^>]*?></p></td><td class=\"amount\"><span[^>]*?>([^<>]*?)</span></td><td class=\"detail\"><a[^>]*?></a></td><td class=\"status\"><p>([^<>]*?)</p><p></p><p[^>]*?></p></td>");
    private static final Pattern consumep4 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?></td><td[^>]*?><p[^>]*?><a[^>]*?>([^<>]*?)</a></p></td><td[^>]*?><p>([^<>]*?)</p></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><a[^>]*?></a></td><td[^>]*?><p>([^<>]*?)</p><p></p><p[^>]*?></p></td><td[^>]*?><select[^>]*?>.*?</select><div[^>]*?><div[^>]*?><span[^>]*?></span><span[^>]*?>.*?</span></div></div></td></tr>");
    private static final Pattern consumep5 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td [^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?>([^<>]*?)</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>&#xe60b;</a></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p><td[^>]*?><div[^>]*?>.*?</div></td></tr>");
    private static final Pattern consumep6 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p><div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\">[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?></div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p><td[^>]*?><div[^>]*?>.*?</div></td></tr>");
    private static final Pattern consumep7 = Pattern.compile("<tr[^>]*?><td[^>]*?><p[^>]*?><img[^>]*?></p></td><td[^>]*?><p>([^<>]*?)</p><p[^>]*?>([^<>]*?)</p></td><td[^>]*?><p[^>]*?><a[^>]*?>([^<>]*?)</a></p><p[^>]*?>([^<>]*?)</p>.*?<div[^>]*?>[^>]*?流水号[^>]*?<a[^>]*?title=\"([^<>]*?)\">[^>]*?</a></div></td><td[^>]*?><span[^>]*?>([^<>]*?)</span></td><td[^>]*?><div[^>]*?><a[^>]*?>[^>]*?</a>.*?</div></td><td[^>]*?><p[^>]*?>([^<>]*?)</p><p[^>]*?></p><p[^>]*?></p><td[^>]*?><div[^>]*?>.*?</div></td></tr>");
    //<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p><p class=\"text-muted\"></p><p class=\"ft-gray\"></p></td>"
    //
    private static final Pattern consumep8 = Pattern.compile("<li[^>]*?bizInNo=([^<>]*?)&[^>]*?bizType=([^<>]*?)\"[^>]*?>备注</li>");
    private static final Pattern consumep11 = Pattern.compile("<li[^>]*?record=([^<>]*?)~[^>]*?>删除</li>");
    private static final Pattern consumep9 = Pattern.compile("<p class=\"consume-title\">.*?<a[^>]*?>([^<>]*?)</a>.*?</p>.*?<p class=\"name p-inline ft-gray\">([^<>]*?)</p>");
    private static final Pattern consumep10 = Pattern.compile("<p class=\"consume-title\">([^<>]*?)</p>.*?<p[^>]*?>([^<>]*?)</p>");
    //三个月记录数
    private static final Pattern consumep12 = Pattern.compile("<div class=\"page fn-right\">.*?<div class=\"page-link\">.*?共([^<>]*?)条.*?</div>.*?</div>");
    
    private static final Pattern consumep13 = Pattern.compile("<td class=\"amount\"><span class=\"amount-pay\">([^<>]*?)</span></td>.*?<td class=\"status\"><p class=\"text-muted\">([^<>]*?)</p><p class=\"text-muted\"></p><p class=\"ft-gray\"></p></td>.*?<li[^>]*?record=([^<>]*?)~[^>]*?>删除</li>");
    
    private static final Pattern formtkp = Pattern.compile("<script type=\"text/javascript\">var form_tk = \"([^<>]*?)\".*?</script>");
    
	private static final String loginUrl = "https://auth.alipay.com/login/index.htm";
	private static final String afterLoginUrl = "https://my.alipay.com/portal/i.htm?referer=https%3A%2F%2Fauth.alipay.com%2Flogin%2Findex.htm";
	private final String bankListUrl = String.format("https://zht.alipay.com/asset/bindQuery.json?_input_charset=utf-8&providerType=BANK&t=%s&ctoken=%s",System.currentTimeMillis(), mCtoken);
	private static final String balUrl = "https://my.alipay.com/portal/i.htm?referer=https://auth.alipay.com/login/index.htm";
	private static final Pattern huabeibalp = Pattern.compile("<p class=\"ft-gray\">可用额度<br /><span class=\"highlight\"><strong class=\"amount\">([^<>]*?)<span class=\"fen\">([^<>]*?)</span></strong></span>元</p><p class=\"ft-gray\">总额度:[^>]*?<strong class=\"amount\">([^<>]*?)<span class=\"fen\">([^<>]*?)</span></strong>[^>]*?元[^>]*?</p>");
	private static final Pattern balancep = Pattern.compile("<div class=\"i-assets-body fn-clear\"><div class=\"i-assets-balance-amount fn-left\"><strong class=\"amount\">([^<>]*?)<span class=\"fen\">([^<>]*?)</span></strong>[^>]*?元[^>]*?</div>.*<p class=\"i-assets-mFund-amount\" id=\"J-assets-mfund-amount\"><strong class=\"amount\">([^<>]*?)<span class=\"fen\">([^<>]*?)</span></strong>[^>]*?元[^>]*?</p>");
	private static final String userInfoUrl = "https://custweb.alipay.com/account/index.htm";
    private static final Pattern userInfop= Pattern.compile("<tr><th>真实姓名</th><td><span[^>]*?>([^<>]*?)</span><span[^>]*?>([^<>]*?)</span><span[^>]*?>([^<>]*?)</span><span[^>]*?>([^<>]*?)</span></td><td[^>]*?>.*?</td></tr><tr><th>邮箱</th><td><span[^>]*?>([^<>]*?)</span></td><td[^>]*?>.*?</td></tr><tr><th>手机</th><td><span[^>]*?>([^<>]*?)</span></span></td><td[^>]*?>.*?</td></tr><tr><th>淘宝会员名</th><td>([^<>]*?)</td><td[^>]*?>.*?</td></tr>.*<tr><th>注册时间</th><td>([^<>]*?)</td><td class=\"last\"><span><a[^>]*?>注销账户</a></span></td></tr>");
    
    private List<String[]> consumeList = new ArrayList<String[]>();
    
	private List<BillItem> billTtem1 = new ArrayList<BillItem>();
	private List<BillItem> billTtem2 = new ArrayList<BillItem>();
	private List<BillItem> billTtem3 = new ArrayList<BillItem>();	
	private List<BillItem> billTtem4 = new ArrayList<BillItem>();
	
	private String monthId1 = "";
	private String monthId2 = "";
	private String monthId3 = "";
	private String monthId4 = "";
	
	private BillList billList1 = new BillList();
	private BillList billList2 = new BillList();
	private BillList billList3 = new BillList();
	private BillList billList4 = new BillList();

	
    private int listSize = 0;
    private FileHelper helper; 
    
    @AfterViews
	void init() {
		wv_show.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wv_show.setVisibility(View.VISIBLE);
		tv_noInfo.setVisibility(View.GONE);
		
		initViews();
		initWebView();
	}
	
    private void initViews() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -3);// 得到前3个月
		Date formNow3Month = calendar.getTime();
		mEndDate = sdf1.format(formNow3Month);
		mBeginDate = sdf1.format(new Date());
		
		monthId4 =  sdf2.format(formNow3Month);
		calendar.add(Calendar.MONTH, +1);// 得到前2个月
		Date formNow2Month = calendar.getTime();
		monthId3 =  sdf2.format(formNow2Month);
		
		calendar.add(Calendar.MONTH, +1);// 得到前1个月
		Date formNow1Month = calendar.getTime();
		monthId2 =  sdf2.format(formNow1Month);
		
		monthId1 = sdf2.format(new Date());
		
	}

	private LoginCallbackListence loginCallbackListence = new LoginCallbackListence() {
        @Override
        public void hideWebView() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
//                	wv_show.setVisibility(View.GONE);
//                    //启动定时器
//                    timer.schedule(task, 25 * 1000);
//                    showMessage("正在认证中，请稍后...");
                }
            });
        }
    };

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void initWebView() {
		wv_show.requestFocus(); // 请求焦点
		wv_show.getSettings().setJavaScriptEnabled(true); // 设置是否支持JavaScript
		wv_show.getSettings().setSupportZoom(true); // 设置是否支持缩放
		wv_show.getSettings().setBuiltInZoomControls(true); // 设置是否显示内建缩放工具
		wv_show.setHorizontalScrollBarEnabled(true);// 滚动条水平是否显示
		wv_show.setVerticalScrollBarEnabled(true); // 滚动条垂直是否显示
		wv_show.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		wv_show.getSettings().setDomStorageEnabled(true); //设置支持DomStorage
        //这里是注入Java对象
        loginParamsJSInterface = new LoginParamsJSInterface(loginCallbackListence);
        wv_show.addJavascriptInterface(loginParamsJSInterface, "SPJSInterface");
		
		wv_show.loadUrl(loginUrl); // 加载在线网页
        //触摸焦点起作用
		wv_show.requestFocus();//如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事

        // Allow third party cookies for Android Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptThirdPartyCookies(wv_show,true);
        }
		
		wv_show.setWebViewClient(new MyWebViewClient());
		
	}

	final  class MyWebViewClient extends WebViewClient {
		// 在WebView中而不是系统默认浏览器中显示页面
		
		 /**
         * 拦截 url 跳转,在里边添加点击链接跳转或者操作
         */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG,"shouldOverrideUrlLoading " + url);
            return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
		}

		// 页面载入前调用
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		// 页面载入完成后调用
		@Override
		public void onPageFinished(WebView view, String url) {
			//注入js
            CookieManager cookieManager = CookieManager.getInstance();
            final String cokieStr = cookieManager.getCookie(url);
            if(url.equals(loginUrl)) {
                //要注入的js
                String js = "document.forms[0].onsubmit=function(event){var userName = document.getElementById(\"J-input-user\").value;var password = document.getElementById(\"password_rsainput\").value;window.SPJSInterface.getLoginParams(userName, password);return false;}";
                Log.w("ATTACK", "inject js, js= " + js);
                view.loadUrl("javascript:" + js);
            }
            
//            if(url.equals("https://consumeprod.alipay.com/record/standard.htm")){
//            	  //要注入的js
//            	if(isFirst){
//	                String js = "document.getElementById(\"J-three-month\").click();";
//	                Log.w("ATTACK", "inject js, js= " + js);
//	                view.loadUrl("javascript:" + js);
//            	}
//            	
//            	view.loadUrl("javascript:window.local_obj.showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//            }
            
			if (cokieStr != null && url.equals(afterLoginUrl)) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						Log.i("cookie", cokieStr);
						isPass = true;
						captureData(cokieStr);
					}
				}).start();
			}

			super.onPageFinished(view, url);
		}
		
		@SuppressWarnings("deprecation")
		@Override
        /**
         * 在每一次请求资源时，都会通过这个函数来回调
         */
        public WebResourceResponse shouldInterceptRequest(WebView view,String url) {
            Log.d(TAG,"shouldInterceptRequest " + url);
            return super.shouldInterceptRequest(view, url);
        }
	}
	
	final class InJavaScriptLocalObj {
		@JavascriptInterface
        public void showSource(String html) throws Exception {
			if(isFirst){
				//获取二维码保存到相册
				Log.d("HTML", html);
				isFirst = false;
				consumeList.clear();
			} else {
				//获取列表并加载下一页
				Log.d("HTML", html);
				String htmlStr = HttpUtils.replRtnSpace(html);
				
//				helper = new FileHelper(getApplicationContext()); 
//				try {
//					helper.createSDFile("test.txt");
//					helper.writeSDFile(html, "test.txt");
//					helper.createSDFile("test1.txt");
//					helper.writeSDFile(htmlStr, "test1.txt");
//					String ss = helper.readSDFile("test.txt");
//					Log.d("test.txt", ss);
//					System.out.println("响应信息-----111"+htmlStr);
//					System.out.println("响应信息-----222"+ss);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				
//				List<String[]> consumeList1 = HttpUtils.getListArray(htmlStr, p1);
//				List<String[]> consumeList2 = HttpUtils.getListArray(htmlStr, p2);
//				List<String[]> consumeList3 = HttpUtils.getListArray(htmlStr, p3);
//				List<String[]> consumeList4 = HttpUtils.getListArray(htmlStr, p4);
//				List<String[]> consumeList5 = HttpUtils.getListArray(htmlStr, p5);
//				List<String[]> consumeList6 = HttpUtils.getListArray(htmlStr, p6);
//				List<String[]> consumeList7 = HttpUtils.getListArray(htmlStr, p7);
				List<String[]> consumeList8 = HttpUtils.getListArray(htmlStr, p8);
				List<String[]> consumeList9 = HttpUtils.getListArray(htmlStr, p9);
				List<String[]> consumeList10 = HttpUtils.getListArray(htmlStr, p10);
				List<String[]> consumeList11 = HttpUtils.getListArray(htmlStr, p11);
				if(listSize == 0){
					String listSizeStr = HttpUtils.getValue(htmlStr, consumep12);
		            if(!TextUtils.isEmpty(listSizeStr)){
		            	listSize = Integer.parseInt(listSizeStr.trim());
		            	
		            } else {
		            	listSize = 0;
		            }
				}
	            
//	            if(consumeList1 != null && consumeList2 != null && consumeList3 != null && consumeList4 != null 
//	            		&& consumeList5 != null && consumeList6 != null && consumeList7 != null){
//	            	consumeList.addAll(consumeList1);
//	            	consumeList.addAll(consumeList2);
//	            	consumeList.addAll(consumeList3);
//	            	consumeList.addAll(consumeList4);
//	            	consumeList.addAll(consumeList5);
//	            	consumeList.addAll(consumeList6);
//	            	consumeList.addAll(consumeList7);
//	            }
				
				if(consumeList8 != null && consumeList9 != null && consumeList10 != null && consumeList11 != null){
	            	consumeList.addAll(consumeList8);
	            	consumeList.addAll(consumeList9);
	            	consumeList.addAll(consumeList10);
	            	consumeList.addAll(consumeList11);
				}
	            
	            continueLoad();//判断结束还是继续加载
	            
			}
        }
    }
	
    private void continueLoad(){
    	mHandler.post(new Runnable() {
            @Override
            public void run() {
            	try {
                	if(consumeList.size() < listSize){
                		wv_show2.loadUrl("javascript:document.getElementsByClassName('page-link')[0].getElementsByClassName('page-next page-trigger')[0].click();");
                	} else {
                		Log.d("consumeList", consumeList.toString());
                	}
				} catch (Exception e) {
					// TODO: handle exception
				}
            }
        });
    }
	
    private void captureData(String cookie) {
    	mCtoken = getCtoken(cookie);
        boolean bal = getBalance(balUrl, cookie);//花呗额度已成功获取
    	boolean userinfob= getUserInfo(userInfoUrl, cookie);//个人信息已成功获取
    	boolean bankListb= getBankList(bankListUrl, cookie);//银行卡
//        boolean consume1 = getmFormTk(consumeUrl2, cookie);//第一次获取form_tk
    	for(int i = 0 ; i< 28;i++){
    		getConsume(consumeUrl3, cookie, i+1);//消费记录
    	}
        
  
        if(bal && userinfob && bankListb) {
            finish("抓取成功");
//        	initWebView2();
        } else {
            finish("抓取失败");
        }
    }
    
	@SuppressLint("SetJavaScriptEnabled")
	private void initWebView2() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
        		wv_show.setVisibility(View.GONE);
        		wv_show2.setVisibility(View.VISIBLE);
        		wv_show2.getSettings().setJavaScriptEnabled(true); // 设置是否支持JavaScript
        		wv_show2.getSettings().setSupportZoom(true); // 设置是否支持缩放
        		wv_show2.getSettings().setBuiltInZoomControls(true); // 设置是否显示内建缩放工具
        		wv_show2.setHorizontalScrollBarEnabled(true);// 滚动条水平是否显示
        		wv_show2.setVerticalScrollBarEnabled(true); // 滚动条垂直是否显示
        		wv_show2.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        		wv_show2.getSettings().setDomStorageEnabled(true); //设置支持DomStorage
        		wv_show2.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        		wv_show2.loadUrl("https://consumeprod.alipay.com/record/standard.htm");
        		wv_show2.setWebViewClient(new MyWebViewClient());
            }
        });
	}

	private String getCtoken(String cookie) {
		String ctoken = "";
		if (cookie != null) {
			String[] s = cookie.split(";");
			for (int i = 0; i < s.length; i++) {
				if(s[i].startsWith("ctoken=")){
					ctoken = s[i].replaceAll("ctoken=", "");
				}
			}
		}
		return ctoken;
	}
    
    private boolean getBalance(String url, String cookie) {
        try{
            Map<String, String> heades = new HashMap<String, String>();
            heades.put("Host", "my.alipay.com");
            heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
            heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            heades.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            heades.put("Referer", "https://authsu18.alipay.com/login/loginResultDispatch.htm");
            heades.put("Cookie", cookie);
            heades.put("Upgrade-Insecure-Requests", "1");
            String result = HttpUtils.get(url, heades, Charset.defaultCharset());
            Log.i("支付宝余额查询响应", result);
            String html = HttpUtils.replRtnSpace(result);
//            userName = HttpUtils.getValue(html, accountnamep);
//            Log.i("用户名：", userName);
            
            List<String[]> banleanceList = HttpUtils.getListArray(html, balancep);
            if (banleanceList != null && banleanceList.size() > 0) {      //账户余额+余额宝
            	String accBal = banleanceList.get(0)[0] + banleanceList.get(0)[1];
            	aliUserInfo.setAccBal(new BigDecimal(accBal.trim()));
            	
            	String ebaoBal = banleanceList.get(0)[2] + banleanceList.get(0)[3];
            	aliUserInfo.setEbaoBal(new BigDecimal(ebaoBal.trim()));
            } else {
            	aliUserInfo.setAccBal(new BigDecimal("0.00"));
            	aliUserInfo.setEbaoBal(new BigDecimal("0.00"));
            }
            List<String[]> huabeiList = HttpUtils.getListArray(html, huabeibalp);
			if (huabeiList != null && huabeiList.size() > 0) {             //与是否开通花呗有关
				String available = huabeiList.get(0)[0] + huabeiList.get(0)[1];
				aliUserInfo.setUsedAmount(new BigDecimal(available.trim()));
				
				String total  = huabeiList.get(0)[2] + huabeiList.get(0)[3];
				aliUserInfo.setAmount(new BigDecimal(total.trim()));
			} else {
				aliUserInfo.setUsedAmount(new BigDecimal("0.00"));
				aliUserInfo.setAmount(new BigDecimal("0.00"));
			}
			return true;
			
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
    
    private boolean getUserInfo(String url, String cookie) {
        try{
            Map<String, String> heades = new HashMap<String, String>();
            heades.put("Host", "custweb.alipay.com");
            heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
            heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            heades.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            heades.put("Referer", "https://my.alipay.com/portal/i.htm?referer=https%3A%2F%2Fauth.alipay.com%2Flogin%2Findex.htm");
            heades.put("Cookie", cookie);
            heades.put("Connection", "keep-alive");
            heades.put("Upgrade-Insecure-Requests", "1");
            heades.put("Cache-Control", "max-age=0");
            String result = HttpUtils.get(url, heades, Charset.defaultCharset());
            Log.i("支付宝个人信息查询响应", result);
            String html = HttpUtils.replRtnSpace(result);
            List<String[]> accAmtList = HttpUtils.getListArray(html, userInfop);
            aliUserInfo.setUserName(accAmtList.get(0)[0].trim());
            aliUserInfo.setIdNum(accAmtList.get(0)[2].trim());
			aliUserInfo.setEmail(accAmtList.get(0)[4].equals("未添加邮箱账户名") ? "" : accAmtList.get(0)[4].trim());
			aliUserInfo.setPhone(accAmtList.get(0)[5].trim());
			aliUserInfo.setTbAccount(accAmtList.get(0)[6].equals("未绑定淘宝账户") ? "" : accAmtList.get(0)[6].trim());
			aliUserInfo.setRegTime(accAmtList.get(0)[7].trim());
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }

//    private boolean getmFormTk(String url, String cookie) {
//        try
//        {
//            Map<String, String> heades = new HashMap<String, String>();
//            heades.put("Host", "consumeprod.alipay.com");
//            heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:55.0) Gecko/20100101 Firefox/55.0");
//            heades.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//            heades.put("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//            heades.put("Referer", "https://my.alipay.com/portal/i.htm");
//            heades.put("Cookie", cookie);
//            heades.put("Connection", "keep-alive");
//            heades.put("Upgrade-Insecure-Requests", "1");
//            heades.put("Cache-Control", "max-age=0");
//            String result = HttpUtils.get(url, heades, Charset.forName("GBK"));
//            Log.i("支付宝获取消费记录返回：", result);
//            String html = HttpUtils.replRtnSpace(result);
//            mFormTk = HttpUtils.getValue(html, formtkp);
//            //通过mFormTk获取json_ua
//            return true;
//        } catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//            return false;
//        }
//    }

    /**获取帐号1426840839@qq.com
     * 获取时间2017.8.30 16：15
     **/    
    private String mRdsToken="LU1kACs0yxG50MraF8DTTF7Opj9EH6rb";
    private String mRdsUa="117n+qZ9mgNqgJnCG0Yus65w7XCvsi/yLDVddU=|nOiH84v4j/yH9I/4j/mN+Fg=|neiHGXz1WexE7V/kge6L84vugeR+0mPEdsx6H78f|mu6b9JEehvJc2F7omyecFm4djTicFW7pfulgGog7lupi6ZAlgOVF|m++T/GIRfglxCWYSZglsH2UMdQ1hDbMKrht+3n4=|mOOM6Yws|meWK74oq|luKW+WcCqR6pGqzRo9F3xXTCespi0WTWcQCkC6cUsAx922nZqx62EKIXsMJ0w2fWcdVgx7Ydqhp/EHUQsBA=|l+GOEGwadQZpHWESZAt/C3AEax9jFmUKfg12BWoeYhdkC38MewioCA==|lO6BH3ofcANsGGMZbc1t|leKNE3YTfAd/C2QSaRJ9CH8LfwenBw==|kuWKFHH8jDuQJY0lVeVX5lH6n/CM9IHum+OW+Yz7g/WNLY0=|k+uEGn/ygjWeK4MrW+tZ6F/0kf5gHGQdcgdwDGMfaBJjG3QBdgFuFmINeA+vwLPcuRKnD6Ymiy+WMoQilzuIP1chpc27Pls0QTZPNUzsTA==|kOqFG34bdABvGm0Ubxa2Fg==|keuEGn/ygjWeK4MrW+tZ6F/0kf6K5ZDnnuWZOZk=|jvmK+Zbliv+Q5JPniP6G/JPrl/iC+pXhleKN+Zbhkv2E65P8iP6R557xh/9f";

	private boolean getConsume(String url, String cookie , int page) {
        try
        {
            Map<String, String> heades = new HashMap<String, String>();
            heades.put("Host", "consumeprod.alipay.com");
            heades.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0");
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
            params.put("ua", mRdsUa);
            params.put("beginDate", mBeginDate);
            params.put("beginTime", "00:00");
            params.put("endDate", mEndDate);
            params.put("endTime", "24:00");
            params.put("dateRange", "threeMonths");
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
            
			String result = HttpUtils.post(url, params, heades,Charset.forName("GBK"));
            String html = HttpUtils.replRtnSpace(result);
			
			List<String[]> consumeList8 = HttpUtils.getListArray(html, p8);
			List<String[]> consumeList9 = HttpUtils.getListArray(html, p9);
			List<String[]> consumeList10 = HttpUtils.getListArray(html, p10);
			List<String[]> consumeList11 = HttpUtils.getListArray(html, p11);
			
			List<String[]> consumeListItem = new ArrayList<String[]>();
			if(consumeList8 != null && consumeList9 != null && consumeList10 != null && consumeList11 != null){
				consumeListItem.addAll(consumeList8);
				consumeListItem.addAll(consumeList9);
				consumeListItem.addAll(consumeList10);
				consumeListItem.addAll(consumeList11);
			}
			
			if(consumeListItem.size() > 0){
				for(int i = 0; i < consumeListItem.size() ; i++){
					BillItem b = new BillItem();
					b.setBillTitle(consumeListItem.get(i)[0]);
					b.setOtherName(consumeListItem.get(i)[1].trim().substring(0,consumeListItem.get(i)[1].trim().length()-1));
					String money = consumeListItem.get(i)[2];
					if(money.startsWith("-") || money.startsWith("+")){
						b.setAmount(new BigDecimal(money.substring(1, money.length()).trim()));
					} else {
						b.setAmount(new BigDecimal(money.trim()));
					}
					b.setStatusName(consumeListItem.get(i)[3]);
					b.setBillTime(consumeListItem.get(i)[4]);
					b.setBillSn(consumeListItem.get(i)[5]);
					b.setBillTypeName(consumeListItem.get(i)[6]);

					 Long lo = sdf.parse(b.getBillTime()).getTime(); 
					 String monthId = sdf2.format(lo);
					 if(monthId.equals(monthId1)){
						 billTtem1.add(b);
					 } else if(monthId.equals(monthId2)){
						 billTtem2.add(b);
					 } else if(monthId.equals(monthId3)){
						 billTtem3.add(b);
					 } else if(monthId.equals(monthId4)){
						 billTtem4.add(b);
					 }
				}
			}
            return true;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
        
    private boolean getBankList(String url, String cookie) {
        try
        {
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
            Log.i("支付宝获取银行卡返回：", result);
            ArrayOfBankInfo arrayOfBankInfo = FastJsonUtils.getSingleBean(result, ArrayOfBankInfo.class);
            if(arrayOfBankInfo.getStat().endsWith("ok")){
            	if(arrayOfBankInfo.getResults() != null && arrayOfBankInfo.getResults().size() > 0){
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
            	 return false;
            }
            
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
    
    private void finish(final String msg) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mDialog != null) {
                    mDialog.cancel();
                }
//                AliPayModel model = new AliPayModel();
//                model.setUserName(userName);
//                model.setAccName(loginParamsJSInterface.getUserName());
//                model.setAccAmtList(accAmtList);
//                model.setConsumeList(consumeList);
//                Intent intent = new Intent(LoginActivity.this, DataActivity.class);
//                intent.putExtra("aliPayModel", model);
//                startActivity(intent);
                // 结束当前这个Activity对象的生命
                finish();
            }
        });

    }
	
    private void showMessage(final String msg) {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(msg);
        mDialog.setCancelable(false);
        mDialog.show();
    }
	
    @SuppressLint("HandlerLeak")
	private Handler closeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1 && !isPass) {
                mDialog.cancel();
                showInfo("认证失败，请重新认证...");
            }
        }
    };

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            Message msg = new Message();
            msg.what = 1;
            closeHandler.sendMessage(msg);
        }
    };
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(TestActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
