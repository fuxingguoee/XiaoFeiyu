package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.order.Record;
import com.il360.xiaofeiyu.util.DataUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<Record> list;
	
	public RecordAdapter(List<Record> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_record, null);
			holder.ivPic = (ImageView) convertView.findViewById(R.id.ivPic);
			holder.tvLineUp = (TextView) convertView.findViewById(R.id.tvLineUp);
			holder.tvLineDown = (TextView) convertView.findViewById(R.id.tvLineDown);
			holder.tvOrderStatus = (TextView) convertView.findViewById(R.id.tvOrderStatus);
			holder.tvOrderTime = (TextView) convertView.findViewById(R.id.tvOrderTime);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.size() == 1) {
			holder.tvLineUp.setVisibility(View.INVISIBLE);
			holder.tvLineDown.setVisibility(View.INVISIBLE);
			holder.ivPic.setBackgroundResource(R.drawable.ic_now);
		} else {
			if (position == 0) {
				holder.tvLineUp.setVisibility(View.INVISIBLE);
				holder.tvLineDown.setVisibility(View.VISIBLE);
				holder.ivPic.setBackgroundResource(R.drawable.ic_now);
			} else if (position == list.size() - 1) {
				holder.tvLineUp.setVisibility(View.VISIBLE);
				holder.tvLineDown.setVisibility(View.INVISIBLE);
				holder.ivPic.setBackgroundResource(R.drawable.ic_before);
			} else {
				holder.tvLineUp.setVisibility(View.VISIBLE);
				holder.tvLineDown.setVisibility(View.VISIBLE);
				holder.ivPic.setBackgroundResource(R.drawable.ic_before);
			}
		}
		holder.tvOrderStatus.setText(list.get(position).getStatusDesc());
		holder.tvOrderTime.setText(DataUtil.getLongToDate(Long.valueOf(list.get(position).getCreateTime())));
		return convertView;
	}
	class ViewHolder{
		ImageView ivPic;
		TextView tvLineUp;
		TextView tvLineDown;
		TextView tvOrderStatus;
		TextView tvOrderTime;
	}
}

