//
//  MainView.h
//  ecard
//
//  Created by randy ren on 16/2/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MainPane.h"


@interface MainView : UIView<UIScrollViewDelegate>

@property (weak, nonatomic) IBOutlet UIImageView *headBg;
@property (weak, nonatomic) IBOutlet UIView *bgButton;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *headCon;
@property (weak, nonatomic) IBOutlet UIImageView *headView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *con3;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *con2;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *con1;

@property (weak, nonatomic) IBOutlet MainPane *mainPane;

-(void)updateMargin;
@end
