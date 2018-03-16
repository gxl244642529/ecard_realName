package com.citywithincity.ecard.models;

import android.content.Context;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.models.DMModel;

public class UpdateModel extends DMModel implements ApiListener ,IDestroyable, DialogListener{

	public static class AppVersion{
		private String url;
		private String name;
		private String code;
		private String content;
		private boolean force;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public boolean isForce() {
			return force;
		}
		public void setForce(boolean force) {
			this.force = force;
		}
	}
	
	private boolean notNotify = true;
	
	private Context context;
	public UpdateModel(Context context){
		this.context = context;
	}
	
	@Override
	public void destroy() {
		context = null;
		super.destroy();
	}
	
	public void setNotNotify(boolean notNotify){
		this.notNotify = notNotify;
	}
	
	public void checkUpdate(){
		final Context context = this.context;
		ApiJob job = createJob("version/check");
		job.put("packageName",context.getPackageName());
		job.put("code", PackageUtil.getVersionCode(context));
		job.put("channel", PackageUtil.getMetaValue(context, "UMENG_CHANNEL"));
		job.setServer(1);
		job.setEntity(AppVersion.class);
		if(!notNotify){
			job.setWaitingMessage("正在检测版本...");
		}
		job.setApiListener(this);
		job.execute();
	}
	
	private AppVersion version;
	private boolean forceUpdate;
	@Override
	public void onJobSuccess(ApiJob job) {
		//检测到新版本
		AppVersion version = job.getData();
		
		if(version!=null){
			//弹出框
			this.version = version;
			forceUpdate = version.isForce();
			Alert.confirm(context,"发现新版本" + version.getName(), version.getContent(), this)
			.setOkText("现在更新").setCancelOnTouchOutside(false);
			
		}else{
			if(!notNotify){
				Alert.alert(context, "已经是最新版本了");
			}
		}
	}

	@Override
	public boolean onJobError(ApiJob job) {
		return notNotify;
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		return notNotify;
	}

	private DownloadModel model;
	@Override
	public void onDialogButton(int id) {
		if(id==R.id._id_ok){
			if(model==null){
				model = new DownloadModel(context,context.getPackageName()+version.getName()+".apk");
			}
			model.downloadApi(version.getUrl(),forceUpdate);
		}else{
			if(version.isForce()){
				LifeManager.closeAll();
				System.exit(0);
			}
		}
	}
	
}
