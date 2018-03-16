package com.citywithincity.ecard.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.myecard.activities.MyECardActivity;
import com.damai.auto.DMActivity;
import com.damai.helper.a.Event;


public class NfcResultActivity extends DMActivity {

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_nfc_result);
		setTitle("nfc余额查询");
	
	}
	
	@Event(requreLogin=true)
	public void onBtnMore(){
		startActivity(new Intent(this,MyECardActivity.class));
	}

}
