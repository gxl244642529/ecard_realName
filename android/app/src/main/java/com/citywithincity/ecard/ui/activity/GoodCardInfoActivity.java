package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.citywithincity.activities.FormActivity;
import com.citywithincity.auto.EventHandler;
import com.citywithincity.auto.EventHandler.EventHandlerId;
import com.citywithincity.auto.Form;
import com.citywithincity.auto.FormElement;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.PickCardModel;
import com.citywithincity.ecard.models.vos.LostCardDetailInfo;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.KeyboardUtil;
import com.citywithincity.widget.RadioGroup;

import java.util.Map;





@Form(layout=R.layout.activity_good_card_info,forms={
	@FormElement(id=R.id.id_phone,description="联系方式",name="phone"),
	@FormElement(id=R.id.id_sex,description="性别",name="sex"),
	@FormElement(id=R.id.id_detail,description="信息",name="message")
},defaultButton=R.id._title_right,beanClass=LostCardDetailInfo.class)
@EventHandler
@Observer
public class GoodCardInfoActivity extends FormActivity{
	
	
	private TextView message;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setTitle("信息登记");
		message = (TextView) findViewById(R.id.id_detail);
	}
	
	
	@EventHandlerId(id=R.id.id_help)
	public void onBtnHelp(){
		Alert.alert(GoodCardInfoActivity.this, "帮助", getResources().getString(R.string.lost_card_info));

	}
	
	
	@Override
	protected void createForm() {
		RadioGroup daGroup = (RadioGroup) findViewById(R.id.id_sex);
		daGroup.setValue(0, 0);
		daGroup.setValue(1, 1);
		daGroup.setValue(2, 2);
	}

	@Override
	protected void onSubmit(Map<String, Object> formValues) {
		if (TextUtils.isEmpty((String)formValues.get("phone"))) {
			Alert.showShortToast("手机号码能让拾卡人第一时间找到您,请输入您的手机号码");
			return;
		}
		String strMessage = (String)formValues.get("message");
		if(strMessage.length() > 200){
			Alert.showShortToast("信息字数太多了");
			return;
		}
		if(TextUtils.isEmpty(strMessage)){
			Alert.showShortToast("亲，说点什么吧");
			return;
		}
		ModelHelper.getModel(PickCardModel.class).saveLostCardInfo(formValues);
	}


	@NotificationMethod(PickCardModel.lost_save)
	public void onSaveSuccess(Object value){
		KeyboardUtil.hideSoftKeyboard(GoodCardInfoActivity.this,message);
		Alert.showShortToast("保存成功");
		setResult(Activity.RESULT_OK);
		finish();
	}



}
