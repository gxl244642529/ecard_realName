//
//  ECardCashierCell.m
//  ecardlib
//
//  Created by 任雪亮 on 16/9/25.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "ECardCashierCell.h"

@interface ECardCashierCell()

@property (weak, nonatomic) IBOutlet UIImageView *icon;
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UIButton *select;

@end

@implementation ECardCashierCell

- (void)awakeFromNib {
    [super awakeFromNib];
     self.selectionStyle = UITableViewCellSelectionStyleNone;
}

-(void)setData:(DMPay*)data{
    _title.text = data.title;
    _icon.image = data.icon;
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    [_select setSelected:!selected];
}

@end
