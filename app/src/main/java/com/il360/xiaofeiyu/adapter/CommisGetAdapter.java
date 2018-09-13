package com.il360.xiaofeiyu.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.user.UserReward;
import com.il360.xiaofeiyu.util.DataUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommisGetAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<UserReward> list;
	
	public CommisGetAdapter(List<UserReward> list, Context context) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView==null){
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_commis_get, null);
			holder.tvOrderNo = (TextView) convertView.findViewById(R.id.tvOrderNo);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvOrderNo.setText("订单号-" + list.get(position).getOrderNo());
		DecimalFormat df = new DecimalFormat("0.00");
		holder.tvMoney.setText("￥"+df.format(list.get(position).getAmount()));
		holder.tvDate.setText(list.get(position).getUpdateTime() != null ? DataUtil.getLongToDate(list.get(position).getUpdateTime()) : "");
		
		return convertView;
	}
	class ViewHolder{
		TextView tvOrderNo;
		TextView tvDate;
		TextView tvMoney;
	}
}
