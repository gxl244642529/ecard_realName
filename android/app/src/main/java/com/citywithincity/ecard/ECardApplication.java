package com.citywithincity.ecard;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;

import com.citywithincity.ecard.enums.Actions;
import com.citywithincity.ecard.interfaces.IECardJsonTaskManager;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.TianYu;
import com.citywithincity.ecard.models.TianYu.ITianyuModel;
import com.citywithincity.ecard.pay.ECardPay;
import com.citywithincity.ecard.pay.PayTypes;
import com.citywithincity.ecard.pay.cmbpay.CMBPay;
import com.citywithincity.ecard.pay.umapay.UMAPay;
import com.citywithincity.ecard.push.PushImpl;
import com.citywithincity.ecard.utils.NfcUtil;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.LruImageCache;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.paylib.ECardPayModel;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.DefaultLocalDataUtil;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.DMApplication;
import com.damai.auto.LifeManager;
import com.damai.core.DMServers;
import com.damai.core.HttpJob;
import com.damai.core.JobManagerImpl;
import com.damai.core.PhpServerHandler;
import com.damai.dl.PluginManager;
import com.damai.dl.PluginManager.IApkListener;
import com.damai.pay.AbsDMPay;
import com.damai.push.IPush;
import com.damai.widget.DownloadProgressDialog;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

public class ECardApplication extends DMApplication implements OnDismissListener,IApkListener, ITianyuModel {


	@Override
	public AbsDMPay createPay(int type) {
		if(type == PayTypes.PayType_CMB){
			return new CMBPay();
		}else if(type == PayTypes.PayType_ECard){
			return new ECardPay();
		}else if(type == PayTypes.PayType_UMA){
			return new UMAPay();
		}
		
		return null;
	}


	@Override
	protected boolean registerApiServerHandler(JobManagerImpl jobManager, Context context) {
        DMServers.setUrl(0, PackageUtil.getMetaValue(context, "PHP_URL"));
        DMServers.setUrl(1, PackageUtil.getMetaValue(context, "JAVA_URL"));
		jobManager.registerApiServerHandler(0,new PhpServerHandler(context));
		jobManager.registerApiServerHandler(1,new NewJavaServerHandler(context));

		return true;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		ex.printStackTrace();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	@Override
	protected void initApplication() {
		DefaultLocalDataUtil.setContext(getApplicationContext());
		TianYu.setModel(this);
		Actions.init(getApplicationContext());
	}


	private DownloadProgressDialog dlgDialog;
	@Override
	public void onLoadApkComplete(File file) {
		dlgDialog.dismiss();
		dlgDialog = null;
		
	}

	@Override
	public void onProgress(int total, int current) {
		dlgDialog.setProgress(total, current);
	}

	@Override
	public void onLoadStart(HttpJob job) {
		dlgDialog = new DownloadProgressDialog(LifeManager.getActivity(), job);
	}

	@Override
	public void onLoadError() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();


		if (isEmulator()) {
			android.os.Process.killProcess(android.os.Process.myPid());
		}


		DefaultLocalDataUtil.setContext(getApplicationContext());
		Alert.setApplicationContext(getApplicationContext());
		JsonTaskManager.javaServerUrl = PackageUtil.getMetaValue(getApplicationContext(), "JAVA_URL") + "/api/";
		ECardConfig.JAVA_SERVER = JsonTaskManager.javaServerUrl;
		IECardJsonTaskManager platform = ECardJsonManager.getInstance();
		ECardConfig.BASE_URL = PackageUtil.getMetaValue(getApplicationContext(), "PHP_URL");
		ECardConfig.API_URL = ECardConfig.BASE_URL + "/api2/";

		ECardConfig.PICC = PackageUtil.getMetaValue(getApplicationContext(), "PICC");

		platform.setBaseUrl(ECardConfig.API_URL);
		platform.setApplication(this);
		platform.setImageCache(new LruImageCache());

		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		crashHandler.setUploader(new ECardCrashUploader());

	//	Alert.setOnDismissListener(this);

		JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush

//		MessageUtil.init();

		ECardPayModel.WEIXIN_APPID = ECardConfig.WEIXIN_APPID;

		// platform.clearSession();


		/**** Beta高级设置*****/
		/**
		 * true表示app启动自动初始化升级模块；
		 * false不好自动初始化
		 * 开发者如果担心sdk初始化影响app启动速度，可以设置为false
		 * 在后面某个时刻手动调用
		 */
		Beta.autoInit = true;

		/**
		 * true表示初始化时自动检查升级
		 * false表示不会自动检查升级，需要手动调用Beta.checkUpgrade()方法
		 */
		Beta.autoCheckUpgrade = true;

		/**
		 * 设置升级周期为60s（默认检查周期为0s），60s内SDK不重复向后天请求策略
		 */
		Beta.initDelay = 1 * 1000;

		/**
		 * 设置通知栏大图标，largeIconId为项目中的图片资源；
		 */
		Beta.largeIconId = R.drawable.ic_launcher;

		/**
		 * 设置状态栏小图标，smallIconId为项目中的图片资源id;
		 */
		Beta.smallIconId = R.drawable.ic_launcher;


		/**
		 * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
		 * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
		 */
		Beta.defaultBannerId = R.drawable.ic_launcher;

		/**
		 * 设置sd卡的Download为更新资源保存目录;
		 * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
		 */
		Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

		/**
		 * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
		 */
		Beta.showInterruptedStrategy = false;

		/**
		 * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
		 * 不设置会默认所有activity都可以显示弹窗;
		 */
		Beta.canShowUpgradeActs.add(ReactEnterActivity.class);


		/**
		 *  设置自定义升级对话框UI布局
		 *  注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
		 *  标题：beta_title，如：android:tag="beta_title"
		 *  升级信息：beta_upgrade_info  如： android:tag="beta_upgrade_info"
		 *  更新属性：beta_upgrade_feature 如： android:tag="beta_upgrade_feature"
		 *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
		 *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
		 *  详见layout/upgrade_dialog.xml
		 */
//        Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;

		/**
		 * 设置自定义tip弹窗UI布局
		 * 注意：因为要保持接口统一，需要用户在指定控件按照以下方式设置tag，否则会影响您的正常使用：
		 *  标题：beta_title，如：android:tag="beta_title"
		 *  提示信息：beta_tip_message 如： android:tag="beta_tip_message"
		 *  取消按钮：beta_cancel_button 如：android:tag="beta_cancel_button"
		 *  确定按钮：beta_confirm_button 如：android:tag="beta_confirm_button"
		 *  详见layout/tips_dialog.xml
		 */
//        Beta.tipsDialogLayoutId = R.layout.tips_dialog;

		/**
		 *  如果想监听升级对话框的生命周期事件，可以通过设置OnUILifecycleListener接口
		 *  回调参数解释：
		 *  context - 当前弹窗上下文对象
		 *  view - 升级对话框的根布局视图，可通过这个对象查找指定view控件
		 *  upgradeInfo - 升级信息
		 */
     /*  Beta.upgradeDialogLifecycleListener = new UILifecycleListener<UpgradeInfo>() {
            @Override
            public void onCreate(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onCreate");
                // 注：可通过这个回调方式获取布局的控件，如果设置了id，可通过findViewById方式获取，如果设置了tag，可以通过findViewWithTag，具体参考下面例子:

                // 通过id方式获取控件，并更改imageview图片
                ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
                imageView.setImageResource(R.mipmap.ic_launcher);

                // 通过tag方式获取控件，并更改布局内容
                TextView textView = (TextView) view.findViewWithTag("textview");
                textView.setText("my custom text");

                // 更多的操作：比如设置控件的点击事件
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), OtherActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onStart(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStart");
            }

            @Override
            public void onResume(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onResume");
            }

            @Override
            public void onPause(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onPause");
            }

            @Override
            public void onStop(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onStop");
            }

            @Override
            public void onDestroy(Context context, View view, UpgradeInfo upgradeInfo) {
                Log.d(TAG, "onDestory");
            }
        };*/

		/**
		 * 自定义Activity参考，通过回调接口来跳转到你自定义的Actiivty中。
		 */
       /* Beta.upgradeListener = new UpgradeListener() {

            @Override
            public void onUpgrade(int ret, UpgradeInfo strategy, boolean isManual, boolean isSilence) {
                if (strategy != null) {
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), UpgradeActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "没有更新", Toast.LENGTH_SHORT).show();
                }
            }
        };*/


		/**
		 * 已经接入Bugly用户改用上面的初始化方法,不影响原有的crash上报功能;
		 * init方法会自动检测更新，不需要再手动调用Beta.checkUpdate(),如需增加自动检查时机可以使用Beta.checkUpdate(false,false);
		 * 参数1： applicationContext
		 * 参数2：appId
		 * 参数3：是否开启debug
		 */
		Bugly.init(getApplicationContext(), "4ee5827505", true);

		/**
		 * 如果想自定义策略，按照如下方式设置
		 */

		/***** Bugly高级设置 *****/
		//        BuglyStrategy strategy = new BuglyStrategy();
		/**
		 * 设置app渠道号
		 */
		//        strategy.setAppChannel(APP_CHANNEL);

		//        Bugly.init(getApplicationContext(), APP_ID, true, strategy);


		//CrashReport.initCrashReport(getApplicationContext(), "4ee5827505", true);
	}

    @Override
    protected IPush createPush() {
        return new PushImpl(getApplicationContext());
    }

    public boolean isEmulator() {
		// model:Android SDK built for x86
		// 只要是在模拟器中，不管是什么版本的模拟器，在它的MODEL信息里就会带有关键字参数sdk
		return Build.MODEL.contains("sdk") || Build.MODEL.contains("SDK");
	}

	@Override
	public void onDismiss(DialogInterface arg0) {
		ECardJsonManager.getInstance().cancelRequest();
	}




	@Override
	public void startTyModule(Activity context, String moduleName, Intent intent,int requestCode) {
		PluginManager.getInstance().startApkModule(TianYu.COMP_NAME, moduleName,intent,requestCode,this);
	}
}
