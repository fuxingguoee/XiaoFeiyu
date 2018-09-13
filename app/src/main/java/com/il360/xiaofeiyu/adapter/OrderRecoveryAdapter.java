package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.recovery.LogisticsInfoActivity_;
import com.il360.xiaofeiyu.model.recovery.UserRecovery;
import com.il360.xiaofeiyu.util.DataUtil;
import com.il360.xiaofeiyu.util.ImageFromTxyUtil;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderRecoveryAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<UserRecovery> list;

	public OrderRecoveryAdapter(List<UserRecovery> list, Context context) {
		this.context = context;
		this.list = list;
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
			convertView = mInflater.inflate(R.layout.listitem_order_recovery, null);
			holder.ivOrderPic = (ImageView) convertView.findViewById(R.id.ivOrderPic);
			holder.tvOrderName = (TextView) convertView.findViewById(R.id.tvOrderName);
			holder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tvOrderStatus);
			holder.tvOrderAssess = (TextView) convertView.findViewById(R.id.tvOrderAssess);
			holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tvOrderTime);
			holder.tvSubmit = (TextView) convertView.findViewById(R.id.tvSubmit);
			convertView.setTag(holder);
			
			if(list.get(position).getRecoveryPic() != null){
				ImageFromTxyUtil.loadImage(context, list.get(position).getRecoveryPic(), holder.ivOrderPic);
			} else {
				holder.ivOrderPic.setBackgroundResource(R.drawable.ic_phone2);
			}
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvOrderName.setText(list.get(position).getPhoneName());
		holder.tvOrderAssess.setText("￥" + list.get(position).getAmount());
		holder.tvOrderTime.setText(DataUtil.getLongToDate(Long.valueOf(list.get(position).getCreateTime())));
		
		// //状态 0已下单 1已收货 2已完成评估 3可以下款 4已完成交易 -1取消订单 5已发货
		if(list.get(position).getStatus() != null && list.get(position).getStatus() == 0){
			if(TextUtils.isEmpty(list.get(position).getExpressNo()) && TextUtils.isEmpty(list.get(position).getExpressCompany())){
				holder.tvOrderStatus.setText("待寄出");
				holder.tvSubmit.setVisibility(View.VISIBLE);
			} else {
				holder.tvOrderStatus.setText(list.get(position).getStatusDesc());
				holder.tvSubmit.setVisibility(View.GONE);
			}
		} else {
			holder.tvOrderStatus.setText(list.get(position).getStatusDesc());
			holder.tvSubmit.setVisibility(View.GONE);
		}
		
		holder.tvSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, LogisticsInfoActivity_.class);
				intent.putExtra("orderNo", list.get(position).getOrderNo());
				context.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	class ViewHolder {
		ImageView ivOrderPic;
		TextView tvOrderName;
		TextView tvOrderStatus;
		TextView tvOrderAssess;
		TextView tvOrderTime;
		TextView tvSubmit;
	}
}