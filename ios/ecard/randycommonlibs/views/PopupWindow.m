//
//  PopupWindow.m
//  ecard
//
//  Created by randy ren on 15/3/28.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "PopupWindow.h"
#import <ecardlib/ecardlib.h>

__weak PopupWindow* _currentWindow;

@interface PopupWindow()
{
     TouchableView* _background;
}

@end

@implementation PopupWindow
+(void)hide{
    if(_currentWindow){
        [_currentWindow remove];
        _currentWindow = nil;
    }
}
+(UIView*)contentView{
    return _currentWindow.contentView;
}
-(void)dealloc{
    _contentView = NULL;
    _background = NULL;
}
-(void)show{
    // adding some styles to background view (behind table alert)
    self.backgroundColor = [UIColor clearColor];
    self.clipsToBounds = YES;
    
    _background = [[TouchableView alloc]initWithFrame:self.bounds];
    [self addSubview:_background];
    [_background setTarget:self withAction:@selector(tapTableview:)];
    
    _background.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.0];
    _background.opaque = NO;
    // adding it as subview of app's UIWindow
    UIWindow *appWindow = [[UIApplication sharedApplication] keyWindow];
    [appWindow addSubview:self];
    
    [self addSubview:self.contentView];
    // get background color darker
    [UIView animateWithDuration:0.2 animations:^{
        _background.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.25];
        self.endState(self.contentView);
    }];

}

+(PopupWindow*)show:(UIView*)contentView beforeState:(void (^)(UIView* contentView))beforeState endState:(void (^)(UIView* contentView))endState  frame:(CGRect)frame{
    PopupWindow* window = [[PopupWindow alloc]init];
    window.autoresizesSubviews = NO;
    window.autoresizingMask = UIViewAutoresizingNone;
    window.frame = frame;
    window.contentView = contentView;
    window.beforeState = beforeState;
    window.endState = endState;
    [window show];
    _currentWindow = window;
    return window;
}


-(void)tapTableview:(id)sender{
    if(!self.notAutoHide){
        [self remove];
    }
}


-(void)remove{
    [UIView animateWithDuration:0.2 animations:^{
        _background.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.25];
        
        self.beforeState(self.contentView);
        
    } completion:^(BOOL finished) {
        if(finished){
            if(self.onRemove!=nil){
                self.onRemove();
            }
            [self removeFromSuperview];
        }
    }];
}

@end
