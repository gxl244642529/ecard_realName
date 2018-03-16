//
//  DMItem.m
//  DMLib
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMItem.h"
#import <DMLib/dmlib.h>

#import "DMConfigReader.h"
#import "DMColorUtil.h"

@interface DMItem()
{
    __weak id _delegate;
    SEL _selector;
    BOOL _isTouched;
    
}
@end

@implementation DMItem


-(void)awakeFromNib{
    [super awakeFromNib];
    _itemNormalColor = self.backgroundColor;
    if(!_highlightColor){
        _highlightColor =  [DMColorUtil colorWithString:[DMConfigReader getString:@"colors" subkey:@"itemHighlight"]] ;; // [DMColorConfig itemHighlightColor];
    }
   
    if(_controllerName || _segueName){
        _currentController = [self findViewController];
        [self setTarget:self withAction:@selector(onTouch:)];
    }
    
    
}


-(DMPopType)onLoginSuccess{
    
    
    [_currentController.navigationController popToViewController:_currentController animated:YES];
    [self trigger];
    
    return DMPopByDelegate;

}

-(void)trigger{
    //这里登录成功
    if(_controllerName){
        Class clazz = NSClassFromString(_controllerName);
        UIViewController* controller = [[clazz alloc]init];
        [_currentController.navigationController pushViewController:controller animated:YES];
    }else{
        [_currentController performSegueWithIdentifier:_segueName sender:self];
    }

}


-(void)onTouch:(id)sender{
    if(_checkLogin && ![DMAccount current]){
        //这里需要打开登录界面
        [DMAccount callLoginController:self];
    }else{
        [self trigger];
    }
}

-(void)setTarget :(id)target withAction:(SEL)selector{
    _delegate=target;
    _selector=selector;
}


-(void)setNormalColor:(UIColor*)normalColor{
    _itemNormalColor = normalColor;
}

-(id)init{
    if(self=[super init]){
        _itemNormalColor = [UIColor clearColor];
        _highlightColor =  [DMColorUtil colorWithString:[DMConfigReader getString:@"colors" subkey:@"itemHighlight"]] ;; // [DMColorConfig itemHighlightColor];

    }
    return self;
}


-(id)initWithFrame:(CGRect)frame{
    if(self=[super initWithFrame:frame]){
        _itemNormalColor = [UIColor clearColor];
        _highlightColor =  [DMColorUtil colorWithString:[DMConfigReader getString:@"colors" subkey:@"itemHighlight"]] ;; // [DMColorConfig itemHighlightColor];
    }
    return self;
}



-(void)dealloc{
    _itemNormalColor = NULL;
    _highlightColor = nil;
}



-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    _isTouched = YES;
    
    [self setBackgroundColor:_highlightColor];
}


-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    [self setBackgroundColor:_itemNormalColor];
    if(_isTouched)
    {
        
        _isTouched = NO;
        PerformSelector(_delegate, _selector, self);
    }
    
    
}

-(void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event{
    [self setBackgroundColor:_itemNormalColor];
    if(_isTouched)
    {
        
        _isTouched = NO;
    }
    
}

@end
