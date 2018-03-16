//
//  DMValue.h
//  DMLib
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

//直接设置值的元素,一般用于单一的值
//值为 简单值:字符串\实体类\NSDictionary
//文本框,标签,checkbox,radiogroup,
//日期\时间选取器
//地区选取器
//地理位置定位
@protocol DMValue <NSObject>

-(id)val;
-(void)setVal:(id)val;

@end



@interface DMValue : NSObject

@end
