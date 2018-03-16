package com.citywithincity.ecard.pay.cmbpay;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMWebActivity;

import cmb.pb.util.CMBKeyboardFunc;

public class CMBPayWebActivity extends DMWebActivity {

    /**
     * 开始的页面为PrePayEUserP
     * http://61.144.248.29:801/netpayment/BaseHttp.dll?MB_EUserP_PayOK
     * @param webSettings
     */


	@Override
	protected void overrideWebSettings(WebSettings webSettings) {
		
		webSettings.setSaveFormData(false);
		webSettings.setSavePassword(false);
	}
	
	@Override
	protected int getLayout() {
		return R.layout.activity_cmb_pay;
	}

	@Override
	protected boolean parseUrl(String url) {
        Log.d("CMB",url);
		CMBKeyboardFunc func = new CMBKeyboardFunc(this);
		if(func.HandleUrlCall(getWebView(), url)){
			return true;
		}
		
		int index = url.indexOf("ecard:");
		if(index>0 && index < 30)
		{
			//成功页面
			setResult(RESULT_OK);
			finish();
			return true;
		}
		return false;
	}



    private boolean isSuccess(String url){
        if(url==null){
            return false;
        }
        return url.endsWith("MB_EUserP_PayOK");
    }

    @Override
    public void onBackPressed() {
        this.backToPrevious();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if(isSuccess(url)){
            Button textView = (Button) findViewById(R.id._id_refresh);
            textView.setText("完成");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    @Override
	protected void backToPrevious() {
        String url = getUrl();
        if(isSuccess(url)){
            setResult(RESULT_OK);
            finish();
            return;
        }
        if(getWebView().canGoBack()) {
            getWebView().goBack();
        } else {

            Alert.confirm(this, "温馨提示", "取消付款吗?", new DialogListener() {

                @Override
                public void onDialogButton(int id) {
                    if (id == R.id._id_ok) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                }
            });

        }
	}

    @Override
    protected void onExit() {
        String url = getUrl();
        if(isSuccess(url)){
            setResult(RESULT_OK);
            finish();
            return;
        }
        super.onExit();
    }
}
