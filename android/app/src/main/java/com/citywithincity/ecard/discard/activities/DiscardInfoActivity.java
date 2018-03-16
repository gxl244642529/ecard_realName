package com.citywithincity.ecard.discard.activities;

import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.discard.vos.BookInfo;
import com.citywithincity.ecard.utils.ValidateUtil;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.damai.auto.DMFragmentActivity;
import com.damai.helper.a.InitData;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.Form;
import com.damai.widget.FormRadioGroup;
import com.damai.widget.OnSubmitListener;
import com.damai.widget.SubmitButton;

import java.util.Map;
/**
 * 申请优惠卡信息页面
 * @author renxueliang
 *
 */
public class DiscardInfoActivity extends DMFragmentActivity implements DialogListener, OnSubmitListener {
	
	
	private SubmitButton button;
	
	@InitData
	private BookInfo info;

    /**
     *
     * @param savedInstanceState
     */
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_discard_info);
		setTitle("填写申请信息");
		button = (SubmitButton) findViewById(R.id.submit);
		button.setOnSubmitListener(this);


	}
	
	/**
	 * 提交成功之后
	 */
	@JobSuccess("book/submitInfo")
	public void onSubmitSuccess(Object value){
		Alert.alert(this, "温馨提示","业务受理成功，请于2个工作日后登录完成办理",this);
	}

	@Override
	public void onDialogButton(int id) {
		finish();
	}

	@Override
	public boolean shouldSubmit(Form formView, Map<String, Object> data) {

        String name = (String) data.get("name");
        if(!ValidateUtil.isChinese(name)){
            Alert.alert(this,"请输入中文姓名");
            return false;
        }

		data.put("status", info.getStatus());
		data.put("type", info.getType());
		data.put("savType", info.getSavType());
		data.put("cardId", info.getCardId());
		data.put("custNo", info.getCustNo());
		data.put("idCardType", 0);
		return true;
	}



}
