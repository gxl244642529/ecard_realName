//
//  SCartCell.h
//  ecard
//
//  Created by randy ren on 15-1-15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import "SellingModel.h"

@class SCartCell;


@protocol ISelObserver <NSObject>

//选择状态改变
-(void)onSelectChange:(CartVo*)data;

//需要充值
-(void)onRequestRechage:(CartVo*)data current:(SCartCell*)cell;

@end

@interface SCartCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *image;
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UILabel *price;
@property (weak, nonatomic) IBOutlet UILabel *count;
@property (weak, nonatomic) IBOutlet UIButton *btnSub;
@property (weak, nonatomic) IBOutlet UIButton *btnAdd;
@property (weak, nonatomic) IBOutlet UIButton *btnRecharge;
@property (weak, nonatomic) IBOutlet UILabel *totalPrice;
@property (weak, nonatomic) IBOutlet UILabel *store;

@property (weak, nonatomic) IBOutlet TouchableView *btnChange;
@property (weak, nonatomic) IBOutlet UIButton *btnSwitch;

@property (weak,nonatomic) NSObject<ISelObserver>* delegate;

/**
 *  设置数据
 *
 *  @param data <#data description#>
 */
-(void)setData:(CartVo*)data;

@end
