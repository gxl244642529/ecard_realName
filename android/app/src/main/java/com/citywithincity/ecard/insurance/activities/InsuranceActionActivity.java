package com.citywithincity.ecard.insurance.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.citywithincity.ecard.ui.base.BaseWebViewActivity;
import com.citywithincity.utils.ActivityUtil;

public class InsuranceActionActivity extends BaseWebViewActivity{

	private String link;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		super.onSetContent(savedInstanceState);
		
		link = getIntent().getExtras().getString("url");
		String title = getIntent().getExtras().getString("title");
		setTitle(title);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onResume() {
		
		webView.loadUrl(link);
		WebSettings settings = webView.getSettings();
//		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		settings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				if (url.contains("ecard")) {
					if (url.contains("safe_ticket")) {
						int index = url.indexOf("safe_ticket") + "safe_ticket".length() + 1;
						String ticket = url.substring(index, index + 6);
						
						InsuranceProductDetailActivity.isFromInsuranceActionActivity = true;
						
						ActivityUtil.startActivity(InsuranceActionActivity.this, InsuranceProductDetailActivity.class,ticket) ;  
						finish();
					}
				} else {
			    	webView.loadUrl(url);
			    }
			    	return true;   // true自身处理，false系统浏览器处理。
			    }
			
			});
		
		super.onResume();
	}
	
}
