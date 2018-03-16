package com.citywithincity.ecard.models;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import com.citywithincity.ecard.PinganConfig;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.utils.NfcUtil;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.models.LocalData;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.PackageUtil;
import com.damai.core.DMAccount;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;

import java.io.File;

public class TianYu {

	public static final int EXAM = 1;
	public static final int RECHARGE = 2;

	// 订单号 orderCode
	public static final String ORDER_CODE = "orderCode";
	// 登录账号 loginName
	public static final String LOGIN_NAME = "loginName";
	// 金额
	public static final String AMOUNT = "amount";
	//他人补登
	public static final String OTHER = "other";
	
	public static final String COMP_NAME = PinganConfig.COMP_NAME;
	public static final String EXAM_CLASS_NAME = PinganConfig.COMP_NAME+".cardbag.model.budeng.ValidateActivity";

	public static final String RECHARGE_CLASS_NAME = PinganConfig.COMP_NAME+".cardbag.model.budeng.BoardChargeActivity";

	private static final String EXAM_APK = "xmytkClient_nfc.apk";

	public static boolean ACTION_RECHARGE = false;
	
	private static File apkFile;
	
	public static final int TIANYU_REQUEST_RECHARGE = 300;
	
	public static final int TIANYU_REQUEST_EXAM = 301;
	

	public static void startExam(Activity context) {
		startTyModule(context, EXAM_CLASS_NAME, null,TIANYU_REQUEST_EXAM);
	}
	
	public interface ITianyuModel{
		void startTyModule(Activity context, String moduleName,Intent intent,int requestCode) ;
	}

	public static void startRecharge(Activity context, String account, String cardId, String orderId,int fee,boolean isOther) {
		if (NfcUtil.isAvailable(context)) {
			DMAccount info = DMAccount.get();
			Intent intent = new Intent();
			intent.putExtra("cardpan", cardId);
			intent.putExtra(ORDER_CODE, orderId);
			intent.putExtra(LOGIN_NAME, account == null ? info.getAccount() : account);
			intent.putExtra(AMOUNT, String.format("%.2f", (float)fee/100));
			intent.putExtra(OTHER, isOther ? 1 : 0);
			startTyModule(context, RECHARGE_CLASS_NAME, intent,TIANYU_REQUEST_RECHARGE);
		} else {
			Alert.showShortToast("您的手机不支持nfc设备");
		}
	}
	
	private static ITianyuModel gModel;
	
	public static void setModel(ITianyuModel model){
		gModel = model;
	}

	private static final String CURRENT_TY_VERSION = "currentTyVersion1";


    private static Intent _intent;
    private static String _moduleName;
    private static int _requestCode;

	/**
	 * 启动天喻模块
	 */
	private static void startTyModule(final Activity context, String moduleName,
			Intent intent,int requestCode) {
		//gModel.startTyModule(context, moduleName, intent,requestCode);
		//66版本，并且还没有安装新的，则使用本地的
		apkFile = new File(context.getFilesDir().getPath(), EXAM_APK);
		if (apkFile.exists()) {
			apkFile.delete();
		}
		apkFile = PackageUtil.extractAssertsToFile(context, EXAM_APK);
		if(apkFile==null || !apkFile.exists()){

			Alert.alert(context,"安装插件失败，请确认sdk卡具有读卡权限");
			return;
		}


        int version = LocalData.read().getInt(CURRENT_TY_VERSION,0);
        if(version < PackageUtil.getVersionCode(context)){
            _intent =intent;
            _moduleName = moduleName;
            _requestCode = requestCode;
            if(installed(context)){
                confirmUninstallOldAndInstallNew(context);
            }else {
                installApk(context);
            }

        }else{


            if (installed(context) && !isApkNewVersion(context)) {
                ACTION_RECHARGE = intent != null;
                runPackage(context, intent, COMP_NAME, moduleName,requestCode);
            } else {
                _intent =intent;
                _moduleName = moduleName;
                _requestCode = requestCode;

                if (isApkNewVersion(context)) {
                    confirmUninstallOldAndInstallNew(context);
                } else {
                    //安装新版本
                    installApk(context);
                }

            }
        }


	}



    public static boolean runPackage(Context context, Intent intent, String packageName, String className, int requestCode) {
        try {
            PackageInfo e = context.getPackageManager().getPackageInfo(packageName, 1);
            if(e != null && e.activities != null) {
                if(intent == null) {
                    intent = new Intent("android.intent.action.MAIN");
                }

                intent.setAction("android.intent.action.VIEW");
                intent.setClassName(e.packageName, className);
                if(requestCode > 0) {
                    ((Activity)context).startActivityForResult(intent, requestCode);
                } else {
                    intent.addFlags(268435456);
                    context.startActivity(intent);
                }

                return true;
            }
        } catch (NameNotFoundException var6) {
            var6.printStackTrace();
        } catch (Exception var7) {
            var7.printStackTrace();
        }

        return false;
    }


    private static void recordInstallOkAndStartModule(final Activity context){
        LocalData.write().putInt( CURRENT_TY_VERSION ,
                PackageUtil.getVersionCode(context) )
                .save();

        ACTION_RECHARGE = _intent != null;
        runPackage(context,_intent, COMP_NAME, _moduleName,_requestCode);
        _intent = null;
    }


    private static final void installApk(final Activity context){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(apkFile),
                "application/vnd.android.package-archive");

        if(context instanceof ActivityResultContainer){

            ActivityResultContainer container = (ActivityResultContainer)context;
            container.startActivityForResult(new ActivityResult() {
                @Override
                public void onActivityResult(Intent intent, int resultCode, int requestCode) {
                    if(requestCode == REQUEST_INSTALL){
                        if(PackageUtil.isPackageInstalled(context, COMP_NAME)){
                           //进入天谕模块
                            MessageUtil.sendMessage(new MessageUtil.IMessageListener() {
                                @Override
                                public void onMessage(int message) {
                                    recordInstallOkAndStartModule(context);
                                }
                            });
                        }

                    }
                }
            },intent, REQUEST_INSTALL );
        }else{
            PackageUtil.installApk(context, apkFile);
        }

        //   LifeManager.getCurrent().startActivityForResult(PluginManager.ApkManager.this, intent, REQUEST_INSTALL);
      //
    }



    private static final int RQUEST_DELETE = 1;

    private static final int REQUEST_INSTALL = 2;

    private static void uninstallOldAndInstallNew(final Activity context){
        // 通过程序的报名创建URI
        Uri packageURI = Uri.parse("package:" + COMP_NAME);
        // 创建Intent意图
        Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);

        if(context instanceof ActivityResultContainer){
            ActivityResultContainer container = (ActivityResultContainer)context;
            container.startActivityForResult(new ActivityResult() {
                @Override
                public void onActivityResult(Intent intent, int resultCode, int requestCode) {
                    if(requestCode == RQUEST_DELETE){
                        if(PackageUtil.isPackageInstalled(context, COMP_NAME)){
                            Alert.showShortToast("取消卸载");
                            return;
                        }
                        MessageUtil.sendMessage(new MessageUtil.IMessageListener() {
                            @Override
                            public void onMessage(int message) {
                                //进入安装
                                installApk(context);
                            }
                        });


                    }
                }
            },intent, RQUEST_DELETE );

        }else{
            // 执行卸载程序
            context.startActivity(intent);
            MessageUtil.sendMessage(0, new MessageUtil.IMessageListener() {

                @Override
                public void onMessage(int message) {
                    while (installed(context)) {
                        //等待卸载完成
                    }
                    //安装新版本
                    PackageUtil.installApk(context, apkFile);
                }
            });
        }

     //   LifeManager.getCurrent().startActivityForResult(PluginManager.ApkManager.this, intent, RQUEST_DELETE);




    }

    private static void confirmUninstallOldAndInstallNew(final Activity context){
        Alert.confirm(context, "卟噔新版本", "安装新版本前，需要先卸载旧版本。", new DialogListener() {

            @Override
            public void onDialogButton(int id) {
                if (id == R.id._id_ok) {

                    uninstallOldAndInstallNew(context);
                }
            }
        });
    }
	
	private static boolean installed(Activity context) {

		boolean installed = PackageUtil.isPackageInstalled(context, COMP_NAME);
        // apk安装包信息
        return installed;
	}
	
	private static boolean isApkNewVersion(Activity context) {
		// apk安装包信息
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(apkFile.getAbsolutePath(), PackageManager.GET_ACTIVITIES);
		if (info != null) {
			PackageInfo appInfo = null;
			try {
				appInfo = context.getPackageManager().getPackageInfo(COMP_NAME, 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			if (appInfo != null) {
                //还没安装最新版本
                return info.versionCode - appInfo.versionCode > 0;
			}
		}
		return false;
	}

}
