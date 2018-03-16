package com.citywithincity.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;

public class FileLogUtil {
	
	/**
	 * 日志保存位置
	 * @return
	 */
	public static File getErrorHome(Context context) throws IOException {
		return SDCardUtil.getSDCardDir(context, "crash_report_logs");
	}
	
	/**
	 * info日志保存位置
	 */
	public static File getInfoLogHome(Context context) throws IOException {
		File file= new File(getErrorHome(context),"log_info");
		if(!file.exists() || !file.isDirectory()){
			file.mkdir();
		}
		return file;
	}
	
	/**
	 * Error日志保存位置
	 */
	public static File getErrorLogHome(Context context) throws IOException {
		File file= new File(getErrorHome(context),"log_err");
		if(!file.exists() || !file.isDirectory()){
			file.mkdir();
		}
		return file;
	}
	
	public static File getErrLogFile(Context context) throws IOException {
		return new File(getErrorLogHome(context),"log_err"+System.currentTimeMillis()+".txt");
	}
	
	public static File getInfoLogFile(Context context) throws IOException {
		return new File(getInfoLogHome(context),"log_info"+System.currentTimeMillis()+".txt");
	}

}
