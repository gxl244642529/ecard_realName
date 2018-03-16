package com.damai.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import android.content.Context;

import com.citywithincity.utils.FileUtil;
import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.SDCardUtil;

 public class DiskCache implements Cache {
	
	File cacheDir;
	
	public DiskCache(Context context) throws IOException {
		cacheDir = SDCardUtil.getCacheDirectory(context, "files");
	}
	
	

	@Override
	public byte[] get(String key) {
		File file = new File(cacheDir,key);
		try{
			return IoUtil.readBytes(file);
		}catch(IOException e){
			return null;
		}
		
	}

	@Override
	public void put(String key, byte[] data) {
		File file = new File(cacheDir,key);
		try{
			IoUtil.writeBytes(file, data);
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}


	@Override
	public void clear() {
		FileUtil.delFolder(cacheDir.getAbsolutePath());
		cacheDir.mkdir();
	}

	/**
	 * 
	 */
	@Override
	public void removeAllCache(final String key) {
		File[] files = cacheDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				return filename.startsWith(key);
			}
		});
		
		if(files!=null){
			for (File file : files) {
				file.delete();
			}
		}
	}


	@Override
	public CacheResult get(String key, CacheExpire cacheExpire) {
		//获取文件路径
		File file = new File(cacheDir,key);
		if (file == null || !file.exists()) {
			return null;
		}
		try{
			byte[] data = IoUtil.readBytes(file);
			if(data==null){
				return null;
			}
			return new CacheResult(data,cacheExpire.isExpire(file.lastModified()));
		}catch(IOException e){
			return null;
		}
	}

}
