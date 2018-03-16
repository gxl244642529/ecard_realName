package com.citywithincity.ecard.push;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.citywithincity.utils.MD5Util;
import com.damai.auto.LifeManager;
import com.damai.core.DMAccount;
import com.damai.core.DeviceUtil;
import com.damai.push.IPush;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by renxueliang on 17/3/30.
 */

public class PushImpl implements IPush{
    private final Context context;

    public PushImpl(Context context){
        this.context = context;
    }

    public void onLogout(){
        JPushInterface.setAlias(LifeManager.getActivity(), "", new TagAliasCallback() {
            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onLogin(DMAccount account) {
        JPushInterface.setAlias(LifeManager.getActivity(), account.getAccount(), new TagAliasCallback() {

            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                System.out.print("绑定alis成功");
            }
        });
    }

    public String getPushId(){
        return JPushInterface.getRegistrationID(context);
    }
    private String deviceId;
    @Override
    public String getUdid() {
        if (deviceId == null) {
            synchronized (PushImpl.class) {
                if (deviceId == null) {
                    final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    final String tmDevice, tmSerial, androidId;
                    tmDevice = tm.getDeviceId();
                    tmSerial = null;// tm.getSimSerialNumber();
                    androidId = android.provider.Settings.Secure
                            .getString(context.getContentResolver(),
                                    android.provider.Settings.Secure.ANDROID_ID);

                    UUID deviceUuid = new UUID(androidId.hashCode(),
                            ((long) tmDevice.hashCode() << 32)
                                    | (tmSerial!=null ?  tmSerial.hashCode() : 0));
                    deviceId = MD5Util.md5Appkey(deviceUuid.toString());

                }
            }
        }
        return deviceId;
    }
}
