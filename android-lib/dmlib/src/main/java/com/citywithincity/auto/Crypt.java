package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据绑定
 * @author Randy
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Crypt {
	public static final int NONE=0;
	public static final int UPLOAD = 1;
	public static final int DOWNLOAD = 2;

	public static final int BOTH = 3;
	
	int value() default BOTH;
}
