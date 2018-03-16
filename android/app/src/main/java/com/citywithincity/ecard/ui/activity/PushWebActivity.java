package com.citywithincity.ecard.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.ui.base.BaseWebViewActivity;
import com.damai.core.DMAccount;

public class PushWebActivity extends BaseWebViewActivity {

	public static final int SELECT_CARD = 1;
	private int id;
	private String url;
	private String title;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// this._layoutResID = R.layout.news_detail;
		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		id = bundle.getInt("id");
		title = bundle.getString("title");
		url = bundle.getString("url");

		setTitle(title);

	}

	private String function;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onResume() {
		loadURL();

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.contains("ecard:login")) {
					DMAccount.callLoginActivity(PushWebActivity.this, null);
				} else if (url.contains("ecard:ecard")) {
					int index = url.indexOf(":");
					function = url.substring(index);

					// ActivityUtil.startActivityForResult(PushWebActivity.this,
					// NewSelectMyCardActivity.class, SELECT_CARD);
				} else {
					webView.loadUrl(url);
				}
				return true; // true自身处理，false系统浏览器处理。
			}
		});
		super.onResume();
	}

	private void loadURL() {
		if (url != null && url != "") {
			if (url.charAt(0) == '/') {
				url = ECardConfig.BASE_URL + url;
				if (ECardJsonManager.getInstance().isLogin()) {
					webView.loadUrl(url);
				} else {
					DMAccount.callLoginActivity(this, null);
				}
			} else {
				webView.loadUrl(url);
			}
		} else {
			url = ECardConfig.BASE_URL + "/index.php/api2/push_detail/index/"
					+ id;
			webView.loadUrl(url);
		}
	}

	@Override
	public void finish() {
		super.finish();
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