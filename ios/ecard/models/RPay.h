//
//  RPay.h
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IPayAction.h"
#import <UIKit/UIKit.h>
#import "CommonMacro.h"
#import "AbsPay.h"

@class  AbsPay;
@class  ECardRPayModel;

@protocol PayDelegate <NSObject>

@required
-(void)onPaySuccess:(ECardRPayModel*)model;

@optional
-(void)onPayCancel:(ECardRPayModel*)model;
-(void)onPayError:(ECardRPayModel*)model code:(NSInteger)code message:(NSString*)message;
-(void)onNotifyServerError:(ECardRPayModel*)model error:(NSString*)error isNetworkError:(BOOL)isNetworkError;

@end

/**
 *  收银台数据,客户端pay,调用prepay接口后生成的数据
 */
@interface CashierInfo : NSObject

-(id)initWithOrderID:(NSString*)orderID fee:(double)fee;
-(id)initWithOrderID:(NSString*)orderID fee:(double)fee extra:(id)extraInfo;
/**
 *  订单号
 *
 *  @return <#return value description#>
 */
-(NSString*)orderID;

/**
 *  格式化的价格
 *
 *  @return <#return value description#>
 */
-(NSString*)formatFee;


/**
 *  总价格
 *
 *  @return <#return value description#>
 */
-(double)fee;


-(id)extraInfo;

@end

//////////////////////////////////////////////////////////////////////////////////////////

/**
 *  支付成功以后返回的数据
 */
@interface OrderInfo : NSObject

@property (nonatomic,retain) NSString* outID;

@property (nonatomic,retain) NSString* platID;

@property (nonatomic,retain) NSString* title;

@property (nonatomic) double fee;

-(NSString*)formatFee;

@end


//////////////////////////////////////////////////////////////////////////////////////////

@interface PayTypeInfo : NSObject

-(id)initWithType:(PayType)type text:(NSString*)text icon:(NSString*)icon;

@property (nonatomic) PayType type;
@property (nonatomic,retain) NSString* text;
@property (nonatomic,retain) NSString* icon;

@end

//////////////////////////////////////////////////////////////////////////////////////////
@interface ECardRPayModel : NSObject

+(ECardRPayModel*)runningInstance;
+(NSString*)WXAppID;
+(void)setWXAppID:(NSString*)appID;


-(id)initWithController:(UIViewController*)parent delegate:(id<PayDelegate>)delegate;

-(BOOL)handleOpenUrl:(NSURL*)url;

/**
 *  设置id
 *
 *  @param ID <#ID description#>
 */
-(void)setId:(NSInteger)ID;

/**
 *  设置支持的支付类型
 *
 *  @param types <#types description#>
 */
-(void)setPayTypes:(NSArray*)types;

/**
 *  设置收银台信息
 *
 *  @param info <#info description#>
 */
-(void)setCashierInfo:(CashierInfo*)info;

/**
 *  获取收银台信息
 *
 *  @return <#return value description#>
 */
-(CashierInfo*)cashierInfo;

/**
 *  <#Description#>
 *
 *  @param action <#action description#>
 */
-(void)setPayAction:(id<IPayAction>)action;

-(void)setCurrentPayIndex:(NSInteger)index;

-(void)prePay;


/**
 *  当前支付方式
 *
 *  @return <#return value description#>
 */
-(id)currentPay;

/**
 *  <#Description#>
 *
 *  @param type   <#type description#>
 *  @param result <#result description#>
 */
-(void)onPaySuccess:(PayType)type result:(id)result;

/**
 *  <#Description#>
 *
 *  @param errorCode <#errorCode description#>
 *  @param error     <#error description#>
 */
-(void)onPayError:(NSInteger)errorCode error:(NSString*)error;

/**
 *  当取消付款的时候
 */
-(void)onPayCancel;

/**
 *  支付数据(结果)
 *
 *  @return <#return value description#>
 */
-(id)payResult;

-(void)setOrderInfo:(OrderInfo*)orderInfo;

-(OrderInfo*)orderInfo;


-(NSArray*)supportPayTyptes:(NSArray*)types;

-(NSInteger)currentIndex;


-(void)onNotifyServerError:(NSString*)error isNetworkError:(BOOL)isNetworkError payType:(NSInteger)payType;


@end


