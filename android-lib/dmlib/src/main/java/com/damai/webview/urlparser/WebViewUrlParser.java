package com.damai.webview.urlparser;

import android.content.Context;

public interface WebViewUrlParser {
	/**
	 * 打开网址
	 * @param command
	 * @param args
	 */
	void parseUrl(Context context,String command,String[] args);
}
