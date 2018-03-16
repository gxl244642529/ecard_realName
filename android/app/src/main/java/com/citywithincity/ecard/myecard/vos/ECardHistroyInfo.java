package com.citywithincity.ecard.myecard.vos;

import com.citywithincity.ecard.R;
import com.damai.helper.a.DataType;

/**
 * 历史记录
 * @author randy
 *
 */
public class ECardHistroyInfo{
	//join表示是其他表的字段
	//@Join(table="APP_JYLX", field = "LX_MC", foreignField = "SN", thisField = "TH_TYPE")
	private int type;

	private String time;
	private double left;
	private double value;
	private String shpName;


	@DataType(DataType.BG_RES)
	public int getTypeStr_BgRes(){
		if (type==1) {
			return(R.drawable.ecard_consume_type_bg);
		} else {
			return(R.drawable.ecard_item_type_bg);
		}
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public double getLeft() {
		return left;
	}


	public void setLeft(double left) {
		this.left = left;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}


	public String getShpName() {
		return shpName;
	}


	public void setShpName(String shpName) {
		this.shpName = shpName;
	}


	public String getTypeStr(){
		final int type = this.type;
		switch (type) {
			case 0:
				return "消费";
			case 1:
				return "充值";
			default:
				return "其他";
		}
	}

	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}



}
