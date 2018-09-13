package com.il360.xiaofeiyu.activity.main;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.MyFragmentActivity;
import com.il360.xiaofeiyu.activity.user.RecommendActivity_;
import com.il360.xiaofeiyu.adapter.FragmentTabAdapter;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.MyApplication;
import com.il360.xiaofeiyu.common.Variables;
import com.il360.xiaofeiyu.fragment.AuthenFragment_;
import com.il360.xiaofeiyu.fragment.DiscoverFragment_;
import com.il360.xiaofeiyu.fragment.HomeFragment_;
import com.il360.xiaofeiyu.fragment.OrderFragment;
import com.il360.xiaofeiyu.fragment.OrderFragment_;
import com.il360.xiaofeiyu.fragment.UserFragment_;
import com.il360.xiaofeiyu.model.user.ProjectileContent;
import com.il360.xiaofeiyu.util.FileUtil;
import com.il360.xiaofeiyu.util.ImageFromTxyUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.CustomDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.ydtong.autoupdate.AuDialogButtonListener;
import com.ydtong.autoupdate.AuUpdateAgent;
import com.ydtong.autoupdate.AuUpdateStatus;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_main)
public class MainActivity extends MyFragmentActivity {
	public static final String TAG = "MainActivity";

	/** 页面碎片 **/
	public static List<Fragment> fragments = new ArrayList<Fragment>();
	/** 处理fragment不重绘的适配器 **/
	private FragmentTabAdapter adapter;
	/** 按返回键的时间 **/
	private long exitTime;

	/** 底部导航栏 **/
	@ViewById
	RadioGroup rgpNavMenu;
	/** 默认显示第几个页面碎片,不传为0 **/
	@ViewById
	TextView tvLine;

	@Extra
	int mShowTabIndex;
	
	@Extra String title,text;

	// 是否由fragment切换
	private int loginFragmentChanged = -1;
	// fragment切换位置
	private int fragmentIndex = 0;
	
	protected ProgressDialog transDialog;

	public int getLoginFragmentChanged() {
		return loginFragmentChanged;
	}

	private static String myToken;

	@Override
	public void onResume() {
		super.onResume();
		if(UserUtil.judgeUserInfo()){
			if(TextUtils.isEmpty(myToken) || !myToken.equals(UserUtil.getToken())){
				//显示红包广告
				myToken = UserUtil.getToken();
				if(UserUtil.getProjectileContent() != null) {
					showCoupon(UserUtil.getProjectileContent());
				}
			}
		}
	}
	
	  @Override
		protected void onCreate(Bundle arg0) {
			// TODO Auto-generated method stub
			super.onCreate(arg0);
			
			AuUpdateAgent.forceUpdate(MainActivity.this);
			AuUpdateAgent.setDialogListener(new AuDialogButtonListener() {
				
				@Override
				public void onClick(int status) {
					// TODO Auto-generated method stub
					
					switch(status) {
					case AuUpdateStatus.Update:
						transDialog = ProgressDialog.show(MainActivity.this, null, "更新中...", true);
						
						break;
					case AuUpdateStatus.Ignore:
						System.exit(0);
						break;
					default:
						System.exit(0);
						break;
					}
				}
			});
	  }

	@AfterViews
	void init() {
		initView();
		initPop();
		createFiles();
		if(!TextUtils.isEmpty(title) && !TextUtils.isEmpty(text)){
			showDialog(title,text);
		}
	}

	private void initView() {
		if (fragments != null) {
			fragments.clear();
		}
		fragments.add(new HomeFragment_());
		fragments.add(new AuthenFragment_());
		fragments.add(new DiscoverFragment_());
		fragments.add(new OrderFragment_());
		fragments.add(new UserFragment_());
		adapter = new FragmentTabAdapter(this, fragments, R.id.flytTabContent, rgpNavMenu, mShowTabIndex);
		adapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
			@Override
			public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
				if (index == 3) {
					Fragment fragment = findFragment(index);
					if (fragment != null) {
						((OrderFragment) fragment).refreshOrder();
					}
				}
				fragmentIndex = index;
			}
		});
	}
	
    private  Fragment findFragment(int index) {
        return this.getSupportFragmentManager().findFragmentByTag(String.valueOf(index));
    }
    
	// 切换到RecoveryFragment
	public void changeToRecoveryFragment() {
		adapter.showTab(1);
	}
	
	// 切换到AuthenFragment
	public void changeToAuthenFragment() {
		adapter.showTab(2);
	}

	public void showNavMenu() {
		tvLine.setVisibility(View.VISIBLE);
		rgpNavMenu.setVisibility(View.VISIBLE);
	}

	public void unShowNavMenu() {
		tvLine.setVisibility(View.GONE);
		rgpNavMenu.setVisibility(View.GONE);
	}

	/**
	 * 初始化SD上的工作目录
	 */
	private void createFiles() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			showInfo(MainActivity.this.getResources().getString(R.string.sd_check_fail));
		} else {
			FileUtil.createDir(Variables.APP_CACHE_SDPATH);
			Log.d(TAG, "init sdcard work dir...");
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					showInfo(MainActivity.this.getResources().getString(R.string.exit_app));
					exitTime = System.currentTimeMillis();
				} else {
					GlobalPara.clean();
					UserUtil.clearUserInfo(); //清空用户登录数据
					finish();
				}
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}
	
	private void showDialog(final String title,final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				if(transDialog!=null && transDialog.isShowing()){
					transDialog.dismiss();
				}
				CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
				builder.setTitle(title);
				builder.setMessage(message);
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}

	/** pop */
	private View pop;
	private PopupWindow popWin;
	/** 初始化Pop */
	private void initPop() {
		pop = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_pop_login_show, null);
		popWin = new PopupWindow(pop, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		popWin.setFocusable(true);
	}

	private void showCoupon(final ProjectileContent projectileContent){
		TextView textview = (TextView)pop.findViewById(R.id.tv_periphery);
		ImageView ivCancel = (ImageView)pop.findViewById(R.id.ivCancel);
		ImageView ivShowCoupon = (ImageView)pop.findViewById(R.id.ivShowCoupon);
		ImageView ivShowCoupon2 = (ImageView)pop.findViewById(R.id.ivShowCoupon2);
		TextView tvTitle = (TextView)pop.findViewById(R.id.tvTitle);
		LinearLayout linearLayout = (LinearLayout)pop.findViewById(R.id.linearLayout);
		LinearLayout linearLayout2 = (LinearLayout)pop.findViewById(R.id.linearLayout2);//红包

		if (projectileContent.getFrameId() != null && projectileContent.getFrameId() == 1) {
			linearLayout.setVisibility(View.GONE);
			linearLayout2.setVisibility(View.VISIBLE);
			if(projectileContent.getPicUrl() != null){
				ImageFromTxyUtil.loadImage(MainActivity.this, projectileContent.getPicUrl(), ivShowCoupon2);
			}
		} else if (projectileContent.getFrameId() != null && projectileContent.getFrameId() == 2) {
			linearLayout.setVisibility(View.VISIBLE);
			linearLayout2.setVisibility(View.GONE);
			if(projectileContent.getPicUrl() != null){
				ImageFromTxyUtil.loadImage(MainActivity.this, projectileContent.getPicUrl(), ivShowCoupon);
			}
			tvTitle.setText(projectileContent.getDesc());
			MobclickAgent.onEvent(MyApplication.getContextObject(), "drainage_show", projectileContent.getTitle());

		} else if (projectileContent.getFrameId() != null && projectileContent.getFrameId() == 3) {
			linearLayout.setVisibility(View.VISIBLE);
			linearLayout2.setVisibility(View.GONE);
			if(projectileContent.getPicUrl() != null){
				ImageFromTxyUtil.loadImage(MainActivity.this, projectileContent.getPicUrl(), ivShowCoupon);
			}
			tvTitle.setText(projectileContent.getDesc());
		}

		MobclickAgent.onEvent(MyApplication.getContextObject(), "top_show", projectileContent.getTitle());

		linearLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MobclickAgent.onEvent(MyApplication.getContextObject(), "top_go" ,projectileContent.getTitle());
				if(projectileContent.getFrameId() != null && projectileContent.getFrameId() == 2) {
					MobclickAgent.onEvent(MyApplication.getContextObject(), "drainage_go" ,projectileContent.getTitle());
				}

				Uri uri = Uri.parse(projectileContent.getHtmlUrl());
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				MainActivity.this.startActivity(it);
				popWin.dismiss();
			}

		});

		linearLayout2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, RecommendActivity_.class);
				MainActivity.this.startActivity(intent);

				popWin.dismiss();
			}
		});

		textview.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popWin.dismiss();//点击pop外围消失
			}
		});

		Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
			@Override
			public boolean queueIdle() {
				popWin.showAtLocation(pop, Gravity.CENTER, 0, 0);
				return false;
			}
		});
	}

	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(MainActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
	}
}
