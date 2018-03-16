package com.damai.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.text.TextUtils;

import com.citywithincity.utils.IoUtil;
import com.damai.dmlib.LibBuildConfig;

public class ConfigReader {
	private Map<String, String> map = new HashMap<String, String>();
	
	public String getString(String key){
		return map.get(key);
	}
	
	public Set<String> getNames(){
		return map.keySet();
	}
	
	
	public int getInteger(String key){
		String value = getString(key,null);
		if(TextUtils.isEmpty(value))return 0;
		return Integer.parseInt(value);
	}
	public double getDouble(String key){
		String value = getString(key,null);
		if(TextUtils.isEmpty(value))return (double) 0;
		return Double.parseDouble(value);
	}
	public boolean getBoolean(String key){
		String value = getString(key,null);
		if(TextUtils.isEmpty(value))return false;
		return Boolean.parseBoolean(value);
	}
	
	
	public String getString(String key,String defaultValue){
		String value = map.get(key);
		if(value==null){
			return defaultValue;
		}
		return value;
	}
	
	
	
	
	
	
	public void load(Context context,String assertName){
		InputStream inputStream = null;
		BufferedReader bufferedReader = null;
		try {
			inputStream = context.getAssets().open(assertName);
			bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line ;
			while((line = bufferedReader.readLine())!=null){
				//加载所有的
				String[] args = line.split("=");
				if(args.length==2){
					//忽略掉不正确的
					String cmd = args[0].trim();
					if(TextUtils.isEmpty(cmd)){
						System.err.print("Config文件格式不正确:"+line);
					}else{
						String arg = args[1].trim();
						map.put(cmd, arg);
					}
				}else{
					if(LibBuildConfig.DEBUG){
						System.err.print("Config文件格式不正确:"+line);
					}
				}
			}
		} catch (IOException e) {
			
		}finally{
			IoUtil.close(inputStream);
			IoUtil.close(bufferedReader);
		}
	}
}
