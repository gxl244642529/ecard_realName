package com.citywithincity.ecard.recharge.activities;

import android.app.Activity;
import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.recharge.models.vos.RechargePointVo;
import com.citywithincity.ecard.utils.MapUtil;
import com.citywithincity.interfaces.IOnItemClickListener;
import com.damai.auto.DMFragmentActivity;
import com.damai.helper.a.Res;
import com.damai.widget.StateListView;
public class RechargePointActivity extends DMFragmentActivity implements IOnItemClickListener<RechargePointVo> {

	@Res
	private StateListView<RechargePointVo> listView;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_recharge_point_list);
		setTitle("卟噔点");
		listView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(Activity activity, RechargePointVo data,
			int position) {
	
		MapUtil.openRoutePlanActivity(this, data.getAddress(), data.getLat(), data.getLng());
	}

}
