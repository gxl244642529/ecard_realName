package com.citywithincity.ecard.utils;

import android.content.Context;

import com.citywithincity.utils.SDCardUtil;

import java.io.File;
import java.io.IOException;

public class SellingSystemUtil {


	/**
	 * 获取购物车数据保存位置
	 * 
	 * @return
	 */
	public static File getCartHome(Context context) throws IOException {
		return SDCardUtil.getSDCardDir(context, "cart");
	}


	/**
	 * 购物车保存位置
	 * 
	 */
	public static File getCardHome(Context context, String fileName) throws IOException {
		File file = new File(getCartHome(context), fileName);
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		return file;
		
	}
	
	/**
	 * 图片保存位置
	 * 
	 */
	public static File getTmpHome(Context context) throws IOException {
		File file = new File(getCartHome(context), "tmp");
		if (!file.exists() || !file.isDirectory()) {
			file.mkdirs();
		}
		return file;
		
	}

	public static File getNewFile(Context context, File dir) {
		return new File(dir, "card_"
				+ System.currentTimeMillis() + ".json");
	}
	
	public static File getTmpFile(Context context, File dir, String fileName) {
		return new File(dir, "card_"
				+ fileName);
	}
	
	

	/**
	 * 新背景图片
	 * @return
	 */
	public static File getNewPageImage(Context context) throws IOException {
		return new File(SellingSystemUtil.getBgImagePageHome(context),"image_"+System.currentTimeMillis()+".jpg");
	}
	/**
	 * 图片保存位置
	 */
	public static File getBgImagePageHome(Context context) throws IOException {
		File file= new File(getBgHome(context),"images");
		if(!file.exists() || !file.isDirectory()){
			file.mkdirs();
		}
		return file;
	}
	/**
	 * 获取图片数据保存位置
	 * @return
	 */
	public static File getBgHome(Context context) throws IOException {
		return SDCardUtil.getSDCardDir(context, "diy");
	}
	
	/**
	 * 旅行配置数据保存位置（封面、头像)
	 */
	public static File getDIYConfigFile(Context context) throws IOException {
		return new File(getDIYHome(context),"config.json");
	}
	/**
	 * 获取旅行数据保存位置
	 * @return
	 */
	public static File getDIYHome(Context context) throws IOException {
		return SDCardUtil.getSDCardDir(context, "travel");
	}

}
