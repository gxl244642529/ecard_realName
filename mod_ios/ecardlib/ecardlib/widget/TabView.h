//
//  TabView.h
//  eCard
//
//  Created by randy ren on 13-11-12.
//  Copyright (c) 2013年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol TabViewDelegate <NSObject>

-(void)tabDidSelected:(id)tab index:(NSInteger)index;

@end

@interface TabView : UIView

//按钮
-(id)initWithFrame:(CGRect)frame buttons:(NSArray*)buttons;

-(void)setCurrentSelectIndex:(NSInteger)index notify:(BOOL)notify;

-(void)setDelegate:(NSObject<TabViewDelegate>*)delegate;

@end
