package com.citywithincity.ecard.insurance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.insurance.models.InsuranceModel;
import com.citywithincity.ecard.insurance.models.MyInsuranceModel;
import com.citywithincity.ecard.insurance.models.PICCPayAction;
import com.citywithincity.ecard.insurance.models.vos.InsuranceDetailVo;
import com.citywithincity.ecard.insurance.models.vos.InsurancePaySuccessNotifyVo;
import com.citywithincity.ecard.insurance.models.vos.InsurancePurchaseSuccessVo;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.MyECardModel;
import com.citywithincity.ecard.models.vos.RealInfoVo;
import com.citywithincity.ecard.pay.PayIds;
import com.citywithincity.ecard.ui.activity.exam.ExamUploadImageView;
import com.citywithincity.ecard.utils.DateTimeUtil_M;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.paylib.ECardPayModel;
import com.citywithincity.paylib.IPay;
import com.citywithincity.paylib.IPay.CashierInfo;
import com.citywithincity.paylib.PayType;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ExternalUtil;
import com.citywithincity.utils.ThreadUtil;
import com.citywithincity.utils.ValidateUtil;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;

import java.io.File;
import java.io.IOException;

@Observer
@EventHandler
public class InsurancePersonalInfoActivity extends BaseActivity implements
		OnClickListener {

	public static boolean isFormInsurancePayErrorActivity = false;

	private EditText nameView;
	private EditText IDCARDNumView;
	private EditText phoneView;
	private Button purchase;
	private ExamUploadImageView image;
	private File imageFile;
	private InsuranceDetailVo data;
	private String insuranceId;

	@SuppressWarnings("unused")
	private String title;

	private boolean isValid;// 身份是否验证过
	private InsurancePurchaseSuccessVo errorResubmitData;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_insurance_personal_info);
		setTitle("个人信息填写");

		init();
		if (isFormInsurancePayErrorActivity) {
			errorResubmitData = (InsurancePurchaseSuccessVo) getIntent()
					.getExtras().get(LibConfig.DATA_NAME);
			insuranceId = errorResubmitData.insurance_id;
			title = errorResubmitData.title;
		} else {
			data = (InsuranceDetailVo) getIntent().getExtras().get(
					LibConfig.DATA_NAME);
			insuranceId = data.insurance_id;
			title = data.title;
		}

		ModelHelper.getModel(MyECardModel.class).getRealInfo();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void init() {
		nameView = (EditText) findViewById(R.id.id_name);
		IDCARDNumView = (EditText) findViewById(R.id.id_idcard_no);
		phoneView = (EditText) findViewById(R.id.id_phone);
		purchase = (Button) findViewById(R.id.id_purchase);
		purchase.setOnClickListener(this);

		image = (ExamUploadImageView) findViewById(R.id.img);
		image.setMethod(ExamUploadImageView.CAPTURE_IMAGE);
		image.setImageResource(R.drawable.bg_insurance_example);
		image.setImageScaleType(ScaleType.CENTER_INSIDE);
		// image.addWaterMark("picc",R.color.text_default_color);
		image.setPicSaveQuality(100);

		IDCARDNumView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String tempIdCard;
				if (isValid) {
					tempIdCard = idCard;
				} else {
					tempIdCard = s.toString();
				}

				if (s.length() == 18) {
					if (!ValidateUtil.is18Idcard(tempIdCard)) {
						Alert.alert(InsurancePersonalInfoActivity.this, "提示",
								"请输入正确的身份证号", new DialogListener() {

									@Override
									public void onDialogButton(int id) {
										
									}
								});
					} else {
						
					}

					if (DateTimeUtil_M.getAge(tempIdCard) < 18) {
						Alert.alert(InsurancePersonalInfoActivity.this, "提示",
								"投保人应该年满十八周岁", new DialogListener() {

									@Override
									public void onDialogButton(int id) {
										
									}
								});
					} else {
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 
	 * @param result
	 */
	@NotificationMethod(MyECardModel.REAL_INFO)
	public void onGetRealInfoSuccess(RealInfoVo result) {
		if (result != null) {
			this.isValid = result.isValid;
			if (!TextUtils.isEmpty(result.name)) {
				nameView.setText(result.name);
			}
			if (!TextUtils.isEmpty(result.idCard)) {
				if (result.isValid) {
					idCard = result.idCard;
					String tempId = result.idCard.substring(0, 4)
							+ "**********" + result.idCard.substring(14);
					IDCARDNumView.setText(tempId);
				} else {
					IDCARDNumView.setText(result.idCard);
				}

				// 身份证是否可以购买
				// checkIdCard(result.idCard);
			}
			if (!TextUtils.isEmpty(result.phone)) {
				phoneView.setText(result.phone);
			} else {
				String userAccount = ECardJsonManager.getInstance()
						.getUserInfo().getAccount();
				if (ValidateUtil.isTel(userAccount)) {
					phoneView.setText(userAccount);
				}
			}
			// 如果已经被验证过则不应该可以改
			if (result.isValid) {
				nameView.setKeyListener(null);
				IDCARDNumView.setKeyListener(null);
			}
		} else {
			String userAccount = ECardJsonManager.getInstance().getUserInfo().getAccount();
			if (ValidateUtil.isTel(userAccount)) {
				phoneView.setText(userAccount);
			}
		}
	}

	
	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.id_purchase) {

			Alert.alert(this, "提示",
					"请务必确保您的个人信息及拍摄照片清晰准确，如因拍摄照片无法辨识所引起的理赔问题，将由您个人承担。",
					new DialogListener() {

						@Override
						public void onDialogButton(int id) {
							if (id == R.id._id_ok) {
								if (validate()) {
									checkData();
								}
							}
						}
					});
			
		}
	}

	/**
	 * 提交资料
	 */
	private void submit() {
		Alert.wait(this, "正在处理图片...");

		ThreadUtil.newTask(this, new Runnable() {

			@Override
			public void run() {
				try {
					imageFile = null;
					imageFile = image.compressToFile(
                            InsurancePersonalInfoActivity.this, 100);
				} catch (IOException e) {

				}
			}
		}, new Runnable() {

			@Override
			public void run() {
				if (imageFile != null) {
					MyInsuranceModel.getInstance().submit(purchase,insuranceId, idCard,
							imageFile, eCard, name, phone, data.ticket);
				} else {
					Alert.showShortToast("提交失败，请尝试重新换一张图片");
				}
				// if (imageFile != null) {
				// imageIsIdCard();
				// } else {
				// Alert.showShortToast("提交失败，请尝试重新换一张图片");
				// }
			}
		});
	}

	
	
	
	//@NotificationMethod(MyInsuranceModel.SUBMIT)
	@JobSuccess(MyInsuranceModel.SUBMIT)
	public void onPurchaseSuccess(InsurancePaySuccessNotifyVo result) {
		if (TextUtils.isEmpty(data.ticket)) {
			// result.insurance_id = insuranceId;

			if (isFormInsurancePayErrorActivity) {

				Alert.alert(this, "提示", "信息提交成功", listener);

			} else {

				IPay model = ModelHelper.getModel(ECardPayModel.class);
				model.setId(PayIds.PAY_ID_PICC);
				model.setCashierInfo(new CashierInfo(result.id, result.fee));
				model.setPayAction(new PICCPayAction());
				model.setPayTypes(new PayType[] { PayType.PAY_WEIXIN });
				ActivityUtil.startActivity(this,
						InsuranceCashierActivity.class, result);

			}
		} else {
			InsuranceProductDetailActivity.isFromInsuranceActionActivity = false;
			Alert.alert(this, "温馨提示", "购买成功", new DialogListener() {

				@Override
				public void onDialogButton(int id) {
					ActivityUtil.startActivity(
							InsurancePersonalInfoActivity.this,
							InsuranceMyPolicyActivity.class);
					finish();
				}
			}).setOkText("我的保单").setCancelOnTouchOutside(false);
		}
	}

	private DialogListener listener = new DialogListener() {

		@Override
		public void onDialogButton(int id) {
			finish();
		}
	};
	
	
	@Res
	private EditText cardId;

	String name;
	String idCard;
	String phone;
	String eCard;

	private boolean validate() {
		name = nameView.getText().toString();
		phone = phoneView.getText().toString();
		eCard = cardId.getText().toString();
		if (!isValid) {
			idCard = IDCARDNumView.getText().toString();
		}

		if (TextUtils.isEmpty(name)) {
			Alert.showShortToast("请输入姓名");
			return false;
		} else if (idCard.length() != 18) {
			if (idCard.length() == 0) {
				Alert.showShortToast("请输入身份证号");
				return false;
			}
			Alert.showShortToast("你输入身份证号格式有误，请重新输入");
			return false;
		} else if (phone.length() != 7 && phone.length() != 11) {
			if (phone.length() == 0) {
				Alert.showShortToast("请输入电话号码");
				return false;
			}
			Alert.showShortToast("你输入电话号码格式有误，请重新输入");
			return false;
		} else if (TextUtils.isEmpty(eCard)) {
			Alert.showShortToast("请输入e通卡卡号");
			return false;
		} else if (!image.hasImage()) {
			Alert.showShortToast("请将身份证（正面）和e通卡（背面）放在一起合照上传");
			return false;
		} else {
			return true;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if(requestCode == ExternalUtil.REQUEST_CAPTURE_IMAGE ||
					requestCode == ExternalUtil.REQUEST_SELECT_IMAGE ){
				if(image!=null){
					image.setImageScaleType(ScaleType.CENTER_INSIDE);
					image.parseResult(requestCode, data);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 检测身份证是否可以购买
	 * 
	 * @param cardId
	 */
	private void checkData() {
		ModelHelper.getModel(InsuranceModel.class).checkData(eCard,
				data.ticket, idCard);
	}

	@NotificationMethod(InsuranceModel.CHECK_DATA)
	public void onCheckIdCardSuccess(boolean result) {
		Alert.cancelWait();
		submit();
	}

	@NotificationMethod(InsuranceModel.CHECK_DATA_ERROR)
	public void onCheckIdCardSuccess(String error, boolean isNetworkError) {
		Alert.cancelWait();
		if (isNetworkError) {
			Alert.alert(this, "温馨提示", "网络错误，请重试", new DialogListener() {

				@Override
				public void onDialogButton(int id) {

				}
			});
		} else {
			Alert.alert(this, "温馨提示", error, new DialogListener() {

				@Override
				public void onDialogButton(int id) {

				}
			});
		}
	}


	@NotificationMethod(IPay.PAY_SUCCESS)
	public void onPaySuccess(Object result) {
		finish();
	}

	@NotificationMethod(IPay.PAY_ERROR)
	public void onPayError(Object result) {
		finish();
	}

	@NotificationMethod(InsuranceModel.CLIENT_NOTIFY_ERROR)
	public void onPiccNotifyFailed(String error, boolean isNetworkError) {
		Alert.cancelWait();
		finish();
	}

	@Override
	public void onBackPressed() {
		InsuranceProductDetailActivity.isFromInsuranceActionActivity = false;
		finish();
		super.onBackPressed();
	}

	@Override
	protected void backToPrevious() {
		InsuranceProductDetailActivity.isFromInsuranceActionActivity = false;
		finish();
		super.backToPrevious();
	}

}
