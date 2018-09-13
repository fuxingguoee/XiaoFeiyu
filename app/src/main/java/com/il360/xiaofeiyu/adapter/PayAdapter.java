package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.hua.PayWay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PayAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<PayWay> list;
	private int myPosition;
	
	public PayAdapter(List<PayWay> list, Context context,int myPosition) {
		this.context = context;
		this.list = list;
		this.myPosition = myPosition;
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
			convertView = mInflater.inflate(R.layout.listitem_pay, null);
			holder.ivPayPic = (ImageView) convertView.findViewById(R.id.ivPayPic);
			holder.tvPayName = (TextView) convertView.findViewById(R.id.tvPayName);
			holder.tvPayDesc = (TextView) convertView.findViewById(R.id.tvPayDesc);
			holder.ivChecked = (ImageView) convertView.findViewById(R.id.ivChecked);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvPayName.setText(list.get(position).getPayName());
		//1支付宝 2连连支付3微信4银行卡
		if(list.get(position).getType() == 1){
			holder.ivPayPic.setBackgroundResource(R.drawable.icon_alipay2);
			holder.tvPayDesc.setText("推荐已开通支付宝支付的用户使用");
		} else if(list.get(position).getType() == 2){
			holder.ivPayPic.setBackgroundResource(R.drawable.icon_wuka);
			holder.tvPayDesc.setText("连连支付");
		} else if(list.get(position).getType() == 3){
			holder.ivPayPic.setBackgroundResource(R.drawable.icon_wechat2);
			holder.tvPayDesc.setText("推荐已开通微信钱包的用户使用");
		} else if(list.get(position).getType() == 4){
			holder.ivPayPic.setBackgroundResource(R.drawable.icon_bankcard);
			holder.tvPayDesc.setText("银行卡支付 单笔最高2万 每天最多3笔");
		} else if(list.get(position).getType() == 5){
			holder.ivPayPic.setBackgroundResource(R.drawable.icon_bankcard);
			holder.tvPayDesc.setText("银行卡快捷支付 每天最多3笔");
		}
		
		if(position == myPosition){
			holder.ivChecked.setBackgroundResource(R.drawable.ic_radio_press);
		} else {
			holder.ivChecked.setBackgroundResource(R.drawable.ic_radio_normal);
		}
		
		return convertView;
	}
	class ViewHolder{
		ImageView ivPayPic;
		TextView tvPayName;
		TextView tvPayDesc;
		ImageView ivChecked;
	}
}