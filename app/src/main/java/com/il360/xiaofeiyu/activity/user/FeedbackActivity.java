package com.il360.xiaofeiyu.activity.user;

import java.io.File;
import java.io.IOException;
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
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.IUploadTaskListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_feedback)
public class FeedbackActivity extends BaseWidgetActivity {

	/** 修改头像-请求码 */
	private static final int IMAGE_REQUEST_CODE = 0;// 相册
	private static final int CAMERA_REQUEST_CODE = 10;// 拍照
	private static final int RESULT_REQUEST_CODE = 2;// 剪切图片

	private Uri imageFilePath;
	private Bitmap picHead1 = null;
	private Bitmap picHead2 = null;
	private Bitmap picHead3 = null;
	private Bitmap picHead4 = null;
	private Bitmap picHead5 = null;
	private Bitmap picHead6 = null;
	private File photoFile = null;

	@ViewById
	EditText etFeedback;
	@ViewById
	TextView tvToSubmit;
	@ViewById
	ImageView ivFeedbackView1;
	@ViewById
	ImageView ivFeedbackView2;
	@ViewById
	ImageView ivFeedbackView3;
	@ViewById
	ImageView ivFeedbackView4;
	@ViewById
	ImageView ivFeedbackView5;
	@ViewById
	ImageView ivFeedbackView6;

	int flag = 0;

	private String cosPath = "";
	private String cosPath1 = "";
	private String cosPath2 = "";
	private String cosPath3 = "";
	private String cosPath4 = "";
	private String cosPath5 = "";
	private String cosPath6 = "";
	private String srcPath = "";
	private String srcPath1 = "";
	private String srcPath2 = "";
	private String srcPath3 = "";
	private String srcPath4 = "";
	private String srcPath5 = "";
	private String srcPath6 = "";
	private String sourceURL = "";
	private String sourceURL1 = "";
	private String sourceURL2 = "";
	private String sourceURL3 = "";
	private String sourceURL4 = "";
	private String sourceURL5 = "";
	private String sourceURL6 = "";

	private Handler handler = null;

	private String picPath = "";

	@AfterViews
	void init() {
		// 创建属于主线程的handler
		handler = new Handler();
	}

	// /**
	// * 显示用户头像
	// */
	// private void initFeedbackImg() {
	// this.runOnUiThread(new Runnable() {
	// @Override
	// public void run() {
	// if (SDCardUtil.hasSDCard(FeedbackActivity.this)) {
	// initSignForUrl();
	// }
	// }
	// });
	// }

	// protected void initSignForUrl() {
	// ExecuteTask.execute(new Runnable() {
	//
	// @Override
	// public void run() {
	// try {
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("token", UserUtil.getToken());
	// params.put("picurl", UserUtil.getUserInfo().getTxyUserPic());
	// TResult<Boolean, String> result =
	// HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign",params);
	// if (result.getSuccess()) {
	// final JSONObject obj = new JSONObject(result.getResult());
	// if (obj.getInt("code") == 1) {
	// loadPic(obj.getString("result"));
	// } else {
	// showInfo(obj.getString("desc"));
	// }
	// } else {
	// showInfo(getString(R.string.A6));
	// }
	// } catch (Exception e) {
	// showInfo(getString(R.string.A2));
	// Log.e("AccountPictureActivity", "initSignForUrl()", e);
	// LogUmeng.reportError(FeedbackActivity.this, e);
	// }
	// }
	// });
	// }

	// private void loadPic(String sign) {
	//
	// final String headUrl = UserUtil.getUserInfo().getTxyUserPic();
	// final String savePath =
	// Environment.getExternalStorageDirectory().getAbsolutePath() +
	// File.separator + "load";
	//
	//
	// GetObjectRequest getObjectRequest = new GetObjectRequest(headUrl,
	// savePath);
	// getObjectRequest.setSign(sign);
	// getObjectRequest.setListener(new IDownloadTaskListener() {
	// @Override
	// public void onProgress(COSRequest cosRequest, final long currentSize,
	// final long totalSize) {
	// }
	//
	// @Override
	// public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
	//
	//
	// new Thread(){
	// public void run(){
	// String fileName = getNameFromUrl(headUrl);
	// picPath = savePath + File.separator + fileName;
	// handler.post(runnableUi);
	// }
	// }.start();
	//
	// Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
	// }
	//
	// @Override
	// public void onFailed(COSRequest COSRequest, COSResult cosResult) {
	// Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
	// }
	//
	// @Override
	// public void onCancel(COSRequest arg0, COSResult arg1) {
	//
	// }
	// });
	//
	// WelcomeActivity.getCOSClient().getObject(getObjectRequest);
	// }

	// // 构建Runnable对象，在runnable中更新界面
	// Runnable runnableUi=new Runnable(){
	// @Override
	// public void run() {
	// //更新界面
	// Bitmap signBitmap = PicUtil.getSmallBitmap(picPath);
	// ivFeedbackView1.setImageBitmap(signBitmap);
	// }
	// };

	@Click
	void ivFeedbackView1() {
		flag = 1;
		Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, flag);
	}

	@Click
	void ivFeedbackView2() {
		flag = 2;
		Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, flag);
	}

	@Click
	void ivFeedbackView3() {
		flag = 3;
		Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, flag);
	}

	@Click
	void ivFeedbackView4() {
		flag = 4;
		Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, flag);
	}

	@Click
	void ivFeedbackView5() {
		flag = 5;
		Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, flag);
	}

	@Click
	void ivFeedbackView6() {
		flag = 6;
		Intent intentFromGallery = new Intent(Intent.ACTION_PICK);
		intentFromGallery.setType("image/*"); // 设置文件类型
		intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intentFromGallery, flag);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != android.app.Activity.RESULT_CANCELED) {
			switch (requestCode) {
			case 1:// 从相册选一张
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					String strpath = BitmapUtil.getimgpath(data, FeedbackActivity.this);
					Uri newUri = Uri.parse("file:///" + strpath); // 将绝对路径转换为URL
					showPic(newUri, flag);
				} else {
					showPic(data.getData(), flag);
				}
				break;
			case 2:// 从相册选一张
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					String strpath = BitmapUtil.getimgpath(data, FeedbackActivity.this);
					Uri newUri = Uri.parse("file:///" + strpath); // 将绝对路径转换为URL
					showPic(newUri, flag);
				} else {
					showPic(data.getData(), flag);
				}
				break;
			case 3:// 从相册选一张
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					String strpath = BitmapUtil.getimgpath(data, FeedbackActivity.this);
					Uri newUri = Uri.parse("file:///" + strpath); // 将绝对路径转换为URL
					showPic(newUri, flag);
				} else {
					showPic(data.getData(), flag);
				}
				break;
			case 4:// 从相册选一张
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					String strpath = BitmapUtil.getimgpath(data, FeedbackActivity.this);
					Uri newUri = Uri.parse("file:///" + strpath); // 将绝对路径转换为URL
					showPic(newUri, flag);
				} else {
					showPic(data.getData(), flag);
				}
				break;
			case 5:// 从相册选一张
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					String strpath = BitmapUtil.getimgpath(data, FeedbackActivity.this);
					Uri newUri = Uri.parse("file:///" + strpath); // 将绝对路径转换为URL
					showPic(newUri, flag);
				} else {
					showPic(data.getData(), flag);
				}
				break;
			case 6:// 从相册选一张
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					String strpath = BitmapUtil.getimgpath(data, FeedbackActivity.this);
					Uri newUri = Uri.parse("file:///" + strpath); // 将绝对路径转换为URL
					showPic(newUri, flag);
				} else {
					showPic(data.getData(), flag);
				}
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/** 显示图片 */
	private void showPic(Uri data, int flag) {
		if (flag == 1) {
			// 获取图片 Bitmap picHead1
			try {
				picHead1 = BitmapUtil.getBitmapFormUri(FeedbackActivity.this, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ivFeedbackView1.setImageBitmap(picHead1);
			srcPath1 = BitmapUtil.getAbsolutePath(FeedbackActivity.this, picHead1);
			initSignForType(flag);
		} else if (flag == 2) {
			try {
				picHead2 = BitmapUtil.getBitmapFormUri(FeedbackActivity.this, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ivFeedbackView2.setImageBitmap(picHead2);
			srcPath2 = BitmapUtil.getAbsolutePath(FeedbackActivity.this, picHead1);
			initSignForType(flag);
		} else if (flag == 3) {
			try {
				picHead3 = BitmapUtil.getBitmapFormUri(FeedbackActivity.this, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ivFeedbackView3.setImageBitmap(picHead3);
			srcPath3 = BitmapUtil.getAbsolutePath(FeedbackActivity.this, picHead1);
			initSignForType(flag);
		} else if (flag == 4) {
			try {
				picHead4 = BitmapUtil.getBitmapFormUri(FeedbackActivity.this, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ivFeedbackView4.setImageBitmap(picHead4);
			srcPath4 = BitmapUtil.getAbsolutePath(FeedbackActivity.this, picHead1);
			initSignForType(flag);
		} else if (flag == 5) {
			try {
				picHead5 = BitmapUtil.getBitmapFormUri(FeedbackActivity.this, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ivFeedbackView5.setImageBitmap(picHead5);
			srcPath5 = BitmapUtil.getAbsolutePath(FeedbackActivity.this, picHead1);
			initSignForType(flag);
		} else if (flag == 6) {
			try {
				picHead6 = BitmapUtil.getBitmapFormUri(FeedbackActivity.this, data);
			} catch (IOException e) {
				e.printStackTrace();
			}
			ivFeedbackView6.setImageBitmap(picHead6);
			srcPath6 = BitmapUtil.getAbsolutePath(FeedbackActivity.this, picHead1);
			initSignForType(flag);
		}
	}

	// /**
	// * 保存裁剪之后的图片数据
	// *
	// * @param picdata
	// */
	// private void getImageToView(final Intent data) {
	// final ProgressDialog dialog = ProgressDialog.show(FeedbackActivity.this,
	// null, "头像上传中...", true);
	// new Thread(new Runnable() {
	// @Override
	// public void run() {
	// try {
	// if (data != null && data.getExtras() != null) {
	// final Bundle extras = data.getExtras();
	// if (extras != null) {
	// picHead = extras.getParcelable("data");
	// srcPath = BitmapUtil.getAbsolutePath(FeedbackActivity.this, picHead);
	// initSignForType();
	// }
	// } else {
	// showInfo("获取图像信息失败");
	// }
	// } catch (Exception e) {
	// showInfo(getString(R.string.A7));
	// } finally {
	// if (dialog != null && dialog.isShowing()) {
	// dialog.dismiss();
	// }
	// initFeedbackImg();
	// }
	// }
	// }).start();
	// }

	protected void initSignForType(final int flag) {
		
		ExecuteTask.execute(new Runnable() {

			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("type", "10");// 3身份证，5签名，6头像
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/querySign", params);
					if (result.getSuccess()) {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							uploadPic(obj.getString("result"), flag);
						} else {
							showInfo(obj.getString("desc"));
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("AccountPictureActivity", "initSignForType()", e);
					LogUmeng.reportError(FeedbackActivity.this, e);
				}
			}
		});

	}

	private void uploadPic(final String sign, final int flag) {
		if (flag == 1) {
			cosPath = cosPath1;
			srcPath = srcPath1;
			sourceURL = sourceURL1;
		} else if (flag == 2) {
			cosPath = cosPath2;
			srcPath = srcPath2;
			sourceURL = sourceURL2;
		} else if (flag == 3) {
			cosPath = cosPath3;
			srcPath = srcPath3;
			sourceURL = sourceURL3;
		} else if (flag == 4) {
			cosPath = cosPath4;
			srcPath = srcPath4;
			sourceURL = sourceURL4;
		} else if (flag == 5) {
			cosPath = cosPath5;
			srcPath = srcPath5;
			sourceURL = sourceURL5;
		} else if (flag == 6) {
			cosPath = cosPath6;
			srcPath = srcPath6;
			sourceURL = sourceURL6;
		}
		PutObjectRequest putObjectRequest = new PutObjectRequest();
		putObjectRequest.setBucket(GlobalPara.getCosName());

		cosPath = "/feedBack/" + "headPic" + "_" + UserUtil.getUserInfo().getUserId() + ".png";
		putObjectRequest.setCosPath(cosPath);
		putObjectRequest.setSrcPath(srcPath);
		putObjectRequest.setSign(sign);
		putObjectRequest.setInsertOnly("1");// 0允许覆盖，1不允许覆盖

		putObjectRequest.setListener(new IUploadTaskListener() {
			@Override
			public void onSuccess(COSRequest cosRequest, COSResult cosResult) {

				PutObjectResult result = (PutObjectResult) cosResult;
				if (result.code == 0) {
					sourceURL = result.source_url;
					PicUtil.deleteTempFile(srcPath);
					upDateUser(flag);
				}
			}

			@Override
			public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
				sourceURL = "";
				showInfo("图片上传失败");
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

	private void upDateUser(int flag) {
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("userJson", makeJsonPost(flag));// userJson
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "user/updateUser",
					params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
				} else {
					showInfo("上传失败，请重试");
				}
			}
		} catch (Exception e) {
			Log.e("UserInfoFragment", "upDateUser", e);
			LogUmeng.reportError(FeedbackActivity.this, e);
		}
	}

	protected String makeJsonPost(int flag) {
		JSONObject json = new JSONObject();
		if (flag == 1) {
			try {
				json.put("txyUserPic", sourceURL1);
				return json.toString();
			} catch (Exception e) {
				Log.e("UserInfoFragment", "makeJsonPost", e);
			}
		} else if (flag == 2) {
			try {
				json.put("txyUserPic", sourceURL2);
				return json.toString();
			} catch (Exception e) {
				Log.e("UserInfoFragment", "makeJsonPost", e);
			}
		} else if (flag == 3) {
			try {
				json.put("txyUserPic", sourceURL3);
				return json.toString();
			} catch (Exception e) {
				Log.e("UserInfoFragment", "makeJsonPost", e);
			}
		} else if (flag == 4) {
			try {
				json.put("txyUserPic", sourceURL4);
				return json.toString();
			} catch (Exception e) {
				Log.e("UserInfoFragment", "makeJsonPost", e);
			}
		} else if (flag == 5) {
			try {
				json.put("txyUserPic", sourceURL5);
				return json.toString();
			} catch (Exception e) {
				Log.e("UserInfoFragment", "makeJsonPost", e);
			}
		} else if (flag == 6) {
			try {
				json.put("txyUserPic", sourceURL6);
				return json.toString();
			} catch (Exception e) {
				Log.e("UserInfoFragment", "makeJsonPost", e);
			}
		}

		return null;
	}

	private String getNameFromUrl(String loadUrl) {
		String[] a = loadUrl.split("/");
		String s = a[a.length - 1];
		return s;
	}

	@Click
	void tvToSubmit() {
		if (etFeedback.toString() != null) {
			toSubmit();
		} else {
			showInfo("请填写反馈内容！");
		}
	}

	private void toSubmit() {
		try {
			String Data = initData();
			if (Data != null) {

				Map<String, String> params = new HashMap<String, String>();
				params.put("feedBackJson", Data);
				params.put("token", UserUtil.getToken());
				TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "user/upFeedBack",
						params);
				if (result.getSuccess()) {
					if (ResultUtil.isOutTime(result.getResult()) != null) {
						showInfo(ResultUtil.isOutTime(result.getResult()));
						Intent intent = new Intent(FeedbackActivity.this, LoginActivity_.class);
						startActivity(intent);
					} else {
						JSONObject obj = new JSONObject(result.getResult());
						if (obj.getInt("code") == 1) {
							JSONObject objRes = obj.getJSONObject("result");
							if (objRes.getInt("returnCode") == 1) {
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
			LogUmeng.reportError(FeedbackActivity.this, e);
		} finally {
		}
	}

	private String initData() {
		JSONObject json = new JSONObject();
		try {
			json.put("comment", etFeedback.toString());
			json.put("pic1", sourceURL1);
			json.put("pic2", sourceURL2);
			json.put("pic3", sourceURL3);
			json.put("pic4", sourceURL4);
			json.put("pic5", sourceURL5);
			json.put("pic6", sourceURL6);

			json.put("token", UserUtil.getToken());
			return json.toString();
		} catch (Exception e) {
			Log.e("VerifiedActivity", "initData", e);
			LogUmeng.reportError(FeedbackActivity.this, e);
		}
		return null;
	}

	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(FeedbackActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// if (picHead != null && !picHead.isRecycled()) {
	// picHead.recycle();
	// picHead = null;
	// }
	// }

}
