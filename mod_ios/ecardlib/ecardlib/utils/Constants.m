//
//  Constants.m
//  ecard
//
//  Created by randy ren on 13-11-7.
//  Copyright (c) 2013年 citywithincity. All rights reserved.
//

#import "Constants.h"


@implementation Constants

+(NSString*)formatUrl:(NSString*)url
{
    return [DMServers formatUrl:0 url:url];
}


+(NSString*)formatUrl:(NSString*)url part:(NSString*)part{
    
    return [NSString stringWithFormat:@"%@%@%@",[DMServers getUrl:0],url,part];
}



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
    return [NSString stringWithFormat:@"%@-%@-%@",
            [strDate substringWithRange:NSMakeRange(0,4)],
            [strDate substringWithRange:NSMakeRange(4,2)],
            [strDate substringWithRange:NSMakeRange(6,2)]
            ];
}




@end
