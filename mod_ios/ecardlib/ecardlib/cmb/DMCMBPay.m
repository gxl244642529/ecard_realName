//
//  DMCMBPay.m
//  ecardlib
//
//  Created by 任雪亮 on 16/10/13.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "DMCMBPay.h"


@interface DMCMBPay()
    @property (nonatomic,weak) id<DMPayModelDelegate> orgDelegate;

@end

@implementation DMCMBPay

@synthesize model=_model;
-(id)init{
    if(self =[super init]){
        self.payType = DMPAY_CMB;
        
          }
    return self;
}
    

-(void)onCMBSuccess:(NSArray*)info{
 //   [_model getOrderInfo:nil];
    [_model notifyClientPaySuccess:self data:nil];
    
}
-(void)onCMBCancel{
    if(!_orgDelegate || _model.delegate != self){
        _orgDelegate = _model.delegate;
        _model.delegate = self;
    }
    
    /*
    dispatch_async(dispatch_get_main_queue(), ^{
     //   [_model getOrderInfo:nil];
    });
     */

}


-(void)pay:(DMPayModel*)pay didPaySuccess:(id)data{
    [_orgDelegate pay:pay didPaySuccess:data];
    _model.delegate = _orgDelegate;
}
//返回值表示,是否对退出做出处理了,如返回false,则应该退出收银台
-(BOOL)payCancel:(DMPayModel*)pay{
   /* _orgDelegate = nil;
     _model.delegate = _orgDelegate;
    return [_orgDelegate payCancel:pay];*/
    return false;
}
-(void)pay:(DMPayModel *)pay getOrderInfoError:(NSString*)error isNetworkError:(BOOL)isNetworkError{
  /*   _model.delegate = _orgDelegate;
    _orgDelegate = nil;
   
   */
   if([error hasPrefix:@"DX4000"])
    {
        
    }else{
       [_orgDelegate pay:pay getOrderInfoError:error isNetworkError:isNetworkError];
        _model.delegate = _orgDelegate;
    }
    
}


-(UIImage*)icon{
    return [UIImage imageNamed:@"ecardlibbundle.bundle/cmb_pay"];
}
-(NSString*)title{
    return @"一网通银行卡支付";
}

-(void)jobSuccess:(DMApiJob*)request{
    NSString* url = request.data;
   CMBWebViewController* controller= [[CMBWebViewController alloc]initWithTitle:@"" url:[self clean:url]];
    controller.delegate = self;
    controller.payDelegate = self;
    [[DMJobManager sharedInstance].topController.navigationController pushViewController:controller animated:YES];
}
-(NSString*)clean:(NSString*)uglyString{
       return [uglyString stringByReplacingOccurrencesOfString:@"|" withString:@"%7C"];
    
}

@end
