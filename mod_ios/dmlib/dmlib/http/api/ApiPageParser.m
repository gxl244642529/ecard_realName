//
//  ApiPageParser.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ApiPageParser.h"
#import "IJsonValueObject.h"
#import "DMPage.h"


@implementation ApiPageParser


-(id)parse:(NSDictionary *)resultMap class:(Class)clazz{
    NSArray* array = [resultMap valueForKey:@"result"];
    if(clazz){
        NSMutableArray* ret = [[NSMutableArray alloc]init];
        for (NSDictionary* dic in array) {
            NSObject<IJsonValueObject>* obj = [[clazz alloc]init];
            [obj fromJson:dic];
            [ret addObject:obj];
        }
        array = ret;
    }
    DMPage* page = [[DMPage alloc]init];
    page.page = [[resultMap valueForKey:@"page"]integerValue];
    page.total = [[resultMap valueForKey:@"total"]integerValue];
    id extra = [resultMap objectForKey:@"extra"];
    if(extra!=nil){
        if(extra != [NSNull null]){
            page.extra =extra;
        }
    }
  
    page.list = array;
    return page;
}

@end
