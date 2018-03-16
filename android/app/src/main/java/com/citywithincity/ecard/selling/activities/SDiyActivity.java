package com.citywithincity.ecard.selling.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseFragmentActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.ItemEventHandler;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.selling.activities.tutorial.FirstIntoDiyFragment;
import com.citywithincity.ecard.selling.diy.models.DiyCard;
import com.citywithincity.ecard.selling.diy.models.dao.CardManager;
import com.citywithincity.ecard.selling.models.CartBModel.IOnCartItemChangeListener;
import com.citywithincity.ecard.selling.models.CartModel;
import com.citywithincity.ecard.selling.models.DiyModel;
import com.citywithincity.ecard.selling.models.OrderBModel;
import com.citywithincity.ecard.selling.models.vos.SDiyPagesVo;
import com.citywithincity.ecard.selling.utils.DiyUtils;
import com.citywithincity.ecard.selling.views.AddToCartView;
import com.citywithincity.ecard.utils.ShareUtil;
import com.citywithincity.imageeditor.EditorUtil;
import com.citywithincity.interfaces.ILocalData;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.LocalData;
import com.citywithincity.models.LocalData.LocalDataMode;
import com.citywithincity.models.SelectImage;
import com.citywithincity.models.SelectImage.ISelectImageListener;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.FragmentUtil;
import com.citywithincity.utils.ImageUtil;
import com.citywithincity.utils.SDCardUtil;
import com.citywithincity.utils.ViewUtil;
import com.damai.core.DMAccount;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Observer
@EventHandler
@ItemEventHandler
public class SDiyActivity extends BaseFragmentActivity implements
		OnClickListener {

	public static int FRONT_IAMGE = 0;
	public static int BACK_IAMGE = 1;

	public static int creatId;
	public static int diyId;

	private ImageView frontImageView;
	private ImageView backImageView;
	private View btnShare, btnAddCart;
	private Button btnPay;
	private TextView cartNum;
	private DiyCard card;
	private FirstIntoDiyFragment fragment;
	private TextView stock, price;


	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.new_diy_main);
		setTitle(R.string.diy2);
		findView();
		ILocalData data = LocalData.createDefault(LocalDataMode.MODE_READ);
		if (data.getBoolean("FirstIntoDIY", true)) {
			fragment = new FirstIntoDiyFragment();
			FragmentUtil.addFragment(this, R.id.container_fragment, fragment,
					true, false);
		}
		data.destroy();
		frontImageView.setOnClickListener(this);
		backImageView.setOnClickListener(this);

		ModelHelper.getModel(DiyModel.class).getList(LibConfig.StartPosition);
	}

	@NotificationMethod(DiyModel.LIST)
	public void onGetBackPageSuccess(List<SDiyPagesVo> result,
			boolean isLastPage) {
		if (result != null && result.size() != 0) {
			card.fImage = result.get(0).img;
			card.bgIdS = result.get(0).id;
			card.nameCard = result.get(0).title;
			card.stock = result.get(0).stock;
			card.price = result.get(0).price;
			ECardJsonManager.getInstance().setImageSrc(card.fImage,
					backImageView);

			price.setText(String.format("%.2f", card.price));
			stock.setText(String.valueOf(card.stock));
		}
	}

	private void gotoEdit(String file, boolean isNewFile) {
		EditorUtil.editImage(SDiyActivity.this, file, FRONT_IAMGE,
				ECardConfig.MIN_CARD_WIDTH, ECardConfig.MIN_CARD_HEIGHT,
				"为保证打印质量，图片宽度必须至少为" + ECardConfig.MIN_CARD_WIDTH + "像素",
				isNewFile);
	}

	private SelectImage selectImage;

	@Override
	public void onClick(View paramView) {
		switch (paramView.getId()) {
		case R.id.diy_back_pages:
			ActivityUtil.startActivity(this, SMyDiyBackPageActivity.class);
			break;
		case R.id.diy_front_pages: {
			if (card.imgPathCard == null) {
				if (selectImage == null) {
					selectImage = new SelectImage(this);
					selectImage.setListenr(new ISelectImageListener() {

						@Override
						public void onSelectImage(File file) {
							gotoEdit(file.getAbsolutePath(), true);
						}
					});
				}
				try{
					selectImage.selectImage(new File(SDCardUtil.getSDCardDir(this,
							"diy"), System.currentTimeMillis() + ".jpg"));
				}catch (IOException e){
					Alert.alert(SDiyActivity.this,"文件打开失败");
				}


			} else {
				gotoEdit(card.imgPathCard, false);
			}
		}

			break;
		case R.id._id_share:
			showShare(card);
			break;

		case R.id._title_right:
			if (ECardJsonManager.getInstance().isLogin()) {
				ActivityUtil.startActivity(this, SCartActivity.class);
			} else {
				DMAccount.callLoginActivity(this, null);
			}
			break;

		case R.id.id_add_cart:
			if (!ECardJsonManager.getInstance().isLogin()) {
				DMAccount.callLoginActivity(this, null);
				return;
			}

			if (TextUtils.isEmpty(card.fImage)) {
				Alert.showShortToast("请选择反面图片");
				return;
			}
			if (TextUtils.isEmpty(card.imgPathCard)) {
				Alert.showShortToast("请选择正面图片");
				return;
			}



			new AddToCartView(this).setCount(1).setRecharge(0)
					.setMaxCount(card.stock).show()
					.setListener(new IOnCartItemChangeListener() {

						@Override
						public void onChange(int count, int recharge) {
							if (card.stock >= count) {

								try {
									ModelHelper.getModel(CartModel.class).add(
                                            card.bgIdS, count, recharge, DiyUtils.getDiy(SDiyActivity.this,card.imgPathCard));
								} catch (IOException e) {
									Alert.alert(SDiyActivity.this,"图片加载失败");
								}
							} else {
								Alert.showShortToast("购买的商品库存不足");
							}

						}
					});

			break;

		case R.id.button1:
			if (!ECardJsonManager.getInstance().isLogin()) {
				DMAccount.callLoginActivity(this, null);
				return;
			}
			if (TextUtils.isEmpty(card.fImage)) {
				Alert.showShortToast("请选择反面图片");
				return;
			}
			if (TextUtils.isEmpty(card.imgPathCard)) {
				Alert.showShortToast("请选择正面图片");
				return;
			}

			new AddToCartView(this).setCount(1).setMaxCount(card.stock)
					.setRecharge(0).show()
					.setListener(new IOnCartItemChangeListener() {

						@Override
						public void onChange(int count, int recharge) {
							if (card.stock >= count) {
								// 购买,直接购买
								ModelHelper.getModel(OrderBModel.class).buyDIY(
										SDiyActivity.this, card, count,
										recharge);
							} else {
								Alert.showShortToast("购买的商品库存不足");
							}

						}
					});

			break;
		default:
			break;
		}
	}

	@NotificationMethod(CartModel.ADD)
	public void onAddToCartSuccess(Object value) {
		Alert.showShortToast("加入购物车成功");
	}


	private void findView() {
		findViewById(R.id._title_right).setOnClickListener(this);

		frontImageView = (ImageView) findViewById(R.id.diy_front_pages);
		backImageView = (ImageView) findViewById(R.id.diy_back_pages);

		price = (TextView) findViewById(R.id.price);
		stock = (TextView) findViewById(R.id.stock);

		btnAddCart = findViewById(R.id.id_add_cart);
		btnPay = (Button) findViewById(R.id.button1);
		btnShare = findViewById(R.id._id_share);

		cartNum = (TextView) findViewById(R.id.num);
		cartNum.setVisibility(View.INVISIBLE);

		btnAddCart.setOnClickListener(this);
		btnPay.setOnClickListener(this);
		btnShare.setOnClickListener(this);

	}


	@Override
	protected void onDestroy() {
		card = null;
		fragment = null;
		super.onDestroy();
		price = null;
		stock = null;
	}

	@Override
	protected void onResume() {
		super.onResume();
		onSetImage();
		price.setText(String.format("%.2f", card.price));
		stock.setText(String.valueOf(card.stock));


	}

	private void onSetImage() {
		card = ModelHelper.getModel(CardManager.class).getCurrent();
		if (!TextUtils.isEmpty(card.imgPathCard)) {
			Bitmap frontBitmap = ImageUtil.decodeBitmap(card.imgPathCard,
					ViewUtil.screenWidth, ViewUtil.screenHeight);// 正面
			frontImageView.setImageBitmap(frontBitmap);
		}

		if (!TextUtils.isEmpty(card.fImage)) {
			ECardJsonManager.getInstance().setImageSrc(card.fImage,backImageView);// 反面
		} 
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (selectImage != null
				&& selectImage.onActivityResult(requestCode, resultCode, data))
			return;

		if (resultCode == RESULT_OK) {
			if (requestCode == FRONT_IAMGE) {
				try{
					String image = data.getExtras().getString(LibConfig.DATA_NAME);
					if (!TextUtils.isEmpty(image)
							&& !image.equals(card.imgPathCard)) {
						card.imgPathCard = image;
						// 创建缩略图
						File file = new File(image + "_thumb.jpg");
						Bitmap bitmap = ImageUtil.decodeBitmap(image, 200, 200);
						ImageUtil.saveAs(bitmap, file);
						card.thumb = file.getAbsolutePath();
						ModelHelper.getModel(CardManager.class).save();

						onSetImage();

					}
				}catch (IOException e){
					Alert.showShortToast("文件保存失败");
				}

			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showShare(DiyCard card) {
		ShareUtil.share(this, "我DIY了一张属于自己的e通卡，你也来试试！？\n");
	}
	
}
