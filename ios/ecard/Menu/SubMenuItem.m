//
//  SubMenuItem.m
//  MXPullDownMenu
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import "SubMenuItem.h"

@interface SubMenuItem()
{
    __weak MenuData* _data;
}

@end

@implementation SubMenuItem

- (void)awakeFromNib
{
  [super awakeFromNib];
    // Initialization code
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}
-(void)setData:(MenuData*)data{
    _data = data;
    self.title.text = data.title;
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    _data.selected = selected;
    if(selected){
        [self setAccessoryType:UITableViewCellAccessoryCheckmark];
    }else{
        [self setAccessoryType:UITableViewCellAccessoryNone];
    }
}

@end
