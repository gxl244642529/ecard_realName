//
//  DMErrorView.m
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMErrorView.h"
#import "DMViewUtil.h"
#import "DMMacro.h"

@implementation DMErrorView


- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.backgroundColor = [UIColor whiteColor];
        AddTapGestureRecognizer(self, onTap);
    }
    return self;
}

-(void)onTap{
    [_loadingDelegate onLoadingRefresh:self];
}

-(void)layoutSubviews{
    [super layoutSubviews];
    UIView* view = [self viewWithTag:1];
    if(view)
        view.center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
}
-(void)setError:(NSString*)error isNetworkError:(BOOL)isNetworkError{
    CGPoint center = CGPointMake(self.frame.size.width/2, self.frame.size.height/2);
    if(isNetworkError){
        
        UILabel* label = [DMViewUtil createLabel:@"数据获取失败" parent:self size:14 bold:YES gray:NO];
        label.center = CGPointMake(center.x, center.y+20);
        [self addSubview:label];
        label = [DMViewUtil createLabel:@"请检查网络后点击重新加载" parent:self size:12 bold:NO gray:YES];
        label.center = CGPointMake(center.x, center.y+40);
        [self addSubview:label];
        
        UIImageView* imageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"ecardlibbundle.bundle/ic_network_error.png"]];
        imageView.center =CGPointMake(center.x, center.y-40);
        [self addSubview:imageView];
        
    }else{
        UIImageView* imageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"ecardlibbundle.bundle/ic_load_error.png"]];
        imageView.tag = 1;
        imageView.center =CGPointMake(center.x, center.y-40);
        [self addSubview:imageView];
        
    }
}
@end
