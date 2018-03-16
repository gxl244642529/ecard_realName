//
//  RealInfo.h
//  ecard
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 citywithincity. All rights reserved.
//
#import <DMLib/DMLib.h>

@interface RealInfo : NSObject<IJsonValueObject>

@property (nonatomic,copy) NSString* idCard;
@property (nonatomic,copy) NSString* name;
@property (nonatomic,copy) NSString* phone;
@property (nonatomic,assign) BOOL isValid;

@end
