//
//  SellingPayController.h
//  ecard
//
//  Created by randy ren on 15/4/2.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"

#import "PayModel.h"


@protocol PaySuccessDelegate <NSObject>

-(void)onPaySuccess:(id)data;

@end


@interface SellingPayController : MyViewController<IDataAdapterListener,IOnItemClickListener,UIAlertViewDelegate,PayDelegate>


+(void)pay:(UIViewController*)parent id:(NSString*)orderId total:(NSInteger)total backController:(UIViewController*)backController delegate:(NSObject<PaySuccessDelegate>*)delegate;

@property (nonatomic,retain) NSString* orderId;
@property (nonatomic) NSInteger total;
@property (nonatomic,weak) UIViewController* backController;
@property (nonatomic,weak) NSObject<PaySuccessDelegate>* delegate;

@property (nonatomic) PayType payType;

@end
