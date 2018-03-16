package com.citywithincity.ecard.recharge.models.vos;

/**
 * Created by renxueliang on 17/4/4.
 */

public class RechargeOrderResult {
    private String id;
    private double fee;
    private int status;
    private String cardId;
    private String userError;
    private String cmAcc;
    private String tyId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getFee() {
        return fee;
    }
    public void setFee(double fee) {
        this.fee = fee;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getUserError() {
        return userError;
    }
    public void setUserError(String userError) {
        this.userError = userError;
    }
    public String getCardId() {
        return cardId;
    }
    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
    public String getCmAcc() {
        return cmAcc;
    }
    public void setCmAcc(String cmAcc) {
        this.cmAcc = cmAcc;
    }
    public String getTyId() {
        return tyId;
    }
    public void setTyId(String tyId) {
        this.tyId = tyId;
    }
}
