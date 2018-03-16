//
//  IoUtil.m
//  ecard
//
//  Created by randy ren on 15/9/8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "IoUtil.h"
#import <ecardlib/ecardlib.h>
@implementation IoUtil
+(NSDictionary*)readJson:(NSString*)fileName{
    NSString* path =[[CacheUtil getPath:@"cache"] stringByAppendingPathComponent:fileName];
    if([[NSFileManager defaultManager]fileExistsAtPath:path]){
        NSData* data = [[NSFileManager defaultManager]contentsAtPath:path];
        NSError* error;
        NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:&error];
        if(!error){
            return resultMap;
        }
    }
    return nil;

}



+(BOOL)writeJson:(NSDictionary*)json toFile:(NSString*)fileName{

    NSError* error;
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:json
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    if(!error){
         NSString* path =[[CacheUtil getPath:@"cache"] stringByAppendingPathComponent:fileName];
        return [jsonData writeToFile:path atomically:YES];
    }
    return NO;
}

@end
