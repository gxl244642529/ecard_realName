package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.ShopInfo;
import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.ecard.utils.ViewUtil;
import com.citywithincity.interfaces.IListDataProviderListener;
import com.citywithincity.models.ListDataProvider;
import com.citywithincity.utils.ActivityUtil;

import java.util.ArrayList;

public class ShopsActivity extends BaseActivity implements
		IListDataProviderListener<ShopInfo>, OnClickListener {

	ListDataProvider<ShopInfo> dataProvider;
	ArrayList<ShopInfo> shops;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView( R.layout.shops_layout);
		setTitle("分店地址");
		shops = (ArrayList<ShopInfo>) getIntent().getExtras().get("data");

		dataProvider = new ListDataProvider<ShopInfo>(this,
				R.layout.shops_item, this);
		ListView listView = (ListView) findViewById(R.id._list_view);
		listView.setAdapter(dataProvider);
		dataProvider.setData(shops, false);
	}
	

	@Override
	public void onInitializeView(View view, ShopInfo data, int position) {
		JsonUtil.setShopItemData(view, data);
		view.findViewById(R.id.shop_info).setTag(data);
		view.findViewById(R.id.shop_phone).setTag(data);
		view.findViewById(R.id.shop_info).setOnClickListener(this);
		view.findViewById(R.id.shop_phone).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		ShopInfo data = (ShopInfo)v.getTag();
		int id= v.getId();
		if(id==R.id.shop_info){
			ViewUtil.openRouteActivity(ShopsActivity.this, data);
		}else{
			ActivityUtil.makeCall(ShopsActivity.this, data.phone);
		}
	}

	

}
