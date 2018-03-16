//
//  AddToCartView.h
//  ecard
//
//  Created by randy ren on 15-1-30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ToggleButtonGroup.h"
#import "PopupWindow.h"



@interface AddToCartView : UIView
@property (weak, nonatomic) IBOutlet UIButton *btnBuy;
@property (weak, nonatomic) IBOutlet ToggleButtonGroup *container;
@property (weak, nonatomic) IBOutlet UILabel *txtCount;
@property (weak, nonatomic) IBOutlet UILabel *title;
@property (weak, nonatomic) IBOutlet UIImageView *image;
@property (weak, nonatomic) IBOutlet UILabel *txtStore;
@property (weak, nonatomic) IBOutlet UILabel *txtPrice;

@property (nonatomic) NSInteger store;

-(void)setCount:(NSInteger)count;
-(void)setRecharge:(NSInteger)recharge;


-(NSInteger)getCount;
-(NSInteger)getRecharge;



+(void)show:(UIViewController*)parent count:(NSInteger)count recharge:(NSInteger)recharge target:(NSObject*)target selector:(SEL)selector title:(NSString*)title data:(id)data store:(NSInteger)store;

-(void)setParam:(NSObject*)target selector:(SEL)selector;

@end
