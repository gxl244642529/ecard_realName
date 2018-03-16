package com.citywithincity.ecard.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.PickCardModel;
import com.citywithincity.ecard.utils.ValidateUtil;
import com.citywithincity.ecard.widget.DateTimePicker;
import com.citywithincity.ecard.widget.DateTimePicker.IDateTimePickerListener;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;

public class LostCardReturnFragment extends Fragment implements OnClickListener, IRequestResult<Object>, IDateTimePickerListener  {
	
	private TextView phone;
	private TextView time;
	private TextView card_id;
	private String cardID;
	private String strTime;
	private DateTimePicker picker;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_return_card, null);
		view.findViewById(R.id._id_ok).setOnClickListener(this);
		time = (TextView)view.findViewById(R.id.pick_card_time);
		phone = (TextView)view.findViewById(R.id.pick_card_phone);
		card_id=(TextView)view.findViewById(R.id.card_number);
		card_id.setText(cardID);
		
		
		time.setOnClickListener(this);
		
		
		view.findViewById(R.id.id_help).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Alert.alert(getActivity(), "帮助", getResources().getString(R.string.pick_card_help));
				
			}
		});
		
		return view;
	}
	
	public void setCardID(String cardID){
		
		this.cardID = cardID;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id._id_ok){
			String phoneNumber = phone.getText().toString().trim();
			
			if(TextUtils.isEmpty(phoneNumber)){
				Alert.showShortToast("请输入联系号码，以便失主可以第一时间联系您");
				return;
			}
			
			if(!ValidateUtil.isMobile(phoneNumber)){
				Alert.showShortToast("请输入正确的手机号码");
				return;
			}
			
			if(TextUtils.isEmpty(strTime)){
				Alert.showShortToast("请输入拾卡时间，以便失主可以进一步确认丢卡时间");
				return;
			}
			
			ModelHelper.getModel(PickCardModel.class).pulishPickCard(cardID,phoneNumber,null,strTime,JsonTaskManager.getInstance().getPushID());
		//	ECardJsonManager.getInstance().pulishPickCard(cardID,phoneNumber,null,strTime,this);
		}else{ 
			
			if(picker!=null){
				picker.destroy();
				picker = null;
			}
			picker = new DateTimePicker(getActivity(), "请选择拾卡时间",this);
		}
		
	}

	@Override
	public void onRequestSuccess(Object result) {
		Alert.showShortToast("发布成功");
		//发布成功
		Alert.cancelWait();
		
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		Alert.showShortToast(errMsg);
		Alert.cancelWait();
	}

	@Override
	public void onDateTimeSelected(DateTimePicker picker) {
		strTime = picker.toString();
		time.setText(strTime);
	}

	@Override
	public void onDestroy() {
		if(picker!=null){
			picker.destroy();
			picker = null;
		}
		Alert.cancelWait();
		super.onDestroy();
	}


}
