package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardContentModel;
import com.citywithincity.ecard.models.vos.CouponInfo;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.OnItemClickListenerOpenActivity;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.widget.StateListView;


/**
 * 
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @since Mar 12, 2013
 * @version 1.0.0
 */
public class CouponActivity extends BaseActivity {
	
	

	@SuppressWarnings("unchecked")
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.base_lv);
		setTitle("优惠券");
		StateListView<CouponInfo> listView = (StateListView<CouponInfo>)findViewById(R.id._list_view);
		listView.setItemRes(R.layout.coupon_item_v2);
		listView.setOnItemClickListener(new OnItemClickListenerOpenActivity<CouponInfo>( CouponDetailActivity.class));
		listView.setTask(ModelHelper.getModel(ECardContentModel.class).getCouponList(LibConfig.StartPosition, 0, 0));
		
	}
	


	
}
