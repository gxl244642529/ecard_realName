//
//  ApiNotifier.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMJobManager.h"
#import "DMApiDelegate.h"
@interface ApiNotifier : NSObject<DMApiDelegate>


-(id)initWithTaskManager:(DMJobManager*)taskManager;

@end
