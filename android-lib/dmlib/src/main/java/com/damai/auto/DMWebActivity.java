package com.damai.auto;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.damai.core.DMServers;
import com.damai.dmlib.LibBuildConfig;
import com.damai.lib.R;
import com.damai.webview.urlparser.OpenActivityUrlParser;
import com.damai.webview.urlparser.WebViewUrlParser;

public class DMWebActivity extends DMFragmentActivity implements OnClickListener, MessageUtil.IMessageListener {

	
	public static void register(String command,WebViewUrlParser parser){
		parserMap.put(command, parser);
	}
	
	private static Map<String, WebViewUrlParser> parserMap;
	static {
		parserMap = new HashMap<String, WebViewUrlParser>();
		parserMap.put("open", new OpenActivityUrlParser());
     //   parserMap.put("safe_ticket", new SafeTicktetParser());


	}

	private WebView webView;
	private View loadingView;
	
	
	protected WebView getWebView(){
		return webView;
	}
	
	
	@Override
	public void onBackPressed() {
		backToPrevious();
	}
	
	@Override
	protected void backToPrevious() {
		if(webView.canGoBack()){
			webView.goBack();
		}else{
			super.backToPrevious();
		}	
	}


    public static void openUrl(Activity context, String url, String title){
        openUrl(context,url,title,true);
    }

    // 加载
	public static void openUrl(Activity context, String url, String title,boolean parseTitle) {
		Intent intent = new Intent(context, DMWebActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("title", title);
		intent.putExtra("parseTitle",parseTitle);
		context.startActivity(intent);
	}

	protected boolean parseUrl(String url) {

        if(url.startsWith("tel:")){

            ActivityUtil.makeCall(this, url.substring(4));
            return true;

        }

		String part ;
        //lastpart
        int index = url.lastIndexOf('/')+1;
        if(index < url.length()){
            part = url.substring(index);
            if (part.startsWith("ecard:")) {
                String[] args = part.split(":");
                String command = args[1];
                WebViewUrlParser parser = parserMap.get(command);
                if (parser != null) {
                    parser.parseUrl(this, command, args);
                } else {
                    if (LibBuildConfig.DEBUG) {
                        System.err.println("找不到url解析器,命令:" + command);
                    }
                }
                return true;
            }
        }


		return false;
	}
	
	protected int getLayout(){
		return R.layout.web_view;
	}
	
	
	protected void overrideWebSettings(WebSettings webSettings){
		
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(timer!=null){

            timer.cancel();
            timer = null;
            task = null;
        }

    }
    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent,
                                    String contentDisposition, String mimetype, long contentLength) {
		/*	Log.i("tag", "url=" + url);
			Log.i("tag", "userAgent=" + userAgent);
			Log.i("tag", "contentDisposition=" + contentDisposition);
			Log.i("tag", "mimetype=" + mimetype);
			Log.i("tag", "contentLength=" + contentLength);*/
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(getLayout());
		webView = (WebView) findViewById(R.id._web_view);
		loadingView = findViewById(R.id._load_state_loading);

		// 访问的时候使用window.plat.xxxx
	//	webView.addJavascriptInterface(insertObj, "plat");
		findViewById(R.id._id_cancel).setOnClickListener(this);
		findViewById(R.id._id_refresh).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.reload();
			}
		});

        webView.setDownloadListener(new MyWebViewDownLoadListener());
     //   webView.getSettings().setSupportZoom(true);
      //  webView.getSettings().setDomStorageEnabled(true);
       // webView.getSettings().setAllowFileAccess(true);
       // webView.getSettings().setPluginsEnabled(true);
     //
     //   webView.getSettings().setBuiltInZoomControls(true);
      //  webView.requestFocus();
      //  webView.getSettings().setLoadWithOverviewMode(true);

     //   String pdfUrl = "http:xxx.pdf";
    //    webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" +pdfUrl);


        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new Callback());
        webView.setWebChromeClient(new webChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);

      //  JavascriptInterface javasriptInterface = new JavascriptInterface(this);
       // webView.addJavascriptInterface(javasriptInterface, "Print");

        overrideWebSettings(webView.getSettings());

		parseIntent(getIntent());

        if(getTitleWithJs){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                timer = new Timer();
                task = new TimerTask() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {

                        MessageUtil.sendMessage(DMWebActivity.this);
                    }
                };
                timer.schedule(task,200,200);
            }

        }
	}



    private Timer timer;
    private TimerTask task;

	private boolean parseTitle = true;

	protected void parseIntent(Intent intent){
		Bundle bundle = intent.getExtras();
		if(bundle!=null){
			String url = bundle.getString("url");
			String title = bundle.getString("title");
			boolean parseTitle = bundle.getBoolean("parseTitle");
			this.parseTitle = parseTitle;
			load(url);
			setTitle(title);
		}
	}


    public String getUrl(){
        return webView.getUrl();
    }


    protected void load(String url){
        Map<String, String> session = DMServers.querySession(url);
        if (session != null) {
            webView.loadUrl(url, session);
        } else {
            webView.loadUrl(url);
        }
    }
	

	/**
	 * 
	 * @param view
	 * @param title
	 */
	protected void onReceivedTitle(WebView view, String title) {
		if(title.startsWith("http")){
			return;
		}
		//setTitle(title);
	}

	@Override
	public void onClick(View v) {
		onExit();
	}

    protected void onExit(){
        finish();
    }


    protected boolean getTitleWithJs;

    @TargetApi(19)
	@Override
    public void onMessage(int message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("document.title", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    setTitle(decode(value.replace("\"","")));
                }
            });
        }
    }


    public static String decode(String unicodeStr) {
        if (unicodeStr == null) {
            return null;
        }
        StringBuffer retBuf = new StringBuffer();
        int maxLoop = unicodeStr.length();
        for (int i = 0; i < maxLoop; i++) {
            if (unicodeStr.charAt(i) == '\\') {
                if ((i < maxLoop - 5) && ((unicodeStr.charAt(i + 1) == 'u') || (unicodeStr.charAt(i + 1) == 'U')))
                    try {
                        retBuf.append((char) Integer.parseInt(unicodeStr.substring(i + 2, i + 6), 16));
                        i += 5;
                    } catch (NumberFormatException localNumberFormatException) {
                        retBuf.append(unicodeStr.charAt(i));
                    }
                else
                    retBuf.append(unicodeStr.charAt(i));
            } else {
                retBuf.append(unicodeStr.charAt(i));
            }
        }
        return retBuf.toString();
    }


    public void onPageFinished(WebView view, String url){



    }

    public void onPageStarted(WebView view, String url, Bitmap favicon){



    }

    protected boolean shouldOverrideUrlLoading(WebView view, String url){
        if (parseUrl(url)) {
            return true;
        }
        load(url);
        return true;
    }

    private class Callback extends WebViewClient{

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
           handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
           return DMWebActivity.this.shouldOverrideUrlLoading(view,url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(loadingView!=null){
                loadingView.setVisibility(View.GONE);
            }

            DMWebActivity.this.onPageFinished(view,url);
            super.onPageFinished(view,url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(loadingView!=null){
                loadingView.setVisibility(View.VISIBLE);
            }
            DMWebActivity.this.onPageStarted(view,url,favicon);
            super.onPageStarted(view, url, favicon);
        }


    }
private class webChromeClient extends WebChromeClient{


    public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {

        quotaUpdater.updateQuota(5 * 1024 * 1024);
    }
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
    //    MainActivity.this.setProgress(newProgress * 1000);
    }


    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {


        Alert.alert(DMWebActivity.this, "温馨提示", message, new DialogListener() {
            @Override
            public void onDialogButton(int id) {
                result.confirm();
            }
        });

        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
       if(parseTitle){
           setTitle(title);
       }
    }
}

}
