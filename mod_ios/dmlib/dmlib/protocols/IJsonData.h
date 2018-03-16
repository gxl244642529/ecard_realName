//
//  IJsonData.h
//  ecard
//
//  Created by randy ren on 16/2/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IJsonData <NSObject>

-(NSDictionary*)toJson;

@end

@interface DMJsonData : NSObject


+(NSArray*)toArray:(NSArray*)arr;

@end
