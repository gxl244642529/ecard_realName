package com.damai.note;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.dmlib.LibBuildConfig;
import com.damai.helper.a.ItemEvent;
import com.damai.util.StrKit;

public class ItemEventParser implements IMethodParser {
	private static final String EVENT_NAME_ON_ITEM = "onItem";
	
	private List<ItemEventViewInfo> list;
	
	public ItemEventViewInfo[] getItemEventViewInfos(Class<?> clazz){
		return itemEventMap.get(clazz);
	}
	/**
	 * 
	 */
	@SuppressLint("UseSparseArrays")
	private static Map<Class<?>, ItemEventViewInfo[]> itemEventMap = new HashMap<Class<?>, ItemEventViewInfo[]>();
	
	@Override
	public void getMethodInfo(Method method,IViewContainer container,List<ClsInfo> extList) {
		 if(method.isAnnotationPresent(ItemEvent.class)){
			
			String name = method.getName();
			if(!name.startsWith(EVENT_NAME_ON_ITEM)){
				if(LibBuildConfig.DEBUG){
					throw new RuntimeException("ItemEvent method's name must start with \"onItem\"");
				}
			}
			ItemEvent itemEvent = method.getAnnotation(ItemEvent.class);
			//获取itemId
			name=name.substring(EVENT_NAME_ON_ITEM.length());
			name=StrKit.firstCharToLowerCase(name);
			ItemEventViewInfo itemEventViewInfo = new ItemEventViewInfo(method, container.getViewId(name),itemEvent.confrim());
			itemEventViewInfo.method = method;

			list.add(itemEventViewInfo);
			extList.add( itemEventViewInfo);
		}
	}

	@Override
	public void beginParse(IViewContainer observer) {
		list = new LinkedList<ItemEventViewInfo>();
	}

	@Override
	public void endParse(IViewContainer observer) {
		if(list.size()>0){
			ItemEventViewInfo[] itemEventViewInfoArray = new ItemEventViewInfo[list.size()];
			list.toArray(itemEventViewInfoArray);
			itemEventMap.put(observer.getClass(), itemEventViewInfoArray);
		}
		
	}

}
