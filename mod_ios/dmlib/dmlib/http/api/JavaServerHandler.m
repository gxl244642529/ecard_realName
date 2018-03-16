//
//  JavaServerHandler.m
//  DMLib
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "JavaServerHandler.h"
#import "AesData.h"
#import "SignUtil.h"
#import "DataUtil.h"
#import "SignUtil.h"
#import "DataUtil.h"
#import "DMApiParser.h"
#import "DMJobManager.h"
#import "DMAccount.h"
#import "CommonUtil.h"
#import "PushUtil.h"

@interface JavaServerHandler()
{
    NSString* _token;
    NSString* _key;
    NSData* _pinKey;
    NSData* _uploadKey;
    AesData* _aes;
   
    NSInteger _time;
}

@end

@implementation JavaServerHandler

-(id)init{
    if(self = [super init]){
        _aes = [[AesData alloc]init];
    }
    return self;
}

-(void)cancelProgress{
    [_network cancel];
}

-(void)dealloc{
     _aes = nil;
}

-(void)clearSession{
    _token = nil;
    _key = nil;
    _pinKey = nil;
    _uploadKey = nil;
}


-(NSMutableDictionary*)getInitObject:(NSMutableDictionary*)dic{
    dic = [[NSMutableDictionary alloc]initWithDictionary:dic];
    [dic setValue:[DMJobManager sharedInstance].deviceID forKey:@"deviceID"];
    NSInteger time = (NSInteger)[[NSDate date]timeIntervalSince1970];
    [dic setValue:[NSNumber numberWithInteger:time] forKey:@"time"];
    NSString* rand = [NSString stringWithFormat:@"%ld",random()%time];
    [dic setValue:[CommonUtil md5:rand] forKey:@"rand_str"];
    _time = 16 - time%16;
    
    return dic;
}


-(NSString*)getUrl:(NSString*)api{
    
    return [NSString stringWithFormat:@"%@%@",self.baseUrl,api];
    
}

-(NSData*)executeJob:(DMApiJob*)job{
    
    
    return [self rawPost:[self getUrl:job.api] data:job.param crypt:job.crypt timeout:job.timeout];
}

-(NSMutableData*)handleFiles:(NSData*)src files:(NSDictionary*)files headers:(NSMutableDictionary*)headers{
    NSMutableString* sb = [[NSMutableString alloc]initWithCapacity:30];
    NSMutableData* result = [[NSMutableData alloc]initWithData:src];
    BOOL first = TRUE;
    
    NSInteger fileLength = 0;
    
    for (NSString* key in files.allKeys) {
        NSData* data = files[key];
        fileLength += data.length;
        
        if(first){
            first = false;
        }else{
            [sb appendString:@","];
        }
        
        [result appendData:data];
        
        [sb appendString:key];
        [sb appendString:@":"];
        [sb appendFormat:@"%d",(int)data.length];
    }
    
    
    [headers setValue:sb forKey:@"attach"];
    [headers setValue:[NSString stringWithFormat:@"%ld",(long)fileLength] forKey:@"fileLength"];
    
    return result;

}

-(NSData*)rawPost:(NSString*)url data:(NSMutableDictionary*)param crypt:(CryptType)crypt timeout:(NSInteger)timeout{
    
    param = [self getInitObject:param];
    [param removeObjectForKey:@"sign"];
    NSMutableDictionary* files = [[NSMutableDictionary alloc]init];
    if(_key){
        [param setValue:[SignUtil sign:param key:_key files:files] forKey:@"sign"];
    }else{
        [param setValue:[SignUtil sign:param key:[[DMJobManager sharedInstance]deviceID] files:files] forKey:@"sign"];
    }
    NSError* error = nil;
    NSData* data = [NSJSONSerialization dataWithJSONObject:param
                                                   options:0
                                                     error:&error];
#ifdef DEBUG
    NSLog(@"Send %@ %@",url ,param);
    
#endif
    if(error){
        @throw [[DMException alloc]initWithReason:@"上传参数包含不能解析成json的数据" code:DMErrorType_InvalidData];
    }
    if(crypt & CryptType_Upload){
        data = [_aes encrpyt:data key:_uploadKey];
    }
    
    NSMutableDictionary* headers = [[NSMutableDictionary alloc]init];
    if(files.count>0){
        data = [self handleFiles:data files:files headers:headers];
    }
    
    if(_token){
         [headers setValue:_token forKey:@"token"];
    }
  
    
    NSData* resultData = [_network execute:timeout url:url extraHeaders:headers data:data];
    if(_network.error){
        @throw [[DMException alloc]initWithReason:@"网络错误" code:DMErrorType_Network];
    }
    if([_currentJob isCanceled]){
        return nil;
    }
    
    if(crypt & CryptType_Download){
        resultData = [self decriptData:resultData];
    }
#ifdef DEBUG
    NSLog(@"%@",[DataUtil getString:resultData]);
#endif
    //这里得到了json
    return resultData;
}

-(NSData*)decriptData:(NSData*)resultData{
    @try{
        Byte first;
        [resultData getBytes:&first length:1];
        if(first!='{'){
            NSData* key;
            if(_pinKey){
                Byte byte[16];
                [_pinKey getBytes:byte range:NSMakeRange(_time, 16)];
                key = [NSData dataWithBytes:byte length:16];
            }else{
                NSString* deviceID= [DMJobManager sharedInstance].deviceID;
                key = [[deviceID substringWithRange:NSMakeRange(_time, 16)]dataUsingEncoding:NSUTF8StringEncoding];
            }
            resultData = [_aes decrpyt:resultData key:key];
        }
        
        return resultData;
    }@catch(NSException* e){
        @throw [[DMException alloc]initWithReason:@"数据解密失败" code:DMErrorType_BusinessProcess];
    }

}


-(BOOL)doLogin:(DMApiJob *)task{
    
    DMAccount* userInfo = [DMAccount current];
    NSMutableDictionary* param = [[NSMutableDictionary alloc]init];
    param[@"account"]=userInfo.userAccount;
    param[@"pwd"]=userInfo.userPwd;
    param[@"deviceID"]=[DMJobManager sharedInstance].deviceID;
    param[@"platform"]=@"iphone";
    param[@"version"]=APP_VERSION;
    
    param[@"pushId"] = [PushUtil getPushId];
   
    
    
    NSData* data = [self rawPost:[self getUrl:@"gate/login"] data:param crypt:CryptType_Both timeout:5];
    NSError* error = nil;
    NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:&error];
    if(error){
        task.error = [[DMException alloc]initWithReason:@"数据解析失败" code:DMErrorType_InvalidData];
        [_delegate jobError:task];
        return FALSE;
    }
    
    int flag = [resultMap[@"flag"]intValue];
    if(flag==0){
        return TRUE;
    }
    if(flag==ErrCode_NotLogin){
        task.error = [[DMException alloc]initWithReason:@"循环登录" code:DMErrorType_BusinessProcess];
        [_delegate jobError:task];
        return FALSE;
    }
    [DMAccount onLoginFail:self];
    return FALSE;
    
}

-(BOOL)doGetAccessToken:(DMApiJob *)task{
    
    
    _key = nil;
    _pinKey = nil;
    _token = nil;

    NSMutableDictionary* param = [[NSMutableDictionary alloc]init];
    param = [self getInitObject:param];
    NSData* data = [self rawPost:[self getUrl:@"gate/token"] data:param crypt:CryptType_Download timeout:5];
    NSError* error = nil;
    NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableLeaves error:&error];
    if(error){
        task.error = [[DMException alloc]initWithReason:@"数据解析失败" code:DMErrorType_InvalidData];
        [_delegate jobError:task];
        return FALSE;
    }
    
    int flag = [resultMap[@"flag"]intValue];
    if(flag==0){
        resultMap = resultMap[@"result"];
        //获取成功
        _token  = [resultMap valueForKey:@"token"];
        _key = [resultMap valueForKey:@"key"];
        _pinKey =  [_key dataUsingEncoding:NSUTF8StringEncoding];
        _uploadKey = [[_key substringWithRange:NSMakeRange(16-_time, 16)] dataUsingEncoding:NSUTF8StringEncoding];
        return TRUE;
    }else{
        //获取accessToken失败
        task.error = [[DMException alloc]initWithReason:@"获取accesstoken失败" code:DMErrorType_BusinessProcess];
        [_delegate jobError:task];
        return FALSE;
    }
    return FALSE;
}


@end
