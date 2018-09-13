package com.il360.xiaofeiyu.adapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.hua.UserCredit;
import com.il360.xiaofeiyu.util.DataUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserCreditAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<UserCredit> list;
	DecimalFormat df = new DecimalFormat("0.00");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public UserCreditAdapter(List<UserCredit> list, Context context) {
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
		if (convertView == null) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_user_credit, null);
			holder.llApplyDate = (LinearLayout) convertView.findViewById(R.id.llApplyDate);
			holder.tvApplyDate = (TextView) convertView.findViewById(R.id.tvApplyDate);
			holder.tvApplyTime = (TextView) convertView.findViewById(R.id.tvApplyTime);
			holder.tvLoan = (TextView) convertView.findViewById(R.id.tvLoan);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String timeStr = sdf.format(list.get(position).getCreateTime());
		if(position > 0){
			String timeStr1 = sdf.format(list.get(position-1).getCreateTime());
			if(DataUtil.getDate(timeStr).equals(DataUtil.getDate(timeStr1))){
				holder.llApplyDate.setVisibility(View.GONE);
			} else {
				holder.llApplyDate.setVisibility(View.VISIBLE);
			}
		} else {
			holder.llApplyDate.setVisibility(View.VISIBLE);
		}
		
		holder.tvApplyDate.setText("申请日期：" + DataUtil.getDate(timeStr));

		holder.tvApplyTime.setText(DataUtil.getTime(timeStr));
		holder.tvLoan.setText(list.get(position).getAmount() + "元");
		holder.tvStatus.setText(list.get(position).getChstatus());
		if (list.get(position).getStatus() != null && list.get(position).getStatus() == 0) {
			holder.tvStatus.setTextColor(context.getResources().getColor(R.color.green));
		} else {
			holder.tvStatus.setTextColor(context.getResources().getColor(R.color.red));
		}

		return convertView;
	}

	class ViewHolder {
		LinearLayout llApplyDate;
		TextView tvApplyDate;
		TextView tvApplyTime;
		TextView tvLoan;
		TextView tvStatus;
	}
}
