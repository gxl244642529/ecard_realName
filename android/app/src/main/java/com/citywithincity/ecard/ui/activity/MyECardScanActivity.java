package com.citywithincity.ecard.ui.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.myecard.models.MyECardModel;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.zxinglib.activity.CaptureActivity;
import com.damai.core.ApiJob;
import com.damai.core.DMMessage;
import com.damai.helper.a.Model;
import com.damai.http.api.a.JobMessage;
import com.damai.http.api.a.JobSuccess;
import com.google.zxing.Result;

public class MyECardScanActivity extends CaptureActivity implements
		OnClickListener {

	private String barcode;

	@Model
	private MyECardModel eCardModel;

	@Override
	protected void sendDecodeData(Result obj, Bitmap barcode) {
		Alert.wait(this, "正在查询条码信息...");
		this.barcode = obj.getText();

		if (this.barcode.length() == 13) {
			this.barcode = this.barcode.substring(0, 12);
		}
		eCardModel.bind(this.barcode);
	}

	@JobSuccess(MyECardModel.bindBarcode)
	public void onScanSuccess(Object value) {
		finish();
	}

	@JobMessage(MyECardModel.bindBarcode)
	public boolean onScanError(ApiJob job) {
		DMMessage message = job.getMessage();
		Alert.confirm(this, "出现错误:" + message.getMessage() + "\n是否继续扫描?",
				new DialogListener() {

					@Override
					public void onDialogButton(int id) {
						if (id == R.id._id_ok) {
							restart();
						} else {
							finish();
						}
					}
				});
		return true;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

	}

}
