//
//  ApiDataParser.h
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DataParser.h"
#import "DMApiParser.h"
@interface ApiDataParser : NSObject<DataParser>
-(id)initWithParser:(id<DMApiParser>)parser;
@end
