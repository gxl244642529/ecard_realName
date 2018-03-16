package com.citywithincity.ecard.recharge.models.vos;

/**
 * 充值成功了
 * @author renxueliang
 *
 */
public class RechargeSuccessVo {
	private String tyId;
	private int fee;
	private String cardId;
	private String cmAcc;


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

    public String getCmAcc() {
        return cmAcc;
    }

    public void setCmAcc(String cmAcc) {
        this.cmAcc = cmAcc;
    }
}
