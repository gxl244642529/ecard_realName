package com.citywithincity.utils;


import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

public class SizeUtil {
	
	public static class Size{
		public Size(){
			
		}
		
		public Size(int width,int height){
			this.width = width;
			this.height = height;
		}
		
		public Size(float width,float height){
			this.width = Math.round(width);
			this.height = Math.round(height);
		}
		
		
		public int width;
		public int height;
		
		
	}
	
	
	public static Point measureView(View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		view.measure(w, h); 
		int height =view.getMeasuredHeight(); 
		int width =view.getMeasuredWidth(); 
		return new Point(width, height);
	}
	
	/**
	 * 设置视图大小以保持比例
	 * @param context
	 * @param view
	 * @param width
	 * @param height
	 */
	public static void setViewSize(View view,int currentWidth,int currentHegiht,int destWidth){

		//计算大小
		float destHeight  = (float)currentHegiht* destWidth / currentWidth   ;
		ViewGroup.LayoutParams lParams = (ViewGroup.LayoutParams)view.getLayoutParams();
		lParams.height = (int)destHeight;
		view.setLayoutParams(lParams);
		
	}
	
	/**
	 * 获取目标在容器中的大小（目标在容器中间，显示全部，如目标比容器小，则等比放大到目标宽度=容器宽度或者目标高度=容器高度
	 * @param containerWith
	 * @param containerHeight
	 * @param targetWidth
	 * @param targetHeight
	 * @return
	 */
	public static Size getCenterInsideSize(int containerWith,int containerHeight,int targetWidth,int targetHeight){
		//计算大小
		float dx = (float)containerWith / targetWidth;
		float dy = (float)containerHeight / targetHeight;
		dx = Math.min(dx, dy);
		
		return new Size(targetWidth * dx,targetHeight * dx);
	}
	
	
	public static Rect getCenterInsideRect(int containerWith,int containerHeight,int targetWidth,int targetHeight){
		//计算大小
		float dx = (float)containerWith / targetWidth;
		float dy = (float)containerHeight / targetHeight;
		dx = Math.min(dx, dy);
		
		int w = (int) (targetWidth * dx);
		int h = (int) (targetHeight * dy);
		
		int left = (int) ((containerWith - w)/2);
		int top = (int) ((containerHeight - h)/2);
		
		
		return new Rect(left, top, left + w, h);
		
	}
}
