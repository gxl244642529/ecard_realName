package com.citywithincity.ecard.models;

import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.damai.core.ApiJob;
import com.damai.core.DMAccount;
import com.damai.core.OnApiSuccessListener;
import com.damai.core.OnJobSuccessListener;
import com.damai.models.DMModel;

import java.io.File;
import java.lang.ref.WeakReference;

public class UserModel extends DMModel implements OnJobSuccessListener<ApiJob> {

	public static final String head = "user/head";
	public static final String bg = "user/bg";

	private WeakReference<OnApiSuccessListener> successListener;
	
	
	/**
	 * 设置头像
	 * 
	 * @param file
	 */
	public void setHeadImage(File file,OnApiSuccessListener listener) {
		successListener = new WeakReference<OnApiSuccessListener>(listener);
		ApiJob job = createJob(head);
		job.put("head", file);
		job.setServer(1);
		job.setWaitingMessage("正在上传...");
		job.setOnSuccessListener(this);
		job.execute();
	}

	/**
	 * 设置背景
	 * 
	 * @param file
	 * @param listener
	 */
	public void setBg(File file,OnApiSuccessListener listener) {
		successListener = new WeakReference<OnApiSuccessListener>(listener);
		ApiJob job = createJob(bg);
		job.put("bg", file);
		job.setServer(1);
		job.setOnSuccessListener(this);
		job.execute();
	}

	@Override
	public void onJobSuccess(ApiJob job) {
		ECardUserInfo user = DMAccount.get();
		String path = job.getData();
		OnApiSuccessListener listener = successListener.get();
		if(job.is(bg)){
			user.setBg(path);
			
		}else{
			user.setHead(path);
		}
		if(listener!=null){
			listener.onJobSuccess(job);
		}
		
		user.save();
	}
}
