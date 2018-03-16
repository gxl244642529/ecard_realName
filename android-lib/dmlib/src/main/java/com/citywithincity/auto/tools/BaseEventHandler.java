package com.citywithincity.auto.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.interfaces.IEventHandler;
import com.citywithincity.interfaces.IJsonTaskManager;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.libs.LibConfig;

public class BaseEventHandler {
protected static IJsonTaskManager jsonTaskManager;
	
	public static void setJsonTaskManager(IJsonTaskManager value){
		jsonTaskManager = value;
	}
	public static Uri getResourceUri(Context context, int res) {
        try {
        Context packageContext = context.createPackageContext(context.getPackageName(),
                    Context.CONTEXT_RESTRICTED);
            Resources resources = packageContext.getResources();
            String appPkg = packageContext.getPackageName();
            String resPkg = resources.getResourcePackageName(res);
            String type = resources.getResourceTypeName(res);
            String name = resources.getResourceEntryName(res);


            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE);
            uriBuilder.encodedAuthority(appPkg);
            uriBuilder.appendEncodedPath(type);
            if (!appPkg.equals(resPkg)) {
                uriBuilder.appendEncodedPath(resPkg + ":" + name);
            } else {
                uriBuilder.appendEncodedPath(name);
            }
            return uriBuilder.build();
        
        } catch (Exception e) {
            return null;
        }
    }
	
	public static IEventHandler CREATOR = new IEventHandler(){

		@Override
		public void registerEvent(final IViewContainer parent) {
			// TODO Auto-generated method stub
			Class<?> clazz = parent.getClass();
			if (!clazz.isAnnotationPresent(EventHandler.class))
				return;
			
			List<Object[]> objs = getViewEventMethod(clazz);
			for (Object[] objects : objs) {
				final Method method = (Method) objects[0];
				int buttonId = (Integer) objects[1];
				final boolean checkLogin = (Boolean) objects[2];
				View child = parent.findViewById(buttonId);
				if(LibConfig.DEBUG){
					if (child == null) {
						throw new Error(String.format("没有找到视图%s",getResourceUri(parent.getActivity(),buttonId)) );
					}
				}
				
				if (child instanceof RadioGroup) {
					if (LibConfig.DEBUG) {
						if (!method.getName().startsWith("onRadioGroup")) {
							throw new Error("radioGroup事件方法名称应该以onRadioGroup开头");
						}
					}
					((RadioGroup) child).setOnCheckedChangeListener(new OnCheckedChangeListener() {
								@Override
								public void onCheckedChanged(RadioGroup group,
										int checkedId) {
									try {
										method.invoke(parent, checkedId);
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
							});
				} else {
					if (LibConfig.DEBUG) {
						if (!method.getName().startsWith("onBtn")) {
							throw new Error("点击事件方法名称应该以onBtn开头");
						}
					}
					child.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (checkLogin && !jsonTaskManager.isLogin()) {
								jsonTaskManager.requestLogin(parent.getActivity());
								return;
							}
							try {
								method.invoke(parent);
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
					});
				}
			}

		}
		
	};
	
	

	/**
	 * viewevent
	 */
	protected static Map<Class<?>, List<Object[]>> viewEventMethodMap = new LinkedHashMap<Class<?>, List<Object[]>>();

	protected static List<Object[]> getViewEventMethod(Class<?> clazz) {

		// 查找缓存
		List<Object[]> objs = viewEventMethodMap.get(clazz);
		if (objs == null) {
			objs = new ArrayList<Object[]>();
			viewEventMethodMap.put(clazz, objs);
			// 查找所有支持时间的method和viewid,找出对应关系

			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.isAnnotationPresent(EventHandlerId.class)) {
					EventHandlerId buttonId = method
							.getAnnotation(EventHandlerId.class);
					objs.add(new Object[] { method, buttonId.id(),
							buttonId.checkLogin() });
				}
			}

		}
		return objs;
	}

}
