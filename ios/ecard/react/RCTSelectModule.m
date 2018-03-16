//
//  RCTSelectModule.m
//  ecard
//
//  Created by 任雪亮 on 16/7/3.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTSelectModule.h"



@implementation RCTSelectModule
RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(select:(NSArray*)titles
                  selectedIndex:(NSInteger)selectedIndex
                  title:(NSString*)title
                  callback:(RCTResponseSenderBlock)callback)
{
 
  dispatch_async(dispatch_get_main_queue(), ^{
    //打开选择的界面
    //键盘小时
    UIView* view = [[DMJobManager sharedInstance].topController.view findFirstResponsder];
    if(view){
      [view resignFirstResponder];
    }
    [DMPopup select:titles selectedIndex:selectedIndex title:title listener:^(NSInteger index, NSString *title) {
      NSString* sel = [NSString stringWithFormat:@"%d",(int)index];
      callback(@[sel]);
    }];
  });
 

}
/*
(void)select:(NSArray<NSString*>*)titles selectedIndex:(NSInteger)selectedIndex title:(NSString*)title listener:BLOCK_PARAM(listener,NSInteger index,NSString* title);
*/
@end
