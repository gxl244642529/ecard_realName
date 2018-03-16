//
//  IJsonTask.h
//  eCard
//
//  Created by randy ren on 14-2-20.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//


#import "IRequestError.h"
#import "IJsonTask.h"


@protocol IRequestResult <IRequestError>

-(void)task:(id)task result:(id)result;

@end
