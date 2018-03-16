//
//  GoodPickHeadView.h
//  ecard
//
//  Created by randy ren on 14-7-16.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ReturnCardView.h"
#import "LostCardInfo.h"

#import <ecardlib/ecardlib.h>

@interface GoodPickHeadView : UIView
@property (weak, nonatomic) IBOutlet UITextField *txtCardNumber;
@property (weak, nonatomic) IBOutlet UIButton *btnQuery;
@property (weak, nonatomic) IBOutlet LostCardInfo *lostCardInfo;
@property (weak, nonatomic) IBOutlet ReturnCardView *returnCardView;
@property (weak, nonatomic) IBOutlet UIButton *btnSaveLostCard;
@property (weak, nonatomic) IBOutlet UITextField *txtLostPhone;
@property (weak, nonatomic) IBOutlet ItemView *btnPickTime;
@property (weak, nonatomic) IBOutlet UILabel *txtPickTime;
@property (weak, nonatomic) IBOutlet ItemView *btnHelp;

@end
