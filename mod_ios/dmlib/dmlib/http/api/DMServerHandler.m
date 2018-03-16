 //
//  DMServerHandler.m
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMServerHandler.h"


//{"flag":10}
//{"flag":10,"result":""}
//

@implementation DMServerHandler

-(id)init{
    if(self = [super init]){
        _network = [[DMApiNetwork alloc]init];
    }
    return self;
}

//解析任务
-(void)handleAndParse:(DMApiJob*)job lastFlag:(int)lastFlag{
    @try{
        NSData* data = [self executeJob:job];
        if([job isCanceled]){
            return;
        }
        [self parse:data job:job lastFlag:lastFlag];
    }@catch(DMException* exception){
        job.error = exception;
        [_delegate jobError:job];
    }@catch(NSException* exception){
        job.error = [[DMException alloc]initWithReason:@"未知错误" code:DMErrorType_BusinessProcess];
        [_delegate jobError:job];

    }
}

-(NSData*)executeJob:(DMApiJob*)job{
    return nil;
}

-(void)parse:(NSData*)data job:(DMApiJob*)task lastFlag:(int)lastFlag{
    if(data==nil){
        task.error = [[DMException alloc]initWithReason:@"数据为空" code:DMErrorType_InvalidData];
        [_delegate jobError:task];
        return;
    }
#ifdef DEBUG
    NSLog(@"%@", [[NSString alloc]initWithData:data encoding:NSUTF8StringEncoding ]);
#endif
    NSError* error = nil;
    NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:&error];
    if(error){
        task.error = [[DMException alloc]initWithReason:@"数据解析失败" code:DMErrorType_InvalidData];
        [_delegate jobError:task];
        return;
    }
    int flag = [resultMap[@"flag"]intValue];
    if(flag==0){
        
        
        id result = [_apiParsers[task.dataType] parse:resultMap class:task.clazz];
        task.data = result;
        [_delegate jobSuccess:task];
        
        if(task.cachePolicy != DMCachePolicy_NoCache){
            [_cache put:task.cacheKey value:data];
        }

        
    }else if(flag < 0){
        task.serverMessage = resultMap[@"result"];
        task.serverMessageType = (flag == -2 ? MessageType_Toast : MessageType_Alert);
        [_delegate jobMessage:task];
        
     }else{
        if(flag == ErrCode_NotLogin){
            
            if(lastFlag == flag){
                task.error = [[DMException alloc]initWithReason:@"登录失败" code:DMErrorType_BusinessProcess];
                [_delegate jobError:task];
                return;
            }
            
            if([self doLogin:task]){
                [self handleAndParse:task lastFlag:flag];
            }
        }else if(flag == ErrCode_RequireAccessToken){
            if(lastFlag == flag){
                task.error = [[DMException alloc]initWithReason:@"获取tocken失败" code:DMErrorType_BusinessProcess];
                [_delegate jobError:task];
                return;
            }
            if([self doGetAccessToken:task]){
                [self handleAndParse:task lastFlag:flag];
            }
        }else{
            //这里输出错误
           
            NSString* error = nil;
            switch (flag) {
                case 1:
                case 2:
                case 3:
                    error = @"服务器繁忙,请稍候重试";
                    break;
                case 4:
                    error= @"请检查客户端参数";
                    break;
                default:
                   error = [NSString  stringWithFormat:@"其他错误%d",flag];
                    break;
            }
           task.error= [[DMException alloc]initWithReason:error code:flag];
            [_delegate jobError:task];
        }
        
    }
   
}


-(BOOL)doLogin:(DMApiJob*)task{
    return FALSE;
}


-(BOOL)doGetAccessToken:(DMApiJob*)task{
    return FALSE;
}

-(NSString*)getUrl:(DMApiJob*)job{
    return nil;
}
-(DMPopType)onLoginSuccess{
    
    //这里需要再次执行
    [_paddingJob execute];
    _paddingJob = nil;
    
    return DMPopBySelf;
}

-(void)onLoginCancel{
    _paddingJob = nil;
}

-(void)handleTaskImpl:(id)task{
    [self handleAndParse:task lastFlag:0];
}

-(NSData*)encodeRequest:(DMApiJob*)job{
    return nil;
}

-(NSInteger)startPosition{
    return 0;
}
-(void)clearSession{
    
}
-(void)initParam:(NSArray<id<DMApiParser>>*)apiParsers delegate:(id<DMApiDelegate>)delegate cache:(id<DMCache>)cache{
    _delegate = delegate;
    _apiParsers = apiParsers;
    _cache = cache;
}
@end
