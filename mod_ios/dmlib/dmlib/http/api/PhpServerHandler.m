//
//  PhpServerHandler.m
//  DMLib
//
//  Created by randy ren on 16/1/14.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "PhpServerHandler.h"
#import "DMAccount.h"
#import "DMJobManager.h"
#import "DataUtil.h"

@implementation PhpServerHandler



-(NSData*)executeJob:(DMApiJob*)job{
    
    [job.param setValue:[[DMJobManager sharedInstance]deviceID] forKey:@"deviceID"];
    
    
    NSMutableData* data = [[NSMutableData alloc]init];
    NSError* error=nil;
    [data appendData:[@"content=" dataUsingEncoding:NSUTF8StringEncoding]];
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:job.param
                        
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:&error];
    if(error){
        @throw [[DMException alloc]initWithReason:@"上传参数包含不能解析成json的数据" code:DMErrorType_InvalidData];
    }
#ifdef DEBUG
    NSLog(@"Start Post %@ %@",  [self getUrlFromApi:job.api], [DataUtil getString:jsonData]);
#endif
    [data appendData:jsonData];
    
    NSData* resultData = [_network execute:job.timeout url:[self getUrlFromApi:job.api] extraHeaders:nil data:data];
    if(_network.error){
        @throw [[DMException alloc]initWithReason:@"网络错误" code:DMErrorType_Network];
    }
    if([_currentJob isCanceled]){
        return nil;
    }

#ifdef DEBUG
    NSLog(@"Recv Data : %@",[DataUtil getString:resultData]);
#endif
     return resultData;
}


-(void)cancelProgress{
    [_network cancel];
}


-(BOOL)doLogin:(DMApiJob *)task{
    
    DMAccount* userInfo = [DMAccount current];
   
    DMJobManager* manager = [DMJobManager sharedInstance];
    
    
    
    NSMutableDictionary* param = [[NSMutableDictionary alloc]init];
    param[@"deviceID"]=manager.deviceID;
    param[@"pushID"]= manager.pushID;
    param[@"platform"]=@"iphone";
    param[@"version"]= APP_VERSION;
    param[@"username"] =userInfo.userAccount;
    param[@"pwd"] = userInfo.userPwd;
   
    
    NSError* error = nil;
    NSDictionary* dic = [self post:[self getUrlFromApi:@"pass_api/login"] data:param error:&error timeout:task.timeout];
    if(error){
        return FALSE;
    }
    int flag = [dic[@"flag"]intValue];
    if(flag==0){
        
    }else{
        //登录失败
        //这里直接提示登录失败
        [DMAccount onLoginFail:self];
        
        
        
        
    }
    return TRUE;

    
}





-(NSData*)rawPost:(NSString*)url data:(NSMutableDictionary*)param error:(NSError**)error timeout:(NSInteger)timeout{
    
    //这里需要加上deviceId
    [param setValue:[[DMJobManager sharedInstance]deviceID] forKey:@"deviceID"];
    
    
    NSMutableData* data = [[NSMutableData alloc]init];
    [data appendData:[@"content=" dataUsingEncoding:NSUTF8StringEncoding]];
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:param
                                                       options:NSJSONWritingPrettyPrinted
                                                         error:error];
    if(*error){
        return nil;
    }
    [data appendData:jsonData];
#ifdef DEBUG
    NSLog(@"Start : %@ %@",url,param);
#endif
    
    NSMutableURLRequest* request = [[NSMutableURLRequest alloc]initWithURL:[NSURL URLWithString:url]];
    [request setHTTPMethod:@"POST"];
    [request setHTTPBody:data];
    NSData* resultData = [_network execute:timeout url:url extraHeaders:nil data:data];
    if(*error){
        //网络错误
#ifdef DEBUG
        NSLog(@"Network error: %@",*error);
#endif
        
        return nil;
    }

#ifdef DEBUG
    NSLog(@"Recv data: %@",[DataUtil getString:resultData]);
#endif
    
    return  resultData;
    
}

-(NSInteger)startPosition{
    return 1;
}

-(NSString*)getUrlFromApi:(NSString*)api{
    return [NSString stringWithFormat:@"%@/index.php/api2/%@",self.baseUrl,api];
}
-(void)postError:(id)task{
    //错误
    [_delegate jobError:task];
}



-(NSDictionary*)post:(NSString*)url data:(NSMutableDictionary*)param error:(NSError**)error  timeout:(NSInteger)timeout{
    
    NSData* data = [self rawPost:url data:param error:error timeout:timeout];
    if(*error){
        return nil;
    }
    if(data==nil){
        *error = [NSError errorWithDomain:@"数据为空" code:1 userInfo:nil];
        return nil;
    }
    NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:error];
    return resultMap;
}

/*
-(BOOL)postAgain:(DMApiJob*)task lastError:(NSInteger)lastError {
    NSError* error = nil;
    NSDictionary* resultMap = [self post:task.url data:task.param error:&error];
    if(error){
        task.error = error;
        [self postError:task];
        return NO;
    }
    //继续解析
    int flag = [resultMap[@"flag"]intValue];
    if(flag == lastError){
        //和上次错误一样,则不应该继续执行
        if(lastError == ErrCode_NotLogin){
            task.error = [NSError errorWithDomain:@"登录失败" code:1 userInfo:nil];
        }else{
            task.error = [NSError errorWithDomain:@"获取token失败" code:1 userInfo:nil];
        }
        [self postError:task];
        return NO;
    }
    
    //是否成功
   return [self parseServerData:flag data:resultMap task:task];
    
    
}*/

@end
