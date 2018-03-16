//
//  RCTScreenModule.m
//  ecard
//
//  Created by 任雪亮 on 17/5/12.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTScreenModule.h"
#import <UIKit/UIKit.h>

@interface RCTScreenModule(){
  NSTimer *timer;
  CGFloat _value;
  CGFloat _currentValue;
  NSInteger _count;
  CGFloat _interval;
}

@end

#define TIME_INTERVAL 0.02

@implementation RCTScreenModule

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(setValue:(NSInteger)value){
  
  dispatch_async(dispatch_get_main_queue(), ^{
      _currentValue =[UIScreen mainScreen].brightness * 100;
    _value = value;
    _count = 0;
    _interval = (float)( _value - _currentValue) / (1/TIME_INTERVAL);
    if(timer){
      [timer invalidate];
      timer = nil;
    }
    timer = [NSTimer scheduledTimerWithTimeInterval:TIME_INTERVAL target:self selector:@selector(action:) userInfo:nil repeats:YES];
    
  });
  
}


-(void)action:(id)sender{
  _count +=2 ;
  CGFloat _cur = _currentValue + _interval * _count;
  [[UIScreen mainScreen] setBrightness:  (float)_cur / 100  ];
  if(_count >= (1/TIME_INTERVAL)){
    [timer invalidate];
    timer = nil;
  }
}

RCT_EXPORT_METHOD(getValue:(RCTResponseSenderBlock)callback){
  dispatch_async(dispatch_get_main_queue(), ^{
    
 

    
    CGFloat currentLight = [UIScreen mainScreen].brightness;
    callback(@[ [NSNumber numberWithInteger:  currentLight * 100 ]  ]);

  });
}

@end
