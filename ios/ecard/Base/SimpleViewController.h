//
//  SimpleViewController.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MyViewController.h"


@interface SimpleViewController : MyViewController

-(id)initWithListener:(NSObject<IViewControllerListener>*)listener;
@end
