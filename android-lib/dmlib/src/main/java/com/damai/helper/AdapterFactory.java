package com.damai.helper;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.dl.IHostBase;
import com.damai.note.ClassParser;
import com.damai.note.ItemEventViewInfo;

public class AdapterFactory {

	public static <T> DMAdapter<T> create(IViewContainer context,int itemResId){
		CellDataSetter<T> dataSetter = createCellDataSetter(context,itemResId);
		return new DMAdapter<T>(context.getActivity(), itemResId, dataSetter);
	}
	
	
	public static <T> CellDataSetter<T> createCellDataSetter(IViewContainer context,int itemResId){
		Class<?> targetClass;
		if(context instanceof IHostBase){
			targetClass = ((IHostBase)context).getPlugin().getClass();
		}else{
			targetClass = context.getClass();
		}
		ItemEventViewInfo[] infos = ClassParser.getItemEventViewInfos(targetClass);
		CellDataSetter<T> dataSetter;
		if(infos==null){
			dataSetter = new CellDataSetter<T>(context.getActivity(), itemResId);
		}else{
			dataSetter = new CellEventItemDataSetter<T>(context, itemResId,infos);
		}
		return dataSetter;
	}
}
