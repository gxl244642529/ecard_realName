package com.damai.note;

import java.lang.reflect.Method;
import java.util.List;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.helper.a.Event;
import com.damai.note.event.OnClickEvent;
import com.damai.util.StrKit;

public class EventParser implements IMethodParser {

	@Override
	public void getMethodInfo(Method method, IViewContainer observer,List<ClsInfo> list) {
		if(!method.isAnnotationPresent(Event.class))return;
		Event event = method.getAnnotation(Event.class);
		//名称
		String name = method.getName();
		if(name.startsWith("on")){
			name = StrKit.firstCharToLowerCase(name.substring(2));
		}
		int id = observer.getViewId(name);
		if(id==0){
			System.err.println("找不到视图 R.id." + name);
			return;
		}
		switch (event.type()) {
		case Event.Event_Click:
			list.add(new OnClickEvent(method,id,event.confirm(),event.requreLogin()));
			return;
		case Event.Event_CheckChange:
			
			break;
		default:
			
			break;
		}
	}

	@Override
	public void beginParse(IViewContainer observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endParse(IViewContainer observer) {
		// TODO Auto-generated method stub
		
	}


}
