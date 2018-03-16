package com.damai.dl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.util.Log;

import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.MD5Util;
import com.damai.core.DMServers;
import com.damai.core.IdReflectImpl;
import com.damai.util.ActivityThreadCompat;
import com.damai.util.FieldUtils;
import com.damai.util.MethodUtils;

import dalvik.system.DexClassLoader;

/**
 * plugin的存在形式有，
 * 1、asset中
 * 2、网络下载的文件
 * 
 * @author renxueliang
 *
 */
public class PluginLoader {
	public static final String TAG  ="PluginLoader";
	
	
	//解决小米JLB22.0 4.1.1系统自带的小米安全中心（lbe.security.miui）广告拦截组件导致的插件白屏问题
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static void fixMiUiLbeSecurity() throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //卸载掉LBE安全的ApplicationLoaders.mLoaders钩子
        Class ApplicationLoaders = Class.forName("android.app.ApplicationLoaders");
        Object applicationLoaders = MethodUtils.invokeStaticMethod(ApplicationLoaders, "getDefault");
        Object mLoaders = FieldUtils.readField(applicationLoaders, "mLoaders", true);
        if (mLoaders instanceof HashMap) {
            HashMap oldValue = ((HashMap) mLoaders);
            if ("com.lbe.security.client.ClientContainer$MonitoredLoaderMap".equals(mLoaders.getClass().getName())) {
                HashMap value = new HashMap();
                value.putAll(oldValue);
                FieldUtils.writeField(applicationLoaders, "mLoaders", value, true);
            }
        }

        //卸载掉LBE安全的ActivityThread.mPackages钩子
        Object currentActivityThread = ActivityThreadCompat.currentActivityThread();
        Object mPackages = FieldUtils.readField(currentActivityThread, "mPackages", true);
        if (mPackages instanceof HashMap) {
            HashMap oldValue = ((HashMap) mPackages);
            if ("com.lbe.security.client.ClientContainer$MonitoredPackageMap".equals(mPackages.getClass().getName())) {
                HashMap value = new HashMap();
                value.putAll(oldValue);
                FieldUtils.writeField(currentActivityThread, "mPackages", value, true);
            }
        }

        //当前已经处在主线程消息队列中的所有消息,找出lbe消息并remove之
        if (Looper.getMainLooper() == Looper.myLooper()) {
            final MessageQueue queue = Looper.myQueue();
            try {
                Object mMessages = FieldUtils.readField(queue, "mMessages", true);
                if (mMessages instanceof Message) {
                    findLbeMessageAndRemoveIt((Message) mMessages);
                }
                Log.e(TAG, "getMainLooper MessageQueue.IdleHandler:" + mMessages);
            } catch (Exception e) {
                Log.e(TAG, "fixMiUiLbeSecurity:error on remove lbe message", e);
            }
        }
    }
    
    //由于消息队列是一个单向链表，我们递归处理。
    //递归当前已经处在主线程消息队列中的所有消息,找出lbe消息并remove之
    private static void findLbeMessageAndRemoveIt(Message message) {
        if (message == null) {
            return;
        }
        Runnable callback = message.getCallback();
        if (message.what == 0 && callback != null) {
            if (callback.getClass().getName().indexOf("com.lbe.security.client") >= 0) {
                message.getTarget().removeCallbacks(callback);
            }
        }

        try {
            Object nextObj = FieldUtils.readField(message, "next", true);
            if (nextObj != null) {
                Message next = (Message) nextObj;
                findLbeMessageAndRemoveIt(next);
            }
        } catch (Exception e) {
            Log.e(TAG, "findLbeMessageAndRemoveIt:error on remove lbe message", e);
        }

    }

	
	public static PluginInfo loadPlugin(Context context, File file,Resources superRes, PluginInfo pluginInfo) throws Exception{
		
		fixMiUiLbeSecurity();
		
		File dexOutputDir = context.getDir("dex", Context.MODE_PRIVATE);
		String dexOutputPath = dexOutputDir.getAbsolutePath();
		DexClassLoader dexClassLoader = new DexClassLoader(
				file.getAbsolutePath(), dexOutputPath, context.getDir("so",
						Context.MODE_PRIVATE).getAbsolutePath(),
				context.getClassLoader());
		AssetManager assetManager = AssetManager.class.newInstance();
		Method addAssetPath = assetManager.getClass().getMethod("addAssetPath",
				String.class);
		addAssetPath.invoke(assetManager, file.getAbsolutePath());

		// packageName

		IdReflectImpl idReflectImpl = new IdReflectImpl();
		// idReflectImpl.init(Resources, packageName)
		Resources resources = new Resources(assetManager,superRes.getDisplayMetrics(), superRes.getConfiguration());
		idReflectImpl.init(resources, pluginInfo.getPackageName(), dexClassLoader );
		// 分析apk的xml
		pluginInfo.setIdReflect(idReflectImpl);
		pluginInfo.setAssetManager(assetManager);
		pluginInfo.setDexClassLoader(dexClassLoader);
		pluginInfo.setFile(file);
		pluginInfo.setResources(resources);
		pluginInfo.setTheme(resources.newTheme());

		return pluginInfo;
	}
	

	public static PluginInfo loadPlugin(Context context, String assertName,Resources superRes,String packageName,String mainActivity) throws Exception{
		//唯一id
		//http://xxxxxxdx/uploads/{moduleName}.{version}.apk
		String uid = new StringBuilder( DMServers.getUrl(0) ) .append("/uploads/").append(assertName).append(".apk").toString();
		uid = MD5Util.md5Appkey(uid);
		
		
		File file = new File(context.getFilesDir(), assertName + ".apk");
		InputStream in = null;
		OutputStream out = null;
		try{
			in = context.getClass().getResourceAsStream("/assets/" + assertName + ".apk");
			out = new FileOutputStream(file);
		}finally{
			IoUtil.copyAndCloseAll(in, out);
		}
		PluginInfo pluginInfo = new PluginInfo();
		pluginInfo.setPackageName(packageName);
	//	pluginInfo.setMainActivity(mainActivity);
		loadPlugin(context,file,superRes,pluginInfo);
		return pluginInfo;
	}

	
}
