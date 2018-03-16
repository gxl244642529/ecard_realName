//
//  IFilter.h
//  ecard
//
//  Created by randy ren on 15-2-2.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IFilter <NSObject>

-(BOOL)accept:(NSObject*)data;

@end
