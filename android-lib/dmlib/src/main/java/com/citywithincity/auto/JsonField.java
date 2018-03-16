package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JsonField {
	
	/**
	 * json数据中的名字
	 * @return
	 */
	String name() default "";
	
	/**
	 * 对应的json数据字段是否需要转成大写
	 * @return
	 */
	boolean upper() default true;
	
	/**
	 * 是否做has判断
	 * @return
	 */
	boolean check() default false;
	
	/**
	 * 是否是图片，前面需要加上前缀的
	 * @return
	 */
	boolean img() default false;
}
