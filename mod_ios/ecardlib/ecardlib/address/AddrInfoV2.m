//
//  AddrInfo.m
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "AddrInfoV2.h"
#import <DMLib/DMLib.h>

@implementation AddrInfoV2
-(void)fromJson:(NSDictionary *)json{
    
    self.name = [json valueForKey:@"pName"];
    self.id = [[json valueForKey:@"id"]integerValue];
    
    
    id arr = [json valueForKey:@"list"];
    if(arr && ![arr isKindOfClass:[NSNull class]]){
        arr = [AddrInfoV2 parseJsonArray:arr class:[AddrInfoV2 class]];
        self.list = arr;
    }
    
}
+(NSMutableArray*)parseJsonArray:(NSArray*)src class:(Class)clazz{
    NSMutableArray* ret = [[NSMutableArray alloc]init];
    for (NSDictionary* dic in src) {
        NSObject<IJsonValueObject>* obj = [[clazz alloc]init];
        [obj fromJson:dic];
        [ret addObject:obj];
    }
    return ret;
}

@end
