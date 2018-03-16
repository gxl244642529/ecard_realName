package com.citywithincity.ecard.user.activities;

import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.damai.auto.DMActivity;

public class QuestionActivity extends DMActivity {


	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_question_item);
		setTitle("常见问题和解答");
	}

}
