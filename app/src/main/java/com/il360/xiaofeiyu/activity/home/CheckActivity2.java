package com.il360.xiaofeiyu.activity.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;
import com.il360.xiaofeiyu.activity.user.LoginActivity_;
import com.il360.xiaofeiyu.adapter.CheckListAdapter;
import com.il360.xiaofeiyu.common.ExecuteTask;
import com.il360.xiaofeiyu.common.MyApplication;
import com.il360.xiaofeiyu.connection.HttpRequestUtil;
import com.il360.xiaofeiyu.connection.NetWork;
import com.il360.xiaofeiyu.connection.TResult;
import com.il360.xiaofeiyu.connection.UrlEnum;
import com.il360.xiaofeiyu.model.order.Order;
import com.il360.xiaofeiyu.model.order.ResultOfOrder;
import com.il360.xiaofeiyu.util.FastJsonUtils;
import com.il360.xiaofeiyu.util.ResultUtil;
import com.il360.xiaofeiyu.util.StringUtil;
import com.il360.xiaofeiyu.util.SystemUtil;
import com.il360.xiaofeiyu.util.UserUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


@EActivity(R.layout.act_check2)
public class CheckActivity2 extends BaseWidgetActivity{

	@ViewById
	TextView tvStep1,tvStep2,tvStep3;
	
	@ViewById
	ListView assessList1, assessList2, assessList3;
	
	@ViewById 
	RelativeLayout rlOne, rlTwo, rlThree;
	private List<String> checkList1 = new ArrayList<String>();
	private List<String> checkList2 = new ArrayList<String>();
	private List<String> checkList3 = new ArrayList<String>();
	private CheckListAdapter myAdapter;
	
	private List<String> checkContents = new ArrayList<String>();
	private List<Integer> checkNums = new ArrayList<Integer>();
	
	private ResultOfOrder resultOfOrder;//返回评估信息
	private Order order;
	private String brandModel, memory, ramMemory, totalMemory;
	
	private int myPosition = 0;
	private int flag = 0;
	
	public static LocationClient mLocationClient;
	public static MyLocationListener mMyLocationListener;
	private int count = 0;
	private String myProvince;
	private String myCity;
	private String myDistrict;
    private String myLocation = "";
    private String myLongitude;//经度
    private String myLatitude;//纬度
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		CheckActivity2.mLocationClient = ((MyApplication) getApplication()).mLocationClient;
		CheckActivity2.mMyLocationListener = new MyLocationListener();
		CheckActivity2.mLocationClient.registerLocationListener(CheckActivity2.mMyLocationListener);
	}
    
    /**
     * 实现实时位置回调监听
     */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			if (location != null) {
				count++;
				if (count == 1) {
					if (location.getLocType() == BDLocation.TypeNetWorkLocation && location.getAddress() != null
							&& !TextUtils.isEmpty(location.getAddress().city)) {

						String PROVINE = location.getAddress().province == null ? "" : location.getAddress().province;
						String CITY = location.getAddress().city == null ? "" : location.getAddress().city;
						String DISTRICT = location.getAddress().district == null ? "" : location.getAddress().district;
						String STREET = location.getAddress().street == null ? "" : location.getAddress().street;
						String STREETNUMBER = location.getAddress().streetNumber == null ? ""
								: location.getAddress().streetNumber;
						String address = STREET + STREETNUMBER;
						myLongitude = location.getLongitude() + "";
						myLatitude = location.getLatitude() + "";
						myLocation = StringUtil.getStringSub(address);
						myProvince = StringUtil.getStringSub(PROVINE);
						myCity = StringUtil.getStringSub(CITY);
						myDistrict = StringUtil.getStringSub(DISTRICT);

//						showInfo(myProvince + myCity + myDistrict + myLocation);
						return;
					} else if (location.getLocType() == 61) {
						showInfo(getResources().getString(R.string.location_failure) + "建议关闭GPS采用网络定位");
					} else {
						showInfo(getResources().getString(R.string.location_failure) + "(" + location.getLocType()
						+ ")");
					}
				}
			} else {
				showInfo(getResources().getString(R.string.location_failure));
			}
		}
	}
    
	private void GPS() {
		if (!NetWork.checkNetWorkStatus(this)) {
			showInfo(getResources().getString(R.string.check_network));
		} else {
			count = 0;
			mLocationClient.start();// 定位SDK start之后会默认发起一次定位请求
			mLocationClient.requestLocation();
		}
	}
	
	@AfterViews
	void init() {
		checkNums.clear();
		GPS();
		initCheck();
		checkList1.add("充电正常");
		checkList1.add("充电无反应/接触不良");
		checkList2.add("正常使用痕迹");
		checkList2.add("破损/掉漆/弯曲变形");
		checkList3.add("无拆无修");
		checkList3.add("屏幕维修");
		checkList3.add("主板维修/多处维修");
		showList(checkList1);
	}
	
	private void initCheck() {
		brandModel = SystemUtil.getSystemModel();
		memory = SystemUtil.getInternalMemorySize(getBaseContext());
		Pattern p = Pattern.compile("[^0-9.]");   
		Matcher m = p.matcher(memory);
		memory = m.replaceAll("".trim());
		if ( Double.parseDouble(memory) < 8){
			memory = "8GB";
		} else if(Double.parseDouble(memory) > 8 && Double.parseDouble(memory) < 16){
			memory = "16GB";
		} else if(Double.parseDouble(memory) > 16 && Double.parseDouble(memory) < 32){
			memory = "32GB";
		} else if(Double.parseDouble(memory) > 32 && Double.parseDouble(memory) < 64){
			memory = "64GB";
		} else if(Double.parseDouble(memory) > 32 && Double.parseDouble(memory) < 128){
			memory = "128GB";
		} else if(Double.parseDouble(memory) > 128 && Double.parseDouble(memory) < 256){
			memory = "256GB";
		}
		ramMemory = SystemUtil.getTotalRam(getBaseContext());
		totalMemory = ramMemory + "+" + memory;
	}
	
	private void showList(List<String> list1) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myPosition = 0;
		myAdapter = new CheckListAdapter(list1, CheckActivity2.this, myPosition);
		assessList1.setAdapter(myAdapter);
		assessList1.setOnItemClickListener(new myListClickListener());
		myAdapter.notifyDataSetChanged();
	}
	
	class myListClickListener implements android.widget.AdapterView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if(flag == 0){
				myPosition = position;
				myAdapter = new CheckListAdapter(checkList1, CheckActivity2.this, myPosition);
				assessList1.setAdapter(myAdapter);
				myAdapter.notifyDataSetChanged();
				tvStep2.setBackgroundResource(R.drawable.bg_check_step);
				rlTwo.setBackgroundResource(R.drawable.dotted_line);
				flag = 1 ;
				checkContents.add(checkList1.get(position).toString());
				checkNums.add(position + 1);
				showList(checkList2);
			} else if(flag == 1){
				myPosition = position;
				myAdapter = new CheckListAdapter(checkList2, CheckActivity2.this, myPosition);
				assessList1.setAdapter(myAdapter);
				myAdapter.notifyDataSetChanged();
				tvStep3.setBackgroundResource(R.drawable.bg_check_step);
				rlThree.setBackgroundResource(R.drawable.dotted_line);
				flag = 2;
				checkContents.add(checkList2.get(position).toString());
				checkNums.add(position + 1);
				showList(checkList3);
			} else if(flag == 2){
				myPosition = position;
				myAdapter = new CheckListAdapter(checkList3, CheckActivity2.this, myPosition);
				assessList1.setAdapter(myAdapter);
				myAdapter.notifyDataSetChanged();
				checkContents.add(checkList3.get(position).toString());
				checkNums.add(position + 1);
				if(myProvince == null || myCity == null || myDistrict == null || myLocation == null){
					showInfo(getResources().getString(R.string.check_network));
					CheckActivity2.this.finish();
				} else {
					updateTheAmout();
				}
			}
		}
	}

	private void goToResult() {
		Intent intent = new Intent(CheckActivity2.this, CheckResultActivity_.class);
		startActivity(intent);
	}
	
	private void updateTheAmout() {
		ExecuteTask.execute(new Runnable() {
			@Override
			public void run() {
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("orderJson", makeOrderJson());
					params.put("goodsSys", brandModel);
					params.put("goodsType", totalMemory);
					params.put("token", UserUtil.getToken());
					TResult<Boolean, String> result = HttpRequestUtil.sendPostRequest(UrlEnum.BIZ_URL,
							"valuation/postValuation", params);
					if (result.getSuccess()) {
						if (ResultUtil.isOutTime(result.getResult()) != null) {
							showInfo(ResultUtil.isOutTime(result.getResult()));
							Intent intent = new Intent(CheckActivity2.this, LoginActivity_.class);
							startActivity(intent);
						} else{
							JSONObject obj = new JSONObject(result.getResult());
							if (obj.getInt("code") == 1) {
								resultOfOrder = FastJsonUtils.getSingleBean(obj.toString(),ResultOfOrder.class);
								order = resultOfOrder.getResult();
								order.setCity(myCity);
								UserUtil.setValuationInfo(order);
								goToResult();
							} else {
								showInfo(obj.getString("desc"));
							}
						}
					} else {
						showInfo(getString(R.string.A6));
					}
				} catch (Exception e) {
					showInfo(getString(R.string.A2));
				}
			}
		});
	}
	
	private String makeOrderJson() {
		Order order = new Order();
		if (SystemUtil.getOperators(getBaseContext()) != null){
			order.setOperator(SystemUtil.getOperators(getBaseContext()));
		} else {
			order.setOperator("未插卡");
		}
		order.setPosition(myProvince + myCity + myDistrict + myLocation);
		order.setCharge(checkNums.get(0));
		order.setAppearance(checkNums.get(1));
		order.setRepair(checkNums.get(2));
		return FastJsonUtils.getJsonString(order);
	}

	private void showInfo(final String info) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(CheckActivity2.this, info, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
 }
