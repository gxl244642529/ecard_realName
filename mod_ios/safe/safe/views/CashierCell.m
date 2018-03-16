//
//  CashierCell.m
//  ecard
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "CashierCell.h"

@implementation CashierCell

- (void)awakeFromNib {
    [super awakeFromNib];
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}

-(void)setData:(id)data{
    _title.text = [data valueForKey:@"title"];
    _icon.image = [data valueForKey:@"icon"];
}
- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    [_btnSelect setSelected:!selected];
}

@end
