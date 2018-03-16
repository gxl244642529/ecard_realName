//
//  StringUtils.m
//  ecard
//
//  Created by randy ren on 15/12/9.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import "StringUtils.h"

@implementation StringUtils
+(BOOL)isEmpty:(NSString*)str{
    if([str isKindOfClass:[NSNull class]]){
        return true;
    }
    if(str==nil)return true;
    return [str isEqualToString:@""];
}
@end
