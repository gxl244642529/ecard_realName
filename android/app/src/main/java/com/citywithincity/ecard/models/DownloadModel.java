package com.citywithincity.ecard.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.Alert.IDialog;
import com.citywithincity.utils.PackageUtil;
import com.citywithincity.utils.SDCardUtil;
import com.damai.auto.LifeManager;
import com.damai.core.DMLib;
import com.damai.core.HttpJob;
import com.damai.core.JobListener;

import java.io.File;
import java.io.IOException;

public class DownloadModel {
	Context context;
	String name;
	
	private HttpJob download;
	
	public DownloadModel(Context context,String name){
		this.context = context;
		this.name = name;
	}
	private String getApkName(){
		return PackageUtil.getVersionCode(LifeManager.getActivity()) + "_"+name+".apk";
	}
	public void downloadApi(String url,final boolean forceUpdate){
		try{
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
							if(forceUpdate){
								LifeManager.closeAll();
							}
						}
					}).setCancelOnTouchOutside(false);

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
			Alert.alert(context,"文件创建失败");
		}
		
	}
}
