//
//  DMFormValidateUtil.h
//  dmlib
//
//  Created by 任雪亮 on 17/1/20.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DMFormValidateUtil : NSObject


+(BOOL)validate:(id)value rule:(NSString*)rule error:(NSString**)error;

@end
