//
//  ApiArrayParser.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMApiArrayParser.h"
#import "IJsonValueObject.h"
@implementation DMApiArrayParser


-(id)parse:(NSDictionary *)resultMap class:(Class)clazz{
    NSArray* array = [resultMap valueForKey:@"result"];
    if(clazz){
        NSMutableArray* ret = [[NSMutableArray alloc]init];
        for (NSDictionary* dic in array) {
            NSObject<IJsonValueObject>* obj = [[clazz alloc]init];
            [obj fromJson:dic];
            [ret addObject:obj];
        }
        return ret;
    }
    return array;

}

@end
