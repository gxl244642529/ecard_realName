package com.citywithincity.models;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.citywithincity.utils.FileDatabase;
import com.citywithincity.utils.SDCardUtil;


/**
 * 替代数据库,只能用于少量数据 <100条
 * @author Randy
 *
 * @param <T>
 */
public final class FileDao<T extends FileObject> {
	
	/**
	 * 默认路径
	 */
	public static final String DEFAULT_DIR = "file_data";
	private Class<T> clazz;
	private File dir;
	
	public FileDao(Context context,Class<T> clazz) throws IOException {
		dir = new File(SDCardUtil.getSDCardDir(context, DEFAULT_DIR),clazz.getName());
		if(!dir.isDirectory()){
			dir.mkdir();
		}
		this.clazz = clazz;
	}

	
	private static final FilenameFilter FILE_FILTER = new FilenameFilter() {
		
		@Override
		public boolean accept(File dir, String filename) {
			return filename.endsWith(".json");
		}
	};
	
	private List<T> dataCache;
	
	public List<T> getAll(){
		if(dataCache==null){
			dataCache = FileDatabase.getListFromDir(dir,FILE_FILTER, clazz, false);
		}
		return dataCache;
	}
	
	
	/**
	 * 创建,如需要不停创建，请在create之间sleep 1毫秒
	 * @return
	 */
	public T create(){
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 创建
	 * @param data
	 * @return
	 */
	private boolean create(T data){
		File file = new File(dir, String.format("%d.json", System.currentTimeMillis()));
		data.path = file.getAbsolutePath();
		return FileDatabase.saveToFile(file, data);
	}
	
	/**
	 * 更新
	 * @param data
	 * @return
	 */
	private boolean update(T data){
		return FileDatabase.saveToFile(new File(data.path), data);
	}
	
	public boolean save(T data){
		boolean ret = false;
		if(TextUtils.isEmpty(data.path)){
			//创建
			ret = create(data);
		}else{
			File file = new File(data.path);
			if(!file.exists()){
				//创建
				ret = create(data);
			}else{
				ret= update(data);
			}
		}
		if(ret){
			dataCache = null;
		}
		return ret;
	}
	
	public void delete(T data){
		File file = new File(data.path);
		if(file.exists()){
			file.delete();
		}
		if(dataCache!=null){
			dataCache.remove(data);
			dataCache = null;
		}
		
	}
	
}
