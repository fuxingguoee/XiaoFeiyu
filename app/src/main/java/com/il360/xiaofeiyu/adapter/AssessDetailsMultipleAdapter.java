package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.recovery.PhoneAssessActivity.ListCallback;
import com.il360.xiaofeiyu.model.recovery.AssessDetails;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AssessDetailsMultipleAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<AssessDetails> list;
	private int myPosition;
	private ListCallback myCallback;

	public AssessDetailsMultipleAdapter(int positon, List<AssessDetails> list, Context context , ListCallback callback) {
		this.context = context;
		this.list = list;
		this.myPosition = positon;
		this.myCallback = callback;
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
		final ViewHolder holder;
		if (convertView == null) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_assess_details, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvName.setText(list.get(position).getAnswerDesc());
		if (list.get(position).isCheck() != null && list.get(position).isCheck()) {
			holder.tvName.setBackgroundResource(R.drawable.bg_cormers_assess_y);
			holder.tvName.setTextColor(ContextCompat.getColor(context, R.color.main_logo));
		} else {
			holder.tvName.setBackgroundResource(R.drawable.bg_cormers_assess_n);
			holder.tvName.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
		}
		
		holder.tvName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (list.get(position).isCheck() != null && list.get(position).isCheck()) {
					list.get(position).setCheck(false);
					holder.tvName.setBackgroundResource(R.drawable.bg_cormers_assess_n);
					holder.tvName.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
				} else {
					list.get(position).setCheck(true);
					holder.tvName.setBackgroundResource(R.drawable.bg_cormers_assess_y);
					holder.tvName.setTextColor(ContextCompat.getColor(context, R.color.main_logo));
				}
//				myCallback.refreshAdapter(myPosition, position);
			}
		});
		
		return convertView;
	}

	class ViewHolder {
		TextView tvName;
	}
}
