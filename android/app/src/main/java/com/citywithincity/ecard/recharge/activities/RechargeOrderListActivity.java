package com.citywithincity.ecard.recharge.activities;

import android.content.Intent;
import android.os.Bundle;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.TianYu;
import com.citywithincity.ecard.recharge.models.RechargeOrderModel;
import com.citywithincity.ecard.recharge.models.vos.RechargeVo;
import com.citywithincity.ecard.utils.NfcUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.widget.ActionSheet;
import com.citywithincity.widget.ActionSheet.OnActionSheetListener;
import com.damai.auto.DMFragmentActivity;
import com.damai.auto.LifeManager;
import com.damai.helper.DataHolder;
import com.damai.helper.a.ItemEvent;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.http.api.a.JobSuccess;
import com.damai.widget.TabListView;
public class RechargeOrderListActivity extends DMFragmentActivity implements
		OnActionSheetListener {

	@Model
	private RechargeOrderModel model;

	@Res
	private TabListView<RechargeVo> _tab_container;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_recharge_order_list);
		setTitle("我的订单");
	}

	@ItemEvent(confrim = "确定要退款吗?")
	public void onItemRefund(RechargeVo data) {
		model.refund(data, null);
	}
	
	@ItemEvent(confrim = "请到易通卡营业厅领取纸质发票。点击该按钮之前请确认是否已经领取到纸质发票？确认后该订单不可再次领取发票哦?")
	public void onItemInvoce(RechargeVo data) {
		model.invoce(data, null);
	}
	
	@ItemEvent()
	public void onItem_container(RechargeVo data) {
		DataHolder.set(RechargeOrderDetailActivity.class,data);
		startActivity(new Intent(getActivity(),RechargeOrderDetailActivity.class));
	}

    private boolean isRefresh;

    @Override
    protected void onResume() {
        super.onResume();
        if(isRefresh){
            _tab_container.refreshWithState();
            isRefresh = false;
        }
    }

    @JobSuccess(RechargeOrderModel.refund)
	public void onRefundSuccess(Boolean  value) {
		if(LifeManager.getCurrent()==this){
			if(value){
				Alert.alert(getActivity(), "退款成功");
			}else{
				Alert.alert(getActivity(), "退款失败，请重试");
			}
            _tab_container.refreshWithState();
		}else{
            isRefresh = true;
        }
	}
	
	
	@JobSuccess(RechargeOrderModel.invoce)
	public void onInvoceSuccess(Object  value) {
		Alert.alert(getActivity(), "领发票成功");
		_tab_container.refreshWithState();
	}

	@ItemEvent
	public void onItemRecharge(RechargeVo data) {
		rechargeVo = data;
		ActionSheet actionSheet = new ActionSheet(getActivity(), new String[] {
				"手机NFC卟噔", "线下卟噔" });
		actionSheet.setTitle("卟噔方式选择");
		actionSheet.setOnActionSheetListener(this);
		actionSheet.show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == TianYu.TIANYU_REQUEST_RECHARGE){
			//找到,是否已经补登成功
			if(rechargeVo!=null){
				//查询订单状态，如果已经成功，则重新加载
				model.getStatus(rechargeVo.getTyId());
			}
		}
	}

	private RechargeVo rechargeVo;

	@Override
	public void onActionSheet(int index) {
		switch (index) {
		case 0:
			if (NfcUtil.isAvailable(getActivity())) {
				TianYu.startRecharge(getActivity(),rechargeVo.getCmAcc(),rechargeVo.getCardId(), rechargeVo.getTyId(), rechargeVo.getFee(),false);
			} else {
				Alert.showShortToast("本手机不支持nfc设备");
			}
			break;
		case 1:
			// 补登点
			startActivity(new Intent(this,RechargePointActivity.class));
			break;

		default:
			break;
		}
	}

	@JobSuccess(RechargeOrderModel.getStatus)
	public void onGetStatus(int status){
		
		if(status == RechargeVo.OrderStatus_Finish)
		{
			_tab_container.refreshWithState();
		}
		
	}

}
