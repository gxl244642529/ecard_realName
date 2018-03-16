//
//  SignUtil.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "SignUtil.h"
#import "CommonUtil.h"

@implementation SignUtil
+(NSString*)sign:(NSMutableDictionary*)dic key:(NSString*)key files:(NSMutableDictionary*)files{
     NSMutableString* sb = [[NSMutableString alloc]init];
    [self toMap:sb param:dic files:files path:nil];
    [sb appendString:key];
    return [CommonUtil md5:sb];
}



+(void)toMap:(NSMutableString*)sb param:(NSMutableDictionary*)param files:(NSMutableDictionary*)files path:(NSString*)path{
    NSArray *myKeys = [param allKeys];
    NSArray *sortedKeys = [myKeys sortedArrayUsingSelector:@selector(compare:)];
    
    for (NSString* key in sortedKeys) {
       [sb appendString:key];
        id value  =[param valueForKey:key];
        if([value isKindOfClass:[NSData class]]){
            [files setValue:value forKey:[self getPath:path key:key]];
            [param removeObjectForKey:key];
            [sb appendString:@"null"];
            continue;
        }
        
        if([value isKindOfClass:[NSArray class]]){
            int count = [SignUtil toList:sb arr:value files:files path:[self getPath:path key:key]];
            if(count){
                
                NSMutableArray* arr = [[NSMutableArray alloc]init];
                for(int i=0; i < count; ++i){
                    [arr addObject:[NSNull null]];
                    [sb appendString:@"null"];
                }
               [param setValue:arr forKey:key];
            }
        }else{
            if([value isKindOfClass:[NSNumber class]]){
                const char* objcType = [value objCType];
                switch (objcType[0]) {
                    case 'i':
                    case 'f':
                    case 'd':
                    case 'q':
                    case 'l':
                        [sb appendFormat:@"%@",value];
                        continue;
                    default:
                        [sb appendString:[value boolValue] ? @"true" : @"false"];
                        continue;
                }
            }
            
            [sb appendFormat:@"%@",value];
        }
    }
    
}

+(NSString*)getPath:(NSString*)path key:(NSString*)key{
    if(!path){
        return key;
    }
    return [NSString stringWithFormat:@"%@.%@",path,key];
}

+(NSString*)getPath:(NSString*)path index:(NSInteger)index{
    if(!path){
        return [NSString stringWithFormat:@"[%d]",index];
    }
    return [NSString stringWithFormat:@"%@[%d]",path,index];
}

+(NSInteger)toList:(NSMutableString*)sb arr:(NSArray*)arr files:(NSMutableDictionary*)files path:(NSString*)path{
    NSInteger arrayFile = 0;
    NSInteger index = 0;
    for (id value in arr) {
        if([value isKindOfClass:[NSDictionary class]]){
            [self toMap:sb param:value files:files path:[self getPath:path index:index]];
        }else if([value isKindOfClass:[NSData class]]){
            //肯定是一个array
            arrayFile++;
            [files setValue:value forKey:[self getPath:path index:index]];
        }
        ++index;
    }
    //这里对原来的做出处理
    
    return arrayFile;
}
@end
