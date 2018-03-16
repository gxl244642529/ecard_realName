//
//  SafePayResult.h
//  ecard
//
//  Created by randy ren on 16/2/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

@interface SafePayResult : NSObject<IJsonValueObject>

@property (nonatomic,copy) NSString* fee;
@property (nonatomic,copy) NSString* payTime;
@property (nonatomic,copy) NSString* piccOrderId;
@property (nonatomic,copy) NSString* noticeUrl;
@property (nonatomic,copy) NSString* protocolUrl;
@property (nonatomic,copy) NSString* serviceTel;

@end
