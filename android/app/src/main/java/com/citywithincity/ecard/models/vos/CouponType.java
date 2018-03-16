package com.citywithincity.ecard.models.vos;
public enum CouponType{
	CouponType_Null,//无效（0）
	CouponType_Limit,//限量，刷二维码使用
	CouponType_Qr,//刷二维码使用
	CouponType_Show,//出示即可使用
	CouponType_ECard,//刷e通卡有效
}
