//
//  Bg.h
//  eCard
//
//  Created by randy ren on 14-1-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


IB_DESIGNABLE
@interface Bg : UIView


-(void)repaint;


@property (nonatomic,retain) IBInspectable NSString* bg;

@end
