//
//  DMConfigReader.h
//  dmlib
//
//  Created by 任雪亮 on 16/9/26.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DMConfigReader : NSObject

//value
+(NSString*)getString:(NSString*)key;
+(NSDictionary*)getMap:(NSString*)key;
+(NSString*)getString:(NSString*)mainkey subkey:(NSString*)subkey;
@end
