//
//  TabIndicator.h
//  ecard
//
//  Created by randy ren on 16/1/4.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface TabIndicator : UIView

-(void)setItems:(NSInteger)items;
-(void)setCurrentItem:(NSInteger)index;
-(void)setCurrentItem:(NSInteger)index animation:(BOOL)animation;
@end
