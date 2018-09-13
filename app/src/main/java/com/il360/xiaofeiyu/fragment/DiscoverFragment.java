package com.il360.xiaofeiyu.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.main.MainActivity;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.MyApplication;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.util.CRequestUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@EFragment(R.layout.fra_discover)
public class DiscoverFragment extends MyFragment {
	
	@ViewById ImageView header_image_return;

	@ViewById WebView wvDiscover;
	
	@AfterViews
	void init() {
		header_image_return.setVisibility(View.GONE);
		initWebView();
	}
	
	@Click
	void header_image_return() {
		wvDiscover.loadUrl(GlobalPara.getDiscoverHomeUrl()); // 加载在线网页
		
		FragmentActivity fragAct = getActivity();
		if (fragAct != null) {
			fragAct.runOnUiThread(new Runnable() {
				public void run() {
					header_image_return.setVisibility(View.GONE);
					((MainActivity) getActivity()).showNavMenu();
				}
			});
		}
	}

	private void initWebView() {
		wvDiscover.requestFocus(); // 请求焦点
		wvDiscover.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		wvDiscover.getSettings().setJavaScriptEnabled(true); // 设置是否支持JavaScript
		wvDiscover.getSettings().setSupportZoom(false); // 设置是否支持缩放
		wvDiscover.getSettings().setBuiltInZoomControls(false); // 设置是否显示内建缩放工具
		wvDiscover.setHorizontalScrollBarEnabled(false);// 滚动条水平是否显示
		wvDiscover.setVerticalScrollBarEnabled(true); // 滚动条垂直是否显示
		wvDiscover.getSettings().setUseWideViewPort(false);// 设置此属性，可任意比例缩放
		wvDiscover.getSettings().setDomStorageEnabled(true); // 设置支持DomStorage
		// 这里是注入Java对象
//		wv_show.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
		wvDiscover.loadUrl(GlobalPara.getDiscoverHomeUrl()); // 加载在线网页
		// 触摸焦点起作用
		wvDiscover.setWebViewClient(new MyWebViewClient());
	}
	
	
	final class MyWebViewClient extends WebViewClient {
		// 在WebView中而不是系统默认浏览器中显示页面

		/**
		 * 拦截 url 跳转,在里边添加点击链接跳转或者操作
		 */
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			String newUrl = "";
			try {
				newUrl = java.net.URLDecoder.decode(url,"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Map<String, String> map = CRequestUtil.URLRequest(newUrl);
			if (map.get("type").equals("share")) {
				platformShare(map.get("imageUrl"),map.get("title"),map.get("content"));
				return true;
			} else if (map.get("type").equals("2")) {
				if(map.get("ishome") !=null && map.get("ishome").equals("0")){
					
					FragmentActivity fragAct = getActivity();
					if (fragAct != null) {
						fragAct.runOnUiThread(new Runnable() {
							public void run() {
								header_image_return.setVisibility(View.VISIBLE);
								((MainActivity) getActivity()).unShowNavMenu();
							}
						});
					}
					MobclickAgent.onEvent(MyApplication.getContextObject(), "discover_go", map.get("tag") != null ? map.get("tag") : "");
					MobclickAgent.onEvent(MyApplication.getContextObject(), "drainage_show", map.get("tag") != null ? map.get("tag") : "");
					wvDiscover.loadUrl(map.get("pageurl") + "?agentNo=" + map.get("agentNo") + "&" +"isphone = 0"); // 加载在线网页
					return super.shouldOverrideUrlLoading(view, url);
				} else {
					url = map.get("pageurl") + "?agentNo=" + map.get("agentNo") + "&isphone = 0";
					Uri uri = Uri.parse(url);
					Intent it = new Intent(Intent.ACTION_VIEW, uri);    
					startActivity(it); 
					MobclickAgent.onEvent(MyApplication.getContextObject(), "drainage_go", map.get("tag") != null ? map.get("tag") : "");
					return true;
				}
				
			} else if (map.get("type").equals("3")) {
				if(map.get("ishome") !=null && map.get("ishome").equals("0")){
					
					FragmentActivity fragAct = getActivity();
					if (fragAct != null) {
						fragAct.runOnUiThread(new Runnable() {
							public void run() {
								header_image_return.setVisibility(View.VISIBLE);
								((MainActivity) getActivity()).unShowNavMenu();
							}
						});
					}
					MobclickAgent.onEvent(MyApplication.getContextObject(), "discover_go",map.get("tag") != null ? map.get("tag") : "");
					wvDiscover.loadUrl(map.get("pageurl")); // 加载在线网页
					
					return super.shouldOverrideUrlLoading(view, url);
				} 
			} else {
//				if(map.get("type").equals("2")){
//					url = map.get("pageurl") + "?agentNo=" + map.get("agentNo") + "&isphone = 0";
//				}
				Uri uri = Uri.parse(url);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);    
				startActivity(it); 
				MobclickAgent.onEvent(MyApplication.getContextObject(), "drainage_go", map.get("tag") != null ? map.get("tag") : "");
				return true;
			}
			
			return true;
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

			super.onPageFinished(view, url);
		}

		@SuppressWarnings("deprecation")
		@Override
		/**
		 * 在每一次请求资源时，都会通过这个函数来回调
		 */
		public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

			return super.shouldInterceptRequest(view, url);
		}
	}

//	@Click
//	void tvRecommend() {
//		if(flag == 99){
//			MobclickAgent.onEvent(MyApplication.getContextObject(), "redbag_share");
//		}
//		platformShare();
//	}
	
	//分享
	public void platformShare(String imageUrl,String title,String content) {
		final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[] { SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE };
		UMImage image = new UMImage(getActivity(),imageUrl);
		String url = "";
		if (UserUtil.judgeUserInfo()) {
			url = Variables.APP_BASE_URL + "tui/personalRecommend.html?agentNo=" + UserUtil.getUserInfo().getLoginName();
		} else {
			url = Variables.APP_BASE_URL + "tui/personalRecommend.html";
		}
		
		UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(content);//描述
		
		
		new ShareAction(getActivity())
		.setDisplayList(displaylist)
		.setCallback(shareListener)
	    .withMedia(web)
	    .open();
	}

	private UMShareListener shareListener = new UMShareListener() {
		/**
		 * @descrption 分享开始的回调
		 * @param platform
		 *            平台类型
		 */
		@Override
		public void onStart(SHARE_MEDIA platform) {

		}

		/**
		 * @descrption 分享成功的回调
		 * @param platform
		 *            平台类型
		 */
		@Override
		public void onResult(SHARE_MEDIA platform) {
//			if(flag == 99){
//				 if(platform.getName().equals("WEIXIN")){
//					MobclickAgent.onEvent(MyApplication.getContextObject(), "redbag_success","微信好友");
//				} else if(platform.getName().equals("WEIXIN_CIRCLE")){
//					MobclickAgent.onEvent(MyApplication.getContextObject(), "redbag_success","微信朋友圈");
//				} else if(platform.getName().equals("QQ")){
//					MobclickAgent.onEvent(MyApplication.getContextObject(), "redbag_success","QQ");
//				} else if(platform.getName().equals("QZONE")) {
//					MobclickAgent.onEvent(MyApplication.getContextObject(), "redbag_success","QQ空间");
//				}
//			}
			Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_SHORT).show();
		}

		/**
		 * @descrption 分享失败的回调
		 * @param platform
		 *            平台类型
		 * @param t
		 *            错误原因
		 */
		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(getActivity(), "分享失败", Toast.LENGTH_SHORT).show();
		}

		/**
		 * @descrption 分享取消的回调
		 * @param platform
		 *            平台类型
		 */
		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(getActivity(), "已取消分享", Toast.LENGTH_SHORT).show();
		}
	};
	
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//    }
}
