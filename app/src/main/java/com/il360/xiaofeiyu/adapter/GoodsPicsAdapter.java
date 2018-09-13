package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.goods.GoodsExt;
import com.il360.xiaofeiyu.util.ImageFromTxyUtil;
import com.il360.xiaofeiyu.util.SDCardUtil;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GoodsPicsAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<GoodsExt> list;
	private boolean flag;
	private int width;
	private int type;

	public GoodsPicsAdapter(List<GoodsExt> list, Context context,int type) {
		this.context = context;
		this.list = list;
		this.type = type;
		flag = SDCardUtil.hasSDCard(context);
		
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

        width = dm.widthPixels;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_goods_pic, null);
			holder.ivImageShow = (ImageView) convertView.findViewById(R.id.ivImageShow);
			
			int screenWidth = width;
			ViewGroup.LayoutParams lp = holder.ivImageShow.getLayoutParams();
			lp.width = screenWidth;
			
			if(type == 2){
				lp.height = screenWidth * 2;
			} else {
				lp.height = screenWidth;
			}
			holder.ivImageShow.setLayoutParams(lp);
			
			if (flag && list.get(position)!= null) {
				ImageFromTxyUtil.loadImage(context, list.get(position).getGoodsPic(), holder.ivImageShow);
			} else {
				holder.ivImageShow.setImageResource(R.drawable.ic_image404);
			}
			
//			holder.ivImageShow.setMaxWidth(screenWidth);
//			holder.ivImageShow.setMaxHeight((int) (screenWidth * 2));// 这里其实可以根据需求而定

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}

	class ViewHolder {
		ImageView ivImageShow;
	}
}