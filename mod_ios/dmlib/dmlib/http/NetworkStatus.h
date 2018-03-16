//
//  NetworkStatus.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSInteger {
    NotReachable = 0,
    ReachableViaWiFi,
    ReachableViaWWAN
} NetworkStatus;