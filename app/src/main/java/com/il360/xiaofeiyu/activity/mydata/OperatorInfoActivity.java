package com.il360.xiaofeiyu.activity.mydata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.home.PhoneAuthenActivity_;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.home.Contact;
import com.il360.xiaofeiyu.model.home.OutContact;
import com.il360.xiaofeiyu.model.user.OutUserRz;
import com.il360.xiaofeiyu.util.ContactUtil;
import com.il360.xiaofeiyu.util.EmojiFilterUtil;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.SIMCardUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.util.ViewUtil;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_operator_info)
public class OperatorInfoActivity extends BaseWidgetActivity {
	
	@ViewById RelativeLayout rlMailList;
	@ViewById LinearLayout llShowOperator;
	@ViewById TextView tvPhone;
	@ViewById EditText etPhonePwd;
	@ViewById Button btnSubmit;
	
	/**上传亲属手机号*/
	@ViewById LinearLayout llUpPhoneNum;
	@ViewById RelativeLayout rlTopContacts;
	@ViewById TextView tvTopContacts;
	private String topConteactsName;
	private String topConteactsNunber;
	
	private static List<Contact> contactList = new ArrayList<Contact>();
	
	protected ProgressDialog transDialog;
	
	@AfterViews
	void init() {
		initViews();
	}
	
	private void initViews() {
		if (GlobalPara.getOutUserRz() != null && GlobalPara.getOutUserRz().getContactCount() != null 
				&& GlobalPara.getOutUserRz().getContactCount() == 1) {
			rlMailList.setVisibility(View.GONE);
			llShowOperator.setVisibility(View.VISIBLE);
			tvPhone.setText(UserUtil.getUserInfo().getLoginName());
			etPhonePwd.setText("");
		} else {
			contactList = ContactUtil.getContactList(OperatorInfoActivity.this);
			llShowOperator.setVisibility(View.GONE);
			rlMailList.setVisibility(View.VISIBLE);
		}
		
		if(GlobalPara.getOutUserRz().getRelationCount() != null && GlobalPara.getOutUserRz().getRelationCount() == 2){
			llUpPhoneNum.setVisibility(View.VISIBLE);
		} else {
			llUpPhoneNum.setVisibility(View.GONE);
		}
	}

	@Click
	void rlMailList() {
		if (contactList != null && contactList.size() > 0) {

			rlMailList.setClickable(false);
			transDialog = ProgressDialog.show(OperatorInfoActivity.this, null, "上传通讯录中...", true);
			ExecuteTask.execute(new Runnable() {
				@Override
				public void run() {
					try {
						Map<String, String> params = new HashMap<String, String>();
						params.put("token", UserUtil.getToken());
						params.put("userContactJson", makeJsonPost());
						TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
								"usercredit/postUserContact", params);
						if (result.getSuccess()) {
							if (ResultUtil.isOutTime(result.getResult()) != null) {
								showInfo(ResultUtil.isOutTime(result.getResult()));
								Intent intent = new Intent(OperatorInfoActivity.this, LoginActivity_.class);
								startActivity(intent);
							} else {
								JSONObject obj = new JSONObject(result.getResult());
								if (obj.getInt("code") == 1) {
									runOnUiThread(new Runnable() {
										public void run() {
											if(GlobalPara.getOutUserRz() != null){
												GlobalPara.getOutUserRz().setContactCount(1);
											}
											initViews();
										}
									});
								}
								showInfo(obj.getString("desc"));
							}
						}
					} catch (Exception e) {
						showInfo(getString(R.string.A2));
						Log.e("OperatorInfoActivity", "rlMailList", e);
						LogUmeng.reportError(OperatorInfoActivity.this, e);
					} finally {
						runOnUiThread(new Runnable() {
							public void run() {
								if (transDialog != null && transDialog.isShowing()) {
									transDialog.dismiss();
								}
								rlMailList.setClickable(true);
							}
						});
					}
				}
			});

		} else {
			showInfo("请确保通讯录不为空");
		}
	}
	
	
	protected void initUpPhone() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					String data = makeJsonPost2();
					if (data != null) {
						Map<String, String> params = new HashMap<String, String>();
						params.put("userRelationJson", data);
						params.put("token", UserUtil.getToken());
						TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
								"usercredit/postUserRelation", params);
						if (result.getSuccess()) {
							if (ResultUtil.isOutTime(result.getResult()) != null) {
								showInfo(ResultUtil.isOutTime(result.getResult()));
								Intent intent = new Intent(OperatorInfoActivity.this, LoginActivity_.class);
								startActivity(intent);
							} else {
								JSONObject obj = new JSONObject(result.getResult());
								if (obj.getInt("code") == 1) {

									runOnUiThread(new Runnable() {
										public void run() {
											if (GlobalPara.getOutUserRz() != null) {
												GlobalPara.getOutUserRz().setRelationCount(1);
											}
											goToPhoneAuthen();
										}
									});
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
				} catch (JSONException e) {
					Log.e("OperatorInfoActivity", "initUpPhone()", e);
					LogUmeng.reportError(OperatorInfoActivity.this, e);
					showInfo(getString(R.string.A2));
				}
			}
		});
	}
	
	private String makeJsonPost() {
		OutContact outContact = new OutContact();
		outContact.setList(contactList);
		return FastJsonUtils.getJsonString(outContact);
	}
	
	private String makeJsonPost2() {
		JSONObject json = new JSONObject();
		try {
			json.put("userId", UserUtil.getUserInfo().getUserId());
			json.put("userName", GlobalPara.getOutUserRz().getName());
			json.put("name", topConteactsName);
			json.put("phone", topConteactsNunber);
			return json.toString();
		} catch (Exception e) {
			Log.e("OperatorInfoActivity", "makeJsonPost2", e);
			LogUmeng.reportError(OperatorInfoActivity.this, e);
		}
		return null;
	}
	
	@Click
	void rlTopContacts(){
		Intent intent=new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(intent,1000);
	}
	
	@Click
	void btnSubmit(){
		if(isOk()){
			if(GlobalPara.getOutUserRz().getRelationCount() != null && GlobalPara.getOutUserRz().getRelationCount() == 2){
				initUpPhone();
			} else {
				goToPhoneAuthen();
			}
		}
	}
	
	private void goToPhoneAuthen() {
		Intent intent = new Intent(OperatorInfoActivity.this,PhoneAuthenActivity_.class);
		intent.putExtra("serviceNumber", ViewUtil.getText(etPhonePwd));
		intent.putExtra("forceRemove", "1");
		startActivityForResult(intent, 0);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1000 && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				String[] contact = getPhoneContacts(uri);
				if (contact != null) {
					topConteactsName = contact[0];// 姓名
					topConteactsNunber = formatPhoneNum(contact[1]);// 手机号
					tvTopContacts.setText(topConteactsName + ": " + topConteactsNunber );
				}
			}
		} else if (resultCode == 101) {
			initOwnInfo();
		} else if (resultCode == 102) {
			OperatorInfoActivity.this.finish();
		}
	}
	
	private void initOwnInfo() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"card/queryUserInfo", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(OperatorInfoActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							GlobalPara.outUserRz = null;
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								JSONObject objRes = obj.getJSONObject("result");
								JSONObject objRetRes = objRes.getJSONObject("returnResult");
								GlobalPara.outUserRz = FastJsonUtils.getSingleBean(objRetRes.toString(), OutUserRz.class);
							}
						}
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
					Log.e("OperatorInfoActivity", "initOwnInfo", e);
					LogUmeng.reportError(OperatorInfoActivity.this, e);
				}
			}
		});
	}
	
	private boolean isOk() {
		if (GlobalPara.getOutUserRz().getRelationCount() != null && GlobalPara.getOutUserRz().getRelationCount() == 2
				&& TextUtils.isEmpty(topConteactsNunber)) {
			showInfo("请选择常用联系人");
		} else if(GlobalPara.getOutUserRz().getRelationCount() != null && GlobalPara.getOutUserRz().getRelationCount() == 2
				&& topConteactsNunber.equals(UserUtil.getUserInfo().getLoginName())){
			showInfo("常用联系人手机号码不能为本人的手机号码");
		}  else if(GlobalPara.getOutUserRz().getRelationCount() != null && GlobalPara.getOutUserRz().getRelationCount() == 2
				&& EmojiFilterUtil.containsEmoji(topConteactsName)){
			showInfo("常用联系人姓名包含特殊字符，请修改或换一个联系人");
		} else if (GlobalPara.getOutUserRz().getRelationCount() != null && GlobalPara.getOutUserRz().getRelationCount() == 2
				&& !SIMCardUtil.isMobileNo(topConteactsNunber)) {
			showInfo("选择的常用联系人手机号码格式有误");
		} else if (TextUtils.isEmpty(ViewUtil.getText(etPhonePwd))) {
			showInfo("请输入认证手机号码的服务密码");
		} else {
			return true;
		}
		return false;
	}
	
	
	/**
     * 读取联系人信息
     * @param uri
     */
	
	private String[] getPhoneContacts(Uri uri) {
		String[] contact = new String[2];
		// 得到ContentResolver对象
		ContentResolver cr = getContentResolver();
		Cursor cursor = cr.query(uri, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			// 取得联系人姓名
			int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			contact[0] = cursor.getString(nameFieldColumnIndex);
			contact[1] = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			Log.i("contacts", contact[0]);
			Log.i("contactsUsername", contact[1]);
			cursor.close();
		} else {
			return null;
		}
		return contact;
	}
	
	private String formatPhoneNum(String phoneNum) {
		String regex = "(\\+86)|(600)|[^0-9]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNum);
		return matcher.replaceAll("");
	}

	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (transDialog != null && transDialog.isShowing()) {
					transDialog.dismiss();
				}
				Toast.makeText(OperatorInfoActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}

}
