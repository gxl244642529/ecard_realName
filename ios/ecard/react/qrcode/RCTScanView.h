//
//  RCTScanView.h
//  ebusiness
//
//  Created by 任雪亮 on 16/11/11.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <React/UIView+React.h>
#import <AVFoundation/AVFoundation.h>
//设备宽/高/坐标
#define kDeviceWidth [UIScreen mainScreen].bounds.size.width
#define KDeviceHeight [UIScreen mainScreen].bounds.size.height
#define KDeviceFrame [UIScreen mainScreen].bounds

#define kLineMinY (KDeviceHeight / 2 - 100)
static const float kReaderViewWidth = 300;
static const float kReaderViewHeight = 300;
@interface RCTScanView : UIView<AVCaptureMetadataOutputObjectsDelegate>

@property (nonatomic, copy) RCTBubblingEventBlock onBarCodeRead;

-(void)stopSession;

-(void)startSession;
@end
