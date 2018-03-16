//
//  SDiyDetailView.h
//  ecard
//
//  Created by randy ren on 15-1-26.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <ecardlib/ecardlib.h>

@interface SDiyDetailView : UIView
@property (weak, nonatomic) IBOutlet ViewPager *viewPager;
@property (weak, nonatomic) IBOutlet ScrollCtrl *ctrl;
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UILabel *author;
@property (weak, nonatomic) IBOutlet UILabel *time;

@end
