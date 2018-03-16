package com.citywithincity.ecard.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.ui.base.BaseWebViewActivity;

public class WebViewActivity extends BaseWebViewActivity {

	
	public static void open(Context context,String url,String title){
		Intent intent = new Intent(context,WebViewActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		context.startActivity(intent);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String url = getIntent().getExtras().getString("url");
		String title = getIntent().getExtras().getString("title");

		boolean hideTitle = getIntent().getExtras().getBoolean("hide", false);
		
		setTitle(title);
		webView.loadUrl(url);
		if(hideTitle){
			View topBar = findViewById(R.id._top_bar);
			if(topBar!=null){
				topBar.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onReceivedTitle(WebView view, String title) {
		setTitle(title);
	}

	
	
}
