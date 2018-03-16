package com.citywithincity.ecard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.enums.Actions;
import com.citywithincity.ecard.models.ECardContentModel;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.vos.BusinessDetail;
import com.citywithincity.ecard.models.vos.BusinessInfo;
import com.citywithincity.ecard.models.vos.CouponInfo;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.models.vos.ImageInfo;
import com.citywithincity.ecard.models.vos.ShopInfo;
import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.ecard.widget.FirstShopItemView;
import com.citywithincity.ecard.widget.WaitingFavButton;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;

import java.util.ArrayList;
import java.util.List;
@Observer
@EventHandler
public class BusinessDetailActivity extends BaseActivity implements
		OnClickListener {
	private ImageView[] businessImages;
	private ViewGroup[] couponItems;
	// 团购
	private View businessCouponView;

	protected List<ImageInfo> limages;
	protected List<CouponInfo> lcoupons;

	private FirstShopItemView _firstShopItemView;
	private WaitingFavButton _toggleCollection;
	private View viewBusinessImages;
	private View viewShopInfo;
	
	private BusinessInfo listData;
	
	@Override
	protected void onDestroy() {
		limages=null;
		lcoupons=null;
		businessCouponView=null;
		businessImages=null;
		couponItems=null;
		
		viewBusinessImages = null;
		viewShopInfo = null;
		
		super.onDestroy();
	}
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.business_detail);
		listData = (BusinessInfo)getIntent().getExtras().get("data");
		_toggleCollection = (WaitingFavButton) findViewById(R.id.toggle_collection);
		viewBusinessImages = findViewById(R.id.business_images);
		viewBusinessImages.setVisibility(View.GONE);
		viewShopInfo = findViewById(R.id.first_shop_info);
		viewShopInfo.setVisibility(View.GONE);
		initControl();
		
		
		JsonTaskManager.getInstance().setImageSrc(listData.img,(ImageView) findViewById(R.id.business_image));
		// 商家名称
		setTitle(listData.title);
		
//		ModelHelper.getModel(ECardContentModel.class).getCouponDetail(listData.id, 0);
		String userId;
		ECardUserInfo userInfo = ECardJsonManager.getInstance().getUserInfo();
		if (userInfo != null) {
			userId = TextUtils.isEmpty(userInfo.getId()) ? "0" : userInfo.getId();
		} else {
			userId = "0";
		}
		ModelHelper.getModel(ECardContentModel.class).getBusinessDetail(listData.id, listData.shopId,Integer.parseInt( userId),0);
	}
	
	@EventHandlerId(id=R.id.toggle_collection,checkLogin=true)
	public void onBtnCollection(){
		_toggleCollection.waiting();
		int action = isCollected ? 1 : 2;
		ModelHelper.getModel(ECardContentModel.class).addBusinessCollection(listData.id,
				1, action);
	}
	
	@NotificationMethod(ECardContentModel.ADD_FAV)
	public void onAddCollectionSuccess(Object isAdded){
		if (isCollected) {
			_toggleCollection.setOn(false);
			Toast.makeText(BusinessDetailActivity.this,
					"取消收藏成功", Toast.LENGTH_SHORT).show();
			isCollected = !isCollected;
		} else {
			_toggleCollection.setOn(true);
			Toast.makeText(BusinessDetailActivity.this,
					"收藏成功", Toast.LENGTH_SHORT).show();
			isCollected = !isCollected;
		}
		
	}
	
	
	
	private void initControl() {
		
		businessImages = new ImageView[3];
		businessImages[0] = (ImageView) findViewById(R.id.business_img0);
		businessImages[1] = (ImageView) findViewById(R.id.business_img1);
		businessImages[2] = (ImageView) findViewById(R.id.business_img2);
		for (int i = 0; i < 3; i++) {
			businessImages[i].setOnClickListener(this);
		}

		// 商家优惠券
		couponItems = new ViewGroup[3];
		couponItems[0] = (ViewGroup) findViewById(R.id.business_coupon_item0);
		couponItems[1] = (ViewGroup) findViewById(R.id.business_coupon_item1);
		couponItems[2] = (ViewGroup) findViewById(R.id.business_coupon_item2);
		for (int i = 0; i < 3; i++) {
			couponItems[i].setOnClickListener(this);
		}
		findViewById(R.id.business_show_more_img).setOnClickListener(this);
		// 优惠劵
		businessCouponView = findViewById(R.id.business_coupon_list);
		businessCouponView.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 联盟商家相册
		case R.id.business_img0:
		case R.id.business_img1:
		case R.id.business_img2:
		case R.id.business_show_more_img:
			String[] photosStringArray = new String[limages.size()];
			int i = 0;
			for (ImageInfo url : limages) {
				photosStringArray[i++] = url.big;
			}

			ActivityUtil.startActivity(BusinessDetailActivity.this,ImageActivity.class, photosStringArray);
			break;
		// 优惠劵
		case R.id.business_coupon_item0: {
			CouponInfo info = lcoupons.get(0);
			enterCouponDetail(info);
			break;
		}
		case R.id.business_coupon_item1: {
			CouponInfo info = lcoupons.get(1);
			enterCouponDetail(info);
		}
			break;
		case R.id.business_coupon_item2: {
			CouponInfo info = lcoupons.get(2);
			enterCouponDetail(info);
		}
			break;
		default:
			break;
		}

	}

	private void enterCouponDetail(CouponInfo info) {
		ActivityUtil.startActivity(this, CouponDetailActivity.class,info);
	}


	protected boolean isCollected;


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode)
		{
		case Actions.REQUEST_CODE_LOGIN:
		{
			
		}
			break;
		}
	}

	@NotificationMethod(ECardContentModel.BUSINESS_DETAIL)
	public void onLoadComplete(BusinessDetail result){
		viewBusinessImages.setVisibility(View.VISIBLE);
		viewShopInfo.setVisibility(View.VISIBLE);
		
		isCollected=result.isCollected;
		((TextView) findViewById(R.id.business_detail)).setText(result.detail);

		ArrayList<ShopInfo> shops = result.shops;
		if (shops.size() > 0) {
			_firstShopItemView = new FirstShopItemView();
			_firstShopItemView.init(BusinessDetailActivity.this, shops);
		}

		int count = result.coupons.size();
		for (int i = 0; i < count; ++i) {
			result.coupons.get(i).bsname = listData.title;
		}

		_toggleCollection.setOn(result.isCollected);

		limages = result.images;
		lcoupons = result.coupons;


		// 优惠券
		if (limages.size() > 0) {
			count = Math.min(limages.size(), businessImages.length);
			for (int i = 0; i < count; i++) {
				JsonTaskManager.getInstance().setImageSrc(limages.get(i).small, businessImages[i]);
			}
		}
		// 优惠券
		if (lcoupons.size() > 0) {
			businessCouponView.setVisibility(View.VISIBLE);

			for (int i = 0; i < lcoupons.size(); i++) {
				JsonUtil.setCouponData(JsonTaskManager.getInstance(), couponItems[i], lcoupons.get(i));
			}
			for (int j = lcoupons.size(); j < 3; j++) {
				couponItems[j].setVisibility(View.GONE);
			}
		}
	}
	

}
