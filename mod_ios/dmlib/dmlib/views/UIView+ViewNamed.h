//
//  UIView+ViewNamed.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>




IB_DESIGNABLE
@interface UIView(ViewNamed)

-(__kindof UIViewController*)findViewController;

@property (nonatomic,retain) IBInspectable NSString* viewName;
-(NSLayoutConstraint*)findHeight;
-(NSLayoutConstraint*)findWidth;

-(__kindof UIView*)findFirstResponsder;

-(void)removeAllSubviews;
@property (nonatomic) CGFloat top;
@property (nonatomic) CGFloat bottom;
@property (nonatomic) CGFloat right;
@property (nonatomic) CGFloat left;

@property (nonatomic) CGFloat width;
@property (nonatomic) CGFloat height;

//快照
-(UIImage*)takeSnapshot;

@end
