package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.citywithincity.models.cache.CachePolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiValue {
	/**
	 * 
	 * @return
	 */
	String api();
	/**
	 * 
	 * @return
	 */
	CachePolicy cachePolicy() default CachePolicy.CachePolity_NoCache;
	/**
	 * 参数
	 * @return
	 */
	String[] params();
	
	String waitingMessage() default "";
	int factory() default 0;
}
