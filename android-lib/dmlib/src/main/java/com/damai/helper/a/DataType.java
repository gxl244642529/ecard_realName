package com.damai.helper.a;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
public @interface DataType {
	//背景颜色int,实际rgb
	public static final int BG_COLOR = 0;
	//可见性 View.GONE|View.VISIBLE|View.INVISIBLE
	public static final int VISIBLE = 1;
	//背景颜色，返回一个resource id
	public static final int BG_RES = 2;
	//用于imageView
	public static final int SRC = 3;
	//用于hint
	public static final int HINT = 4;
	
	public int value();
}
