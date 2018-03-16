package com.damai.dl;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.auto.LifeManager;
import com.damai.core.DMLib;
import com.damai.core.ILife;
import com.damai.dl.PluginManager.IPluginListener;
import com.damai.helper.ActivityResult;
import com.damai.lib.R;
import com.damai.note.ClassParser;
import com.damai.widget.RoundProgressBar;

public class HostActivity extends FragmentActivity implements IHostActivity,
		IViewContainer, IPluginListener {

	private static final String PACKAGE_NAME = "packageName";
	private static final String CLASS_NAME = "className";

	public static void startPlugin(Context context,String packageName,String className){
		try{
			Class<?> clazz = Class.forName(className);
			context.startActivity(new Intent(context, clazz));
		}catch(ClassNotFoundException e){
			Intent intent = new Intent(context, HostActivity.class);
			intent.putExtra(PACKAGE_NAME, packageName);
			intent.putExtra(CLASS_NAME, className);
			
			context.startActivity(intent);
		}
		
	}
	
	private static final String THIS_PACKAGE_NAME = DMLib.getJobManager().getApplicationContext().getPackageName();
	
	private IPluginActivity pluginActivity;
	private Resources resources;
	private PluginInfo pluginInfo;

	@Override
	public PluginInfo getPluginInfo() {
		return pluginInfo;
	}

	@Override
	public void onLoadPluginComplete(PluginInfo pluginInfo) {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String className = bundle.getString(CLASS_NAME);
		try {
			load(pluginInfo, className, null);
			if(isLoaded){
				pluginActivity.onResume();
			}
		} catch (Exception e) {
			onloadPluginFail(e);
		}
	}

	private void onloadPluginFail(Exception e) {
		e.printStackTrace();
	}
	
	private RoundProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String packageName = bundle.getString(PACKAGE_NAME);
		String className = bundle.getString(CLASS_NAME);
		try {
			PluginInfo pluginInfo = PluginManager.getInstance().getPluginInfo(packageName);
			if (pluginInfo == null || pluginInfo.getAssetManager()==null) {
				//加载
				super.setContentView(R.layout.layout_loading_plugin);
				progressBar = (RoundProgressBar) findViewById(R.id._progress_bar);
				progressBar.setMax(100);
				progressBar.setProgress(0);
				PluginManager.getInstance().load(packageName, this);
				return;
			}

			load(pluginInfo, className, savedInstanceState);
		} catch (Exception e) {
			onloadPluginFail(e);
		}
	}
	@Override
	public void onLoadError() {
		
		
	}
	private void load(PluginInfo pluginInfo, String className,
			Bundle savedInstanceState) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		this.pluginInfo = pluginInfo;
		pluginActivity = (IPluginActivity) pluginInfo.loadClass(className).newInstance();
		pluginActivity.setHost(this);
		LifeManager.onCreate(this);
		ClassParser.parse(pluginActivity);
		pluginActivity.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if(pluginActivity!=null)pluginActivity.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(pluginActivity!=null)pluginActivity.onStop();
	}
	
	private boolean isLoaded;

	@Override
	protected void onResume() {
		super.onResume();
		isLoaded = true;
		LifeManager.onResume(this);
		if(pluginActivity!=null)pluginActivity.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(pluginActivity!=null)pluginActivity.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(pluginActivity!=null)pluginActivity.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(pluginActivity!=null)pluginActivity.onDestroy();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(pluginActivity!=null)pluginActivity.onNewIntent(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (result != null) {
			result.onActivityResult(data, resultCode, requestCode);
			result = null;
		} else {
			if(pluginActivity!=null)pluginActivity.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void setContentView(int layoutResID) {
		View view = inflate(layoutResID);
		super.setContentView(view);
	}

	class MyLayoutInflater extends LayoutInflater {

		public MyLayoutInflater(LayoutInflater original, Context newContext) {
			super(original, newContext);
			// TODO Auto-generated constructor stub
		}

		protected MyLayoutInflater(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		@Override
		public LayoutInflater cloneInContext(Context newContext) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View inflate(int resource, ViewGroup root) {

			return HostActivity.this.inflate(resource);
		}

		@Override
		public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
			return HostActivity.this.inflate(resource);
		}
	}

	@Override
	public void setOverrideResources(boolean override) {
		if (override) {
			resources = pluginInfo.getResources();
			pluginInfo.getTheme().setTo(super.getTheme());
		} else {
			this.resources = null;
		}
	}

	public View inflate(int layoutId) {
		setOverrideResources(true);
		View view = LayoutInflater.from(this).inflate(layoutId, null);
		setOverrideResources(false);
		return view;
	}

	@Override
	public LayoutInflater getLayoutInflater() {
		if (this.resources == null) {
			return getWindow().getLayoutInflater();
		}
		return new MyLayoutInflater(this);
	}

	@Override
	public Resources getResources() {
		return resources == null ? super.getResources() : resources;
	}

	@Override
	public AssetManager getAssets() {
		return resources == null ? super.getAssets() : pluginInfo
				.getAssetManager();
	}

	@Override
	public ClassLoader getClassLoader() {
		return resources == null ? super.getClassLoader() : pluginInfo
				.getDexClassLoader();
	}

	@Override
	public Theme getTheme() {
		return resources == null ? super.getTheme() : pluginInfo.getTheme();
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(getRealIntent(intent));
	}
	
	private static final String MY_CLASS_NAME = HostActivity.class.getName();
	
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		
		super.startActivityForResult(getRealIntent(intent),requestCode);
	}
	
	private Intent getRealIntent(Intent intent){
		ComponentName c = intent.getComponent();
		if (c != null && !c.getClassName().equals(MY_CLASS_NAME)) {
			// 启动plugin的实际class
			if(intent.getAction()==null && THIS_PACKAGE_NAME.equals(c.getPackageName())){  
				Bundle bundle = getIntent().getExtras();
				String packageName = bundle.getString(PACKAGE_NAME);
				intent = new Intent(this, HostActivity.class);
				intent.putExtra(PACKAGE_NAME, packageName);
				intent.putExtra(CLASS_NAME, c.getClassName());
			}
		}
		return intent;
	}

	private ActivityResult result;

	@Override
	public void startActivityForResult(ActivityResult result, Intent intent,
			int requestCode) {
		this.result = result;
		this.startActivityForResult(intent, requestCode);
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	@Override
	public void addLife(ILife life) {
		pluginActivity.addLife(life);
	}

	@Override
	public View findViewByName(String name) {
		return findViewById(pluginInfo.getViewId(name));
	}

	@Override
	public int getViewId(String name) {
		return pluginInfo.getViewId(name);
	}

	@Override
	public String idToString(int id) {
		return pluginInfo.idToString(id);
	}

	@Override
	public void onProgress(int total, int current) {
		if(total>0){
			progressBar.setProgress(  (int) (100f*current/total));
		}
	}

	@Override
	public IPluginBase getPlugin() {
		return pluginActivity;
	}

	

}
