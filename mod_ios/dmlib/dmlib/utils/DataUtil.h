//
//  DataUtil.h
//  test_dev
//
//  Created by randy ren on 16/1/6.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DataUtil : NSObject

+(NSString*)getString:(NSData*)data;
+(NSInteger)getInteger:(id)data key:(NSString*)key;
+(NSString*)getString:(id)data key:(NSString*)key;

+(NSDictionary*)arrayToDictionary:(NSArray*)arr;


@end
