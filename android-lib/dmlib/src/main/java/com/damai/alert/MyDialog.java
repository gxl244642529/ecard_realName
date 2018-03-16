package com.damai.alert;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert.IDialog;
import com.citywithincity.utils.Alert.OnDialogInitContentView;
import com.damai.lib.R;
class MyDialog extends Dialog implements
android.view.View.OnClickListener, IDialog{
	private TextView titleTextView;
	private LinearLayout contentContainer;
	private View buttonContainerOkCancel;
	private View buttonContainerOk;
	private View contentView;

	private Button okButton;
	private Button cancelButton;

	private DialogListener listener;
	private OnDialogInitContentView initViewListener;
	private boolean notified;

	private View rootView;

	public MyDialog(Context context) {
		super(context, R.style.dialog_custom);
		super.setContentView(R.layout.cus_dialog_content);

		titleTextView = (TextView) findViewById(R.id.cus_dialog_title);
		contentContainer = (LinearLayout) findViewById(R.id.cus_dialog_content);

		buttonContainerOkCancel = findViewById(R.id.cus_dialog_buttons_ok_cancel);
		buttonContainerOk = findViewById(R.id.cus_dialog_buttons_ok);

		rootView = findViewById(R.id._container);
	}

	public View getRootView() {
		return rootView;
	}

	@Override
	public IDialog setDialogListener(DialogListener listener) {
		this.listener = listener;
		return this;
	}

	@Override
	public void dismiss() {
		if (!notified) {
			notified = true;
			if (listener != null) {
				listener.onDialogButton(R.id._id_cancel);
			}
		}
		rootView = null;
		okButton = null;
		cancelButton = null;
		contentView = null;
		contentContainer = null;
		titleTextView = null;
		listener = null;
		super.dismiss();

	}

	public View getContentView() {
		return contentView;
	}

	@Override
	public void setContentView(View view) {
		setContentView(view, null);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		contentView = view;
		contentContainer.removeAllViews();
		if (params == null) {
			contentContainer.addView(view);
		} else {
			contentContainer.addView(view, params);
		}

		if (initViewListener != null) {
			initViewListener.onInitContentView(contentView);
		}

	}

	@Override
	public IDialog setContent(int resID) {
		setContentView(resID);
		return this;
	}

	@Override
	public void setContentView(int resID) {
		View view = LayoutInflater.from(getContext()).inflate(resID, null);
		setContentView(view);
	}

	@Override
	public IDialog setTitle(String title) {
		titleTextView.setText(title);
		return this;
	}

	@Override
	public void onClick(View v) {
		notified = true;
		if (listener != null) {
			listener.onDialogButton(v.getId());
		}
		dismiss();
	}

	public void setType(int type) {
		if (type == IAlert.OK_CANCEL) {
			buttonContainerOkCancel.setVisibility(View.VISIBLE);
			buttonContainerOk.setVisibility(View.GONE);

			okButton = (Button) buttonContainerOkCancel
					.findViewById(R.id._id_ok);
			cancelButton = (Button) buttonContainerOkCancel
					.findViewById(R.id._id_cancel);
			okButton.setOnClickListener(this);
			cancelButton.setOnClickListener(this);
		} else if (type == IAlert.OK) {
			buttonContainerOkCancel.setVisibility(View.GONE);
			buttonContainerOk.setVisibility(View.VISIBLE);

			okButton = (Button) buttonContainerOk.findViewById(R.id._id_ok);
			buttonContainerOk.findViewById(R.id._id_cancel).setVisibility(
					View.GONE);
			okButton.setVisibility(View.VISIBLE);

			okButton.setOnClickListener(this);
		} else {
			buttonContainerOkCancel.setVisibility(View.GONE);
			buttonContainerOk.setVisibility(View.VISIBLE);

			cancelButton = (Button) buttonContainerOk
					.findViewById(R.id._id_cancel);
			buttonContainerOk.findViewById(R.id._id_ok).setVisibility(
					View.GONE);
			cancelButton.setVisibility(View.VISIBLE);
			cancelButton.setOnClickListener(this);
		}

	}

	@Override
	public IDialog setOkText(String ok) {
		okButton.setText(ok);
		return this;
	}

	@Override
	public IDialog setCancelText(String cancel) {
		cancelButton.setText(cancel);
		return this;
	}

	@Override
	public IDialog setOkText(int ok) {
		okButton.setText(getContext().getResources().getString(ok));
		return this;
	}

	@Override
	public IDialog setCancelText(int cancel) {
		cancelButton.setText(getContext().getResources().getString(cancel));
		return this;
	}

	@Override
	public IDialog setContent(View contentView) {
		setContentView(contentView);
		return this;
	}

	@Override
	public IDialog setCancelOnTouchOutside(boolean v) {
		setCanceledOnTouchOutside(v);
		return this;
	}

	@Override
	public IDialog setOnInitContentViewListener(
			OnDialogInitContentView listener) {

		if (contentView != null) {
			listener.onInitContentView(contentView);
		} else {
			initViewListener = listener;
		}

		return this;
	}

}
