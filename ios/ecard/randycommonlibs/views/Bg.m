//
//  Bg.m
//  eCard
//
//  Created by randy ren on 14-1-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "Bg.h"


@interface Bg()
{

}

@end

@implementation Bg

-(void)dealloc{
    self.bg = nil;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    if(!_bg){
        _bg = @"bg_ecard_info";
    }
     UIImage *backgroundImage = [UIImage imageNamed:_bg];
     backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(6, 6,6, 6)];
     UIImageView* svRect = [[UIImageView alloc] initWithImage:backgroundImage];
    svRect.frame=self.bounds;
    svRect.tag = 8888;
    [self insertSubview:svRect atIndex:0];
}

-(void)layoutSubviews{
    [super layoutSubviews];
    [self repaint];
}

-(void)repaint
{
    UIImageView* svRect =(UIImageView*)[self viewWithTag:8888];
    if(!svRect){
        svRect = [[UIImageView alloc] initWithFrame:self.bounds];
        svRect.tag = 8888;
        [self insertSubview:svRect atIndex:0];
        if(!_bg){
            _bg = @"bg_ecard_info";
        }
        UIImage *backgroundImage = [UIImage imageNamed:_bg];
        backgroundImage = [backgroundImage resizableImageWithCapInsets:UIEdgeInsetsMake(6, 6,6, 6)];
        [svRect setImage:backgroundImage];
    }else{
        svRect.frame = self.bounds;
    }
    
    
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
