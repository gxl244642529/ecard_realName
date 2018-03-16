//
//  CMBWebViewController.h
//  ecard
//
//  Created by 任雪亮 on 16/6/17.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>

@protocol CMBDelegate <NSObject>

-(void)onCMBSuccess:(NSArray*)info;
-(void)onCMBCancel;

@end

@interface CMBWebViewController : DMWebViewController<DMWebViewUrlParser,UIGestureRecognizerDelegate>
@property (weak,nonatomic) id<CMBDelegate> payDelegate;
@end
