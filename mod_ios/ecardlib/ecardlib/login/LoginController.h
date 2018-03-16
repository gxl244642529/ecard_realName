//
//  LoginController.h
//  ecard
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <DMLib/DMLib.h>


#define kLoginSuccess @"kLoginSuccess"

@interface LoginController : DMViewController<DMLoginController,DMSubmitDelegate,DMApiDelegate>
@property (nonatomic,weak) id<DMLoginDelegate> delegate;
@end
