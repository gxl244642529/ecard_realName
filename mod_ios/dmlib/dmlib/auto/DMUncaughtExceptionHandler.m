//
//  UncaughtExceptionHandler.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMUncaughtExceptionHandler.h"

#import "DMUncaughtExceptionHandler.h"
#include <libkern/OSAtomic.h>
#include <execinfo.h>
#import <UIKit/UIKit.h>
#import "ReflectUtil.h"

//http://www.cocoachina.com/newbie/tutorial/2012/0829/4672.html
NSString * const UncaughtExceptionHandlerSignalExceptionName = @"UncaughtExceptionHandlerSignalExceptionName";
NSString * const UncaughtExceptionHandlerSignalKey = @"UncaughtExceptionHandlerSignalKey";
NSString * const UncaughtExceptionHandlerAddressesKey = @"UncaughtExceptionHandlerAddressesKey";

volatile int32_t UncaughtExceptionCount = 0;
const int32_t UncaughtExceptionMaximum = 10;

const NSInteger UncaughtExceptionHandlerSkipAddressCount = 4;
const NSInteger UncaughtExceptionHandlerReportAddressCount = 5;

@implementation DMUncaughtExceptionHandler

+ (NSArray*)backtrace
{
    void* callstack[128];
    int frames = backtrace(callstack, 128);
    char **strs = backtrace_symbols(callstack, frames);
    NSMutableArray* arr = [[NSMutableArray alloc]initWithCapacity:frames];
    for (int i = 0; i < frames;++i)
    {
        [arr addObject:[NSString stringWithUTF8String:strs[i]]];
        // [str appendString:[NSString stringWithUTF8String:strs[i]]];
        // [str appendString:@"\n"];
    }
    free(strs);
    return arr;
}

@end

void UninstallUncaughtExceptionHandler(void){
    NSSetUncaughtExceptionHandler(NULL);
    signal(SIGABRT, SIG_DFL);
    signal(SIGILL, SIG_DFL);
    signal(SIGSEGV, SIG_DFL);
    signal(SIGFPE, SIG_DFL);
    signal(SIGBUS, SIG_DFL);
    signal(SIGPIPE, SIG_DFL);
    
}

void HandleException(NSException *exception)
{
    NSArray* arr = [exception callStackSymbols];
    NSLog(@"%@",arr);
    UninstallUncaughtExceptionHandler();
    [exception raise];
}

void SignalHandler(int signal)
{
    UninstallUncaughtExceptionHandler();
    //如果是
    kill(getpid(), signal);
}

void InstallUncaughtExceptionHandler(void)
{
    NSSetUncaughtExceptionHandler(&HandleException);
    signal(SIGABRT, SignalHandler);
    signal(SIGILL, SignalHandler);
    signal(SIGSEGV, SignalHandler);
    signal(SIGFPE, SignalHandler);
    signal(SIGBUS, SignalHandler);
    signal(SIGPIPE, SignalHandler);
}
