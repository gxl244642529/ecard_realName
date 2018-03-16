package com.citywithincity.ecard.selling.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alipay.sdk.pay.AlixId;
import com.alipay.sdk.pay.BaseHelper;
import com.alipay.sdk.pay.CheckPayState;
import com.alipay.sdk.pay.ResultChecker;
import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.models.AliPayModel;
import com.citywithincity.ecard.selling.models.OrderBModel;
import com.citywithincity.ecard.selling.models.OrderModel;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;

@Observer
@EventHandler
public class SCashierActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private String orderId;

	static String TAG = "AliPay";
	private RadioGroup payGroup;
	private int payMethod = -1;
	private TextView priceView;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_s_cashier);
		setTitle("收银台");
		
//		Bundle bundle = getIntent().getExtras();
//		
//		float price = bundle.getFloat("price");
//		id = bundle.getString("id");
//		
//		priceView = (TextView) findViewById(R.id.total_pay_price);
//		priceView.setText(id);
//		payGroup = (RadioGroup) findViewById(R.id.radio_group1);
//		payGroup.setOnCheckedChangeListener(this);
		
		priceView = (TextView) findViewById(R.id.total_pay_price);
		priceView.setText(ModelHelper.getModel(OrderBModel.class)
				.getRealPayPay());
		payGroup = (RadioGroup) findViewById(R.id.radio_group1);
		payGroup.setOnCheckedChangeListener(this);
	}

	@EventHandlerId(id = R.id.s_pay_btn)
	public void onBtnPay() {
		if (payMethod == -1) {
			Alert.showShortToast("请选择支付方式");
			return;
		}
		// 支付
		ModelHelper.getModel(OrderBModel.class).pay(payMethod);
	}

	@NotificationMethod(OrderModel.PAY)
	public void onRequestSuccess(String result) {
		AliPayModel.getInstance().aliPay(this, result, mHandler);

	}

	//
	// the handler use to receive the pay result.
	// 这里接收支付结果，支付宝手机端同步通知
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			try {
				String strRet = (String) msg.obj;

				switch (msg.what) {
				case AlixId.RQF_PAY: {
					if (LibConfig.DEBUG) {
					BaseHelper.log(TAG, strRet);
					}
					// 从通知中获取参数
					try {
						// 获取交易状态，具体状态代码请参看文档
						String memo = "memo=";
						int imemoStart = strRet.indexOf("memo={");
						imemoStart += memo.length() + 1;
						int imemoEnd = strRet.indexOf("};result=");
						memo = strRet.substring(imemoStart, imemoEnd);
						
						// 对通知进行验签
						ResultChecker resultChecker = new ResultChecker(strRet);
						
						String payResult = resultChecker.getResult();
						
						Bundle bundle = new Bundle();
						bundle.putString("orderId", orderId);
						switch (CheckPayState.checkPayState(strRet)) {
						case 9000:// 支付成功
							ModelHelper.getModel(OrderModel.class).payNotify(Base64.encodeToString(payResult.getBytes(),
									 0));
							// url
							// Base64.e
//							 SellingModel.getInstance().orderPay(Base64.encodeToString(payResult.getBytes(),
//							 0),paySuccessListener);
							// bundle.putInt("result", 9000);
							// ActivityUtil.startActivity(SCashierActivity.this,
							// SPayResultActivity.class, bundle);
							// finish();
							break;
						case 8000:// 正在处理中
							bundle.putInt("result", 8000);
							// bundle.putDouble("price", value);
							ActivityUtil.startActivity(SCashierActivity.this,
									SPayResultActivity.class, bundle);
							// Alert.showShortToast("正在处理中");
							BaseHelper.showDialog(SCashierActivity.this, "提示",
									memo, R.drawable.infoicon);
							finish();
							break;
						case 6001:// 用户中途取消
							bundle.putInt("result", 6001);
							// bundle.putDouble("price", value);
							ActivityUtil.startActivity(SCashierActivity.this,
									SPayResultActivity.class, bundle);
							// Alert.showShortToast("用户中途取消");
							BaseHelper.showDialog(SCashierActivity.this, "提示",
									memo, R.drawable.infoicon);
							finish();
							break;
						case 6002:// 网络连接出错
							bundle.putInt("result", 6002);
							// bundle.putDouble("price", value);
							ActivityUtil.startActivity(SCashierActivity.this,
									SPayResultActivity.class, bundle);
							// Alert.showShortToast("网络连接出错");
							BaseHelper.showDialog(SCashierActivity.this, "提示",
									memo, R.drawable.infoicon);
							finish();
							break;
						case 4000:// 订单支付失败
							bundle.putInt("result", 4000);
							// bundle.putDouble("price", value);
							ActivityUtil.startActivity(SCashierActivity.this,
									SPayResultActivity.class, bundle);
							// Alert.showShortToast("订单支付失败");
							BaseHelper.showDialog(SCashierActivity.this, "提示",
									memo, R.drawable.infoicon);
							finish();
							break;

						default:
							break;
						}

					} catch (Exception e) {
						e.printStackTrace();

						BaseHelper.showDialog(SCashierActivity.this, "提示",
								strRet, R.drawable.infoicon);
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

//	private IRequestResult<Boolean> paySuccessListener = new IRequestResult<Boolean>() {
//
//		@Override
//		public void onRequestSuccess(Boolean result) {
//			if (result) {
//				// Alert.showShortToast("支付成功,我们会尽快发货");
//				Bundle bundle = new Bundle();
//				bundle.putInt("result", 9000);
//				bundle.putString("orderId", orderId);
//				// bundle.putDouble("price", value);
//				ActivityUtil.startActivity(SCashierActivity.this,
//						SPayResultActivity.class, bundle);
//				finish();
//			}
//
//		}
//
//		@Override
//		public void onRequestError(String errMsg, boolean isNetworkError) {
//			Alert.showShortToast(errMsg);
//
//		}
//	};
	
	@NotificationMethod(OrderModel.PAY_NOTITY)
	public void onPayNotitySuccess(Boolean result) {
		Bundle bundle = new Bundle();
		bundle.putInt("result", 9000);
		bundle.putString("orderId", orderId);
		// bundle.putDouble("price", value);
		ActivityUtil.startActivity(SCashierActivity.this,
				SPayResultActivity.class, bundle);
		finish();
	}
	
	@NotificationMethod(OrderModel.PAY_NOTITY_ERROR)
	public void onPayNotifyError(String errMsg) {
		Alert.showShortToast(errMsg);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.s_ali_pay:
			payMethod = 1;
			break;
		case R.id.s_weixin_pay:

			break;
		case R.id.s_ecard_pay:

			break;
		case R.id.s_union_pay:

			break;

		default:
			break;
		}

	}

}
