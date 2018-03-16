//
//  CashierCell.h
//  ecard
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#include <DMLib/DMLib.h>

@interface CashierCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIButton *btnSelect;
@property (weak, nonatomic) IBOutlet UIImageView *icon;
@property (weak, nonatomic) IBOutlet UILabel *title;

-(void)setData:(id)data;
@end