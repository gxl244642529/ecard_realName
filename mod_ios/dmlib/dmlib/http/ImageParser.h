//
//  ImageParser.h
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DataParser.h"
#import "ImageErrors.h"

@interface ImageParser : NSObject<DataParser>


-(id)initWithLock:(NSObject*)lock error:(ImageErrors*)error;


@end
