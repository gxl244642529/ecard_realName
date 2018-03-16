package com.citywithincity.ecard.user;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

/**
 * 一个倒计时的类
 */
public class MyDownTime extends CountDownTimer {

	private View btnView;
	private TextView timeDown;
	
    public MyDownTime(long millisInFuture, long countDownInterval,View btn, TextView text) {     
        super(millisInFuture, countDownInterval); 
        btnView = btn;
        timeDown =text;
    }
    
    @Override     
    public void onFinish() {     
    	btnView.setClickable(true);
    	timeDown.setText("重发验证码");        
    }
    
    @Override     
    public void onTick(long millisUntilFinished) {     
    	timeDown.setText("(" + millisUntilFinished / 1000 + ")" + "重发验证码");     
    }    
}
