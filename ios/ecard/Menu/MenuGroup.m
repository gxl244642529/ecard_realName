//
//  MenuGroup.m
//  ecard
//
//  Created by randy ren on 15/3/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MenuGroup.h"

#define TITLE_HEIGHT 0
#define MENU_HEIGHT 37

@interface MenuGroup()
{
    NSMutableArray* _menus;
    NSMutableArray* _triggers;
    TouchableView* _backGroundView;
    //加载菜单的父元素
    __weak UIView* _parent;
    
    __weak MenuView* _currentMenu;
}

@end

@implementation MenuGroup

-(void)dealloc{
    _menus = nil;
    _backGroundView = nil;
}

-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent{
    if(self = [super init]){
        _menus = [[NSMutableArray alloc]init];
        _parent = parent;
        _triggers = [[NSMutableArray alloc]init];
        // 创建背景
        CGRect frame = [UIScreen mainScreen].bounds;
        
        _backGroundView = [[TouchableView alloc] initWithFrame:CGRectMake(0,TITLE_HEIGHT+MENU_HEIGHT,frame.size.width,frame.size.height - TITLE_HEIGHT - MENU_HEIGHT)];
        _backGroundView.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.0];
        _backGroundView.opaque = NO;
        
        [_backGroundView setTarget:self withAction:@selector(tapBackGround:)];
    }
    return  self;
}


- (void)tapBackGround:(id)sender
{
    
   [self hideBackground];
}

-(void)setData:(NSArray*)data index:(NSInteger)index{
    MenuView* view = _menus[index];
    [view setData:data];
}

-(MenuView*)createSingleMenu:(ItemView*)triggerView{
    triggerView.tag = _menus.count;
    [triggerView setTarget:self withAction:@selector(onTriggerMenu:)];
    // 创建背景
    CGRect frame = [UIScreen mainScreen].bounds;
    MenuView* view = [[MenuView alloc]initWithFrame:CGRectMake(0,TITLE_HEIGHT+MENU_HEIGHT,frame.size.width,frame.size.height - TITLE_HEIGHT - MENU_HEIGHT)];
    view.index = triggerView.tag;
    [view setDelegate:self];
    [_menus addObject:view];
    [_triggers addObject:triggerView];
    return view;
}


-(TreeMenuView*)createTreeMenu:(ItemView*)triggerView{
    triggerView.tag = _menus.count;
    [triggerView setTarget:self withAction:@selector(onTriggerMenu:)];
    // 创建背景
    CGRect frame = [UIScreen mainScreen].bounds;
    TreeMenuView* view = [[TreeMenuView alloc]initWithFrame:CGRectMake(0,TITLE_HEIGHT+MENU_HEIGHT,frame.size.width,frame.size.height - TITLE_HEIGHT - MENU_HEIGHT)];
    view.index = triggerView.tag;
    [view setDelegate:self];
    [_menus addObject:view];
    [_triggers addObject:triggerView];
    return view;
}

-(MenuView*)getMenu:(NSInteger)index{
    return _menus[index];
}

-(void)onClickMenu:(MenuData *)menuData index:(NSInteger)index{
     MenuView* menu = _menus[index];
    [menu removeFromSuperview];
    _currentMenu = nil;
    [self hideBackground];
    
    
    UIView* trigger = _triggers[index];
    UILabel* label = trigger.subviews[0];
    if(menuData.realTitle){
        label.text = menuData.realTitle;
    }else{
        label.text = menuData.title;
    }
    
    [label sizeToFit];
    label.center = CGPointMake(label.superview.bounds.size.width/2, label.superview.bounds.size.height/2);
    
    
    [self.delegate onClickMenu:menuData index:index];

}

-(void)onTriggerMenu:(ItemView*)view{
    NSInteger index = view.tag;
    MenuView* menu = _menus[index];
    //如果两次点击的index一样,则背景消失,如不是同一个,则另一个消失
    if(_currentMenu){
        if(_currentMenu==menu){
            [menu removeFromSuperview];
            [self hideBackground];
            _currentMenu = nil;
        }else{
            [_currentMenu removeFromSuperview];
            [_parent addSubview:menu];
            _currentMenu = menu;
        }
    }else{
        [self showBg];
        [_parent addSubview:menu];
        _currentMenu = menu;
    }
}


-(void)showBg{
    //背景出现
    [_parent addSubview:_backGroundView];
    [UIView animateWithDuration:0.2 animations:^{
        _backGroundView.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.3];
    }];
}
//
-(void)hideBackground{
    //菜单消失
    [UIView animateWithDuration:0.2 animations:^{
        _backGroundView.backgroundColor = [UIColor colorWithWhite:0.0 alpha:0.0];
    } completion:^(BOOL finished) {
        [_backGroundView removeFromSuperview];
    }];
}


@end
