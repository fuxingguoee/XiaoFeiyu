package com.il360.xiaofeiyu.activity.main;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;

import android.net.http.SslError;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

@EActivity(R.layout.act_url_to_web)
public class UrlToWebActivity extends BaseWidgetActivity {

	@ViewById
	TextView tvActionTitle;
	@ViewById
	TextView tv_noInfo;
	@ViewById
	WebView wv_show;

	@Extra
	String title;
	@Extra
	String url;
	
	private boolean SupportZoom = true;

	@AfterViews
	void initViews() {
		
		Bundle bun = getIntent().getExtras();
		if(bun.containsKey("supportZoom")) {
			SupportZoom = bun.getBoolean("supportZoom");
		}
		
		tvActionTitle.setText(title);
		wv_show.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

		if (TextUtils.isEmpty(url)) {
			tv_noInfo.setText("暂无内容");
			wv_show.setVisibility(View.GONE);

		} else {
			wv_show.setVisibility(View.VISIBLE);
			tv_noInfo.setVisibility(View.GONE);
			wv_show.requestFocus(); // 请求焦点
			wv_show.getSettings().setJavaScriptEnabled(true); // 设置是否支持JavaScript
			wv_show.getSettings().setSupportZoom(true); // 设置是否支持缩放
			wv_show.getSettings().setBuiltInZoomControls(SupportZoom); // 设置是否显示内建缩放工具
			wv_show.setHorizontalScrollBarEnabled(SupportZoom);//滚动条水平是否显示
			wv_show.setVerticalScrollBarEnabled(SupportZoom); //滚动条垂直是否显示
			wv_show.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放
			wv_show.loadUrl(url); // 加载在线网页
			wv_show.setWebViewClient(new MyWebViewClient());
		}
	}

	private class MyWebViewClient extends WebViewClient {

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();
		}
	}
}
