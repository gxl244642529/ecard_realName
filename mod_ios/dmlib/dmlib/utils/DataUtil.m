//
//  DataUtil.m
//  test_dev
//
//  Created by randy ren on 16/1/6.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DataUtil.h"

@implementation DataUtil
+(NSString*)getString:(NSData*)data{
    return [[NSString alloc] initWithData:data  encoding:NSUTF8StringEncoding];
}

+(NSInteger)getInteger:(id)data key:(NSString*)key{
    
    id value = [data objectForKey:key];
    if(!value)return 0;
    if(value == [NSNull null])return 0;
    return [value integerValue];
}


+(NSString*)getString:(id)data key:(NSString*)key{
    id value;
    if([data respondsToSelector:@selector(objectForKey:)]){
        value = [data objectForKey:key];
    }else{
        value = [data valueForKey:key];
    }
    if(!value)return nil;
    if(value == [NSNull null])return nil;
    if([value isKindOfClass:[NSString class]])return value;
    return [NSString stringWithFormat:@"%@",value];
}


+(NSDictionary*)arrayToDictionary:(NSArray*)arr{
    NSMutableDictionary* result = [[NSMutableDictionary alloc]init];
    for (NSString* key in arr) {
        result[key]=@1;
    }
    return result;
}


@end
