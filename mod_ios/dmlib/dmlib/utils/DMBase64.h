//
//  Base64.h
//  ecard
//
//  Created by randy ren on 15/9/4.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <UIKit/UIKit.h>

@interface DMBase64 : NSObject
+(NSString*)encodeURL:(NSString *)string;
+(NSString*)encodeString:(NSString*)src;

+(NSString*)encodeImage:(UIImage*)image;

@end
