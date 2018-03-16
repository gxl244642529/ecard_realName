package com.citywithincity.ecard.recharge.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.citywithincity.auto.ApiSuccess;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.TianYu;
import com.citywithincity.ecard.pay.OrderStatus;
import com.citywithincity.ecard.recharge.models.RechargeOrderModel;
import com.citywithincity.ecard.recharge.models.vos.RechargeOrderResult;
import com.citywithincity.ecard.recharge.models.vos.RechargeSuccessVo;
import com.citywithincity.ecard.recharge.models.vos.RechargeVo;
import com.citywithincity.ecard.utils.NfcUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.ViewUtil;
import com.damai.auto.DMFragmentActivity;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMError;
import com.damai.core.DMLib;
import com.damai.helper.a.Event;
import com.damai.helper.a.InitData;
import com.damai.helper.a.Model;
import com.damai.http.api.a.JobError;
import com.damai.http.api.a.JobSuccess;

public class RechargePaySuccessActivity extends DMFragmentActivity implements MessageUtil.IMessageListener, ApiListener {


    String orderId;
    int count = 0;
    @Model
    private RechargeOrderModel model;

    private View successView;
    private View loadingView;
    private View errorView;
	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_recharge_pay_success);
		setTitle("付款结果");
        successView = findViewById(R.id.success);
        loadingView = findViewById(R.id.loading);
        errorView = findViewById(R.id.error);

        errorView.setVisibility(View.GONE);
        successView.setVisibility(View.GONE);

        orderId = getIntent().getExtras().getString("orderId");

        getPayInfo();
	}

    private void getPayInfo() {
        model.getPayInfo(orderId,this);
        ++count;
    }


    private RechargeOrderResult result;

    /**
     *
     * @param result
     */
    public void onGetPayInfo(RechargeOrderResult result){
        this.result = result;
        ViewUtil.setTextFieldValue(successView,R.id.cardId,result.getCardId());
        if(result.getFee() >= 2000){
            result.setFee( result.getFee() / 100 );
        }
        ViewUtil.setTextFieldValue(successView,R.id.fee, String.format("¥ %.02f",result.getFee()));
        if(result.getStatus() == OrderStatus.OrderStatus_Submiting){
            if(count < 3){
                //继续加载
                MessageUtil.sendMessage(100,this);
            }else{
                //否则显示超时问题
                errorView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
            }

        }else if(result.getStatus() == OrderStatus.OrderStatus_NoPay){
            Alert.alert(this,"订单未支付，请确认是否支付成功");
        }else if(result.getStatus() == OrderStatus.OrderStatus_SubmitError){
            errorView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
        }else if(result.getStatus() == OrderStatus.OrderStatus_Success){
            successView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
        }else{
            Alert.alert(this,"订单状态错误");
        }
    }

    @Override
    public void onMessage(int msg) {
        getPayInfo();
    }


	@Event
	public void btn_nfc() {
		if (NfcUtil.isAvailable(getActivity())) {
			TianYu.startRecharge(getActivity(),
                    result.getCmAcc(),
                    result.getCardId(),
                    result.getTyId(), (int) (result.getFee() * 100),false);
		} else {
			Alert.showShortToast("本手机不支持nfc设备");
		}
	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(requestCode ==TianYu.TIANYU_REQUEST_RECHARGE ){

            model.getStatus(result.getTyId());

        }
    }

    @JobSuccess(RechargeOrderModel.getStatus)
    public void onGetStatus(int status){
        if(status == RechargeVo.OrderStatus_Finish)
        {
           finish();
        }
    }

    @Event
    public void tryAgain(){

    }

    @Event
    public void refund(){

    }

    @Event
	public void btn_no_nfc() {
		startActivity(new Intent(getActivity(),RechargePointActivity.class));
	}


    @Override
    public boolean onApiMessage(ApiJob apiJob) {



        return false;
    }

    @Override
    public boolean onJobError(ApiJob apiJob) {

        errorView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);

        return true;
    }

    @Override
    public void onJobSuccess(ApiJob apiJob) {
        RechargeOrderResult result = apiJob.getData();
        onGetPayInfo(result);
    }
}
