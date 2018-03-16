//
//  SDiyFController.h
//  ecard
//
//  Created by randy ren on 15-2-13.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "SellingModel.h"
#import "CardModel.h"
#import "SDiyFController.h"

@protocol SDiyFControllerDelegate <NSObject>

-(void)editFComplete:(SDiyPagesVo*)data;

@end

@interface SDiyFController : MyViewController<IArrayRequestResult>

@property (nonatomic,weak) NSObject<SDiyFControllerDelegate>* delegate;

@end
