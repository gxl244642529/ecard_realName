package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.citywithincity.models.cache.CachePolicy;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiDetail {
	String api();
	CachePolicy cachePolicy() default CachePolicy.CachePolicy_TimeLimit;
	
	/**
	 * 详情一般为id类型
	 * @return
	 */
	String[] params() default {"id"};
	Class<?> clazz();
	
	String waitingMessage() default "";
	
	int factory() default 0;
}
