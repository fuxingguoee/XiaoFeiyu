package com.il360.xiaofeiyu.fragment;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.util.UserUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class MyFragment extends Fragment {
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		// super.onSaveInstanceState(outState);
	}
	
	public void platformShare() {
		final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[] { SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
				SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE };
		UMImage image = new UMImage(getActivity(),
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_app_share));
		String url = "";
		if (UserUtil.judgeUserInfo()) {
			url = Variables.APP_BASE_URL + "tui/personalRecommend.html?agentNo=" + UserUtil.getUserInfo().getLoginName();
		} else {
			url = Variables.APP_BASE_URL + "tui/personalRecommend.html";
		}
		
		UMWeb web = new UMWeb(url);
        web.setTitle("【草莓商城】额度秒出 高至10000元 有芝麻分即可");//标题
        web.setThumb(image);  //缩略图
        web.setDescription("没钱不是事，邀请好友另可领66元奖励金");//描述
		new ShareAction(getActivity())
		.setDisplayList(displaylist)
		.setCallback(shareListener)//回调监听器
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
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(getActivity());// 友盟统计
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(getActivity());// 友盟统计
	}
}
