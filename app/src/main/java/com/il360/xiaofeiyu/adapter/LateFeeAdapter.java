package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.hua.CreditPenaltyfee;
import com.il360.xiaofeiyu.util.DataUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LateFeeAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<CreditPenaltyfee> list;
	
	public LateFeeAdapter(List<CreditPenaltyfee> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_late_fee, null);
			holder.tvData = (TextView) convertView.findViewById(R.id.tvData);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);	
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvData.setText(DataUtil.getDate(list.get(position).getOccurDay()));
		int status = list.get(position).getStatus() != null ? list.get(position).getStatus() : -99;
		if (status == 1) {
			holder.tvMoney.setText("+￥" + list.get(position).getAmount());
			holder.tvMoney.setTextColor(context.getResources().getColor(R.color.red));
		} else if (status == 2) {
			holder.tvMoney.setText("-￥" + list.get(position).getAmount());
			holder.tvMoney.setTextColor(context.getResources().getColor(R.color.green));
		}
		
		return convertView;
	}
	class ViewHolder{
		TextView tvData;
		TextView tvMoney;
	}
}
