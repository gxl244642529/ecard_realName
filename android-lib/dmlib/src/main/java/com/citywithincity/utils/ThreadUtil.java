package com.citywithincity.utils;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public class ThreadUtil {

	  /** Handler to the main thread. */
    private static final Handler mHandler = new Handler(Looper.getMainLooper());
    
    public static void run(final Runnable threadRun){
    	AsyncTask<Runnable, Integer, Boolean> task = new AsyncTask<Runnable, Integer, Boolean>() {
    		
			@Override
			protected Boolean doInBackground(Runnable... params) {
				threadRun.run();
				return true;
			}
		};
		task.execute();
    }
    
    public static void newTask(final Activity activity,final Runnable threadRun,final Runnable finishRun){
    	AsyncTask<Runnable, Integer, Boolean> task = new AsyncTask<Runnable, Integer, Boolean>() {
    		
			@Override
			protected Boolean doInBackground(Runnable... params) {
				threadRun.run();
				return true;
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				if(activity.isFinishing()){
					return;
				}
				finishRun.run();
			}
		};
		task.execute();
    }
    
	
	/**
	 * ui线程
	 * @param runnable
	 */
	public static void post(Runnable runnable){
		mHandler.post(runnable);
	}
	
	/**
	 * 一段时间后
	 * @param runnable		
	 * @param delayMillis	毫秒
	 */
	public static void postAfter(Runnable runnable,long delayMillis){
		mHandler.postDelayed(runnable, delayMillis);
	}
	
}
