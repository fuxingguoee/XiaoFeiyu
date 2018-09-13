package com.il360.xiaofeiyu.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.order.LeaseOrder;
import com.il360.xiaofeiyu.model.order.Stages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderRenewAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<Stages> list;
	private int myPosition;
	private LeaseOrder leaseOrder;
	DecimalFormat df = new DecimalFormat("#0.00");
	DecimalFormat df1 = new DecimalFormat("#0.0");

	public OrderRenewAdapter(List<Stages> list, Context context, int myPosition, LeaseOrder leaseOrder) {
		this.context = context;
		this.list = list;
		this.myPosition = myPosition;
		this.leaseOrder = leaseOrder;
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
			convertView = mInflater.inflate(R.layout.listitem_order_renew, null);
			holder.tvRenewDay = (TextView) convertView.findViewById(R.id.tvRenewDay);
			holder.tvRenewPrice = (TextView) convertView.findViewById(R.id.tvRenewPrice);
			holder.tvDiscount = (TextView) convertView.findViewById(R.id.tvDiscount);
			holder.ivChecked = (ImageView) convertView.findViewById(R.id.ivChecked);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvRenewDay.setText(list.get(position).getStagesNumber() + "天");
		if(list.get(position).getStagesRate().equals("1")){
			holder.tvDiscount.setText("");
		} else {
			holder.tvDiscount.setText(df1.format(Double.parseDouble(list.get(position).getStagesRate()) * 10) + "折");
		}
		holder.tvRenewPrice.setText( df.format(Double.parseDouble(list.get(position).getExt1())) + "元");
		if (position == myPosition) {
			holder.ivChecked.setBackgroundResource(R.drawable.re_checkbox_checked);
		} else {
			holder.ivChecked.setBackgroundResource(R.drawable.re_checkbox_nor);
		}
		

		return convertView;
	}
	
	class ViewHolder {
		TextView tvRenewDay;
		TextView tvRenewPrice;
		TextView tvDiscount;
		ImageView ivChecked;
	}
}
