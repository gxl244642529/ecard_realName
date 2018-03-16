//
//  SOrderCardView.h
//  ecard
//
//  Created by randy ren on 15-1-23.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import "CardModel.h"
@interface SOrderCardView : TouchableView
@property (weak, nonatomic) IBOutlet UIImageView *image;
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UILabel *recharge;
@property (weak, nonatomic) IBOutlet UILabel *price;
@property (weak, nonatomic) IBOutlet UILabel *count;


-(void)setData:(CartVo*)cartVo;

@end
