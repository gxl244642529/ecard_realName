//
//  ApiObjectParser.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ApiObjectParser.h"
#import "IJsonValueObject.h"
@implementation ApiObjectParser
-(id)parse:(NSDictionary *)resultMap class:(Class)clazz{
   
    id data = [resultMap valueForKey:@"result"];
    if(data == [NSNull null]){
        return nil;
    }
    if(clazz){
        NSObject<IJsonValueObject>* obj = [[clazz alloc]init];
        [obj fromJson:data];
        return obj;
        
    }
    return data;
}
@end
