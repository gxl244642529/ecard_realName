package com.citywithincity.ecard.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.enums.Actions;
import com.citywithincity.ecard.models.ECardContentModel;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.vos.CouponDetail;
import com.citywithincity.ecard.models.vos.CouponInfo;
import com.citywithincity.ecard.models.vos.CouponType;
import com.citywithincity.ecard.models.vos.ImageInfo;
import com.citywithincity.ecard.utils.QrUtil;
import com.citywithincity.ecard.widget.FirstShopItemView;
import com.citywithincity.ecard.widget.WaitingFavButton;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;

/**
 * @author randy
 * 
 */
@Observer
public class CouponDetailActivity extends BaseActivity implements
		OnClickListener {
	// 验证信息
	private TextView usednumTextView;
	// 收藏切换
	private WaitingFavButton _toggleCollection;
	/**
	 * 优惠券信息
	 */
	private CouponDetail _couponDetail;

	private CouponInfo listData;
	// 第一家分店信息
	private FirstShopItemView _firstShopItemView;

	@Override
	protected void onDestroy() {
		if (_firstShopItemView != null) {
			_firstShopItemView.destroy();
			_firstShopItemView = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.coupon_detail);
		
		listData = (CouponInfo)getIntent().getExtras().get("data");
		// 判断是我的还是一般的
		setTitle("优惠券详情");
		usednumTextView = (TextView) findViewById(R.id.coupon_used_count);
	
		usednumTextView.setVisibility(View.GONE);

		findViewById(R.id.coupon_img_group).setVisibility(View.GONE);
		findViewById(R.id.business_img0).setOnClickListener(this);
		findViewById(R.id.business_img1).setOnClickListener(this);
		findViewById(R.id.business_img2).setOnClickListener(this);
		_toggleCollection = (WaitingFavButton) findViewById(R.id.toggle_collection);
		_toggleCollection.setOnClickListener(favListener);
		
		loadingView = findViewById(R.id.loading_layout);
		errorView = findViewById(R.id.network_error);
		
		ModelHelper.getModel(ECardContentModel.class).getCouponDetail(listData.id, 0);
		
	}

	private OnClickListener favListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {

			default:
				if (JsonTaskManager.getInstance().isLogin()) {
					_toggleCollection.waiting();
					//ModelHelper.getModel(ECardContentModel.class).addCouponCollection(, add, listener)
				} else {
					// 登录
					JsonTaskManager.getInstance().requestLogin(CouponDetailActivity.this);
					
				}
				break;
			}

		}
	};

	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 联盟商家相册
		case R.id.business_img0:
		case R.id.business_img1:
		case R.id.business_img2:
		case R.id.business_show_more_img:
			String[] photosStringArray = new String[_couponDetail.images.size()];
			int i = 0;
			for (ImageInfo url : _couponDetail.images) {
				photosStringArray[i++] = url.big;
			}

			ActivityUtil.startActivity(CouponDetailActivity.this,
					ImageActivity.class, photosStringArray);
			break;
		case R.id.coupon_fetch: {
			switch (listData.type) {
			case CouponType_Limit:
				if (ECardJsonManager.getInstance().isLogin()) {
					
				} else {
					JsonTaskManager.getInstance().requestLogin(this);
				}
				break;
			case CouponType_Qr:// 显示二维码
			case CouponType_Show:// 显示图片
			{
				// _qrView.setCode(_couponDetail.code);
				if (_couponDetail != null)
					showQr(_couponDetail.code);
			}
				break;
			default:
				break;
			}

		}
			break;

		default:
			break;
		}
	}

	private void showQr(String code) {
		QrUtil.showQrView(this, QrUtil.Qr_Coupon, code);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Actions.REQUEST_CODE_SEND_GIFT:
			if (resultCode == RESULT_OK)
			break;
		}
	}

//	@Override
//	public void onRequestSuccess(Boolean result) {
//		_couponDetail.isCollected = result;
//		if (result) {
//			Toast.makeText(CouponDetailActivity.this, "收藏成功",
//					Toast.LENGTH_SHORT).show();
//		} else {
//			Toast.makeText(CouponDetailActivity.this, "取消收藏成功",
//					Toast.LENGTH_SHORT).show();
//		}
//		_toggleCollection.setOn(result);
//
//	}
//
//	@Override
//	public void onRequestError(String errMsg, boolean isNetworkError) {
//		_toggleCollection.setVisibility(View.GONE);
//		Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_LONG)
//				.show();
//	}
	
	private View loadingView,errorView;
	
	@NotificationMethod(ECardContentModel.COUPON_DETAIL)
	public void onDataLoadError(String result) {
		Alert.showShortToast(result);
		loadingView.setVisibility(View.GONE);
	}

	@NotificationMethod(ECardContentModel.COUPON_DETAIL)
	public void onDataLoadComplete(CouponDetail result) {
		_couponDetail = result;
		if (result.images != null && result.images.size() > 0) {

			// 图片
			findViewById(R.id.coupon_img_group).setVisibility(View.VISIBLE);
			int count = Math.min(result.images.size(), 3);
			if (count >= 1) {
				JsonTaskManager.getInstance().setImageSrc(result.images.get(0).small,
						(ImageView) findViewById(R.id.business_img0));
			}
			if (count >= 2) {
				JsonTaskManager.getInstance().setImageSrc(result.images.get(1).small,
						(ImageView) findViewById(R.id.business_img1));
			}
			if (count >= 2) {
				JsonTaskManager.getInstance().setImageSrc(result.images.get(2).small,
						(ImageView) findViewById(R.id.business_img2));
			}
		}

		_toggleCollection.setOn(result.isCollected);
		if (listData.type == CouponType.CouponType_ECard) {
			usednumTextView.setVisibility(View.GONE);
		} else {
			usednumTextView.setVisibility(View.VISIBLE);
			usednumTextView.setText(String.valueOf(result.used) + "人验证");
		}
		JsonTaskManager.getInstance().setImageSrc(listData.img,
				(ImageView) findViewById(R.id.coupon_img));

		_firstShopItemView = new FirstShopItemView();
		_firstShopItemView.init(CouponDetailActivity.this, result.shops);
		String veryfyText = null;
		switch (listData.type) {
		case CouponType_Limit:
			veryfyText = ("验证有效，剩余" + (result.leftCount) + "张");
			break;
		case CouponType_Qr:
			veryfyText = ("验证有效，不限次数");
			break;
		case CouponType_Show:
			veryfyText = ("出示即可使用");
			break;
		case CouponType_ECard:
			veryfyText = ("刷e通卡享受优惠");
			break;
		default:

			break;
		}
		// 有效期
		ViewUtil.setTextFieldValue(this, R.id.coupon_period_of_validity,
				listData.title);

		ViewUtil.setTextFieldValue(this, R.id.coupon_verify_text, veryfyText);
		// 剩余时间
		ViewUtil.setTextFieldValue(this, R.id.coupon_lifttime_text,
				result.leftTime);
		// 使用须知
		ViewUtil.setTextFieldValue(this, R.id.coupon_tip, result.tip);

		loadingView.setVisibility(View.GONE);
		errorView.setVisibility(View.GONE);

	}

	

}
