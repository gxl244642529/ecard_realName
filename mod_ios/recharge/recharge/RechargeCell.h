//
//  RechargeCell.h
//  ecard
//
//  Created by randy ren on 15/7/24.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RechargeCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *txtCardId;
@property (weak, nonatomic) IBOutlet UILabel *txtTime;
@property (weak, nonatomic) IBOutlet UILabel *txtPay;
@property (weak, nonatomic) IBOutlet UIButton *btnGo;
@property (weak, nonatomic) IBOutlet UIButton *btnCancel;

@end
