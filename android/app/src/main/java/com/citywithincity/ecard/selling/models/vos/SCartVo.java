package com.citywithincity.ecard.selling.models.vos;

import com.citywithincity.models.FileObject;

import java.io.Serializable;

public class SCartVo extends FileObject implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String imgPathCard;//图片
	public String thumb;//小图
	public int idS;
	public String nameCard;//卡名
	public float unitPrice;//单价
	public int countS = 1;//数量
	public int preChargeS;//预充值
	public String preChargeSByShow;//用来显示金额
	public float totalPriceCart;//总价
	public boolean isSeleted;//购物车是否选中
	public String cardTye;//卡片类型

	//diy
	public int bgIdS;//背面卡面ID
	public String front;//正面
	
	public void calulate() {
		totalPriceCart = (preChargeS + unitPrice) * countS; 
	}
	
}
