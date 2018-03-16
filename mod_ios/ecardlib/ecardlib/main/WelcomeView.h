//
//  WelcomeView.h
//  ecard
//
//  Created by randy ren on 15/4/24.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ViewPager.h"
#import "ScrollCtrl.h"

@protocol WelcomeViewDelegate <NSObject>

-(void)onWelcomeComplete;

@end



@interface WelcomeView : UIView<ViewPagerDelegate>

@property (nonatomic,weak) NSObject<WelcomeViewDelegate>* delegate;

@end
