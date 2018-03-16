package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.ui.base.BaseWebViewActivity;

public class HelperActivity extends BaseWebViewActivity {
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("帮助");
		webView.loadUrl(ECardConfig.BASE_URL + "/uploads/help2/index.html");
	}

	
}
