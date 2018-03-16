//
//  AbsPay.h
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "IPayAction.h"
#include "RPay.h"

@class  ECardRPayModel;

@interface AbsPay : NSObject<IPayActionListener>
{
     PayType _payType;
     __weak ECardRPayModel* _model;
    __weak UIViewController* _parent;
}

-(id)initWithModel:(ECardRPayModel*)model controller:(UIViewController*)parent;

-(PayType)payType;

-(BOOL)handleOpenUrl:(NSURL*)url;

@end
