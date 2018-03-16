//
//  NSDictionary+Values.m
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "NSDictionary+Values.h"
#import "IJsonValueObject.h"

@implementation NSDictionary(Values)
-(NSString*)getString:(NSString*)key{
    id value = [self objectForKey:key];
    if(!value)return nil;
    if(value == [NSNull null])return nil;
    if([value isKindOfClass:[NSString class]])return value;
    return [NSString stringWithFormat:@"%@",value];
}
-(NSInteger)getInteger:(NSString*)key{
    id value = [self objectForKey:key];
    if(!value)return 0;
    if(value == [NSNull null])return 0;
    return [value integerValue];
}


-(NSMutableArray*)parseArray:(NSString*)key class:(Class)clazz{
    NSMutableArray* result = [[NSMutableArray alloc]init];
    NSArray* arr = [self valueForKey:key];
    for (NSDictionary* dic in arr) {
        
        NSObject<IJsonValueObject>* value = [[clazz alloc]init];
        [value fromJson:dic];
        [result addObject:value];
    }
    return result;
    
}

@end
