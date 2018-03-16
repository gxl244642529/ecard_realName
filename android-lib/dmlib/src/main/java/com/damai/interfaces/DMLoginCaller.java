package com.damai.interfaces;

import android.app.Activity;

import com.damai.core.LoginListener;

/**
 * Created by renxueliang on 17/1/1.
 */

public interface DMLoginCaller {
    void callLogin(Activity activity,LoginListener listener);
}
