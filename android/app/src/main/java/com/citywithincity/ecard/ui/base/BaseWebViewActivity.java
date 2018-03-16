package com.citywithincity.ecard.ui.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

@SuppressLint("SetJavaScriptEnabled")
public class BaseWebViewActivity extends BaseActivity implements
		OnRefreshListener<WebView> {
	protected ProgressBar progressBar;
	protected PullToRefreshWebView pwb;
	protected WebView webView;


	/**
	 * 
	 * @param view
	 * @param title
	 */
	protected void onReceivedTitle(WebView view, String title) {
		
	}
	
	@Override
	public void onBackPressed() {
		if(webView.canGoBack()){
			webView.goBack();
		}else{
			super.backToPrevious();
		}
	}
	
	@Override
	protected void backToPrevious() {
		if(webView.canGoBack()){
			webView.goBack();
		}else{
			super.backToPrevious();
		}
		
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<WebView> refreshView) {
		webView.reload();
	}

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.base_web_view);
		pwb = (PullToRefreshWebView) findViewById(R.id._web_view);
		pwb.setOnRefreshListener(this);
		pwb.setMode(Mode.PULL_FROM_START);
		progressBar = (ProgressBar) findViewById(R.id._progress_bar);
		webView =  pwb.getRefreshableView();
		
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				webView.loadUrl(url);
				return true;
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {

				if (newProgress == 100) {
					pwb.onRefreshComplete();
					progressBar.setVisibility(View.GONE);
				} else {
					if (progressBar.getVisibility() == View.GONE)
						progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress(newProgress);
				}

				super.onProgressChanged(view, newProgress);
			}
			@Override
			public void onReceivedTitle(WebView view, String title) {
				BaseWebViewActivity.this.onReceivedTitle(view,title);
			}

		});
		
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
}
