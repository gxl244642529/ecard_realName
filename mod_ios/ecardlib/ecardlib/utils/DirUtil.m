//
//  DirUtil.m
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "DirUtil.h"
#import "CacheUtil.h"
@implementation DirUtil


+(NSString*)getImageDir{
    return [CacheUtil getPath:@"image"];
}
+(NSString*)getHeadImagePath{
    return [[DirUtil getImageDir]stringByAppendingPathComponent:@"my_head.png"];
}
@end
