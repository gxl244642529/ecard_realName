package com.citywithincity.ecard.react;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.citywithincity.ecard.ReactEnterActivity;
import com.citywithincity.ecard.bus.BusActivity;
import com.citywithincity.ecard.discard.activities.DiscardActivity;
import com.citywithincity.ecard.insurance.activities.InsuranceHomeActivity;
import com.citywithincity.ecard.insurance.activities.InsuranceMyPolicyActivity;
import com.citywithincity.ecard.licai.LicaiActivity;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.models.vos.ECardVo;
import com.citywithincity.ecard.myecard.activities.BindECardActivity;
import com.citywithincity.ecard.myecard.activities.MyECardActivity;
import com.citywithincity.ecard.myecard.activities.MyECardDetailActivity;
import com.citywithincity.ecard.pingan.FundActivity;
import com.citywithincity.ecard.pingan.PinganActivity;
import com.citywithincity.ecard.recharge.activities.RechargeECardActivity;
import com.citywithincity.ecard.recharge.activities.RechargeOrderListActivity;
import com.citywithincity.ecard.recharge.activities.RechargeOtherActivity;
import com.citywithincity.ecard.selling.activities.SAllMyOrderActivity;
import com.citywithincity.ecard.selling.activities.SMainActivity;
import com.citywithincity.ecard.selling.activities.SMyCollectionActivity;
import com.citywithincity.ecard.selling.activities.SOrderListActivity;
import com.citywithincity.ecard.ui.activity.BusinessActivity;
import com.citywithincity.ecard.ui.activity.BusinessMainActivity;
import com.citywithincity.ecard.ui.activity.GoodCardActivity;
import com.citywithincity.ecard.ui.activity.MyCollectionActivity;
import com.citywithincity.ecard.ui.activity.NewsActivity;
import com.citywithincity.ecard.user.activities.QuestionActivity;
import com.citywithincity.ecard.user.activities.SettingActivity;
import com.citywithincity.ecard.user.activities.UserInfoActivity;
import com.citywithincity.ecard.utils.NfcUtil;
import com.citywithincity.ecard.yuanxin.YuanxinActivity;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.LifeManager;
import com.damai.core.DMAccount;
import com.damai.helper.DataHolder;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renxueliang on 17/3/21.
 */

public class SysModule extends ReactContextBaseJavaModule {

    private static Map<String,Class<?>> map = new HashMap<String,Class<?>>();


    static{
        map.put("yuanxin", YuanxinActivity.class);
        map.put("selling", SMainActivity.class);
        map.put("recharge", RechargeECardActivity.class);
        map.put("pingan", PinganActivity.class);
        map.put("licai", LicaiActivity.class);
        map.put("myecard", MyECardActivity.class);
        map.put("safe", InsuranceHomeActivity.class);
        map.put("bus", BusActivity.class);
        map.put("fund", FundActivity.class);
        map.put("discard", DiscardActivity.class);

        map.put("personInfo", UserInfoActivity.class);
        map.put("setting", SettingActivity.class);
        map.put("myorder", SAllMyOrderActivity.class);
        map.put("myrecharge", RechargeOrderListActivity.class);
        map.put("mysafe", InsuranceMyPolicyActivity.class);
        map.put("mycollection", SMyCollectionActivity.class);
        map.put("business", BusinessMainActivity.class);
        map.put("question", QuestionActivity.class);
        map.put("pickCard", GoodCardActivity.class);
        map.put("otherRecharge", RechargeOtherActivity.class);
        map.put("news", NewsActivity.class);
        map.put("bindECard", BindECardActivity.class);
        map.put("ecardDetail", StartMyDetail.class);

        //
        map.put("hce", StartHce.class);

    }


    public static interface IStartActivity{
        void start(Activity context,ReadableMap map);
    }

    public static class StartMyDetail implements IStartActivity{

        @Override
        public void start(Activity context, ReadableMap map) {
            Intent intent= new Intent(context,MyECardDetailActivity.class);

            ECardVo eCardVo = new ECardVo();
            eCardVo.setCardid( map.getString("cardid") );
            eCardVo.setCardIdExt( map.getString("cardidExt") );
            eCardVo.setCardName( map.getString("cardName") );
            eCardVo.setExpireTime( map.getString("expireTime") );
            eCardVo.setCardType( map.getString("cardType") );
            eCardVo.setCardFlag(map.getInt("cardFlag"));
            eCardVo.setCreateDate(map.getString("createDate"));
            /**
             * private String cardid;
             private String cardidExt;
             private String cardName;
             private String expireTime;
             private String cardType;
             */

            DataHolder.set(MyECardDetailActivity.class,eCardVo);
            context.startActivity(intent);
        }
    }

    public static class StartHce implements IStartActivity{

        @Override
        public void start(Activity context,ReadableMap map) {
           /* Intent intent= new Intent(context,SplashView.class);
          //  ECardUserInfo account = DMAccount.get();

            String hash =  map.getString("hash");   //(String)account.getData().get("hash");
            String name =  map.getString("userName");    //account.getAccount();
            String phone =  map.getString("phoneNum");     //account.getPhone();

            intent.putExtra("hash", hash );
            intent.putExtra("userName",name);
            intent.putExtra("phoneNum",phone );
            context.startActivity(intent);
            */
        }
    }

    public SysModule(ReactApplicationContext reactContext) {
        super(reactContext);
        sysModule = this;
    }

    @Override
    public String getName() {
        return "SysModule";
    }

    @ReactMethod
    public void hce(ReadableMap map){
      /*  Intent intent= new Intent(getCurrentActivity(),SplashView.class);
        //  ECardUserInfo account = DMAccount.get();

        String hash =  map.getString("hash");   //(String)account.getData().get("hash");
        String name =  map.getString("userName");    //account.getAccount();
        String phone =  map.getString("phoneNum");     //account.getPhone();

        intent.putExtra("hash", hash );
        intent.putExtra("userName",name);
        intent.putExtra("phoneNum",phone );
        getCurrentActivity().startActivity(intent);*/

    }

    /**
     *
     *
     * @param module
     */
    @ReactMethod
    public void callModule(
            String module,
            @Nullable
            @javax.annotation.Nullable
            ReadableMap data
    ){
        Class<?> clazz = map.get(module);
        if(clazz != null){
            if(Activity.class.isAssignableFrom(clazz)){
                Activity activity = getCurrentActivity();
                if( activity!=null){
                    activity.startActivity(new Intent(activity,clazz));
                }
            }else{
                IStartActivity activity = null;
                try {
                    activity = (IStartActivity) clazz.newInstance();
                    activity.start(getCurrentActivity(),data);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @ReactMethod
    public boolean nfc(){
        return NfcUtil.isAvailable(getCurrentActivity());
    }


    private static SysModule sysModule;

    public static SysModule getRunningInstance() {
        return sysModule;
    }


    public static void onResume(){
        if(!isStartup){
            return;
        }
        SysModule instance = getRunningInstance();
        if(instance!=null){
            instance.getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("viewWillAppear", Arguments.createMap());//对应的javascript层的事件名为logInConsole，注册该事件即可进行回调
        }
    }


    public static void onPause(){
        if(!isStartup){
            return;
        }
        SysModule instance = getRunningInstance();
        if(instance!=null){
            instance.getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("viewWillDispear", Arguments.createMap());//对应的javascript层的事件名为logInConsole，注册该事件即可进行回调
        }
    }

    private static boolean isStartup = false;

    @ReactMethod
    public void onStartup(){
        isStartup = true;
        ReactEnterActivity activity = (ReactEnterActivity)getCurrentActivity();
        if(activity!=null){
            activity.hide();
        }

    }


    @ReactMethod
    public void enableCapture(boolean enable){

        ReactEnterActivity activity = LifeManager.findActivity(ReactEnterActivity.class);
        if(activity==null){
            return;
        }
        activity.enableCapture(enable);

    }


    public static final String SUB_FIRST_USE = "first_use";
    //private PluginManager manager;
    public static final String SUB_VERSON_CODE = "version_code";

    public static boolean isFirstUse(Context context){
        SharedPreferences sp = context.getSharedPreferences("default",
                Context.MODE_PRIVATE);
        // 是否是第一次使用
        boolean isFirstUse = sp.getBoolean(SUB_FIRST_USE, true);
        int versionCode = sp.getInt(SUB_VERSON_CODE, 0);
        if (isFirstUse || (versionCode < PackageUtil.getVersionCode(context))) {
            return true;
        }
        return false;
    }

    @ReactMethod
    public void setFirstRead(){
        SharedPreferences sp = getCurrentActivity().getSharedPreferences("default",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(SUB_FIRST_USE, false);
        editor.putInt(SUB_VERSON_CODE,PackageUtil.getVersionCode(getCurrentActivity()));
        editor.commit();
    }


    /**
     *
     * @param eventName
     * @param value
     */
    public void emmit(String eventName,WritableMap value){
        getReactApplicationContext().getJSModule( DeviceEventManagerModule.RCTDeviceEventEmitter.class )
                .emit(eventName,value);
    }
}
