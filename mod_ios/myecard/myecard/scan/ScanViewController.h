//
//  ScanViewController.h
//  ecard
//
//  Created by randy ren on 15/4/14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//
#import <DMLib/DMLib.h>


#import <AVFoundation/AVFoundation.h>
extern int REQUEST_SCAN;

@class ScanViewController;

/**
 *  扫描成功后调用
 */
@protocol SCanDelegate <NSObject>

-(void)onScan:(ScanViewController*)controller data:(NSString*)data;

@end


@interface ScanViewController : DMViewController<AVCaptureMetadataOutputObjectsDelegate>
{
    //设置扫描画面
    
    
}

-(void)resumeScan;
-(id)initWithListener:(NSObject<SCanDelegate>*)listener;

@end
