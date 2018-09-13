package com.il360.xiaofeiyu.adapter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.home.PayActivity_;
import com.il360.xiaofeiyu.common.GlobalPara;
import com.il360.xiaofeiyu.model.order.OrderPeriodsExt;
import com.il360.xiaofeiyu.util.DataUtil;
import com.il360.xiaofeiyu.view.CustomDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotPayBackAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<OrderPeriodsExt> list;
	DecimalFormat df = new DecimalFormat("0.00");
	
	public NotPayBackAdapter(List<OrderPeriodsExt> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_not_pay_back, null);
			holder.tvNumber = (TextView) convertView.findViewById(R.id.tvNumber);
			holder.tvMoney = (TextView) convertView.findViewById(R.id.tvMoney);
			holder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			holder.ivPrompt = (ImageView) convertView.findViewById(R.id.ivPrompt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvNumber.setText(list.get(position).getNumber() + "期");
		holder.tvMoney.setText(df.format(list.get(position).getFee().add(list.get(position).getAmount()).add(list.get(position).getOverdueFee())));
		holder.tvTime.setText(DataUtil.getLongToDateShort(Long.valueOf(list.get(position).getExpireTime())));
		
		if(list.get(position).getOverdueFee() != null && list.get(position).getOverdueFee().compareTo(BigDecimal.ZERO) == 1){
			holder.ivPrompt.setVisibility(View.VISIBLE);
		} else {
			holder.ivPrompt.setVisibility(View.GONE);
		}
		
		if(list.get(position).getStatus() != null && list.get(position).getStatus() == 1){
			holder.tvStatus.setBackgroundResource(R.drawable.bg_corners_transparent_white);
			holder.tvStatus.setText("已还清");
			holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
		} else {
			holder.tvStatus.setBackgroundResource(R.drawable.bg_corners_blue);
			holder.tvStatus.setText("去还款");
			holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
		}
		
		holder.ivPrompt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				double d = 0.00;//默认
				if(GlobalPara.getCardConfigList() != null && GlobalPara.getCardConfigList().size() > 0){
					for (int i = 0; i < GlobalPara.getCardConfigList().size(); i++) {
						if(GlobalPara.getCardConfigList().get(i).getConfigGroup().equals("penaltyfee") 
								&& GlobalPara.getCardConfigList().get(i).getConfigName().equals("penaltyfee1")){
							d = Double.valueOf(GlobalPara.getCardConfigList().get(i).getConfigValue()) / 100;
						}
					}
				}
				
				showDailog(df.format(d), list.get(position).getOverdueFee());
			}
		});
		
		holder.tvStatus.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(list.get(position).getStatus() != null && list.get(position).getStatus() == 0){
					Intent intent = new Intent(context,PayActivity_.class);
					intent.putExtra("number", list.get(position).getNumber() + "");
					intent.putExtra("orderNo", list.get(position).getOrderNo());
					intent.putExtra("buyType", "2");
					intent.putExtra("money", df.format(list.get(position).getFee().add(list.get(position).getAmount()).add(list.get(position).getOverdueFee())));
					context.startActivity(intent);
				}
			}
		});
		
		return convertView;
	}
	
	private void showDailog(String penaltyfee,BigDecimal overdueFee) {
		CustomDialog.Builder builder = new CustomDialog.Builder(context);
		builder.setTitle(R.string.app_name);
		builder.setMessage("逾期利率："+penaltyfee+"%/天"+"\n您已逾期，滞纳金：￥" + overdueFee);
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
		TextView tvTime;
		TextView tvStatus;
		ImageView ivPrompt;
	}
}
