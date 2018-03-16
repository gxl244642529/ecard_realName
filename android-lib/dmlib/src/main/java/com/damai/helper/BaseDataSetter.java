package com.damai.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.dl.IHostActivity;
import com.damai.dl.PluginInfo;
import com.damai.dmlib.LibBuildConfig;


public class BaseDataSetter implements IDestroyable {
	
	private Resources resources;
	private String packageName;
	public BaseDataSetter(Context context){
		if(context instanceof IHostActivity){
			PluginInfo a = ((IHostActivity)context).getPluginInfo();
			resources = a.getResources();
			packageName = a.getPackageName();
		}else{
			resources = context.getResources();
			packageName = context.getPackageName();
		}
		
	}
	@Override
	public void destroy() {
		resources = null;
	}
	
	protected IViewInfo[] createViewInfos(View view,Object data){
		//找到viewInfo
		List<IViewInfo> list = new LinkedList<IViewInfo>();
		createViewInfos(data,view,list);
		IViewInfo[] viewInfos = new IViewInfo[list.size()];
		list.toArray(viewInfos);
		if(LibBuildConfig.DEBUG){
			System.out.println("ViewInfo created ===================");
			for (IViewInfo viewInfo : viewInfos) {
				System.out.println("ViewInfo " + viewInfo.getName());
			}
		}
		return viewInfos;
	}
	
	protected static DataSetter[] createDataSetters(View view,IViewInfo[] viewInfos){
		DataSetter[] setters = new DataSetter[viewInfos.length];
		int i=0;
		for (IViewInfo info : viewInfos) {
			setters[i++] = info.createSetter(view);
		}
		return setters;
	}
	
	protected void createViewInfos(Object data,View view,List<IViewInfo> list){
		if(data instanceof JSONObject){
			JSONObject json = (JSONObject)data;
			JSONArray array = json.names();
			for(int i =0 , count=array.length(); i < count; ++i){
				try {
					String name = array.getString(i);
					create( name,json.get(name),view, list);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		}else{
			Class<?> clazz = data.getClass();
			for (Field field : clazz.getFields()) {
				create(field, view,list);
			}
			for(Method method : clazz.getMethods()){
				//getter
				if(Modifier.isStatic(method.getModifiers())){
					continue;
				}
				create(method,  view, list);

				
			}
		}
	}
	
	private void create(String name, Object value,View view, List<IViewInfo> list) {
		View subView = findView(name,  view);
		if(subView!=null){
			ViewInfo info = ViewInfo.create(name,value.getClass(), subView);
			if(info!=null){
				list.add(info);
			}
		}
	}

	
	private void create(Field field, View view, List<IViewInfo> list) {
		View subView = findView(field.getName(),  view);
		if(subView!=null){
			ViewInfo info = ViewInfo.create(field,subView);
			if(info!=null){
				list.add(info);
			}
		}
	}


	private void create(Method method,View view,List<IViewInfo> list){
		ViewInfo info = ViewInfo.create(view,method,this);
		if(info!=null){
			list.add(info);
		}
	}
	/**
	 * 
	 * @param name
	 * @param view
	 * @return
	 */
	protected View findView(String name, View view) {
		int id = resources.getIdentifier(name, "id", packageName);
		if(id>0){
			//如果有，则进行查找
			View subView = view.findViewById(id);
			return subView;
		}
		return null;
	}


}
