package com.citywithincity.ecard.recharge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.enums.Actions;
import com.citywithincity.ecard.recharge.models.RechargeOrderModel;
import com.citywithincity.ecard.recharge.models.RechargePayActionHandler;
import com.citywithincity.ecard.widget.MyAutoCompleteTextView;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMFragmentActivity;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMAccount;
import com.damai.helper.a.Event;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.interfaces.PopupListener;
import com.damai.widget.Popups;

import org.json.JSONException;
import org.json.JSONObject;

public class RechargeECardActivity extends DMFragmentActivity implements OnClickListener, ApiListener{

	@Res
	private MyAutoCompleteTextView cardId;
	
	private RechargePayActionHandler actionHandler;
	
	@Model
	private RechargeOrderModel model;
	
	@Res
	private Button radio_precharge_20;
	@Res
	private Button radio_precharge_50;
	@Res
	private Button radio_precharge_100;
	@Res
	private Button radio_precharge_200;
	
	private Button[] buttons;
	
	private boolean lastEnable = true;
	
	private void enableButtons(boolean enable){
		if(lastEnable!=enable){
			for (Button button : buttons) {
				button.setEnabled(enable);
			}
			lastEnable = enable;
		}
	}

	
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_recharge_ecard);
		setTitle("卟噔充值");
	
		cardId.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				enableButtons(s.length()>0);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		actionHandler = new RechargePayActionHandler(getActivity());
		
		radio_precharge_20.setTag(20);
		radio_precharge_50.setTag(50);
		radio_precharge_100.setTag(100);
		radio_precharge_200.setTag(200);
		
		buttons = new Button[]{radio_precharge_20,
				radio_precharge_50,
				radio_precharge_100,
				radio_precharge_200};
		
		for (Button button : buttons) {
			button.setOnClickListener(this);
		}
		
		model.load(cardId);


		Intent intent = getIntent();
		if(intent!=null){
			Bundle bundle = intent.getExtras();
			if(bundle!=null){
				String id = bundle.getString("cardId");
				if(id!=null){
					cardId.setText(id);
                    enableButtons(id.length()>0);
				}
			}
		}
	}
	
	
	@Override
	public void onJobSuccess(ApiJob job) {
		model.save(cardId.getText().toString());
		enableButtons(true);
		//这里进行预充值
		JSONObject jsonObject = job.getData();
		try {
			actionHandler.startup(jsonObject.getString("order_id"), jsonObject.getInt("fee"));
			startActivity(new Intent(getPackageName()+ ".action.PAYCASHIER"));
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean onJobError(ApiJob job) {
		enableButtons(true);
        Alert.cancelWait();
        return false;
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		enableButtons(true);
        Alert.cancelWait();
		return false;
	}
	
	@Override
	public void onClick(View v) {
        if(!DMAccount.isLogin()){
            DMAccount.callLoginActivity(this,null);
            return;
        }
		String id = cardId.getText().toString();
		enableButtons(false);
		model.submit((Integer)v.getTag(), id, this);
		
	}
	
	@Event
	public void onRechargePoint(){
		startActivity(new Intent(getActivity(),RechargePointActivity.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_CANCELED && requestCode == Actions.REQUEST_CODE_LOGIN){
			enableButtons(true);
		}
	}

	@Event
	public void onViewQuestion(){
		///Popups.bottomPopup(this, contentView);
		View view = getLayoutInflater().inflate(R.layout.view_recharge_budeng_introduce_alert,
				null);
		Popups.createBottomPopup(getActivity(), view,new PopupListener<View>() {

			@Override
			public void onPopup(int id, View contentView) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Event(requreLogin = true)
	public void onMyRecharge(){
		startActivity(new Intent(getActivity(),RechargeOrderListActivity.class));
	}

	@Event
	public void onOtherRecharge(){
		startActivity(new Intent(getActivity(),RechargeOtherActivity.class));
	}
	
}
