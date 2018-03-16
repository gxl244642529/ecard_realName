//
//  IoUtil.h
//  ecard
//
//  Created by randy ren on 15/9/8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface IoUtil : NSObject

/**
 从文件加载
 */
+(NSDictionary*)readJson:(NSString*)fileName;

+(BOOL)writeJson:(NSDictionary*)json toFile:(NSString*)fileName;

@end
