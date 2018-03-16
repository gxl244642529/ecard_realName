package com.damai.alert;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.damai.lib.R;
class MyAlertDialog extends MyDialog{
	private TextView messageTextView;

	public MyAlertDialog(Context context) {
		super(context);
		setType(IAlert.OK);
		setContentView(R.layout.dialog_msg_text_view);
		messageTextView = (TextView) findViewById(R.id.cus_dialog_message);
	}

	public void setMessage(String message) {
		if (message == null) {
			messageTextView.setVisibility(View.GONE);
		} else {
			messageTextView.setVisibility(View.VISIBLE);
			messageTextView.setText(message);
		}
	}

	@Override
	public void dismiss() {
		messageTextView = null;
		super.dismiss();
	}
}