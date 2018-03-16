//
//  TouchableView.m
//  eCard
//
//  Created by randy ren on 14-1-26.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "TouchableView.h"
#import <dmlib/dmlib.h>

@interface TouchableView()
{
    BOOL checkLogin;
    
    __weak UIViewController* _parent;
}
@property (weak,nonatomic) id delegate;
@property (nonatomic) SEL selector;

@end

@implementation TouchableView


-(void)dealloc
{
    self.delegate =NULL;
    self.selector = NULL;
}

-(void)setTarget :(id)target withAction:(SEL)selector
{
    _delegate=target;
    _selector=selector;
    checkLogin = FALSE;
}
-(void)setLoginTarget:(id)target parent:(UIViewController*)parent withAction:(SEL)selector{
    _delegate=target;
    _parent = parent;
    _selector=selector;
    checkLogin = TRUE;
}

-(void)setLoginTarget:(UIViewController*)target withAction:(SEL)selector{
    _parent = target;
    _delegate=target;
    _selector=selector;
    checkLogin = TRUE;
}
-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    _isTouched = YES;
}

-(void)touchesCancelled:(NSSet *)touches withEvent:(UIEvent *)event
{
    _isTouched=NO;
}

-(void)onLoginSuccess:(id)sender{
    PerformSelector(_delegate, _selector, self);
}

-(void)touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
   
    
    if(_isTouched)
    {
        if(checkLogin){
            if([DMAccount isLogin]){
                 PerformSelector(_delegate, _selector, self);
            }else{
                [DMAccount callLoginController:nil];
            }
          
            
        }else{
           PerformSelector(_delegate, _selector, self);
        }
        
    }
     _isTouched=NO;
}


@end
