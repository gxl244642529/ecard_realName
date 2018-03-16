package com.citywithincity.auto.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.citywithincity.auto.ItemEventHandler.ItemEventId;
import com.citywithincity.auto.tools.AutoCreator.IItemEventHandlerCreator;
import com.citywithincity.interfaces.IItemEventHandler;
import com.citywithincity.interfaces.IItemEventHandlerGroup;

public class BaseItemEventHandler implements IItemEventHandler {

	protected Method method;
	protected Object target;

	public void init(Method method, Object target) {
		this.method = method;
		this.target = target;
	}

	protected Object data;

	@Override
	public void setData(Object data) {
		this.data = data;
	}

	public static class OnClickItemEventHandler extends BaseItemEventHandler
			implements OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				method.invoke(target, data);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}

	public static class OnCheckItemEventHandler extends BaseItemEventHandler
			implements android.widget.CompoundButton.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			try {
				method.invoke(target, data, buttonView.isChecked());
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static class OnCheckChangeItemEventHandler extends
			BaseItemEventHandler implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			try {
				method.invoke(target, data, checkedId);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	
	protected static class ItemEventHanderGroup implements IItemEventHandlerGroup{

		private final Object target;
		private Map<View, IItemEventHandler[]> map;
		public ItemEventHanderGroup(Object target){
			this.target = target;
			map = new LinkedHashMap<View, IItemEventHandler[]>();
		}
		
		@Override
		public void setItemEvent(View view, Object data) {
			// TODO Auto-generated method stub
			IItemEventHandler[] handlers = map.get(view);
			if (handlers == null) {
				// 创建
				handlers = getItemEventHandlers(target, view);
				map.put(view, handlers);
			}
			for (IItemEventHandler handler : handlers) {
				handler.setData(data);
			}
		}
		
	}
	
	public static IItemEventHandlerCreator CREATOR = new IItemEventHandlerCreator() {
		
		@Override
		public IItemEventHandlerGroup createItemEventHandlerGroup(Object target) {
			return new ItemEventHanderGroup(target);
		}
	};
	

	protected static IItemEventHandler[] getItemEventHandlers(Object target,
			View view) {
		Method[] methods = target.getClass().getMethods();
		List<BaseItemEventHandler> list = new ArrayList<BaseItemEventHandler>();
		BaseItemEventHandler handler = null;
		for (Method method : methods) {
			if (method.isAnnotationPresent(ItemEventId.class)) {
				ItemEventId element = method.getAnnotation(ItemEventId.class);
				View v = view.findViewById(element.id());
				// 设置
				if (v instanceof RadioGroup) {
					// 创建事件
					OnCheckChangeItemEventHandler changeItemEventHandler = new OnCheckChangeItemEventHandler();
					changeItemEventHandler.init(method, target);
					handler = changeItemEventHandler;
					((RadioGroup) v)
							.setOnCheckedChangeListener(changeItemEventHandler);
				} else if (v instanceof CompoundButton) {

					OnCheckItemEventHandler checkItemEventHandler = new OnCheckItemEventHandler();
					checkItemEventHandler.init(method, target);
					handler = checkItemEventHandler;
					((CompoundButton) v)
							.setOnCheckedChangeListener(checkItemEventHandler);

				} else  if(v!=null){
					OnClickItemEventHandler changeItemEventHandler = new OnClickItemEventHandler();
					changeItemEventHandler.init(method, target);
					handler = changeItemEventHandler;
					v.setOnClickListener(changeItemEventHandler);
				}else{
					continue;
				}
				list.add(handler);
			}
		}
		IItemEventHandler[] array = new IItemEventHandler[list.size()];
		list.toArray(array);
		return array;
	}

}
