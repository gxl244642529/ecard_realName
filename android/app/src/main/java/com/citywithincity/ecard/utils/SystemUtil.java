package com.citywithincity.ecard.utils;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.citywithincity.ecard.R;
import com.citywithincity.utils.SDCardUtil;

import java.io.File;
import java.io.IOException;

public class SystemUtil {

	
	protected static int _requestID;
	protected static int densityDpi;


	
	public static void showWaiting(Context context,View rotateView){
		rotateView.setVisibility(View.VISIBLE);
		Animation operatingAnim = AnimationUtils.loadAnimation(context, R.anim.map_rotate);  
		LinearInterpolator lin = new LinearInterpolator();  
		operatingAnim.setInterpolator(lin);
		if (operatingAnim != null) {  
		    rotateView.startAnimation(operatingAnim);  
		} 
	}
	
	public static void cancelWait(View rotateView){
		rotateView.clearAnimation();  
		rotateView.setVisibility(View.GONE);
	}


	public static int dipToPx(int dip) {
		return dip * densityDpi;
	}

	public static float pxToDip(int px) {
		return (float) px / densityDpi;
	}

	public static int getRequestID() {
		return _requestID;
	}

	public static void replaceFragment(FragmentActivity context, int contentID,
			Fragment fragment, boolean playAnimation, boolean addToBackStack) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (playAnimation)
			fragmentTransaction.setCustomAnimations(R.anim.push_left_in,
					R.anim.push_left_out);
		fragmentTransaction.replace(contentID, fragment);
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(null);
		}
		fragmentTransaction.commit();
	}

	public static void removeFragment(FragmentActivity context,
			Fragment fragment, boolean playAnimation) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (playAnimation)
			fragmentTransaction.setCustomAnimations(R.anim.push_right_in,
					R.anim.push_right_out);
		fragmentTransaction.remove(fragment);
		
		fragmentTransaction.commit();
	}

	public static void addFragment(FragmentActivity context, int contentResID,
			Fragment fragment) {
		addFragment(context, contentResID, fragment, true, true);
	}

	public static void addFragment(FragmentActivity context, int contentResID,
			Fragment fragment, boolean playAnimation, boolean addToBackStack) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (playAnimation) {
			fragmentTransaction.setCustomAnimations(R.anim.base_slide_right_in,
					0, 0, R.anim.push_right_out);
		}
		fragmentTransaction.add(contentResID, fragment);
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(null);
		}

		fragmentTransaction.commit();
	}

	
	
	
	
	
	public static int getWebViewResId(Context context){
		return R.layout.base_web_view;
	}
	public static int getSettingLayout(Context context) {
		return R.layout.setting;
	}
	
	
	public static int getRegisterResId(Context context){
		return R.layout.register_step1;
	}
	public static int getMyECardListResId(Context context) {
		return R.layout.my_ecard_list;
	}

	public static int getMyECardItemResId(Context context) {
		return R.layout.my_ecard_list_item;
	}
	
	public static int getMyECardDetailItemResId(Context context) {
		return R.layout.my_ecard_detail_item;
	}
	
	
	public static int getBindECardLayoutResId(Context context) {
		return R.layout.activity_bind_ecard;
	}

	
	public static int getMyECardDetalLayoutResId(Context context) {
		return R.layout.my_ecard_detail;
		
	}
	public static int getRenameECardLayoutResId(Context context) {
		return R.layout.activity_rename_ecard;
	}

	
	
	
	/**
	 * 获取旅行数据保存位置
	 * @return
	 */
	public static File getTravelHome(Context context) throws IOException {
		return SDCardUtil.getSDCardDir(context, "travel");
	}
	/**
	 * 图片保存位置
	 */
	public static File getTravelHeadImageHome(Context context) throws IOException {
		File file= new File(getTravelHome(context),"images_head");
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		return file;
	}
	
	/**
	 * 文件
	 * @param context
	 * @param dir
	 * @return
	 */
	public static File getFileDirByFileDir(Context context,String parentDir,String dir) throws IOException {
		File file= new File(getFileDir(context,parentDir),dir);
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		return file;
	}
	
	public static File getFileDir(Context context,String dir) throws IOException {
		return SDCardUtil.getSDCardDir(context, dir);
	}
	
	public static File getTravelLogImageHome(Context context) throws IOException {
		File file= new File(getTravelHome(context),"log_image");
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		return file;
	}
	
	public static File getTravelLogImage(Context context) throws IOException {
		return new File(getTravelLogImageHome(context),"log_image_"+System.currentTimeMillis()+".png");
	}
	
	/**
	 * 图片保存位置
	 */
	public static File getTravelImagePageHome(Context context) throws IOException {
		File file= new File(getTravelHome(context),"images_page");
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 旅行配置数据保存位置（封面、头像)
	 */
	public static File getTravelConfigFile(Context context) throws IOException {
		return new File(getTravelHome(context),"config.json");
	}
	
	/**
	 * 旅行日志保存位置
	 */
	public static File getTravelLogHome(Context context) throws IOException {
		File file= new File(getTravelHome(context),"logs");
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		return file;
	}

	/**
	 * 新头像
	 * @return
	 */
	public static File getNewHeadImage(Context context) throws IOException {
		return new File(SystemUtil.getTravelHeadImageHome(context),"image_"+System.currentTimeMillis()+".jpg");
	}

	public static File getNewLogFile(Context context) throws IOException {
		return new File(SystemUtil.getTravelLogHome(context),"log_"+System.currentTimeMillis()+".json");
	}

	
//	/**
//	 * crash日志保存位置
//	 */
//	public static File getErrLogFile(Context context){
//		File file= new File(getTravelHome(context),"logs");
//		if(!file.exists() || !file.isDirectory()){
//			file.mkdirs();
//		}
//		return file;
//	}



	
	
	

}
