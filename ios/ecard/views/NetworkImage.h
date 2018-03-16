//
//  NetworkImage.h
//  ecard
//
//  Created by randy ren on 15/4/23.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "JsonTaskManager.h"
#import <DMLib/DMLib.h>

@class NetworkImage;

@protocol NetworkImageDelete <NSObject>

-(void)networkImageDidLoaded:(NetworkImage*)imageView;

@end

@interface NetworkImage : DMNetworkImage




@property (nonatomic,weak) id<NetworkImageDelete> delegate;

@end
