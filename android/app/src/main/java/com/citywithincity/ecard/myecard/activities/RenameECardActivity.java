package com.citywithincity.ecard.myecard.activities;

import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.vos.ECardVo;
import com.damai.auto.DMActivity;
import com.damai.helper.a.InitData;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.Form;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;

import java.util.Map;

public class RenameECardActivity extends DMActivity implements OnSubmitListener {
	@InitData
	private ECardVo data;
	
	@Res
	private SubmitButton rename;
	
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_myecard_rename);
		setTitle("修改备注");
		rename.setOnSubmitListener(this);
	}

	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {
		data.put("cardId", this.data.getCardid());
		return true;
	}
	
	@JobSuccess("ecard/update")
	public void onRenameSuccess(Object value){
		data.setCardName((String) rename.getJob().getParams().get("remark"));
		setResult(RESULT_OK);
		
		finish();
	}
	
	
}
