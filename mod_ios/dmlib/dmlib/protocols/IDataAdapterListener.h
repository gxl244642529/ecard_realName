//
//  IDataAdapterListener.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@protocol IDataAdapterListener <NSObject>
-(void)onInitializeView:(UIView*)parent cell:(UIView*)cell data:(NSObject*)data index:(NSInteger)index;
@end
