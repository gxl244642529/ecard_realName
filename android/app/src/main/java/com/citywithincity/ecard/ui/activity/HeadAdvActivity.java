package com.citywithincity.ecard.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.models.vos.HeadAdvVo;
import com.citywithincity.ecard.ui.base.BaseWebViewActivity;
import com.citywithincity.utils.ActivityUtil;
import com.damai.core.DMAccount;

public class HeadAdvActivity extends BaseWebViewActivity {

	public static final int SELECT_CARD = 1;

	HeadAdvVo info;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// this._layoutResID = R.layout.news_detail;
		super.onCreate(savedInstanceState);
		info = (HeadAdvVo) getIntent().getExtras().get("data");
		setTitle(info.title);
		
		WebSettings ws = webView.getSettings();
		ws.setJavaScriptEnabled(true);
		ws.setSupportZoom(true);//设定支持缩放 
		ws.setBuiltInZoomControls(true);
		ws.setUseWideViewPort(true);//设置此属性，可任意比例缩放。
		
//		webView.getSettings().setBuiltInZoomControls(true);
//		webView.getSettings().setSupportZoom(true);//设定支持缩放   
//		webView.getSettings().setUseWideViewPort(true);//设置此属性，可任意比例缩放。
		
	}

	private String function;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onResume() {

		String link;
		if (info.type == HeadAdvVo.AD_TYPE_HTML) {

			link = "/api2/head_detail/index/";
			if (info.url.equals("null")) {
				link = link + info.id;
			} else {
				link = info.url + link + info.id;
			}

			if (link.charAt(0) == '/') {
				link = ECardConfig.BASE_URL + link;

				webView.loadUrl(link);
				webView.getSettings().setJavaScriptEnabled(true);
				webView.setWebViewClient(new WebViewClient() {
					@Override
					public boolean shouldOverrideUrlLoading(WebView view, String url) {
						if (url.contains("tel")) {
							String tel = url.substring(4);
							ActivityUtil.makeCall(HeadAdvActivity.this, tel);
						} else {
							webView.loadUrl(url);
						}
						return true; // true自身处理，false系统浏览器处理。
					}
				});
			}
		} else {

			if (info.isEvent == 1) {
				ECardUserInfo user = ECardJsonManager.getInstance()
						.getUserInfo();
				if (user != null && user.getId() !=null) {
					link = info.url + "/" + user.getId();
				} else {
					link = info.url;
				}
			} else {
				link = info.url;
			}

			if (link.charAt(0) == '/') {
				link = ECardConfig.BASE_URL + link;
			}

			webView.loadUrl(link);
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					if (url.contains("ecard:login")) {
						DMAccount.callLoginActivity(HeadAdvActivity.this, null);
					} else if (url.contains("ecard:ecard")) {
						// ecard:ecard:onSetCardId
						int index = url.indexOf(":");
						function = url.substring(index);
						ActivityUtil.startActivityForResult(
								HeadAdvActivity.this, MyECardActivity.class,
								SELECT_CARD);
					} else if(url.contains("tel")) {
						String tel = url.substring(4);
						ActivityUtil.makeCall(HeadAdvActivity.this, tel);
					} else {
						webView.loadUrl(url);
					}
					return true; // true自身处理，false系统浏览器处理。
				}
			});

		}
		
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_CARD) {

				int id = data.getIntExtra("id", -1);
				if (id != -1) {
					webView.loadUrl("javascript:" + function + "(" + id + ")");
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
