package com.il360.xiaofeiyu.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.user.UserWithdrawals;
import com.il360.xiaofeiyu.util.DataUtil;
import com.il360.xiaofeiyu.util.NumReplaceUtil;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommisApplyAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<UserWithdrawals> list;
	
	public CommisApplyAdapter(List<UserWithdrawals> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_commis_apply, null);
			holder.tvBanKCard = (TextView) convertView.findViewById(R.id.tvBanKCard);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);	
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			
		}
		holder.tvBanKCard.setText(list.get(position).getBankName() + "("
				+ NumReplaceUtil.lastNum(list.get(position).getBankNo(), 4) + ")" + "-");
		holder.tvDate.setText(DataUtil.getLongToDate(list.get(position).getCreateTime()));		
		DecimalFormat df = new DecimalFormat("0.00");
		holder.tvMoney.setText("￥"+df.format(list.get(position).getAmount()));
		
		int status = list.get(position).getStatus() != null ? list.get(position).getStatus() : -99;
		if (status == -1) {
			holder.tvStatus.setText("提现失败");
			holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
		} else if (status == 0) {
			holder.tvStatus.setText("申请提现");
			holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.text_gray2));
		} else if (status == 1) {
			holder.tvStatus.setText("提现成功");
			holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.text_green2));
		} else {
			holder.tvStatus.setText("未知状态");
			holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.text_gray2));
		}
		
		return convertView;
	}
	class ViewHolder{
		TextView tvBanKCard;
		TextView tvStatus;
		TextView tvDate;
		TextView tvMoney;
	}
}
