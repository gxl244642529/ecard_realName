package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.PickCardDetailInfo;
import com.citywithincity.utils.ActivityUtil;

public class PickCardDetailActivity extends BaseActivity implements OnClickListener {
        
	private PickCardDetailInfo dataInfo;
	
	@Override
	protected void onDestroy() {
		dataInfo=null;
		super.onDestroy();
	}
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_pick_card_detail);
		setTitle("拾卡详情");
		dataInfo=(PickCardDetailInfo)getIntent().getExtras().get("data");
		setTitle("拾卡详情");
		//设置
		((TextView)findViewById(R.id.card_number)).setText(dataInfo.cardID);
		//((TextView)findViewById(R.id.pick_card_addr)).setText(dataInfo.addr);
		((TextView)findViewById(R.id.pick_card_time)).setText(dataInfo.time);
		
		findViewById(R.id._id_ok).setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		ActivityUtil.makeCall(this, dataInfo.phone);
	}


	
}
