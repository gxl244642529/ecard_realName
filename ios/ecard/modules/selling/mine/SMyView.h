//
//  SMyView.h
//  ecard
//
//  Created by randy ren on 15/3/31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <ecardlib/ecardlib.h>
#import "OrderModel.h"
@interface SMyView : UIView<IArrayRequestResult,OnResumeUserInfo>


-(void)setParent:(UIViewController*)parent;
-(UIViewController*)parent;


-(void)onViewWillAppear;

@end
