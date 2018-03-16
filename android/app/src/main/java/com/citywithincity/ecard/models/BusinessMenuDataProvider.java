package com.citywithincity.ecard.models;

import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.models.AbsDataProvider;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.widget.data.MenuData;

import java.util.ArrayList;
import java.util.List;

public class BusinessMenuDataProvider extends AbsDataProvider<List<List<MenuData>>> implements IListRequsetResult<List<MenuData>>{
	

	private Object defaultData = "";
	private int defaultDistance = 0;
	@Override
	public void load() {
		MenuParser menuParser  = MenuParser.getMenuParser(defaultData,this);
		IArrayJsonTask<MenuData> task = JsonTaskManager
				.getInstance()
				.createArrayJsonTask("bs_type",CachePolicy.CachePolicy_TimeLimit,false);
				task.setParser(menuParser).setOnParseListener(menuParser)
				.setListener(menuParser)
				.execute();
	}
	
	/**
	 * 
	 */
	public void setDefaultData(Object path,int distance){
		this.defaultData = path;
		this.defaultDistance = distance;
	}



	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		// TODO Auto-generated method stub
		
	}
	
	private List<List<MenuData>> listList;
	
	@Override
	public void onRequestSuccess(List<MenuData> result, boolean isLastPage) {
		
	/*	List<MenuData> list = new ArrayList<MenuData>();
		for (MenuData menuData : result) {
			if (menuData.parentId != null) {
				if (((Integer)menuData.parentId)==0) {
					list.add(menuData);
				}
			}
			
		}
		for (MenuData menuData : result) {
			for (MenuData data : list) {
				if (menuData.parentId != null) {
					if (((Integer)menuData.parentId)==((Integer)data.data)) {
						if (data.children == null) {
							data.children = new ArrayList<MenuData>();
						}
						data.children.add(menuData);
					}
				}
			}
		}
		
		
		list.add(0, createAll());
		*/
		listList = new ArrayList<List<MenuData>>();
		listList.add(result);
		listList.add(getDefaultDistanceData(this.defaultDistance));
		listener.onReceive(listList);
	}
	

	
	public static List<MenuData> getDefaultDistanceData(int defaultDistance){
		List<MenuData> list = new ArrayList<MenuData>();
		//全城
		MenuData data = new MenuData();
		data.label = "1千米";
		data.data = 1000;
		list.add(data);
		
		
		data = new MenuData();
		data.label = "3千米";
		data.data = 3000;
		list.add(data);
		
		data = new MenuData();
		data.label = "5千米";
		data.data = 5000;
		list.add(data);
		
		data = new MenuData();
		data.label = "10千米";
		data.data = 10000;
		list.add(data);
		
		
		data = new MenuData();
		data.label = "全城";
		data.data = 0;
		list.add(data);
		
		for(int i=0, count = list.size(); i < count; ++i){
			data  = list.get(i);
			if(data.data.equals(defaultDistance)){
				data.selected = true;
				break;
			}
		}
		
		return list;
	}

}
