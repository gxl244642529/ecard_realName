package com.citywithincity.ecard.recharge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.TianYu;
import com.citywithincity.ecard.recharge.models.RechargeOrderModel;
import com.citywithincity.ecard.recharge.models.vos.RechargeVo;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.ActionSheet;
import com.citywithincity.widget.ActionSheet.OnActionSheetListener;
import com.damai.auto.DMFragmentActivity;
import com.damai.helper.a.Event;
import com.damai.helper.a.InitData;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;

public class RechargeOrderDetailActivity extends DMFragmentActivity implements
		OnActionSheetListener {
	@Model
	private RechargeOrderModel model;
	@InitData
	private RechargeVo data;

	@Res
	private Button refund;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_recharge_order_detail);
		setTitle("订单详情");

		if(data.getStatus()==4){
			findViewById(R.id.statusBg).setVisibility(View.GONE);
		}
	}

	@Override
	public void onActionSheet(int index) {
		switch (index) {
		case 0:
			TianYu.startRecharge(getActivity(),data.getCmAcc(),  data.getCardId(), data.getTyId(),data.getFee() ,false);
			break;
		case 1:
			// 补登点
			startActivity(new Intent(this,RechargePointActivity.class));
			break;

		default:
			break;
		}
	}

	@Event(confirm = "确定要退款吗?")
	public void onRefund() {
		model.refund(data, refund);
	}

	@Event
	public void onRecharge() {
		ActionSheet actionSheet = new ActionSheet(getActivity(), new String[] {
				"手机NFC卟噔", "线下卟噔" });
		actionSheet.setTitle("卟噔方式选择");
		actionSheet.setOnActionSheetListener(this);
		actionSheet.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(requestCode ==TianYu.TIANYU_REQUEST_RECHARGE ){

			model.getStatus(data.getTyId());

		}
	}

	@JobSuccess(RechargeOrderModel.getStatus)
	public void onGetStatus(int status){

		if(status == RechargeVo.OrderStatus_Finish)
		{
            //补登成功

			finish();
		}

	}


	@JobSuccess(RechargeOrderModel.refund)
	public void onRefundSuccess(Boolean value) {
		if(value){
			Alert.alert(getActivity(), "温馨提示", "退款成功",new DialogListener(){

                @Override
                public void onDialogButton(int id) {
                    finish();
                }
            });

		}else{
            Alert.alert(getActivity(), "退款失败，请重试");
		}

	}


}
