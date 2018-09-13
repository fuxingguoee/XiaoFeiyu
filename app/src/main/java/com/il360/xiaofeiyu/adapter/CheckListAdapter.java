package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CheckListAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<String> list;
	private int myPosition;

	public CheckListAdapter(List<String> list, Context context, int myPosition) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_check, null);
			holder.tvCheckContent = (TextView) convertView.findViewById(R.id.tvCheckContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvCheckContent.setText(list.get(position).toString());
//		if (position == myPosition) {
		holder.tvCheckContent.setBackgroundResource(R.drawable.bg_check_btn);
//		} else {
//			holder.tvCheckContent.setBackgroundResource(R.drawable.bg_check_btn2);
//		}

		return convertView;
	}
	
	class ViewHolder {
		TextView tvCheckContent;
	}
}
