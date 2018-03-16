//
//  MyDelegate.h
//  libs
//
//  Created by randy ren on 16/1/10.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>
#import "DMViewController.h"
#import "DMJobManager.h"


@interface DMDelegate : UIResponder <UIApplicationDelegate>
/**
 初始化友盟5318134c56240bbc1b09deff
 */
-(void)initYoumen:(NSString*)key;
-(DMJobManager*)manager;
-(void)createWithServers:(NSArray*)servers rootController:(DMViewController*)rootController;
@end
