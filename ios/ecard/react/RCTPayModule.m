//
//  RCTPayModule.m
//  MyProject
//
//  Created by 任雪亮 on 16/7/17.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTPayModule.h"
#import <ecardlib/ecardlib.h>
#import "DMBasePayAction.h"
#import "MainViewController.h"

//#import ""
/**
 public static final int PAY_SUCCESS = 0;
 public static final int PAY_CANCEL = 1;
 public static final int PAY_ERROR = 2;
 public static final int PREPAY_ERROR = 3;
 public static final int REPAY_SUCCESS = 4;
 */
typedef enum _PayResult{
  
  GetOrderInfoSuccess,      //拉取订单成功
  PayCancel,                //支付取消
  GetOrderInfoError,        //支付错误
  PrePayError,              //预支付错误
  PrePaySuccess,            //预支付成功
  ClinetPaySuccess,         //客户端支付成功
  ClientPayError            //客户端支付失败
}PayResult;


@interface RCTPayModule()
{
  DMBasePayAction* _payAction;
  DMPayModel* _payModel;
  id _info;
}
@property (nonatomic,readwrite,copy) RCTResponseSenderBlock callback;
@end


@implementation RCTPayModule


-(void)dealloc{
  _payModel = nil;
  _payAction = nil;
  _info = nil;
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(create:(NSString*)moduleId
                  supportPayTypes:(NSArray*)supportPayTypes){
  
  [self createPay:moduleId types:supportPayTypes];
  
}

RCT_EXPORT_METHOD(call:(NSString*)api
                  map:(NSDictionary*)map
                  callback:(RCTResponseSenderBlock)callback){
  _callback = callback;
  [_payModel prePay:api data:map];
  
}

RCT_EXPORT_METHOD(setIndex:(NSInteger)index){
  [_payModel setCurrentIndex:index];
}

RCT_EXPORT_METHOD(destroy){
  _payAction = nil;
  _payModel = nil;
  _info = nil;
}


RCT_EXPORT_METHOD(getPayInfo:(RCTResponseSenderBlock)callback){
  _callback = callback;
  [_payModel getOrderInfo:_info];
}


-(void)clientPaySuccess:(DMPayModel *)pay type:(NSInteger)type data:(id)data{
  //这里需要做特殊处理
  //将order一起传递过去
  _info = data;
  NSMutableDictionary* dic = [[NSMutableDictionary alloc]init];
  dic[@"orderId"] = pay.orderId;
  dic[@"fee"] = [NSNumber numberWithInteger:pay.fee];
  dic[@"info"] = data;
  _callback(@[ [NSNumber numberWithInt:ClinetPaySuccess] ,dic ]);
  
  /*
  dispatch_async(dispatch_get_main_queue(), ^{
    if( [[DMJobManager sharedInstance].topController isKindOfClass:[ECardPayCashierController class]]){
      [[DMJobManager sharedInstance].topController.navigationController popViewControllerAnimated:YES];
    }
  });*/
  
  MainViewController* controller = [CommonUtil findController:[DMJobManager sharedInstance].topController.navigationController clazz:[MainViewController class]];
   [[DMJobManager sharedInstance].topController.navigationController popToViewController:controller animated:YES];
  
}


-(void)createPay:(NSString*)moduleId types:(NSArray*)types{
  
  _payAction = [[DMBasePayAction alloc]init];
  _payAction.moduleId = moduleId;
  
  _payModel = [[DMJobManager sharedInstance]createPayModel:moduleId supportTypes:types];
  _payModel.delegate = self;
  _payModel.action = _payAction;
  _payModel.currentIndex = 0;
  
}


-(BOOL)prePayError:(DMPayModel*)pay error:(NSString*)error isNetworkError:(BOOL)isNetworkError{
  
  
  if(self.callback!=nil){
    self.callback(@[ [NSNumber numberWithInteger:PrePayError] ,@{@"error":error, @"isNetworkError":[NSNumber numberWithBool:isNetworkError] } ]);
    self.callback =nil;
    
  }
  return YES;
}

RCT_EXPORT_METHOD(pay:(NSString*)orderId
                  fee:(NSInteger)fee
                  moduleId:(NSString*)moduleId
                  types:(NSArray*)types
                  callback:(RCTResponseSenderBlock)callback ){
  
  dispatch_async(dispatch_get_main_queue(), ^{
    self.callback = callback;
    
    [self createPay:moduleId types:types];
    
    
    _payModel.orderId = orderId;
    _payModel.fee = fee;
    
   ECardPayCashierController * controller= [[ECardPayCashierController alloc]init];
   [[DMJobManager sharedInstance].topController.navigationController pushViewController: controller animated:YES];
    
  });
  
  
  
}

-(void)pay:(DMPayModel*)pay didPaySuccess:(id)data{
  if(self.callback!=nil){
    self.callback(@[ @0,data ]);
    self.callback =nil;
    _payModel = nil;
    _payAction = nil;

  }
  
  UIViewController* prevController = [CommonUtil findPrevController:[DMJobManager sharedInstance].topController];
  [[DMJobManager sharedInstance].topController.navigationController popToViewController:prevController animated:YES];

}


-(BOOL)payCancel:(DMPayModel *)pay{
  if(self.callback!=nil){
    self.callback(@[[NSNumber numberWithInteger:PayCancel] ]);
    self.callback =nil;
  }
  return NO;
}

-(void)pay:(DMPayModel *)pay getOrderInfoError:(NSString *)error isNetworkError:(BOOL)isNetworkError{
  if(self.callback!=nil){
    self.callback(@[ @1,@{@"error":error, @"isNetworkError":[NSNumber numberWithBool:isNetworkError] } ]);
    self.callback =nil;
    _payModel = nil;
    _payAction = nil;

  }
}
@end
