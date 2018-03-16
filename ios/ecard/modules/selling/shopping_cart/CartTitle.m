//
//  CartTitle.m
//  ecard
//
//  Created by randy ren on 15-1-22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "CartTitle.h"
#import "CartController.h"
#import "SaleUtil.h"
#import "ECardTaskManager.h"
#import "CardModel.h"
@interface CartTitle()
{
    UIButton* _button;
}

@end



@implementation CartTitle

-(void)dealloc{
    _button = nil;
}


+(void)createWidthViewController:(UIViewController*)controller{
    CartTitle* title = [[CartTitle alloc]initWithViewController:controller];
    [title setTarget:title withAction:@selector(onGotoCart:)];
    controller.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:title];
}


-(void)onFirstLoginGotoCart:(id)sender{
    [self updateCount];
    [self onLoginGotoCart:sender];
}

-(void)onLoginGotoCart:(id)sender{
    CartController* controller = [[CartController alloc]init];
    controller.shopController = [SaleUtil root];
    [self.parent.navigationController pushViewController:controller animated:YES];
}

-(void)onGotoCart:(id)sender{
    
    if([[JsonTaskManager sharedInstance]isLogin]){
        [self onLoginGotoCart:nil];
    }else{
        [[ECardTaskManager sharedInstance]checkLogin:self parent:self.parent loginSuccess:@selector(onFirstLoginGotoCart:)];
    }
    
}
-(void)updateCount{
    if([[JsonTaskManager sharedInstance]isLogin]){
        [[CartModel sharedInstance]getList:self];
    }else{
        [[CartModel sharedInstance]addObserver:self];
    }
}
-(void)task:(id)task result:(NSArray *)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    [self setCount:result.count];
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [self setCount:0];
}

-(id)initWithViewController:(UIViewController*)controller{
    CGRect frame = CGRectMake(0, 0, 45, 45);
    if(self=[super initWithFrame:frame]){
        [self setCount:0];
        [self updateCount];
        
        self.parent = controller;
        UIImageView* imageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"s_ic_cart_title"]];
        [self addSubview:imageView];
        imageView.frame = CGRectMake(0,0, 21, 19);
        imageView.center = CGPointMake(22, 22);
        _button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 12, 12)];
        [_button setBackgroundImage:[UIImage imageNamed:@"s_ic_count"] forState:UIControlStateNormal];
        [_button.titleLabel setFont:[UIFont systemFontOfSize:10]];
        [_button.titleLabel setTextColor:[UIColor whiteColor]];
        _button.userInteractionEnabled = NO;
        [self addSubview:_button];
        _button.center = CGPointMake(30, 12);
    }
    return self;
}




-(void)setCount:(NSInteger)count{
    [_button setTitle:[NSString stringWithFormat:@"%ld",(long)count] forState:UIControlStateNormal];
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
