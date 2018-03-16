package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于数据绑定
 * 
 * 可用于getter或者public 属性
 * @author Randy
 *
 */


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
public @interface ViewId {
	
	/**
	 * 返回text
	 */
	public static final int TYPE_VALUE = 0;//值
	
	/**
	 * 返回View.GONE View.VISIBLE View.INVISIBLE
	 */
	public static final int TYPE_VISIBILITY = 1;//可见性
	/**
	 * 返回true false
	 */
	public static final int TYPE_ENABLE = 2;//可用性
	
	/**
	 * 返回int
	 */
	public static final int TYPE_BACKGROUND_RESOURCE = 3;//背景资源
	
	/**
	 * 返回int
	 */
	public static final int TYPE_COLOR = 4;//颜色
	
	/**
	 * 返回int
	 */
	public static final int TYPE_TEXT_COLOR = 5;//字体颜色
	
	
	/**
	 * 
	 * @return
	 */
	int id();
	
	/**
	 * 
	 * @return
	 */
	int type() default TYPE_VALUE;
}
