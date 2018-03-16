//
//  SPayCell.h
//  ecard
//
//  Created by randy ren on 15/4/2.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "PayModel.h"
@interface SPayCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *title;

@property (weak, nonatomic) IBOutlet UIImageView *icon;

@property (weak, nonatomic) IBOutlet UIButton *btnSelected;

@property (nonatomic) PayType payType;
@end
