package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * list、gridview的item事件处理
 * @author Randy
 *
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ItemEventHandler {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface ItemEventId{
		/**
		 * 视图id
		 * @return
		 */
		int id();
		
		/**
		 * 是否检查登录
		 * @return
		 */
		boolean checkLogin() default false;
	}
	
}
