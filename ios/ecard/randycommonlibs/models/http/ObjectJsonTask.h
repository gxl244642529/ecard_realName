//
//  ObjectJsonTask.h
//  ecard
//
//  Created by randy ren on 14-4-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "JsonTask.h"
#import "IRequestResult.h"
#import "IObjectJsonTask.h"

@interface ObjectJsonTask : JsonTask<IObjectJsonTask>
{
}



@property (copy,readwrite,nonatomic) void(^successListener)(id result);



@end
