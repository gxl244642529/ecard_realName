package com.citywithincity.mvc;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.interfaces.IDestroyable;

public class Model implements IDestroyable {
	
	public Model(){
		Notifier.register(this);
	}
	
	/**
	 * 发送通知
	 * @param notification
	 * @param args
	 */
	public void sendNotification(String notification,Object...args){
		Notifier.notifyObservers(notification, args);
	}

	@Override
	public void destroy() {
		Notifier.unregister(this);
	}
}
