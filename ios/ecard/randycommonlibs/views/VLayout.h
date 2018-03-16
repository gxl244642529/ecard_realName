//
//  VLayout.h
//  ecard
//
//  Created by randy ren on 15-1-17.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface VLayout : UIView
@property (nonatomic) NSInteger padding;
@property (nonatomic) NSInteger spacing;
-(float)height;
-(float)calcHeight;
@end
