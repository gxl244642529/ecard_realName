package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.citywithincity.models.cache.CachePolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiArray {
	public static final String[] DEFAULT_PARAM = new String[]{"position"};
	
	/**
	 * api
	 * @return
	 */
	String api();
	
	/**
	 * 
	 * @return
	 */
	CachePolicy cachePolicy() default CachePolicy.CachePolicy_TimeLimit;
	

	
	/**
	 * 是否私有数据 （需要登录）
	 * @return
	 */
	boolean isPrivate() default false;
	
	/**
	 * 参数名称
	 * @return
	 */
	String[] params() default {};
	
	/**
	 * 实体类
	 * @return
	 */
	Class<?> clazz();
	
	/**
	 * 是否是分页的
	 * @return
	 */
	boolean paged() default true;
	
	String waitingMessage() default "";
	
	
	int factory() default 0;
}
