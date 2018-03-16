package com.citywithincity.ecard.discard.activities;

import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.discard.vos.ExamInfo;
import com.citywithincity.ecard.utils.ValidateUtil;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.damai.auto.DMFragmentActivity;
import com.damai.helper.a.InitData;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.Form;
import com.damai.widget.FormRadioGroup;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;

import java.util.Map;
public class ExamInfoActivity extends DMFragmentActivity implements OnSubmitListener {

	@InitData
	private ExamInfo examInfo;

	@Res
	private SubmitButton submit;

	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_exam_info);
		setTitle("填写年审信息");
		submit.setOnSubmitListener(this);

	}
	
	@JobSuccess("exam/submitInfo")
	public void onSubmitInfoSuccess(Object vaObject){
		
		Alert.alert(this, "温馨提示", "您的信息已提交成功，我们将在2~3个工作日内审核完毕，请耐心等待",new DialogListener() {
			
			@Override
			public void onDialogButton(int id) {
				finish();
			}
		});
		
	}

	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {

	//	LibBuildConfig.DEBUG
		data.put("type",examInfo.getType());
		data.put("idCardType",0);
		data.put("birth", examInfo.getBirth());
		return true;
	}
}
