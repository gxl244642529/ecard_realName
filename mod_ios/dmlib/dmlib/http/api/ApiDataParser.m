//
//  ApiDataParser.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ApiDataParser.h"
#import "DMApiJob.h"
@interface ApiDataParser ()
{
    id<DMApiParser> _parser;
}

@end

@implementation ApiDataParser
-(id)initWithParser:(id<DMApiParser>)parser{
    if(self = [super init]){
        _parser = parser;
    }
    return  self;
}

-(void)dealloc{
    _parser = nil;
}

-(id)parseData:(DMApiJob*)request data:(id)data error:(NSError**)error{
     NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:error];
    if(*error){
        return nil;
    }
    
    return [_parser parse:resultMap class:request.clazz];
}
@end
