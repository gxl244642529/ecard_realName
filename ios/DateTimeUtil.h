//
//  DateTimeUtil.h
//  ebusiness
//
//  Created by 任雪亮 on 16/10/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DateTimeUtil : NSObject


+(NSString*)dateToString:(NSDate*)date format:(NSString*)format;


+(NSString*)dateToString:(NSDate *)date;


@end
