package com.citywithincity.ecard.selling.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.selling.models.ShopModel;
import com.citywithincity.ecard.selling.models.vos.SCardListVo;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.ActivityUtil;
import com.citywithincity.widget.StateListView;

/**
 * 我的收藏
 * 
 * @author randy
 * 
 */
@Observer
@EventHandler
public class SMyCollectionActivity extends BaseActivity implements OnClickListener, IListDataProviderListener<SCardListVo> {

	// 集成上拉刷新
//	protected PullToRefreshListGroup<SCardListVo, GridView> dataGroup;
	private StateListView<SCardListVo> listView;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.s_activity_my_collection);
		setTitle("我的收藏");
		
		findViewById(R.id._title_right).setOnClickListener(this);
		
		listView = (StateListView<SCardListVo>) findViewById(R.id._list_view);
		listView.setItemRes(R.layout.cardlist_grid_item);
		listView.setOnItemClickListener(new OnItemClickListenerOpenActivity<SCardListVo>(SCardDetailActivity.class));
		listView.setTask(ModelHelper.getModel(ShopModel.class).getCollectionList(LibConfig.StartPosition));


        listView.setDataListener(this);
	}
	
	
	


//	@Override
//	public void onInitializeView(View view, SCardListVo data, int position) {
//		//数据的加载(加载到的是cardlist_grid_item.xml布局里面)
//		ImageView imageView = (ImageView) view.findViewById(R.id.card_image);
//		
//		ECardJsonManager.getInstance().setImageSrc(data.thumb, imageView);
//		TextView priceTextView = (TextView) view.findViewById(R.id.card_price_text);
//		priceTextView.setText("￥:"+data.price);
//	}

	@Override
	public void onDestroy() {
		ECardJsonManager.getInstance().onDestroy(this);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id._title_right:
			ActivityUtil.startActivity(SMyCollectionActivity.this, SCartActivity.class);
			break;

		default:
			break;
		}
		
	}

    @Override
    public void onInitializeView(View view, SCardListVo data, int position) {
        ImageView imageView = (ImageView) view.findViewById(R.id.thumb);
		ECardJsonManager.getInstance().setImageSrc(data.getThumb(), imageView);
		TextView priceTextView = (TextView) view.findViewById(R.id.formatPrice);
		priceTextView.setText("￥:"+ String.format("%.2f", (float) data.getPrice() /100 ) );

        TextView title = (TextView)view.findViewById(R.id.title);
        title.setText(data.getTitle());
    }
}
