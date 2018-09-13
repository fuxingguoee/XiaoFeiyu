package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.order.Stages;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PeriodsAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<Stages> list;
	private int myPositon;

	public PeriodsAdapter(List<Stages> list, Context context, int myPositon) {
		this.context = context;
		this.list = list;
		this.myPositon = myPositon;
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
			convertView = mInflater.inflate(R.layout.listitem_attribute, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvName.setText(list.get(position).getStagesNumber() + "期");
		if (position == myPositon) {
			holder.tvName.setBackgroundResource(R.drawable.bg_corners_transparent_red);
			holder.tvName.setTextColor(ContextCompat.getColor(context, R.color.red));
		} else {
			holder.tvName.setBackgroundResource(R.drawable.bg_corners_transparent_gray);
			holder.tvName.setTextColor(ContextCompat.getColor(context, R.color.black));
		}
		return convertView;
	}

	class ViewHolder {
		TextView tvName;
	}
}
