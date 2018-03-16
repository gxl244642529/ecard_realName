//
//  DMViewPropFinder.h
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//
#import <UIKit/UIKit.h>
#import "DMViewInfoCreater.h"
//视图属性查找
@interface DMViewPropFinder : NSObject

//根据class找到所有的,对于实体类
+(NSMutableArray<DMViewInfo*>*)findAllViewInfo:(UIView*)view class:(Class)clazz;


@end
