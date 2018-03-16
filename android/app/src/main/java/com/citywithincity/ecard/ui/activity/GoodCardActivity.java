package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.enums.Actions;
import com.citywithincity.ecard.models.PickCardModel;
import com.citywithincity.ecard.models.vos.MyLostCardDetailInfo;
import com.citywithincity.ecard.utils.MyECardUtil;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.damai.helper.ActivityResult;

@Observer
public class GoodCardActivity extends BaseActivity implements OnClickListener,
		IRequestResult<MyLostCardDetailInfo>, ActivityResult {

	MyLostCardDetailInfo _myLostCardDetailInfo;
	TextView statusText;
	Button okButton;
	Button hasFindButton;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_good_card);
		setTitle("拾卡不昧");
		findViewById(R.id.good_card_info).setOnClickListener(this);
		findViewById(R.id.good_card_query).setOnClickListener(this);
		findViewById(R.id.good_card_return).setOnClickListener(this);
		hasFindButton = (Button) findViewById(R.id._id_cancel);
		hasFindButton.setOnClickListener(this);
		findViewById(R.id._id_ok).setOnClickListener(this);
		statusText = (TextView) findViewById(R.id._text_view);
		statusText.setVisibility(View.GONE);
		okButton = (Button) findViewById(R.id._id_ok);

		okButton.setVisibility(View.GONE);
		hasFindButton.setVisibility(View.GONE);
		if (JsonTaskManager.getInstance().isLogin()) {
			loadInfo();
		} else {
			final DialogListener login = new DialogListener() {

				@Override
				public void onDialogButton(int id) {
					if (id == R.id._id_cancel) {
						finish();
					} else {
						JsonTaskManager.getInstance().requestLogin(
								GoodCardActivity.this);
					}
				}
			};
			Alert.confirm(this, "登录后才能登记我的卡片信息,去登录吗?", login);
		}
	}

	/**
	 * 
	 */
	private void loadInfo() {
		ModelHelper.getModel(PickCardModel.class).getMyLostCardInfo()
				.setListener(this);
	}

	private View clickedView;

	@Override
	public void onClick(View v) {

		if (_myLostCardDetailInfo == null) {
			clickedView = v;
			loadInfo();
			return;
		}

		switch (v.getId()) {
		case R.id.good_card_info:
			open_edit();
			break;
		case R.id.good_card_query:
			ActivityUtil.startActivity(this, GoodCardQueryActivity.class);
			break;
		case R.id.good_card_return:
			ActivityUtil.startActivity(this, GoodCardReturnActivity.class);
			break;
		case R.id._id_cancel: {
			ModelHelper.getModel(PickCardModel.class).lostCardRevocation();

			break;
		}
		case R.id._id_ok: {
			if (JsonTaskManager.getInstance().isLogin()) {
				// 如果有信息
				if (_myLostCardDetailInfo.info == null) {
					onNoInfo();
				} else {
					if (_myLostCardDetailInfo.info.vry_time > 0) {
						if (_myLostCardDetailInfo.info.vry_result
								.equals("error")) {
							if (_myLostCardDetailInfo.info.status != 3) {
								final DialogListener updateListener = new DialogListener() {

									@Override
									public void onDialogButton(int id) {
										// TODO Auto-generated method stub
										if (id == R.id._id_ok) {
											open_edit();
										}
									}
								};
								Alert.confirm(this,
										"您登记的信息没有审核通过，请您先修改信息以后在重新发布,要去修改吗",
										updateListener);
								return;
							}

						}
					}

					// MyECardActivity.selectECart(this);

					MyECardUtil.selectECard(GoodCardActivity.this);
				}

			}
			break;
		}
		default:
			break;
		}
	}

	protected void requestBindEcard() {
		ActivityUtil.startActivityForResult(GoodCardActivity.this,
				BindECardActivity.class, Actions.REQUEST_CODE_BIND_ECARD);
	}

	protected void setLostCard(final String cardNumber) {
		// 直接发布
		ModelHelper.getModel(PickCardModel.class).lostCardPublish(cardNumber)
				.setListener(new IRequestResult<Object>() {

					@Override
					public void onRequestSuccess(Object result) {
						Alert.alert(GoodCardActivity.this, "提示",
								(String) result);
						finding(cardNumber);
					}

					@Override
					public void onRequestError(String errMsg,
							boolean isNetworkError) {
						if (isNetworkError) {
							Alert.showShortToast(errMsg);
						} else {
							Alert.showShortToast("发布失败，请稍候重试");
						}
					}
				});

	}

	@Override
	public void onRequestSuccess(MyLostCardDetailInfo result) {
		Alert.cancelWait();
		_myLostCardDetailInfo = result;
		if (result.info == null) {
			onNoInfo();
		} else {
			statusText.setVisibility(View.VISIBLE);
			// 查看状态
			if (result.info.vry_time > 0) {
				if (!TextUtils.isEmpty(result.info.cardID)) {
					finding(result.info.cardID);
					return;
				} else {
					if (result.info.vry_result.equals("pass")) {
						if (TextUtils.isEmpty(result.info.cardID)) {
							statusText.setText("您登记的信息已经审核通过,可选择失卡发布");
							okButton.setVisibility(View.VISIBLE);
							hasFindButton.setVisibility(View.GONE);
						} else {
							finding(result.info.cardID);
						}
					} else if (result.info.vry_result.equals("error")) {
						if (result.info.status == 3) {
							statusText.setText("您修改的信息审核未通过，原因是:"
									+ result.info.vry_error);
							okButton.setVisibility(View.VISIBLE);
							hasFindButton.setVisibility(View.GONE);
						} else {
							statusText.setText("您登记的信息审核未通过，原因是:"
									+ result.info.vry_error);
							okButton.setVisibility(View.VISIBLE);
							hasFindButton.setVisibility(View.GONE);
						}

					} else {
						if (TextUtils.isEmpty(result.info.cardID)) {
							statusText.setText("您登记的信息正在审核中，请耐心等待...");
							okButton.setVisibility(View.GONE);
							hasFindButton.setVisibility(View.VISIBLE);
						} else {
							statusText.setText("您登记的信息正在审核中,您已经发布失卡:"
									+ result.info.cardID + "请耐心等待...");
							okButton.setVisibility(View.VISIBLE);
							hasFindButton.setVisibility(View.GONE);
						}

					}
				}

			} else {
				// 非法状态
				if (!TextUtils.isEmpty(result.info.cardID)) {
					statusText.setText("您登记的信息正在审核中,您已经发布失卡:"
							+ result.info.cardID + "请耐心等待...");
					okButton.setVisibility(View.GONE);
					hasFindButton.setVisibility(View.VISIBLE);
				} else {
					statusText.setText("您登记的信息正在审核中，请耐心等待...");
					lost_card();
				}

			}
		}

		if (clickedView != null) {
			onClick(clickedView);
			clickedView = null;
		}

	}

	protected void lost_card() {
		okButton.setVisibility(View.VISIBLE);
		hasFindButton.setVisibility(View.GONE);
	}

	protected void finding(String cardNumber) {
		if (_myLostCardDetailInfo.info.status == 3) {
			statusText.setText("您的卡" + cardNumber + "失卡信息已经发布，如雷锋捡到，相信会与您联系");

		} else {
			statusText
					.setText("您登记的信息正在审核中,您已经发布失卡:" + cardNumber + "请耐心等待...");
		}
		okButton.setVisibility(View.GONE);
		hasFindButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		Alert.cancelWait();
		Alert.showShortToast("加载信息失败,请检查您的网络");
		finish();
	}

	@Override
	protected void onDestroy() {
		Alert.cancelWait();
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Actions.REQUEST_CODE_LOGIN: {
				loadInfo();
			}
				return;
			case Actions.REQUEST_CODE_GOOD_INFO_ADD: {
				// 添加或者修改成功
				loadInfo();
			}
				return;
			}
		}

		if (requestCode == Actions.REQUEST_CODE_LOGIN
				&& resultCode == RESULT_CANCELED) {
			finish();
			return;
		}

		super.onActivityResult(requestCode, resultCode, data);

	}

	/**
	 * 没有登记
	 */
	protected void onNoInfo() {
		final DialogListener infoListener = new DialogListener() {

			@Override
			public void onDialogButton(int id) {
				// TODO Auto-generated method stub
				if (id == R.id._id_ok) {
					open_edit();
				}
			}
		};
		Alert.confirm(this, "您还没有登记信息,登记信息可帮助您在丢失卡片以后找回,去登记吗?", infoListener);
		okButton.setVisibility(View.VISIBLE);
		hasFindButton.setVisibility(View.GONE);
	}

	protected void open_edit() {
		// 加载数据失败
		Intent intent = new Intent(GoodCardActivity.this,
				GoodCardInfoActivity.class);
		if (_myLostCardDetailInfo.info != null) {
			intent.putExtra("data", _myLostCardDetailInfo.info);
		}
		startActivityForResult(intent, Actions.REQUEST_CODE_GOOD_INFO_ADD);
	}

	@Override
	public void onActivityResult(final Intent intent, int resultCode, int requestCode) {
		// 发布失去卡
		if (resultCode == Activity.RESULT_OK) {
			MessageUtil.sendMessage(new MessageUtil.IMessageListener() {
				@Override
				public void onMessage(int message) {
					String cardId = MyECardUtil.getCardId(intent);
					ModelHelper.getModel(PickCardModel.class).lostCardPublish(cardId);
				}
			});

		}
	}

	@NotificationMethod(PickCardModel.lost_publish)
	public void onLostSuccess(Object object) {
		Alert.cancelWait();
		loadInfo();
	}
	@NotificationMethod(PickCardModel.lost_revocation)
	public void onLoadRevocation(Object object) {
		Alert.cancelWait();
		// 退回成功
		if (_myLostCardDetailInfo.info.status == 3) {
			statusText.setText("您登记的信息已经审核通过,可选择失卡发布");
		} else {
			statusText.setText("您登记的信息正在审核中，请耐心等待...");
		}

		hasFindButton.setVisibility(View.GONE);
		okButton.setVisibility(View.VISIBLE);
	}
	
	

}
