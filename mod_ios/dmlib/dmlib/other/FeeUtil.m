//
//  FeeUtil.m
//  ecard
//
//  Created by randy ren on 16/2/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "FeeUtil.h"

@implementation FeeUtil
+(NSString*)formatFee:(NSInteger)fen{
    return [NSString stringWithFormat:@"%.02f",(float)fen/100];
}
@end
