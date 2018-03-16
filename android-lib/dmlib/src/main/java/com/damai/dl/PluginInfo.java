package com.damai.dl;

import java.io.File;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;

import com.damai.core.IdReflect;

public class PluginInfo {
	private File file;
	private Theme theme;
	private AssetManager assetManager;
	private Resources resources;
	private ClassLoader dexClassLoader;
	private String packageName;
	
	public Class<?> loadClass(String name) throws ClassNotFoundException{
		Class<?> clazz = Class.forName(name,false, dexClassLoader);
		return clazz;
	}
	
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	public AssetManager getAssetManager() {
		return assetManager;
	}
	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
	public Resources getResources() {
		return resources;
	}
	public void setResources(Resources resources) {
		this.resources = resources;
	}
	public ClassLoader getDexClassLoader() {
		return dexClassLoader;
	}
	public void setDexClassLoader(ClassLoader dexClassLoader) {
		this.dexClassLoader = dexClassLoader;
	}


	public int getViewId(String name) {
		return resources.getIdentifier(name, "id", packageName);
	}


	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	private IdReflect idReflect;

	public String idToString(int id) {
		return idReflect.idToString(id);
	}


	public IdReflect getIdReflect() {
		return idReflect;
	}


	public void setIdReflect(IdReflect idReflect) {
		this.idReflect = idReflect;
	}
}
