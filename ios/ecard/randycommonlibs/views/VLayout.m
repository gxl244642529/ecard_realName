//
//  VLayout.m
//  ecard
//
//  Created by randy ren on 15-1-17.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "VLayout.h"

@interface VLayout()
{
    float _height;
}

@end

@implementation VLayout

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.autoresizesSubviews = NO;
        _height = 0;
    }
    return self;
}

-(float)calcHeight{
    float height = 0;
    NSInteger padding = self.padding;
    NSInteger spacing = self.spacing;
    height = padding;
    for (UIView* view in self.subviews) {
        if(!view.hidden){
            
            if([view isKindOfClass:[VLayout class]]){
                VLayout* vLayout = (VLayout*)view;
                height += spacing + [vLayout calcHeight];
            }else{
               CGRect frame = view.frame;
                height +=spacing + frame.size.height;
            }

        }
    }
    return height;

}
-(void)awakeFromNib{
  [super awakeFromNib];
    self.autoresizesSubviews = NO;
    _height = 0;
}
-(float)height{
    return _height;
}

-(void)layoutSubviews{
    [super layoutSubviews];
 
    NSInteger padding = self.padding;
    NSInteger spacing = self.spacing;
    NSInteger pos = padding;
    for (UIView* view in self.subviews) {
        if(view.hidden){
             view.frame = CGRectMake(padding, padding, view.frame.size.width, view.frame.size.height);
        }else{
            CGRect frame = view.frame;
            if([view isKindOfClass:[VLayout class]]){
                VLayout* vLayout = (VLayout*)view;
                float height = [vLayout calcHeight];
                view.frame = CGRectMake(padding, pos, view.frame.size.width,height);
                pos += spacing+height;
            }else{
                view.center =CGPointMake(padding + frame.size.width/2, pos + frame.size.height/2);
                pos +=spacing + frame.size.height;
            }

        }
    }
    pos += padding;
    _height = pos;
  
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
