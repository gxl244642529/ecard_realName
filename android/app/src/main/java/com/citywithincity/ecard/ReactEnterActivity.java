package com.citywithincity.ecard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.citywithincity.ecard.activities.AdvActivity;
import com.citywithincity.ecard.myecard.models.MyECardModel;
import com.citywithincity.ecard.nfc.ECardNfcModel;
import com.citywithincity.ecard.react.ECardReactActivityDelegate;
import com.citywithincity.ecard.react.ECardReactUtils;
import com.citywithincity.ecard.react.NfcModule;
import com.citywithincity.ecard.react.SysModule;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.damai.auto.DMWebActivity;
import com.damai.core.DMLib;
import com.damai.core.ILife;
import com.damai.core.IdManager;
import com.damai.core.IdReflect;
import com.damai.helper.ActivityResult;
import com.damai.http.api.a.JobSuccess;
import com.damai.util.ViewUtil;
import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.bridge.Arguments;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReactEnterActivity extends ReactActivity implements IViewContainer {


    @Override
    protected String getMainComponentName() {
        return "ecard";
    }


    private ECardNfcModel nfcModel;

    private Set<ILife> lifeSet;
    private IdReflect idReflect;

    public ReactEnterActivity() {
        // 这里肯定是主要的idManager
        idReflect = IdManager.getDefault();
    }

    private ECardReactActivityDelegate mDelegate;
    @Override
    protected ReactActivityDelegate createReactActivityDelegate() {
        mDelegate = new ECardReactActivityDelegate(this, getMainComponentName());
        return mDelegate;
    }


    private boolean isPushHanled = false;

    public void hide(){
        mDelegate.hide();
        //这里处理推送消息
        if(!isPushHanled){
            Bundle bundle = getIntent().getExtras();
            if(bundle!=null){
                String title = bundle.getString("title");
                if(bundle.containsKey("alert")){
                    String alert = bundle.getString("alert");
                    Alert.alert(this, alert);
                }else{
                    if(bundle.containsKey("web")){
                        String url = bundle.getString("url");
                        AdvActivity.openUrl(this, url, title);
                    }else{
                        //远程的模块
                        ECardReactUtils.notifyObservers("onPush",bundle);
                    }

                }
            }
            isPushHanled = true;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcModel = new ECardNfcModel(this);
        if(NfcModule.getRunningInstance()!=null){
            nfcModel.setListener(NfcModule.getRunningInstance());
        }


      //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        ViewUtil.initParam(this);
        com.citywithincity.utils.ViewUtil.initParam(this);
        //首先解析
        DMLib.getJobManager().onCreate(this);
        DMLib.getJobManager().onViewCreate(this);

    }


    @JobSuccess({"ecard/bind", MyECardModel.unbind,"ecard/update",MyECardModel.bindBarcode})
    public void onRenameSuccess(Object value){

        Log.i("ECARD","test");

        SysModule.getRunningInstance().emmit("ecardUpdate", Arguments.createMap());

    }

    public void enableCapture(final boolean enable){
        MessageUtil.sendMessage(new MessageUtil.IMessageListener() {
            @Override
            public void onMessage(int i) {
                if(enable){
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }else{
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
                }
            }
        });

    }

    // 这里需要确认是哪一个
    @Override
    public void addLife(ILife life) {
        if (lifeSet == null) {
            lifeSet = new HashSet<ILife>();
        }
        if (!lifeSet.contains(life)) {
            lifeSet.add(life);
        }

    }

    @Override
    protected void onDestroy() {
        idReflect = null;
        DMLib.getJobManager().onDestroy(this);
        lifeSet = null;
        actiMap = null;
        super.onDestroy();
    }


    @Override
    public void onNewIntent(Intent intent) {
        if (lifeSet != null) {
            for (ILife life : lifeSet) {
                life.onNewIntent(intent, this);
            }
        }
        nfcModel.onNewIntent(intent);
    }

    @Override
    protected void onPause() {
        nfcModel.onPause(this);
        if (lifeSet != null) {
            for (ILife life : lifeSet) {
                life.onPause(this);
            }
        }
        super.onPause();
        if(needsStatistics()){

        }

        SysModule.onPause();
    }


    protected boolean needsStatistics(){
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        nfcModel.onResume(this);
        if(needsStatistics()){

        }

        if (lifeSet != null) {
            for (ILife life : lifeSet) {
                life.onResume(this);
            }
        }
        DMLib.getJobManager().onResume(this);
        SysModule.onResume();
    }

    private Map<Integer, ActivityResult> actiMap;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*if (result != null) {
			result.onActivityResult(data, resultCode, requestCode);
			result = null;
		}*/
        if(actiMap!=null){
            ActivityResult result = actiMap.get(requestCode);
            if (result != null) {
                result.onActivityResult(data, resultCode, requestCode);
                actiMap.remove(requestCode);
                if(actiMap.size()==0){
                    actiMap = null;
                }
            }
        }
        super.onActivityResult(requestCode,resultCode,data);
    }

    @SuppressLint("UseSparseArrays")
    public void startActivityForResult(ActivityResult result, Intent intent,
                                       int requestCode) {
        if(actiMap==null){
            actiMap = new HashMap<Integer, ActivityResult>();
        }
        actiMap.put(requestCode, result);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public View findViewByName(String name) {
        // 查找
        Resources resources = getResources();
        int id = resources.getIdentifier(name, "id", getPackageName());
        return findViewById(id);
    }

    @Override
    public int getViewId(String name) {
        Resources resources = getResources();
        return resources.getIdentifier(name, "id", getPackageName());
    }

    /**
     *
     */
    public String idToString(int id) {
        return idReflect.idToString(id);
    }

}
