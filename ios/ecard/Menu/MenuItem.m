//
//  MenuItem.m
//  MXPullDownMenu
//
//  Created by randy ren on 14-10-24.
//  Copyright (c) 2014年 Mx. All rights reserved.
//

#import "MenuItem.h"

@interface MenuItem()
{
    __weak MenuData* _data;
}

@end

@implementation MenuItem

- (void)awakeFromNib
{
  [super awakeFromNib];
    // Initialization code
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
}
-(void)setData:(MenuData*)data{
    _data = data;
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];
    if(selected){
        [self setAccessoryType:UITableViewCellAccessoryCheckmark];
    }else{
        [self setAccessoryType:UITableViewCellAccessoryNone];
    }
    _data.selected = selected;
    // Configure the view for the selected state
}

@end
