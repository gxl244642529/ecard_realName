package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;

import com.citywithincity.activities.FormActivity;
import com.citywithincity.auto.Form;
import com.citywithincity.auto.FormElement;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.SystemModel;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;

import java.util.Map;


@Form(layout=R.layout.feedback,
forms={
		@FormElement(name="content",id=R.id.feedback_content,description="您的建议",validate=FormElement.REQUIRED),
		@FormElement(name="by",id=R.id.feedback_by,description="您的联系方式")
},
defaultButton=R.id._title_right)
@Observer
public class FeedbackActivity extends FormActivity{
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		super.onSetContent(savedInstanceState);
		setTitle("意见反馈");
	}


	@Override
	protected void onSubmit(Map<String, Object> formValues) {
		ModelHelper.getModel(SystemModel.class).feedback(formValues);
	}
	
	@NotificationMethod(SystemModel.FEEDBACK)
	public void onFeedbackSuccess(Object value){
		Alert.showShortToast("感谢您的反馈");
		finish();
	}
	

	
}
