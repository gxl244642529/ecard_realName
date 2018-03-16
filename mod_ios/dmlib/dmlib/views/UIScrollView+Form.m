//
//  UIScrollView+Form.m
//  DMLib
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "UIScrollView+Form.h"
#import <objc/runtime.h>
#import "UIView+ViewNamed.h"

@interface ScrollViewForm : NSObject

-(id)initWithScrollView:(UIScrollView*)scrollView;

@end

@implementation UIScrollView(Form )
static char* UIScrollViewForm = "UIScrollViewForm";
-(void)form{
    objc_setAssociatedObject(self, UIScrollViewForm, [[ScrollViewForm alloc]initWithScrollView:self], OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}


@end


@interface ScrollViewForm()
{
    __weak UIScrollView* _scrollView;
    NSInteger _height;
}

@end

@implementation ScrollViewForm

-(void)dealloc{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}


-(id)initWithScrollView:(UIScrollView*)scrollView{
    if(self = [super init]){
        //注意在初始化之前必须要先有contentView
        _scrollView = scrollView;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onWillShow:) name:UIKeyboardWillShowNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onWillHide) name:UIKeyboardWillHideNotification object:nil];
        
    }
    return self;
}

-(void)onWillHide{
    [UIView animateWithDuration:0.25 animations:^{
        _scrollView.contentSize = CGSizeMake(0, _height);
        _height = 0;
    }];
}

-(void)onWillShow:(NSNotification*)sender{
    NSValue *keyboardBoundsValue = [[sender userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardEndRect = [keyboardBoundsValue CGRectValue];
    float kbheight = keyboardEndRect.size.height;
   
    UIView* first = [_scrollView findFirstResponsder];
    if(!_height){
        _height =_scrollView.contentSize.height;
    }
    _scrollView.contentSize = CGSizeMake(0, _height + kbheight);
    
   // _scrollView.contentSize = CGSizeMake(_contentSize.width, _contentSize.height+kbheight);
    //first的位置是否在屏幕之内
    CGRect frame = [first convertRect:first.bounds toView:nil];
    CGRect bounds = [UIScreen mainScreen].bounds;
    bounds = CGRectMake(0, 0, bounds.size.width, bounds.size.height-kbheight);
    //计算位置,如果frame在上半个屏幕之内,则不用动,如果不在,则需要调整scrollview的位置
    if(CGRectContainsRect(bounds, frame)){
        return;
    }
    //这里进行调整
    [UIView animateWithDuration:0.25 animations:^{
        _scrollView.contentOffset = CGPointMake(0, _scrollView.contentOffset.y  + frame.origin.y - bounds.size.height + frame.size.height);
    }];
    
    
    
}
@end
