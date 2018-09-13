package com.il360.xiaofeiyu.activity.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.fragment.UserFragment;
import com.il360.xiaofeiyu.model.hua.OutUserBank;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_my_bank_card)
public class MyBankCardActivity extends BaseWidgetActivity {
	
	@ViewById TextView tvTextClick;
	
	@ViewById TextView tvStatus;
	@ViewById TextView tvBankName;
	@ViewById TextView tvBankNo;
	@ViewById TextView tvUserName;
	@ViewById TextView tvBankPhone;
	
//	@ViewById TextView tvSubmit;

	private OutUserBank response;

	@AfterViews
	void init() {
		if(GlobalPara.getOutUserRz().getBankRz() == 0){
			initViews();
			tvTextClick();
		} else {
			initData();
		}
	}

	private void initData() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/queryCard",params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(MyBankCardActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								JSONObject objRes = obj.getJSONObject("result");
								response = FastJsonUtils.getSingleBean(objRes.toString(), OutUserBank.class);
								GlobalPara.outUserBank = response;
							} else {
								GlobalPara.outUserBank = null;
							}
						}
					} else {
						showInfo(result.getResult());
					}
				} catch (Exception e) {
					Log.e("MyBankCardActivity", "initData", e);
					LogUmeng.reportError(MyBankCardActivity.this, e);
				} finally {
					runOnUiThread(new Runnable() {
						public void run() {
							initViews();
						}
					});
				}
			}
		});
	}
	
	private void initViews() {
		if (GlobalPara.getOutUserBank() != null) {

			if (GlobalPara.getOutUserBank().getStatus() != null) {
				if (GlobalPara.getOutUserBank().getStatus() == 0) {
					tvStatus.setText("新建");
					tvTextClick.setVisibility(View.VISIBLE);
				} else if (GlobalPara.getOutUserBank().getStatus() == 1) {
					tvStatus.setText("通过");
					tvTextClick.setVisibility(View.VISIBLE);
				} else if (GlobalPara.getOutUserBank().getStatus() == 2) {
					tvStatus.setText("待审核");
					tvTextClick.setVisibility(View.GONE);
				} else if (GlobalPara.getOutUserBank().getStatus() == -1) {
					tvStatus.setText("不通过");
					tvTextClick.setVisibility(View.VISIBLE);
				}
			} else {
				tvStatus.setText("未知");
				tvTextClick.setVisibility(View.VISIBLE);
			}

			tvBankNo.setText(GlobalPara.getOutUserBank().getBankNo() != null ? GlobalPara.getOutUserBank().getBankNo() : "");
			tvBankPhone.setText(GlobalPara.getOutUserBank().getPhone() != null ? GlobalPara.getOutUserBank().getPhone() : "");
			tvBankName.setText(GlobalPara.getOutUserBank().getBankName() != null ? GlobalPara.getOutUserBank().getBankName() : "");
			tvUserName.setText(GlobalPara.getOutUserBank().getCardName() != null ? GlobalPara.getOutUserBank().getCardName() : "");
			tvTextClick.setText("修改");
//			tvSubmit.setVisibility(View.VISIBLE);
		} else {
			tvStatus.setText("");
			tvBankNo.setText("");
			tvBankPhone.setText("");
			tvBankName.setText("");
			tvUserName.setText("");
			tvTextClick.setText("添加");
			tvTextClick.setVisibility(View.VISIBLE);
//			tvSubmit.setVisibility(View.INVISIBLE);
		}
	}
	
	@Click
	void tvTextClick() {
		Intent intent = new Intent(MyBankCardActivity.this, BindingBankCardActivity_.class);
		if (GlobalPara.getOutUserBank() != null) {
			intent.putExtra("outUserBank", GlobalPara.getOutUserBank());
		}
		startActivityForResult(intent, 0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == UserFragment.CODE_SECCESS) {
			initData();
		}
	}
	
//	@Click
//	void tvSubmit(){
//		tvSubmit.setClickable(false);
//		unBindingData();
//	}
	
//	private void showDialog() {
//		CustomDialog.Builder builder = new CustomDialog.Builder(MyBankCardActivity.this);
//		builder.setTitle(R.string.app_name);
//		builder.setMessage("是否解绑该银行卡？");
//		builder.setPositiveButton("解绑", new android.content.DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				unBindingData();
//				dialog.dismiss();
//			}
//		});
//		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
	
//	protected void unBindingData() {
//		ExecuteTask.execute(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Map<String, String> params = new HashMap<String, String>();
//					params.put("token", UserUtil.getToken());
//					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "card/unbindingBank",params);
//					if (!result.getSuccess()) {
//						showInfo(result.getResult());
//						return;
//					}
//					JSONObject obj = new JSONObject(result.getResult());
//					if (obj.getInt("code") == 1) {
//						showInfo("解绑成功");
//						initData();
//					} else {
//						showInfo(obj.getString("desc"));
//					}
//				} catch (Exception e) {
//					Log.e("MyBankCardActivity", "unBindingData", e);
//					LogUmeng.reportError(MyBankCardActivity.this, e);
//				}finally {
//					tvSubmit.setClickable(true);
//				}
//			}
//		});
//	}


	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(MyBankCardActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
