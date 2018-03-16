package com.citywithincity.ecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

//import com.allcitygo.qrlib.QRSdk;
import com.citywithincity.ecard.discard.vos.BookInfo;
import com.citywithincity.ecard.push.PushImpl;
import com.citywithincity.ecard.react.ECardReactPackage;
import com.citywithincity.utils.PackageUtil;
import com.damai.amap.AMapLocationService;
import com.damai.core.DMAccount;
import com.damai.core.LoginListener;
import com.damai.core.LoginListenerWrap;
import com.damai.interfaces.DMLoginCaller;
import com.damai.map.LocationInfo;
import com.damai.map.LocationListener;
import com.damai.map.LocationUtil;
import com.damai.push.Push;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;

public class MainApplication extends ECardApplication implements ReactApplication, DMLoginCaller, LocationListener {
  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.asList(
              new MainReactPackage(),new ECardReactPackage()
      );
    }
  };


  @Override
  public void onCreate() {
    super.onCreate();
    //看下有没有，通过反射

    /*
    String config = PackageUtil.getMetaValue(getApplicationContext(),"TSP");
    String[] args = config.split("\\|");

      QRSdk.setHost(Boolean.parseBoolean(args[0]),args[1],Integer.parseInt(args[2]));//true 表示 https
      QRSdk.getDeviceInfo(getApplicationContext());
    */

    //定位
      LocationUtil.setLocationService(new AMapLocationService(getApplicationContext()));
      LocationUtil.getLocation(this);

    SoLoader.init(this, /* native exopackage */ false);
    DMAccount.setLoginCaller(this);

  }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);

    }

    @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }



  @Override
  public void callLogin( Activity activity, LoginListener loginListener) {
    LoginListenerWrap.getInstance().setListener(loginListener);
      activity.startActivity(new Intent(activity.getPackageName() + ".action.LOGIN"));
  }

    @Override
    public void onGetLocation(LocationInfo locationInfo) {

    }
}
