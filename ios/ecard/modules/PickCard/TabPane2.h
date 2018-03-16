//
//  TabPane2.h
//  ecard
//
//  Created by randy ren on 15/4/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import "TabPaneView.h"

#import "TabPaneDelegate.h"

@interface TabPane2 : UIView<ViewPagerDelegate>


-(void)setTitles:(NSArray*)arr;
-(void)setCurrentIndex:(NSInteger)index;

@property (nonatomic,weak) id<TabPaneViewDelegate2> delegate;


@end
