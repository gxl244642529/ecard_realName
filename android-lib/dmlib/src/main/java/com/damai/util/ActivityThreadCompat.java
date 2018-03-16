package com.damai.util;

import java.lang.reflect.InvocationTargetException;

import android.app.Instrumentation;
import android.os.Handler;
import android.os.Looper;


/**
 * Created by Andy Zhang(zhangyong232@gmail.com) on 2015/3/14.
 */
public class ActivityThreadCompat {

    private static Object sActivityThread;

    @SuppressWarnings("rawtypes")
	private static Class sClass = null;

    public synchronized static final Object currentActivityThread() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (sActivityThread == null) {
            sActivityThread = MethodUtils.invokeStaticMethod(activityThreadClass(), "currentActivityThread");
            if (sActivityThread == null) {
                sActivityThread = currentActivityThread2();
            }
        }
        return sActivityThread;
    }

    public static final Class activityThreadClass() throws ClassNotFoundException {
        if (sClass == null) {
            sClass = Class.forName("android.app.ActivityThread");
        }
        return sClass;
    }

    private static Object currentActivityThread2() {
        Handler handler = new Handler(Looper.getMainLooper());
        final Object sLock = new Object();
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    sActivityThread = MethodUtils.invokeStaticMethod(activityThreadClass(), "currentActivityThread");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    synchronized (sLock) {
                        sLock.notify();
                    }
                }

            }
        });
        if (sActivityThread == null && Looper.getMainLooper() != Looper.myLooper()) {
            synchronized (sLock) {
                try {
                    sLock.wait(300);
                } catch (InterruptedException e) {
                }
            }
        }
        return null;
    }

    public static Instrumentation getInstrumentation() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        Object obj = currentActivityThread();
        return (Instrumentation) MethodUtils.invokeMethod(obj, "getInstrumentation");
    }
}

