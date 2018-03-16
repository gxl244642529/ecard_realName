//
//  RCTBusQrModule.m
//  ecard
//
//  Created by 任雪亮 on 17/3/31.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTBusQrModule.h"

#import "qr_code_sdk.h"
#import <ecardlib/ecardlib.h>

__weak RCTBusQrModule* __RCTBusQrModule;


@interface RCTBusQrModule (){
  QRCode_SDK_API* _api;
}

@end

@implementation RCTBusQrModule


+(void)initialize{
  
}

+(void)disableToken{
  if(__RCTBusQrModule){
    [__RCTBusQrModule disableToken];
  }else{
    QRCode_SDK_API* _api=[[QRCode_SDK_API alloc] init];
    [_api disableToken];
  }
}


-(id)init{
  if(self = [super init]){
    __RCTBusQrModule = self;
    _api=[[QRCode_SDK_API alloc] init];
    
    NSString* server = [DMConfigReader getString:@"servers" subkey:@"tsp"];
    NSString* port = [DMConfigReader getString:@"servers" subkey:@"tsp_port"];
    
    [_api setHost:server setPort:port];
  }
  return self;
}


RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(getQr:(NSString*)accountId  callback:(RCTResponseSenderBlock)callback){
  dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
    @synchronized (self) {
      
      @try {
        NSDictionary *token=  [_api getQRCode:accountId];
        NSLog(@"\n==================tsp获取token=%@==================\n",token);
        callback(@[token]);
      } @catch (NSException *exception) {
        callback(@[@{}]);
      }@catch(NSError* error){
        callback(@[@{}]);
      }
      @finally {
        
      }
    }
 });
}

RCT_EXPORT_METHOD(disableToken){
   [_api disableToken];
}

RCT_EXPORT_METHOD(  activate:(NSString*)accountId  callback:(RCTResponseSenderBlock)callback){
  dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
    @synchronized (self) {
      int  ret = [_api activeDevice:accountId];
      NSLog(@"\n==================tsp开户结果%d=======================\n",ret);
      callback(@[[NSNumber numberWithInt:ret]]);
    }
  });
}

RCT_EXPORT_METHOD(inactiveDevice  ){
  [_api inactiveDevide];
}

@end
