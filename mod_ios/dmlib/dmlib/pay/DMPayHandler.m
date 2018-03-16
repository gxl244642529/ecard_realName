//
//  DMPayHandler.m
//  ecard
//
//  Created by randy ren on 16/2/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMPayHandler.h"
#import "DMJobManager.h"
#import "CommonUtil.h"
#import "Alert.h"


@implementation DMPayHandler
-(void)dealloc{
    _payModel = nil;
}
-(id)initWithParent:(UIViewController*)parent payAction:(DMPayAction*)action module:(NSString*)module supportTypes:(NSArray<NSNumber*>*)supportTypes{
    if(self =[super init]){
        _parent = parent;
        _payModel = [[DMJobManager sharedInstance]createPayModel:module supportTypes:supportTypes];
        _payModel.action = action;
        //默认
        _payModel.delegate = self;
        
    }
    return self;
}

-(void)controllerWillFinish:(DMViewController*)controller{
    [_delegate onPaySuccess:controller.data];
}
-(void)initParam:(NSInteger)fee orderId:(NSString*)orderId data:(id)data{
    _payModel.fee = fee;
    _payModel.orderId = orderId;
    DMViewController* c =  [[DMJobManager sharedInstance] createPayCashier:_cashierController];
    c.data = data;
    [_parent.navigationController pushViewController: c animated:YES];
}
-(void)initParam:(NSInteger)fee orderId:(NSString*)orderId{
    _payModel.fee = fee;
    _payModel.orderId = orderId;
     DMViewController* c =  [[DMJobManager sharedInstance] createPayCashier:_cashierController];
    [_parent.navigationController pushViewController:c animated:YES];
}
-(void)pay:(DMPayModel*)pay didPaySuccess:(id)data{
    UIViewController* prevController ;
    if(_paySuccessAction==PaySuccessAction_BacktoPreviousController){
        //这里应该解析数据,并展示结果页面
        prevController = [CommonUtil findPrevController:_parent];
        [_parent.navigationController popToViewController:prevController animated:NO];
    }else if(_paySuccessAction==PaySuccessAction_BacktoCurrentController){
        prevController = self.parent;
        [_parent.navigationController popToViewController:prevController animated:NO];
    }
    
    DMViewController* controller= [[_payResultController alloc]init];
    if( [controller conformsToProtocol:@protocol(DMControllerDelegateContainer) ]){
        id<DMControllerDelegateContainer> container = (id<DMControllerDelegateContainer>)controller;
        container.controllerDelegate = self;
    }
    controller.data = data;
    [prevController.navigationController pushViewController:controller animated:YES];
}

-(BOOL)payCancel:(DMPayModel *)pay{
    if(_payCancelAction == PayCancelAction_BacktoCurrentController){
        [_parent.navigationController popToViewController:_parent animated:YES];
    }else{
        UIViewController* prevController = [CommonUtil findPrevController:_parent];
        [_parent.navigationController popToViewController:prevController animated:YES];
    }
    
    
    return YES;
}


-(void)pay:(DMPayModel *)pay getOrderInfoError:(NSString *)error isNetworkError:(BOOL)isNetworkError{
    [Alert alert:error];
}

@end
