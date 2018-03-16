//
//  DMPayModel.m
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMPayModel.h"
#import "DMPayAction.h"
__weak DMPayModel* _model;
@interface DMPayModel()
{
   //  NSArray* _allTypes;
    //支持的支付类型
 
    NSArray<DMPay*>* _payTypes;
}

@end

@implementation DMPayModel

-(void)dealloc{
    _pay = nil;
    _action = nil;
    _payTypes = nil;
}
-(BOOL)handleOpenUrl:(NSURL*)url{
    return [_pay handleOpenUrl:url];
}

-(void)notifyPayCancel{
    if([_delegate respondsToSelector:@selector(payCancel:)]){
         [_delegate payCancel:self];
    }
}

+(DMPayModel*)runningInstance{
    return _model;
}
-(void)notifyPaySuccess:(id)data{
    [_delegate pay:self didPaySuccess:data];
}
-(void)jobSuccess:(DMApiJob*)request{
    //这里已经成功了
    [self notifyPaySuccess:request.data];
}

-(void)notifyClientPaySuccess:(DMPay *)pay data:(id)data{
    if([_delegate respondsToSelector:@selector(clientPaySuccess:type:data:)]){
        [_delegate clientPaySuccess:self type:pay.payType data:data];
    }else{
        [self getOrderInfo:data];
    }
}

-(BOOL)jobError:(DMApiJob*)request{
    //拉取信息失败,网络错误
    //这个不一定会失败
    if([_delegate respondsToSelector:@selector(pay:getOrderInfoError:isNetworkError:)]){
        [_delegate pay:self getOrderInfoError:@"网络错误" isNetworkError:YES];
    }
    return NO;
}

-(BOOL)jobMessage:(DMApiJob*)request{
    //拉取信息失败,服务器消息
    //这个一定失败了
    if([_delegate respondsToSelector:@selector(pay:getOrderInfoError:isNetworkError:)]){
        [_delegate pay:self getOrderInfoError:request.serverMessage isNetworkError:NO];
        return YES;
    }
   
    return NO;
}
-(void)prePay:(NSString*)api data:(NSDictionary*)data{
    _pay.model = self;
    //调用action进行实际的预支付
    [_action prePay:_pay.payType api:api data:data delegate:_pay];
}
-(BOOL)notifyControllerExit{
    if([_delegate respondsToSelector:@selector(payCancel:)]){
        return [_delegate payCancel:self];
    }
    return NO;
}


-(void)setCurrentIndex:(NSInteger)currentIndex{
    if(currentIndex != _currentIndex){
        _currentIndex = currentIndex;
        DMPay* pay = _payTypes[currentIndex];
        NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
        [def setValue:[NSNumber numberWithInteger:pay.payType] forKey:[NSString stringWithFormat: @"pay_for_default_%@",_module]];
        [def synchronize];
        _pay = pay;
    }
}




-(void)setPayType:(NSArray<DMPay*>*)payTypes{
    _payTypes = payTypes;
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults ];
    id defType = [def valueForKey:[ NSString stringWithFormat: @"pay_for_default_%@",_module]];
    NSInteger defValue = payTypes[0].payType;
    if(defType){
        defValue = [defType integerValue];
    }
    //求出当前下表
    NSInteger index = 0;
    for(DMPay* pay in payTypes){
        if(defValue == pay.payType){
            _pay = pay;
            _currentIndex =index;
            break;
        }
        index ++;
    }
    
}
-(NSArray<DMPay*>*)getPayTypes{
    return _payTypes;
}

-(void)getOrderInfo:(id)info{
    [_action getOrderInfo:_orderId info:info delegate:self];
}
-(void)prePay{
    _pay.model = self;
    //调用action进行实际的预支付
    [_action prePay:_pay.payType orderId:_orderId delegate:_pay];

}
-(BOOL)notifyPrePayError:(NSString*)reason isNetworkError:(BOOL)isNetworkError{
    
    
    if(![_delegate respondsToSelector:@selector(prePayError:error:isNetworkError:)]){
        
        return NO;
       //  [_delegate prePayError:self error:reason isNetworkError:isNetworkError];
    }
    
   return [_delegate prePayError:self error:reason isNetworkError:isNetworkError];
    
}
-(id)init{
    if(self = [super init]){
        _model = self;
    }
    return self;
}


@end
