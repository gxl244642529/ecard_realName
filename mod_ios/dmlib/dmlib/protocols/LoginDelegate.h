//
//  LoginDelegate.h
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "UserInfo.h"

@protocol LoginDelegate <NSObject>

-(void)onLoginSuccess:(UserInfo*)userInfo;
-(void)onLoginCancel;

@end
