package com.damai.adapter;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.helper.AdapterFactory;
import com.damai.helper.DMAdapter;

/**
 * 分段
 * 
 * @author renxueliang
 * 
 */
public class SegmentAdapter extends BaseAdapter {

	@SuppressWarnings("rawtypes")
	private DMAdapter[] adapters;
	private int counts[];
	private int count;

	public SegmentAdapter(IViewContainer context, int[] itemResIds) {
		adapters = new DMAdapter[itemResIds.length];
		counts = new int[itemResIds.length];
		for (int i = 0; i < itemResIds.length; ++i) {
			adapters[i] = AdapterFactory.create(context, itemResIds[i]);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setData(int segment, List data,boolean notify) {
		count-=counts[segment];
		counts[segment] = data.size();
		adapters[segment].setData(data);
		count+=counts[segment];
		if(notify){
			notifyDataSetChanged();
		}
	}
	

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		// 查找哪个
		for (int i = 0, c = counts.length; i < c; ++i) {
			if (position < counts[i]) {
				return adapters[i].getItem(position);
			}
			position -= counts[i];
		}
		throw new RuntimeException("position out of bounds " + position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 查找哪个
		for (int i = 0, c = counts.length; i < c; ++i) {
			if (position < counts[i]) {
				return adapters[i].getView(position, convertView, parent);
			}
			position -= counts[i];
		}
		throw new RuntimeException("position out of bounds " + position);
	}

}
