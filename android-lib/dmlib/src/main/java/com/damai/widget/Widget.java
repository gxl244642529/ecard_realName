package com.damai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * 思路：
 * 某一个widget如果作为视图的话，只能拥有一个特性，而java不支持继承，所以，在一个widget被初始化的时候，首先获取绑定的对应处理model
 * 则一切的特性，由处理model来进行处理，这样，一个widget可以拥有多种特性
 * 
 * 如：
 * 一个详情页，也同时可以是一个表单页
 * 
 * @author renxueliang
 *
 */
public class Widget {
	public static void widgetBind(Class<? extends View> clazz,Class<? extends Widget> wClass){
		
	}
	
	/**
	 * 当绑定的时候
	 * @param context
	 * @param attrs
	 */
	public void onBind(Context context, AttributeSet attrs){
		
	}
	
	
	/**
	 * 销毁
	 */
	public void onDestroy(){
		
	}
}
