//
//  DMTabContainer.m
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMTabContainer.h"
#import "DMJobManager.h"
#import "UIView+ViewNamed.h"


@interface DMTabContainer()
{
    NSArray* _names;
    NSMutableArray<UIView*>* _views;
    __weak UIView* _currentView;
    __weak UIViewController* _currentController;
}

@end

@implementation DMTabContainer

-(void)dealloc{
    _names = nil;
    _views = nil;
}

-(void)awakeFromNib{
    _currentController = [DMJobManager sharedInstance].topController;
   _names = [_viewNames componentsSeparatedByString:@","];
    _views = [[NSMutableArray alloc]initWithCapacity:_names.count];
    //先初始化第一个视图
    dispatch_async(dispatch_get_main_queue(), ^{
        
        _currentView = [self createView:0];
        [_currentController setTitle:_currentView.viewName];
        
         [_views addObject:_currentView];
        for(NSInteger i =1 ; i < _names.count; ++i){
            [_views addObject: (UIView*)[NSNull null]];
        }
    });
}

-(UIView*)createView:(NSInteger)index{
    Class clazz = NSClassFromString(_names[index]);
    UIViewController* controller = [[clazz alloc]init];
    [_currentController addChildViewController:controller];
    controller.view.frame = self.bounds;
    [self addSubview:controller.view];
    return controller.view;

}

-(void)layoutSubviews{
    [super layoutSubviews];
    _currentView.frame = self.bounds;
}

-(void)tab:(id)tab didSelectIndex:(NSInteger)index{
    //切换到
    _currentView.hidden = YES;
    id view = _views[index];
    if(view == [NSNull null]){
        _views[index] =[self createView:index];
    }else{
        [view setHidden:NO];
    }
    _currentView = _views[index];
    [_currentController setTitle:_currentView.viewName];
    
}
@end
