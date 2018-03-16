//
//  SellingModel.h
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CommonMacro.h"
#import "JsonTaskManager.h"
#import "FileArrayJsonTask.h"
#import "MenuView.h"

#define CARD_WIDTH 1016
#define CARD_HEIGHT 638

typedef enum _CardType{
    CardType_Normal,
    CardType_Diy,
    CardType_Student,
    CardType_Old
}CardType;

typedef enum _OrderState{
    //没有付款
    ORDER_NOPAY = 0,
    //正在支付
    ORDER_PAYING = 1,
    //已经支付
    ORDER_PAYED = 2,
    //退款中
    ORDER_RETURN = 6,
    //退款成功
    ORDER_RETURN_FINISH,
    //退款失败
    ORDER_RETURN_FAIL,
    //关闭
    ORDER_CLOSED,
    //已经发货
    ORDER_DELIVERED=12,
    //已经收获
    ORDER_RECEIVED,
    //退货中
    ORDER_BACK,
    //已经退货
    ORDER_BACKED,
    //退货失败
    ORDER_BACK_FAIL

}OrderState;


@interface STypeVo : MenuData<IJsonValueObject>



@end

/**
 *  购物车
 */
@interface CartVo : FileObject<IJsonValueObject>
@property (nonatomic) NSInteger count;
@property (nonatomic) NSInteger recharge;
@property (nonatomic) NSInteger ID;
@property (nonatomic,retain) NSString* thumb;
@property (nonatomic,retain) NSString* cartID;
@property (nonatomic) int imageID;
@property (nonatomic) int store;
/**
 *  选中
 */
@property (nonatomic) BOOL selected;

/**
 * 标题
 */
@property (nonatomic,retain) NSString* title;
/**
 * 价格
 */
@property (nonatomic) double price;


-(NSString*)priceString;
-(NSString*)rechargeString;

-(float)totalPrice;
-(NSString*)totalPriceString;
-(NSString*)countString;




@end

////////////////////////////////////////////////////////

/** *  卡面
 */
@interface SDiyPagesVo : NSObject<IJsonValueObject>

@property(nonatomic) NSInteger ID;
/**
 * 正面图片
 */
@property (nonatomic,retain) NSString* img;

@property (nonatomic,retain) NSString* title;
/**
 * 正面小图
 */
@property (nonatomic,retain) NSString* thumb;
/**
 * 价格
 */
@property (nonatomic) double price;

@property (nonatomic) NSInteger store;
-(NSString*)stringPrice;


@end
////////////////////////////////////////////////////////
/** *  售卡详情
 */
@interface SCardDetailVo : NSObject<IJsonValueObject>

/**
 * 正面图片
 */
@property (nonatomic,retain) NSString* img;
/**
 * 反面图片
 */
@property (nonatomic,retain) NSString* imgF;
/**
 * null
 */
@property (nonatomic) int hasInfo;
/**
 * 销售数量
 */
@property (nonatomic) int saled;
/**
 * 库存
 */
@property (nonatomic) int store;
/**
 * 购卡须知
 */
@property (nonatomic,retain) NSString* tip;


@end
////////////////////////////////////////////////////////
/**
 *  vo
 */
/** *  获取售卡列表
 */
@interface SCardListVo : CartVo<IJsonValueObject>



@end

@interface SBookListStVo : NSObject
/**
 * 姓名
 */
@property (nonatomic,retain) NSString* name;
/**
 * 证件类型
 */
@property (nonatomic,retain) NSString* idtype;
/**
 * 身份证号
 */
@property (nonatomic,retain) NSString* idcode;
/**
 * 电话
 */
@property (nonatomic,retain) NSString* phone;
/**
 * 学校
 */
@property (nonatomic,retain) NSString* school;
/**
 * 学生证号
 */
@property (nonatomic,retain) NSString* stcode;
/**
 * 出生日期
 */
@property (nonatomic,retain) NSString* birth;
/**
 * 办卡费用
 */
@property (nonatomic) int price;
/**
 * null
 */
@property (nonatomic,retain) NSString* img1;



@end


////////////////////////////////////////////////////////////////////////////////

/** *  地址列表
 */
@interface SAddrListVo : NSObject<IJsonValueObject>

/**
 * 省
 */
@property (nonatomic,retain) NSString* sheng;
/**
 * 市
 */
@property (nonatomic,retain) NSString* shi;
/**
 * 区
 */
@property (nonatomic,retain) NSString* qu;
/**
 * 详细地址
 */
@property (nonatomic,retain) NSString* jie;
/**
 * 邮编
 */
@property (nonatomic,retain) NSString* pc;
/**
 * 是否是默认地址
 */
@property (nonatomic) int def;
/**
 * 姓名
 */
@property (nonatomic,retain) NSString* name;
/**
 * null
 */
@property (nonatomic,retain) NSString* phone;
/**
 * null
 */
@property (nonatomic) int ID;



@property (nonatomic) NSInteger sheng_id;
@property (nonatomic) NSInteger shi_id;
@property (nonatomic) NSInteger qu_id;


-(NSString*)getDetalAddr;

-(NSString*)getArea;

@end

////////////////////////////////////////////////////////////////////////////////


/** *  diy列表
 */
@interface SDiyListVo : NSObject<IJsonValueObject>

/**
 * 标题
 */
@property (nonatomic,retain) NSString* title;
/**
 *
 */
@property (nonatomic,retain) NSString* time;
/**
 * 缩略图
 */
@property (nonatomic,retain) NSString* thumb;
/**
 * 正面大图
 */
@property (nonatomic,retain) NSString* img;
/**
 * 图片反面
 */
@property (nonatomic,retain) NSString* imgF;


@end
////////////////////////////////////////////////////////////////////////////////

/** *  订单卡片
 */
@interface SOrdCrdVo : NSObject<IJsonValueObject>

/**
 * 预充值
 */
@property (nonatomic) int recharge;
/**
 * 购买数量
 */
@property (nonatomic) int count;
/**
 * 总价
 */
@property (nonatomic) double total;
/**
 * 应该显示的内容
 */
@property (nonatomic,retain) NSString* title;
/**
 *
 */
@property (nonatomic) double price;
/**
 *
 */
@property (nonatomic,retain) NSString* thumb;
/**
 * 卡号
 */
@property (nonatomic,retain) NSString* ID;

-(NSString*)rechargeString;
-(NSString*)priceString;


@end
////////////////////////////////////////////////////////////////////////////////


/** *  全部订单
 */
@interface SOrderListVo : NSObject<IJsonValueObject>

/**
 * 应付价
 */
@property (nonatomic) double realPrice;
/**
 * 状态
 */
@property (nonatomic) int status;
/**
 * 订单时间
 */
@property (nonatomic,retain) NSString* time;
/**
 * 编号
 */
@property (nonatomic,retain) NSString* ID;
/**
 * 标题
 */
@property (nonatomic,retain) NSString* title;
/**
 * 购买商品总数
 */
@property (nonatomic) int count;
/**
 * 图片
 */
@property (nonatomic,retain) NSString* img;





-(UIColor*)getColor;
-(NSString*)realPriceString;

@end
////////////////////////////////////////////////////////////////////////////////

/** *  订单详情
 */
@interface SOrderDetailVo : NSObject<IJsonValueObject>

@property (nonatomic,retain) NSString* deliverCompany;
@property (nonatomic,retain) NSString* deliverCode;

@property (nonatomic,retain) NSString* ID;

//下单时间
@property (nonatomic,retain) NSString* time;
/**
 * 收货地址
 */
@property (nonatomic,retain) NSString* addr;
/**
 * 收货人姓名
 */
@property (nonatomic,retain) NSString* name;
/**
 * 收货电话
 */
@property (nonatomic,retain) NSString* phone;
/**
 * 邮编
 */
@property (nonatomic,retain) NSString* pcode;
/**
 * 应付价
 */
@property (nonatomic) double realPrice;

@property (nonatomic) double totalPrice;
/**
 * 状态
 */
@property (nonatomic) int state;
@property (nonatomic) double realFee;

@property (nonatomic) double transFee;

@property (nonatomic,retain) NSMutableArray* cards;

-(NSString*)transFeeString;
-(NSString*)totalPriceString;
-(NSString*)realFeeString;
@end
////////////////////////////////////////////////////////////////////////////////
/**
 *  线上售卡模型
 */
@interface SellingModel : NSObject


DECLARE_SHARED_INSTANCE_DIRECT(SellingModel);





/**
 *  从购物车购买
 *
 *  @param parent <#parent description#>
 */
-(void)onBuyFromCart:(UIViewController*)parent data:(NSArray*)data;



-(void)onBuyDirect:(UIViewController*)parent data:(CartVo*)data;
/**
 *  直接购物
 *
 *  @param parent <#parent description#>
 *  @param data   <#data description#>
 */
-(void)onBuyDirect:(UIViewController*)parent data:(SCardListVo*)data count:(NSInteger)count recharget:(NSInteger)recharge;

+(NSString*)getState:(int)state;


@end
