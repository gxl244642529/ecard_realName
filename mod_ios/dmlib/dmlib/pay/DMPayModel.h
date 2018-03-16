//
//  DMPayModel.h
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMPayHeader.h"
#import "DMApiDelegate.h"
#import "DMViewController.h"
#import "DMMacro.h"
#import "DMPayAction.h"
#import "DMPay.h"

@class DMPay;
@class DMPayModel;
@class DMViewController;

@protocol DMPayFactory <NSObject>

-(DMPay*)createPay:(DMPayType)type;
-(DMViewController*)createPayCashier:(Class)payCashierClass;
@end

@protocol DMPayModelDelegate <NSObject>

@optional
-(void)prePaySuccess:(DMPayModel*)pay;
-(BOOL)prePayError:(DMPayModel*)pay error:(NSString*)error isNetworkError:(BOOL)isNetworkError;
-(void)clientPaySuccess:(DMPayModel*)pay type:(NSInteger)type data:(id)data;

-(void)pay:(DMPayModel*)pay didPaySuccess:(id)data;
//返回值表示,是否对退出做出处理了,如返回false,则应该退出收银台
-(BOOL)payCancel:(DMPayModel*)pay;
-(void)pay:(DMPayModel *)pay getOrderInfoError:(NSString*)error isNetworkError:(BOOL)isNetworkError;
@end

@interface DMPayModel : NSObject<DMApiDelegate>

//这些需要配置
@property (nonatomic,retain) DMPayAction* action;
@property (nonatomic,copy) NSString* orderId;
@property (nonatomic,retain) id extraInfo;
@property (nonatomic,weak) id<DMPayModelDelegate> delegate;

@property (nonatomic,assign) NSInteger fee;

@property (nonatomic,weak) id<DMPayFactory> factory;

+(DMPayModel*)runningInstance;

-(BOOL)handleOpenUrl:(NSURL*)url;

-(void)getOrderInfo:(id)info;
-(void)prePay;
-(void)prePay:(NSString*)api data:(NSDictionary*)data;
-(BOOL)notifyControllerExit;
-(void)notifyPaySuccess:(id)data;
-(NSArray<DMPay*>*)getPayTypes;
-(void)setPayType:(NSArray<DMPay*>*)payTypes;
-(void)notifyPayCancel;
-(BOOL)notifyPrePayError:(NSString*)reason isNetworkError:(BOOL)isNetworkError;

@property (nonatomic,assign) NSInteger currentIndex;

@property (nonatomic,weak) DMPay* pay;

-(void)notifyClientPaySuccess:(DMPay*)pay data:(id)data;
//业务id
@property (nonatomic,copy) NSString* module;

@end
