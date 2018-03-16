package com.damai.pay.platform;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.util.Base64;

import com.alipay.sdk.app.PayTask;
import com.citywithincity.paylib.AliUtil;
import com.citywithincity.paylib.PayType;
import com.citywithincity.utils.ThreadUtil;
import com.damai.core.ApiJob;
import com.damai.lib.R;
import com.damai.pay.AbsDMPay;

public class AliPay extends AbsDMPay implements Runnable {
	private int mState;
	private String mResult;
	private String serverInfo;
	private Activity mContext;

	public AliPay(Activity context) {
		mContext = context;
	}

	@Override
	public void onJobSuccess(ApiJob job) {
		serverInfo = job.getData();

		new Thread(this).start();
	}

	@Override
	public boolean onJobError(ApiJob job) {
		return getModel().notifyError(job.getError());
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		return getModel().notifyMessage(job.getMessage());
	}

	@Override
	public void run() {
		PayTask alipay = new PayTask(mContext);
		String result = alipay.pay(serverInfo,true);
		mState = AliUtil.checkPayState(result);
		mResult = AliUtil.getResult(result);
		if (mState == 9000) {
			// orderInfo.fee = AliUtil.getTotalFee(result);
			// mOrderInfo = orderInfo;
		}
		ThreadUtil.post(runnable);
	}

	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			switch (mState) {
			case 9000: {
				// 通知服务器成功
				try {
					String str64 = Base64.encodeToString(mResult.getBytes("UTF-8"), Base64.NO_WRAP);

					getModel().notifyPaySuccess(getPayType(),URLEncoder.encode(str64, "UTF-8"),true);
					//getModel().getOrderInfo(URLEncoder.encode(str64, "UTF-8"),true);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}

				break;
			default:
				break;
			}
		}
	};

	@Override
	public String getTitle() {
		return "支付宝";
	}

	@Override
	public int getPayType() {
		return PayType.PAY_ALIPAY.value();
	}

	@Override
	public int getIcon() {
		return R.drawable._pay_ali;
	}

	@Override
	public boolean checkInstalled() {
		return true;
	}

}
