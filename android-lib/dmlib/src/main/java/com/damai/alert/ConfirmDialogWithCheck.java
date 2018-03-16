package com.damai.alert;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;

import com.citywithincity.models.LocalData;
import com.citywithincity.utils.Alert.ICDCDialogListener;
import com.damai.lib.R;
public class ConfirmDialogWithCheck extends Dialog implements
android.view.View.OnClickListener {

	public static final String SP_KEY = "confirm_dialog_";

	private CheckBox checkBox;
	private ScrollView contentContainer;
	private ICDCDialogListener listener;
	private int dialogId;

	private TextView titleTextView;

	public ConfirmDialogWithCheck(Context context, int dialogId,
			int okTextResId, ICDCDialogListener listener) {
		super(context, R.style.dialog_custom);
		View view = LayoutInflater.from(context).inflate(
				R.layout.cus_confirm_dialog_with_check, null);
		super.setContentView(view);
		this.dialogId = dialogId;
		this.listener = listener;
		contentContainer = (ScrollView) findViewById(R.id._scroll_view);
		checkBox = (CheckBox) view.findViewById(R.id._check_box);
		View buttonContainerOk = findViewById(R.id.cus_dialog_buttons_ok_cancel);
		findViewById(R.id.cus_dialog_buttons_ok).setVisibility(View.GONE);
		;
		titleTextView = (TextView) findViewById(R.id.cus_dialog_title);

		Button okButton = (Button) buttonContainerOk
				.findViewById(R.id._id_ok);
		okButton.setText(okTextResId);
		Button cancelButton = (Button) buttonContainerOk
				.findViewById(R.id._id_cancel);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		
	}

	@Override
	public void setTitle(CharSequence title) {
		titleTextView.setText(title);
	}

	@Override
	public void setContentView(int layoutResID) {
		View view = LayoutInflater.from(getContext()).inflate(layoutResID,
				null);
		contentContainer.addView(view);
	}

	private boolean notify;

	protected void notifyOk(int buttonId) {
		if (buttonId == R.id._id_ok && checkBox.isChecked()) {
			LocalData.write().putBoolean(SP_KEY + dialogId, true).destroy();
		}
		listener.onCDCDialogEvent(buttonId, dialogId);
		notify = true;
		dismiss();
	}

	@Override
	public void dismiss() {
		if (!notify && listener != null) {
			listener.onCDCDialogEvent(R.id._id_cancel, dialogId);
		}
		super.dismiss();
		listener = null;
		contentContainer = null;
	}

	@Override
	public void onClick(View v) {
		notifyOk(v.getId());
	}
}
