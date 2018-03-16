//
//  DMModel.h
//  ecard
//
//  Created by 任雪亮 on 16/2/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMApiJob.h"

@interface DMModel : NSObject



-(DMApiJob*)createApi:(NSString*)api;

@end
