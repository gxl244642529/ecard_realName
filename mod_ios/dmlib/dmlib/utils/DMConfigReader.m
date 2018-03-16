//
//  DMConfigReader.m
//  dmlib
//
//  Created by 任雪亮 on 16/9/26.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "DMConfigReader.h"
NSMutableDictionary* _config;

@interface DMConfigReader (){

}

@end

@implementation DMConfigReader
+(NSString*)getString:(NSString*)mainkey subkey:(NSString*)subkey{
    return _config[mainkey][subkey];
}
+(void)initialize{
    //加载配置,由程序的applicaiton.plist提供
    NSString *plistPath =  [[NSBundle mainBundle] pathForResource:@"application" ofType:@"plist"];
    NSMutableDictionary *config = [[NSMutableDictionary alloc] initWithContentsOfFile:plistPath];
    
    
    _config = config;
}

+(NSDictionary*)getMap:(NSString*)key{
    return _config[key];
}

+(NSString*)getString:(NSString*)key{
    return _config[key];
}
@end
