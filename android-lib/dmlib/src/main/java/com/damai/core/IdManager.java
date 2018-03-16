package com.damai.core;

import android.content.Context;

import com.citywithincity.interfaces.IViewContainer;

public class IdManager {
	private static IdReflect defaultIdReflect;

	public static IdReflect getDefault() {
		return defaultIdReflect;
	}
	
	public static void setDefault(IdReflect idReflect){
		defaultIdReflect = idReflect;
	}
	
	public static String idToString(Context context,int id){
		
		return ((IViewContainer)context).idToString(id);
		
	}
	
}
