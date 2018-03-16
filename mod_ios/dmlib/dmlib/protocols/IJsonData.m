//
//  IJsonData.m
//  ecard
//
//  Created by randy ren on 16/2/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "IJsonData.h"

@implementation DMJsonData
+(NSArray*)toArray:(NSArray*)arr{
    NSMutableArray* result = [[NSMutableArray alloc]initWithCapacity:arr.count];
    
    for(id<IJsonData> data in arr){
        [result addObject: [data toJson]];
    }
    
    return result;
}
@end
