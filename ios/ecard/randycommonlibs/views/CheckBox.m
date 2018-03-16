//
//  CheckBoxItemView.m
//  ecard
//
//  Created by randy ren on 15/4/15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "CheckBox.h"

@interface CheckBox()
{
    UIButton* _item;
}

@end

@implementation CheckBox

-(void)dealloc{
    _item = nil;
}
-(void)setSelected:(BOOL)selected{
    if(selected!=_item.selected){
        _item.selected = selected;
        if(self.delegate){
            [self.delegate checkBox:self valueChanged:selected];
        }
    }
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
