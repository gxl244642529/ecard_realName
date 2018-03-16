//
//  SellingModel.m
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SellingModel.h"
#import "JsonTaskManager.h"
#import "SOrderConfirmController.h"
#import <ecardlib/ecardlib.h>

#import "SAddrManager.h"

////////////////////////////////////////////////////////////////

@implementation STypeVo

-(void)fromJson:(NSDictionary *)json{
    
    self.title = [json valueForKey:@"TITLE"];
    self.path = [json valueForKey:@"ID"];
    
}

@end

////////////////////////////////////////////////////////////////
@implementation CartVo



-(void)fromJson:(NSDictionary *)json{
    self.ID=[[json valueForKey:@"ID"]intValue];
    self.cartID = [NSString stringWithFormat:@"%@",[json valueForKey:@"CART_ID"]];
    self.title=[json valueForKey:@"TITLE"];
    self.price=[[json valueForKey:@"PRICE"]intValue];
    self.thumb=[json valueForKey:@"THUMB"];
    self.count = [[json valueForKey:@"COUNT"]intValue];
    self.recharge = [[json valueForKey:@"RECHARGE"]intValue] / 100;
    self.imageID = [[json valueForKey:@"IMG_ID"]intValue];
    self.store = [[json valueForKey:@"STOCK"]intValue];
}

-(id)init{
    if(self=[super init]){
        self.count = 1;
    }
    return self;
}


-(NSString*)countString{
    return [NSString stringWithFormat:@"%ld",(long)self.count];
}

-(NSString*)rechargeString{
    return [NSString stringWithFormat:@"%ld",(long)self.recharge];
}
-(float)totalPrice{
    return (float)self.count * (self.recharge*100 + self.price) / 100;
}
-(NSString*)priceString{
    return [NSString stringWithFormat:@"%.2f",self.price/100];
}
-(NSString*)totalPriceString{
    return [NSString stringWithFormat:@"%.2f",self.totalPrice];
}
-(void)dealloc{
    self.title = NULL;
    self.thumb = nil;
}
@end
////////////////////////////////////////////////////////////////
@implementation SCardDetailVo
-(void)dealloc{
    self.img=NULL;
	self.imgF=NULL;
	self.tip=NULL;
}

-(void)fromJson:(NSDictionary*)json{
    self.img=[Constants formatUrl:[json valueForKey:@"IMG"]];
    self.imgF=[Constants formatUrl:[json valueForKey:@"IMG_F"]];
    self.hasInfo=[[json valueForKey:@"HAS_INFO"]intValue];
    self.saled=[[json valueForKey:@"SALED"]intValue];
    self.store=[[json valueForKey:@"STORE"]intValue];
    self.tip=[json valueForKey:@"TIP"];
    
}

@end
////////////////////////////////////////////////////////////////

@implementation SCardListVo
-(void)dealloc{
	self.thumb=NULL;
}

-(void)fromJson:(NSDictionary *)json{
    self.ID=[[json valueForKey:@"ID"]integerValue];
    self.title=[json valueForKey:@"TITLE"];
    self.price=[[json valueForKey:@"PRICE"]intValue];
    self.thumb=[json valueForKey:@"THUMB"];
}
@end
////////////////////////////////////////////////////////////////


@implementation SDiyPagesVo
-(void)dealloc{
    self.img=NULL;
	self.thumb=NULL;
    self.title = nil;
    
}

-(void)fromJson:(NSDictionary*)json{
    self.img=[json valueForKey:@"IMG"];
    self.ID=[[json valueForKey:@"ID"]integerValue];
    self.thumb=[json valueForKey:@"THUMB"];
    self.title = [json valueForKey:@"TITLE"];
    self.price=[[json valueForKey:@"PRICE"]intValue];
    self.store = [[json valueForKey:@"STORK"]integerValue];
}
-(NSString*)stringPrice{
    return [NSString stringWithFormat:@"%.2f",self.price/100];
}
@end

////////////////////////////////////////////////


@implementation SBookListStVo

-(void)dealloc{
    self.name=NULL;
	self.idtype=NULL;
	self.idcode=NULL;
	self.phone=NULL;
	self.school=NULL;
	self.stcode=NULL;
	self.birth=NULL;
	self.img1=NULL;
}

@end

////////////////////////////////////////////////////////////////
@implementation SAddrListVo
-(void)dealloc{
    self.sheng=NULL;
	self.shi=NULL;
	self.qu=NULL;
	self.jie=NULL;
	self.pc=NULL;
	self.name=NULL;
	self.phone=NULL;
    
}

-(void)fromJson:(NSDictionary *)json{
     self.sheng_id=[[json valueForKey:@"SHENG_ID"]integerValue];
     self.shi_id=[[json valueForKey:@"SHI_ID"]integerValue];
     self.qu_id=[[json valueForKey:@"QU_ID"]integerValue];
    self.sheng=[json valueForKey:@"SHENG"];
    self.shi=[json valueForKey:@"SHI"];
    self.qu=[json valueForKey:@"QU"];
    self.jie=[json valueForKey:@"JIE"];
    self.pc=[json valueForKey:@"PC"];
    self.def=[[json valueForKey:@"DEF"]intValue];
    self.name=[json valueForKey:@"NAME"];
    self.phone=[json valueForKey:@"PHONE"];
    self.ID=[[json valueForKey:@"ID"]intValue];
}
-(NSString*)getDetalAddr{
    if(self.qu){
       return [NSString stringWithFormat:@"%@%@%@%@",self.sheng,self.shi,self.qu,self.jie];
    }
    return [NSString stringWithFormat:@"%@%@%@",self.sheng,self.shi,self.jie];
}
-(NSString*)getArea{
    if(self.qu){
        return [NSString stringWithFormat:@"%@ %@ %@",self.sheng,self.shi,self.qu];
    }
    return [NSString stringWithFormat:@"%@ %@",self.sheng,self.shi];

}
@end
////////////////////////////////////////////////////////////////


@implementation SDiyListVo
-(void)dealloc{
    self.title=NULL;
	self.time=NULL;
	self.thumb=NULL;
	self.img=NULL;
	self.imgF=NULL;
    
}

-(void)fromJson:(NSDictionary*)json{
    self.title=[json valueForKey:@"TITLE"];
    self.time=[json valueForKey:@"TIME"];
    self.thumb=[Constants formatUrl:[json valueForKey:@"THUMB"]];
    self.img=[Constants formatUrl:[json valueForKey:@"IMG"]];
    self.imgF=[Constants formatUrl:[json valueForKey:@"IMG_F"]];
    
}

@end
////////////////////////////////////////////////////////////////

@implementation SOrdCrdVo
-(void)dealloc{
    self.title=NULL;
	self.thumb=NULL;
	self.ID=NULL;
    
}

-(void)fromJson:(NSDictionary*)json{
    self.recharge=[[json valueForKey:@"RECHARGE"]intValue];
    self.count=[[json valueForKey:@"COUNT"]intValue];
    self.total=[[json valueForKey:@"TOTAL"]intValue];
    self.title=[json valueForKey:@"TITLE"];
    self.price=[[json valueForKey:@"PRICE"]intValue];
    self.thumb=[json valueForKey:@"THUMB"];
    self.ID=[json valueForKey:@"ID"];
    
}
-(NSString*)rechargeString{
    return [NSString stringWithFormat:@"%d",self.recharge / 100];
}
-(NSString*)priceString{
    return [NSString stringWithFormat:@"%.2f",self.price / 100];
}
@end

////////////////////////////////////////////////////////////////


@implementation SOrderListVo
-(void)dealloc{
    self.time=NULL;
	self.title=NULL;
	self.img=NULL;
    self.ID = nil;
    
}

-(void)fromJson:(NSDictionary*)json{
    self.realPrice=[[json valueForKey:@"REAL_PRICE"]intValue];
    self.status=[[json valueForKey:@"STATE"]intValue];
    self.time=[json valueForKey:@"TIME"];
    self.ID=[NSString stringWithFormat:@"%@",[json valueForKey:@"ID"]];
    self.title=[json valueForKey:@"TITLE"];
    self.count=[[json valueForKey:@"COUNT"]intValue];
    self.img=[json valueForKey:@"IMG"];
    
}


-(NSString*)realPriceString{
    return [NSString stringWithFormat:@"%.2f",self.realPrice/100];
}

-(UIColor*)getColor{
    /*
    switch (self.status) {
        case OrderState_Unpay:
            return RGB(f8,6c,19);
        case OrderState_Undelivered:
            return RGB(66,B0,F1);
        case OrderState_Unreceived:
            return RGB(85,C3,02);
        case OrderState_Unaudited:
            return RGB(9B,9B,F1);
        case OrderState_Finished:
            return RGB(ff, 3d, 2c);
        default:
            break;
    }*/
    return NULL;

}
@end
////////////////////////////////////////////////////////////////



@implementation SOrderDetailVo
-(void)dealloc{
	self.addr=NULL;
	self.name=NULL;
	self.phone=NULL;
	self.pcode=NULL;
    self.cards = NULL;
    self.deliverCode = nil;
    self.deliverCompany = nil;
    self.time = nil;
    self.ID = nil;
}

-(void)fromJson:(NSDictionary*)json{
    self.ID=[json valueForKey:@"ID"];
    self.deliverCompany=[json valueForKey:@"DELIVER"];
    self.deliverCode=[json valueForKey:@"DELIVER_CODE"];
    self.time=[json valueForKey:@"TIME"];
    
    self.addr=[json valueForKey:@"ADDR"];
    self.name=[json valueForKey:@"NAME"];
    self.phone=[json valueForKey:@"PHONE"];
    self.pcode=[json valueForKey:@"PCODE"];
    self.realPrice=[[json valueForKey:@"REAL_PRICE"]intValue];
    self.state=[[json valueForKey:@"STATE"]intValue];
    self.cards = [[NSMutableArray alloc]init];
    self.transFee = [[json valueForKey:@"ATTACH"] doubleValue];
    self.totalPrice = [[json valueForKey:@"TOTAL_PRICE"]intValue];
    self.realFee =[[json valueForKey:@"REAL_PRICE"]intValue];
    NSArray* arr = [json valueForKey:@"cards"];
    for (NSDictionary* dic in arr) {
        SOrdCrdVo* vo = [[SOrdCrdVo alloc]init];
        [vo fromJson:dic];
        [self.cards addObject:vo];
    }
}
-(NSString*)transFeeString{
    return [NSString stringWithFormat:@"%.2f",self.transFee/100];
}

-(NSString*)totalPriceString{
     return [NSString stringWithFormat:@"%.2f",self.totalPrice/100];
}

-(NSString*)realFeeString{
    return [NSString stringWithFormat:@"%.2f",self.realFee/100];
}
@end

////////////////////////////////////////////////////////////////

@interface SellingModel()
{
}

@end

@implementation SellingModel




IMPLEMENT_SHARED_INSTANCE_DIRECT(SellingModel);

-(void)dealloc{
    
}

-(void)onBuyFromCart:(UIViewController*)parent data:(NSArray *)data{
    [SVProgressHUD showWithStatus:@"正在加载..."];
    
    [[SAddrManager sharedInstance]getDefaultAddr:^(SAddrListVo *defaultAddr) {
        [SVProgressHUD dismiss];
        SOrderConfirmController* controller = [[SOrderConfirmController alloc]init];
        controller.backController = parent;
        controller.type = ConfirmType_Cart;
        controller.addr = defaultAddr;
        controller.data = data;
        [parent.navigationController pushViewController:controller animated:YES];
    }];
}


-(void)onBuyDirect:(UIViewController*)parent data:(CartVo*)data{
    
    //首先获取地址列表
    [SVProgressHUD showWithStatus:@"正在加载..."];
    [[SAddrManager sharedInstance]getDefaultAddr:^(SAddrListVo *defaultAddr) {
        [SVProgressHUD dismiss];
        SOrderConfirmController* controller = [[SOrderConfirmController alloc]init];
        controller.backController = parent;
        controller.type = ConfirmType_Direct;
        controller.cardtype = CardType_Normal;
        controller.data = data;
        controller.addr = defaultAddr;
        [parent.navigationController pushViewController:controller animated:YES];
    }];

    
}


-(void)onBuyDirect:(UIViewController*)parent data:(SCardListVo*)data count:(NSInteger)count recharget:(NSInteger)recharge{
    CartVo* cart = [[CartVo alloc]init];
    cart.thumb = data.thumb;
    cart.ID = data.ID;
    cart.title = data.title;
    cart.count = count;
    cart.recharge = recharge;
    cart.price = data.price;
    
    [self onBuyDirect:parent data:cart];
    
}
+(NSString*)getState:(int)state{
    switch (state) {
        case ORDER_NOPAY:
        case ORDER_PAYING:
            return @"待付款";
        case ORDER_PAYED:
            return @"待发卡";
        case ORDER_DELIVERED:
            return @"待收卡";
        case ORDER_BACK:
            return @"退货中";
        case ORDER_BACK_FAIL:
            return @"退货失败";
        case ORDER_RETURN:
            return @"退款中";
        case ORDER_RETURN_FAIL:
            return @"退款中";
        case ORDER_CLOSED:
            return @"交易关闭";
        case ORDER_RECEIVED:
            return @"交易完成";
        case ORDER_RETURN_FINISH:
            return @"退款成功";
        case ORDER_BACKED:
            return @"退货成功";
        default:
            break;
    }
    return @"未知状态";
}








@end
