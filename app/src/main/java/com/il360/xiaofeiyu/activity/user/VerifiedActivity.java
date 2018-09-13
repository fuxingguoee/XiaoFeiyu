package com.il360.xiaofeiyu.activity.user;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.WelcomeActivity;
import com.il360.xiaofeiyu.activity.mydata.AutoVerifiedActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.user.OutUserRz;
import com.il360.xiaofeiyu.util.BitmapUtil;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.IdCardUtil;
import com.il360.xiaofeiyu.util.PicUtil;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;
import com.il360.xiaofeiyu.view.CustomDialog;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_user_verified)
public class VerifiedActivity extends BaseWidgetActivity {
	private Context context = this;
	@ViewById
	TextView tvTextClick;
	@ViewById
	TextView applicationStatus;
	@ViewById
	EditText realName;
	@ViewById
	TextView phoneNum;
	@ViewById
	EditText etinviteCode;
//	@ViewById
//	EditText etpassword1;
//	@ViewById
//	EditText etpassword2;
	@ViewById
	RelativeLayout rlIDPhoto1;
	@ViewById
	RelativeLayout rlIDPhoto2;
	@ViewById
	RelativeLayout rlIDPhoto3;
	@ViewById
	ImageView IDPhoto1;//正面照
	@ViewById
	ImageView IDPhoto2;//反面照
	@ViewById
	ImageView IDPhoto3;//手持照
	@ViewById
	Button submit;
	private int flag = 0;
	private File photoFile = null;
	private Uri imageFilePath;
	private Bitmap photo1 = null;
	private Bitmap photo2 = null;
	private Bitmap photo3 = null;
	
	private static Timer timer = null;
	
	Pattern pt = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
	
	protected ProgressDialog transDialog;
	
	private String srcPath1 = "";
	private String srcPath2 = "";
	private String srcPath3 = "";
	
	private String cosPath1 = "";
	private String cosPath2 = "";
	private String cosPath3 = "";
	
	private String sourceURL1 = "";
	private String sourceURL2 = "";
	private String sourceURL3 = "";
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		try {
			if(outState != null) {
				String path = "content://media" + imageFilePath.getPath();
				outState.putString("picPath", path);
			}
			super.onSaveInstanceState(outState);
		} catch (Exception e) {
			LogUmeng.reportError(VerifiedActivity.this, e);
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		try {
			if(savedInstanceState != null) {
				String path = savedInstanceState.getString("picPath");
				imageFilePath = Uri.parse(path);
			}
			super.onRestoreInstanceState(savedInstanceState);
		} catch (Exception e) {
			LogUmeng.reportError(VerifiedActivity.this, e);
		}
	}
	
	@AfterViews
	void init() {
		if(GlobalPara.getCanAutoVerified() && GlobalPara.getRemainTimes() > 0 
				&& GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getNameRz() != null 
				&& (GlobalPara.getOutUserRz().getNameRz() == -1 || GlobalPara.getOutUserRz().getNameRz() == 0)){
			tvTextClick.setText("人脸认证");
		}
		
		OutUserRz outUserRz = GlobalPara.getOutUserRz();
		
		if(outUserRz != null && outUserRz.getNameRz() != null){
			if(outUserRz.getNameRz() == -1){
				applicationStatus.setText("审核未通过，请重新申请！\n原因：" + outUserRz.getNameDesc());
				applicationStatus.setTextColor(0xFFFF0000);
			} else if(outUserRz.getNameRz() == 0){
				applicationStatus.setText("你还未提交过实名认证申请！");
				applicationStatus.setTextColor(0xFF000000);
			} else if(outUserRz.getNameRz() == 1){
				applicationStatus.setText("您已审核通过！");
				applicationStatus.setTextColor(0xFF000000);
			} else if(outUserRz.getNameRz() == 2){
				applicationStatus.setText("正在审核中！");
				applicationStatus.setTextColor(0xFF000000);
			} 
			realName.setText(outUserRz.getName());
			etinviteCode.setText(outUserRz.getIdNo());
		} else {
			applicationStatus.setText("你还未提交过实名认证申请！");
			applicationStatus.setTextColor(0xFF000000);
		}
		
		if (UserUtil.getUserInfo() != null && !TextUtils.isEmpty(UserUtil.getUserInfo().getLoginName())) {
			phoneNum.setText(UserUtil.getUserInfo().getLoginName());
		} else {
			phoneNum.setText("");
		}
	}
	
	@Click
	void tvTextClick(){
		if(GlobalPara.getCanAutoVerified() && GlobalPara.getRemainTimes() > 0 
				&& GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getNameRz() != null 
				&& (GlobalPara.getOutUserRz().getNameRz() == -1 || GlobalPara.getOutUserRz().getNameRz() == 0)){
			Intent intent = new Intent(VerifiedActivity.this,AutoVerifiedActivity_.class);
			startActivity(intent);
			VerifiedActivity.this.finish();
		}
	}

	@Click
	void submit() {
		if (isOk()) {
			submit.setClickable(false);	
			transDialog = ProgressDialog.show(VerifiedActivity.this, null,
					getString(R.string.C16), true);
			ExecuteTask.execute(new Runnable() {

				@Override
				public void run() {
					if(!TextUtils.isEmpty(sourceURL1) && !TextUtils.isEmpty(sourceURL2) && !TextUtils.isEmpty(sourceURL3)){
						initVerified();
					} else {
						initSign();
					}
				}
			});
		}
	}

	protected void initSign() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			params.put("type", "3");//3身份证，5签名，6头像
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign", params);
			if (result.getSuccess()) {
				if (ResultUtil.isOutTime(result.getResult()) != null) {
					showInfo(ResultUtil.isOutTime(result.getResult()));
					Intent intent = new Intent(VerifiedActivity.this, LoginActivity_.class);
					startActivity(intent);
				} else {
					JSONObject obj = new JSONObject(result.getResult());
					if (obj.getInt("code") == 1) {
						upUserRegPic(obj.getString("result"),1);
					} else {
						showInfo(obj.getString("desc"));
					}
				}
			} else {
				showInfo(getString(R.string.A6));
			}
		} catch (Exception e) {
			showInfo(getString(R.string.A2));
			Log.e("VerifiedActivity", "initSign()", e);
			LogUmeng.reportError(VerifiedActivity.this, e);
		} finally {
			if (transDialog != null && transDialog.isShowing()) {
				transDialog.dismiss();
			}
			submit.setClickable(true);
		}
	}

	private void initVerified() {
		try {
			String Data = initData();
			if (Data != null) {

				Map<String, String> params = new HashMap<String, String>();
				params.put("rushJson", Data);
				TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "card/certificate",
						params);
				if (result.getSuccess()) {
					if (ResultUtil.isOutTime(result.getResult()) != null) {
						showInfo(ResultUtil.isOutTime(result.getResult()));
						Intent intent = new Intent(VerifiedActivity.this, LoginActivity_.class);
						startActivity(intent);
					} else {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							JSONObject objRes = obj.getJSONObject("result");
							if (objRes.getInt("returnCode") == 1) {
								initUserReg();
							} else {
								showInfo(objRes.getString("returnMessage"));
							}
						} else {
							showInfo(obj.getString("desc"));
						}
					}
				} else {
					showInfo(getString(R.string.A6));
				}
			} else {
				showInfo("提交的数据有误！");
			}
		} catch (Exception e) {
			showInfo(getString(R.string.A2));
			Log.e("VerifiedActivity", "initVerified()", e);
			LogUmeng.reportError(VerifiedActivity.this, e);
		} finally {
			if (transDialog != null && transDialog.isShowing()) {
				transDialog.dismiss();
			}
			submit.setClickable(true);
		}
	}
	
	private void initUserReg() {
		try {
			GlobalPara.outUserRz = null;
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
					"card/queryUserInfo", params);
			if (result.getSuccess()) {
				if (ResultUtil.isOutTime(result.getResult()) != null) {
					showInfo(ResultUtil.isOutTime(result.getResult()));
					Intent intent = new Intent(VerifiedActivity.this, LoginActivity_.class);
					startActivity(intent);
				} else {
					JSONObject obj = new JSONObject(result.getResult());
					if (obj.getInt("code") == 1) {
						JSONObject objRes = obj.getJSONObject("result");
						JSONObject objRetRes = objRes.getJSONObject("returnResult");
						GlobalPara.outUserRz = FastJsonUtils.getSingleBean(objRetRes.toString(), OutUserRz.class);
					}
				}
			}
		} catch (Exception e) {
			Log.e("VerifiedActivity", "initUserReg", e);
			LogUmeng.reportError(context, e);
		}finally {
			Intent intent = new Intent(context, VerifiedFinishActivity_.class);
			intent.putExtra("realName", realName.getText().toString());
			startActivityForResult(intent, 0);
//			showDialog("工作人员将在5分钟内审核完成请耐心等待!");
		}
	}

	private void showInfo(final String info) {
		
		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(VerifiedActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Click
	void rlIDPhoto1() {
		flag = 1;
		rlIDPhoto1.setClickable(false);
		showDialog();

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				rlIDPhoto1.setClickable(true);
			}
		};
		timer.schedule(task, 1000 * 1);
	}

	@Click
	void rlIDPhoto2() {
		flag = 2;
		rlIDPhoto2.setClickable(false);
		showDialog();

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				rlIDPhoto2.setClickable(true);
			}
		};
		timer.schedule(task, 1000 * 1);
	}
	
	@Click
	void rlIDPhoto3() {
		flag = 3;
		rlIDPhoto3.setClickable(false);
		showDialog();

		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				rlIDPhoto3.setClickable(true);
			}
		};
		timer.schedule(task, 1000 * 1);
	}

	/** 验证输入 */
	private boolean isOk() {
		if(TextUtils.isEmpty(ViewUtil.getText(phoneNum))){
			showInfo("获取手机号失败，请返回重试或重新登录");
		} else if (TextUtils.isEmpty(ViewUtil.getText(realName))) {
			showInfo(getString(R.string.C1));
		} else if (!IdCardUtil.isIdcard(ViewUtil.getText(etinviteCode))) {
			showInfo("请输入正确的身份证号码");
		} else if (photo1 == null) {
			showInfo(getString(R.string.C9));
		} else if (photo2 == null) {
			showInfo(getString(R.string.C10));
		} else if (photo3 == null) {
			showInfo(getString(R.string.C11));
		} else {
			return true;
		}
		return false;
	}

	// 提示框
	private void showDialog() {
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(R.string.app_name);
		builder.setMessage("请选择图片来源");
		builder.setPositiveButton("取消", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("拍照 ", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (intent.resolveActivity(getPackageManager()) != null) {
						SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
						Date date = new Date(System.currentTimeMillis());
						String fileName = format.format(date);
						// 创建一个File
						photoFile = new File(VerifiedActivity.this.getExternalCacheDir(), fileName + ".jpg");
						if (photoFile != null) {
							//判断版本大于等于7.0
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
								imageFilePath =  FileProvider.getUriForFile(VerifiedActivity.this, VerifiedActivity.this.getApplicationContext().getPackageName() + ".provider", photoFile);
							} else {
								imageFilePath = Uri.fromFile(photoFile);
							}

							// 将Uri传递给系统相机
							intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath);
							startActivityForResult(intent, flag);
						}
					}
					
					/*Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					ContentValues values = new ContentValues(3);
					values.put(MediaStore.Images.Media.DISPLAY_NAME, "Forun Image");
					values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
					values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
					imageFilePath = VerifiedActivity.this.getContentResolver()
							.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFilePath); // 这样就将文件的存储方式和uri指定到了Camera应用中
					startActivityForResult(intent, flag);*/
				} else {
					Toast.makeText(VerifiedActivity.this, "请确认已经插入SD卡", Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
				try {
					timer = new Timer(true);
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							VerifiedActivity.this.finish();
						}
					}, 180 * 1000);
				} catch (Exception e) {
					e.printStackTrace();
					LogUmeng.reportError(VerifiedActivity.this, e);
				}
			}
		});
		builder.create().show();
	}
	
	private Bitmap getImageToView(Intent data , int i) {
		Bitmap photo = null;
		if (data == null) {
//			photo = PicUtil.getSmallBitmap(imageFilePath.getPath());

			String filepath = "";
			if (imageFilePath.getScheme() != null && "content".equalsIgnoreCase(imageFilePath.getScheme())) {
				filepath = photoFile.getAbsolutePath();
			} else {
				filepath = imageFilePath.getPath();
			}
			photo = PicUtil.getSmallBitmap(filepath);

			if (i == 1) {
				srcPath1 = BitmapUtil.getAbsolutePath(VerifiedActivity.this, photo);
			} else if (i == 2) {
				srcPath2 = BitmapUtil.getAbsolutePath(VerifiedActivity.this, photo);
			} else if (i == 3) {
				srcPath3 = BitmapUtil.getAbsolutePath(VerifiedActivity.this, photo);
			}
//			PicUtil.deleteTempFile(filePath);
		} else {
			String filePath = BitmapUtil.getimgpath(data, this);
			// 是否选择图片类型
			if (filePath.endsWith("jpg") || filePath.endsWith("png")
					|| filePath.endsWith("JPG") || filePath.endsWith("PNG")) {
				photo = PicUtil.getSmallBitmap(filePath);
				if (i == 1) {
					srcPath1 = BitmapUtil.getAbsolutePath(VerifiedActivity.this, photo);
				} else if (i == 2) {
					srcPath2 = BitmapUtil.getAbsolutePath(VerifiedActivity.this, photo);
				} else if (i == 3) {
					srcPath3 = BitmapUtil.getAbsolutePath(VerifiedActivity.this, photo);
				}
			} else {
				Toast.makeText(VerifiedActivity.this, R.string.C14,
						Toast.LENGTH_SHORT).show();
			}
		}
		return photo;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode != android.app.Activity.RESULT_CANCELED) {
				switch (requestCode) {
				case 1:
					photo1 = getImageToView(data,1);
					IDPhoto1.setImageBitmap(photo1);
					break;
				case 2:
					photo2 = getImageToView(data,2);
					IDPhoto2.setImageBitmap(photo2);
					break;
				case 3:
					photo3 = getImageToView(data,3);
					IDPhoto3.setImageBitmap(photo3);
					break;
				}
			}
		} catch (Exception e) {
			Log.e("VerifiedActivity", "获取照片异常", e);
			LogUmeng.reportError(VerifiedActivity.this, e);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(timer != null) {
			timer.cancel();
			timer = null;
		}
	}
	
	private String initData(){
		JSONObject json = new JSONObject();
		try {
			json.put("name", realName.getText().toString());
			json.put("phone", phoneNum.getText().toString());
			json.put("idNo", etinviteCode.getText().toString());

			json.put("identityPic1", sourceURL1);
			json.put("identityPic2", sourceURL2);
			json.put("qualificationPic", sourceURL3);
			
			json.put("token", UserUtil.getToken());
			return json.toString();
		} catch (Exception e) {
			Log.e("VerifiedActivity", "initData", e);
			LogUmeng.reportError(VerifiedActivity.this, e);
		}
		return null;
	}
	
	private void showDialog(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				if(transDialog!=null && transDialog.isShowing()){
					transDialog.dismiss();
				}
				CustomDialog.Builder builder = new CustomDialog.Builder(VerifiedActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage(message);
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						VerifiedActivity.this.finish();
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if(!photo1.isRecycled()) {
				photo1.recycle();
			}
			if(!photo2.isRecycled()) {
				photo2.recycle();
			}
			if(!photo3.isRecycled()) {
				photo3.recycle();
			}
		} catch (Exception e) {
		}
	}
	
	

	private void upUserRegPic(final String sign, final int i) {

		PutObjectRequest putObjectRequest = new PutObjectRequest();
		putObjectRequest.setBucket(GlobalPara.getCosName());
		if (i == 1) {
			cosPath1 = "/card/" + "idFront" + "_" + UserUtil.getUserInfo().getUserId() + ".png";
			putObjectRequest.setCosPath(cosPath1);
			putObjectRequest.setSrcPath(srcPath1);
		} else if (i == 2) {
			cosPath2 = "/card/" + "idBack" + "_" + UserUtil.getUserInfo().getUserId() + ".png";
			putObjectRequest.setCosPath(cosPath2);
			putObjectRequest.setSrcPath(srcPath2);
		} else if (i == 3) {
			cosPath3 = "/card/" + "holdId" + "_" + UserUtil.getUserInfo().getUserId() + ".png";
			putObjectRequest.setCosPath(cosPath3);
			putObjectRequest.setSrcPath(srcPath3);
		}
		putObjectRequest.setSign(sign);
		putObjectRequest.setInsertOnly("0");//0允许覆盖，1不允许覆盖

		putObjectRequest.setListener(new IUploadTaskListener() {
			@Override
			public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

				PutObjectResult result = (PutObjectResult) cosResult;
				if (result.code == 0) {
					if (i == 1) {
						sourceURL1 = result.source_url;
						PicUtil.deleteTempFile(srcPath1);
						upUserRegPic(sign,2);
					} else if (i == 2) {
						sourceURL2 = result.source_url;
						PicUtil.deleteTempFile(srcPath2);
						upUserRegPic(sign,3);
					} else if (i == 3) {
						sourceURL3 = result.source_url;
						PicUtil.deleteTempFile(srcPath3);
						initVerified();
					}
				}
//				StringBuilder stringBuilder = new StringBuilder();
//				stringBuilder.append(" 上传结果： ret=" + result.code + "; msg =" + result.msg + "\n");
//				stringBuilder.append(" access_url= " + result.access_url + "\n");
//				stringBuilder.append(" resource_path= " + result.resource_path + "\n");
//				stringBuilder.append(" url= " + result.url);
//				Log.w("TEST", stringBuilder.toString());
			}

			@Override
			public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
				if (i == 1) {
					sourceURL1 = "";
					showInfo("身份证正面照片上传失败");
				} else if (i == 2) {
					sourceURL2 = "";
					showInfo("身份证背面照片上传失败");
				} else if (i == 3) {
					sourceURL3 = "";
					showInfo("手持身份证照片上传失败");
				}
			}

			@Override
			public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
			}

			@Override
			public void onCancel(COSRequest arg0, COSResult arg1) {
				if (i == 1) {
					sourceURL1 = "";
					showInfo("身份证正面照片上传已取消");
				} else if (i == 2) {
					sourceURL2 = "";
					showInfo("身份证背面照片上传已取消");
				} else if (i == 3) {
					sourceURL3 = "";
					showInfo("手持身份证照片上传已取消");
				}
			}
		});
		WelcomeActivity.getCOSClient().putObject(putObjectRequest);
	}
}
