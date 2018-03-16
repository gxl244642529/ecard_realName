package com.citywithincity.ecard.user.activities;

import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMActivity;
import com.damai.http.api.a.JobSuccess;


public class FeedbackActivity extends DMActivity{
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_user_feedback);
		setTitle("意见反馈");
	}

	@JobSuccess("feedback/add")
	public void onSubmitSuccess(Object value){
		Alert.showShortToast("提交成功");
		finish();
	}

	
}
