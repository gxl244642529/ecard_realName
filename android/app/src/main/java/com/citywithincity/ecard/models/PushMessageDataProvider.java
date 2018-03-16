package com.citywithincity.ecard.models;

import android.annotation.SuppressLint;
import android.content.Context;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IListRequsetResult;
import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.citywithincity.utils.SDCardUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PushMessageDataProvider implements IDestroyable, IMessageListener {
	
	public static class PushInfo{
		public String title;
		public String description;
		public String customContent;
		public String time;
	}
	
	Context context;
	List<PushInfo> result;
	IListRequsetResult<List<PushInfo>> listener;
	
	public PushMessageDataProvider(Context context,IListRequsetResult<List<PushInfo>> listener){
		this.context = context.getApplicationContext();
		this.listener = listener;
	}
	
	@Override
	public void destroy() {
		this.context = null;
		this.result = null;
		this.listener = null;
	}
	
	public static void saveToFile(final Context context,final String title,final String description,final String customContent ){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
                try{
                    File dir = getPushDir(context);
                    if(!dir.isDirectory()){
                        dir.mkdirs();
                    }
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("title", title);
                        jsonObject.put("desc", description);
                        jsonObject.put("custom", customContent);

                        File file = new File(dir,System.currentTimeMillis() + ".json");
                        IoUtil.writeToFile(jsonObject.toString(), file);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }catch (IOException e){

                }

			}
		}).start();
		
	}
	
	private static File getPushDir(Context context) throws IOException {
		 return SDCardUtil.getCacheDirectory(context,"push");
	}
	
	
	public void getMyPushList(){
		result = new ArrayList<PushInfo>();
		new Thread(new Runnable() {
			@SuppressLint("SimpleDateFormat")
			@Override
			public void run() {
				try{

					File dir = getPushDir(context);
					if(!dir.isDirectory()){
						dir.mkdirs();
						MessageUtil.sendMessage(0, PushMessageDataProvider.this);
						return;
					}

					File[] files = dir.listFiles(new FileFilter() {
						@Override
						public boolean accept(File pathname) {
							String name=pathname.getName();
							return name.endsWith(".json");
						}
					});

					int i = 0;
					for (File file : files) {
						//解析

						try {
							String content = IoUtil.readFromFile(file);
							JSONObject json = new JSONObject(content);
							PushInfo info = new PushInfo();
							info.title = json.getString("title");
							info.description = json.getString("desc");
							info.customContent = json.getString("custom");
							SimpleDateFormat s=new SimpleDateFormat("yyyy-MM-dd HH:mm");
							info.time = s.format(new Date(file.lastModified()));
							result.add(info);
							i++;
							if(i>20){
								break;
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}catch (IOException e){

				}

				
				MessageUtil.sendMessage(0, PushMessageDataProvider.this);
				
			}
		}).start();
	}


	@Override
	public void onMessage(int message) {
		listener.onRequestSuccess(result, true);
	}

	
}
