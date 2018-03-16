package com.citywithincity.ecard.nfc;

import com.jzoom.nfc.vos.CardTradeVo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author randy
 *
 */


public class NfcResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3779611124992114262L;


	/**
	 * 交易
	 * @author randy
	 *
	 */

//	public static class CardTradeVo implements Serializable{
//
//		/**
//		 *
//		 */
//		private static final long serialVersionUID = -5116026592278369465L;
//
//
//		public static final int TYPE_ADD = 1;
//		public static final int TYPE_SUB = 0;
//
//
//		public String getTypeStr(){
//			return getType()==1 ? "充值" : "支付";
//		}
//
//
//
//		public int getType() {
//			return type;
//		}
//		public void setType(int type) {
//			this.type = type;
//		}
//
//		public String getTime() {
//			return time;
//		}
//
//		public void setTime(String time) {
//			this.time = time;
//		}
//
//		public String getCash() {
//			return cash;
//		}
//
//		public void setCash(String cash) {
//			this.cash = cash;
//		}
//
//		private int type;
//		//交易时间
//		private String time;
//		//交易金额
//		private String cash;
//
//	}
//
	//卡号
	private String cardId;
	
	//余额
	private String cash;

	
	private List<CardTradeVo> list;


	public String getCardId() {
		return cardId;
	}


	public void setCardId(String cardId) {
		this.cardId = cardId;
	}


	public String getCash() {
		return cash;
	}


	public void setCash(String cash) {
		this.cash = cash;
	}


	public List<CardTradeVo> getList() {
		return list;
	}


	public void setList(List<CardTradeVo> list) {
		this.list = list;
	}


}
