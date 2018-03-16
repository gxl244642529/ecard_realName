//
//  DMExceptionHandler.h
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>


@protocol DMExceptionHandler <NSObject>

-(void)handleException:(NSException*)exception;

@end

@interface DMExceptionHandler : NSObject

+(void)handleException:(NSException*)exception;
+(void)setExceptionHandler:(id<DMExceptionHandler>)handler;

@end
