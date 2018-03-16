package com.damai.helper;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.interfaces.IAdapterListener;

/**
 * 为listView的cell做数据设置
 * @author Randy
 *
 */
public class CellDataSetter<T> extends BaseDataSetter implements IDestroyable, IAdapterListener<T> {
	@SuppressLint("UseSparseArrays")
	private static Map<Integer, IViewInfo[]> map = new HashMap<Integer, IViewInfo[]>();
	private IViewInfo[] viewInfos;
	private Integer resId;
	
	public CellDataSetter(Context context,int resId){
		super(context);
		viewInfos = map.get(resId);
		this.resId = resId;
	}
	
	
	
	@Override
	public void destroy() {
		super.destroy();
		viewInfos = null;
	}
	
	@Override
	public void onInitializeView(View view, T data, int position) {
		if(viewInfos==null){
			viewInfos = createViewInfos(view, data);
			map.put(resId, viewInfos);
		}
		
		DataSetter[] setters = (DataSetter[])view.getTag();
		if(setters==null){
			setters = createDataSetters(view, viewInfos);
			view.setTag(setters);
		}
		for (DataSetter dmDataSetter : setters) {
			dmDataSetter.setValue(data);
		}
	}
	
	
	
	
	
	



	



}
