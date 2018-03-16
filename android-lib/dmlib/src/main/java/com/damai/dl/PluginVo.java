package com.damai.dl;

import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.interfaces.IJsonValueObject;

public class PluginVo implements IJsonValueObject {

	// 网址,下载后的文件为new File(context.getFilesDir(), Md5(url));
	private String url;
	// 版本
	private int versionCode;
	// 版本名称
	private String versionName;
	// 主页
	private String mainActivity;
	//
	private String packageName;

	@Override
	public void fromJson(JSONObject json) throws JSONException {
		this.url = json.getString("url");
		this.versionCode = json.getInt("versionCode");
		this.versionName = json.getString("versionName");
		this.mainActivity = json.getString("mainActivity");
		this.packageName = json.getString("packageName");
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(String mainActivity) {
		this.mainActivity = mainActivity;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
