package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.recovery.CreditAmount;
import com.il360.xiaofeiyu.util.ImageFromTxyUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhoneAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<CreditAmount> list;

	public PhoneAdapter(List<CreditAmount> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_phone, null);
			holder.tvPhoneName = (TextView) convertView.findViewById(R.id.tvPhoneName);
			holder.tvPhonePic = (ImageView) convertView.findViewById(R.id.tvPhonePic);
			convertView.setTag(holder);

			if (list.get(position).getRecoveryPic() != null) {
				ImageFromTxyUtil.loadImage(context, list.get(position).getRecoveryPic(), holder.tvPhonePic);
			} else {
				holder.tvPhonePic.setBackgroundResource(R.drawable.ic_phone);
			}

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvPhoneName.setText(list.get(position).getPhoneGeneration());
		return convertView;
	}

	class ViewHolder {
		ImageView tvPhonePic;
		TextView tvPhoneName;
	}
}
