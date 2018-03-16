package com.citywithincity.ecard.selling.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.models.CartBModel.IOnCartItemChangeListener;
import com.citywithincity.ecard.selling.models.CartModel;
import com.citywithincity.ecard.selling.models.DiyModel;
import com.citywithincity.ecard.selling.models.OrderBModel;
import com.citywithincity.ecard.selling.models.ShopModel;
import com.citywithincity.ecard.selling.models.vos.SCardDetailVo;
import com.citywithincity.ecard.selling.models.vos.SCardListVo;
import com.citywithincity.ecard.selling.views.AddToCartView;
import com.citywithincity.ecard.utils.ShareUtil;
import com.citywithincity.interfaces.IViewPagerDataProviderListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.ViewPagerDataProvider;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;
import com.citywithincity.widget.StateScrollView;
import com.citywithincity.widget.ViewPagerIndicator;
import com.damai.core.DMLib;
import com.damai.helper.a.InitData;

import java.util.List;

/**
 * 售卡详情
 * 
 * @author randy
 * 
 */
@EventHandler
@Observer
public class SCardDetailActivity extends BaseActivity implements
		IViewPagerDataProviderListener<String>, OnGlobalLayoutListener {
	@InitData
	private SCardListVo sCardListVo;
	private ViewPager viewPager;
	private ViewPagerDataProvider<String> dataProvider;
	private boolean isCollected = false;
	private TextView favState;
	private View bottomView;
	private ViewPagerIndicator indicator;
	private SCardDetailVo detailData;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.card_detail);
		setTitle(R.string.card_detail);
		favState = (TextView) findViewById(R.id._text_view);
		bottomView = findViewById(R.id.bottom_container);
		bottomView.setVisibility(View.GONE);
		indicator = (ViewPagerIndicator) findViewById(R.id._indicator_view);
		dataProvider = new ViewPagerDataProvider<String>(getLayoutInflater(),
				R.layout.card_detail_pages_bg_view, this);
		viewPager = (ViewPager) findViewById(R.id._view_pager);
		viewPager.setAdapter(dataProvider);
		viewPager.setOnPageChangeListener(dataProvider);
		//ViewUtil.setBinddataViewValues(sCardListVo, this);

		if(sCardListVo==null){
			sCardListVo = (SCardListVo) getIntent().getExtras().get("data");
		}


        TextView text = (TextView) findViewById(R.id.id_disprice);
        text.setText(sCardListVo.getFormatPrice());
        text = (TextView) findViewById(R.id.id_title);
        text.setText(sCardListVo.getTitle());


		@SuppressWarnings("unchecked")
		StateScrollView<SCardDetailVo> scrollView = (StateScrollView<SCardDetailVo>) findViewById(R.id._scroll_view);
		scrollView.setTask(ModelHelper.getModel(ShopModel.class)
				.getSCardDetail(sCardListVo.getId()));

		viewPager.getViewTreeObserver().addOnGlobalLayoutListener(this);
		
		if (isLogin()) {
			isCollected();
		}
	}

	@Override
	public void onGlobalLayout() {
		LayoutParams lp = (LayoutParams) viewPager.getLayoutParams();
		lp.height = (int) ((float) viewPager.getWidth()
				* DiyModel.MIN_CARD_HEIGHT / DiyModel.MIN_CARD_WIDTH);
		viewPager.setLayoutParams(lp);

		viewPager.getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	@NotificationMethod(ShopModel.CARD_DETAIL)
	public void onDataLoadComplete(SCardDetailVo data) {
		detailData = data;
		ViewUtil.setBinddataViewValues(data, this);
		bottomView.setVisibility(View.VISIBLE);
		indicator.setIndicatorCount(data.list.size());
		dataProvider.setData(data.list);
	}

	@EventHandlerId(id = R.id._id_share)
	public void onBtnShare() {
		showShare(sCardListVo);
	}

	@EventHandlerId(id = R.id.id_collection, checkLogin = true)
	public void onBtnCollect() {
		if (!isCollected) {
			ModelHelper.getModel(ShopModel.class).collection(sCardListVo.getId());
			// SellingModel.getInstance().addCollectionCard(sCardListVo.id,addCollectionReqest);
		} else {
			cancleCollection();
		}
	}

	@EventHandlerId(id = R.id._id_ok, checkLogin = true)
	public void onBtnBuy() {
		new AddToCartView(this).setCount(1).setMaxCount(detailData.stock)
				.setRecharge(0).show()
				.setListener(new IOnCartItemChangeListener() {

					@Override
					public void onChange(int count, int recharge) {
						if (detailData.stock >= count) {
							// 购买,直接购买
							ModelHelper.getModel(OrderBModel.class).buy(
									SCardDetailActivity.this, sCardListVo,
									detailData.rawPrice, count, recharge);
						} else {
							Alert.showShortToast("购买的商品库存不足");
						}

					}
				});
	}

	@EventHandlerId(id = R.id.id_add_cart, checkLogin = true)
	public void onBtnAddToCart() {
		new AddToCartView(this).setCount(1).setRecharge(0)
				.setMaxCount(detailData.stock).show()
				.setListener(new IOnCartItemChangeListener() {

					@Override
					public void onChange(int count, int recharge) {
						if (detailData.stock >= count) {
							// 加入购物车
							ModelHelper.getModel(CartModel.class).add(
									sCardListVo.getId(), count, recharge, null);
						} else {
							Alert.showShortToast("购买的商品库存不足");
						}

					}
				});
	}

	@NotificationMethod(CartModel.ADD)
	public void onAddToCartSuccess(Object value) {
		Alert.showShortToast("加入购物车成功");
		ModelHelper.getModel(CartModel.class).getList(LibConfig.StartPosition);
	}
	

	@EventHandlerId(id = R.id.card_pages_detail)
	public void onBtnViewDetail() {
		String url = String.format("%s/index.php/api2/s_card_info2/index/%d",
				ECardConfig.BASE_URL, sCardListVo.getId());
		ActivityUtil.startActivity(this, SCardPagesDetail.class, url);
	}

	@Override
	protected void onDestroy() {
		dataProvider.destroy();
		dataProvider = null;
		super.onDestroy();
	}

	@Override
	public void onViewPagerDestroyView(View view, int index) {

	}

	@Override
	public void onViewPagerCreateView(View view, int index, String data) {
		ImageView imageView = (ImageView) view;
		DMLib.getJobManager().loadImage(data,imageView);
	//	ECardJsonManager.getInstance().setImageSrc(data, imageView);
	}

	@Override
	public void onViewPagerPageSelect(View view, int index, String data) {
		indicator.setCurrent(index);
	}

	@NotificationMethod(ShopModel.COLLECTION)
	public void onCollectionSuccess(boolean result) {
		if (result) {
			Alert.showShortToast("收藏成功");
			isCollected = true;
			favState.setText("取消收藏");
			Drawable topDrawable = getResources().getDrawable(
					R.drawable.s_card_colleted);
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
					topDrawable.getMinimumHeight());
			favState.setCompoundDrawables(null, topDrawable, null, null);
		}
	}

	@NotificationMethod(ShopModel.COLLECTION)
	public void onCollectionError(String error) {
		Alert.showShortToast(error);
	}

	@NotificationMethod(ShopModel.CANCLE_COLLECTION)
	public void onCancleCollectionSuccess(boolean result) {
		if (result) {
			Alert.showShortToast("取消收藏成功");
			isCollected = false;
			favState.setText("收藏");
			Drawable topDrawable = getResources().getDrawable(
					R.drawable.s_card_colletion);
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
					topDrawable.getMinimumHeight());
			favState.setCompoundDrawables(null, topDrawable, null, null);
		}
	}

	@NotificationMethod(ShopModel.CANCLE_COLLECTION)
	public void onCancleCollectionError(String error) {
		Alert.showShortToast(error);
	}

	private void showShare(SCardListVo sCardListVo) {
		ShareUtil.share(this, "在e通卡APP买卡款式多，还包邮！你也来试试？\n" );
	}

	private void cancleCollection() {
		// SellingModel.getInstance().sFavDelete(id, isDeleteListener);
		ModelHelper.getModel(ShopModel.class).cancleCollection(sCardListVo.getId());
	}

	private void isCollected() {
		ModelHelper.getModel(ShopModel.class).getCollectionList(
				LibConfig.StartPosition);
	}

	@NotificationMethod(ShopModel.COLLECTION_LIST)
	public void onGetCollectionList(List<SCardListVo> result, boolean isLastPage) {
		for (SCardListVo sCardListVo : result) {
			if (sCardListVo.getId() == this.sCardListVo.getId()) {
				isCollected = true;
				break;
			}
		}
		if (isCollected) {
			isCollected = true;
			favState.setText("取消收藏");
			Drawable topDrawable = getResources().getDrawable(
					R.drawable.s_card_colleted);
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
					topDrawable.getMinimumHeight());
			favState.setCompoundDrawables(null, topDrawable, null, null);
		} else {
			isCollected = false;
			favState.setText("收藏");
			Drawable topDrawable = getResources().getDrawable(
					R.drawable.s_card_colletion);
			topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
					topDrawable.getMinimumHeight());
			favState.setCompoundDrawables(null, topDrawable, null, null);
		}
	}

}
