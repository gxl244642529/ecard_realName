//
//  RCTDatePickerModule.m
//  ebusiness
//
//  Created by 任雪亮 on 16/10/27.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTDatePickerModule.h"
#import "DateTimeUtil.h"
#import "SRMonthPicker.h"


@implementation RCTDatePickerModule

RCT_EXPORT_MODULE();


RCT_EXPORT_METHOD(select:(NSString*)date title:(NSString*)title type:(NSString*)type opts:(NSDictionary*)opts callback:(RCTResponseSenderBlock)callback){
  
  dispatch_async(dispatch_get_main_queue(), ^{
    UIView* view = [[DMJobManager sharedInstance].topController.view findFirstResponsder];
    if(view){
      [view resignFirstResponder];
    }
    if([type isEqualToString:@"month"]){
      SRMonthPicker* picker = [[SRMonthPicker alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
      picker.yearFirst = YES;
      NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
      [dateFormatter setDateFormat: @"yyyy-MM"];
      NSDate *destDate= [dateFormatter dateFromString:date];
      if(destDate){
        picker.date = destDate;
      }
      
      [DMPopup bottom:picker title:title listener:^(SRMonthPicker* picker, BOOL ok) {
        if(ok){
          callback(@[[dateFormatter stringFromDate:picker.date]]);
        }
      }];
      
      
    }else{
      UIDatePicker* picker = [[UIDatePicker alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
      
      NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
      if([type isEqualToString:@"date"]){
        [dateFormatter setDateFormat: @"yyyy-MM-dd"];
        picker.datePickerMode = UIDatePickerModeDate;
        
        // dateFormatter dateFromString:<#(nonnull NSString *)#>
        
      }else if([type isEqualToString:@"datetime"]){
        [dateFormatter setDateFormat: @"yyyy-MM-dd HH:mm:ss"];
        picker.datePickerMode = UIDatePickerModeDateAndTime;
      }else if([type isEqualToString:@"time"]){
        picker.datePickerMode = UIDatePickerModeTime;
        [dateFormatter setDateFormat: @"HH:mm"];
      }
      
      NSString* minDate = [opts objectForKey:@"min"];
      if(minDate){
        picker.minimumDate = [dateFormatter dateFromString:minDate];
      }
      
      NSString* maxDate = [opts objectForKey:@"max"];
      if(maxDate){
        picker.maximumDate = [dateFormatter dateFromString:maxDate];
      }
      
      
      NSDate *destDate= [dateFormatter dateFromString:date];
      if(destDate){
        picker.date = destDate;
      }
      
      [DMPopup bottom:picker title:title listener:^(UIDatePicker* picker, BOOL ok) {
        if(ok){
          callback(@[ [dateFormatter stringFromDate:picker.date] ]);
        }
      }];
      
    }
    
  });
  
  
  
}

@end
