package com.il360.xiaofeiyu.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.order.OrderPeriodsExt;
import com.il360.xiaofeiyu.util.DataUtil;
import com.il360.xiaofeiyu.view.CustomDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PayBackAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<OrderPeriodsExt> list;
	DecimalFormat df = new DecimalFormat("0.00");
	
	public PayBackAdapter(List<OrderPeriodsExt> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_pay_back, null);
			holder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
			holder.ivPrompt = (ImageView) convertView.findViewById(R.id.ivPrompt);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvNumber.setText(list.get(position).getNumber() + "期");
		
		holder.tvMoney.setText(df.format(list.get(position).getFee().add(list.get(position).getAmount()).add(list.get(position).getOverdueFee())));
		
		holder.tvTime.setText(DataUtil.getLongToDate(Long.valueOf(list.get(position).getPayTime())));
		if(list.get(position).getOverdueFee() != null && list.get(position).getOverdueFee().compareTo(BigDecimal.ZERO) == 1){
			holder.ivPrompt.setVisibility(View.VISIBLE);
		} else {
			holder.ivPrompt.setVisibility(View.GONE);
		}
		
		holder.ivPrompt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDailog(list.get(position).getOverdueFee());
			}
		});
		
		return convertView;
	}
	
	private void showDailog(BigDecimal overdueFee) {
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(R.string.app_name);
		builder.setMessage("您已逾期，滞纳金：￥" + overdueFee);
		builder.setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	class ViewHolder{
		TextView tvNumber;
		TextView tvMoney;
		ImageView ivPrompt;
		TextView tvTime;
	}
}
