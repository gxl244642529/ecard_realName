//
//  DMCheckItem.m
//  ecard
//
//  Created by randy ren on 16/2/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMCheckItem.h"

@implementation DMCheckItem

-(void)awakeFromNib{
    [super awakeFromNib];
    for (__kindof UIView* view in self.subviews) {
        if([view isKindOfClass:[UIButton class]]){
            _button = view;
            break;
        }
    }
    
    [self setTarget:self withAction:@selector(onTouch:)];
}


-(void)onTouch:(id)sender{
    _button.selected = !_button.selected;
}


@end
