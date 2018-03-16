//
//  SafeCounterView.h
//  ecard
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <DMLib/dmlib.h>


@protocol SafeCounterViewDelegate <NSObject>

-(void)onCountChanged:(NSInteger)count;

@end

@interface SafeCounterView : UIView<DMValue>

@property (nonatomic,weak) id<SafeCounterViewDelegate> delegate;

-(NSInteger)count;
-(void)setCount:(NSInteger)count;

@end
