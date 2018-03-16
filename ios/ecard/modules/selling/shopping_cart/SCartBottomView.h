//
//  SCartBottomView.h
//  ecard
//
//  Created by randy ren on 15-1-16.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>

@interface SCartBottomView : UIView
@property (weak, nonatomic) IBOutlet UIButton *btnOrder;
@property (weak, nonatomic) IBOutlet UILabel *price;
@property (weak, nonatomic) IBOutlet ItemView *btnDel;

@end
