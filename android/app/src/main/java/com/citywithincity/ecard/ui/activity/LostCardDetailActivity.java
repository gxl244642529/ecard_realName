package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.LostCardDetailInfo;

public class LostCardDetailActivity extends BaseActivity   {


	LostCardDetailInfo dataInfo;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_lost_card_detail);
		setTitle("失主留言");
		dataInfo = (LostCardDetailInfo)getIntent().getExtras().get("data");
		((TextView)findViewById(R.id.lost_card_info)).setText(dataInfo.message);
	
	}

	
	
	

	
}
