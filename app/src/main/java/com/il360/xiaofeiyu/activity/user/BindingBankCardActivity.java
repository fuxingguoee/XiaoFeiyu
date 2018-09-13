package com.il360.xiaofeiyu.activity.user;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
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
import com.il360.xiaofeiyu.model.user.OutUserRz;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.SIMCardUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_binding_bank_card)
public class BindingBankCardActivity extends BaseWidgetActivity{
	
	@ViewById TextView tvAuditDesc;
	@ViewById TextView submit;
	protected ProgressDialog transDialog;

	@ViewById TextView tvtishi;
	@ViewById TextView tvchoice_bank;
	@ViewById TextView tvBankName;
	@ViewById EditText etBankBranchName;
	@ViewById EditText cardNum;
	@ViewById EditText bankPhone;
	@ViewById TextView realName;
	@ViewById TextView bankBranchName;
	public static final int BANK_CODE_SECCESS = 201;// 银行
	public static final int BANK_BRANCH_CODE_SECCESS = 202;// 支行
	
	/** 传递过来的参数 */
	@Extra String bankname;
	@Extra String bankbranchname;
	@Extra String bankbranchno;
	@Extra String bankCode;
	@Extra OutUserBank outUserBank;
	
	@AfterViews
	void init(){
		initviews();
	}
	
	private void initviews() {
		tvtishi.setText(Html.fromHtml("<font color='black'>"+"请绑定您的收款银行卡,作为个人转账银行账户"+"</font>"+"<font color='red'>"+"持卡人必须是本人才有效（需详细选择银行卡的分支行）"+"</font>"));
		tvchoice_bank.setText(Html.fromHtml("<font color='#666666'>"+"选择银行"+"</font>"+"<font color='red'>"+"（建议选择工、农、中、建等银行）"+"</font>"+"<font color='#666666'>"+":"+"</font>"));

		if(GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getName() != null){
			realName.setText(GlobalPara.getOutUserRz().getName());
		} else {
			realName.setText("");
		}
		
		if(outUserBank !=null){
			tvBankName.setText(outUserBank.getBankName() != null ? outUserBank.getBankName() : "");
			bankBranchName.setText(outUserBank.getBranchName() !=null ? outUserBank.getBranchName() : "");
			cardNum.setText(outUserBank.getBankNo() != null ? outUserBank.getBankNo() : "");
			bankPhone.setText(outUserBank.getPhone() != null ? outUserBank.getPhone() : UserUtil.getUserInfo().getLoginName());
			bankbranchno = outUserBank.getBranchNo();
			bankCode = outUserBank.getBankCode();
			if (outUserBank.getStatus() == 2) {
				tvAuditDesc.setText("正在审核中！");
				tvAuditDesc.setTextColor(0xFF000000);
			} else if (outUserBank.getStatus() == -1) {
				tvAuditDesc.setText("审核未通过，请重新提交申请！\n原因：" + outUserBank.getAuditDesc());
				tvAuditDesc.setTextColor(0xFFFF0000);
			} else {
				tvAuditDesc.setVisibility(View.GONE);
			}
		}
	}
	
	@Click
	void rlChoice_Bank(){
		Intent intent = new Intent();
		intent.setClass(this, BankNameListActivity_.class);
		startActivityForResult(intent, 0);
	}
	
	@Click
	void tvSearchBranch() {
		if (ViewUtil.getText(tvBankName).equals("")) {
			showInfo(getResources().getString(R.string.F1));
		} else {
			if (ViewUtil.getText(etBankBranchName).equals("")) {
				showInfo(getResources().getString(R.string.F2));
			} else {
				Intent intent = new Intent();
				intent.setClass(this, BranchBankNameListActivity_.class);
				intent.putExtra("bankname", ViewUtil.getText(tvBankName));
				intent.putExtra("searchcondition", ViewUtil.getText(etBankBranchName));
				startActivityForResult(intent, 0);
			}
		}
	}
	
	@Click
	void submit() {
		if (isOk()) {
			submit.setClickable(false);
			transDialog = ProgressDialog.show(BindingBankCardActivity.this, null, getString(R.string.C16), true);
			ExecuteTask.execute(new Runnable() {

				@Override
				public void run() {
					try {
						initVerified();
					} catch (Exception e) {
						Log.e("VerifiedActivity", "submit()", e);
						LogUmeng.reportError(BindingBankCardActivity.this, e);
						showInfo(getString(R.string.A7));
					}
				}
			});
		}
	}
	
	private void initVerified() {
		String jsonStr = makeJsonPost();
		if (jsonStr != null) {
			try {
				Map<String, String> params = new HashMap<String, String>();
				params.put("userBankJson", jsonStr);
				params.put("token", UserUtil.getToken());
				TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL, "card/bindingBank",
						params);
				if (result.getSuccess()) {
					JSONObject obj = new JSONObject(result.getResult());
					showInfo(obj.getString("desc"));
					if (obj.getInt("code") == 1) {
						initUserRz();
					}
				} else {
					showInfo(getString(R.string.A6));
				}
			} catch (JSONException e) {
				Log.e("BindingBankCardActivity", "initVerified()", e);
				LogUmeng.reportError(BindingBankCardActivity.this, e);
			} finally {
				runOnUiThread(new Runnable() {
					public void run() {
						if (transDialog != null && transDialog.isShowing()) {
							transDialog.dismiss();
						}
						submit.setClickable(true);
					}
				});
			}
		} else {
			showInfo("提交的数据有误！");
			submit.setClickable(true);
		}
	}
	
	private void initUserRz() {
		GlobalPara.outUserRz = null;
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("token", UserUtil.getToken());
			TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL, "card/queryUserInfo", params);
			if (result.getSuccess()) {
				JSONObject obj = new JSONObject(result.getResult());
				if (obj.getInt("code") == 1) {
					JSONObject objRes = obj.getJSONObject("result");
					JSONObject objRetRes = objRes.getJSONObject("returnResult");
					GlobalPara.outUserRz = FastJsonUtils.getSingleBean(objRetRes.toString(), OutUserRz.class);
				}
			}
		} catch (Exception e) {
		} finally {
			setResult(UserFragment.CODE_SECCESS);
			BindingBankCardActivity.this.finish();
		}
	}
	
	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(BindingBankCardActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private boolean isOk() {
		if (ViewUtil.getText(tvBankName).equals("")) {
			showInfo(getResources().getString(R.string.F1));
		} else if (ViewUtil.getText(bankBranchName).equals("")) {
			showInfo(getResources().getString(R.string.F10));
		} else if (ViewUtil.getText(realName).equals("")) {
			showInfo(getResources().getString(R.string.F11));
		} else if (ViewUtil.getText(bankPhone).equals("")) {
			showInfo("银行预留手机号码为空，无法绑定银行卡");
		} else if (!SIMCardUtil.isMobileNo(ViewUtil.getText(bankPhone))) {
			showInfo(getResources().getString(R.string.B2));
		} else if (ViewUtil.getText(cardNum).length() < 16) {
			showInfo(getResources().getString(R.string.F3));
		} else if (ViewUtil.getText(cardNum).startsWith("6011")) {
			showInfo("暂不支持此卡");
		} else {
			return true;
		}
		return false;
	}
	
//	private void showDialog() {
//		CustomDialog.Builder builder = new CustomDialog.Builder(BindingBankCardActivity.this);
//		builder.setTitle(R.string.app_name);
//		builder.setMessage(R.string.C25);
//		builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//			}
//		});
//		builder.create().show();
//	}
	
	private String makeJsonPost() {
		OutUserBank out = new OutUserBank();
		out.setCardType(1);
		out.setStatus(2);
		out.setCardName(ViewUtil.getText(realName));
		out.setBankNo(ViewUtil.getText(cardNum));
		out.setBankName(ViewUtil.getText(tvBankName));
		out.setBranchName(ViewUtil.getText(bankBranchName));
		out.setBranchNo(bankbranchno);
		out.setBankCode(bankCode);
		out.setPhone(ViewUtil.getText(bankPhone));
		return FastJsonUtils.getJsonString(out);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == BindingBankCardActivity.BANK_CODE_SECCESS) {
			Bundle b = data.getExtras();
			if (bankname != null && !bankname.equals(b.getString("bankname"))) {
				bankname = b.getString("bankname");
				tvBankName.setText(bankname);
				bankBranchName.setText("");
				bankbranchno = null;
				bankCode =  null;
			} else {
				bankname = b.getString("bankname");
				tvBankName.setText(bankname);
			}
		} else if (resultCode == BindingBankCardActivity.BANK_BRANCH_CODE_SECCESS) {
			Bundle b = data.getExtras();
			bankbranchname = b.getString("bankbranchname");
			bankbranchno = b.getString("bankbranchno");
			bankCode =  b.getString("bankCode");
			bankBranchName.setText(bankbranchname);
			if (etBankBranchName.isFocused()) {
				etBankBranchName.clearFocus();
				cardNum.requestFocus();
			}
		}
	}
}
