package com.il360.xiaofeiyu.adapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.order.RecordOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderRecordAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<RecordOrder> list;
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DecimalFormat df = new DecimalFormat("#0.00");

	public OrderRecordAdapter(List<RecordOrder> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_order_record, null);
			holder.tvOrderNo2 = (TextView) convertView.findViewById(R.id.tvOrderNo2);
			holder.tvPayTime2 = (TextView) convertView.findViewById(R.id.tvPayTime2);
			holder.tvPayMethod2 = (TextView) convertView.findViewById(R.id.tvPayMethod2);
			holder.tvPayResult = (TextView) convertView.findViewById(R.id.tvPayResult);
			holder.tvPayPrice = (TextView) convertView.findViewById(R.id.tvPayPrice);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvOrderNo2.setText(list.get(position).getOrderNo());
		holder.tvPayTime2.setText(sdf.format(Long.parseLong(list.get(position).getOrderSendTime())));

		if(list.get(position).getNumber() .equals("2")){
			holder.tvPayMethod2.setText("冻结订单");
		} else if (list.get(position).getNumber() .equals("1")){
			if(list.get(position).getType().equals("1")){
				holder.tvPayMethod2.setText("续租");
			} else if(list.get(position).getType().equals("2")){
				holder.tvPayMethod2.setText("赎回");
			} else if(list.get(position).getType().equals("3")){
				holder.tvPayMethod2.setText("超时缴费");
			}
		}

		if(list.get(position).getStatus().equals("0")){
			holder.tvPayResult.setText("未支付");
		} else if(list.get(position).getStatus().equals("-1")){
			holder.tvPayResult.setText("支付失败");
		} else if(list.get(position).getStatus().equals("1")){
			holder.tvPayResult.setText("支付成功");
		} else if(list.get(position).getStatus().equals("2")){
			holder.tvPayResult.setText("处理中");
		}
		holder.tvPayPrice.setText(df.format(Double.parseDouble(list.get(position).getOrderSendAmt())/100) + "元");
		
		return convertView;
	}
	
	class ViewHolder {
		TextView tvOrderNo2;
		TextView tvPayTime2;
		TextView tvPayMethod2;
		TextView tvPayResult;
		TextView tvPayPrice;
	}
}
