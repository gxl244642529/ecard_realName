//
//  ValidateUtil.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ValidateUtil : NSObject
/**
 是否是电话号码
 */
+(BOOL)isPhoneNumber:(NSString*)mobileNum;

+(BOOL)isEmail:(NSString *)email;

+(BOOL)isIdCard:(NSString*)str;


+(BOOL)isPostCode:(NSString*)str;

@end
