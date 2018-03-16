package com.citywithincity.ecard.widget;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.ShopInfo;
import com.citywithincity.ecard.ui.activity.ShopsActivity;
import com.citywithincity.ecard.utils.JsonUtil;
import com.citywithincity.ecard.utils.ViewUtil;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.utils.ActivityUtil;

import java.util.ArrayList;

/**
// *详细里面的
 * @author Randy
 *
 */
public class FirstShopItemView implements OnClickListener,IDestroyable {
	
	private ArrayList<ShopInfo> shopsGruop;
	private Activity _parentActivity;
	public void init(Activity parent,ArrayList<ShopInfo> shops){
		if(shops.size() > 0 )
		{
			JsonUtil.setShopItemData(parent, shops.get(0));
			parent.findViewById(R.id.shop_info).setOnClickListener(this);
			parent.findViewById(R.id.shop_phone).setOnClickListener(this);
			parent.findViewById(R.id.shop_btn_all_shops).setOnClickListener(this);
			shopsGruop=shops;
			_parentActivity=parent;
			String btnTextString = "查看" + String.valueOf(shops.size()) + "家可用分店";
			((TextView)parent.findViewById(R.id.shop_all_shops_text)).setText(btnTextString);
		}else{
			parent.findViewById(R.id.first_shop_info).setVisibility(View.GONE);
		}
	}
	@Override
	public void destroy() {
		if(shopsGruop!=null){
			shopsGruop.clear();
			shopsGruop = null;
		}
		
		_parentActivity = null;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shop_info:
			ViewUtil.openRouteActivity(_parentActivity, shopsGruop.get(0));
			break;
		case R.id.shop_phone:
		{
			ActivityUtil.makeCall(_parentActivity, shopsGruop.get(0).phone);
		}
		break;
		case R.id.shop_btn_all_shops:
		{
			//查看分店
			ActivityUtil.startActivity(_parentActivity,ShopsActivity.class,shopsGruop);
		}
			
			break;
		default:
			break;
		}
	}

	
}
