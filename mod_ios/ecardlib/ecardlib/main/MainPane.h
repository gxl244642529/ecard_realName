//
//  MainPane.h
//  ecard
//
//  Created by randy ren on 16/1/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <DMLib/dmlib.h>

@interface MainPane : UIView

@property (weak, nonatomic) IBOutlet DMButton *btnBack;
@property (weak, nonatomic) IBOutlet DMButton *btnBusiness;
@property (weak, nonatomic) IBOutlet DMButton *btnNet;
@property (weak, nonatomic) IBOutlet DMButton *btnQuestion;

@property (nonatomic,weak) UIButton* icon;
@property (nonatomic,weak) UIImageView* bg;

-(void)makeSmall;
-(void)makeBig;
-(void)hideButtons;
-(void)showButtons;

@end
