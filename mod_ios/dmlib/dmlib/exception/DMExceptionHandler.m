//
//  DMExceptionHandler.m
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMExceptionHandler.h"

__weak id<DMExceptionHandler> _handler;

@implementation DMExceptionHandler
+(void)handleException:(NSException*)exception{
    [_handler handleException:exception];
}
+(void)setExceptionHandler:(id<DMExceptionHandler>)handler{
    _handler = handler;
}
@end
