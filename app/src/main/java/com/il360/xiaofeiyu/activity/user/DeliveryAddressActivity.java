package com.il360.xiaofeiyu.activity.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.Extra;
import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.main.MainActivity_;
import com.il360.xiaofeiyu.adapter.UserAddressAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.common.LogUmeng;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.address.ArrayOfUserAddress;
import com.il360.xiaofeiyu.model.address.UserAddress;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.UserUtil;
import com.il360.xiaofeiyu.view.CustomDialog;
import com.il360.xiaofeiyu.view.ListViewForScrollView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

@EActivity(R.layout.act_delivery_address)
public class DeliveryAddressActivity extends BaseWidgetActivity {

	@ViewById
	ListViewForScrollView addressList;
	@ViewById
	TextView tvAdd;

	private List<UserAddress> list = new ArrayList<UserAddress>();
	private UserAddressAdapter adapter;
	
	@Extra Integer code;

	@AfterViews
	void init() {
		initData();
	}

	private void initData() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
							"user/queryUserAddress", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(DeliveryAddressActivity.this, LoginActivity_.class);
							startActivity(intent);
						} else {
							GlobalPara.userAddressList = null;
							list.clear();
							ArrayOfUserAddress arrayOfUserAddress = FastJsonUtils.getSingleBean(result.getResult(), ArrayOfUserAddress.class);
							if (arrayOfUserAddress.getCode() == 1) {
								if(arrayOfUserAddress.getResult() != null && arrayOfUserAddress.getResult().size() > 0){
									GlobalPara.userAddressList = arrayOfUserAddress.getResult();
									list.addAll(arrayOfUserAddress.getResult());
								} else {
									showInfo("暂无数据");
								}
							} else {
								showInfo(arrayOfUserAddress.getDesc());
							}
						}
					}
				} catch (Exception e) {
					Log.e("DeliveryAddressActivity", "initData", e);
					LogUmeng.reportError(DeliveryAddressActivity.this, e);
				}finally {
					runOnUiThread(new Runnable() {
						public void run() {
							adapter = new UserAddressAdapter(list, DeliveryAddressActivity.this);
							addressList.setAdapter(adapter);
							addressList.setOnItemClickListener(new OnItemClickListener());//单击
						}
					});
				}
			}
		});
	}
	
	class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(code != null && code == 1001) {
				Intent intent = new Intent();
				Bundle b = new Bundle();
				b.putString("addressId", list.get(position).getAddressId());
				b.putString("address", (list.get(position).getProvince() != null ? list.get(position).getProvince() : "")
						+ (list.get(position).getCity() != null ? list.get(position).getCity() : "")
						+ (list.get(position).getArea() != null ? list.get(position).getArea() : "")
						+ list.get(position).getAddress());
				b.putString("phone", list.get(position).getPhone());
				b.putString("name", list.get(position).getName());
				intent.putExtras(b);
				setResult(1001, intent);
				DeliveryAddressActivity.this.finish();
			} else {
				Intent intent = new Intent(DeliveryAddressActivity.this, AddressModifyActivity_.class);
				intent.putExtra("userAddress", list.get(position));
				startActivityForResult(intent, 0);
			}
		}		
	}

	@Click
	void tvAdd() {
		if(UserUtil.judgeAuthentication()){
			Intent intent = new Intent(DeliveryAddressActivity.this, AddressModifyActivity_.class);
			startActivityForResult(intent, 0);
		} else {
			showDialog2();
		}
	}
	
	
	private void showDialog2() {
		runOnUiThread(new Runnable() {
			public void run() {
				CustomDialog.Builder builder = new CustomDialog.Builder(DeliveryAddressActivity.this);
				builder.setTitle(R.string.app_name);
				builder.setMessage("请先申请通过信用评估！");
				builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(DeliveryAddressActivity.this, MainActivity_.class);
						intent.putExtra("mShowTabIndex", 2);
						startActivity(intent);
						dialog.dismiss();
					}
				});
				builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
			}
		});
	}
	
	private void showInfo(final String info) {

		runOnUiThread(new Runnable() {
			public void run() {
//				if (transDialog != null && transDialog.isShowing()) {
//					transDialog.dismiss();
//				}
				Toast.makeText(DeliveryAddressActivity.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == 101){
			initData();
		}
	}
}
