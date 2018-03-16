//
//  IDatabaseJsonTaskManager.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IJsonTaskManager.h"
#import "DbJsonTask.h"

@protocol IDatabaseJsonTaskManager <IJsonTaskManager>
-(DbJsonTask*)createDbJsonTask:(NSString*)api;
@end
