//
//  IRequestError.h
//  randycommonlibs
//
//  Created by randy ren on 14-7-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IJsonTask.h"


@protocol IRequestError <NSObject>
-(void)task:(id)task error:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError;
@end
