//
//  BaseViewController.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import "IRequestResult.h"
#import "IArrayRequestResult.h"
#import "IData.h"
#import "MyViewController.h"
#import "JsonTaskManager.h"

#define RESULT_OK 0
#define RESULT_CANCEL -1
@interface BaseViewController : MyViewController
{
    NSInteger _requstCode;
}



-(void)setRequestController:(BaseViewController*)controller requestCode:(NSInteger)requestCode data:(NSObject *)data modal:(BOOL)modal;

-(void)openControllerForResult:(BaseViewController*)controller requestCode:(NSInteger)requestCode data:(NSObject *)data modal:(BOOL)modal;

-(void)openController:(BaseViewController*)controller data:(NSObject *)data;

-(void)setResult:(NSInteger)resultCode data:(NSObject *)data;
-(void)setResult:(NSInteger)resultCode data:(NSObject *)data animate:(BOOL)animate;
/**
 重载
 */
-(void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSObject *)data;
-(void)openModalController:(UIViewController*)controller;


-(BOOL)isModal;
@end
