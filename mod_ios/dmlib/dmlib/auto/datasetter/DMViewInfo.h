//
//  DMViewInfo.h
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//
#import <UIKit/UIKit.h>

#import "DMDataSetter.h"



/**
 * 视图信息
 */
@interface DMViewInfo : NSObject

@property (nonatomic,assign) NSInteger tag;
//数据字段名称
@property (nonatomic,copy) NSString* name;

//是否应该处理
-(BOOL)shouldHandle:(NSDictionary*)properties;

-(id<DMDataSetter>)createSetter:(UIView*)parentView;

@end
