package com.damai.note;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.http.api.a.JobError;
import com.damai.http.api.a.JobMessage;
import com.damai.http.api.a.JobSuccess;
import com.damai.http.api.a.Notification;


public class NotificationParser implements IMethodParser {

	private Map<String,List<NotifyInfo>> notificationMap = new HashMap<String,List<NotifyInfo>>();
	
	
	public class NotifyInfo extends MethodInfo{
		public NotifyInfo(Method method) {
			super(method);
		}

		/**
		 * 通知名称
		 */
		private String notification;

		public String getNotification() {
			return notification;
		}

		public void setNotification(String notification) {
			this.notification = notification;
		}
		
		public void setTarget(IViewContainer observer) {
			super.setTarget(observer);
			//这里需要进行注册
			List<NotifyInfo> list = notificationMap.get(notification);
			if(list==null){
				list = new LinkedList<NotifyInfo>();
				notificationMap.put(notification, list);
			}
			list.add(this);
		}

		@Override
		public void clearObserver() {
			observer = null;
			List<NotifyInfo> list = notificationMap.get(notification);
			if(list!=null){
				//这里清空对象
				list.remove(this);
				if(list.size()==0){
					notificationMap.remove(notification);
				}
			}
			
		}

	}
	
	
	

	/**
	 * 通知观察者
	 * @param key
	 * @param data
	 */
	public boolean notifyObservers(String key,Object data){
		List<NotifyInfo> list = notificationMap.get(key);
		if(list!=null){
			for (NotifyInfo notifyInfo : list) {
				notifyInfo.invoke(data);
			}
			return true;
		}
		return false;
	}

	@Override
	public void getMethodInfo(Method method,IViewContainer container,List<ClsInfo> list) {
		if (method.isAnnotationPresent(JobSuccess.class)) {
			JobSuccess m =  method.getAnnotation(JobSuccess.class);
			for (String noteName : m.value()) {
				NotifyInfo info = new NotifyInfo(method);
				info.setNotification("n_s_"+noteName);
				list.add(info);
			}
		}else if (method.isAnnotationPresent(JobError.class)) {
			JobError m =  method.getAnnotation(JobError.class);
			for (String noteName : m.value()) {
				NotifyInfo info = new NotifyInfo(method);
				info.setNotification("n_e_"+noteName);
				list.add(info);
			}
		}else if (method.isAnnotationPresent(JobMessage.class)) {
			JobMessage m =  method.getAnnotation(JobMessage.class);
			for (String noteName : m.value()) {
				NotifyInfo info = new NotifyInfo(method);
				info.setNotification("n_m_"+noteName);
				list.add(info);
			}
		}else if(method.isAnnotationPresent(Notification.class)){
			Notification m =  method.getAnnotation(Notification.class);
			for (String noteName : m.value()) {
				NotifyInfo info = new NotifyInfo(method);
				info.setNotification(noteName);
				list.add(info);
			}
		}else {
			return;
		}
		
	}
	
	@Override
	public void beginParse(IViewContainer observer) {
		
	}

	@Override
	public void endParse(IViewContainer observer) {
		
	}

	
	public static final String makeJobSuccessNotification(String id){
		return new StringBuilder("n_s_").append(id).toString();
	}
	
	public static final String makeJobErrorNotification(String id){
		return new StringBuilder("n_e_").append(id).toString();
	}
	
	public static final String makeJobMessageNotification(String id){
		return new StringBuilder("n_m_").append(id).toString();
	}
	
}
