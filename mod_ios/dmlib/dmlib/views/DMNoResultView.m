//
//  DMNoResultView.m
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMNoResultView.h"
#import "DMViewUtil.h"
#import "DMMacro.h"
@implementation DMNoResultView

#ifdef DEBUG
-(void)dealloc{
    NSLog(@"%@ dealloc",self);
}
#endif

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.backgroundColor = [UIColor whiteColor];
        
        UILabel* label = [DMViewUtil createLabel:@"没有搜索结果" parent:self];
        label.tag = 1;
        
        label = [DMViewUtil createLabel:@"点击刷新" parent:self];
        label.tag = 2;
         AddTapGestureRecognizer(self, onTap);
    }
    return self;
}


-(void)onTap{
    [_loadingDelegate onLoadingRefresh:self];
}

-(void)layoutSubviews{
    [super layoutSubviews];
    [self viewWithTag:1].center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2 - 15);
    [self viewWithTag:2].center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2 + 15);
}

@end
