package com.damai.helper.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
public @interface Singleton {
	public static final int ALL = 0;
	
	/**
	 * 在第一个引用的activity finish之后，就销毁
	 */
	public static final int DESTROY_WHEN_FINISH_FIRST_ACTIVITY = 1;
	
	
	int scope() default DESTROY_WHEN_FINISH_FIRST_ACTIVITY;
}
