package com.citywithincity.ecard.react;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.utils.FragmentUtil;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.LifeManager;
import com.damai.core.DMAccount;
import com.damai.core.DMServers;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;

import javax.annotation.Nullable;

/**
 * Created by renxueliang on 16/12/19.
 */

public class ECardReactActivityDelegate extends ReactActivityDelegate {


    private Activity context;

    public ECardReactActivityDelegate(Activity activity, @Nullable String mainComponentName) {
        super(activity, mainComponentName);
        this.context = activity;
    }

    public ECardReactActivityDelegate(FragmentActivity fragmentActivity, @Nullable String mainComponentName) {
        super(fragmentActivity, mainComponentName);
        this.context = fragmentActivity;
    }


    @Nullable
    @Override
    protected Bundle getLaunchOptions() {
        Bundle bundle;

        if(DMAccount.isLogin()){
            ECardUserInfo account = DMAccount.get();
            bundle =  account.toBundle(context);
        }else{
            bundle = new Bundle();
        }
        bundle.putBoolean("isFirst", SysModule.isFirstUse(context));
        bundle.putString("imageUrl", DMServers.getUrl(0) );
        bundle.putInt("version",PackageUtil.getVersionCode(this.context));
        return bundle;

    }




    public void hide() {
        MessageUtil.sendMessage(new MessageUtil.IMessageListener() {
            @Override
            public void onMessage(int i) {
                if(root.getChildCount() > 1){
                    root.removeViewAt(1);
                }

            }
        });

    }


    public static class SplashFragment extends Fragment{

        @Override
        public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @android.support.annotation.Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @android.support.annotation.Nullable ViewGroup container, @android.support.annotation.Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.splash_activity,null,false);
        }
    }

    private FrameLayout root;

    protected void loadApp(String appKey) {
        ReactRootView rootView = createRootView();
        rootView.startReactApplication(
                getReactNativeHost().getReactInstanceManager(),
                appKey,
                getLaunchOptions());
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setId(R.id._container);
        frameLayout.addView(rootView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        View view = context.getLayoutInflater().inflate(R.layout.splash_activity,null,false);
        frameLayout.addView(view,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root = frameLayout;
        context.setContentView(frameLayout);

    }
}
