package com.citywithincity.ecard.selling.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshWebView;

public class SCardPagesDetail extends BaseActivity implements OnRefreshListener<WebView> {
	protected ProgressBar progressBar;
	protected WebView webView;
	protected PullToRefreshWebView PullToRefreshWebView;
	private String url;
	

	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.card_pages_detail);
		setTitle(R.string.card_pages_detail);
		
		url = (String) getIntent().getExtras().get("data");
		
		PullToRefreshWebView = (PullToRefreshWebView) findViewById(R.id._web_view);
		PullToRefreshWebView.setOnRefreshListener(this);
		PullToRefreshWebView.setMode(Mode.PULL_FROM_START);
		progressBar = (ProgressBar) findViewById(R.id._progress_bar);
		webView =  PullToRefreshWebView.getRefreshableView();

		webView.getSettings().setJavaScriptEnabled(true);
		webView.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(String url, String userAgent,
					String contentDisposition, String mimetype,
					long contentLength) {
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {

				if (newProgress == 100) {
					PullToRefreshWebView.onRefreshComplete();
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
				SCardPagesDetail.this.onReceivedTitle(view,title);
			}

		});
		
//		String url = ECardConfig.BASE_URL +"/index.php/api2/s_card_info2/index/"+SCardDetailActivity.id;
		webView.loadUrl(url);
	}
	
	

	/**
	 * 
	 * @param view
	 * @param title
	 */
	protected void onReceivedTitle(WebView view, String title) {
		
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<WebView> refreshView) {
		webView.reload();
	}
}
