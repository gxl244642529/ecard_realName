package com.citywithincity.ecard.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.Alert.IDialog;
import com.citywithincity.utils.MD5Util;
import com.citywithincity.utils.PackageUtil;
import com.citywithincity.utils.SDCardUtil;
import com.damai.auto.LifeManager;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.core.HttpJob;
import com.damai.core.JobListener;

import java.io.File;
import java.io.IOException;

public class DodobaoUtil {

	public static boolean isInstalled(Activity context) {
		// 判断老版本的是否存在
		boolean isDo = checkApkExist(context, "com.dodopal.dosdk.ui");
		boolean isDoS = checkApkExist(context, "com.dodopal.dosdk.xm");
		if (isDo) {
			// 通过程序的报名创建URI
			Uri packageURI = Uri.parse("package:" + "com.dodopal.dosdk.ui");
			// 创建Intent意图
			Intent intent = new Intent(Intent.ACTION_DELETE, packageURI);
			// 执行卸载程序
			context.startActivity(intent);
			return false;
		}
		return isDoS;

	}

	public static void activate(final Activity context) throws IOException {
		if (isInstalled(context)) {
			DMAccount account = DMAccount.get();
			String info = getOrderInfo("", String.valueOf(account.getAccount()));
//			System.out.println("将要传递的参数" + info);
			Intent intent = new Intent();
			ComponentName comp = new ComponentName("com.dodopal.dosdk.xm",
					"com.dodopal.dosdk.xm.MainActivity");
			intent.setComponent(comp);
			intent.setAction("android.intent.action.VIEW");
			intent.putExtra("user_info", info);
			context.startActivityForResult(intent, 1);
		} else {
			if (!install(context)) {
				// 下载
				Alert.confirm(context, "其他充值方式", "该充值方式需具有NFC功能的手机使用支付宝支付并下载插件，确认下载吗？",
						new DialogListener() {

							@Override
							public void onDialogButton(int id) {
								if (id == R.id._id_ok) {
									// 下载
									downloadApk(context);
								}
							}
						});
			}
		}

	}

	private static HttpJob download;
	

	private static void downloadApk(final Context context) {
		// 最新版本下载
		// 首先检查本地有没有
		try{
			String url = "http://dodopal.com/driver/apk/DoCardSdk.apk";
			File destFile = new File(SDCardUtil.getSDCardDir(context, "apk"),getApkName());
			View view = LayoutInflater.from(context).inflate(
					R.layout.progress_dialog_layout, null);
			final ProgressBar progressBar = (ProgressBar) view
					.findViewById(R.id._progress_bar);
			final TextView totalTxt = (TextView) view
					.findViewById(R.id.max_progress);
			final TextView progressTxt = (TextView) view
					.findViewById(R.id.progress_text);
			totalTxt.setText("");
			progressTxt.setText("");
			final IDialog pop = Alert.popup(context, view, "正在下载...", Alert.CANCEL,
					new DialogListener() {

						@Override
						public void onDialogButton(int id) {
							download.destroy();
						}
					});

			download = DMLib.getJobManager().download(url, destFile, new JobListener<HttpJob>() {

				@Override
				public boolean onJobError(HttpJob job) {

					Alert.showShortToast(job.getError().getReason());
					return false;
				}

				@Override
				public void onJobProgress(HttpJob job) {
					int total = job.getTotalBytes();
					if (total > 0) {
						progressBar.setMax(total);
						progressBar.setProgress(job.getCurrentBytes());
						totalTxt.setText(String.valueOf(total));
						progressTxt.setText(String.valueOf(job.getCurrentBytes()));
					}
				}

				@Override
				public void onJobSuccess(HttpJob job) {
					File result = job.getData();
					pop.dismiss();
					PackageUtil.installApk(context, result);
				}
			});

		}catch (IOException e){
			Alert.alert(context,"文件打开失败");
		}

	}

	private static File getApkFile(Context context) throws IOException {
		File file = SDCardUtil.getSDCardDir(context, "apk");
		return new File(file, getApkName());
	}
	
	private static String getApkName(){
		return PackageUtil.getVersionCode(LifeManager.getActivity()) + "_dudubao.apk";
	}

	private static boolean install(Activity context) throws IOException {
		File apk = getApkFile(context);
		if (apk.exists()) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			String type = "application/vnd.android.package-archive";

			intent.setDataAndType(Uri.fromFile(apk), type);
			context.startActivity(intent);
			return true;
		}
		return false;
	}

	public static final String apkno = "XMETK";// 定值szykt nbykt nomalyk
	public static final String apkver = "1.0";// 定值
	public static final String partner = "411101101000036";// 定值
	public static final String mobile = "18147979756";// 用户名如果是手机号则传手机号
	public static final String uuid = "591007778710009878232";
	public static final String signtype = "MD5";// 定值
	public static String signer = "";
	public static final String md5_key = "xmdoapk";

	public static String getOrderInfo(String number, String userid) {
		signer = signStr(apkno + partner + userid + md5_key);
		String orderInfo = "apkno=" + apkno;
		orderInfo += "&";
		orderInfo += "apkver=" + apkver;
		orderInfo += "&";
		orderInfo += "partner=" + partner;
		orderInfo += "&";
		orderInfo += "mobile=" + number;
		orderInfo += "&";
		orderInfo += "uuid=" + userid;
		orderInfo += "&";
		orderInfo += "signtype=" + signtype;
		orderInfo += "&";
		orderInfo += "signer=" + signer;
		return orderInfo;
	}

	public static boolean checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return false;
		try {
			context.getPackageManager().getApplicationInfo(packageName,
					PackageManager.GET_UNINSTALLED_PACKAGES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 验签函数
	 * 
	 * @param mingwen
	 * @param miwen
	 * @return
	 */
	public static boolean matchStr(String mingwen, String miwen) {

		boolean flag = false;

		String signStr = MD5Util.md5Appkey(mingwen);
		if (signStr.equals(miwen)) {
			flag = true;
		} else {
			System.out.println("" + "商户验签失败!");
		}

		return flag;
	}

	/**
	 * 签名返回密文
	 * 
	 * @param mingwen
	 * @return
	 */
	public static String signStr(String mingwen) {
		return MD5Util.md5Appkey(mingwen);
	}
}
