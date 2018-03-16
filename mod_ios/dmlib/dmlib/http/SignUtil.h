//
//  SignUtil.h
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface SignUtil : NSObject

+(NSString*)sign:(NSMutableDictionary*)dic key:(NSString*)key files:(NSMutableDictionary*)files;

@end
