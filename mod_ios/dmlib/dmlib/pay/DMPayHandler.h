//
//  DMPayHandler.h
//  ecard
//
//  Created by randy ren on 16/2/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMPayModel.h"
#import "DMControllerDelegate.h" 

@protocol DMPayDelegate <NSObject>

-(void)onPaySuccess:(id)data;

@end



typedef enum {
   //返回到上一个controller
    PaySuccessAction_BacktoPreviousController,
    //返回到当前controller
    PaySuccessAction_BacktoCurrentController,
    //不做任何事情
    PaySuccessAction_None,
}PaySuccessAction;


typedef enum {
    //返回到上一个controller
    PayCancelAction_BacktoPreviousController,
    //返回到当前controller
    PayCancelAction_BacktoCurrentController,
}PayCancelAction;


/**
 payaction
 定义 使用哪个cashier,指定cashiercontroller(收银台)
 哪些支付方式
 下单后去收银台之前，是否关闭本界面
 支付成功之后，是回到上一个界面，还是上一个界面(回到哪个界面）
 支付取消之后，是回到上一个界面，还是这个界面不动
 支付失败之后，怎么处理
 */
@interface DMPayHandler : NSObject<DMPayModelDelegate,DMControllerDelegate>
{
    DMPayModel* _payModel;
}
-(id)initWithParent:(UIViewController*)parent payAction:(DMPayAction*)action module:(NSString*)module supportTypes:(NSArray<NSNumber*>*)supportTypes;

/*
-(id)initWithParent:(UIViewController*)parent payResultController:(Class)payResultController payAction:(DMPayAction*)action cachierController:(Class)cashierController  supportTypes:(NSArray*)supportTypes;*/

@property (nonatomic,assign) PaySuccessAction paySuccessAction;
@property (nonatomic,assign) PayCancelAction payCancelAction;


-(void)initParam:(NSInteger)fee orderId:(NSString*)orderId;
-(void)initParam:(NSInteger)fee orderId:(NSString*)orderId data:(id)data;

@property (nonatomic,weak) NSObject<DMPayDelegate>* delegate;
@property (nonatomic) Class payResultController;
@property (nonatomic) Class cashierController;
@property (nonatomic,weak) UIViewController* parent;


@end
