package com.citywithincity.ecard;

import com.citywithincity.ecard.CrashHandler.CrashUploader;
import com.citywithincity.interfaces.IValueJsonTask;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.PackageUtil;

public class ECardCrashUploader implements CrashUploader{

	@Override
	public void update(String deviceInfo, String crashInfo) {
		uploadCrash(
				deviceInfo,
				crashInfo,
		PackageUtil.getVersionName(JsonTaskManager.getApplicationContext()));
	}
	

	public void uploadCrash(String phoneInfo, String error, String versionName) {
		IValueJsonTask<Boolean> task = JsonTaskManager.getInstance().createBooleanJsonTask("crash_report");
		task.put("phone", phoneInfo);
		task.put("error", error);
		task.put("version", versionName);
		task.execute();
	}

}
