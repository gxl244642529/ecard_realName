package com.citywithincity.ecard.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.citywithincity.utils.ViewUtil;

import java.util.List;


public class ListGridAdapter<T> extends BaseAdapter implements IDestroyable, OnClickListener {

	
	
	//具体
	private int itemResID;
	
	private int columns;
	
	private LayoutInflater layoutInflater;
	
	private int count=0;
	private int total;
	
	private List<T> dataList;
	
	private int avgWidth;
	IOnItemClickListener<T> listener;
	
	
	/**
	 * 监听
	 */
	protected IListDataProviderListener<T> _listener;
	
	public ListGridAdapter(int columns,LayoutInflater layotuInflater,int itemResID,
			IListDataProviderListener<T> listener){
		this.columns  =columns;
		this.itemResID = itemResID;
		this.layoutInflater = layotuInflater;
		_listener = listener;
		
		avgWidth= ViewUtil.screenWidth/columns;
	}
	
	public void setOnItemClickListener(IOnItemClickListener<T> listener){
		this.listener = listener;
	}
	
	public int getTotal(){
		return total;
	}
	
	public void setData(List<T> data,boolean addToEnd){
		if(addToEnd){
			dataList.addAll(data);
		}else{
			dataList = data;
		}
		total = dataList.size();
		count = total / columns;
		if(total%columns>0){
			count += 1;
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position * columns);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout viewGroup = null;
		if(convertView==null){
			viewGroup = (LinearLayout)layoutInflater.inflate(R.layout.item_list_grid, null);
			convertView = viewGroup;
			
			for(int i=0; i < columns; ++i){
				LinearLayout layout = (LinearLayout)viewGroup.getChildAt(i);
				LayoutParams lp = (LayoutParams)layout.getLayoutParams();
				lp.width = avgWidth;
				layout.setLayoutParams(lp);
			}
		}else{
			viewGroup = (LinearLayout)convertView;
		}
		
		//增加child
		for(int i=0; i < columns; ++i){
			LinearLayout layout = (LinearLayout)viewGroup.getChildAt(i);
			
			if(layout.getChildCount()==0){
				View childView = layoutInflater.inflate(itemResID, null);
				layout.addView(childView);
				childView.setOnClickListener(this);
			}
		}
		
		for(int i=0 ; i < columns; ++i){
			int index = position * columns + i;
			LinearLayout layout = (LinearLayout)viewGroup.getChildAt(i);
			if(index < total){
				T data = dataList.get(index);
				layout.getChildAt(0).setVisibility(View.VISIBLE);
				layout.getChildAt(0).setTag(data);
				_listener.onInitializeView(layout.getChildAt(0), data, position);
			}else{
				layout.getChildAt(0).setVisibility(View.GONE);
			}
			
		}
		
		
		return convertView;
	}

	@Override
	public void onClick(View v) {
		@SuppressWarnings("unchecked")
		T data = (T)v.getTag();
		if(listener!=null){
			listener.onItemClick(null,data,0);
		}
	}

	@Override
	public void destroy() {
		listener =  null;
		if(this.dataList!=null){
			this.dataList.clear();
			dataList=null;
		}
		layoutInflater = null;
			
	}

}
