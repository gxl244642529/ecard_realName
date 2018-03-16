package com.citywithincity.ecard.activities;

import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.damai.auto.DMActivity;
import com.damai.helper.a.InitData;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;
import com.damai.widget.vos.AddressVo;

import java.util.Map;

public class AddressEditActivity extends DMActivity implements OnSubmitListener {
	
	@Res
	private SubmitButton _id_ok;
	
	@InitData
	private AddressVo addressVo;

	
	
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_address_edit);
        setTitle("编辑地址");
		_id_ok.setOnSubmitListener(this);
	}


    @JobSuccess("address/save")
    public void onSaveOk(Object value){
        finish();
	}


	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {
		if(addressVo!=null){
			data.put("tyId", addressVo.getTyId());
		}
		return true;
	}

}
