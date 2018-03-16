//
//  TabView.m
//  eCard
//
//  Created by randy ren on 13-11-12.
//  Copyright (c) 2013年 citywithincity. All rights reserved.
//

#import "TabView.h"
@interface TabView()
{
    UIButton* _currentItem;
    __weak NSObject<TabViewDelegate>* _delegate;
}
@end

@implementation TabView

-(void)dealloc{
    _currentItem = NULL;
    _delegate = NULL;
}
//按钮
-(id)initWithFrame:(CGRect)frame buttons:(NSArray*)buttons;
{
    if(self=[super initWithFrame:frame])
    {
        [self setBackgroundColor:[UIColor clearColor]];
        NSInteger index=100;
        for (UIButton* button in buttons)//; <#condition#>; <#increment#>) {
        {
            [self addSubview:button];
            button.tag = index++;
            [button addTarget:self action:@selector(onClick:) forControlEvents:UIControlEventTouchUpInside];
        }
    }
    return self;
}



-(void)onClick:(UIButton*)sender
{
    [self setCurrentItem:sender];
    
    [_delegate tabDidSelected:self index:sender.tag-100];
}

-(void)setCurrentItem:(UIButton*)item
{
    if(item != _currentItem)
    {
        [_currentItem setSelected:FALSE];
        [item setSelected:TRUE];
        _currentItem = item;
    }

}


-(void)setDelegate:(NSObject<TabViewDelegate>*)delegate
{
    _delegate = delegate;
}

-(void)setCurrentSelectIndex:(NSInteger)index notify:(BOOL)notify
{
    UIButton* button = (UIButton*)[self viewWithTag:index+100];
    [self setCurrentItem:button];
    if(notify)
    {
        [_delegate tabDidSelected:self index:index];
    }
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
