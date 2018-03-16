//
//  RCTQrViewManager.m
//  ecard
//
//  Created by 任雪亮 on 16/12/5.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTQrViewManager.h"

#import "RCTQrView.h"

@implementation RCTQrViewManager
RCT_EXPORT_MODULE()

- (UIView *)view
{
 
  RCTQrView* view = [[RCTQrView alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
  
  
  
  
  return view;
}


RCT_EXPORT_VIEW_PROPERTY(content, NSString*)

@end
