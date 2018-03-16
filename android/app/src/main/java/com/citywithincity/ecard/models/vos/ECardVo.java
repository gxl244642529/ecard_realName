package com.citywithincity.ecard.models.vos;

import android.view.View;

import com.citywithincity.ecard.R;
import com.damai.helper.a.DataType;

import java.io.Serializable;



public class ECardVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3343174515725618879L;
	private String cardid;
	private String cardidExt;
	private String cardName;
	private String expireTime;
	private String cardType;
	private int cardFlag;

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    private String createDate;
	
	public String getCardid() {
		return cardid;
	}
	public void setCardid(String cardid) {
		this.cardid = cardid;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	public String getCardidExt() {
		return cardidExt;
	}
	public void setCardidExt(String cardidExt) {
		this.cardidExt = cardidExt;
	}

    @DataType(DataType.SRC)
    public int getCardIcon(){
        if(createDate==null){
            return R.drawable.card_noreal;
        }else{
            return R.drawable.isreal;
        }
    }
	@DataType(DataType.VISIBLE)
	public int getRealSign_Visible(){
		if(createDate==null){
			return View.GONE;
		}else{
			return View.VISIBLE;
		}
	}
	
	@DataType(DataType.VISIBLE)
	public int getCardidExt_Visible(){
		if(cardidExt==null){
			return View.GONE;
		}
		if(cardidExt.startsWith("000000000000000000")){
			return View.GONE;
		}
		if(cardidExt.isEmpty()){
			return View.GONE;
		}
		
		return View.VISIBLE;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}


	public void setCardIdExt(String cardIdExt) {
		this.cardidExt = cardIdExt;
	}

	public int getCardFlag() {
		return cardFlag;
	}

	public void setCardFlag(int cardFlag) {
		this.cardFlag = cardFlag;
	}
}
