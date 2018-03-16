package com.citywithincity.ecard.recharge.activities;

import android.app.Activity;
import android.content.IntentFilter;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.TianYu;
import com.citywithincity.ecard.nfc.NfcResult;
import com.citywithincity.ecard.nfc.CardReader;
import com.citywithincity.ecard.nfc.ECardNfcModel;
import com.citywithincity.ecard.recharge.models.RechargeOrderModel;
import com.citywithincity.ecard.recharge.models.vos.RechargeVo;
import com.citywithincity.utils.Alert;
import com.damai.auto.DMFragmentActivity;
import com.damai.helper.a.Model;
import com.damai.http.api.a.JobSuccess;
import com.jzoom.nfc.NfcException;

import java.io.IOException;

public class RechargeOtherActivity extends DMFragmentActivity implements  com.jzoom.nfc.NfcListener{

	@Model
	private RechargeOrderModel orderModel;

	private ECardNfcModel nfcModel;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_recharge_other);
		setTitle("为他人卟噔");

		nfcModel = new ECardNfcModel(this);
		nfcModel.setListener(this);
	//	NfcUtil.createNfcModel(LifeManager.getCurrent(),  INfcReader.MODE_RECHARGE, this);
	}
	
	@JobSuccess("recharge/otherList")
	public void onGetList(RechargeVo data) {
		if(data==null || data.getFee()==0){
			Alert.showShortToast( "没有找到订单" ) ;
			return ;
		}
		
		startRecharge(this,data);
	}

	private static void startRecharge(Activity context, RechargeVo data) {

		TianYu.startRecharge(context, data.getCmAcc(), data.getCardId(), data.getTyId(), data.getFee(),true);
	}


	@Override
	public void onNfcEvent(TagTechnology tag) {
		//orderModel.otherList( result.getFile05(), resul.getFile15(), result.getRandom());

		try {
			CardReader.ChargeInfo info = nfcModel.getOtherRechargeInfo();
			orderModel.otherList( info.file05, info.file15, info.random);
		} catch (IOException e) {
			Alert.showShortToast("请重新贴卡");
		} catch (NfcException e) {
			Alert.showShortToast("请重新贴卡");
		}
	}


    public void onNecReaded(NfcResult result) {
        System.out.print(result);
    }
}
