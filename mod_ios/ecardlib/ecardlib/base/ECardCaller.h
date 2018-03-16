//
//  ECardCaller.h
//  ecardlib
//
//  Created by 任雪亮 on 16/9/24.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import <UIKit/UIKit.h>



@interface ECardCaller : NSObject

+(void)setRootController:(UIViewController*)controller;

+(void)callECard:(UIViewController*)parent account:(NSString*)account;

+(UINavigationController*)createMain;

+(BOOL)handleOpenURL:(NSURL*)url;



@end
