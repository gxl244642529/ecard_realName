//
//  CartTitle.h
//  ecard
//
//  Created by randy ren on 15-1-22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <ecardlib/ecardlib.h>

#import "IArrayRequestResult.h"
@interface CartTitle : ItemView<IArrayRequestResult>

+(void)createWidthViewController:(UIViewController*)controller;

-(id)initWithViewController:(UIViewController*)controller;
-(void)updateCount;
-(void)setCount:(NSInteger)count;
@property (nonatomic,weak) UIViewController* parent;
@end
