package com.damai.helper.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Event {
	/**
	 * 适用于按钮
	 */
	public static final int Event_Click=1;
	/**
	 * 适用于checkbox
	 */
	public static final int Event_CheckChange=2;
	
	
	
	
	int type() default Event_Click;
	
	
	/**
	 * 是否提示
	 */
	String confirm() default "";
	
	/**
	 * 是否需要登录
	 */
	boolean requreLogin() default false;
	
}
