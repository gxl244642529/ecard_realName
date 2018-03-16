package com.citywithincity.ecard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.interfaces.ILog;
import com.citywithincity.models.LogFactory;
import com.citywithincity.utils.FileUtil;
import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.NetworkUtil;
import com.citywithincity.utils.SDCardUtil;
import com.citywithincity.utils.ThreadUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author user
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler, Runnable {

	public static final String TAG = "CrashHandler";
	
	

	// 系统默认的UncaughtException处理类
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	// CrashHandler实例
	private static CrashHandler INSTANCE = new CrashHandler();
	// 程序的Context对象
	private Context mContext;
	// 用来存储设备信息和异常信息
	private Map<String, String> infos = new HashMap<String, String>();

	// 用于格式化日期,作为日志文件名的一部分
	@SuppressLint("SimpleDateFormat")
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}
	private CrashUploader uploader;
	public void setUploader(CrashUploader uploader){
		this.uploader = uploader;
	}

	public File[] getCrashFileList() throws IOException {
		File file = getDir(mContext);
		return file.listFiles();
	}
	
	
	/**
	 * 上传崩溃文件
	 */
	public void uploadCrashFile(){
		ThreadUtil.run(this);
	
	}

	public void clear() throws IOException {
		File file = getDir(mContext);
		FileUtil.delAllFile(file.getAbsolutePath());
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context) {
		mContext = context;
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "error : ", e);
			}
			// 退出程序
			android.os.Process.killProcess(android.os.Process.myPid());
//			System.exit(1);
			//按照正常退出处理
			List<Activity> list = ECardJsonManager.getInstance().getRunningActivities();
			for (Activity activity : list) {
				int i = 0;
				i++;
				activity.finish();
				LogFactory.setConfig(ILog.Level_Info, ILog.Type_File, mContext);
				LogFactory.getLog(activity.getClass()).info(activity.getClass().getName() + i +" : finished");
			}

			System.exit(0);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG)
						.show();
				Looper.loop();
			}
		}.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(mContext, ex);
		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Context context, Throwable ex) {
		ex.printStackTrace();
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				File dir = getDir(context);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(new File(dir,
						fileName));
				fos.write(sb.toString().getBytes());
				fos.close();
			}
			return fileName;
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}

	private static File getDir(Context context) throws IOException {
		return SDCardUtil.getCacheDirectory(context, "crash_report");
	}
	
	public interface CrashUploader{
		void update(String deviceInfo,String crashInfo);
	}

	@Override
	public void run() {
		try{
			if (NetworkUtil.isNetworkAvaliable(mContext)){
				File[] files = getCrashFileList();
				if (files == null || files.length == 0)
					return;
				StringBuffer sBuffer = new StringBuffer();
				for (File file : files) {
					String str;
					try {
						str = IoUtil.readFromFile(file);
						sBuffer.append(str);
						sBuffer.append("\r\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				int version = 3;
				Class<android.os.Build.VERSION> build_version_class = android.os.Build.VERSION.class;
				// 取得 android 版本
				java.lang.reflect.Field field;
				try {
					field = build_version_class.getField("SDK_INT");
					version = (Integer) field.get(new android.os.Build.VERSION());

					Class<android.os.Build> build_class = android.os.Build.class;
					// 取得牌子
					java.lang.reflect.Field manu_field = build_class
							.getField("MANUFACTURER");
					String manufacturer = (String) manu_field
							.get(new android.os.Build());
					// 取得型號
					java.lang.reflect.Field field2 = build_class.getField("MODEL");
					String model = (String) field2.get(new android.os.Build());
					// 模組號碼
					java.lang.reflect.Field device_field = build_class
							.getField("DEVICE");
					String device = (String) device_field
							.get(new android.os.Build());
					final String deviceInfo =new StringBuilder(manufacturer).append( " :" ).append( model)
							.append(" :")
							.append(version).append( " :" ).append( device).toString();
					final String crashInfo = sBuffer.toString();
					ThreadUtil.post(new Runnable() {

						@Override
						public void run() {
							uploader.update(deviceInfo, crashInfo);
						}
					});
				/*ECardJsonManager.getInstance().uploadCrash(
						deviceIno,
						sBuffer.toString(),
						PackageUtil.getVersionName(mContext));*/


				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					clear();
				}

			}
		}catch (IOException e){

		}

		
		
	}
}