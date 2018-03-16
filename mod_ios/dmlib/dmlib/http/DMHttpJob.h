//
//  HttpRequest.h
//  core
//
//  Created by randy ren on 15/12/28.
//  Copyright © 2015年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMJobDelegate.h"
#import "DMJob.h"
#import "DMCachePolicy.h"



typedef enum {
    GET,
    POST
} Method;


/**
 * 网络任务
 */
@interface DMHttpJob : DMJob
{
     NSString* _cacheKey;
}

-(NSString*)cacheKey;
-(void)clear;





@property (nonatomic,retain) NSDictionary* httpHeader;

@property (nonatomic,assign) Method method;


@property (nonatomic,assign) NSInteger dataType;

@property (nonatomic,retain) NSString* url;

//反馈
@property (nonatomic,weak) id delegate;


@property (nonatomic,assign) BOOL isRunning;

//超时时间（秒）
@property (nonatomic,assign) NSInteger timeout;

/**
 * 服务器
 */

@property (nonatomic,assign) NSInteger server;
//附带的信息
@property (nonatomic,weak) id extra;


//最终的结果
@property (nonatomic,retain) id data;

@property (nonatomic,retain) DMException* error;


//缓存策略
@property (nonatomic,assign) DMCachePolicy cachePolicy;



#pragma protected
-(NSString*)makeCacheKey;

@end
