//
//  RCTApiModule.m
//  MyProject
//
//  Created by 任雪亮 on 16/7/16.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTApiModule.h"

const NSInteger ERROR_NETWORK = 0;
const NSInteger ERROR_SERVER = 1;
const NSInteger ERROR_ALERT = 2;
const NSInteger ERROR_TOAST = 3;

@interface ApiInfo : NSObject

@property (nonatomic,weak) DMJob* job;
@property (nonatomic,readwrite,copy) RCTResponseSenderBlock successCallback;
@property (nonatomic,readwrite,copy) RCTResponseSenderBlock errorCallback;

@end

@implementation ApiInfo

-(void)dealloc{
  _successCallback = nil;
  _errorCallback = nil;
  _job = nil;
}



@end
static char kRCTApiModuleAssociatedObjectKey;

@interface RCTApiModule()
{

}

@end



@implementation RCTApiModule
RCT_EXPORT_MODULE();

-(id)init{
  if(self = [super init]){
  }
  return self;
}

// DMPay
RCT_EXPORT_METHOD(cancel:(NSString*)api){
 /* ApiInfo* info = [taskMap objectForKey:api];
  if(info && info.job){
    [info.job cancel];
  }*/
  
  
  
}

-(void)dealloc{

}


RCT_EXPORT_METHOD(api:(NSDictionary*)data
                  successCallback:(RCTResponseSenderBlock)successCallback
                  errorCallback:(RCTResponseSenderBlock)errorCallback
                  )
{
  
  NSString* api = data[@"api"];
  
 
  
  
  
  NSDictionary* files = [data objectForKey:@"files"];
  NSDictionary* postData = [data objectForKey:@"data"];
  
/*  ApiInfo* info = [taskMap objectForKey:api];
  if(info && info.job){
    [info.job cancel];
  }*/
  
  
  DMApiJob* job=nil;
  NSInteger type = [[data objectForKey:@"type"]integerValue];
  if(type==0){
    job=[[DMJobManager sharedInstance]createJsonTask:api cachePolicy:(DMCachePolicy)[data[@"cachePolicy"]integerValue] server:0];
  }else{
    job =  [[DMJobManager sharedInstance]createPageJsonTask:api cachePolicy:(DMCachePolicy)[data[@"cachePolicy"]integerValue] server:0];

  }
  
  
  NSMutableDictionary* dic = [[NSMutableDictionary alloc]initWithDictionary:postData];
  if(files){
    for (NSString* key in files.allKeys) {
      id value = files[key];
      if([value isKindOfClass:[NSArray class]]){
        NSMutableArray* result = [[NSMutableArray alloc]init ];
        for (NSString* path in value) {
          NSData* content = [NSData dataWithContentsOfFile:path];
          [result addObject:content];
        }
        [dic setValue:result forKey:key];
      }else{
        NSString* path = files[key];
        NSData* content = [NSData dataWithContentsOfFile:path];
        [dic setValue:content forKey:key];
      }
    }
  }
  
  NSString* waitingMessage = [data objectForKey:@"waitingMessage"];
  job.server = 1;
  job.waitingMessage = waitingMessage;
  job.crypt = (CryptType)[data[@"crypt"]integerValue];
  

  ApiInfo* info = [[ApiInfo alloc]init];
  info.successCallback =successCallback;
  info.errorCallback = errorCallback;
  info.job = job;
  objc_setAssociatedObject(job, &kRCTApiModuleAssociatedObjectKey, info, OBJC_ASSOCIATION_RETAIN);
  
  id timeoutMs = [data objectForKey:@"timeoutMs"];
  if(timeoutMs){
    [job setTimeout: [timeoutMs integerValue] / 1000];
  }
//  job setTimeout:<#(NSInteger)#>
  
  job.delegate = self;
  [job putAll:dic];
  [job execute];
}


-(BOOL)jobMessage:(DMApiJob*)request{
  ApiInfo* info = objc_getAssociatedObject(request, &kRCTApiModuleAssociatedObjectKey);

  if(info.errorCallback!=nil){
    
    NSInteger errorType = (request.serverMessageType == MessageType_Alert) ? ERROR_ALERT : ERROR_TOAST;
    
    info.errorCallback(@[ request.serverMessage, [NSNumber numberWithInteger:errorType] ]);
    return TRUE;
  }
 
  return FALSE;
}

-(BOOL)jobError:(DMApiJob*)request{
  ApiInfo* info = objc_getAssociatedObject(request, &kRCTApiModuleAssociatedObjectKey);
  if(info.errorCallback!=nil)
  {
    DMException* error= request.error;
    NSInteger errorType = error.isNetworkError ? ERROR_NETWORK : ERROR_SERVER;
    info.errorCallback(@[error.reason,[NSNumber numberWithInteger:errorType]]);
    return TRUE;
  }
 
  return FALSE;
}

-(void)jobSuccess:(DMApiJob*)request{
  ApiInfo* info = objc_getAssociatedObject(request, &kRCTApiModuleAssociatedObjectKey);
  if(info.successCallback != nil){
    if( [request.data isKindOfClass:[DMPage class]]){
      info.successCallback(@[[ ((DMPage*)request.data) toJson ] ]);

    }else{
      if(request.data==nil){
        info.successCallback(@[]);
      }else{
        info.successCallback(@[request.data]);
      }
      
    }
    
  }
}
RCT_EXPORT_METHOD(logout){
  [[DMJobManager sharedInstance]logout];
  
}
RCT_EXPORT_METHOD(finish){
  
  dispatch_async(dispatch_get_main_queue(), ^{
    
    
    [[DMJobManager sharedInstance].topController.navigationController popViewControllerAnimated:YES];
    
  });
  
}

@end
