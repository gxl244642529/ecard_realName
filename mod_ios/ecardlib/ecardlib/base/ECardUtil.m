//
//  ECardUtil.m
//  ecard
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardUtil.h"

@implementation ECardUtil
+(NSString*)parseTime:(NSString*) strTime {
    return [NSString stringWithFormat:@"%@-%@-%@ %@:%@",
            [strTime substringWithRange:NSMakeRange(0,4)],
            [strTime substringWithRange:NSMakeRange(4,2)],
            [strTime substringWithRange:NSMakeRange(6,2)],
            [strTime substringWithRange:NSMakeRange(8,2)],
            [strTime substringWithRange:NSMakeRange(10,2)]
            ];
}


+(NSString*)parseDate:(NSString*) strDate {
    if(strDate == [NSNull null]){
        return nil;
    }
    if(strDate.length < 8){
        return nil;
    }
    return [NSString stringWithFormat:@"%@-%@-%@",
            [strDate substringWithRange:NSMakeRange(0,4)],
            [strDate substringWithRange:NSMakeRange(4,2)],
            [strDate substringWithRange:NSMakeRange(6,2)]
            ];
}


@end
