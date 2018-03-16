package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.selling.diy.models.DiyCard;
import com.citywithincity.ecard.selling.diy.models.dao.CardManager;
import com.citywithincity.ecard.selling.models.DiyModel;
import com.citywithincity.ecard.selling.models.vos.SDiyPagesVo;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.models.ListGroup.IListGroupListener;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;
import com.citywithincity.widget.HorizontalListView;

import java.util.List;

@Observer
public class SMyDiyBackPageActivity extends BaseActivity implements
		OnItemClickListener, IListGroupListener<SDiyPagesVo> {

	private ImageView imageView;

	private DiyCard currentCard;

	private static HorizontalListView bgListView;
	private ListDataProvider<SDiyPagesVo> bgDataProvider;
	private TextView price,stock;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.diy_pages_background_view);

		ViewUtil.initParam(this);
		setTitle("选择反面");
		findView();
		// 判断是否从草稿的页面点击进来的
		bgDataProvider = new ListDataProvider<SDiyPagesVo>(this,R.layout.diy_online_page_item, this);
		bgListView.setAdapter(bgDataProvider);
		bgListView.setOnItemClickListener(this);
		currentCard = ModelHelper.getModel(CardManager.class).getCurrent();
		
		onLoadData(LibConfig.StartPosition);// 加载反面的数据
		Alert.wait(this, "正在加载数据...");

		if (!TextUtils.isEmpty(currentCard.fImage)) {
			ECardJsonManager.getInstance().setImageSrc(currentCard.fImage,
					imageView);
//			setImageViewSize();
		}
		
		findViewById(R.id._title_right).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ModelHelper.getModel(CardManager.class).save();
				finish();
			}
		});
	}

	private void findView() {
		bgListView = (HorizontalListView) findViewById(R.id.card_diy_background_gallery);
		imageView = (ImageView) findViewById(R.id.card_diy_background_imageview);
		
		price = (TextView) findViewById(R.id.price);
		stock = (TextView) findViewById(R.id.stock);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> paramAdapterView, View paramView,
			int position, long paramLong) {

		SDiyPagesVo vo = (SDiyPagesVo) paramAdapterView.getAdapter().getItem(
				position);
		currentCard.bgIdS = vo.id;
		currentCard.fImage = vo.img;
		currentCard.nameCard = vo.title;
		currentCard.stock = vo.stock;
		currentCard.price = vo.price;
		ECardJsonManager.getInstance().setImageSrc(vo.img, imageView);
		price.setText(String.format("%.2f",currentCard.price));
		stock.setText(String.valueOf(currentCard.stock));
	}

	@Override
	public void onInitializeView(View view, SDiyPagesVo data, int position) {
		
		if (position == 0) {
			currentCard.bgIdS = data.id;
			currentCard.fImage = data.img;
			currentCard.nameCard = data.title;
			currentCard.stock = data.stock;
			currentCard.price = data.price;
			ECardJsonManager.getInstance().setImageSrc(data.img, imageView);
			price.setText(String.format("%.2f",currentCard.price));
			stock.setText(String.valueOf(currentCard.stock));
//			setImageViewSize();
		}
		// TODO Auto-generated method stub
		ImageView thumImageView = (ImageView) view
				.findViewById(R.id.diy_online_thumb_pages);
		ECardJsonManager.getInstance().setImageSrc(data.img, thumImageView);
	}
	
	@NotificationMethod(DiyModel.LIST)
	public void onGetBackPageSuccess(List<SDiyPagesVo> result,boolean isLastPage) {
		bgDataProvider.setData(result, false);
		Alert.cancelWait();
	}
	
	@NotificationMethod(DiyModel.LIST)
	public void onGetBackPageError(String error) {
		Alert.cancelWait();
		Alert.showShortToast(error);
	}

	@Override
	protected void onDestroy() {
		Alert.cancelWait();
		super.onDestroy();

	}

	@Override
	public void finish() {
//		CardManager.getInstance().save();
		super.finish();
	}

	@Override
	public void onLoadData(int position) {
		ModelHelper.getModel(DiyModel.class).getList(position);
		
	}

}
