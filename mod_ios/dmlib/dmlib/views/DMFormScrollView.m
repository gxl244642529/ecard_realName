//
//  DMFormScrollView.m
//  DMLib
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMFormScrollView.h"
#import "DMScrollView.h"
#import "DMViewUtil.h"
#import "UIView+ViewNamed.h"
#import "DMMacro.h"

@interface DMFormScrollView()
{
    __weak UIView* _contentView;
}

@end

@implementation DMFormScrollView

-(void)dealloc{
    _contentView = NULL;
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}
-(id)contentView{
    return _contentView;
}

-(void)awakeFromNib{
    [super awakeFromNib];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onChange:) name:UIKeyboardWillChangeFrameNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onHide) name:UIKeyboardDidHideNotification object:nil];
    _contentView = self.subviews[0];
}

-(id)initWithFrame:(CGRect)frame contentNibName:(NSString*)contentNibName{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onChange:) name:UIKeyboardWillChangeFrameNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onHide) name:UIKeyboardDidHideNotification object:nil];

        _contentView = [DMViewUtil createViewFormNibName:contentNibName bundle:CREATE_BUNDLE_WHEN_NOT_NULL(_bundleName) owner:self];
        _contentView.frame = CGRectMake(0, 0, [UIScreen mainScreen].bounds.size.width, _contentView.frame.size.height);
        [self addSubview:_contentView];
        self.contentSize = CGSizeMake(0, CGRectGetHeight(_contentView.frame));
    }
    return self;
    
}




-(void)onHide{
    [UIView animateWithDuration:0.25 animations:^{
        self.contentSize = CGSizeMake(0, CGRectGetHeight(_contentView.frame));
    }];
    
    
}

-(void)onChange:(NSNotification*)sender{
    NSValue *keyboardBoundsValue = [[sender userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardEndRect = [keyboardBoundsValue CGRectValue];
    float kbheight = keyboardEndRect.size.height;
    self.contentSize = CGSizeMake(0, CGRectGetHeight(_contentView.frame)+kbheight);
    
    
    UIView* first = [_contentView findFirstResponsder];
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
        self.contentOffset = CGPointMake(0, self.contentOffset.y  + frame.origin.y - bounds.size.height + frame.size.height);
    }];
    
    
    
}
@end

