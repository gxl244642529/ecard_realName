package com.citywithincity.ecard.recharge.models.vos;

import android.view.View;

import com.citywithincity.ecard.R;
import com.damai.helper.a.DataType;
import com.damai.interfaces.IStateData;

/**
 * 订单
 *
 * @author renxueliang
 *
 */
public class RechargeVo implements IStateData {
	// 没有支付
	public static final int OrderStatus_NoPay = 0;
	// 交易成功
	public static final int OrderStatus_Success = 1;
	// 已经退款
	public static final int OrderStatus_Refund = 2;
	/**
	 * 状态完成
	 */
	public static final int OrderStatus_Finish = 3;

	public static final int OrderStatus_Invoce = 4;

	@Override
	public int getState() {
		switch (status) {
		case OrderStatus_NoPay:
			return 0;
		case OrderStatus_Success:
			return 0;
		case OrderStatus_Refund:
			return 2;
		default:
        {
            if(isRefundFail()){
                return 0;
            }else{
                return 1;
            }
        }
		}
	}

	/**
	 *
	 *
	 /* if(status==10){ return RechargeModel.OrderStatus_Finish; }
	 * if(status==99 || status == 88){ return OrderStatus.OrderStatus_Refund; }
	 * return OrderStatus.OrderStatus_Success;
	 */

	private int fee;
	private String cardId;
	private String tyId;
	private String payTime;
	private int status;

	private String cmAcc;

	@DataType(DataType.VISIBLE)
	public int getInvoce_Visible() {
		return status == OrderStatus_Finish ? View.VISIBLE : View.GONE;
	}


	@DataType(DataType.VISIBLE)
	public int getRefund_Visible() {
		return isUnfinished() ? View.VISIBLE : View.GONE;
	}


    private boolean isRefundFail(){
        return status > 10;
    }

    private boolean isUnfinished(){
        return isRefundFail() || status == OrderStatus_Success;
    }

    @DataType(DataType.VISIBLE)
    public int getRecharge_Visible() {
        return status == OrderStatus_Success ? View.VISIBLE : View.GONE;
    }

	@DataType(DataType.BG_RES)
	public int getStatusBg() {
		switch (status) {
			case OrderStatus_Success:
				return R.drawable.recharge_status1;
			case OrderStatus_Refund:
				return R.drawable.recharge_status3;
			default:
                if(isRefundFail()){
                    return R.drawable.recharge_status3;
                }
				return R.drawable.recharge_status2;
		}
	}


	public String getStatusStr() {
		switch (status) {
		case OrderStatus_NoPay:
			return "未付款";
		case OrderStatus_Success:
			return "未完成";
		case OrderStatus_Refund:
			return "已退款";
			case OrderStatus_Invoce:
				return "已领发票";
		default:
            if(isRefundFail()){
                return "未完成";
            }
			return "已完成";
		}
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public String getTyId() {
		return tyId;
	}

	public void setTyId(String tyId) {
		this.tyId = tyId;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

    public String getCmAcc() {
        return cmAcc;
    }

    public void setCmAcc(String cmAcc) {
        this.cmAcc = cmAcc;
    }
}
