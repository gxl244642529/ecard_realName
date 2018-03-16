//
//  RPay.m
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "RPay.h"

#import "AliPay.h"

#import <dmlib/dmlib.h>

////////////////////////////////////////////////////////////////////////////////////

@interface CashierInfo()
{
    NSString* _orderID;
    double _fee;
    id _extraInfo;
}
@end

@implementation CashierInfo

-(void)dealloc{
    _orderID = NULL;
    _extraInfo = nil;
}

-(id)initWithOrderID:(NSString*)orderID fee:(double)fee{
    if(self = [super init]){
        _orderID = orderID;
        _fee = fee;
    }
    return self;
}
-(id)initWithOrderID:(NSString*)orderID fee:(double)fee extra:(id)extraInfo{
    if(self = [super init]){
        _orderID = orderID;
        _fee = fee;
        _extraInfo = extraInfo;
    }
    return self;

}
/**
 *  订单号
 *
 *  @return <#return value description#>
 */
-(NSString*)orderID{
    return _orderID;
}

-(id)extraInfo{
    return _extraInfo;
}
/**
 *  格式化的价格
 *
 *  @return <#return value description#>
 */
-(NSString*)formatFee{
    return [NSString stringWithFormat:@"%.2f",_fee];
}


/**
 *  总价格
 *
 *  @return <#return value description#>
 */
-(double)fee{
    return _fee;
}




@end

////////////////////////////////////////////////////////////////////////////////

@interface OrderInfo()
{
   
}

@end


@implementation OrderInfo

-(void)dealloc{
    self.outID = nil;
    self.platID = nil;
    self.title = nil;
}
-(NSString*)formatFee{
    return [NSString stringWithFormat:@"%.2f",self.fee];
}
@end

//////////////////////////////////////////////////////////////////////////////////////////

@implementation PayTypeInfo
-(id)initWithType:(PayType)type text:(NSString*)text icon:(NSString*)icon{
    if(self = [super init]){
        self.type = type;
        self.text = text;
        self.icon = icon;
    }
    return self;
}
-(void)dealloc{
    self.text = nil;
    self.icon = nil;
}

@end

//////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////
__weak ECardRPayModel* g_model;

NSString* gWXAppID;




@interface ECardRPayModel()
{
    CashierInfo* _cashierInfo;
    id<IPayAction> _action;
    NSArray* _payTypes;
    AbsPay* _pay;
    
    //支持的支付类型
    NSMutableArray* _supportPayTypeInfos;
    OrderInfo* _orderInfo;
     NSArray* _allTypes;
    //支付结果(数据)
    id _payResult;
    
    
    NSInteger _currentIndex;
    NSInteger _id;
    __weak UIViewController* _parent;
    __weak id<PayDelegate> _delegate;
}

@end

@implementation ECardRPayModel
+(ECardRPayModel*)runningInstance{
    return g_model;
}


+(NSString*)WXAppID{
    return gWXAppID;
}
+(void)setWXAppID:(NSString*)appID{
    gWXAppID = appID;
}

-(void)setOrderInfo:(OrderInfo*)orderInfo{
    _orderInfo = orderInfo;
}

-(OrderInfo*)orderInfo{
 
    return _orderInfo;
}
-(void)dealloc{
    _allTypes = NULL;
    _payResult = nil;
    _orderInfo = nil;
    _payTypes = nil;
    _cashierInfo = nil;
    _action = nil;
    _supportPayTypeInfos = nil;
    _pay = nil;
}


-(BOOL)handleOpenUrl:(NSURL*)url{
    
    
    return [_pay handleOpenUrl:url];
}
-(id)initWithController:(UIViewController*)parent delegate:(id<PayDelegate>)delegate{
    if(self = [super init]){
        g_model = self;
        _parent = parent;
        _delegate = delegate;
        _supportPayTypeInfos = [[NSMutableArray alloc]init];
        _allTypes = @[
                      [[PayTypeInfo alloc]initWithType:PAY_WEIXIN text:@"微信" icon:@"safe_weixin_pay"],
                      [[PayTypeInfo alloc]initWithType:PAY_ALIPAY text:@"支付宝" icon:@"safe_ali_pay"],
                       [[PayTypeInfo alloc]initWithType:PAY_ETONGKA text:@"e通卡后台账户" icon:@"s_pay_3"],
                      [[PayTypeInfo alloc]initWithType:PAY_UNION text:@"银联支付" icon:@"s_pay_1"]
                      ];
    }
    return self;
}



-(NSArray*)supportPayTyptes:(NSArray*)types{
    
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults ];
    id defType = [def valueForKey:[ NSString stringWithFormat: @"pay_for_default_%d",(int)_id]];
    NSInteger defaultType;
    NSInteger index = 0;
    if(defType){
        defaultType = [defType integerValue];
    }else{
        defaultType = [_payTypes[0]integerValue];
    }
    _currentIndex = 0;
    [_supportPayTypeInfos removeAllObjects];
    for (PayTypeInfo* info in _allTypes) {
        BOOL contains = false;
        for(NSNumber* num in _payTypes){
            if([num integerValue] == info.type){
                //包含在里面
                contains = true;
                break;
            }
        }
        
        if(contains){
            [_supportPayTypeInfos addObject:info];
            if(info.type == defaultType){
                _currentIndex = index;
            }
            index++;
        }
    }
    return _supportPayTypeInfos;
}

/**
 *  设置id
 *
 *  @param ID <#ID description#>
 */
-(void)setId:(NSInteger)ID{
    _id = ID;
}

/**
 *  设置支持的支付类型
 *
 *  @param types <#types description#>
 */
-(void)setPayTypes:(NSArray*)types{
    _payTypes = types;
}

/**
 *  设置收银台信息
 *
 *  @param info <#info description#>
 */
-(void)setCashierInfo:(CashierInfo*)info{
    _cashierInfo = info;
}

/**
 *  获取收银台信息
 *
 *  @return <#return value description#>
 */
-(CashierInfo*)cashierInfo{
    return _cashierInfo;
}

/**
 *  <#Description#>
 *
 *  @param action <#action description#>
 */
-(void)setPayAction:(id<IPayAction>)action{
    _action = action;
}

-(void)prePay{
    _orderInfo = nil;
    PayTypeInfo* info = _supportPayTypeInfos[_currentIndex];
    switch (info.type) {
        case PAY_ALIPAY:
            _pay = [[ AliPay alloc]initWithModel:self controller:_parent action:_action];
            break;
        case PAY_ETONGKA:
       //     _pay = [[ECardPay alloc]initWithModel:self controller:_parent];
            break;
        case PAY_WEIXIN:
          //  _pay = [[WXPay alloc]initWithModel:self controller:_parent action:_action];
            break;
        default:
            break;
    }
    [_action prePay:info.type orderID:_cashierInfo.orderID listener:_pay];
}


-(id)currentPay{
    return _pay;
}
/**
 *  <#Description#>
 *
 *  @param type   <#type description#>
 *  @param result <#result description#>
 */
-(void)onPaySuccess:(PayType)type result:(id)result{
    _payResult = result;
    //通知成功
    [_delegate onPaySuccess:self];
    
}
-(void)setCurrentPayIndex:(NSInteger)index{
    if(index != _currentIndex){
        PayTypeInfo* info = _supportPayTypeInfos[index];
        _currentIndex = index;
        NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
        [def setValue:[NSNumber numberWithInteger:info.type] forKey:[NSString stringWithFormat: @"pay_for_default_%d",(int)_id]];
        [def synchronize];
    }
}

/**
 *  支付数据(结果)
 *
 *  @return <#return value description#>
 */
-(id)payResult{
    return _payResult;
}

/**
 *  <#Description#>
 *
 *  @param errorCode <#errorCode description#>
 *  @param error     <#error description#>
 */
-(void)onPayError:(NSInteger)errorCode error:(NSString*)error{
    if([_delegate respondsToSelector:@selector(onPayError:code:message:)]){
         [_delegate onPayError:self code:errorCode message:error];
    }else{
        [Alert alert:error];
    }
   
}
-(void)onNotifyServerError:(NSString*)error isNetworkError:(BOOL)isNetworkError payType:(NSInteger)payType{
   // if(_delegate respondsToSelector:<#(SEL)#>)
    if([_delegate respondsToSelector:@selector(onNotifyServerError:error:isNetworkError:)]){
        [_delegate onNotifyServerError:self error:error isNetworkError:isNetworkError];
    }
}
/**
 *  当取消付款的时候
 */
-(void)onPayCancel{
    if([_delegate respondsToSelector:@selector(onPayCancel:)]){
        [_delegate onPayCancel:self];
    }
}


-(NSInteger)currentIndex{
    return _currentIndex;
}
@end

