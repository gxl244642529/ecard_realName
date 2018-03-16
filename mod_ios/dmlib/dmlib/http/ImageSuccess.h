//
//  ImageSuccess.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMHttpJob.h"

@interface ImageSuccess : NSObject<DMJobSuccess>

-(id)initWithDelegate:(id<DMJobFinish>)delegate;

@end
