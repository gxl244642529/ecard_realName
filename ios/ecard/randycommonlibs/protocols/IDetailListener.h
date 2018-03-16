//
//  IDetailListener.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IDetailListener <NSObject>

-(void)onInitializeView:(UIView*)contentView listData:(NSObject*)listData detailData:(NSObject*)detailData;

@end
