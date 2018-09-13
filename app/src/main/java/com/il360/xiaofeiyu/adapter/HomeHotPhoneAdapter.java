package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.goods.Goods;
import com.il360.xiaofeiyu.util.ImageFromTxyUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeHotPhoneAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<Goods> list;

	public HomeHotPhoneAdapter(List<Goods> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_home_hot_phone, null);
			holder.ivGoodsPic = (ImageView) convertView.findViewById(R.id.ivGoodsPic);
			holder.tvGoodsName = (TextView) convertView.findViewById(R.id.tvGoodsName);
			holder.tvPhonePrice = (TextView) convertView.findViewById(R.id.tvPhonePrice);
			convertView.setTag(holder);
			
			// 从腾讯云下载图片
			if (list.get(position).getPic() != null) {
				ImageFromTxyUtil.loadImage(context, list.get(position).getPic(), holder.ivGoodsPic);
			}
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvGoodsName.setText(list.get(position).getExt2());
		holder.tvPhonePrice.setText("￥" + list.get(position).getAmount());

		return convertView;
	}

	class ViewHolder {
		ImageView ivGoodsPic;
		TextView tvGoodsName;
		TextView tvPhonePrice;
	}
}
