package com.il360.xiaofeiyu.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class RepayAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}
//	private Context context;
//	private LayoutInflater mInflater;
//	private List<Order> list;
//
//	public RepayAdapter(List<Order> list, Context context) {
//		this.context = context;
//		this.list = list;
//	}
//
//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHolder holder;
//		if (convertView == null) {
//			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			holder = new ViewHolder();
//			convertView = mInflater.inflate(R.layout.listitem_repay, null);
//			holder.ivGoodsPic = (ImageView) convertView.findViewById(R.id.ivGoodsPic);
//			holder.tvOrderNo = (TextView) convertView.findViewById(R.id.tvOrderNo);
//			holder.tvPhoneDesc = (TextView) convertView.findViewById(R.id.tvPhoneDesc);
//			holder.tvPhoneAtt = (TextView) convertView.findViewById(R.id.tvPhoneAtt);
//			holder.tvPhonePrice = (TextView) convertView.findViewById(R.id.tvPhonePrice);
//			holder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tvOrderStatus);
//			holder.tvOrderTip = (TextView) convertView.findViewById(R.id.tvOrderTip);
//
//			// 从腾讯云下载图片
//			if(list.get(position).getSmallPic() != null){
//				ImageFromTxyUtil.loadImage(context, list.get(position).getSmallPic(), holder.ivGoodsPic);
//			}
//			convertView.setTag(holder);
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		
//		holder.tvOrderNo.setText("订单号：" + list.get(position).getOrderNo());
//		holder.tvPhoneDesc.setText(list.get(position).getGoodsDesc()!= null ? list.get(position).getGoodsDesc() : "无");
//		holder.tvPhoneAtt.setText("配置：" + (list.get(position).getGoodsType() != null ? list.get(position).getGoodsType() : "")
//				+ " " + (list.get(position).getGoodsColour() != null ? list.get(position).getGoodsColour() : "")
//				+ " " + (list.get(position).getGoodsVersion() != null ? list.get(position).getGoodsVersion() : ""));
//		holder.tvPhonePrice.setText("￥" + list.get(position).getGoodsPrice());
//		if(list.get(position).getPayStatus() != null && list.get(position).getPayStatus() == 0){
//			holder.tvOrderStatus.setText("待还款信息");
//			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
//			holder.tvOrderTip.setVisibility(View.VISIBLE);
//		} else if(list.get(position).getPayStatus() != null && list.get(position).getPayStatus() == 1) {
//			holder.tvOrderStatus.setText("已还款信息");
//			holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.blue));
//			holder.tvOrderTip.setVisibility(View.GONE);
//		}
//		return convertView;
//	}
//
//	
//
//	class ViewHolder {
//		ImageView ivGoodsPic;
//		TextView tvPhoneDesc;
//		TextView tvPhoneAtt;
//		TextView tvPhonePrice;
//		TextView tvOrderStatus;
//		TextView tvOrderNo;
//		TextView tvOrderTip;
//	}
}
