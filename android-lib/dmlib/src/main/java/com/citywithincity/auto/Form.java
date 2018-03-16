package com.citywithincity.auto;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.citywithincity.models.vos.Null;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Form {
	/**
	 * 表单元素数组
	 * @return
	 */
	FormElement[] forms() default {};
	/**
	 * 默认提交按钮
	 * @return
	 */
	int defaultButton();
	
	/**
	 * 布局文件
	 * @return
	 */
	int layout();
	
	Class<? extends Serializable> beanClass() default Null.class;
}
