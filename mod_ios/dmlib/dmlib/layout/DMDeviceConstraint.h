//
//  DMDeviceConstraint.h
//  ecard
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>



//和屏幕有关的约束
IB_DESIGNABLE
@interface DMDeviceConstraint : NSLayoutConstraint

//段屏幕上面的比例
@property (nonatomic,assign) IBInspectable CGFloat shortRate;

//长屏上面的比例
@property (nonatomic,assign) IBInspectable CGFloat longRate;

@end
