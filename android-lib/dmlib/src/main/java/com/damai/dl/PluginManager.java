package com.damai.dl;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MD5Util;
import com.citywithincity.utils.PackageUtil;
import com.citywithincity.utils.SDCardUtil;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMLib;
import com.damai.core.DMServers;
import com.damai.core.HttpJob;
import com.damai.core.JobListener;
import com.damai.dmlib.ExceptionHandler;
import com.damai.helper.ActivityResult;
import com.damai.models.DMModel;

public class PluginManager extends DMModel{
	

	private static String getUrl(String url){
		if(!url.startsWith("http://")){
			if(!url.startsWith("/")){
				url = "/" + url;
			}
			url = DMServers.getImageUrl(0, url);
		}
		return url;
	}
	
	
	private static interface PluginVersionListener{
		void onPluginVersionCheckComplete(PluginVo data);
		void onPluginVersionCheckError();
	}
	
	public static interface IPluginListener{
		void onLoadPluginComplete(PluginInfo pluginInfo);
		void onProgress(int total,int current);
		void onLoadError();
	}
	public static interface IApkListener{
		void onLoadApkComplete(File file);
		void onProgress(int total,int current);
		void onLoadStart(HttpJob job);
		void onLoadError();
	}
	
	private class ApkManager implements PluginVersionListener, JobListener<HttpJob>, ActivityResult{

		private static final int RQUEST_DELETE = 1;
		
		private static final int REQUEST_INSTALL = 2;
		
		private Context context;
		private WeakReference<IApkListener> listener;
		private PluginVo currentData;
		private String moduleName;
		private Intent intent;
		public ApkManager(Context context){
			this.context = context;
		}
		
		public void setListener(IApkListener listener,String moduleName,Intent intent){
			this.moduleName = moduleName;
			this.intent = intent;
			this.listener = new WeakReference<PluginManager.IApkListener>(listener);
		}
		
		private File getFile(PluginVo data) throws IOException {
			return new File(SDCardUtil.getSDCardDir(context, "apk"), MD5Util.md5Appkey(data.getUrl())+".apk");
		}
		@Override
		public void onPluginVersionCheckComplete(final PluginVo data) {
			currentData = data;
			
			//判断有没有安装
			if(PackageUtil.isPackageInstalled(context, data.getPackageName())){
				//如果有安装，判断版本
				if(data.getVersionCode() != PackageUtil.getVersionCode(context,data.getPackageName())){
					Alert.alert(LifeManager.getActivity(),"温馨提示", "本模块的旧版本已经不能使用，请您先卸载旧版本",new DialogListener() {
						
						@Override
						public void onDialogButton(int id) {
							// 通过程序的报名创建URI
							Uri packageURI = Uri.parse("package:" + data.getPackageName());
							// 创建Intent意图
							Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
							// 执行卸载程序
							LifeManager.getCurrent().startActivityForResult(ApkManager.this, intent, RQUEST_DELETE);
							
						}
					});
				}else{
					startModule();
				}
			}else{
				File file = null;
				try {
					file = getFile(data);
					download(currentData, file);
				} catch (IOException e) {
					Alert.alert(LifeManager.getActivity(),"文件写入失败,请确认sdk是否有安装");
				}

			}
		}
		
		private void startModule(){
			Context context =LifeManager.getActivity();
			PackageUtil.runPackage(context,intent, currentData.getPackageName(), moduleName ,requestCode);
		}
		
		private void install(File file,PluginVo data){
			//这里解析文件是否合法
			// apk安装包信息
		
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			LifeManager.getCurrent().startActivityForResult(ApkManager.this, intent, REQUEST_INSTALL);
		}
		
	
		
		private void download(PluginVo data,File file){
			IApkListener listener = this.listener.get();
			if(listener==null){
				return;
			}
			String url = data.getUrl();
			HttpJob httpJob = DMLib.getJobManager().download(getUrl(url),file,this);
			listener.onLoadStart(httpJob);
		}

		@Override
		public void onPluginVersionCheckError() {
			IApkListener listener = this.listener.get();
			if(listener!=null){
				listener.onLoadError();
			}
			Alert.showShortToast("加载插件失败,请确认网络是否通畅");
		}

		@Override
		public void onJobSuccess(HttpJob job) {
			IApkListener listener = this.listener.get();
			if(listener!=null){
				File file = job.getData();
				listener.onLoadApkComplete(file);
				install(file, currentData);
			}
		}

		@Override
		public void onJobProgress(HttpJob job) {
			IApkListener listener = this.listener.get();
			if(listener==null){
				return;
			}
			listener.onProgress(job.getTotalBytes(), job.getCurrentBytes());
		}

		@Override
		public boolean onJobError(HttpJob job) {
			
			IApkListener listener = this.listener.get();
			if(listener==null){
				return false;
			}
			listener.onLoadError();
			return false;
		}

		@Override
		public void onActivityResult(Intent intent, int resultCode,
				int requestCode) {
			if(requestCode == RQUEST_DELETE){
				if(PackageUtil.isPackageInstalled(context, currentData.getPackageName())){
					Alert.showShortToast("取消卸载");
					return;
				}
				PluginVo data = currentData;
				//开始下载文件
                File file = null;
                try {
                    file = getFile(data);
                    download(data, file);
                } catch (IOException e) {
                   Alert.alert(LifeManager.getActivity(),"文件写入失败，请确认sdk是否有安装");
                }

			}else if(requestCode == REQUEST_INSTALL){
				if(!PackageUtil.isPackageInstalled(context, currentData.getPackageName())){
					Alert.showShortToast("安装模块没有成功");
					return;
				}
				startModule();
			}
		}
		
	}
	
	
	private static class PluginInfoManager implements PluginVersionListener, JobListener<HttpJob>{

		private Map<String, PluginInfo> pluginCache;
		private Context context;
		
		private WeakReference<IPluginListener> pluginListener;
		
		public PluginInfoManager(Context context){
			this.context = context;
		}
		

		public void setPluginListener(IPluginListener listener) {
			pluginListener = new WeakReference<PluginManager.IPluginListener>(listener);
		}
		
		private PluginVo currentData;
		
		@Override
		public void onPluginVersionCheckComplete(PluginVo data) {
			if(pluginCache==null){
				pluginCache = new HashMap<String, PluginInfo>();
			}
			PluginInfo info = pluginCache.get(data.getPackageName());
			if(info!=null){
				IPluginListener listener = pluginListener.get();
				if(listener!=null){
					listener.onLoadPluginComplete(info);
				}
				return;
			}
			
			final File file = new File(context.getFilesDir(), MD5Util.md5Appkey(data.getUrl())+".apk");
			if(!file.exists()){
				currentData = data;
				//下载文件,
				//为防止重复下载，将正在下载的链接放在缓存里面
				String url = data.getUrl();
				
				DMLib.getJobManager().download(getUrl(url),file,this);
				return;
			}
			loadFile(file,data);
		}
		
		
		
		public PluginInfo getPluginInfo(String packageName){
			if(pluginCache==null)return null;
			return pluginCache.get(packageName);
		}
		
		
		private void loadFile(File file,PluginVo data){
			try{
				PluginInfo info = createPluginInfo(data.getPackageName());
				PluginLoader.loadPlugin(context, file, context.getResources(),info);
				IPluginListener listener = pluginListener.get();
				if(listener!=null){
					listener.onLoadPluginComplete(info);
				}
			}catch(Exception e){
				ExceptionHandler.handleException(e);
			}
		}
		
		private PluginInfo createPluginInfo(String packageName){
			PluginInfo info = new PluginInfo();
			info.setPackageName(packageName);
			pluginCache.put(packageName, info);
			return info;
		}

		
		
		@Override
		public void onJobSuccess(HttpJob job) {
			File file = job.getData();
			loadFile(file, currentData);
		}

		@Override
		public void onJobProgress(HttpJob job) {
			IPluginListener listener = pluginListener.get();
			if(listener!=null){
				listener.onProgress(job.getTotalBytes(), job.getCurrentBytes());
			}
		}

		@Override
		public boolean onJobError(HttpJob job) {
			onPluginVersionCheckError();
			return false;
		}


		@Override
		public void onPluginVersionCheckError() {
			IPluginListener listener = pluginListener.get();
			if(listener!=null){
				listener.onLoadError();
			}
		}

	}

	
	private static class PluginVersionManager implements ApiListener{
		private ApiJob job;
		private boolean isUpdating;
		private Map<String, PluginVo> pluginInfoCache;
		private PluginVersionListener listener;
		private String packageName;
		
		public PluginVersionManager(){
			ApiJob job = DMLib.getJobManager().createArrayApi("version/module");
			job.setCachePolicy(CachePolicy.CachePolity_NoCache);
			job.setServer(1);
			job.setEntity(PluginVo.class);
			job.setApiListener(this);
			this.job = job;
		}
		
		
		public void checkVersion(String packageName,PluginVersionListener listener){
			if(pluginInfoCache!=null){
				PluginVo data = pluginInfoCache.get(packageName);
				if(data==null){
					doCheckVersion(packageName, listener);
					return;
				}
				listener.onPluginVersionCheckComplete(data);
				return;
			}
			if(isUpdating)return;
			doCheckVersion(packageName, listener);
		
		}
		
		private void doCheckVersion(String packageName,PluginVersionListener listener){
			this.packageName = packageName;
			//从网络加载
			isUpdating=true;
			job.execute();
			this.listener = listener;
		}
		
		@Override
		public void onJobSuccess(ApiJob job) {
			isUpdating=false;
			pluginInfoCache = new HashMap<String, PluginVo>();
			//当前的
			List<PluginVo> list = job.getData();
			Map<String, PluginVo> map = pluginInfoCache;
			for (PluginVo pluginVo : list) {
				map.put(pluginVo.getPackageName(), pluginVo);
			}
			PluginVo data = pluginInfoCache.get(packageName);
			if(data==null){
				Alert.alert(LifeManager.getCurrent().getActivity(), "找不到插件,请确认是否已经上传至服务器:["+packageName+"]");
				return;
			}
			listener.onPluginVersionCheckComplete(data);
		}

		@Override
		public boolean onJobError(ApiJob job) {
			isUpdating=false;
			return false;
		}

		@Override
		public boolean onApiMessage(ApiJob job) {
			isUpdating=false;
			return false;
		}
	}
	
	
	//等待的任务

	private static PluginManager instance;
	
	public static PluginManager getInstance(){
		if(instance==null){
			instance = new PluginManager(LifeManager.getActivity().getApplicationContext());
		}
		return instance;
	}
	
	
	private PluginVersionManager versionManager;
	private PluginInfoManager infoManager;
	private ApkManager apkManager;
	
	
	public PluginManager(Context context){
		instance = this;
		versionManager = new PluginVersionManager();
		infoManager = new PluginInfoManager( context.getApplicationContext());
		apkManager = new ApkManager(context.getApplicationContext());
	}
	
	
	public PluginInfo getPluginInfo(String packageName){
		return infoManager.getPluginInfo(packageName);
	}
	
	
	/**
	 * 通过包名称来加载，并打开主页
	 * @param packageName
	 * @throws Exception 
	 */
	public void load(String packageName,IPluginListener listener) throws Exception{
		infoManager.setPluginListener(listener);
		versionManager.checkVersion(packageName, infoManager);
	}

	int requestCode;
	
	/**
	 * 通过包名称来加载，并打开主页
	 * @param packageName
	 * @throws Exception 
	 */
	public void startApkModule(String packageName,String moduleName,Intent intent,int requestCode, IApkListener listener){
		this.requestCode = requestCode;
		apkManager.setListener(listener,moduleName,intent);
		versionManager.checkVersion(packageName, apkManager);
	}
	
	
}
