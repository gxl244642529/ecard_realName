//
//  SAddAddrView.h
//  ecard
//
//  Created by randy ren on 15-1-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import "VLayout.h"
#import "WhiteItemView.h"

@interface SAddAddrView : VLayout
@property (weak, nonatomic) IBOutlet ItemView *btnSelectArea;
@property (weak, nonatomic) IBOutlet UITextField *txtAddr;
@property (weak, nonatomic) IBOutlet UITextField *txtPhone;
@property (weak, nonatomic) IBOutlet UITextField *txtName;
@property (weak, nonatomic) IBOutlet UITextField *txtPostCode;
@property (weak, nonatomic) IBOutlet WhiteItemView *btnSetDef;
@property (weak, nonatomic) IBOutlet UILabel *detailAddr;
@property (weak, nonatomic) IBOutlet UIButton *chkDef;
@end
