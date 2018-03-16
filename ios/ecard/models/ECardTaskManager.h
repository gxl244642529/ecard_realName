//
//  ECardTaskManager.h
//  ecard
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "JsonTaskManager.h"
#import "CommonMacro.h"


#import "IViewControllerListener.h"
#import "OnClickListenerExt.h"
#define REQUEST_LOGIN 100
#define PLATFORM @"iphone"


@interface ECardTaskManager : JsonTaskManager


+(id<IJsonTaskManager>)sharedInstance;

+(void)setImageSrc:(UIImageView*)imageView src:(NSString*)url;




@end
