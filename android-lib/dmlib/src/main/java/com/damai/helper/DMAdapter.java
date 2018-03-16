package com.damai.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.interfaces.IAdapterListener;

public class DMAdapter<T> extends BaseAdapter implements IDestroyable {
	
	
	/**
	 * 数据
	 */
	public List<T> dataList;

	/**
	 * itemview的资源id
	 */
	protected int viewResID;

	private IAdapterListener<T> listener;
	/**
	 * 用于创建子view
	 */
	private LayoutInflater layoutInflater;
	
	public DMAdapter(Context context,int itemResId,IAdapterListener<T> listener){
		layoutInflater = LayoutInflater.from(context); // ((Activity)context).getLayoutInflater();
		this.listener = listener;
		this.viewResID = itemResId;
		dataList = new ArrayList<T>();
	}
	
	/**
	 * 需要在activity或者fragment的onDestroy中调用
	 */
	@Override
	public void destroy() {
		if(listener!=null){
			/**
			 * 这里判断下listener是否是IDestroyable的
			 */
			if(listener instanceof IDestroyable){
				((IDestroyable)listener).destroy();
			}
			listener = null;
		}
		
		dataList = null;
		layoutInflater = null;
	}
	

	public void setItemRes(int res) {
		viewResID = res;
	}
	
	public void setListener(IAdapterListener<T> listener){
		this.listener = listener;
	}
	
	public void remove(T data) {
		dataList.remove(data);
		notifyDataSetChanged();
	}

	
	public List<T> getData() {
		return dataList;
	}

	public void setData(List<T> dataSource) {
		dataList = dataSource;
		notifyDataSetChanged();
	}
	
	public void appendData(List<T> dataSource){
		dataList.addAll(dataSource);
		notifyDataSetChanged();
	}

	/**
	 * 增加
	 * 
	 * @param data
	 */
	public void add(T data) {
		dataList.add(data);
		notifyDataSetChanged();
	}

	/**
	 * 插入数据
	 * @param index
	 * @param data
	 */
	public void insert(int index,T data){
		dataList.add(index,data);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return dataList==null ? 0 : dataList.size();
	}

	@Override
	public T getItem(int arg0) {
		return dataList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(viewResID, null);
		}
		T data = dataList.get(position);
		listener.onInitializeView(convertView, data, position);
		return convertView;
	}

	
}
