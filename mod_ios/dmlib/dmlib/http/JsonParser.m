//
//  JsonParser.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "JsonParser.h"
#import "DMHttpJob.h"
@implementation JsonParser
-(id)parseData:(DMHttpJob*)task data:(NSData*)data error:(NSError *__autoreleasing *)error{
    return [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:error];
}
@end
