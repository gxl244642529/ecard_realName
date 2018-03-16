//
//  DMServerHandler.h
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMAbsHandler.h"
#import "DMApiNetwork.h"
#import "DMCache.h"
#import "DMLoginController.h"
#import "DMApiJob.h"
#import "DMApiParser.h"
#import "DMApiDelegate.h"


typedef enum : NSUInteger {
    // 服务器错误
    ErrCode_Service = 1,
    // 服务器端数据库操作失败
    ErrCode_Database,
    // 用户没有操作这个接口的权限
    ErrCode_NoRight ,
    // 客户端提交的参数错误
    ErrCode_Param ,
    // 客户端提交的参数错误
    ErrCode_Json_Format,
    // 客户端提交的参数错误
    ErrCode_Null_Value,
    // 用户没有登录，但是本接口要求用户登录
    ErrCode_NotLogin,
    // 客户端提交的数据版本与服务器端的数据版本一致
    ErrCode_SameVersion,
    // 签名失败，需要下载客户端密钥
    ErrCode_RequireAccessToken,
    // 签名失败
    ErrCode_SignError,
    // 错误地址
    ErrCode_UrlError,
    //商户id错误
    ErrCode_BusinessID,
    //商户状态错误
    ErrCode_BusinessStatus,
    //请求时间和服务器时间相差太多(5分钟)
    ErrCode_RequestTime
    
} ApiErrorCode;

@interface DMServerHandler : DMAbsHandler<DMLoginDelegate>
{
    //正在执行的task
    __weak DMApiJob* _paddingJob;
    __weak NSArray<id<DMApiParser>>* _apiParsers;
    DMApiNetwork* _network;
    __weak id<DMApiDelegate> _delegate;
    __weak id<DMCache> _cache;
}
-(void)parse:(NSData*)data job:(DMApiJob*)task lastFlag:(int)lastFlag;
//解析任务
-(void)handleAndParse:(DMApiJob*)job lastFlag:(int)lastFlag;
-(NSData*)encodeRequest:(DMApiJob*)job;
-(NSInteger)startPosition;

-(void)initParam:(NSArray<id<DMApiParser>>*)apiParsers delegate:(id<DMApiDelegate>)delegate cache:(id<DMCache>)cache;
-(void)clearSession;


-(BOOL)doLogin:(DMApiJob*)task;


-(BOOL)doGetAccessToken:(DMApiJob*)task;

@property (nonatomic,retain) NSString* baseUrl;

@end
