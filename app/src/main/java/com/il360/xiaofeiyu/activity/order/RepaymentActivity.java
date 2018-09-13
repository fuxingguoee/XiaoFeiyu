package com.il360.xiaofeiyu.activity.order;

import org.androidannotations.annotations.EActivity;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.BaseWidgetActivity;

@EActivity(R.layout.act_repayment)
public class RepaymentActivity extends BaseWidgetActivity {
//
//	@ViewById PullToRefreshScrollView pull_refresh_scrollview;
//	
//	@ViewById TextView tvStayStill;
//	@ViewById TextView tvAlreadyRepaid;
//	@ViewById TextView tvLine1;
//	@ViewById TextView tvLine2;
//	
//	@ViewById ListViewForScrollView myList;
//	
//	private List<Order> myOrder = new ArrayList<Order>();
//	private RepayAdapter myAdapter;
//	
//	private int flag;
//	/** 当前页码 **/
//	private int pageNo = 1;
//	/** 默认每页加载个数 **/
//	private int pageSize = 20;
//	
//	private ArrayOfOrder arrayOfOrder;
//	
//	protected ProgressDialog transDialog;
//	
//	@AfterViews
//	void init(){
//		initViews();
//		initRefreshListener();
//		tvStayStill();
//	}
//
//
//	private void initViews() {
////		pull_refresh_scrollview.setMode(Mode.BOTH);
//		
//	}
//	
//	
//	private void initRefreshListener() {
//
//		pull_refresh_scrollview.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				myList.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						pageNo = 1;
//						showList();
//					}
//				}, 500);
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
//				if (isLast()) {
//					myList.postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							pull_refresh_scrollview.onRefreshComplete();
//							showInfo("已经到底部了！");
//						}
//					}, 1000);
//				} else {
//					pageNo++;
//					showList();
//				}
//			}
//		});
//	}
//	
//	protected boolean isLast() {
//		if (arrayOfOrder != null && pageNo * pageSize < arrayOfOrder.getTotalCount()) {
//			return false;
//		} else {
//			return true;
//		}
//	}
//
//
//	@Click
//	void tvStayStill(){
//		tvStayStill.setTextColor(ContextCompat.getColor(RepaymentActivity.this, R.color.main_logo));
//		tvAlreadyRepaid.setTextColor(ContextCompat.getColor(RepaymentActivity.this, R.color.black));
//		tvLine1.setBackgroundResource(R.color.main_logo);
//		tvLine2.setBackgroundResource(R.color.line_d);
//		flag = 0;
//		myOrder.clear();
//		showList();
//	}
//	
//
//	@Click
//	void tvAlreadyRepaid(){
//		tvStayStill.setTextColor(ContextCompat.getColor(RepaymentActivity.this, R.color.black));
//		tvAlreadyRepaid.setTextColor(ContextCompat.getColor(RepaymentActivity.this, R.color.main_logo));
//		tvLine1.setBackgroundResource(R.color.line_d);
//		tvLine2.setBackgroundResource(R.color.main_logo);
//		flag = 1;
//		myOrder.clear();
//		showList();
//	}
//	
//	
//	private void showList() {
//		if (flag == 0 || flag == 1) {
//			pull_refresh_scrollview.setMode(Mode.BOTH);
//			myAdapter = new RepayAdapter(myOrder, RepaymentActivity.this);
//			myList.setAdapter(myAdapter);
//			myList.setOnItemClickListener(new OnItemClickListener());
//			myAdapter.notifyDataSetChanged();
//			initMyList();
//		} else {
//			pull_refresh_scrollview.setMode(Mode.PULL_FROM_START);
//			pull_refresh_scrollview.onRefreshComplete();
//		}
//	}
//	
//	class OnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
//
//		@Override
//		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			if(myOrder.get(position).getPayStatus() == 1){
//				Intent intent = new Intent(RepaymentActivity.this,PayBackActivity_.class);
//				intent.putExtra("status", "1");
//				intent.putExtra("orderNo", myOrder.get(position).getOrderNo());
//				startActivity(intent);
//			} else if(myOrder.get(position).getPayStatus() == 0){
//				Intent intent = new Intent(RepaymentActivity.this,NotPayBackActivity_.class);
//				intent.putExtra("status", "0");
//				intent.putExtra("orderNo", myOrder.get(position).getOrderNo());
//				startActivity(intent);
//			}
//			
//		}
//	}
//
//
//	private void initMyList() {
//		transDialog = ProgressDialog.show(RepaymentActivity.this, null, "加载中...", true);
//		ExecuteTask.execute(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Map<String, String> params = new HashMap<String, String>();
//					params.put("pageNo", pageNo + "");
//					params.put("pageSize", pageSize + "");
//					params.put("token", UserUtil.getToken());
//					params.put("payStatus", flag + "");
//					params.put("lab", "2");//lab 1代表全额2代表分期
//					TResult<Boolean, String> result = HttpRequestUtil.sendGetRequest(UrlEnum.BIZ_URL,
//							"order/queryOrderList", params);
//					if (result.getSuccess()) {
//						JSONObject obj = new JSONObject(result.getResult());
//						if (obj.getInt("code") == 1) {
//							JSONObject objRes = obj.getJSONObject("result");
//							JSONObject objResult = objRes.getJSONObject("returnResult");
//							arrayOfOrder = FastJsonUtils.getSingleBean(objResult.toString(), ArrayOfOrder.class);
//							if (arrayOfOrder != null) {
//								if (pageNo == 1) {
//									myOrder.clear();
//								}
//								myOrder.addAll(arrayOfOrder.getList());
//							}
//						} else {
//							showInfo(obj.getString("desc"));
//						}
//					} else {
//						showInfo(getString(R.string.A6));
//					}
//				} catch (Exception e) {
//					Log.e("MyCommissionActivity", "initMyList", e);
//					LogUmeng.reportError(RepaymentActivity.this, e);
//				} finally {
//
//					runOnUiThread(new Runnable() {
//						public void run() {
//							
//							if (transDialog != null && transDialog.isShowing()) {
//								transDialog.dismiss();
//							}
//							myAdapter.notifyDataSetChanged();
//							pull_refresh_scrollview.onRefreshComplete();
//						}
//					});
//
//				}
//			}
//		});
//	}
//	
//	
//	private void showInfo(final String info) {
//
//		runOnUiThread(new Runnable() {
//			public void run() {
//				if (transDialog != null && transDialog.isShowing()) {
//					transDialog.dismiss();
//				}
//				Toast.makeText(RepaymentActivity.this, info, Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
}
