package com.damai.push;

import com.damai.core.DMAccount;

/**
 * Created by renxueliang on 17/3/30.
 */

public interface IPush {

    String getPushId();
    String getUdid();

    void onLogout();
    void onLogin(DMAccount account);

}
