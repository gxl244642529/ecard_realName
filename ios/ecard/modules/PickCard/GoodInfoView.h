//
//  GoodInfoView.h
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RadioButtonGroup.h"
#import <ecardlib/ecardlib.h>
@interface GoodInfoView : UIView

@property (weak, nonatomic) IBOutlet ItemView *btnHelp;
@property (weak, nonatomic) IBOutlet UIButton *btnOk;
@property (nonatomic,retain) RadioButtonGroup* sex;
@property (weak, nonatomic) IBOutlet UITextView *txtInfo;
@property (weak, nonatomic) IBOutlet UITextField *txtPhone;

@end
