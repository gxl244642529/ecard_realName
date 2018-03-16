package com.citywithincity.auto;

/**
 * 表示本类将要发出的通知
 * @author randy
 *
 */
public @interface NotificationList {
	Notification[] value();
	
	/**
	 * 通知的名称和类型
	 * @author randy
	 *
	 */
	public @interface Notification{
		/**
		 * 名称
		 * @return
		 */
		String name();
		/**
		 * 通知的类型
		 * @return
		 */
		Class<?>[] type();
		
		/**
		 * 说明文字
		 * @return
		 */
		String description();
	}
}
