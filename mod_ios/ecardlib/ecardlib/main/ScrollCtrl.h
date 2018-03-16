//
//  ScrollCtrl.h
//  ecard
//
//  Created by randy ren on 15-1-21.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ScrollCtrl : UIView

-(id)initWithItems:(NSInteger)items;
-(void)setItems:(NSInteger)items;
-(void)setCurrentItem:(NSInteger)index;


@end
