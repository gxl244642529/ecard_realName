package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.citywithincity.models.cache.CachePolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiObject {
	String api();
	CachePolicy cachePolicy() default CachePolicy.CachePolity_NoCache;
	
	String[] params();
	Class<?> clazz();
	
	String waitingMessage() default "";
	int factory() default 0;
}
