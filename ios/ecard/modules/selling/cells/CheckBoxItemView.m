//
//  CheckBoxItemView.m
//  ecard
//
//  Created by randy ren on 15/4/15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "CheckBoxItemView.h"

@interface CheckBoxItemView()
{
    UIButton* _item;
}

@end

@implementation CheckBoxItemView

-(void)dealloc{
    
}
-(void)setSelected:(BOOL)selected{
    _item.selected = selected;
}
-(BOOL)selected{
    return _item.selected;
}


-(void)onSel:(id)sender{
    [self setSelected:!self.selected];
}

-(void)awakeFromNib{
  [super awakeFromNib];
    _item = (UIButton*)[self viewWithTag:100];
    [self setTarget:self withAction:@selector(onSel:)];
}
@end
