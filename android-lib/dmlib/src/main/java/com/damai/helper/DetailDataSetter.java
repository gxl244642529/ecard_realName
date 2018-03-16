package com.damai.helper;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IDestroyable;

public class DetailDataSetter extends BaseDataSetter implements IDestroyable {
	private ViewGroup view;
	private String name;
	private static Map<String, IViewInfo[]> cachedViewInfos = new HashMap<String, IViewInfo[]>();
	/**
	 * 创建详情页面数据设置器
	 * @param context
	 * @param name
	 */
	public DetailDataSetter(Context context,ViewGroup view,String name) {
		super(context);
		this.view = view;
		this.name = name;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		this.view = null;
	}
	
	public void setInitData(Object data) {
		if(data!=null){
			String key = name+ data.getClass().getName() + "init";
			setData(data, key);
		}

	}
	
	private void setData(Object data,String key){
		IViewInfo[] viewInfos = cachedViewInfos.get(key);
		if(viewInfos==null){
			viewInfos = createViewInfos(view, data);
			cachedViewInfos.put(key, viewInfos);
		}
		DataSetter[] setters = createDataSetters(view, viewInfos);
		for (DataSetter dmDataSetter : setters) {
			dmDataSetter.setValue(data);
		}
	}

	/**
	 * 
	 * @param data
	 */
	public void setLoadedData(Object data){
		String key = name+data.getClass().getName() + "Loaded";
		setData(data, key);
	}
}
