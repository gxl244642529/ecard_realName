//
//  JsonTaskManager.h
//  randycommonlibs
//
//  Created by randy ren on 14-7-20.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//


#import "IJsonTaskManager.h"
#import "INotLoginListener.h"
#import "IDestroyable.h"
#import "DiskCache.h"
#import "MemCache.h"
#import "CommonMacro.h"

#import <DMLib/dmlib.h>





@interface JsonTaskManager : NSObject<IDestroyable,IJsonTaskManager,DMLoginDelegate>
{
   
}
+(BOOL)isEmpty:(id)str;

+(NSObject<IJsonTaskManager>*)sharedInstance;

+(void)setInstance:(id)instance;
//同步
+(void)setImageSrcSync:(UIImageView*)image src:(NSString*)src;

+(void)setImageSrcDirect:(UIImageView*)image src:(NSString*)src;

+(void)setImagePath:(NSString*)path image:(UIImageView*)image;


-(void)doLogout;

/**
 开始
 */
-(void)uploadDeviceToken:(NSString*)deviceToken;




@end
