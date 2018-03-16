//
//  DMColorConfig.h
//  DMLib
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 damai. All rights reserved.
//
#import <UIKit/UIKit.h>

@interface DMColorConfig : NSObject

+(UIColor*)itemHighlightColor;
+(UIColor*)tintColor;
+(UIColor*)item:(NSString*)key;
@end
