package com.citywithincity.ecard.nfc;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.myecard.activities.MyECardActivity;
import com.citywithincity.ecard.nfc.NfcResult;
import com.citywithincity.ecard.recharge.activities.RechargeECardActivity;
import com.citywithincity.ecard.ui.activity.NfcResultActivity;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;
import com.damai.helper.DataHolder;

public class NfcDialog  {
	
	private final Activity context;
	DialogListener listener;
	public NfcDialog(Activity context,DialogListener listener){
		this.context = context;
		//new NfcModelImpl((IViewContainer)context,INfcReader.MODE_HISTORY ).setListener(this);
		this.listener = listener;
	}


	public void onNecReaded(final NfcResult result) {
		if(result==null){
			return;
		}
		View view = context.getLayoutInflater().inflate( R.layout.nfc_result, null);
		Alert.popup(context,view, "e通卡", Alert.OK, listener);
		
		final OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = v.getId();
				if(id==R.id.main_my_ecard){
					context.startActivity(new Intent(context,MyECardActivity.class));
				}else if(id==R.id.main_recharge){
					Intent intent = new Intent(context,RechargeECardActivity.class);
					intent.putExtra("cardId",result.getCardId());
					context.startActivity(intent);
				}else if(id==R.id.nfc_query){
					DataHolder.set(NfcResultActivity.class,result);
					context.startActivity(new Intent(context,NfcResultActivity.class));
				}
				
			}
		};
		
		view.findViewById(R.id.main_my_ecard).setOnClickListener(onClickListener);
		view.findViewById(R.id.main_recharge).setOnClickListener(onClickListener);
		view.findViewById(R.id.nfc_query).setOnClickListener(onClickListener);
		
		ViewUtil.setBinddataViewValues(result, view, R.layout.nfc_result);
		
		ViewUtil.setTextFieldValue(view, R.id.card_number, result.getCardId());
		ViewUtil.setTextFieldValue(view, R.id.id_left, result.getCash());
		
	}

}
