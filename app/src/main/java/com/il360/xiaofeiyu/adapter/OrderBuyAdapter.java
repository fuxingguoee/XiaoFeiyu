package com.il360.xiaofeiyu.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.home.PayActivity_;
import com.il360.xiaofeiyu.activity.order.BillActivity_;
import com.il360.xiaofeiyu.activity.order.LogisticsInfoActivity2_;
import com.il360.xiaofeiyu.activity.order.RedeemActivity_;
import com.il360.xiaofeiyu.activity.order.RenewActivity_;
import com.il360.xiaofeiyu.model.order.FrozenFeeOrder;
import com.il360.xiaofeiyu.model.order.LeaseOrder;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrderBuyAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<LeaseOrder> list;
	private FrozenFeeOrder frozenFeeOrder;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public OrderBuyAdapter(List<LeaseOrder> list, Context context, FrozenFeeOrder frozenFeeOrder) {
		this.context = context;
		this.list = list;
		this.frozenFeeOrder = frozenFeeOrder;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_order_buy, null);
			holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tvOrderTime);
			holder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tvOrderStatus);
			holder.tvPhoneName = (TextView) convertView.findViewById(R.id.tvPhoneName);
			holder.tvPhonePrice = (TextView) convertView.findViewById(R.id.tvPhonePrice);
			holder.tvLine = (TextView) convertView.findViewById(R.id.tvLine);
			holder.tvRedeem = (TextView) convertView.findViewById(R.id.tvRedeem);
			holder.tvRenew = (TextView) convertView.findViewById(R.id.tvRenew);
			holder.tvOverdueFee = (TextView) convertView.findViewById(R.id.tvOverdueFee);
			holder.tvRecovery = (TextView) convertView.findViewById(R.id.tvRecovery);
			holder.tvFrozenFee = (TextView) convertView.findViewById(R.id.tvFrozenFee);
			holder.llRemind = (LinearLayout) convertView.findViewById(R.id.llRemind);
			holder.llButton = (LinearLayout) convertView.findViewById(R.id.llButton);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvOrderTime.setText(sdf.format(Long.parseLong(list.get(position).getCreateTime())));
		if(list.get(position).getGoodsSysDetail() != null){
			holder.tvPhoneName.setText(list.get(position).getGoodsSysDetail());
		} else {
			holder.tvPhoneName.setText("其他机型");
		}
		holder.tvPhonePrice.setText("设备金额:" + list.get(position).getMoney() + "元");
		
		// 0审核中 -1未通过 1.通过审核，可以放款，2已下款，租赁中，3已赎回
		if(list.get(position).getStatus() != null && list.get(position).getStatus() == 0){
			holder.tvOrderStatus.setText("审核中");
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textChecking));
		} else if(list.get(position).getStatus() != null && list.get(position).getStatus() == -1){
			holder.tvOrderStatus.setText("未通过");
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
		}  else if(list.get(position).getStatus() != null && list.get(position).getStatus() == 1){
			holder.tvOrderStatus.setText("审核通过，等待下款");
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textChecking));
		} else if(list.get(position).getStatus() != null && list.get(position).getStatus() == 2){
			if(list.get(position).getExt1() != null && (Integer.parseInt(list.get(position).getExt1()) > 0
					|| Integer.parseInt(list.get(position).getExt1()) == 0)){
				holder.llButton.setVisibility(View.VISIBLE);
				holder.tvLine.setVisibility(View.VISIBLE);
				holder.tvOverdueFee.setVisibility(View.GONE);
				holder.tvRecovery.setVisibility(View.GONE);
				holder.tvFrozenFee.setVisibility(View.GONE);
				holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textExpire));
				if(list.get(position).getExt1().equals("0")){
					holder.tvOrderStatus.setText( "今天到期");
				} else if(list.get(position).getExt1().equals("1")){
					holder.tvOrderStatus.setText( "明天到期");
				} else {
					holder.tvOrderStatus.setText( list.get(position).getExt1() + "天后到期");
				}
				
			} else if(list.get(position).getExt1() != null && Integer.parseInt(list.get(position).getExt1()) < 0){
				holder.llButton.setVisibility(View.VISIBLE);
				holder.tvLine.setVisibility(View.VISIBLE);
				holder.tvRenew.setVisibility(View.GONE);
				holder.tvRedeem.setVisibility(View.GONE);
				holder.tvRecovery.setVisibility(View.GONE);
				holder.tvFrozenFee.setVisibility(View.GONE);
				holder.llRemind.setVisibility(View.VISIBLE);
				holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textOverTime));
				holder.tvOrderStatus.setText("已超时" + Integer.parseInt(list.get(position).getExt1())/-1 + "天");
				holder.tvPhonePrice.setText("需支付总费用:" + list.get(position).getAllOverdueFee() + "元");
			}
		} else if(list.get(position).getStatus() != null && list.get(position).getStatus() == 3){
			holder.tvOrderStatus.setText("已赎回");
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textChecking));
		} else if(list.get(position).getStatus() != null && list.get(position).getStatus() == 4){
			holder.llButton.setVisibility(View.VISIBLE);
			holder.tvLine.setVisibility(View.VISIBLE);
			holder.tvRenew.setVisibility(View.GONE);
			holder.tvRedeem.setVisibility(View.GONE);
			holder.tvFrozenFee.setVisibility(View.GONE);
			holder.tvOverdueFee.setVisibility(View.GONE);
			holder.tvRecovery.setVisibility(View.VISIBLE);
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textOverTime));
			holder.tvOrderStatus.setText("可以寄回");
		} else if(list.get(position).getStatus() != null && list.get(position).getStatus() == 5){
			holder.tvOrderStatus.setText("已寄回");
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textChecking));
		} else if(list.get(position).getStatus() != null && list.get(position).getStatus() == 6){
			holder.tvOrderStatus.setText("手机归还");
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textChecking));
		} else if(list.get(position).getStatus() != null && list.get(position).getStatus() == -2){
			holder.tvOrderStatus.setText("非正常关闭");
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textChecking));
		} else if(list.get(position).getStatus() != null && list.get(position).getStatus() == -3){
			holder.tvOrderStatus.setText("订单冻结");
			holder.llButton.setVisibility(View.VISIBLE);
			holder.tvLine.setVisibility(View.VISIBLE);
			holder.tvRenew.setVisibility(View.GONE);
			holder.tvRedeem.setVisibility(View.GONE);
			holder.tvRecovery.setVisibility(View.GONE);
			holder.tvOverdueFee.setVisibility(View.GONE);
			if(frozenFeeOrder == null){
				holder.llButton.setVisibility(View.GONE);
			} else {
				holder.tvPhonePrice.setText("今日需支付" + frozenFeeOrder .getThisFee() + "元");
			}
			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.textChecking));
		}
		
		holder.tvRedeem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,RedeemActivity_.class);
				intent.putExtra("leaseOrder", list.get(position));
				context.startActivity(intent);
			}
		});
		holder.tvRenew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,RenewActivity_.class);
				intent.putExtra("leaseOrder", list.get(position));
				context.startActivity(intent);
			}
		});
		
		holder.tvOverdueFee.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,BillActivity_.class);
				intent.putExtra("type", "3");
				intent.putExtra("leaseOrder", list.get(position));
				context.startActivity(intent);
			}
		});

		holder.tvFrozenFee.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,PayActivity_.class);
				intent.putExtra("type", "-3");
				intent.putExtra("frozenFeeOrder", frozenFeeOrder);
				context.startActivity(intent);
			}
		});

		holder.tvRecovery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,LogisticsInfoActivity2_.class);
				intent.putExtra("leaseOrder", list.get(position));
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	class ViewHolder {
		TextView tvOrderTime;
		TextView tvOrderStatus;
		TextView tvPhoneName;
		TextView tvPhonePrice;
		TextView tvLine;
		TextView tvRedeem;
		TextView tvRenew;
		TextView tvOverdueFee;
		TextView tvRecovery;
		TextView tvFrozenFee;
		LinearLayout llRemind;
		LinearLayout llButton;
	}
}
