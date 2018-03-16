package com.citywithincity.models;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AbsListAdapter extends BaseAdapter {
	/**
	 * 数据
	 */
	public List<?> dataList;

	// 子view 资源id
	protected int viewResID;

	/**
	 * 用于创建子view
	 */
	private final LayoutInflater layoutInflater;
	

	
	@SuppressWarnings("rawtypes")
	public AbsListAdapter(Context context,int itemViewResID){
		layoutInflater = LayoutInflater.from(context);
		this.viewResID = itemViewResID;
		dataList = new ArrayList();
	}

	public void setData(List<?> list){
		this.dataList = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dataList.get(arg0);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = layoutInflater.inflate(viewResID, null);
		}
		
		initView(convertView, dataList.get(position));
		return convertView;
	}
	
	protected abstract void initView(View view,Object data);
}
