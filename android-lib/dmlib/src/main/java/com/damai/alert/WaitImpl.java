package com.damai.alert;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.widget.TextView;

import com.damai.auto.LifeManager;
import com.damai.core.DMLib;
import com.damai.lib.R;
public class WaitImpl implements IWait {
	private static class WaitProgress extends Dialog {
		private boolean cancelable;
		private TextView textView;

		public WaitProgress(Context context, String message) {
			super(context, R.style.dialog_custom);
			setContentView(R.layout._dialog_wait);
			textView = (TextView) findViewById(R.id._text_view);
			setMessage(message);
		}

		public void setMessage(String message) {
			textView.setText(message);
		}

		public void setDismissListener(OnDismissListener listener) {
			super.setOnDismissListener(listener);
		}

		@Override
		public void setOnDismissListener(OnDismissListener listener) {

		}

		@Override
		public void setCancelable(boolean flag) {
			super.setCancelable(flag);
			this.cancelable = flag;
		}

		@Override
		public void onBackPressed() {
			if (cancelable) {
				cancel();
			} else {
				dismiss();
				DMLib.getJobManager().stopRequest(LifeManager.getActivity());
			//	LifeManager.getActivity().finish();
			}
		}
		

	}

	private WaitProgress _wait;
	private final OnDismissListener g_dismissListener = new OnDismissListener() {
		@Override
		public void onDismiss(DialogInterface arg0) {
			_wait = null;
		}
	};




	@Override
	public void cancelWait() {
		if (_wait != null) {
			try {_wait.dismiss();} catch (Exception e) {}
			_wait = null;
		}
	}


	public void wait(Context context,String title, boolean cancelable,
			OnCancelListener onCancelListener) {
		if(_wait!=null){
			_wait.setMessage(title);
			return;
		}
		
		if (_wait == null) {
			Activity activity = (Activity)context;
			if(activity.isFinishing()){
				context = LifeManager.findPrevious(context);
			}
			_wait = new WaitProgress(context, title);
		}
		
		_wait.setCancelable(cancelable);
		_wait.setCanceledOnTouchOutside(cancelable);
		_wait.setOnCancelListener(onCancelListener);
		_wait.setDismissListener(g_dismissListener);
		_wait.show();
	}
}
