package com.citywithincity.ecard.user.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.UpdateModel;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.DMActivity;
import com.damai.auto.DMWebActivity;
import com.damai.core.DMLib;
import com.damai.core.DMServers;
import com.damai.helper.a.Event;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.push.Push;
import com.damai.util.PhoneUtil;
import com.tencent.bugly.beta.Beta;


public class SettingActivity extends DMActivity  {

	
	

	@Res
	private ImageView toggle_button;
	
	@Res
	private TextView setting_version;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_user_setting);
		setTitle("设置");

		setting_version.setText(PackageUtil.getVersionName(this));

		toggle_button.setSelected(Push.isEnable());
	}
	
	@Event
	public void setting_push(){
		Push.setEnable(!Push.isEnable());
		toggle_button.setSelected(Push.isEnable());
	}
	
	@Event
	public void setting_cache(){
		DMLib.getJobManager().clearCache();
		JsonTaskManager.getInstance().clearCache();
		Alert.showShortToast("清空缓存成功");
	}

	@Event
	public void setting_update(){
		Beta.checkUpgrade();
	}
	
	@Event
	public void setting_concat(){
		PhoneUtil.makeCall(this, "0592968870");
	}
	
	@Event
	public void setting_help(){
		DMWebActivity.openUrl(this,DMServers.getImageUrl(0, "/index.php/api2/helper/index") , "帮助");
	}
	
	@Event
	public void pane_question(){
		startActivity(new Intent(this,QuestionActivity.class));
	}

	@Event
	public void setting_feedback(){
		startActivity(new Intent(this,FeedbackActivity.class));
	}
	
}
