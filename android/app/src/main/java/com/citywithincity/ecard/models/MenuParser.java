package com.citywithincity.ecard.models;

import android.annotation.SuppressLint;

import com.citywithincity.interfaces.IJsonParser;
import com.citywithincity.interfaces.IJsonTaskListener;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.models.Observable;
import com.citywithincity.widget.data.MenuData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuParser extends Observable<IListRequsetResult<List<MenuData>>>  implements IJsonTaskListener<MenuData>,IJsonParser<MenuData>, IListRequsetResult<List<MenuData>>{

	private List<MenuData> menuList;
	private MenuData mainMenuData;
	private Map<Integer, MenuData> menuMap;
	
	//默认
	private Object defaultData;
	private int[] resIDs;
	
	@SuppressLint("UseSparseArrays")
	public MenuParser(){
		menuMap = new HashMap<Integer, MenuData>();
		menuList = new ArrayList<MenuData>();
	}
	
	public void setDefaultData(Object data){
		this.defaultData  =data;
	}
	
	
	public void setIconResIDS(int[] resIDS){
		this.resIDs = resIDS;
	}
	
	
	@Override
	public MenuData parseResult(JSONObject json) throws JSONException {
		int parent_id = json.getInt("PARENT_ID");
		if(parent_id==0){
			MenuData data = new MenuData();
			parse(json,data);
			data.index = menuList.size();
			menuMap.put(data.id, data);
			menuList.add(data);
			return data;
		}
		MenuData data = new MenuData();
		parse(json,data);
		MenuData mainMenuData = menuMap.get(parent_id);
		if(mainMenuData.children==null){
			mainMenuData.children=new ArrayList<MenuData>();
			MenuData nMenuData = new MenuData();
			nMenuData.label = "全部";
			nMenuData.topLabel = mainMenuData.label;
			nMenuData.data = mainMenuData.data;
			nMenuData.id = mainMenuData.id;
			mainMenuData.children.add(nMenuData);
		}
		data.index = mainMenuData.children.size();
		mainMenuData.children.add(data);
		
		return data;
	}
	
	protected void parse(JSONObject json,MenuData data) throws JSONException {
		data.label = json.getString("TITLE");
		data.data = json.getString("PATH");
		data.id = json.getInt("ID");
	}


	@Override
	public Object beforeParseData() {
		menuMap.clear();
		menuList.clear();
		
		if(mainMenuData==null){
			mainMenuData = new MenuData();
			mainMenuData.id = 0;
			mainMenuData.data = "";
			mainMenuData.label = "全部";
		}
		menuList.add(mainMenuData);
		
		
		return null;
	}
	
	
	private static MenuParser menuParser;

	public static MenuParser getMenuParser(Object defaultData,IListRequsetResult<List<MenuData>> listener){
		if(menuParser==null){
			menuParser = new MenuParser();
		}
		menuParser.setDefaultData(defaultData);
		menuParser.setListener(listener);
		return menuParser;
	}


	@Override
	public void afterParseData(List<MenuData> data, Object result) {
		menuMap.clear();
		int i=0;
		for (MenuData menuData : menuList) {
			if(resIDs!=null){
				menuData.imageRes = resIDs[i++];
			}
			menuData.selected = menuData.data.equals(defaultData);
		}
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		// TODO Auto-generated method stub
		listener.onRequestError(errMsg, isNetworkError);
	}

	@Override
	public void onRequestSuccess(List<MenuData> result, boolean isLastPage) {
		// TODO Auto-generated method stub
		listener.onRequestSuccess(menuList, true);
	}

}
