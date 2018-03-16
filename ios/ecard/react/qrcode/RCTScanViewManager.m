//
//  RCTScanViewManager.m
//  ebusiness
//
//  Created by 任雪亮 on 16/11/11.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTScanViewManager.h"
#import "RCTScanView.h"

@interface RCTScanViewManager (){

  RCTScanView* _scanView;
}

@end

@implementation RCTScanViewManager


RCT_EXPORT_MODULE()
RCT_EXPORT_VIEW_PROPERTY(onBarCodeRead, RCTBubblingEventBlock)
-(void)dealloc{
  _scanView = nil;
}

- (UIView *)view
{
  if(!_scanView){
    _scanView =[[RCTScanView alloc]init];
   
  }
   [_scanView startSession];
  return _scanView;
}

@end
