package com.citywithincity.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.os.Environment.MEDIA_MOUNTED;

public class SDCardUtil {
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";


	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				//	L.w("Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				//L.i("Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}
	@SuppressLint("SdCardPath")
	public static File getCacheDirectory(Context context, boolean preferExternal) {
		File appCacheDir = null;
		if (preferExternal && MEDIA_MOUNTED
				.equals(Environment.getExternalStorageState()) && hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
			//	L.w("Can't define system cache directory! '%s' will be used.", cacheDirPath);
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}


	protected static File getCacheDirectory(Context context) {
		return getCacheDirectory(context, true);
	}

	public static File getCacheDirectory(Context context,String dirName) throws IOException {
		File dir = getCacheDirectory(context, true);
		if(dir!=null){
			File file = new File(dir,dirName);
			if(!file.exists()){
				if(!file.mkdir()){
					Log.e("StorageUtil", "Cannot create dir");
					throw new  IOException("Cannot create dir");
				}
			}
			return file;
		}
        throw new  IOException("Cannot create dir");
	}

	/**
	 * 获取sd卡根目录
	 * @param context
	 * @return
	 */
	private static File getSDCardRoot(Context context) throws IOException {
		File file = getExternalSdCardPath();
		File dir = new File(file,context.getPackageName());
		if(!dir.exists()){
			dir.mkdir();
			if(!dir.canWrite()){
				throw new IOException("sd卡不可写");
			}
		}
		return dir;
	}

	/**
	 * 获取sk卡目录
	 * @param context
	 * @param name
	 * @return
	 */
	public static File getSDCardDir(Context context,String name) throws IOException {
		try{
			File file = getSDCardRoot(context);
			File ret = new File(file,name);
			if(!ret.exists() || !ret.isDirectory()){
				ret.mkdir();
				if(!ret.canWrite()){
					throw new  IOException("目录不可写");
				}
			}
			return ret;
		}catch (IOException e){
			return getCacheDirectory(context,name);
		}

	}





	/**
	 * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
	 *
	 * @return
	 */
	private static ArrayList<String> getDevMountList() throws IOException {
		String[] toSearch = IoUtil.readFromFile(new File("/etc/vold.fstab")).split(" ");
		ArrayList<String> out = new ArrayList<String>();
		for (int i = 0; i < toSearch.length; i++) {
			if (toSearch[i].contains("dev_mount")) {
				if (new File(toSearch[i + 2]).exists()) {
					out.add(toSearch[i + 2]);
				}
			}
		}
		return out;
	}

	/**
	 * 判断SDCard是否可用
	 *
	 * @return
	 */
	public static boolean isSDCardEnable()
	{
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);

	}




	/**
	 * 获取扩展SD卡存储目录
	 *
	 * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
	 * 否则：返回内置SD卡目录
	 *
	 * @return
	 */
	private static File getExternalSdCardPath() throws IOException {

		if (isSDCardEnable()) {
			File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
			return sdCardFile;
		}

		String path = null;
		File sdCardFile = null;
		ArrayList<String> devMountList = getDevMountList();
		for (String devMount : devMountList) {
			File file = new File(devMount);

			if (file.isDirectory() && file.canWrite()) {
				path = file.getAbsolutePath();
				String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
				File testWritable = new File(path, "test_" + timeStamp);
				if (testWritable.mkdirs()) {
					testWritable.delete();
				} else {
					path = null;
				}
			}
		}

		if (path != null) {
			sdCardFile = new File(path);
			return sdCardFile;
		}
		throw new IOException("找不到sd卡目录");
	}


	/**
	 * 获取临时图片目录
	 */
	public static File getTempImageDir(Context context) throws IOException {
		return getSDCardDir(context,"image");
	}

	public static File getTempImageFile(Context context) throws IOException {
		return new File(getSDCardDir(context,"image"), System.currentTimeMillis() + ".jpg");
	}
}
