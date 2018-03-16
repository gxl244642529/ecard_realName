package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.citywithincity.activities.BaseFragmentActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.PickCardModel;
import com.citywithincity.ecard.models.vos.MyLostCardDetailInfo;
import com.citywithincity.ecard.ui.fragment.LostCardInfoFragment;
import com.citywithincity.ecard.ui.fragment.LostCardReturnFragment;
import com.citywithincity.ecard.utils.SystemUtil;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;

public class GoodCardReturnActivity extends BaseFragmentActivity implements OnClickListener,IRequestResult<MyLostCardDetailInfo>, 
 DialogListener {
	TextView cardNumber;
	LostCardInfoFragment lostCardInfoFragment;
	LostCardReturnFragment lostCardReturnFragment;
	String cardID;
	
	
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_good_card_return);
		setTitle("拾卡招领");
		findViewById(R.id._id_ok).setOnClickListener(this);
		cardNumber = (TextView)findViewById(R.id.card_number);
	}
	
	
	protected void replaceFragment(Fragment fragment){
		SystemUtil.replaceFragment(this, R.id._container, fragment,false,false);
	}
	protected void replaceFragment(Fragment fragment,boolean animation){
		SystemUtil.replaceFragment(this, R.id._container, fragment,animation,false);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id._id_ok){
			String card_number = cardNumber.getText().toString().trim();
			if(TextUtils.isEmpty(card_number)){
				Alert.showShortToast(R.string.input_pick_card_number);
				return;
			}
			Alert.wait(this,R.string.waiting_loading);
			cardID = card_number;
			
			ModelHelper.getModel(PickCardModel.class)
			.queryLostCard(cardID,JsonTaskManager.getInstance().getPushID()).setListener(this);
		}
	}

	@Override
	public void onRequestSuccess(MyLostCardDetailInfo result) {
		Alert.cancelWait();
		switch (result.ret) {
		case 0:
			//找到了
			if(lostCardInfoFragment==null){
				lostCardInfoFragment = new LostCardInfoFragment();
				lostCardInfoFragment.setData(result.info);
				replaceFragment(lostCardInfoFragment,true);
			}else{
				lostCardInfoFragment.setData(result.info);
			}
			break;
		case 1://不存在 
			Alert.showShortToast("此卡不存在，请确认是否输入正确");
			break;
		case 2:
			//没有找到
			if(lostCardInfoFragment==null){
				lostCardReturnFragment = new LostCardReturnFragment();
				lostCardReturnFragment.setCardID(cardID);
				replaceFragment(lostCardReturnFragment,true);
			}else{
				lostCardReturnFragment.setCardID(cardID);
			}
			
			
			break;
		case 3:
			//此卡是自己的，并且已经挂失
			Alert.confirm(this, "此卡是本人的,并且已经登记找回，是否撤销找回?",this);
			break;
		case 4:
			//此卡是自己的，并且没有挂失
			Alert.alert(this, "本人的卡", "此卡是本人的");
			break;
		default:
			break;
		}
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		Alert.cancelWait();
		Alert.showShortToast(errMsg);
	}

	@Override
	protected void onDestroy() {
		Alert.cancelWait();
		super.onDestroy();
	}



	@Override
	public void onDialogButton(int id) {
		// TODO Auto-generated method stub
		if(id==R.id._id_ok){
			Alert.wait(this, R.string.waiting_revocation);
			ModelHelper.getModel(PickCardModel.class)
			.lostCardRevocation().setListener(new IRequestResult<Object>() {

				@Override
				public void onRequestSuccess(Object result) {
					Alert.cancelWait();
					Alert.showShortToast("撤销找回成功...");
				}

				@Override
				public void onRequestError(String errMsg, boolean isNetworkError) {
					Alert.cancelWait();
				}
			});
			
		}
	}

	

}
