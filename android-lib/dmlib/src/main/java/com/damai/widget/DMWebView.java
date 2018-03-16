package com.damai.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class DMWebView extends WebView {

	
	
	private WebChromeClient chromeClient = new WebChromeClient() {
		@Override
		public void onProgressChanged(WebView view, int progress) {

		}
	};
	
	@SuppressLint("SetJavaScriptEnabled")
	public DMWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setWebChromeClient(chromeClient);
		WebSettings webSettings = getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDefaultTextEncodingName("utf-8");
		//访问的时候使用window.plat.xxxx
		addJavascriptInterface(insertObj, "plat");
	}
	
	private Object insertObj = new Object() {
		
	};

}
