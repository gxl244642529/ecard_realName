package com.citywithincity.models;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.citywithincity.auto.ItemEventHandler;
import com.citywithincity.auto.tools.AutoCreator;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IItemEventHandlerContainer;
import com.citywithincity.interfaces.IItemEventHandlerGroup;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.utils.ViewUtil;

public class ListDataProvider<T> extends BaseAdapter implements IDestroyable {
	/**
	 * 数据
	 */
	public List<T> dataList;

	// 子view 资源id
	protected int viewResID;

	/**
	 * 用于创建子view
	 */
	private final LayoutInflater layoutInflater;

	/**
	 * 监听
	 */
	public IListDataProviderListener<T> listener;


	private final Context context;
	private IItemEventHandlerGroup itemEventHandler;
	private final Object itemEventHandlerTarget;

	public ListDataProvider(Context context, int itemViewResID,
			IListDataProviderListener<T> listener) {
		this.context = context;
		this.viewResID = itemViewResID;
		this.listener = listener;
		if(LibConfig.DEBUG){
			if(!(context instanceof Activity)){
				throw new Error("context不是activity");
			}
		}
		
		this.layoutInflater =  ((Activity)context).getLayoutInflater();
		
		if (context.getClass().isAnnotationPresent(ItemEventHandler.class)) {
			itemEventHandlerTarget = context;
		}else if(listener.getClass().isAnnotationPresent(ItemEventHandler.class)){
			itemEventHandlerTarget = listener;
		}else if(context instanceof IItemEventHandlerContainer){
			itemEventHandlerTarget = ((IItemEventHandlerContainer)context).getItemEventHandler();
		}else{
			itemEventHandlerTarget = null;
		}
		init();
	}
	
	public void setListener(IListDataProviderListener<T> listener){
		this.listener = listener;
	}
	
	
	public ListDataProvider(Context context){
		this.context = context;
		if(LibConfig.DEBUG){
			if(!(context instanceof Activity)){
				throw new Error("context不是activity");
			}
		}
		
		this.layoutInflater =  ((Activity)context).getLayoutInflater();
		if (context.getClass().isAnnotationPresent(ItemEventHandler.class)) {
			itemEventHandlerTarget = context;
		}else if(context instanceof IItemEventHandlerContainer){
			itemEventHandlerTarget = ((IItemEventHandlerContainer)context).getItemEventHandler();
		}else{
			itemEventHandlerTarget = null;
		}
		init();
	}
	
	public ListDataProvider(Context context,Object itemEventTarget){
		this.context = context;
		this.layoutInflater = LayoutInflater.from(context);
		itemEventHandlerTarget = itemEventTarget;
		init();
	}
	private void init() {
		this.dataList = new ArrayList<T>();
		if(itemEventHandlerTarget!=null){
			itemEventHandler=AutoCreator.createItemEventHandler(itemEventHandlerTarget);
		}
	}

	public void remove(T data) {
		dataList.remove(data);
		notifyDataSetChanged();
	}

	@Override
	public void destroy() {
		dataList = null;
		listener = null;
	}

	public void setData(T[] dataSource, boolean addToEnd) {
		if (!addToEnd) {
			dataList = new ArrayList<T>();
		}
		for (T data : dataSource) {
			dataList.add(data);
		}
		notifyDataSetChanged();
	}

	public List<T> getData() {
		return dataList;
	}

	public void setData(List<T> dataSource, boolean addToEnd) {
		if (addToEnd) {
			dataList.addAll(dataSource);
		} else
			dataList = dataSource;
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
		return dataList.size();
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(viewResID, null);
		}
		T data = dataList.get(position);
		// 设置数据
		ViewUtil.setBinddataViewValues(data, convertView,viewResID);
		// itemevent
		
		if(itemEventHandler!=null){
			itemEventHandler.setItemEvent(convertView, data);
		}
		if(listener!=null){
			listener.onInitializeView(convertView, data, position);
		}
		
		return convertView;
	}

	
	public void setItemRes(int res) {
		viewResID = res;
	}
	
	

}
