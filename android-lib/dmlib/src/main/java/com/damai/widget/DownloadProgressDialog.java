package com.damai.widget;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.Alert.IDialog;
import com.damai.core.HttpJob;
import com.damai.lib.R;

public class DownloadProgressDialog {

	ProgressBar progressBar;
	TextView totalTxt;
	TextView progressTxt;
	private WeakReference<HttpJob> download;
	IDialog pop;
	public DownloadProgressDialog(Context context,HttpJob job){
		download = new WeakReference<HttpJob>(job);
		View view = LayoutInflater.from(context).inflate(
				R.layout.progress_dialog_layout, null);
		progressBar = (ProgressBar) view
				.findViewById(R.id._progress_bar);
		totalTxt = (TextView) view
				.findViewById(R.id.max_progress);
		progressTxt = (TextView) view
				.findViewById(R.id.progress);
		totalTxt.setText("");
		progressTxt.setText("");
		pop = Alert.popup(context, view, "正在下载...", Alert.CANCEL,
				new DialogListener() {

					@Override
					public void onDialogButton(int id) {
						if(download!=null){
							HttpJob job = download.get();
							if(job!=null){
								job.cancel();
								download = null;
							}
						}
						
					}
				});
	}
	
	public void dismiss(){
		pop.dismiss();
	}
	
	public void setProgress(int total,int current){
		if (total > 0) {
			progressBar.setMax(total);
			progressBar.setProgress(current);
			totalTxt.setText(String.valueOf(total));
			progressTxt.setText(String.valueOf(current));
		}
	}

}
