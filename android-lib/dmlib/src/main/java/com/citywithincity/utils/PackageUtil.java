package com.citywithincity.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;

/**
 * 处理权限判断 判断其他程序的安装 运行其他程序等
 * 
 * @author randy
 * 
 */
public class PackageUtil {

	public static void uninstall(Context context, String packageName) {
		// 通过程序的报名创建URI
		Uri packageURI = Uri.parse("package:" + packageName);
		// 创建Intent意图
		Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
		// 执行卸载程序
		context.startActivity(intent);
	}

	/**
	 * 从asserts目录写入文件
	 * 
	 * @param context
	 * @param assertsName
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("WorldReadableFiles")
	public static File extractAssertsToFile(Context context, String assertsName) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {

			is = context.getResources().getAssets().open(assertsName);

			fos = context.openFileOutput(assertsName, Context.MODE_PRIVATE
					+ Context.MODE_WORLD_READABLE);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			fos.flush();

			return new File(context.getFilesDir().getPath(), assertsName);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * 获取metadata
	 * 
	 * @param context
	 * @param metaKey
	 * @return
	 */
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}

	/**
	 * 判断本进程有没有在运行指定的activity
	 * 
	 * @param context
	 * @param packageName
	 * @param className
	 * @return
	 */
	public static boolean isRunning(Context context, String packageName,
			Class<? extends Activity> clsRef) {
		ActivityManager aManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String className = clsRef.getName();
		List<RunningTaskInfo> list = aManager.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			System.out.println(info.topActivity.getPackageName());
			System.out.println(info.topActivity.getClassName());
			if (info.topActivity.getPackageName().equals(packageName)
					&& info.topActivity.getClassName().equals(className)) {

				return true;
			}
		}
		return false;
	}

	/**
	 * 判断本进程有没有在运行指定的activity
	 * 
	 * @param context
	 * @param packageName
	 * @param className
	 * @return
	 */
	public static boolean isRunning(Context context,
			Class<? extends Activity> clsRef) {
		ActivityManager aManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String className = clsRef.getName();
		String packageName = context.getPackageName();
		List<RunningTaskInfo> list = aManager.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			System.out.println(info.topActivity.getPackageName());
			System.out.println(info.topActivity.getClassName());
			if (info.topActivity.getPackageName().equals(packageName)
					&& info.topActivity.getClassName().equals(className)) {

				return true;
			}
		}
		return false;
	}

	/**
	 * 判断本进程有没有在运行指定的activity
	 * 
	 * @param context
	 * @param packageName
	 * @param className
	 * @return
	 */
	public static boolean isRunning(Context context, String packageName) {
		ActivityManager aManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = aManager.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			System.out.println(info.topActivity.getPackageName());
			System.out.println(info.topActivity.getClassName());
			if (info.topActivity.getPackageName().equals(packageName)) {

				return true;
			}
		}
		return false;
	}

	/**
	 * 判断本进程有没有在运行指定的activity
	 * 
	 * @param context
	 * @param packageName
	 * @param className
	 * @return
	 */
	public static boolean isRunning(Context context) {
		ActivityManager aManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = aManager.getRunningTasks(100);
		String packageName = context.getPackageName();
		for (RunningTaskInfo info : list) {
			System.out.println(info.topActivity.getPackageName());
			System.out.println(info.topActivity.getClassName());
			if (info.topActivity.getPackageName().equals(packageName)) {

				return true;
			}
		}
		return false;
	}

	
	/**
	 * 版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context,String packageName) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(packageName,0);
			int version = packInfo.versionCode;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return 0;
	}

	
	/**
	 * 版本号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			int version = packInfo.versionCode;
			return version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 获取版本名称
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {

		PackageInfo info;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 安装apk
	 * 
	 * @param activity
	 * @param file
	 */
	public static void installApk(Context context, File file,
			Collection<Activity> activities) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);

		for (Activity activity : activities) {
			activity.finish();
		}

	}

	/**
	 * 安装apk
	 * 
	 * @param activity
	 * @param file
	 */
	public static void installApk(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		context.startActivity(intent);

	}

	/**
	 * 判断有没有安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isPackageInstalled(Context context, String packageName) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			return packageInfo != null;
		} catch (Exception e) {
			return false;
		}
	}

	

	public static boolean runPackage(Context context, Intent intent,
			String packageName, String className,int requestCode) {
		
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			if (packageInfo != null) {
				if (packageInfo.activities != null) {
					if(intent==null){
						intent = new Intent(Intent.ACTION_MAIN);
					}
					
					intent.setAction(Intent.ACTION_VIEW);
					intent.setClassName(packageInfo.packageName,
							packageInfo.packageName + className);
					if(requestCode>0){
						((Activity)context).startActivityForResult(intent,requestCode);
					}else{
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						context.startActivity(intent);
					}
					

					return true;
				}
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}

	
	/**
	 * 启动指定apk的指定activity
	 * 
	 * @param context
	 * @param intent
	 *            可以传参
	 * @param packageName
	 * @param className
	 * @return
	 */
	public static boolean runPackage(Context context, Intent intent,
			String packageName, String className) {
		return runPackage(context, intent, packageName, className, 0);
	}

}
