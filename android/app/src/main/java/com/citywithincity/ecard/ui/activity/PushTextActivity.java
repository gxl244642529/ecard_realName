package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;

public class PushTextActivity extends BaseActivity {


	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_push_text);
		setTitle("");
		Bundle bundle = getIntent().getExtras();
		if (bundle.containsKey("type")) {
			
		}
		
		((TextView)findViewById(R.id._title_text)).setText(bundle.getString("title"));
		((TextView)findViewById(R.id.text1)).setText(bundle.getString("content"));
	}
	
	@Override
	public void finish() {
		super.finish();
	}
}
