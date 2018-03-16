package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.citywithincity.activities.BaseActivity;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.MyECardModel;
import com.citywithincity.ecard.models.vos.ECardInfo;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ViewUtil;

public class MemberInfoActivity extends BaseActivity implements OnClickListener{

	
	public static void call(Activity activity,ECardInfo eCardInfo){
		Intent intent = new Intent(activity,MemberInfoActivity.class);
		ModelHelper.setListData(eCardInfo);
		activity.startActivity(intent);
	}
	
	
	private EditText name;
	private EditText idCode;
	private EditText phone;
	private RadioGroup sex;
	private String cardId;
	private EditText address;
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_member_info);
		setTitle("绑会员卡");
		
		
		findViewById(R.id._id_ok).setOnClickListener(this);
		name = (EditText)findViewById(R.id.id_name);
		idCode = (EditText)findViewById(R.id.id_idcode);
		phone = (EditText)findViewById(R.id.id_phone);
		sex = (RadioGroup)findViewById(R.id.id_sex);
		address = (EditText)findViewById(R.id.id_address);
		
		
		ECardInfo eCardInfo = ModelHelper.getListData();
		if(eCardInfo!=null){
			cardId = eCardInfo.id;
			ViewUtil.setTextFieldValue(this, R.id.id_card, cardId);
			if(!TextUtils.isEmpty(eCardInfo.cmName)){
				name.setText(eCardInfo.cmName);
				idCode.setText(eCardInfo.cmIdCode);
				phone.setText(eCardInfo.cmPhone);
				if(eCardInfo.cmSex==1){
					sex.check(R.id.id_male);
				}else if(eCardInfo.cmSex==2){
					sex.check( R.id.id_female);
				}
				
				address.setText(eCardInfo.cmAddress);
			}
			//激活了
			if(eCardInfo.state==1){
				
			}
		}
		
		
	}

	@Override
	public void onClick(View v) {
		int id=  v.getId();
		if(id==R.id._id_ok){
			if( this.sex.getCheckedRadioButtonId()==0){
				Alert.showShortToast("请选择性别");
				return;
			}
			int sex = this.sex.getCheckedRadioButtonId()==R.id.id_male ? 1 :2;
			String name = this.name.getText().toString().trim();
			if(TextUtils.isEmpty(name)){
				Alert.showShortToast(this.name.getHint());
				return;
			}
			String phone = this.phone.getText().toString().trim();
			if(TextUtils.isEmpty(phone)){
				Alert.showShortToast(this.name.getHint());
				return;
			}
			String idCode = this.idCode.getText().toString().trim();
			if(TextUtils.isEmpty(idCode)){
				Alert.showShortToast(this.name.getHint());
				return;
			}
			
			String address = this.address.getText().toString().trim();
			if(TextUtils.isEmpty(address)){
				Alert.showShortToast(this.address.getHint());
				return;
			}
			
			Alert.wait(this, "正在绑定...");
			ModelHelper.getModel(MyECardModel.class)
			.updateMemberInfo(cardId,name,sex,phone,idCode,address);
		}
	}


}
