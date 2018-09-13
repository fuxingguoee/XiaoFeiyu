package com.il360.xiaofeiyu.adapter;

import java.util.ArrayList;
import java.util.List;

import com.il360.xiaofeiyu.R;
import com.il360.xiaofeiyu.activity.recovery.PhoneAssessActivity.ListCallback;
import com.il360.xiaofeiyu.model.recovery.AssessDetails;
import com.il360.xiaofeiyu.model.recovery.AssessTitle;
import com.il360.xiaofeiyu.view.ListViewForScrollView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AssessAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<AssessTitle> list;
	private AssessDetailsSingleAdapter singleAdapter;
	private AssessDetailsMultipleAdapter multipleAdapter;
	private List<AssessDetails> singleList = new ArrayList<AssessDetails>();
	private List<AssessDetails> multipleList = new ArrayList<AssessDetails>();
	private ListCallback callback;

	public AssessAdapter(List<AssessTitle> list, Context context, ListCallback callback) {
		this.context = context;
		this.list = list;
		this.callback = callback;
		mInflater = LayoutInflater.from(context);
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
	
    public void update(int index,ListView listview){
        //得到第一个可见item项的位置
        int visiblePosition = listview.getFirstVisiblePosition();
        //得到指定位置的视图，对listview的缓存机制不清楚的可以去了解下
        View view = listview.getChildAt(index - visiblePosition);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.detailsList = (ListViewForScrollView) view.findViewById(R.id.detailsList);
        setData(holder,index);
    }
    private void setData(ViewHolder holder,int index){
		if(list.get(index).getType() == 0) { //单选
			singleList.clear();
			singleList.addAll(list.get(index).getList());
			singleAdapter = new AssessDetailsSingleAdapter(index,singleList, context,callback);
			holder.detailsList.setAdapter(singleAdapter);
		} else if(list.get(index).getType() == 1) {  //多选
			multipleList.clear();
			multipleList.addAll(list.get(index).getList());
			multipleAdapter = new AssessDetailsMultipleAdapter(index,multipleList, context,callback);
			holder.detailsList.setAdapter(multipleAdapter);
		}
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listitem_assess_title, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
			holder.tvMultiple = (TextView) convertView.findViewById(R.id.tvMultiple);
			holder.detailsList = (ListViewForScrollView) convertView.findViewById(R.id.detailsList);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tvName.setText(list.get(position).getItemDesc());
		if(list.get(position).getType() == 0) { //单选
			singleList.clear();
			holder.tvMultiple.setVisibility(View.GONE);
			singleList.addAll(list.get(position).getList());
			singleAdapter = new AssessDetailsSingleAdapter(position,singleList, context,callback);
			holder.detailsList.setAdapter(singleAdapter);
		} else if(list.get(position).getType() == 1) {  //多选
			multipleList.clear();
			holder.tvMultiple.setVisibility(View.VISIBLE);
			multipleList.addAll(list.get(position).getList());
			multipleAdapter = new AssessDetailsMultipleAdapter(position,multipleList, context,callback);
			holder.detailsList.setAdapter(multipleAdapter);
		}
		
		return convertView;
	}
	
	/**
     * 局部更新数据，调用一次getView()方法；Google推荐的做法
     *
     * @param listView 要更新的listview
     * @param position 要更新的位置
     */
    public void notifyDataSetChanged(ListView listView, int position) {
        /**第一个可见的位置**/
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        /**最后一个可见的位置**/
        int lastVisiblePosition = listView.getLastVisiblePosition();

        /**在看见范围内才更新，不可见的滑动后自动会调用getView方法更新**/
        if (position >= firstVisiblePosition && position <= lastVisiblePosition) {
            /**获取指定位置view对象**/
            View view = listView.getChildAt(position - firstVisiblePosition);
            getView(position, view, listView);
        }
    }
	
	class ViewHolder {
		TextView tvName;
		TextView tvMultiple;
		ListViewForScrollView detailsList;
	}
}
