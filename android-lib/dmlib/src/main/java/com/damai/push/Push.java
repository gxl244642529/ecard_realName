package com.damai.push;

import android.text.TextUtils;

import com.citywithincity.models.LocalData;
import com.citywithincity.models.cache.CachePolicy;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.core.JobManager;

import java.util.Set;

public class Push {


	private static IPush _push;

    public static void setPush(IPush push){
        _push = push;
    }

	public static String getPushId(){
		return _push.getPushId();
	}

    public static void onLogout() {
        if(_push!=null){
            _push.onLogout();
        }

    }

    public static void onLogin(DMAccount account){
        _push.onLogin(account);
    }

    public static String getUdid(){
        return _push.getUdid();
    }


    public static String PUSH_ID="pushID";
    public static String PUSH_ENABLE = "pushEnable";


    public static void setEnable(boolean enable){
        isEnable = enable;
        LocalData.write().putBoolean(PUSH_ENABLE, enable).destroy();
    }

    private static Boolean isEnable;

    public static boolean isEnable(){
        if(isEnable==null){
            isEnable= LocalData.read().getBoolean(PUSH_ENABLE, false);
        }
        return isEnable;
    }




}
