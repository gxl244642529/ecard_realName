//
//  SafeCounterView.h
//  ecard
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMValue.h"


@protocol DMCounterViewDelegate <NSObject>

-(void)onCountChanged:(NSInteger)count;

@end

@interface DMCounterView : UIView<DMValue>

@property (nonatomic,weak) id<DMCounterViewDelegate> delegate;

-(NSInteger)count;
-(void)setCount:(NSInteger)count;

@end
