//
//  EBusinessServerHandler.m
//  ebusiness
//
//  Created by 任雪亮 on 16/10/25.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "EBusinessServerHandler.h"


#import <ecardlib/ecardlib.h>

__weak EBusinessServerHandler* _ebusinessServerHandlerInstance;

@interface EBusinessServerHandler()
{
  NSString* _token;
  NSString* _key;
  NSData* _pinKey;
  NSData* _uploadKey;
  AesData* _aes;
  
  NSInteger _time;
}

@end

@implementation EBusinessServerHandler

+(EBusinessServerHandler*)runningInstance{
  return _ebusinessServerHandlerInstance;
}
-(NSString*)token{
    return _token;
}
-(id)init{
  if(self = [super init]){
    _aes = [[AesData alloc]init];
    _ebusinessServerHandlerInstance = self;
    
    //保存一下
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];

    NSString* token  = [def valueForKey:@"token_value"];
   /* if(token){
      _token = token;
      _key = [def valueForKey:@"key_value"];
      _pinKey =  [_key dataUsingEncoding:NSUTF8StringEncoding];
      _uploadKey = [[def valueForKey:@"pin_key_value"] dataUsingEncoding:NSUTF8StringEncoding];

    }
    */
    
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

  [DMAccount onLoginFail:self];
  return FALSE;
  
}

-(void)setToken:(NSDictionary*)resultMap{
  _token  = [resultMap valueForKey:@"token"];
  _key = [resultMap valueForKey:@"key"];
  _pinKey =  [_key dataUsingEncoding:NSUTF8StringEncoding];
  _uploadKey =  [[resultMap valueForKey:@"pinKey"]dataUsingEncoding:NSUTF8StringEncoding];  // [[_key substringWithRange:NSMakeRange(16-_time, 16)] dataUsingEncoding:NSUTF8StringEncoding];
  
  //保存一下
  NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
  [def setValue:_token forKey:@"token_value"];
  [def setValue:_key forKey:@"key_value"];
  [def setValue:_uploadKey forKey:@"pin_key_value"];
    [def synchronize];
  

}
-(BOOL)doGetAccessToken:(DMApiJob *)task{
  
  
  _key = nil;
  _pinKey = nil;
  _token = nil;
  
  
  if(![DMAccount isLogin] || ![DMAccount current].userID){
    [DMAccount onLoginFail:self];

    return FALSE;
  }
  
  NSMutableDictionary* param = [[NSMutableDictionary alloc]init];
  ECardUserInfo* account = [DMAccount current];
  [param setValue:account.userHash forKey:@"hash"];
  NSData* data = [self rawPost:[self getUrl:@"passport/token"] data:param crypt:0 timeout:5];
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
    [self setToken:resultMap];
        return TRUE;
  }else if(flag==7){
    [DMAccount onLoginFail:self];

  }else{
    //获取accessToken失败
    task.error = [[DMException alloc]initWithReason:@"获取accesstoken失败" code:DMErrorType_BusinessProcess];
    [_delegate jobError:task];
    return FALSE;

  }
  return FALSE;
}


@end
