//
//  TabPaneView.h
//  eCard
//
//  Created by randy ren on 13-11-17.
//  Copyright (c) 2013年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import "TabPaneDelegate.h"


@interface TabPaneView : NSObject<UIScrollViewDelegate,TabViewDelegate>

-(void)setCurrentSelected:(NSInteger)index notify:(BOOL)nofity;

-(void)setDelegate:(NSObject<TabPaneViewDelegate>*)delegate;

- (id)initWithParam:(TabView*)tabView scrollView:(UIScrollView*)scrollView;
//增加滚动面板
-(void)addSubPane:(UIView*)pane;
-(void)increaseSubPane;
//下一个framett
-(CGRect)getNextFrame;

@property (nonatomic) CGRect scrollFrame;

@end
