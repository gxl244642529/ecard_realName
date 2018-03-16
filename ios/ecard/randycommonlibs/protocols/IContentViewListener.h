//
//  IContentViewListener.h
//  ecard
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IContentViewListener <NSObject>
-(void)initContentView:(UIView*)contentView scrollView:(UIScrollView*)scrollView data:(id)data;
@end