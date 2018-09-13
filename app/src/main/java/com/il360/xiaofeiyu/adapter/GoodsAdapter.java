package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.model.goods.Goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GoodsAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<Goods> list;

	public GoodsAdapter(List<Goods> list, Context context) {
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
//		ViewHolder holder;
//		if (convertView == null) {
//			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			holder = new ViewHolder();
//			convertView = mInflater.inflate(R.layout.listitem_goods, null);
//			holder.ivGoodsPic = (ImageView) convertView.findViewById(R.id.ivGoodsPic);
//			holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tvGoodsName);
//			holder.tvGoodsPrice = (TextView) convertView.findViewById(R.id.tvGoodsPrice);
//			holder.tvGoodsSelled = (TextView) convertView.findViewById(R.id.tvGoodsSelled);
//			holder.tvPayForWeeks = (TextView) convertView.findViewById(R.id.tvPayForWeeks);
//			holder.tvPeriods = (TextView) convertView.findViewById(R.id.tvPeriods);
//			convertView.setTag(holder);
//			
//			if(list.get(position).getSmallPic() != null){
//				ImageFromTxyUtil.loadImage(context, list.get(position).getSmallPic(), holder.ivGoodsPic);
//			}
//			
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		
//		DecimalFormat df = new DecimalFormat("0.00");
//		holder.tvGoodsName.setText(list.get(position).getGoodsDesc());
//		holder.tvGoodsPrice.setText("￥" + list.get(position).getGoodsPrice());
//		holder.tvGoodsSelled.setText(list.get(position).getGoodsSell().toString());
//		double payForWeeks = list.get(position).getGoodsPrice().doubleValue() * (1 + GlobalPara.getDefaultRate()) / 10;
//		holder.tvPayForWeeks.setText("周供:￥" + df.format(payForWeeks));
//		holder.tvPeriods.setText("10期");
		return convertView;
	}

	class ViewHolder {
		ImageView ivGoodsPic;
		TextView tvGoodsName;
		TextView tvGoodsPrice;
		TextView tvGoodsSelled;
		TextView tvPayForWeeks;
		TextView tvPeriods;
	}
}
