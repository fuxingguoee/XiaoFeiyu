package com.il360.xiaofeiyu.activity.user;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.WelcomeActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.util.BitmapUtil;
import com.il360.xiaofeiyu.util.PicUtil;
import com.il360.xiaofeiyu.util.SDCardUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.CircleImageView;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.GetObjectRequest;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IDownloadTaskListener;
import com.tencent.cos.task.listener.IUploadTaskListener;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

@EActivity(R.layout.act_account_picture)
public class AccountPictureActivity extends BaseWidgetActivity {

	/** 修改头像-请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;// 相册
	private static final int CAMERA_REQUEST_CODE = 10;// 拍照
	private static final int RESULT_REQUEST_CODE = 2;// 剪切图片

	private Uri imageFilePath;
	private Bitmap picHead = null;
	private File photoFile = null;

	@ViewById
	CircleImageView account_iv_user_head;

	private String cosPath = "";
	private String srcPath = "";
	private String sourceURL = "";
	
	private Handler handler = null;
	
	private String picPath = "";

	@AfterViews
	void init() {
		//创建属于主线程的handler  
		handler = new Handler();
		initUserHead();
	}

	/**
	 * 显示用户头像
	 */
	private void initUserHead() {
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (UserUtil.getUserInfo() != null &&  !TextUtils.isEmpty(UserUtil.getUserInfo().getTxyUserPic())
						&& SDCardUtil.hasSDCard(AccountPictureActivity.this)) {
//					if (UserUtil.getUserInfo().getUserPic().startsWith("http")) {
//						ImageLoaderUtil.getInstance().displayListItemImage(UserUtil.getUserInfo().getUserPic(),
//								account_iv_user_head);
//					} else {
//						ImageLoaderUtil.getInstance().displayListItemImage(
//								Variables.APP_BASE_URL + UserUtil.getUserInfo().getUserPic(), account_iv_user_head);
//					}

					initSignForUrl();

				} else {
					account_iv_user_head.setImageResource(R.drawable.ic_touxiang);
				}
			}
		});
	}

	protected void initSignForUrl() {
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					params.put("picurl", UserUtil.getUserInfo().getTxyUserPic());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign",params);
					if (result.getSuccess()) {
						final JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							loadPic(obj.getString("result"));
						} else {
							showInfo(obj.getString("desc"));
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("AccountPictureActivity", "initSignForUrl()", e);
					LogUmeng.reportError(AccountPictureActivity.this, e);
				}
			}
		});
	}

	private void loadPic(String sign) {

		final String headUrl = UserUtil.getUserInfo().getTxyUserPic();
		final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
	                File.separator + "load";
		

		GetObjectRequest getObjectRequest = new GetObjectRequest(headUrl, savePath);
		getObjectRequest.setSign(sign);
		getObjectRequest.setListener(new IDownloadTaskListener() {
			@Override
			public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
			}

			@Override
			public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

				
				new Thread(){  
	                public void run(){    
	                	String fileName = getNameFromUrl(headUrl);
	                	picPath = savePath + File.separator + fileName;
	                    handler.post(runnableUi);   
	                    }                     
	            }.start();    
				
				Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
			}

			@Override
			public void onFailed(COSRequest COSRequest, COSResult cosResult) {
				Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
			}

			@Override
			public void onCancel(COSRequest arg0, COSResult arg1) {

			}
		});

		WelcomeActivity.getCOSClient().getObject(getObjectRequest);
	}
	
	
	 // 构建Runnable对象，在runnable中更新界面  
    Runnable  runnableUi=new  Runnable(){  
        @Override  
        public void run() {  
            //更新界面  
			Bitmap signBitmap = PicUtil.getSmallBitmap(picPath);
			account_iv_user_head.setImageBitmap(signBitmap);
        }  
          
    };  

	@Click
	void account_rl_from_image() {
		Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, IMAGE_REQUEST_CODE);
	}

	@Click
	void account_rl_from_camera() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (intent.resolveActivity(getPackageManager()) != null) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = new Date(System.currentTimeMillis());
				String fileName = format.format(date);
				// 创建一个File
				photoFile = new File(this.getExternalCacheDir(), fileName + ".jpg");
				if (photoFile != null) {
					//判断版本大于等于7.0
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
						imageFilePath =  FileProvider.getUriForFile(AccountPictureActivity.this, AccountPictureActivity.this.getApplicationContext().getPackageName() + ".provider", photoFile);
					} else {
						imageFilePath = Uri.fromFile(photoFile);
					}

					// 将Uri传递给系统相机
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);
					startActivityForResult(intent, CAMERA_REQUEST_CODE);
				}
			}
		} else {
			showInfo("请确认已经插入SD卡");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != android.app.Activity.RESULT_CANCELED) {
			switch (requestCode) {
				case IMAGE_REQUEST_CODE:// 从相册选一张
					if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
						String strpath = BitmapUtil.getimgpath(data, AccountPictureActivity.this);
						//判断版本大于等于7.0
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
							Uri newUri =FileProvider.getUriForFile(AccountPictureActivity.this, AccountPictureActivity.this.getApplicationContext().getPackageName() + ".provider",  new File(strpath));
							startPhotoZoom(newUri);
						} else {
							Uri newUri = Uri.parse("file:///" + strpath); // 将绝对路径转换为URL
							startPhotoZoom(newUri);
						}
					} else {
						startPhotoZoom(data.getData());
					}
					break;
				case CAMERA_REQUEST_CODE:// 拍照
					if (SDCardUtil.hasSDCard(AccountPictureActivity.this)) {
						startPhotoZoom(imageFilePath);
					} else {
						showInfo("未找到存储卡，无法存储照片！");
					}
					break;
				case RESULT_REQUEST_CODE:// 裁切图片
					getImageToView(data);
					break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 裁剪图片 */
	private void startPhotoZoom(Uri data) {
		Intent in = PicUtil.getCutPictureIntent(data);
		startActivityForResult(in, RESULT_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 *
	 * @param picdata
	 */
	private void getImageToView(final Intent data) {
		final ProgressDialog dialog = ProgressDialog.show(AccountPictureActivity.this, null, "头像上传中...", true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (data != null && data.getExtras() != null) {
						final Bundle extras = data.getExtras();
						if (extras != null) {
							picHead = extras.getParcelable("data");
							// uploadPic(PicUtil.compressImage(picHead, 400));
							srcPath = BitmapUtil.getAbsolutePath(AccountPictureActivity.this, picHead);
							// BitmapUtil.saveImageToGallery(AccountPictureActivity.this,picHead);
							initSignForType();
						}
					} else {
						showInfo("获取图像信息失败");
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A7));
				} finally {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
					}
					initUserHead();
				}
			}
		}).start();
	}

	protected void initSignForType() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			params.put("type", "6");// 3身份证，5签名，6头像
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					uploadPic(obj.getString("result"));
				} else {
					showInfo(obj.getString("desc"));
				}
			} else {
				showInfo(getString(R.string.A6));
			}
		} catch (Exception e) {
			showInfo(getString(R.string.A2));
			Log.e("AccountPictureActivity", "initSignForType()", e);
			LogUmeng.reportError(AccountPictureActivity.this, e);
		}
	}

	private void uploadPic(final String sign) {
		PutObjectRequest putObjectRequest = new PutObjectRequest();
		putObjectRequest.setBucket(GlobalPara.getCosName());

		cosPath = "/userPic/" + "headPic" + "_" + UserUtil.getUserInfo().getUserId() + ".png";
		putObjectRequest.setCosPath(cosPath);
		putObjectRequest.setSrcPath(srcPath);
		putObjectRequest.setSign(sign);
		putObjectRequest.setInsertOnly("0");// 0允许覆盖，1不允许覆盖

		putObjectRequest.setListener(new IUploadTaskListener() {
			@Override
			public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

				PutObjectResult result = (PutObjectResult) cosResult;
				if (result.code == 0) {
					sourceURL = result.source_url;
					PicUtil.deleteTempFile(srcPath);
					upDateUser();
				}
			}

			@Override
			public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
				sourceURL = "";
				showInfo("头像上传成功");
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

	private void upDateUser() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userJson", makeJsonPost());// userJson
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "user/updateUser",
					params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					UserUtil.getUserInfo().setTxyUserPic(sourceURL);
				} else {
					showInfo("上传失败，请重试");
				}
			}
		} catch (Exception e) {
			Log.e("UserInfoFragment", "upDateUser", e);
			LogUmeng.reportError(AccountPictureActivity.this, e);
		}
	}

	protected String makeJsonPost() {
		JSONObject json = new JSONObject();
		try {
			json.put("txyUserPic", sourceURL);
			return json.toString();
		} catch (Exception e) {
			Log.e("UserInfoFragment", "makeJsonPost", e);
		}
		return null;
	}
	
	private String getNameFromUrl(String loadUrl) {
		String[] a = loadUrl.split("/");
		String s = a[a.length - 1];
		return s;
	}

	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(AccountPictureActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (picHead != null && !picHead.isRecycled()) {
			picHead.recycle();
			picHead = null;
		}
	}

}
