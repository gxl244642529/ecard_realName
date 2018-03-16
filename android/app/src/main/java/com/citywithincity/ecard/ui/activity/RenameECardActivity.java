package com.citywithincity.ecard.ui.activity;

import android.os.Bundle;

import com.citywithincity.activities.FormActivity;
import com.citywithincity.auto.Form;
import com.citywithincity.auto.FormElement;
import com.citywithincity.auto.NotificationList;
import com.citywithincity.auto.NotificationList.Notification;
import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.MyECardModel;
import com.citywithincity.ecard.models.vos.ECardInfo;
import com.citywithincity.mvc.ModelHelper;

import java.util.Map;

@Observer
@NotificationList(@Notification(name=MyECardModel.ECARD_RENAME,type=String.class,description="当备注修改成功时候"))
@Form(layout = R.layout.activity_rename_ecard, forms = { @FormElement(id = R.id.my_ecard_remark, name = "name", description = "卡备注", validate = FormElement.REQUIRED, clazz = String.class) }, defaultButton = R.id.rename)
public class RenameECardActivity extends FormActivity {

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		super.onSetContent(savedInstanceState);
		setTitle("修改备注");
	}
	
	

	@Override
	protected void onSubmit(Map<String, Object> formValues) {
		newName = (String) formValues.get("name");
		ModelHelper.getModel(MyECardModel.class).renameCard(
				((ECardInfo) getEditValue()).id,newName );
	}
	
	private String newName;

	
	@NotificationMethod(MyECardModel.ECARD_RENAME)
	public void onRenameSuccess(Object result){
		Notifier.notifyObservers(MyECardModel.NAME_HAS_CHANGED, newName);
		finish();
	}
	
}
