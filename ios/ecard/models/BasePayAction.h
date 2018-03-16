//
//  BasePayAction.h
//  ecard
//
//  Created by randy ren on 15/8/28.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "RPay.h"

@interface BasePayAction : NSObject<IPayAction>
{
    NSString* _prePayApi;
    NSString* _notifyServerApi;
}
-(id)initWithPrePay:(NSString*)prePayApi notifyServerApi:(NSString*)notifyServerApi;

@end
