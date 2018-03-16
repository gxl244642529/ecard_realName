//
//  Constants.h
//  ecard
//
//  Created by randy ren on 13-11-7.
//  Copyright (c) 2013年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#import <DMLib/DMLib.h>

@interface Constants : NSObject

+(NSString*)formatUrl:(NSString*)url;
+(NSString*)formatUrl:(NSString*)url part:(NSString*)part;

+(NSString*)parseTime:(NSString*) strTime;
+(NSString*)parseDate:(NSString*) strDate;



@end

#define PACKAGE @"com.citywithincity.ecard"
#define PLATFORM @"iphone"
//全局notification




//退出程序销毁




