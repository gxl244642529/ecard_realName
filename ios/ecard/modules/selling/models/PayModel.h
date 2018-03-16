//
//  PayModel.h
//  ecard
//
//  Created by randy ren on 15-2-5.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JsonTaskManager.h"
#import "CommonMacro.h"
#import "RPay.h"




@interface PayModel : NSObject<IRequestResult>
DECLARE_SHARED_INSTANCE_DIRECT(PayModel)

@property (nonatomic) PayType type;
@property (nonatomic,weak) NSObject<PayDelegate>* delegate;
-(void)onPaySuccess:(id)successResult;
-(void)pay:(id)ID type:(PayType)type;
-(void)onPayCancel;
-(void)cancel;
@end
