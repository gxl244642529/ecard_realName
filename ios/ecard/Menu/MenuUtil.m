//
//  MenuUtil.m
//  ecard
//
//  Created by randy ren on 15-1-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MenuUtil.h"


@implementation MenuUtil


+(NSArray*)createOrderArray{
    
    NSMutableArray* arr = [[NSMutableArray alloc]init];
    MenuData* data = [[MenuData alloc]init];
    data.title = @"更新时间从早到晚";
    data.ID = 1;
    data.selected = YES;
    [arr addObject:data];
    
    
    data = [[MenuData alloc]init];
    data.title = @"更新时间从早到晚";
    data.ID = 11;
    [arr addObject:data];
    
    
    
    data = [[MenuData alloc]init];
    data.title = @"按价格升序";
    data.ID = 2;
    [arr addObject:data];
    
    
    
    data = [[MenuData alloc]init];
    data.title = @"按价格降序";
    data.ID = 12;
    [arr addObject:data];
    
    
    
    data = [[MenuData alloc]init];
    data.title = @"按销量升序";
    data.ID = 3;
    [arr addObject:data];
    
    
    data = [[MenuData alloc]init];
    data.title = @"按销量降序";
    data.ID = 13;
    [arr addObject:data];
    
    return arr;
    
}
@end
