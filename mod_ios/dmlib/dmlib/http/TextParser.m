//
//  TestParser.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "TextParser.h"
#import "CommonUtil.h"
#import "DataUtil.h"
#import "DMHttpJob.h"
@implementation TextParser


-(id)parseData:(DMHttpJob*)request data:(NSData *)data error:(NSError *__autoreleasing *)error{
    return [DataUtil getString:data];
}

@end
