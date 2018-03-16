//
//  JsonUtil.h
//  ecard
//
//  Created by randy ren on 15/5/13.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface JsonUtil : NSObject

+(NSInteger)getInteger:(NSDictionary*)dic key:(NSString*)key;
+(double)getDouble:(NSDictionary*)dic key:(NSString*)key;
+(NSString*)getString:(NSDictionary*)dic key:(NSString*)key;

+(NSString*)getString:(NSDictionary*)dic key:(NSString*)key defaultValue:(NSString*)value;
@end
