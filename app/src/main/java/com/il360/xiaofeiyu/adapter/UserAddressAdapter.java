package com.il360.xiaofeiyu.adapter;

import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.model.address.UserAddress;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserAddressAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mInflater;
	private List<UserAddress> list;
	
	public UserAddressAdapter(List<UserAddress> list, Context context) {
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
			convertView = mInflater.inflate(R.layout.listitem_address, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvDefault = (TextView) convertView.findViewById(R.id.tvDefault);
			holder.tvPhone = (TextView) convertView.findViewById(R.id.tvPhone);
			holder.tvAddress = (TextView) convertView.findViewById(R.id.tvAddress);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(list.get(position).getName());
		holder.tvPhone.setText(list.get(position).getPhone());
		holder.tvAddress.setText((list.get(position).getProvince() != null ? list.get(position).getProvince() : "")
				+ (list.get(position).getCity() != null ? list.get(position).getCity() : "")
				+ (list.get(position).getArea() != null ? list.get(position).getArea() : "")
				+ list.get(position).getAddress());
		if(list.get(position).getIsDefault() != null && list.get(position).getIsDefault() == 1){
			holder.tvDefault.setVisibility(View.VISIBLE);
		} else {
			holder.tvDefault.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	class ViewHolder{
		TextView tvName;
		TextView tvDefault;
		TextView tvPhone;
		TextView tvAddress;
	}
}

