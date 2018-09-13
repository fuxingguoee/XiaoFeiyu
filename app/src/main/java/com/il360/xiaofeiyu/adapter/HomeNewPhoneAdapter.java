package com.il360.xiaofeiyu.adapter;

import java.text.DecimalFormat;
import java.util.List;

import com.il360.xiaofeiyu.model.goods.Goods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeNewPhoneAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<Goods> list;
	DecimalFormat df = new DecimalFormat("0.00");

	public HomeNewPhoneAdapter(List<Goods> list, Context context) {
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
//			convertView = mInflater.inflate(R.layout.listitem_home_hot_phone, null);
//			holder.ivGoodsPic = (ImageView) convertView.findViewById(R.id.ivGoodsPic);
//			holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tvGoodsName);
//			convertView.setTag(holder);
//			
//			// 从腾讯云下载图片
//			if (list.get(position).getSmallPic() != null) {
//				ImageFromTxyUtil.loadImage(context, list.get(position).getSmallPic(), holder.ivGoodsPic);
//			}
//			holder.tvGoodsName.setText(list.get(position).getGoodsName());
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
		
		

		return convertView;
	}

	class ViewHolder {
		ImageView ivGoodsPic;
		TextView tvGoodsName;
	}
}
