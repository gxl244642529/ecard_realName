//
//  SOrderConfirmController.h
//  ecard
//
//  Created by randy ren on 15/4/1.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "BaseViewController.h"
#import "CardModel.h"
#import "SellingPayController.h"
@interface SOrderConfirmController : BaseViewController<IDataAdapterListener,PaySuccessDelegate,IRequestResult>


@property (nonatomic,weak) UIViewController* backController;
@property (nonatomic) ConfirmType type;
@property (nonatomic,retain) SAddrListVo* addr;
@property (nonatomic) CardType cardtype;
@property (nonatomic) float transFee;
@property (nonatomic) float totalFee;

-(void)updateAddr;

@end
