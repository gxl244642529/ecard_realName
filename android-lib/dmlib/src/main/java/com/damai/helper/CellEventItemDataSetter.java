package com.damai.helper;

import java.util.List;

import android.util.Log;
import android.view.View;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.note.ItemEventViewInfo;

public class CellEventItemDataSetter<T> extends CellDataSetter<T>{

	private ItemEventViewInfo[] infos;
	
	public CellEventItemDataSetter(IViewContainer context,int resId,ItemEventViewInfo[] infos){
		super(context.getActivity(), resId);
		this.infos = infos;
	}
	
	protected void createViewInfos(Object data,View view,List<IViewInfo> list){
		super.createViewInfos(data, view, list);
		for (ItemEventViewInfo viewInfo : infos) {
			View subView = view.findViewById(viewInfo.getId());
			if(subView!=null){
				list.add(viewInfo);
			}else{
				//找不到
				Log.w("WARNING", "Can not find view " + viewInfo.getId() );
			}
			
		}
	}
	

}
