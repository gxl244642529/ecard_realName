//
//  LoginImageHandler.h
//  ecard
//
//  Created by randy ren on 15/9/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "BaseImageHandler.h"


#import <dmlib/dmlib.h>

@interface LoginImageHandler : BaseImageHandler<DMJobDelegate>

-(id)initWithDefaultImage:(NSString *)defaultImage localImage:localImage key:(NSString*)key  type:(int)type  cornor:(BOOL)cornor;

@end
