//
//  DMControllerDelegate.h
//  ecard
//
//  Created by randy ren on 16/2/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol DMControllerDelegate <NSObject>

-(void)controllerWillFinish:(UIViewController*)controller;

@end
@protocol DMControllerDelegateContainer <NSObject>

@property (nonatomic,weak) NSObject<DMControllerDelegate>* controllerDelegate;

@end
