package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.models.vos.NewsInfo;
import com.citywithincity.ecard.ui.base.BaseWebViewActivity;

public class NewsDetailActivity extends BaseWebViewActivity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("新闻资讯");
		NewsInfo info = (NewsInfo)getIntent().getExtras().get("data");
		String url=ECardConfig.BASE_URL + "/app_news/news/"+info.id;
		webView.loadUrl(url);
	}

	

}
