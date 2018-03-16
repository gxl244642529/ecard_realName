package com.citywithincity.auto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FormElement {
	/**
	 * 是否必须
	 */
	public static final int REQUIRED = 0x1;
	/**
	 * 是否email
	 */
	public static final int EMAIL = 0x2;
	/**
	 * 是否手机号码
	 */
	public static final int MOBILE = 0x4;
	/**
	 * 是否账号（字母开头、字母数字下划线）
	 */
	public static final int ACCOUNT = 0x8;
	/**
	 * 是否密码
	 */
	public static final int PASSWORD = 0x10;
	/**
	 * 是否是整数
	 */
	public static final int INTEGER = 0x20;
	/**
	 * 是否是数字
	 */
	public static final int NUMBER = 0x40;
	/**
	 * 是否电话
	 */
	public static final int TEL = 0x80;
	
	/**
	 * 是否身份证
	 */
	public static final int IDCARD = 0x100;
	
	/**
	 * 是否邮编
	 */
	public static final int ZIPCODE = 0x200;
	
	/**
	 * view id
	 * @return
	 */
	int id();
	
	/**
	 * 验证规则 如 REQUIRED|EMAIL
	 * @return
	 */
	int validate() default 0;
	/**
	 * 表单元素名称
	 * @return
	 */
	String name();
	
	/**
	 * 表单元素说明
	 * @return
	 */
	String description() default "";
	
	/**
	 * 额外验证规则
	 * 
	 * 长度:len[6-12]
	 * 一致:equalTo[name]
	 * 大于gt[name]
	 * 小于lt[name]
	 * 
	 * 
	 * @return
	 */
	String extra() default "";
	
	
	Class<?> clazz() default String.class;
	
}
