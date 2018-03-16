//
//  DateTimeUtil.m
//  ebusiness
//
//  Created by 任雪亮 on 16/10/28.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "DateTimeUtil.h"

@implementation DateTimeUtil
+(NSString*)dateToString:(NSDate*)date format:(NSString*)format{
  NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
  dateFormatter.dateFormat = format;
  return [dateFormatter stringFromDate:date];
}
+(NSString*)dateToString:(NSDate *)date{
  return [DateTimeUtil dateToString:date format:@"yyyy-MM-dd"];
}
@end
